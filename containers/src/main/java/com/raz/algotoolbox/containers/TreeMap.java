package com.raz.algotoolbox.containers;

import com.raz.algotoolbox.datatypes.RedBlackTree;

import java.util.*;
import java.util.function.BiConsumer;

public class TreeMap<K extends Comparable<K>, V> extends RedBlackTree<K, V> implements Map<K, V> {

	@Override
	public boolean containsKey(Object keyObj) {
		try {
			return get(keyObj) != null;
		} catch (ClassCastException | NullPointerException e) {
			return false;
		}
	}

	@Override
	public boolean containsValue(final Object o) {
		@SuppressWarnings("unchecked")
		V value = (V) Objects.requireNonNull(o);
		return values().stream().anyMatch(v -> v == value);
	}

	@Override
	public V get(Object key) {
		@SuppressWarnings("unchecked")
		K k = (K) Objects.requireNonNull(key);
		return retrieve(k);
	}

	@Override
	public V put(K key, V value) {
		return insert(key, value);
	}

	@Override
	public V remove(Object key) {
		@SuppressWarnings("unchecked")
		K k = (K) Objects.requireNonNull(key);
		return delete(k);
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		m.forEach(this::insert);
	}

	@Override
	public void forEach(BiConsumer<? super K, ? super V> action) {
		Objects.requireNonNull(action);
		Node node = getRoot();
		if (node == null) {
			return;
		}
		while (node.getLeft() != null) {
			node = node.getLeft();
		}
		while (node != null) {
			action.accept(node.getKey(), node.getValue());
			node = successor(node);
		}
	}

	@Override
	public Set<K> keySet() {
		return fillKeySet(new HashSet<>(), getRoot());
	}

	@Override
	public Collection<V> values() {
		return getValues();
	}

	@Override
	public Set<Entry<K, V>> entrySet() {
		return fillEntrySet(new HashSet<>(), getRoot());
	}

	private Set<K> fillKeySet(Set<K> set, final Node node) {
		if (node != null) {
			fillKeySet(set, node.getLeft());
			set.add(node.getKey());
			fillKeySet(set, node.getRight());
		}
		return set;
	}

	private Set<Entry<K, V>> fillEntrySet(Set<Entry<K, V>> set, final Node node) {
		if (node != null) {
			fillEntrySet(set, node.getLeft());
			set.add(new Entry<K, V>() {
				@Override
				public K getKey() {
					return node.getKey();
				}
				@Override
				public V getValue() {
					return node.getValue();
				}
				@Override
				public V setValue(V value) {
					V oldValue = node.getValue();
					node.setValue(value);
					return oldValue;
				}
			});
			fillEntrySet(set, node.getRight());
		}
		return set;
	}

}

