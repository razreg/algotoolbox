package com.raz.algotoolbox.datatypes.hashtables;

@FunctionalInterface
interface ProbingHashFunction<K> {
	Integer apply(K key, Integer offset, Integer capacity);
}
