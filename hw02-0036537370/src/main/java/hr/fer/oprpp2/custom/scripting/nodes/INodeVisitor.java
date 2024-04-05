package hr.fer.oprpp2.custom.scripting.nodes;

/**
 * Interface providing methods for smart script nodes visitor.
 */
public interface INodeVisitor {

    void visitTextNode(TextNode node);

    void visitForLoopNode(ForLoopNode node);

    void visitEchoNode(EchoNode node);

    void visitDocumentNode(DocumentNode node);

}
