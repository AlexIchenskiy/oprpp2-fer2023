package hr.fer.oprpp2.custom.scripting.elems;

import java.util.Objects;

/**
 * Class representing an expression variable.
 */
public class ElementVariable extends Element {

    /**
     * Variable name.
     */
    private final String name;

    /**
     * Constructs a variable with a defined name.
     * @param name Variable name
     */
    public ElementVariable(String name) {
        this.name = name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String asText() {
        return this.name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ElementVariable)) return false;
        ElementVariable variable = (ElementVariable) o;
        return Objects.equals(name, variable.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

}
