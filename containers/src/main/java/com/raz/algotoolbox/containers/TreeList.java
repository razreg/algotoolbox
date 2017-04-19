package com.raz.algotoolbox.containers;

import com.raz.algotoolbox.datatypes.trees.RankedRedBlackTree;
import com.raz.algotoolbox.internal.NotNull;

import java.util.*;

public class TreeList<V> extends RankedRedBlackTree<Integer, V> implements List<V> {

	@Override
	public boolean contains(Object o) {
		@SuppressWarnings("unchecked")
		V value = (V) o;
		return containsValue(getRoot(), value);
	}

	@Override
	public Iterator<V> iterator() {
		return new TreeListIterator();
	}

	@Override
	public Object[] toArray() {
		return fillArray(new Object[size()]);
	}

	@Override
	public <T> T[] toArray(T[] a) {
		T[] ts = a;
		try {
			if (ts == null || ts.length < size()) {
				Object obj = java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size());
				ts = (T[]) a.getClass().cast(obj);
			}
			fillArray(ts);
		} catch (ClassCastException e) {
			throw new ArrayStoreException(a.getClass() + " is not compatible with the type of this list's elements.");
		}
		return ts;
	}

	@Override
	public boolean add(V v) {
		insert(size(), v);
		return true;
	}

	@Override
	public boolean remove(Object o) {
		@SuppressWarnings("unchecked")
		V value = (V) o;
		return removeValue(getRoot(), value);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		for(Object obj : c) {
			if (!contains(obj)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean addAll(Collection<? extends V> c) {
		if (c == null || c.isEmpty()) {
			return false;
		}
		c.forEach(this::add);
		return true;
	}

	@Override
	public boolean addAll(int index, Collection<? extends V> c) {
		if (c == null || c.isEmpty()) {
			return false;
		}
		validateIndexBounds(index);
		shift(index, c.size());
		for (V elem : c) {
			insert(index, elem);
		}
		return true;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		boolean listChanged = false;
		if (c != null) {
			for (Object o : c) {
				listChanged = listChanged || remove(o);
			}
		}
		return listChanged;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		if (c == null || c.isEmpty()) {
			return false;
		}
		boolean listChanged = false;
		for (Node node = getMin(); node != null; node = successor(node)) {
			if (c.contains(node.getValue())) {
				delete(node.getKey());
				listChanged = true;
			}
		}
		return listChanged;
	}

	@Override
	public V get(int index) {
		validateIndexBounds(index);
		return retrieve(index);
	}

	@Override
	public V set(int index, V element) {
		validateIndexBounds(index);
		Node curr = treeLocation(index);
		V oldElement = curr.getValue();
		curr.setValue(element);
		return oldElement;
	}

	@Override
	public void add(int index, V element) {
		validateIndexBounds(index);
		shift(index, 1);
		insert(index, element);
	}

	@Override
	public V remove(int index) {
		return delete(index);
	}

	@Override
	public int indexOf(Object o) {
		@SuppressWarnings("unchecked")
		V value = (V) o;
		int index = 0;
		Node node = getMin();
		while (node != null && !bothNullOrJustEqual(node.getValue(), value)) {
			node = successor(node);
			++index;
		}
		return node == null ? -1 : index;
	}

	@Override
	public int lastIndexOf(Object o) {
		@SuppressWarnings("unchecked")
		V value = (V) o;
		int lastIndex = -1;
		Node node = getMin();
		for (int index = 0; node != null; node = successor(node), ++index) {
			if (bothNullOrJustEqual(node.getValue(), value)) {
				lastIndex = index;
			}
		}
		return lastIndex;
	}

	@Override
	public ListIterator<V> listIterator() {
		return listIterator(0);
	}

	@Override
	public ListIterator<V> listIterator(int index) {
		return new TreeListListIterator(index);
	}

	@Override
	public List<V> subList(int fromIndex, int toIndex) {
		if (fromIndex < 0 || toIndex > size() || fromIndex > toIndex) {
			String from = fromIndex < 0 ? "fromIndex must be non-negative, but was " + fromIndex + ". " : "";
			String to = toIndex > size() ? "toIndex must be size() or lower, but was " + toIndex + ". " : "";
			String fromTo = fromIndex > toIndex ? "fromIndex must be smaller than toIndex, but they were [" + fromIndex + ", " + toIndex + ")." : "";
			throw new IndexOutOfBoundsException(from + to + fromTo);
		}
		TreeList<V> subList = new TreeList<>();
		Node node = select(fromIndex);
		for (int index = fromIndex; index < toIndex; ++index) {
			subList.add(node.getValue());
			node = successor(node);
		}
		return subList;
	}

	private void shift(int index, int offset) {
		Node curr = treeLocation(index);
		if (curr != null) {
			int currIndex = index;
			do {
				curr.setKey(currIndex += offset);
				curr = successor(curr);
			} while (curr != getMax());
		}
	}

	private boolean containsValue(Node node, V v) {
		return node != null &&
				(bothNullOrJustEqual(node.getValue(), v) || containsValue(node.getLeft(), v) ||
						containsValue(node.getRight(), v));
	}

	private boolean bothNullOrJustEqual(V v1, V v2) {
		return (v1 == null && v2 == null) || (v1 != null && v1.equals(v2));
	}

	private <T> T[] fillArray(@NotNull T[] arr) {
		int i = 0;
		for (V value : this) {
			arr[i++] = (T) value;
		}
		return arr;
	}

	private boolean removeValue(Node node, V v) {
		if (node == null) {
			return false;
		}
		if (bothNullOrJustEqual(node.getValue(), v)) {
			delete(node.getKey());
			return true;
		}
		return removeValue(node.getLeft(), v) || removeValue(node.getRight(), v);
	}

	private void validateIndexBounds(int index) {
		if (index < 0 || index > size() - 1) {
			throw new IndexOutOfBoundsException("Index must be between 0 and " + (size() - 1) + ". Received: " + index);
		}
	}

	private class TreeListIterator implements Iterator<V> {
		private Node node = getMin();

		@Override
		public boolean hasNext() {
			return node != null;
		}

		@Override
		public V next() {
			if (node == null) {
				throw new NoSuchElementException();
			}
			V value = node.getValue();
			node = successor(node);
			return value;
		}

		@Override
		public void remove() {
			if (node == null) {
				throw new IllegalStateException();
			}
			delete(node.getKey());
		}
	}

	private class TreeListListIterator extends TreeListIterator implements ListIterator<V> {
		private int index;

		TreeListListIterator(int index) {
			this.index = index;
		}

		@Override
		public boolean hasPrevious() {
			return super.node != null;
		}

		@Override
		public V previous() {
			if (super.node == null) {
				throw new NoSuchElementException();
			}
			V value = super.node.getValue();
			super.node = predecessor(super.node);
			return value;
		}

		@Override
		public int nextIndex() {
			return index + 1;
		}

		@Override
		public int previousIndex() {
			return index - 1;
		}

		@Override
		public void set(V v) {
			if (super.node == null) {
				throw new IllegalStateException();
			}
			super.node.setValue(v);
		}

		@Override
		public void add(V v) {
			TreeList.this.add(index, v);
			if (super.node == null) {
				super.node = getMin();
			} else {
				super.node = predecessor(super.node);
			}
		}
	}
}
