package hr.fer.oprpp2.custom.scripting.nodes;

import java.util.Objects;

/**
 * Class representing a text node.
 */
public class TextNode extends Node {

    /**
     * Text value of the node.
     */
    private final String text;

    /**
     * Constructs a text node by its text value.
     * @param text Text value of the node
     */
    public TextNode(String text) {
        this.text = text;
    }

    /**
     * Getter for the nodes' text value.
     * @return The nodes' text value
     */
    public String getText() {
        return this.text;
    }

    /**
     * Returns a string representation of the text node.
     * @return String representation of the text node
     */
    @Override
    public String toString() {
        return this.text.replace("\\", "\\\\")
                .replace("{", "\\{");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TextNode)) return false;
        if (!super.equals(o)) return false;
        TextNode textNode = (TextNode) o;
        return Objects.equals(text, textNode.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), text);
    }

    @Override
    public void accept(INodeVisitor visitor) {
        visitor.visitTextNode(this);
    }

}
