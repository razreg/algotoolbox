package com.raz.algotoolbox.datatypes.hashtables;

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LinearProbingHashTableTest {

	@Test
	public void testSize() {
		int bound = 50;
		LinearProbingHashTable<Integer, Integer> hashTable = new LinearProbingHashTable<>();
		Set<Integer> keys = new HashSet<>();
		Random rand = new Random();
		for (int i = 0; i < bound; ++i) {
			int op = keys.isEmpty() ? 1 : rand.nextInt(4);
			if (op % 4 != 0) {
				int key;
				do {
					key = rand.nextInt(Integer.MAX_VALUE) + 1;
				} while (keys.contains(key));
				hashTable.put(key, key);
				keys.add(key);
			} else {
				int index = rand.nextInt(keys.size());
				Integer key = (Integer) keys.toArray()[index];
				keys.remove(key);
				hashTable.remove(key);
			}
		}
		assertEquals(keys.size(), hashTable.size());
	}

	@Test
	public void testEmptyTable() {
		int bound = 50;
		LinearProbingHashTable<Integer, Integer> hashTable = new LinearProbingHashTable<>();
		Set<Integer> keys = new HashSet<>();
		Random rand = new Random();
		for (int i = 0; i < bound; ++i) {
			int key;
			do {
				key = rand.nextInt(Integer.MAX_VALUE) + 1;
			} while (keys.contains(key));
			hashTable.put(key, key);
			keys.add(key);
		}
		assertEquals(hashTable.size(), keys.size());
		for (Integer key : keys) {
			hashTable.remove(key);
		}
		assertTrue(hashTable.isEmpty());
	}

	@Test
	public void testContainsKey() {
		int bound = 50;
		LinearProbingHashTable<Integer, Integer> hashTable = new LinearProbingHashTable<>();
		Set<Integer> keys = new HashSet<>();
		Random rand = new Random();
		for (int i = 0; i < bound; ++i) {
			int op = keys.isEmpty() ? 1 : rand.nextInt(4);
			if (op % 4 != 0) {
				int key;
				do {
					key = rand.nextInt(Integer.MAX_VALUE) + 1;
				} while (keys.contains(key));
				hashTable.put(key, key);
				keys.add(key);
			} else {
				int index = rand.nextInt(keys.size());
				Integer key = (Integer) keys.toArray()[index];
				keys.remove(key);
				hashTable.remove(key);
			}
		}
		for (Integer key : keys) {
			assertTrue(hashTable.containsKey(key));
		}
		int count = 0;
		for (int i = 0; i < 100; ++i) {
			if (!keys.contains(i)) {
				assertFalse(hashTable.containsKey(i));
				count++;
			}
			if (count > 10) {
				break;
			}
		}
	}

	@Test
	public void testContainsValue() {
		int bound = 50;
		LinearProbingHashTable<Integer, Integer> hashTable = new LinearProbingHashTable<>();
		Set<Integer> keys = new HashSet<>();
		Random rand = new Random();
		for (int i = 0; i < bound; ++i) {
			int op = keys.isEmpty() ? 1 : rand.nextInt(4);
			if (op % 4 != 0) {
				int key;
				do {
					key = rand.nextInt(Integer.MAX_VALUE - 1000) + 1;
				} while (keys.contains(key));
				hashTable.put(key, key + 567);
				keys.add(key);
			} else {
				int index = rand.nextInt(keys.size());
				Integer key = (Integer) keys.toArray()[index];
				keys.remove(key);
				hashTable.remove(key);
			}
		}
		for (Integer key : keys) {
			assertTrue(hashTable.containsValue(key + 567));
		}
		int count = 0;
		for (int i = 0; i < 100; ++i) {
			if (!keys.contains(i)) {
				assertFalse(hashTable.containsValue(i + 567));
				count++;
			}
			if (count > 10) {
				break;
			}
		}
	}

	@Test
	public void testPutAndGet() {
		String[] strings = {"Howdy", "Hello", "Hi", "Goodbye"};
		String suffix =  " Neighbor!";
		LinearProbingHashTable<String, String> hashTable = new LinearProbingHashTable<>();
		for (String s : strings) {
			hashTable.put(s, s + suffix);
		}
		Arrays.sort(strings);
		for (String s : strings) {
			assertEquals(s + suffix, hashTable.get(s));
		}
	}

	@Test
	public void testRemove() {
		String[] strings = {"Howdy", "Hello", "Hi", "Goodbye"};
		String suffix =  " Neighbor!";
		LinearProbingHashTable<String, String> hashTable = new LinearProbingHashTable<>();
		for (String s : strings) {
			hashTable.put(s, s + suffix);
		}
		Arrays.sort(strings);
		for (String s : strings) {
			assertTrue(hashTable.containsKey(s));
			assertEquals(s + suffix, hashTable.remove(s));
			assertFalse(hashTable.containsKey(s));
		}
	}

	@Test
	public void testMassiveLoad() {
		int bound = 10000;
		LinearProbingHashTable<Integer, Integer> hashTable = new LinearProbingHashTable<>();
		Set<Integer> keys = new HashSet<>();
		Random rand = new Random();
		for (int i = 0; i < bound; ++i) {
			int op = keys.isEmpty() ? 1 : rand.nextInt(4);
			if (op % 4 != 0) {
				int key;
				do {
					key = rand.nextInt(Integer.MAX_VALUE) + 1;
				} while (keys.contains(key));
				hashTable.put(key, key);
				keys.add(key);
			} else {
				int index = rand.nextInt(keys.size());
				Integer key = (Integer) keys.toArray()[index];
				keys.remove(key);
				hashTable.remove(key);
			}
		}
		assertEquals(keys.size(), hashTable.size());
	}

	@Test
	public void testPutAll() {
		String[] strings = {"Howdy", "Hello", "Hi", "Goodbye"};
		Map<String, String> stringMap = new HashMap<>();
		String suffix =  " Neighbor!";
		LinearProbingHashTable<String, String> hashTable = new LinearProbingHashTable<>();
		for (String s : strings) {
			stringMap.put(s, s + suffix);
		}
		hashTable.putAll(stringMap);
		Arrays.sort(strings);
		for (String s : strings) {
			assertEquals(s + suffix, hashTable.get(s));
		}
	}

	@Test
	public void testClear() {
		int bound = 20;
		LinearProbingHashTable<Integer, Integer> hashTable = new LinearProbingHashTable<>();
		Set<Integer> keys = new HashSet<>();
		Random rand = new Random();
		for (int i = 0; i < bound; ++i) {
			int op = keys.isEmpty() ? 1 : rand.nextInt(4);
			if (op % 4 != 0) {
				int key;
				do {
					key = rand.nextInt(Integer.MAX_VALUE - 1000) + 1;
				} while (keys.contains(key));
				hashTable.put(key, key + 567);
				keys.add(key);
			} else {
				int index = rand.nextInt(keys.size());
				Integer key = (Integer) keys.toArray()[index];
				keys.remove(key);
				hashTable.remove(key);
			}
		}
		hashTable.clear();
		assertTrue(hashTable.isEmpty());
	}

	@Test
	public void testForEachEntry() {
		String[] strings = {"Howdy", "Hello", "Hi", "Goodbye"};
		String suffix =  " Neighbor!";
		LinearProbingHashTable<String, String> hashTable = new LinearProbingHashTable<>();
		for (String s : strings) {
			hashTable.put(s, s + suffix);
		}
		hashTable.forEach(e -> e.setValue(e.getValue() + "!"));
		Arrays.sort(strings);
		for (String s : strings) {
			assertEquals(s + suffix + "!", hashTable.get(s));
		}
	}

	@Test
	public void testForEachKeyValuePair() {
		String[] strings = {"Howdy", "Hello", "Hi", "Goodbye"};
		String suffix =  " Neighbor!";
		LinearProbingHashTable<String, String> hashTable = new LinearProbingHashTable<>();
		for (String s : strings) {
			hashTable.put(s, s + suffix);
		}
		Set<String> set = new HashSet<>();
		hashTable.forEach((k, v) -> set.add(k + "-" + v));
		String[] retrieved = set.toArray(new String[0]);
		Arrays.sort(retrieved);
		Arrays.sort(strings);
		for (int i = 0; i < strings.length; ++i) {
			assertEquals(strings[i] + "-" + strings[i] + suffix, retrieved[i]);
		}
	}

	@Test
	public void testKeysIterator() {
		String[] strings = {"Howdy", "Hello", "Hi", "Goodbye"};
		String suffix =  " Neighbor!";
		LinearProbingHashTable<String, String> hashTable = new LinearProbingHashTable<>();
		for (String s : strings) {
			hashTable.put(s, s + suffix);
		}
		String[] retrieved = new String[strings.length];
		int i = 0;
		for (Iterator<String> iter = hashTable.keysIterator(); iter.hasNext(); ++i) {
			retrieved[i] = iter.next();
		}
		Arrays.sort(retrieved);
		Arrays.sort(strings);
		for (i = 0; i < strings.length; ++i) {
			assertEquals(strings[i], retrieved[i]);
		}
	}

	@Test
	public void testUseExternalFunction() {
		ProbingHashFunction<Integer> function = (k, i, c) -> i % c; // bad function but should work
		int bound = 100;
		LinearProbingHashTable<Integer, Integer> hashTable = new LinearProbingHashTable<>(function);
		Set<Integer> keys = new HashSet<>();
		Random rand = new Random();
		for (int i = 0; i < bound; ++i) {
			int op = keys.isEmpty() ? 1 : rand.nextInt(4);
			if (op % 4 != 0) {
				int key;
				do {
					key = rand.nextInt(Integer.MAX_VALUE) + 1;
				} while (keys.contains(key));
				hashTable.put(key, key);
				keys.add(key);
			} else {
				int index = rand.nextInt(keys.size());
				Integer key = (Integer) keys.toArray()[index];
				keys.remove(key);
				hashTable.remove(key);
			}
		}
		assertEquals(keys.size(), hashTable.size());
	}

}
