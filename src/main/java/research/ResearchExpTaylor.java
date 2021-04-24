package main.java.research;

import main.java.model.PolynomialRegression;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.util.CombinatoricsUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class ResearchExpTaylor {
    private static final String SPACE = " ";
    private static final int taylorDegree = 4;
    private static final int maxRegularization = 500;
    private static final double regStep = 0.01;

    public static void researchOnRegularizationParam(String inFilename) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(inFilename))) {
            int N = Integer.decode(bufferedReader.readLine());
            int chunkSize = 50;
            for (int chunk = 0, chunks = N / chunkSize; chunk < chunks; chunk++) {
                double minNorm = -1.;
                double bestRegNorm = 0.;
                double bestSigma = -1.;
                double bestRegSigma = 0.;

                double[] T = new double[chunkSize];
                double[] X = new double[chunkSize];

                for (int j = 0; j < chunkSize; j++) {
                    String[] dots = bufferedReader.readLine().split(SPACE);
                    T[j] = Double.parseDouble(dots[0]);
                    X[j] = Double.parseDouble(dots[1]);
                }

                PolynomialRegression polyRegr = new PolynomialRegression();

                for (double regularization = 0; regularization < maxRegularization; regularization += regStep) {
                    polyRegr.fit(T, X, taylorDegree, regularization);

                    double norm = polyRegr.getZ().transpose().preMultiply(polyRegr.getZ())
                            .preMultiply(MatrixUtils.createRealVector(polyRegr.getBetas()))
                            .subtract(polyRegr.getZ().transpose().preMultiply(MatrixUtils.createRealVector(X)))
                            .getNorm();

                    if (minNorm < 0 || norm < minNorm) {
                        bestRegNorm = regularization;
                        minNorm = norm;
                    }
                    if (bestSigma < 0 || polyRegr.getSigma() < bestSigma) {
                        bestRegSigma = regularization;
                        bestSigma = polyRegr.getSigma();
                    }
                }
                System.out.println("interval: [" + T[0] + ", " + T[chunkSize - 1] + "]");
                System.out.println("\tbestRegNorm:" + bestRegNorm);
                System.out.println("\tnorm:" + minNorm);
                System.out.println("\tbestRegNorm:" + bestRegSigma);
                System.out.println("\tsigma:" + bestSigma);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void researchBetas(String inFilename, double regularization) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(inFilename))) {
            int N = Integer.decode(bufferedReader.readLine());
            int chunkSize = 50;
            for (int chunk = 0, chunks = N / chunkSize; chunk < chunks; chunk++) {
                double[] T = new double[chunkSize];
                double[] X = new double[chunkSize];

                for (int j = 0; j < chunkSize; j++) {
                    String[] dots = bufferedReader.readLine().split(SPACE);
                    T[j] = Double.parseDouble(dots[0]);
                    X[j] = Double.parseDouble(dots[1]);
                }

                PolynomialRegression polyRegr = new PolynomialRegression();
                polyRegr.fit(T, X, taylorDegree, regularization);
                double[] gammas = polyRegr.getBetas();
                double[][] confidenceIntervals = polyRegr.getConfidenceIntervals();
                System.out.println("interval: [" + T[0] + ", " + T[chunkSize - 1] + "]\tsigma: " + polyRegr.getSigma());
                for (int i = 0; i < gammas.length; i++) {
                    System.out.println("\tgamma" + i + ": " + gammas[i] + "\n"
                            + "\t\tconfidence interval: [" + confidenceIntervals[i][0] + ", " + confidenceIntervals[i][1] + "]");
                }
                for (double beta : restoreBetas(polyRegr.getBetas(), 4)) {
                    System.out.println(beta);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static double[] restoreBetas(double[] betas, int taylorDegree) {
        double[] restoredBetas = new double[taylorDegree + 1];
        restoredBetas[0] = betas[0];
        for (int i = 1; i <= taylorDegree; i++) {
            double nFact = (double) CombinatoricsUtils.factorial(i);
            restoredBetas[i] = (i % 2 == 0 ? 1: -1) * Math.pow(nFact * Math.abs(betas[i] / betas[0]), (1. / i));
        }
        return restoredBetas;
    }

    public static double getTaylorValue(double[] betas, double t) {
        double value = 0;
        double pow = 1;

        for (double beta : betas) {
            value += beta * pow;
            pow *= t;
        }

        return value;
    }

    public static double getExpoValue(double beta0, double beta1, double t) {
        return beta0 * Math.exp(beta1 * t);
    }
}
