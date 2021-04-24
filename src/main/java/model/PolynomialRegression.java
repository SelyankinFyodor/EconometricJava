package main.java.model;

import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.linear.*;

public class PolynomialRegression {
    private double[] betas;
    private double sigma;
    private double[][] confidenceIntervals;
    private RealMatrix A;
    private RealMatrix Z;
    private RealVector b;

    public double[][] getConfidenceIntervals() {
        return confidenceIntervals;
    }

    public double[] getBetas() {
        return betas.clone();
    }

    public double getSigma() {
        return sigma;
    }

    public RealVector getB(){
        return b;
    }

    public RealMatrix getA(){
        return A;
    }

    public RealMatrix getZ(){
        return Z    ;
    }

    public void fit(double[] T, double[] X) {
        this.fit(T, X, 3, 0, 0.95);
    }

    public void fit(double[] T, double[] X, int degree) {
        this.fit(T, X, degree, 0, 0.95);
    }

    public void fit(double[] T, double[] X, int degree, double regularization) {
        this.fit(T, X, degree, regularization, 0.95);
    }

    public void fit(double[] T, double[] X, int degree, double regularization, double confidence) {
        if (T.length != X.length) {
            return;
        }
        int n = X.length;

        RealVector vectorX = MatrixUtils.createRealVector(X);
        Z = this.buildZ(T,degree + 1);
        A = Z.multiply(Z.transpose());
        b = Z.transpose().preMultiply(vectorX);

        if (regularization != 0) {
            RealMatrix alphaE = MatrixUtils.createRealIdentityMatrix(degree + 1).scalarMultiply(regularization);
            A = A.add(alphaE);
        }

        betas = new SingularValueDecomposition(A).getSolver().solve(b).toArray();

        double mse = 0;
        for (int i = 0; i < n; i++) {
            double d = X[i] - getValue(T[i]);
            mse += d * d;
        }
        this.sigma = Math.sqrt(mse / (n - degree - 1));

        TDistribution tDistribution = new TDistribution(n - (degree + 1));
        double L = tDistribution.inverseCumulativeProbability((1 - confidence) / 2);
        double U = tDistribution.inverseCumulativeProbability((1 + confidence) / 2);
        double[][] AInv = MatrixUtils.inverse(A).getData();

        confidenceIntervals = new double[degree + 1][2];
        for (int i = 0; i < degree + 1; i++) {
            double k = sigma * Math.sqrt(AInv[i][i]);
            confidenceIntervals[i][0] = betas[i] + L * k;
            confidenceIntervals[i][1] = betas[i] + U * k;
        }
    }

    public RealMatrix buildZ(double[] T, int k) {
        double[][] Z = new double[k][T.length];

        for (int i = 0; i < T.length; i++) {
            Z[0][i] = 1;
            for (int j = 1; j < k; j++) {
                Z[j][i] = T[i] * Z[j - 1][i];
            }
        }

        return MatrixUtils.createRealMatrix(Z);
    }

    public double getValue(double t) {
        double value = 0;
        double pow = 1;

        for (double beta : betas) {
            value += beta * pow;
            pow *= t;
        }

        return value;
    }
}
