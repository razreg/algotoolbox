package com.raz.algotoolbox.datatypes.trees;

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;

public class RankedRedBlackTreeTest extends RedBlackTreeTest {

	@Test
	public void testMin() {
		RankedRedBlackTree<Integer, Integer> tree = (RankedRedBlackTree<Integer, Integer>) this.<Integer, Integer>newTree();
		List<Integer> keys = new ArrayList<>();
		buildRandomTree(tree, keys);
		Collections.sort(keys);
		for (int i = 0; i < 5; ++i) {
			assertEquals(keys.get(0), tree.getMin().getKey());
			Integer key = keys.remove(0);
			tree.delete(key);
		}
	}

	@Test
	public void testMax() {
		RankedRedBlackTree<Integer, Integer> tree = (RankedRedBlackTree<Integer, Integer>) this.<Integer, Integer>newTree();
		List<Integer> keys = new ArrayList<>();
		buildRandomTree(tree, keys);
		Collections.sort(keys);
		for (int i = 0; i < 5; ++i) {
			assertEquals(keys.get(keys.size()-1), tree.getMax().getKey());
			Integer key = keys.remove(keys.size()-1);
			tree.delete(key);
		}
	}

	@Test
	public void testSelect() {
		RankedRedBlackTree<Integer, Integer> tree = (RankedRedBlackTree<Integer, Integer>) this.<Integer, Integer>newTree();
		Integer[] arr = { 1, 4, 7, 11, 13, 22, 43, 71, 89 };
		List<Integer> shuffled =  Arrays.asList(arr.clone());
		Collections.shuffle(shuffled);
		for (Integer a : shuffled) {
			tree.insert(a, a);
		}
		for (int i = 0; i < arr.length; ++i) {
			assertEquals(arr[i], tree.select(i).getKey());
		}
	}

	@Override
	<K extends Comparable<K>, V> RedBlackTree<K, V> newTree() {
		return new RankedRedBlackTree<>();
	}

	private void buildRandomTree(RankedRedBlackTree<Integer, Integer> tree, List<Integer> keys) {
		Random rand = new Random();
		for (int i = 0; i < 100; ++i) {
			int op = keys.isEmpty() ? 1 : rand.nextInt(4);
			if (op % 4 != 0) {
				int key;
				do {
					key = rand.nextInt(Integer.MAX_VALUE) + 1;
				} while (keys.contains(key));
				tree.insert(key, key);
				keys.add(key);
			} else {
				int index = rand.nextInt(keys.size());
				Integer key = keys.remove(index);
				tree.delete(key);
			}
		}
	}

}
