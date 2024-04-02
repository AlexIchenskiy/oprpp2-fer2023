package hr.fer.oprpp2.custom.scripting.elems;

import java.util.Objects;

/**
 * Class representing an integer constant.
 */
public class ElementConstantInteger extends Element {

    /**
     * Constant value.
     */
    private final int value;

    /**
     * Constructs an integer constant with a defined value.
     * @param value Variable value
     */
    public ElementConstantInteger(int value) {
        this.value = value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String asText() {
        return this.value + "";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ElementConstantInteger)) return false;
        ElementConstantInteger that = (ElementConstantInteger) o;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

}
