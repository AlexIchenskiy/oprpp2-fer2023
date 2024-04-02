package hr.fer.oprpp2.custom.scripting.elems;

import java.util.Objects;

/**
 * Class representing a double constant.
 */
public class ElementConstantDouble extends Element {

    /**
     * Constant value.
     */
    private final double value;

    /**
     * Constructs a double constant with a defined value.
     * @param value Variable value
     */
    public ElementConstantDouble(double value) {
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
        if (!(o instanceof ElementConstantDouble)) return false;
        ElementConstantDouble that = (ElementConstantDouble) o;
        return Double.compare(that.value, value) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

}
