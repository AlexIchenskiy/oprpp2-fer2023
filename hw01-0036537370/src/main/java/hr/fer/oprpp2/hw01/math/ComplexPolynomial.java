package hr.fer.oprpp2.hw01.math;

import java.util.Arrays;
import java.util.Objects;

/**
 * Class representing a complex polynomial.
 */
public class ComplexPolynomial {

    private Complex[] factors;

    /**
     * Creates a complex polynomial from given complex numbers (z0, z1, ..., zn).
     * @param factors Complex factors of the polynomial
     */
    public ComplexPolynomial(Complex... factors) {
        Objects.requireNonNull(factors, "Factors cant be null!");
        for (Complex factor : factors) Objects.requireNonNull(factor, "Factor cant be null!");

        this.factors = Arrays.copyOf(factors, factors.length);
    }

    /**
     * Returns an order of the polynomial.
     * @return Order of the polynomial
     */
    public short order() {
        return (short) (this.factors.length - 1);
    }

    /**
     * Multiplies this polynomial with the provided one.
     * @param p Complex polynomial to be multiplied with
     * @return Multiplication result
     */
    public ComplexPolynomial multiply(ComplexPolynomial p) {
        Objects.requireNonNull(p, "Complex polynomial cant be null!");

        int resultLength = this.order() + p.order() + 1;
        Complex[] resultFactors = new Complex[resultLength];

        for (int i = 0; i < resultLength; i++) {
            resultFactors[i] = Complex.ZERO;
        }

        for (int i = 0; i <= this.order(); i++) {
            for (int j = 0; j <= p.order(); j++) {
                resultFactors[i + j] = resultFactors[i + j].add(this.factors[i].multiply(p.factors[j]));
            }
        }

        return new ComplexPolynomial(resultFactors);
    }

    /**
     * Derives this complex polynomial.
     * @return Derivation result
     */
    public ComplexPolynomial derive() {
        if (this.factors.length == 1) {
            return new ComplexPolynomial(Complex.ZERO);
        }

        int resultLength = this.order();
        Complex[] resultFactors = new Complex[resultLength];

        for (int i = 0; i < resultLength; i++) {
            resultFactors[i] = this.factors[i + 1].multiply(new Complex(i + 1, 0));
        }

        return new ComplexPolynomial(resultFactors);
    }

    /**
     * Applies a complex number value to the complex polynomial (effectively a value of the polynomial in some point).
     * @param z Complex number to be applied
     * @return Applying result
     */
    public Complex apply(Complex z) {
        Complex result = this.factors[0];

        for (int i = 1; i <= this.order(); i++) {
            result = result.add(this.factors[i].multiply(z.power(i)));
        }

        return result;
    }

    /**
     * Returns a string representation of a complex polynomial.
     * @return A string representation of a complex polynomial
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int i = this.order(); i >= 0; i--) {
            if (sb.length() > 0) {
                sb.append(" + ");
            }

            sb.append(this.factors[i]);

            if (i > 0) {
                sb.append(" * z");
                if (i > 1) {
                    sb.append("^").append(i);
                }
            }
        }

        return sb.toString();
    }

}