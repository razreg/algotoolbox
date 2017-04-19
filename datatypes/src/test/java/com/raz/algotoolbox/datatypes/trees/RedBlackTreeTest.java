package com.raz.algotoolbox.datatypes.trees;

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class RedBlackTreeTest {

	@Test(expected = NullPointerException.class)
	public void testInsertNullKey() {
		RedBlackTree<Integer, Integer> tree = newTree();
		tree.insert(null, 2);
	}

	@Test
	public void testRetrieveNonExistingKey() {
		RedBlackTree<Integer, Integer> tree = newTree();
		tree.insert(1, 2);
		assertNull(tree.retrieve(3));
	}

	@Test(expected = NullPointerException.class)
	public void testRetrieveNullKey() {
		RedBlackTree<Integer, Integer> tree = newTree();
		tree.insert(1, 2);
		tree.retrieve(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testDeleteNonExistingKey() {
		RedBlackTree<Integer, Integer> tree = newTree();
		tree.insert(1, 2);
		tree.delete(3);
	}

	@Test(expected = NullPointerException.class)
	public void testDeleteNullKey() {
		RedBlackTree<Integer, Integer> tree = newTree();
		tree.insert(1, 2);
		tree.delete(null);
	}

	@Test
	public void testEmptyTree() {
		RedBlackTree<Integer, String> emptyTree = newTree();
		assertTrue(emptyTree.isEmpty());
		assertTrue(isTreeLegal(emptyTree));
	}

	@Test
	public void testInsertDoesNotViolateTreeProperties() {
		testInsertDoesNotViolateTreeProperties(1000);
	}

	@Test
	public void testSize() {
		testSize(50);
	}

	@Test
	public void testBlackHeight() {
		testBlackHeight(100);
	}

	@Test
	public void testRandomInsertionsAndDeletions() {
		testRandomInsertionsAndDeletions(10000);
	}

	@Test
	public void testGetValues() {
		RedBlackTree<Integer, String> tree = newTree();
		String[] startFrom = { "A", "B", "C", "E", "F", "D" };
		String[] expected = { "A", "B", "C", "D", "E", "F" };
		for (int i = 0; i < startFrom.length - 1; ++i) {
			tree.insert(i*2, startFrom[i]);
		}
		tree.insert(5, startFrom[startFrom.length - 1]);
		Object[] values = tree.getValues().toArray();
		for (int i = 0; i < values.length; ++i) {
			assertEquals(expected[i], values[i]);
		}
		assertTrue(isTreeLegal(tree));
	}

	private void testSize(int bound) {
		RedBlackTree<Integer, Integer> tree = newTree();
		List<Integer> keys = new ArrayList<>();
		Random rand = new Random();
		for (int i = 0; i < bound; ++i) {
			int op = keys.isEmpty() ? 1 : rand.nextInt(4);
			if (op % 4 != 0) {
				int key;
				do {
					key = rand.nextInt(Integer.MAX_VALUE) + 1;
				} while (keys.contains(key));
				int value = rand.nextInt(Integer.MAX_VALUE) + 1;
				tree.insert(key, value);
				keys.add(key);
			} else {
				int index = rand.nextInt(keys.size());
				Integer key = keys.remove(index);
				tree.delete(key);
			}
		}
		assertEquals(keys.size(), tree.size());
	}

	private void testInsertDoesNotViolateTreeProperties(int bound) {
		Set<Integer> keys = new HashSet<>();
		final RedBlackTree<Integer, Integer> tree = newTree();
		Random rand = new Random();
		for (int i = 0; i < bound; ) {
			int key = rand.nextInt(Integer.MAX_VALUE) + 1;
			if (!keys.contains(key)) {
				tree.insert(key, key);
				keys.add(key);
				++i;
			}
		}
		assertTrue(isTreeLegal(tree));
	}

	private void testRandomInsertionsAndDeletions(int bound) {
		Map<Integer, Integer> keyMap = new HashMap<>();
		List<Integer> keys = new ArrayList<>();
		final RedBlackTree<Integer, Integer> tree = newTree();
		Random rand = new Random();
		for (int i = 0; i < bound; ++i) {
			int op = keys.isEmpty() ? 1 : rand.nextInt(4);
			if (op % 4 != 0) {
				int key = rand.nextInt(Integer.MAX_VALUE) + 1;
				int value = rand.nextInt(Integer.MAX_VALUE) + 1;
				tree.insert(key, value);
				keyMap.put(key, value);
				keys.add(key);
			} else {
				int index = rand.nextInt(keys.size());
				Integer key = keys.remove(index);
				int treeValue = tree.delete(key);
				int mapValue = keyMap.remove(key);
				assertEquals(treeValue, mapValue);
			}
		}
		keyMap.forEach((k, v) -> assertEquals(v, tree.retrieve(k)));
		assertTrue(isTreeLegal(tree));
	}

	private void testBlackHeight(int bound) {
		RedBlackTree<Integer, Integer> tree = newTree();
		List<Integer> keys = new ArrayList<>();
		Random rand = new Random();
		for (int i = 0; i < bound; ++i) {
			int op = keys.isEmpty() ? 1 : rand.nextInt(4);
			if (op % 4 != 0) {
				int key = rand.nextInt(Integer.MAX_VALUE) + 1;
				int value = rand.nextInt(Integer.MAX_VALUE) + 1;
				tree.insert(key, value);
				keys.add(key);
			} else {
				int index = rand.nextInt(keys.size());
				Integer key = keys.remove(index);
				tree.delete(key);
			}
		}
		int blackHeight = countBlackNodes(tree.getRoot());
		assertTrue(blackHeight <= Math.ceil(Math.log(keys.size() + 1) / Math.log(2)));
	}

	private <K extends Comparable<K>, V> boolean isTreeLegal(RedBlackTree<K, V> tree) {
		return isRedRuleKept(tree) && isBlackRuleKept(tree);
	}

	private <K extends Comparable<K>, V> boolean isRedRuleKept(RedBlackTree<K, V> tree) {
		return isRedRuleKept(tree.getRoot());
	}

	private <K extends Comparable<K>, V> boolean isRedRuleKept(RedBlackTree.Node node) {
		if (node == null) {
			return true;
		}
		boolean redRuleKeptAtThisLevel = node.isBlack() ||
				((node.getRight() == null || node.getRight().isBlack()) &&
						(node.getLeft() == null || node.getLeft().isBlack()));
		return redRuleKeptAtThisLevel && isRedRuleKept(node.getLeft()) && isRedRuleKept(node.getRight());
	}

	private <K extends Comparable<K>, V> boolean isBlackRuleKept(RedBlackTree<K, V> tree) {
		boolean rootIsBlack = (tree.size() == 0 && tree.getRoot() == null) || tree.getRoot().isBlack();
		if (!rootIsBlack) {
			return false;
		} else {
			try {
				countBlackNodes(tree.getRoot());
				return true;
			} catch (IllegalStateException e) {
				return false;
			}
		}
	}

	private int countBlackNodes(RedBlackTree.Node node) {
		if (node == null) {
			return 0;
		}
		int leftCount = countBlackNodes(node.getLeft());
		int rightCount = countBlackNodes(node.getRight());
		if (leftCount != rightCount) {
			throw new IllegalStateException("left = " + leftCount + ", right = " + rightCount);
		}
		return (node.isBlack() ? 1 : 0) + leftCount;
	}

	<K extends Comparable<K>, V> RedBlackTree<K, V> newTree() {
		return new RedBlackTree<>();
	}

}
