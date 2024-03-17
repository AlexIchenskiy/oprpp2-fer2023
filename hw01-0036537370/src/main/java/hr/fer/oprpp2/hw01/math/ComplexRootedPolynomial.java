package hr.fer.oprpp2.hw01.math;

import java.util.Arrays;

/**
 * Class representing a complex rooted polynomial.
 */
public class ComplexRootedPolynomial {

    private Complex constant;
    private Complex[] roots;

    /**
     * Creates a complex rooted polynomial with a constant and roots.
     * @param constant A constant of the polynomial
     * @param roots Roots of the polynomial
     */
    public ComplexRootedPolynomial(Complex constant, Complex... roots) {
        this.constant = constant;
        this.roots = Arrays.copyOf(roots, roots.length);
    }

    /**
     * Applies a complex number to the polynomial.
     * @param z A complex number to be applied
     * @return Applying result
     */
    public Complex apply(Complex z) {
        Complex result = this.constant;

        for (Complex root : this.roots) {
            result = result.multiply(z.sub(root));
        }

        return result;
    }

    /**
     * Converts this polynomial to the complex polynomial
     * @return A complex polynomial
     */
    public ComplexPolynomial toComplexPolynom() {
        ComplexPolynomial result = new ComplexPolynomial(this.constant);

        for (Complex root : this.roots) {
            result = result.multiply(new ComplexPolynomial(root.negate(), Complex.ONE));
        }

        return result;
    }

    /**
     * Returns a string representation of the complex rooted polynomial.
     * @return A string representation of the complex rooted polynomial
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.constant);

        for (Complex root : this.roots) {
            sb.append(" * (z - ").append(root).append(")");
        }

        return sb.toString();
    }

    /**
     * Returns the index of the closest root for the provided complex number calculated using the given threshold.
     * @param z Complex number for comparison
     * @param threshold Comparison threshold
     * @return The closes root (-1 if none)
     */
    public int indexOfClosestRootFor(Complex z, double threshold) {
        int closestIndex = -1;
        double closestDistance = z.sub(this.roots[0]).module();

        for (int i = 1; i < this.roots.length; i++) {
            double distance = z.sub(this.roots[i]).module();

            if (distance < threshold && distance < closestDistance) {
                closestIndex = i;
                closestDistance = distance;
            }
        }

        return closestIndex;
    }

//    public static void main(String[] args) {
//        ComplexRootedPolynomial crp = new ComplexRootedPolynomial( new Complex(2,0), Complex.ONE,
//                Complex.ONE_NEG, Complex.IM, Complex.IM_NEG );
//        ComplexPolynomial cp = crp.toComplexPolynom();
//        System.out.println(crp);
//        System.out.println(cp);
//        System.out.println(cp.derive());
//        System.out.println(cp.apply(new Complex(1, 0)));
//    }

}
