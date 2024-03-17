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
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A class for the Newton-Ralph fractal generation using Executors API.
 */
public class NewtonP1 {

    /**
     * Maximum iterations for the fractal calculation (more = slower, but more detailed fractal).
     */
    private static final int MAX_ITERATIONS = 16 * 16;

    /**
     * A main program for user input and fractal generation.
     * @param args Two optional arguments can be accepted, number of workers (--workers n or -w n) and a number of
     *             tracks (--tracks n or -t n).
     */
    public static void main(String[] args) {
        int workers = Runtime.getRuntime().availableProcessors();
        int tracks = 4 * workers;
        boolean isWorkersPresent = false, isTracksPresent = false;

        for (int i = 0; i < args.length; i++) {
            if (args[i].toLowerCase().startsWith("--workers") || args[i].toLowerCase().startsWith("-w")) {
                if (isWorkersPresent) throw new IllegalArgumentException("Number of workers can be specified only " +
                        "once!");
                isWorkersPresent = true;
                try {
                    if (args[i].toLowerCase().startsWith("--workers")) {
                        workers = Integer.parseInt(args[i].split("=")[1]);
                    } else {
                        workers = Integer.parseInt(args[++i]);
                    }

                    if (workers < 1) {
                        throw new IllegalArgumentException("Number of workers cant be lower than 1!");
                    }
                } catch (Exception e) {
                    throw new IllegalArgumentException("Invalid workers number!");
                }
            }

            if (args[i].toLowerCase().startsWith("--tracks") || args[i].toLowerCase().startsWith("-t")) {
                if (isTracksPresent) throw new IllegalArgumentException("Number of tracks can be specified only " +
                        "once!");
                isTracksPresent = true;
                try {
                    if (args[i].toLowerCase().startsWith("--tracks")) {
                        tracks = Integer.parseInt(args[i].split("=")[1]);
                    } else {
                        tracks = Integer.parseInt(args[++i]);
                    }

                    if (tracks < 1) {
                        throw new IllegalArgumentException("Number of tracks cant be lower than 1!");
                    }
                } catch (Exception e) {
                    throw new IllegalArgumentException("Invalid tracks number!");
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

        FractalViewer.show(new NewtonP1.NewtonFractalProducer(new ComplexRootedPolynomial(Complex.ONE,
                roots.toArray(Complex[]::new)), workers, tracks));
    }

    /**
     * Class representing a job for Newton fractal calculation using ExecutorService.
     */
    private static class NewtonFractalJob implements Runnable {

        double reMin, reMax, imMin, imMax;
        int width, height;
        int yMin, yMax, m;
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
         */
        public NewtonFractalJob(double reMin, double reMax, double imMin, double imMax, int width, int height,
                                int yMin, int yMax, int m, short[] data, ComplexRootedPolynomial rootedPolynomial,
                                ComplexPolynomial polynomial) {
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
        }

        /**
         * When an object implementing interface {@code Runnable} is used
         * to create a thread, starting the thread causes the object's
         * {@code run} method to be called in that separately executing
         * thread.
         * <p>
         * The general contract of the method {@code run} is that it may
         * take any action whatsoever.
         *
         * @see Thread#run()
         */
        @Override
        public void run() {
            Newton.calculate(reMin, reMax, imMin, imMax, width, height, yMin, yMax, m, data,
                    rootedPolynomial, polynomial);
        }

    }

    /**
     * Class representing a fractal producer.
     */
    private static class NewtonFractalProducer implements IFractalProducer {

        ExecutorService executorService;
        ComplexRootedPolynomial rootedPolynomial;
        ComplexPolynomial polynomial;
        int workers, tracks;

        /**
         * Creates a new fractal producer with a given complex rooted polynomial, number of workers and a number of
         * tracks.
         * @param rootedPolynomial Complex rooted polynomial for the fractal producer
         * @param workers Number of workers
         * @param tracks Number of tracks (= image height if too big)
         */
        public NewtonFractalProducer(ComplexRootedPolynomial rootedPolynomial, int workers, int tracks) {
            this.rootedPolynomial = rootedPolynomial;
            this.polynomial = rootedPolynomial.toComplexPolynom();
            this.workers = workers;
            this.tracks = tracks;
        }

        /**
         * Function to initialize the Newton fractal producer with a new thread pool.
         */
        @Override
        public void setup() {
            int workers = Math.min(this.workers, Runtime.getRuntime().availableProcessors());
            System.out.println("Number of workers: " + workers);

            this.executorService = Executors.newFixedThreadPool(workers);
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
            List<Future<?>> futures = new ArrayList<>();

            int tracks = Math.min(this.tracks, height);
            int trackHeight = height / tracks;

            System.out.println("Number of tracks: " + tracks);

            for (int i = 0; i < tracks; i++) {
                int yMin = i * trackHeight;
                int yMax = (i + 1) * trackHeight - 1;

                if (i == tracks - 1) {
                    yMax = height - 1;
                }

                NewtonFractalJob job = new NewtonFractalJob(
                        reMin, reMax, imMin, imMax, width, height, yMin, yMax, MAX_ITERATIONS, data,
                        rootedPolynomial, polynomial
                );

                futures.add(this.executorService.submit(job));
            }

            futures.forEach(future -> {
                try {
                    future.get();
                } catch (InterruptedException | ExecutionException e) {
                    System.out.println("Exception inside of job: ");
                    e.printStackTrace();
                }
            });

            iFractalResultObserver.acceptResult(data, (short) (this.polynomial.order() + 1), l);
        }

        /**
         * Function for destructing the Newton fractal producer.
         */
        @Override
        public void close() {
            this.executorService.shutdown();
        }

    }

}
