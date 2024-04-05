package hr.fer.oprpp2.custom.scripting.exec;

import java.util.Objects;

/**
 * Class representing a wrapper for numeric values.
 */
public class ValueWrapper {

    private Object value;

    public ValueWrapper(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public void add(Object incValue) {
        this.performOperation(incValue, 0);
    }

    public void subtract(Object decValue) {
        this.performOperation(decValue, 1);
    }

    public void multiply(Object mulValue) {
        this.performOperation(mulValue, 2);
    }

    public void divide(Object divValue) {
        this.performOperation(divValue, 3);
    }

    public int numCompare(Object withValue) {
        if (this.value == null && withValue == null) return 0;

        double val1 = Double.parseDouble(String.valueOf(this.parseValue(this.value)));
        double val2 = Double.parseDouble(String.valueOf(this.parseValue(withValue)));;

        return Double.compare(val1, val2);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ValueWrapper wrapper = (ValueWrapper) o;
        return Objects.equals(value, wrapper.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    private Object parseValue(Object value) {
        Object val = value == null ? Integer.valueOf(0) : value;
        val = String.valueOf(val).replaceAll("\"", "");

        try {
            val = Double.parseDouble((String) val);
        } catch (Exception e) {
            try {
                val = Integer.parseInt((String) val);
            } catch (Exception e1) {
                throw new RuntimeException("Invalid data format provided.");
            }
        }

        return val;
    }

    private boolean isDouble(Object value) {
        if (value instanceof Double) {
            return true;
        } else if (value instanceof String) {
            try {
                Integer.parseInt((String) value);
                return false;
            } catch (Exception ignored) {
            }

            try {
                Double.parseDouble((String) value);
                return true;
            } catch (Exception e) {
                return false;
            }
        }

        return false;
    }

    /**
     * Perform an operation on the value stored in the wrapper.
     * @param value Operand
     * @param operation Operation
     */
    private void performOperation(Object value, int operation) {
        Double val1 = Double.parseDouble(String.valueOf(this.parseValue(this.value)).replace("\"", ""));
        Double val2 = Double.parseDouble(String.valueOf(this.parseValue(value)).replace("\"", ""));

        Object result = switch (operation) {
            case 0 -> val1 + val2;
            case 1 -> val1 - val2;
            case 2 -> val1 * val2;
            case 3 -> {
                if (val2 == 0) throw new ArithmeticException("Division by zero.");
                yield val1 / val2;
            }
            default -> throw new RuntimeException("Unsupported operation.");
        };

        if (this.isDouble(this.value) || this.isDouble(value)) {
            this.value = result;
        } else {
            this.value = ((Double) result).intValue();
        }
    }

}
