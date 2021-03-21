package main.java.model;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;

public class PolynomialRegression {
    private double[] betas;
    private double sigma;

    public double[] getBetas() {
        return betas.clone();
    }

    public double getSigma() {
        return sigma;
    }

    public void fit(double[] T, double[] X) {
        this.fit(T, X, 3);
    }

    public void fit(double[] T, double[] X, int degree) {
        if (T.length != X.length) {
            return;
        }
        int n = X.length;

        RealVector vectorX = MatrixUtils.createRealVector(X);
        RealMatrix matrixZ = this.buildZ(T, X.length, degree + 1);
        RealMatrix matrixA = matrixZ.multiply(matrixZ.transpose());
        RealVector vectorB = matrixZ.transpose().preMultiply(vectorX);

        this.betas = new SingularValueDecomposition(matrixA).getSolver().solve(vectorB).toArray();

        double mse = 0;
        for (int i = 0; i < n; i++) {
            double d = X[i] - getValue(T[i]);
            mse += d * (X[i] - getValue(T[i]));
        }
        this.sigma = Math.sqrt(mse / (n - 1));
    }

    private RealMatrix buildZ(double[] T, int n, int k) {
        double[][] Z = new double[k][n];

        for (int i = 0; i < n; i++) {
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
