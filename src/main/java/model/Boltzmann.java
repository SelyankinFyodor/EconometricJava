package main.java.model;

import org.apache.commons.math3.distribution.MultivariateNormalDistribution;

public class Boltzmann extends Annealing {

    public Boltzmann (double[] T, double[] X, double[][] compactBorders) {
        super(T, X, compactBorders);
    }

    @Override
    double calcT(double startT, int i) {
         return startT / Math.log(1 + i);
    }

    @Override
    double[] newX(double[] x, double T) {
        return new MultivariateNormalDistribution(x, new double[][]{{T, 0}, {0, T}}).sample();
    }
}
