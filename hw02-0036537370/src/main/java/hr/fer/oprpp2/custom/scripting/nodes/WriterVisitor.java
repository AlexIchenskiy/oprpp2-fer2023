package hr.fer.oprpp2.custom.scripting.nodes;

public class WriterVisitor implements INodeVisitor {

    @Override
    public void visitTextNode(TextNode node) {
        System.out.print(node.toString());
    }

    @Override
    public void visitForLoopNode(ForLoopNode node) {
        System.out.print("{$ FOR " + node.getVariable().asText() + " " + node.getStartExpression().asText() + " "
            + node.getEndExpression().asText() + " ");

        if (node.getStepExpression() != null) {
            System.out.print(node.getStepExpression().asText());
        }

        System.out.print(" $}");

        this.checkChildren(node);

        System.out.print("{$ END $}");
    }

    @Override
    public void visitEchoNode(EchoNode node) {
        System.out.print(node.toString());
    }

    @Override
    public void visitDocumentNode(DocumentNode node) {
        this.checkChildren(node);
    }

    private void checkChildren(Node node) {
        for (int i = 0; i < node.numberOfChildren(); i++) {
            Node child = node.getChild(i);

            if (child instanceof TextNode) this.visitTextNode((TextNode) child);
            if (child instanceof ForLoopNode) this.visitForLoopNode((ForLoopNode) child);
            if (child instanceof EchoNode) this.visitEchoNode((EchoNode) child);
        }
    }

}
