package com.raz.algotoolbox.datatypes.hashtables;

import com.raz.algotoolbox.internal.NotNull;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * A hash table which uses a linear probing scheme to resolve collisions.
 * This hash table actually gives the behavior of a hash map, but can be converted to a hash set quite simply
 * by using the value as the key as well, or using a dummy object as value and using the actual value as key
 * (i.e. the approach taken by java.util.HashSet).
 * @param <K> is the type of the keys in the hash table.
 * @param <V> is the type to be stored as value in the hash table.
 */
public class LinearProbingHashTable<K, V> {

	private static final int INITIAL_CAPACITY = 8;
	private static final String BAD_HASH = "The table is not full but the hash function did not yield an available position";

	private Entry<K, V>[] entries;
	private ProbingHashFunction<K> probingHashFunction = (k, i, c) -> Math.abs((k.hashCode() + i) % c);
	private int currentLoad;

	@SuppressWarnings("unchecked")
	public LinearProbingHashTable() {
		entries = (Entry<K, V>[]) new Entry[INITIAL_CAPACITY];
	}

	/**
	 * @param hashFunction must be a linear probing function!
	 */
	protected LinearProbingHashTable(ProbingHashFunction<K> hashFunction) {
		this();
		this.probingHashFunction = hashFunction;
	}

	public int size() {
		return currentLoad;
	}

	public boolean isEmpty() {
		return size() == 0;
	}

	public boolean containsKey(Object key) {
		@SuppressWarnings("unchecked")
		K k = (K) Objects.requireNonNull(key);
		for (int i = 0; i < entries.length; ++i) {
			int hash = hash(k, i);
			if (entries[hash] == null) {
				break;
			}
			if (sameKey(entries[hash].getKey(), k)) {
				return true;
			}
		}
		return false;
	}

	public boolean containsValue(Object value) {
		@SuppressWarnings("unchecked")
		V v = (V) value;
		for (Entry<K, V> entry : entries) {
			if (entry != null && Objects.equals(entry.getValue(), v)) {
				return true;
			}
		}
		return false;
	}

	public V get(Object key) {
		@SuppressWarnings("unchecked")
		@NotNull K k = (K) Objects.requireNonNull(key);
		for (int i = 0; i < entries.length; ++i) {
			int hash = hash(k, i);
			if (entries[hash] == null) {
				break;
			}
			if (sameKey(getEntryKey(entries[hash]), k)) {
				return entries[hash].getValue();
			}
		}
		return null;
	}

	public V put(K key, V value) {
		if (entries.length * 0.8 < size()) {
			realloc(largerCapacity());
		}
		for (int i = 0; i < entries.length; ++i) {
			int hash = hash(key, i);
			if (entries[hash] == null) {
				entries[hash] = new Entry<>(key, value);
				currentLoad++;
				return null;
			}
			if (sameKey(entries[hash].getKey(), key)) {
				V oldValue = entries[hash].getValue();
				entries[hash] = new Entry<>(key, value);
				return oldValue;
			}
		}
		throw new IllegalStateException(BAD_HASH);
	}

	public V remove(Object key) {
		@SuppressWarnings("unchecked")
		K k = (K) Objects.requireNonNull(key);
		for (int i = 0; i < entries.length; ++i) {
			int hash = hash(k, i);
			if (entries[hash] == null) {
				break;
			}
			if (sameKey(getEntryKey(entries[hash]), k)) {
				V value = entries[hash].getValue();
				entries[hash] = null;
				currentLoad--;
				if (size() < 0.1 * entries.length && entries.length > INITIAL_CAPACITY) {
					realloc(smallerCapacity()); // rehashing as a side effect
				} else {
					fixTable(hash);
				}
				return value;
			}
		}
		return null;
	}

	public void putAll(Map<? extends K, ? extends V> m) {
		m.forEach(this::put);
	}

	@SuppressWarnings("unchecked")
	public void clear() {
		entries = (Entry<K, V>[]) new Entry[INITIAL_CAPACITY];
		currentLoad = 0;
	}

	public void forEach(BiConsumer<? super K, ? super V> action) {
		for (Entry<K, V> entry :  entries) {
			if (entry != null) {
				action.accept(entry.getKey(), entry.getValue());
			}
		}
	}

	public void forEach(Consumer<Entry<K, V>> action) {
		for (Entry<K, V> entry :  entries) {
			if (entry != null) {
				action.accept(entry);
			}
		}
	}

	public Iterator<K> keysIterator() {
		return new Iterator<K>() {

			private int index = -1;
			{
				index = findNextIndex();
			}

			@Override
			public boolean hasNext() {
				return index < entries.length;
			}

			@Override
			public K next() {
				K k = entries[index].getKey();
				index = findNextIndex();
				return k;
			}

			private int findNextIndex() {
				for (int i = index + 1; i < entries.length; ++i) {
					if (entries[i] != null) {
						return i;
					}
				}
				return entries.length;
			}
		};
	}

	private K getEntryKey(Entry<K, V> entry) {
		return entry == null ? null : entry.getKey();
	}

	private boolean sameKey(K k1, K k2) {
		return k1 == null ? k2 == null : k1.equals(k2);
	}

	@SuppressWarnings("unchecked")
	private void realloc(int newCapacity) {
		Entry<K, V>[] newEntries = (Entry<K, V>[]) new Entry[newCapacity];
		for (Entry<K, V> entry : entries) {
			loopBody: if (entry != null) {
				for (int i = 0; i < newCapacity; ++i) {
					int hash = hash(entry.getKey(), i, newCapacity);
					if (newEntries[hash] == null) {
						newEntries[hash] = entry;
						break loopBody;
					}
				}
				throw new IllegalStateException(BAD_HASH);
			}
		}
		entries = newEntries;
	}

	private void fixTable(int start) {
		int i = (start + 1) % entries.length;
		do {
			if (entries[i] == null) {
				break;
			}
			int hash = hash(entries[i].getKey(), 0);
			if ((i < hash && hash <= start) || (i > start && (hash <= start || hash > i))) {
				entries[start] = entries[i];
				entries[i] = null;
				start = i;
			}
			i = (i + 1) % entries.length;
		} while (i != start);
	}

	private int largerCapacity() {
		int oldCapacity = entries.length;
		if (oldCapacity == Integer.MAX_VALUE) {
			throw new ArrayStoreException("The load on the hash table is too large");
		}
		if (oldCapacity < 1024) {
			return oldCapacity << 1;
		}
		if (oldCapacity < Integer.MAX_VALUE - 1024) {
			return oldCapacity + 1024;
		}
		return Integer.MAX_VALUE;
	}

	private int smallerCapacity() {
		return Math.max(entries.length >>> 1, INITIAL_CAPACITY);
	}

	private int hash(K key, int offset, int capacity) {
		return probingHashFunction.apply(key, offset, capacity);
	}

	private int hash(K key, int offset) {
		return probingHashFunction.apply(key, offset, entries.length);
	}

	protected static class Entry<K, V> implements Map.Entry<K, V> {

		private K key;
		private V value;

		public Entry(K k, V v) {
			key = k;
			value = v;
		}

		public K getKey() {
			return key;
		}

		protected void setKey(K key) {
			this.key = key;
		}

		public V getValue() {
			return value;
		}

		public V setValue(V value) {
			V oldValue = this.value;
			this.value = value;
			return oldValue;
		}
	}

}
