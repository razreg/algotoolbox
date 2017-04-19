package com.raz.algotoolbox.datatypes.trees;

public class UnionFindTree<T> {

	public Node makeSet(T t) {
		return newNode(t);
	}

	public boolean isSameSet(Node x, Node y) {
		return find(x) == find(y);
	}

	public Node find(Node node) {
		if (node.getParent() != node) {
			node.setParent(find(node.getParent()));
		}
		return node.getParent();
	}

	public Node union(Node x, Node y) {
		return link(find(x), find(y));
	}

	private Node link(Node x, Node y) {
		if (x.getRank() > y.getRank()) {
			y.setParent(x);
		} else {
			x.setParent(y);
			if (x != y && x.getRank() == y.getRank()) {
				y.setRank(y.getRank() + 1);
			}
		}
		return find(x);
	}

	protected Node newNode(T t) {
		return new Node(t);
	}

	public class Node {

		private T t;
		private Node parent;
		private int rank;

		private Node(T t) {
			this.t = t;
			parent = this;
		}

		public T get() {
			return t;
		}

		protected Node getParent() {
			return parent;
		}

		protected void setParent(Node parent) {
			this.parent = parent;
		}

		protected int getRank() {
			return rank;
		}

		protected void setRank(int rank) {
			this.rank = rank;
		}
	}

}
