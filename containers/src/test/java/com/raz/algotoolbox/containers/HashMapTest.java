package com.raz.algotoolbox.containers;

import org.junit.Test;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class HashMapTest {

	@Test
	public void testKeySet() {
		HashMap<Integer, String> map = new HashMap<>();
		for (int i = 0; i < 1000; i += 17) {
			map.put(i, "key = " + i);
		}
		Set<Integer> set = map.keySet();
		assertEquals(map.size(), set.size());
		map.forEach((k, v) -> assertTrue(set.contains(k)));
	}

	@Test
	public void testValues() {
		HashMap<Integer, String> map = new HashMap<>();
		for (int i = 0; i < 1000; i += 17) {
			String s = "key = " + i;
			map.put(i, "key = " + i);
		}
		Collection<String> collection = map.values();
		assertEquals(map.size(), collection.size());
		map.forEach((k, v) -> assertTrue(collection.contains(v)));
	}

	@Test
	public void testEntrySet() {
		HashMap<Integer, String> map = new HashMap<>();
		for (int i = 0; i < 1000; i += 17) {
			map.put(i, "key = " + i);
		}
		Set<Map.Entry<Integer, String>> set = map.entrySet();
		assertEquals(map.size(), set.size());
		set.forEach(entry -> assertEquals(map.get(entry.getKey()), entry.getValue()));
	}

}
