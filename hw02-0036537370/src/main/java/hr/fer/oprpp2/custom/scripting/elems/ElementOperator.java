package hr.fer.oprpp2.custom.scripting.elems;

import java.util.Objects;

/**
 * Class representing an operator.
 */
public class ElementOperator extends Element {

    /**
     * Operator symbol.
     */
    private final String symbol;

    /**
     * Constructs a string element with a defined value.
     * @param symbol Variable value
     */
    public ElementOperator(String symbol) {
        this.symbol = symbol;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String asText() {
        return this.symbol;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ElementOperator)) return false;
        ElementOperator that = (ElementOperator) o;
        return Objects.equals(symbol, that.symbol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol);
    }
}
