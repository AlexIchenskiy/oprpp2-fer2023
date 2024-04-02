package hr.fer.oprpp2.custom.scripting.elems;

import java.util.Objects;

/**
 * Class representing a string element.
 */
public class ElementString extends Element {

    /**
     * Element value.
     */
    private final String value;

    /**
     * Constructs a string element with a defined value.
     * @param value Variable value
     */
    public ElementString(String value) {
        this.value = value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String asText() {
        return "\"" + this.value.replace("\\", "\\\\").replace("\"", "\\\"") + "\"";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ElementString)) return false;
        ElementString that = (ElementString) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

}
