package com.raz.algotoolbox.containers;

import com.raz.algotoolbox.datatypes.hashtables.LinearProbingHashTable;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class HashMap<K, V> extends LinearProbingHashTable<K, V> implements Map<K, V> {

	@Override
	public Set<K> keySet() {
		Set<K> set = new HashSet<>();
		forEach((k, v) -> set.add(k));
		return set;
	}

	@Override
	public Collection<V> values() {
		Set<V> set = new HashSet<>();
		forEach((k, v) -> set.add(v));
		return set;
	}

	@Override
	public Set<Map.Entry<K, V>> entrySet() {
		Set<Map.Entry<K, V>> set = new HashSet<>();
		forEach(set::add);
		return set;
	}
}
