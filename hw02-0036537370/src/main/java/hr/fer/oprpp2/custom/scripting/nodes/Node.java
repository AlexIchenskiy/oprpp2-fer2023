package hr.fer.oprpp2.custom.scripting.nodes;

import hr.fer.oprpp2.custom.collections.ArrayIndexedCollection;

import java.util.Objects;

/**
 * Base class for all graph nodes.
 */
public class Node {

    private ArrayIndexedCollection children;

    /**
     * Adds given child to the node.
     * @param child Child to be added
     */
    public void addChildNode(Node child) {
        if (child == null) {
            throw new NullPointerException("Child cant be null!");
        }

        if (this.children == null) {
            this.children = new ArrayIndexedCollection();
        }

        this.children.add(child);
    }

    /**
     * Returns the number of the direct node children.
     * @return Number of the direct node children
     */
    public int numberOfChildren() {
        if (this.children == null) {
            return 0;
        }

        return this.children.size();
    }

    /**
     * Returns a nodes' child by its index.
     * @param index Index of the child
     * @return Node on the provided index
     */
    public Node getChild(int index) {
        if (this.children == null || index < 0 || index >= this.children.size()) {
            throw new IndexOutOfBoundsException("Invalid index!");
        }

        return (Node) this.children.get(index);
    }

    public void accept(INodeVisitor visitor) {
        return;
    }

	@Override
	public int hashCode() {
		return Objects.hash(children);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Node other = (Node) obj;
		return Objects.equals(children, other.children);
	}
    
}
