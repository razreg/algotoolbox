package com.raz.algotoolbox.datatypes.trees;

import com.raz.algotoolbox.internal.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class RedBlackTree<K extends Comparable<K>, V> {

	private int size = 0;
	private Node root = null;

	public int size() {
		return size;
	}

	public boolean isEmpty() {
		return size() == 0;
	}

	public void clear() {
		root = null;
		size = 0;
	}

	public V retrieve(K key) {
		Objects.requireNonNull(key);
		Node node = treeLocation(key);
		return node != null && node.sameKey(key) ? node.getValue() : null;
	}

	public V insert(K key, V value) {
		Objects.requireNonNull(key);
		V previousValue = null;
		Node insertionLocation = treeLocation(key);
		if (insertionLocation == null) {
			initRoot(key, value);
			size++;
		} else if (insertionLocation.sameKey(key)) {
			previousValue = delete(insertionLocation.getKey());
			insert(key, value);
		} else {
			Node node = insert(insertionLocation, key, value);
			fixTreeAfterInsert(node);
		}
		return previousValue;
	}

	public V delete(K key) {
		Objects.requireNonNull(key);
		Node node = treeLocation(key);
		if (node == null || !node.sameKey(key)) {
			throw new IllegalArgumentException();
		}
		boolean isColorBlack = node.isBlack();
		Node fixupNode;
		Node fixupNodeDaddy;
		V value = node.getValue();
		if (node.getLeft() == null) {
			fixupNode = node.getRight();
			fixupNodeDaddy = node.getParent();
			transplant(node, node.getRight());
		} else if (node.getRight() == null) {
			fixupNode = node.getLeft();
			fixupNodeDaddy = node.getParent();
			transplant(node, node.getLeft());
		} else {
			Node successor = successor(node); // at this point, it is definitely in node's right sub-tree
			isColorBlack = successor.isBlack(); // successor exists so can't be null
			fixupNode = successor.getRight();
			fixupNodeDaddy = successor;
			if (successor.getParent() != node) {
				fixupNodeDaddy = successor.getParent();
				transplant(successor, successor.getRight());
				setRightChild(successor, node.getRight());
			}
			transplant(node, successor);
			setLeftChild(successor, node.getLeft());
			successor.setBlack(node.isBlack());
		}
		size--;
		if (isColorBlack) {
			fixTreeAfterDelete(fixupNode, fixupNodeDaddy);
		}
		return value;
	}

	public Collection<V> getValues() {
		return fillValueCollection(new ArrayList<>(), root);
	}

	protected Node getRoot() {
		return root;
	}

	protected Node successor(@NotNull Node node) {
		if (node.getRight() == null) {
			while (node.getParent() != null && isRightChild(node.getParent(), node)) {
				node = node.getParent();
			}
			node = node.getParent();
		} else {
			node = node.getRight();
			while (node.getLeft() != null) {
				node = node.getLeft();
			}
		}
		return node;
	}

	protected Node predecessor(@NotNull Node node) {
		if (node.getLeft() == null) {
			while (node != root && !isRightChild(node.getParent(), node)) {
				node = node.getParent();
			}
			node = node.getParent();
		} else {
			node = node.getLeft();
			while (node.getRight() != null) {
				node = node.getRight();
			}
		}
		return node;
	}

	private void transplant(@NotNull Node a, Node b) {
		if (a == root) {
			root = b;
			if (b != null) {
				b.setParent(null);
			}
		} else if (isRightChild(a.getParent(), a)) {
			setRightChild(a.getParent(), b);
		} else {
			setLeftChild(a.getParent(), b);
		}
		a.setParent(null);
	}

	private void fixTreeAfterDelete(Node node, Node daddy) {
		while (node != root && (node == null || node.isBlack())) {
			node = isRightChild(daddy, node) ?
					rightFixIterationAfterDelete(daddy) : leftFixIterationAfterDelete(daddy);
			if (node != root) {
				daddy = node.getParent();
			}
		}
		if (node != null) {
			node.setBlack();
		}
	}

	private Node rightFixIterationAfterDelete(Node daddy) {
		Node node;
		Node sibling = daddy.getLeft();
		if (sibling.isRed()) {
			sibling.setBlack();
			daddy.setRed();
			rightRotate(daddy);
			sibling = daddy.getLeft();
		}
		if ((sibling.getLeft() == null || sibling.getLeft().isBlack()) &&
				(sibling.getRight() == null || sibling.getRight().isBlack())) {
			sibling.setRed();
			node = daddy;
		} else {
			if (sibling.getLeft() == null || sibling.getLeft().isBlack()) {
				if (sibling.getRight() != null) {
					sibling.getRight().setBlack();
				}
				sibling.setRed();
				leftRotate(sibling);
				sibling = daddy.getLeft();
			}
			sibling.setBlack(daddy.isBlack());
			daddy.setBlack();
			if (sibling.getLeft() != null) {
				sibling.getLeft().setBlack();
			}
			rightRotate(daddy);
			node = root;
		}
		return node;
	}

	private Node leftFixIterationAfterDelete(Node daddy) {
		Node node;
		Node sibling = daddy.getRight();
		if (sibling.isRed()) {
			sibling.setBlack();
			daddy.setRed();
			leftRotate(daddy);
			sibling = daddy.getRight();
		}
		if ((sibling.getLeft() == null || sibling.getLeft().isBlack()) &&
				(sibling.getRight() == null || sibling.getRight().isBlack())) {
			sibling.setRed();
			node = daddy;
		} else {
			if (sibling.getRight() == null || sibling.getRight().isBlack()) {
				if (sibling.getLeft() != null) {
					sibling.getLeft().setBlack();
				}
				sibling.setRed();
				rightRotate(sibling);
				sibling = daddy.getRight();
			}
			sibling.setBlack(daddy.isBlack());
			daddy.setBlack();
			if (sibling.getRight() != null) {
				sibling.getRight().setBlack();
			}
			leftRotate(daddy);
			node = root;
		}
		return node;
	}

	private void fixTreeAfterInsert(@NotNull Node node) {
		while (node.getParent() != null && node.getParent().isRed()) {
			Node daddy = node.getParent();
			node = isRightChild(daddy.getParent(), daddy) ?
					fixWhenDaddyIsRightChild(node) : fixWhenDaddyIsLeftChild(node);
		}
		root.setBlack();
	}

	private Node fixWhenDaddyIsLeftChild(@NotNull Node node) {
		Node daddy = node.getParent();
		Node grandpa = daddy.getParent();
		Node uncle = grandpa.getRight();
		if (uncle == null || uncle.isBlack()) {
			if (isRightChild(daddy, node)) {
				node = daddy;
				leftRotate(node);
			}
			daddy = node.getParent();
			daddy.setBlack();
			grandpa = daddy.getParent();
			grandpa.setRed();
			rightRotate(grandpa);
		} else {
			uncle.setBlack();
			daddy.setBlack();
			grandpa.setRed();
			node = grandpa;
		}
		return node;
	}

	private Node fixWhenDaddyIsRightChild(@NotNull Node node) {
		Node daddy = node.getParent();
		Node grandpa = daddy.getParent();
		Node uncle = grandpa.getLeft();
		if (uncle == null || uncle.isBlack()) {
			if (!isRightChild(daddy, node)) {
				node = daddy;
				rightRotate(node);
			}
			daddy = node.getParent();
			daddy.setBlack();
			grandpa = daddy.getParent();
			grandpa.setRed();
			leftRotate(grandpa);
		} else {
			uncle.setBlack();
			daddy.setBlack();
			grandpa.setRed();
			node = grandpa;
		}
		return node;
	}

	private void leftRotate(@NotNull Node node) {
		Node rightChild = node.getRight();
		transplant(node, rightChild);
		setRightChild(node, rightChild.getLeft());
		setLeftChild(rightChild, node);
	}

	private void rightRotate(@NotNull Node node) {
		Node leftChild = node.getLeft();
		transplant(node, leftChild);
		setLeftChild(node, leftChild.getRight());
		setRightChild(leftChild, node);
	}

	private void setRightChild(Node daddy, Node node) {
		if (node != null) {
			node.setParent(daddy);
		}
		if (daddy != null) {
			daddy.setRight(node);
		}
	}

	private void setLeftChild(Node daddy, Node node) {
		if (node != null) {
			node.setParent(daddy);
		}
		if (daddy != null) {
			daddy.setLeft(node);
		}
	}

	private boolean isRightChild(Node parent, Node node) {
		if (parent != null) {
			if (parent.getRight() == node) {
				return true;
			}
			if (parent.getLeft() == node) {
				return false;
			}
		}
		throw new IllegalStateException("Parent of node {" + node + "} does not reference it as its child");
	}

	private Collection<V> fillValueCollection(Collection<V> collection, Node node) {
		if (node != null) {
			fillValueCollection(collection, node.getLeft());
			collection.add(node.getValue());
			fillValueCollection(collection, node.getRight());
		}
		return collection;
	}

	protected Node treeLocation(K k) {
		Node currNode = root;
		Node parent = root;
		while (currNode != null) {
			if (currNode.sameKey(k)) {
				return currNode;
			}
			parent = currNode;
			currNode = currNode.goRight(k) ? currNode.getRight() : currNode.getLeft();
		}
		return parent;
	}

	@NotNull
	private Node insert(Node insertionLocation, K key, V value) {
		Node node = newNode(key, value);
		if (insertionLocation.goRight(key)) {
			setRightChild(insertionLocation, node);
		} else {
			setLeftChild(insertionLocation, node);
		}
		size++;
		return node;
	}

	private void initRoot(K key, V value) {
		root = newNode(key, value);
		root.setBlack();
	}

	Node newNode(K key, V value) {
		return new Node(key, value);
	}

	protected class Node {

		private K key;
		private V value;
		private Node parent = null;
		private Node left = null;
		private Node right = null;
		private boolean black = false;

		Node(K key, V value) {
			this.key = key;
			this.value = value;
		}

		public K getKey() {
			return key;
		}

		public void setKey(K key) {
			this.key = key;
		}

		public V getValue() {
			return value;
		}

		public void setValue(V value) {
			this.value = value;
		}

		public Node getParent() {
			return parent;
		}

		protected void setParent(Node parent) {
			this.parent = parent;
		}

		public Node getLeft() {
			return left;
		}

		protected void setLeft(Node left) {
			this.left = left;
		}

		public Node getRight() {
			return right;
		}

		protected void setRight(Node right) {
			this.right = right;
		}

		protected boolean isBlack() {
			return black;
		}

		protected boolean isRed() {
			return !black;
		}

		protected void setBlack(boolean black) {
			this.black = black;
		}

		protected void setBlack() {
			black = true;
		}

		protected void setRed() {
			black = false;
		}

		protected boolean sameKey(K k) {
			return key.compareTo(k) == 0;
		}

		protected boolean goRight(K k) {
			return key.compareTo(k) < 0;
		}

		protected boolean goLeft(K k) {
			return key.compareTo(k) > 0;
		}

		@Override
		public String toString() {
			return key == null ? "null" : key.toString();
		}
	}

}
