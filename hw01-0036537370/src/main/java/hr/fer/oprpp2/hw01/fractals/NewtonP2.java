package hr.fer.oprpp2.hw01.fractals;

import hr.fer.oprpp2.hw01.math.Complex;
import hr.fer.oprpp2.hw01.math.ComplexPolynomial;
import hr.fer.oprpp2.hw01.math.ComplexRootedPolynomial;
import hr.fer.zemris.java.fractals.viewer.FractalViewer;
import hr.fer.zemris.java.fractals.viewer.IFractalProducer;
import hr.fer.zemris.java.fractals.viewer.IFractalResultObserver;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A class for the Newton-Ralph fractal generation using ForkJoinPool.
 */
public class NewtonP2 {

    /**
     * Maximum iterations for the fractal calculation (more = slower, but more detailed fractal).
     */
    private static final int MAX_ITERATIONS = 16 * 16;

    /**
     * A main program for user input and fractal generation.
     * @param args One optional argument can be accepted, a minimal number of tracks (--mintracks k or -m k).
     */
    public static void main(String[] args) {
        int mintracks = 16;
        boolean isMintracksPresent = false;

        for (int i = 0; i < args.length; i++) {
            if (args[i].toLowerCase().startsWith("--mintracks") || args[i].toLowerCase().startsWith("-m")) {
                if (isMintracksPresent) throw new IllegalArgumentException("Minimal number of tracks can be " +
                        "specified only once!");
                isMintracksPresent = true;
                try {
                    if (args[i].toLowerCase().startsWith("--workers")) {
                        mintracks = Integer.parseInt(args[i].split("=")[1]);
                    } else {
                        mintracks = Integer.parseInt(args[++i]);
                    }

                    if (mintracks < 1) {
                        throw new IllegalArgumentException("Minimal number of tracks cant be lower than 1!");
                    }
                } catch (Exception e) {
                    throw new IllegalArgumentException("Invalid workers number!");
                }
            }
        }

        int count = 1;
        String val;
        Scanner in = new Scanner(System.in);
        List<Complex> roots = new ArrayList<>();

        System.out.println("Welcome to Newton-Raphson iteration-based fractal viewer.\nPlease enter at least two " +
                "roots, one root per line. Enter 'done' when done.");

        while (true) {
            System.out.print("Root " + count + "> ");
            val = in.nextLine().trim();

            if (val.equals("done")) {
                if (count < 3) {
                    System.out.println("At least two roots must be provided!");
                    continue;
                }

                System.out.println("Image of fractal will appear shortly. Thank you.");
                break;
            }

            if (val.trim().equals("")) {
                System.out.println("A non-empty complex number must be provided!");
                continue;
            }

            try {
                roots.add(Complex.parseComplex(val));
                // System.out.println(Complex.parseComplex(val));
            } catch (Exception e) {
                System.out.println("A valid complex number must be provided!");
                continue;
            }

            count++;
        }

        FractalViewer.show(new NewtonP2.NewtonFractalProducer(new ComplexRootedPolynomial(Complex.ONE,
                roots.toArray(Complex[]::new)), mintracks));
    }

    /**
     * Class representing a job for Newton fractal calculation using ExecutorService.
     */
    private static class NewtonFractalJob extends RecursiveAction {

        double reMin, reMax, imMin, imMax;
        int width, height;
        int yMin, yMax, m, mintracks;
        short[] data;
        ComplexRootedPolynomial rootedPolynomial;
        ComplexPolynomial polynomial;

        /**
         * Creates a new job with parameters required for fractal generation.
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
         * @param mintracks Minimal number of tracks for recursive decomposition
         */
        public NewtonFractalJob(double reMin, double reMax, double imMin, double imMax, int width, int height,
                                int yMin, int yMax, int m, short[] data, ComplexRootedPolynomial rootedPolynomial,
                                ComplexPolynomial polynomial, int mintracks) {
            this.reMin = reMin;
            this.reMax = reMax;
            this.imMin = imMin;
            this.imMax = imMax;
            this.width = width;
            this.height = height;
            this.yMin = yMin;
            this.yMax = yMax;
            this.m = m;
            this.data = data;
            this.rootedPolynomial = rootedPolynomial;
            this.polynomial = polynomial;
            this.mintracks = mintracks;
        }

        /**
         * The main computation performed by this task.
         */
        @Override
        protected void compute() {
            if (yMax - yMin <= mintracks) {
                Newton.calculate(reMin, reMax, imMin, imMax, width, height, yMin, yMax, m, data,
                        rootedPolynomial, polynomial);
                return;
            }

            int middle = (yMin + yMax) / 2;

            NewtonFractalJob lower = new NewtonFractalJob(reMin, reMax, imMin, imMax, width, height, yMin, middle, m,
                    data, rootedPolynomial, polynomial, mintracks);
            NewtonFractalJob higher = new NewtonFractalJob(reMin, reMax, imMin, imMax, width, height, middle + 1,
                    yMax, m, data, rootedPolynomial, polynomial, mintracks);

            invokeAll(lower, higher);
        }

    }

    /**
     * Class representing a fractal producer using ForkJoinPool.
     */
    private static class NewtonFractalProducer implements IFractalProducer {

        ForkJoinPool forkJoinPool;
        ComplexRootedPolynomial rootedPolynomial;
        ComplexPolynomial polynomial;
        int mintracks;

        /**
         * Creates a new fractal producer with a given complex rooted polynomial, number of workers and a number of
         * tracks.
         * @param rootedPolynomial Complex rooted polynomial for the fractal producer
         * @param mintracks Minimal number of tracks
         */
        public NewtonFractalProducer(ComplexRootedPolynomial rootedPolynomial, int mintracks) {
            this.rootedPolynomial = rootedPolynomial;
            this.polynomial = rootedPolynomial.toComplexPolynom();
            this.mintracks = mintracks;
        }

        /**
         * Function to initialize the Newton fractal producer with a new thread pool.
         */
        @Override
        public void setup() {
            this.forkJoinPool = new ForkJoinPool();
        }

        /**
         * Function for producing the Newton-Ralph fractal.
         * @param reMin Real number min value
         * @param reMax Real number max value
         * @param imMin Imaginary number min value
         * @param imMax Imaginary number max value
         * @param width Image width
         * @param height Image height
         * @param l Request number
         * @param iFractalResultObserver Observer for the fractal visualization
         * @param atomicBoolean Cancellation boolean
         */
        @Override
        public void produce(double reMin, double reMax, double imMin, double imMax, int width, int height, long l,
                            IFractalResultObserver iFractalResultObserver, AtomicBoolean atomicBoolean) {
            short[] data = new short[width * height];

            forkJoinPool.invoke(new NewtonFractalJob(reMin, reMax, imMin, imMax, width, height, 0, height - 1,
                    MAX_ITERATIONS, data, rootedPolynomial, polynomial, mintracks));

            iFractalResultObserver.acceptResult(data, (short) (this.polynomial.order() + 1), l);
        }

        /**
         * Function for destructing the Newton fractal producer.
         */
        @Override
        public void close() {
            this.forkJoinPool.shutdown();
        }

    }

}
