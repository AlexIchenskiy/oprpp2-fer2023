package hr.fer.oprpp2.custom.scripting.nodes;

/**
 * Class representing a document node.
 */
public class DocumentNode extends Node {

    /**
     * Returns a string representation of the document node.
     * @return String representation of the document node
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < this.numberOfChildren(); i++) {
            sb.append(this.getChild(i).toString());
        }

        return sb.toString();
    }

    @Override
    public void accept(INodeVisitor visitor) {
        visitor.visitDocumentNode(this);
    }

}
