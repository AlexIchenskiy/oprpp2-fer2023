package hr.fer.oprpp2.custom.scripting.nodes;

public interface INodeVisitor {

    void visitTextNode(TextNode node);

    void visitForLoopNode(ForLoopNode node);

    void visitEchoNode(EchoNode node);

    void visitDocumentNode(DocumentNode node);

}
