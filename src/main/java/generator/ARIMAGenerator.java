package main.java.generator;

import org.apache.commons.math3.distribution.NormalDistribution;

public class ARIMAGenerator {
    double[] a;
    double[] c;
    int p;
    int q;
    int d;
    double sigma;
    NormalDistribution nd;

    public ARIMAGenerator() {
    }

    public ARIMAGenerator(double[] a, double[] c, double sigma, int p, int q, int d) {
        this.a = a.clone();
        this.c = c.clone();
        this.nd = new NormalDistribution(0, sigma);
        this.p = p;
        this.q = q;
        this.d = d;
    }

    public double[] generate(int N) {
        double[] X = new double[N];
        double[] eps = new double[N];

        for (int i = 0; i < N; i++) {
            eps[i] = nd.sample();
            for (int j = 1; j <= this.p; j++) {
                if (i < j) {
                    break;
                }
                X[i] += this.a[j - 1] * X[i - j];
            }
            X[i] += eps[i];
            for (int j = 1; j <= this.q; j++) {
                if (i < j) {
                    break;
                }
                X[i] += this.c[j - 1] * eps[i - j];
            }
        }

        for (int i = 0; i < this.d; i++) {
            X = integrate(X);
        }
        return X;
    }

    double[] integrate(double[] x) {
        double[] newX = new double[x.length];
        for (int i = 1; i < x.length; i++) {
            newX[i] = newX[i - 1] + x[i];
        }
        return newX;
    }
}
