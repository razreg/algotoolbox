package com.raz.algotoolbox.containers;

import com.raz.algotoolbox.internal.Procedure;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class HashSet<T> implements Set<T> {

	private final transient HashMap<T, Object> map = new HashMap<>();

	@Override
	public int size() {
		return map.size();
	}

	@Override
	public boolean isEmpty() {
		return map.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return map.containsKey(o);
	}

	@Override
	public Iterator<T> iterator() {
		return map.keysIterator();
	}

	@Override
	public Object[] toArray() {
		Object[] objs = new Object[size()];
		int i = 0;
		for (T t : this) {
			if (t != null) {
				objs[i++] = t;
			}
		}
		return objs;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <S> S[] toArray(S[] a) {
		S[] arr = a;
		if (arr.length < size()) {
			Object obj = java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size());
			try {
				arr = (S[]) obj;
			} catch (ClassCastException e) {
				throw new ArrayStoreException();
			}
		}
		int i = 0;
		for (T t : this) {
			if (t != null) {
				arr[i++] = (S) t;
			}
		}
		return arr;
	}

	@Override
	public boolean add(T t) {
		return mapChanged(() -> map.put(t, null));
	}

	@Override
	public boolean remove(Object o) {
		return mapChanged(() -> map.remove(o));
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		if (c != null) {
			for (Object o : c) {
				if (!map.containsKey(o)) {
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public boolean addAll(Collection<? extends T> c) {
		return c != null && mapChanged(() -> c.forEach(this::add));
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return mapChanged(() -> {
			if (c == null) {
				clear();
			} else {
				final Set<T> removeSet = new HashSet<>();
				map.forEach((k, v) -> {
					if (!c.contains(k)) {
						removeSet.add(k);
					}
				});
				removeSet.forEach(map::remove);
			}
		});
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return c != null && mapChanged(() -> c.forEach(map::remove));
	}

	@Override
	public void clear() {
		map.clear();
	}

	private boolean mapChanged(Procedure p) {
		int oldSize = size();
		p.apply();
		return size() != oldSize;
	}
}
