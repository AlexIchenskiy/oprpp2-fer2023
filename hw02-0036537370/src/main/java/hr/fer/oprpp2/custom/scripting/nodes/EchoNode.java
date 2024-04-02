package hr.fer.oprpp2.custom.scripting.nodes;

import hr.fer.oprpp2.custom.scripting.elems.Element;

import java.util.Arrays;

/**
 * Class representing a command which generates textual output.
 */
public class EchoNode extends Node {

    /**
     * Elements of the echo node.
     */
    private final Element[] elements;

    /**
     * Constructs an echo node with its elements.
     * @param elements Elements to be used
     */
    public EchoNode(Element[] elements) {
        if (elements == null) {
            throw new NullPointerException("Elements cant be null!");
        }

        this.elements = elements;
    }

    /**
     * Return echo node elements.
     * @return Echo node elements
     */
    public Element[] getElements() {
        return elements;
    }

    /**
     * Returns a string representation of the echo node.
     * @return String representation of the echo node
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("{$= ");

        for (Element i : this.elements) {
            sb.append(i.asText()).append(" ");
        }

        sb.append("$}");

        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EchoNode)) return false;
        if (!super.equals(o)) return false;
        EchoNode echoNode = (EchoNode) o;
        return Arrays.equals(elements, echoNode.elements);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + Arrays.hashCode(elements);
        return result;
    }

    @Override
    public void accept(INodeVisitor visitor) {
        visitor.visitEchoNode(this);
    }

}
