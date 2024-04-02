package hr.fer.oprpp2.custom.scripting.elems;

import java.util.Objects;

public class ElementFunction extends Element {

    /**
     * Function name.
     */
    private final String name;

    /**
     * Constructs a function with a defined name.
     * @param name Function name
     */
    public ElementFunction(String name) {
        this.name = name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String asText() {
        return "@" +  this.name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ElementFunction)) return false;
        ElementFunction that = (ElementFunction) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
