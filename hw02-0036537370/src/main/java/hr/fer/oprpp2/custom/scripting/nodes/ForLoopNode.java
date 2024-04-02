package hr.fer.oprpp2.custom.scripting.nodes;

import hr.fer.oprpp2.custom.scripting.elems.Element;
import hr.fer.oprpp2.custom.scripting.elems.ElementVariable;

import java.util.Objects;

/**
 * Class representing a node for the for loop.
 */
public class ForLoopNode extends Node {

    /**
     * Loop variable.
     */
    private final ElementVariable variable;

    /**
     * Loop start expression.
     */
    private final Element startExpression;

    /**
     * Loop end expression.
     */
    private final Element endExpression;

    /**
     * Loop step expression.
     */
    private final Element stepExpression;

    /**
     * Constructs a for loop node with all required values.
     * @param variable Loop variable
     * @param startExpression Loop start expression
     * @param endExpression Loop end expression
     * @param stepExpression Loop step expression
     */
    public ForLoopNode(ElementVariable variable, Element startExpression, Element endExpression, Element stepExpression) {
        if (variable == null || startExpression == null || endExpression == null) {
            throw new NullPointerException("Values cant be null!");
        }

        this.variable = variable;
        this.startExpression = startExpression;
        this.endExpression = endExpression;
        this.stepExpression = stepExpression;
    }

    /**
     * Getter for the for loop variable.
     * @return For loop variable
     */
    public ElementVariable getVariable() {
        return variable;
    }

    /**
     * Getter for the for loop start expression.
     * @return For loop start expression
     */
    public Element getStartExpression() {
        return startExpression;
    }

    /**
     * Getter for the for loop end expression
     * @return For loop end expression
     */
    public Element getEndExpression() {
        return endExpression;
    }

    /**
     * Getter for the for loop step expression
     * @return For loop step expression
     */
    public Element getStepExpression() {
        return stepExpression;
    }

    /**
     * Returns a string representation of the for loop node.
     * @return String representation of the for loop node
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("{$ FOR ")
                .append(this.variable.asText()).append(" ")
                .append(this.startExpression.asText()).append(" ")
                .append(this.endExpression.asText()).append(" ");

        if (this.stepExpression != null) {
            sb.append(this.stepExpression.asText());
        }

        sb.append("$}");

        for (int i = 0; i < this.numberOfChildren(); i++) {
            sb.append(this.getChild(i).toString());
        }

        sb.append("{$ END $}");

        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ForLoopNode)) return false;
        if (!super.equals(o)) return false;
        ForLoopNode that = (ForLoopNode) o;
        return Objects.equals(variable, that.variable) && Objects.equals(startExpression, that.startExpression)
                && Objects.equals(endExpression, that.endExpression) && Objects.equals(stepExpression, that.stepExpression);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), variable, startExpression, endExpression, stepExpression);
    }

    @Override
    public void accept(INodeVisitor visitor) {
        visitor.visitForLoopNode(this);
    }

}
