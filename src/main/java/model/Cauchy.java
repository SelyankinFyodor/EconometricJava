package main.java.model;

import org.apache.commons.math3.distribution.CauchyDistribution;

import java.util.function.DoubleBinaryOperator;

public class Cauchy extends Annealing {
    public Cauchy(double[] T, double[] X, double[][] compactBorders) {
        super(T, X, compactBorders);
    }

    @Override
    double calcT(double startT, int i) {
        return startT / i;
    }

    @Override
    double[] newX(double[] x, double T) {
        return new double[]{
                new CauchyDistribution(x[0], T).sample(),
                new CauchyDistribution(x[1], T).sample()
        };
    }
}
