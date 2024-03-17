package hr.fer.oprpp2.hw01.math;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Class representing a complex number.
 */
public class Complex {

    /**
     * The maximal possible deviation between two complex numbers in comparison.
     */
    private static final double EPSILON = 0.01E-8;

    /**
     * A complex zero.
     */
    public static final Complex ZERO = new Complex(0, 0);

    /**
     * A complex real one.
     */
    public static final Complex ONE = new Complex(1, 0);

    /**
     * A complex real one negative.
     */
    public static final Complex ONE_NEG = new Complex(-1, 0);

    /**
     * A complex imaginary one.
     */
    public static final Complex IM = new Complex(0, 1);

    /**
     * A complex imaginary one negative.
     */
    public static final Complex IM_NEG = new Complex(0, -1);

    private double re;
    private double im;

    /**
     * Creates a complex number equal to zero.
     */
    public Complex() {
        this(0, 0);
    }

    /**
     * Creates a complex number with a real and an imaginary part.
     * @param re A real part of the complex number
     * @param im An imaginary part of the complex number
     */
    public Complex(double re, double im) {
        this.re = re;
        this.im = im;
    }

    /**
     * Returns a module of the complex number (r).
     * @return Value of the module
     */
    public double module() {
        return Math.sqrt(re * re + im * im);
    }

    /**
     * Multiplies two complex numbers.
     * @param c Complex number to multiply with
     * @return Multiplication result
     */
    public Complex multiply(Complex c) {
        Objects.requireNonNull(c, "Complex number cant be null!");

        double tempRe = this.re * c.re - this.im * c.im;
        double tempIm = this.re * c.im + this.im * c.re;

        return new Complex(tempRe, tempIm);
    }

    /**
     * Divides this complex number by a provided complex number.
     * @param c Complex number to divide with
     * @return Division result
     */
    public Complex divide(Complex c) {
        Objects.requireNonNull(c, "Complex number cant be null!");

        double denom = c.re * c.re + c.im * c.im;

        if (denom == 0) {
            throw new ArithmeticException("Division by zero");
        }

        double tempRe = (this.re * c.re + this.im * c.im) / denom;
        double tempIm = (this.im * c.re - this.re * c.im) / denom;

        return new Complex(tempRe, tempIm);
    }

    /**
     * Adds two complex numbers.
     * @param c Complex number to sum with
     * @return Sum result
     */
    public Complex add(Complex c) {
        Objects.requireNonNull(c, "Complex number cant be null!");

        return new Complex(this.re + c.re, this.im + c.im);
    }

    /**
     * Subtracts a provided complex number from this one.
     * @param c Complex number to subtract with
     * @return Subtraction result
     */
    public Complex sub(Complex c) {
        Objects.requireNonNull(c, "Complex number cant be null!");

        return new Complex(this.re - c.re, this.im - c.im);
    }

    /**
     * Negates a complex number.
     * @return A complex number with both real and imaginary parts negated
     */
    public Complex negate() {
        return new Complex(-this.re, -this.im);
    }

    /**
     * Raises this complex number to the provided power.
     * @param n Power to be raised to
     * @return Raised complex number
     */
    public Complex power(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Exponent cant be negative!");
        }

        double m = Math.pow(Math.hypot(this.re, this.im), n);
        double thetaRad = Math.atan2(this.im, this.re);
        double theta = thetaRad < 0 ? thetaRad + 2 * Math.PI : thetaRad;

        return new Complex(m * Math.cos(n * theta), m * Math.sin(n * theta));
    }

    /**
     * Returns an n-th root of this complex number.
     * @param n Roots order
     * @return Rooted complex number
     */
    public List<Complex> root(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Exponent cant be lower than 1!");
        }

        List<Complex> roots = new ArrayList<>();
        double magnitudeRoot = Math.pow(this.module(), 1.0 / n);
        double angleIncrement = 2 * Math.PI / n;

        for (int k = 0; k < n; k++) {
            double angle = k * angleIncrement;
            double newRe = magnitudeRoot * Math.cos(angle);
            double newIm = magnitudeRoot * Math.sin(angle);
            roots.add(new Complex(newRe, newIm));
        }

        return roots;
    }

    /**
     * Returns a complex number argument (angle).
     * @return Angle value
     */
    public double argument() {
        return Math.atan2(im, re);
    }

    /**
     * Returns a string representation of the complex number.
     * @return A string representation of the complex number
     */
    @Override
    public String toString() {
        if (this.im >= 0) {
            return String.format("(%f+i%f)", this.re, this.im);
        } else {
            return String.format("(%f-i%f)", this.re, -this.im);
        }
    }

    /**
     * Getter for the real part of the complex number.
     * @return Real part of the complex number
     */
    public double getReal() {
        return this.re;
    }

    /**
     * Getter for the imaginary part of the complex number.
     * @return Imaginary part of the complex number
     */
    public double getImaginary() {
        return this.im;
    }

    /**
     * Parses a complex number from a given string.
     * @param val String to be parsed
     * @return Parsed complex number
     */
    static public Complex parseComplex(String val) {
        Objects.requireNonNull(val, "Complex number cant be null!");

        // parsing the default values
        switch (val) {
            case "0", "+0", "-0", "i0", "+i0", "-i0", "0+i0", "0-i0", "+0+i0", "+0-i0", "-0+i0", "-0-i0" -> {
                return Complex.ZERO;
            }
            case "1", "+1", "1+i0", "1-i0", "+1+i0", "+1-i0" -> { return Complex.ONE; }
            case "-1", "-1+i0", "-1-i0" -> { return Complex.ONE_NEG; }
            case "i", "+i", "0+i", "+0+i", "-0+i", "0+i1", "+0+i1", "-0+i1" -> { return Complex.IM; }
            case "-i", "0-i", "+0-i", "-0-i", "0-i1", "+0-i1", "-0-i1" -> { return Complex.IM_NEG; }
        }

        // parsing all other cases
        boolean neg = false;

        if (val.startsWith("+")) {
            val = val.substring(1);
        }

        if (val.startsWith("-")) {
            val = val.substring(1);
            neg = true;
        }

        if (val.contains("+") || val.contains("-")) {
            String[] vals;
            double re, im;
            boolean imNeg = false;
            if (val.contains("+")) {
                vals = val.split("\\+");
            } else {
                vals = val.split("-");
                imNeg = true;
            }

            re = Double.parseDouble(vals[0]);
            String imString = vals[1].replace("i", "").replace("-", "");
            if (imString.length() > 0) {
                im = Double.parseDouble(imString);
            } else {
                im = 1;
            }

            if (neg) {
                re = -re;
            }

            if (imNeg) {
                im = -im;
            }

            return new Complex(re, im);
        }

        if (val.contains("i")) {
            if (neg) {
                return new Complex(0, -Double.parseDouble(val.replace("i", "")));
            }

            return new Complex(0, Double.parseDouble(val.replace("i", "")));
        }

        if (val.length() > 0) {
            if (neg) {
                return new Complex(-1.0 * Double.parseDouble(val), 0);
            }

            return new Complex(Double.parseDouble(val), 0);
        }

        throw new IllegalArgumentException("Invalid complex number format!");
    }

    /**
     * Checks whether two complex numbers are equal.
     * @param o Complex number to be compared with
     * @return A boolean representing whether those two complex numbers are equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Complex)) return false;
        Complex complex = (Complex) o;
        return Math.abs(complex.re - this.re) < EPSILON && Math.abs(complex.im - this.im) < EPSILON;
    }

}

