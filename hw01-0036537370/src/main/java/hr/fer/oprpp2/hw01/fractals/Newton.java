package hr.fer.oprpp2.hw01.fractals;

import hr.fer.oprpp2.hw01.math.Complex;
import hr.fer.oprpp2.hw01.math.ComplexPolynomial;
import hr.fer.oprpp2.hw01.math.ComplexRootedPolynomial;

/**
 * Class representing a calculation job for the fractal calculation.
 */
public class Newton {

    /**
     * Fractal convergence threshold.
     */
    private static final double CONVERGENCE_THRESHOLD = 0.001;

    /**
     * Fractal root threshold.
     */
    private static final double ROOT_THRESHOLD = 0.002;

    /**
     * Creates a job with provided arguments.
     * @param reMin Real number min value
     * @param reMax Real number max value
     * @param imMin Imaginary number min value
     * @param imMax Imaginary number max value
     * @param width Image width
     * @param height Image height
     * @param yMin Height start
     * @param yMax Height finish
     * @param m Max iterations
     * @param data Data array
     * @param rootedPolynomial A complex rooted polynomial
     * @param polynomial A complex polynomial
     */
    public static void calculate(double reMin, double reMax, double imMin, double imMax, int width, int height,
                                 int yMin, int yMax, int m, short[] data, ComplexRootedPolynomial rootedPolynomial,
                    ComplexPolynomial polynomial) {
        int offset = yMin * width;

        for (int y = yMin; y <= yMax; y++) {
            for (int x = 0; x < width; x++) {
                Complex zn = new Complex(x / (width - 1.0) * (reMax - reMin) + reMin,
                        (height - 1.0 - y) / (height - 1.0) * (imMax - imMin) + imMin);
                Complex znold;
                int iter = 0;

                do {
                    znold = zn;
                    zn = zn.sub(polynomial.apply(zn).divide(polynomial.derive().apply(zn)));
                    iter++;
                } while (zn.sub(znold).module() > CONVERGENCE_THRESHOLD && iter < m);

                data[offset++] = (short)(rootedPolynomial.indexOfClosestRootFor(zn, ROOT_THRESHOLD) + 1);
            }
        }
    }

}
