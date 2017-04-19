package com.raz.algotoolbox.datatypes.trees;

public class RankedRedBlackTree<K extends Comparable<K>, V> extends RedBlackTree<K, V> {

	private Node min;
	private Node max;

	protected Node select(long index) {
		if (index < 0) {
			throw new IndexOutOfBoundsException("Index must be non-negative");
		} else if (getRoot() == null || index >= size()) {
			throw new IndexOutOfBoundsException("Requested index was " + index + " but tree size is only " + size());
		}
		long leftSubTreeSize = ((RankedNode) getRoot()).leftRank;
		if (index == leftSubTreeSize) {
			return getRoot();
		}
		return index < leftSubTreeSize ? findNodeFromMin(index) : findNodeFromMax(index);
	}

	protected Node getMin() {
		return min;
	}

	protected Node getMax() {
		return max;
	}

	@Override
	public V delete(K key) {
		if (isMin(key)) {
			min = min == null ? treeLocation(key) : successor(min);
		}
		if (isMax(key)) {
			max = max == null ? treeLocation(key) : predecessor(max);
		}
		return super.delete(key);
	}

	@Override
	public V insert(K key, V value) {
		boolean findNewMin = min == null || min.goLeft(key);
		boolean findNewMax = max == null || max.goRight(key);
		V oldValue = super.insert(key, value);
		if (findNewMin) {
			min = min == null ? treeLocation(key) : predecessor(min);
		}
		if (findNewMax) {
			max = max == null ? treeLocation(key) : successor(max);
		}
		return oldValue;
	}

	@Override
	Node newNode(K key, V value) {
		return new RankedNode(key, value);
	}

	private boolean isMin(K k) {
		return min != null && min.sameKey(k);
	}

	private boolean isMax(K k) {
		return max != null && max.sameKey(k);
	}

	private Node findNodeFromMin(long index) {
		RankedNode node = (RankedNode) min;

		// climb up from min
		while (node.getRank() <= index) {
			node = (RankedNode) node.getParent();
		}

		// go down from top
		long soFar = 0, updatedSoFar;
		do {
			updatedSoFar = soFar + node.leftRank;
			if (updatedSoFar < index) {
				soFar = updatedSoFar + 1;
				node = (RankedNode) node.getRight();
			} else if (updatedSoFar > index) {
				node = (RankedNode) node.getLeft();
			}
		}
		while (updatedSoFar != index);
		return node;
	}

	private Node findNodeFromMax(long index) {
		RankedNode node = (RankedNode) max;

		// climb up from max
		while (size() - node.getRank() > index) {
			node = (RankedNode) node.getParent();
		}

		// go down from top
		long soFar = size() - 1, updatedSoFar;
		do {
			updatedSoFar = soFar - node.rightRank;
			if (updatedSoFar > index) {
				soFar = updatedSoFar - 1;
				node = (RankedNode) node.getLeft();
			} else if (updatedSoFar < index) {
				node = (RankedNode) node.getRight();
			}
		}
		while (updatedSoFar != index);
		return node;
	}

	protected class RankedNode extends Node {

		private long leftRank = 0;
		private long rightRank = 0;

		RankedNode(K key, V value) {
			super(key, value);
		}

		@Override
		public void setLeft(Node node) {
			long oldLeftRank = leftRank;
			super.setLeft(node);
			leftRank = node == null ? 0 : ((RankedNode) node).getRank();
			if (leftRank != oldLeftRank && getParent() != null) {
				((RankedNode) getParent()).propagateRank(this, getRank());
			}
		}

		@Override
		public void setRight(Node node) {
			long oldRightRank = rightRank;
			super.setRight(node);
			rightRank = node == null ? 0 : ((RankedNode) node).getRank();
			if (rightRank != oldRightRank && getParent() != null) {
				((RankedNode) getParent()).propagateRank(this, getRank());
			}
		}

		private long getRank() {
			return leftRank + 1 + rightRank;
		}

		private void propagateRank(RankedNode child, long rank) {
			if (getLeft() == child) {
				leftRank = rank;
			} else if (getRight() == child){
				rightRank = rank;
			} else {
				throw new IllegalStateException("Child cannot have a parent that does not know it");
			}
			if (getParent() != null) {
				((RankedNode) getParent()).propagateRank(this, getRank());
			}
		}

	}

}
