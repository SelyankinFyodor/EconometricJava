package main.java.model;

import main.java.main.ExpDataGenerator;
import org.apache.commons.math3.distribution.UniformRealDistribution;

import java.util.ArrayList;


abstract public class Annealing {
    protected double[] T;
    protected double[] X;
    protected double[][] compactBorders;
    protected ArrayList<Integer> iterations;
    protected ArrayList<Double> sigmas;
    protected ArrayList<Double> temperatures;

    public ArrayList<Integer> getIterations() {
        return iterations;
    }

    public ArrayList<Double> getSigma(){
        return sigmas;
    }

    public ArrayList<Double> getTemperatures() {
        return temperatures;
    }

    public Annealing(double[] T, double[] X, double[][] compactBorders) {
        this.T = T;
        this.X = X;
        this.compactBorders = compactBorders;
        this.iterations = new ArrayList<>();
        this.sigmas = new ArrayList<>();
        this.temperatures = new ArrayList<>();
    }

    abstract double calcT(double startT, int i);

    double calcE(double[] x) {
        double S = 0;
        double expInPoint;
        for (int i = 0; i < T.length; i++) {
            expInPoint = ExpDataGenerator.exprVal(x, T[i]);
            S += Math.pow((X[i] - expInPoint), 2);
        }
        return S;
    }

    abstract double[] newX(double[] x, double T);

    double h(double dE, double T) {
        return 1 / (1 + Math.exp(dE / T));
    }

    boolean inComplact(double[] x) {
        for (int i = 0; i < x.length; i++) {
            if (x[i] < compactBorders[i][0] && x[i] > compactBorders[i][1]) {
                return false;
            }
        }
        return true;
    }

    public double[] fit(double[] startPoint, int iterationStep, double startT, double tolerance, int madxIterations) {
        double T = startT;
        double[] globalMinPoint = startPoint;
        double globalMinE = calcE(startPoint);
        double E = globalMinE;
        double[] x = startPoint, xNew;
        double Enew;
        double alpha;
        int i = 1;
        iterations.clear();
        sigmas.clear();
        while (T > tolerance && madxIterations > i) {
            if (E < globalMinE) {
                globalMinE = E;
                globalMinPoint = x;
            }
            do {
                xNew = newX(x, T);
            } while (!inComplact(xNew));

            Enew = calcE(xNew);
            alpha = new UniformRealDistribution().sample();

            if (alpha < h(Enew - E, T)) {
                x = xNew;
                E = Enew;
            }

            T = calcT(startT, i);
            i++;

            if (i % iterationStep == 0) {
                double sigma = 0;
                for (int j = 0; j < this.T.length; j++) {
                    double val = ExpDataGenerator.exprVal(x, this.T[j]);
                    sigma += (X[j] - val) * (X[j] - val);
                }
                sigma = Math.sqrt(sigma / (this.T.length));
                iterations.add(i);
                sigmas.add(sigma);
                temperatures.add(T);
            }
        }

        return globalMinPoint;
    }
}
