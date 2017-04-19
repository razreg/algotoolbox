package com.raz.algotoolbox.datatypes.trees;

import org.junit.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class UnionFindTreeTest {

	@Test
	public void testBasicUnionFind() {
		UnionFindTree<String> unionFindTree = new UnionFindTree<>();
		UnionFindTree<String>.Node node1 = unionFindTree.makeSet("Hi");
		UnionFindTree<String>.Node node2 = unionFindTree.makeSet("Hello");
		UnionFindTree<String>.Node node3 = unionFindTree.makeSet("Goodbye");
		assertNotEquals(unionFindTree.find(node1), unionFindTree.find(node2));
		unionFindTree.union(node1, node2);
		assertEquals(unionFindTree.find(node1), unionFindTree.find(node2));
		assertNotEquals(unionFindTree.find(node1), unionFindTree.find(node3));
		assertNotEquals(unionFindTree.find(node2), unionFindTree.find(node3));
		unionFindTree.union(node2, node3);
		assertEquals(unionFindTree.find(node1), unionFindTree.find(node2));
		assertEquals(unionFindTree.find(node1), unionFindTree.find(node3));
		assertEquals(unionFindTree.find(node2), unionFindTree.find(node3));
		unionFindTree.union(node3, node1);
		assertEquals(unionFindTree.find(node1), unionFindTree.find(node2));
		assertEquals(unionFindTree.find(node1), unionFindTree.find(node3));
		assertEquals(unionFindTree.find(node2), unionFindTree.find(node3));
	}

	@Test
	public void testSets() {
		int size = 100;
		UnionFindTree<Integer> unionFindTree = new UnionFindTree<>();
		Map<Integer, UnionFindTree<Integer>.Node> map = new HashMap<>();

		for (int i = 0; i < size; ++i) {
			map.put(i, unionFindTree.makeSet(i));
		}
		unionAndAssert(unionFindTree, map, 0, size, 13, size - (int) Math.ceil(size / 13.0));
		unionAndAssert(unionFindTree, map, 23, size, 23,size - (int) Math.ceil(size / 13.0) - (int) Math.ceil(size / 23.0) + 1);
		unionAndAssert(unionFindTree, map, 0, size, 1, 1);
	}

	private void unionAndAssert(UnionFindTree<Integer> unionFindTree,
								Map<Integer, UnionFindTree<Integer>.Node> map,
								int start, int size, int interval, int expectedNumOfSets) {
		Set<UnionFindTree<Integer>.Node> set = new HashSet<>();
		for (int i = start; i < size - 1; i += interval) {
			unionFindTree.union(map.get(i), map.get(i + 1));
		}
		for (Map.Entry<Integer, UnionFindTree<Integer>.Node> entry : map.entrySet()) {
			set.add(unionFindTree.find(entry.getValue()));
		}
		assertEquals(expectedNumOfSets, set.size());
		set.clear();
	}

}
