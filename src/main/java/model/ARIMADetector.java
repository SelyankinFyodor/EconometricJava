package main.java.model;

import main.java.writer.GenerationWriter;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;

import java.util.ArrayList;
import java.util.Arrays;

public class ARIMADetector {
    double var;
    int d;

    private static class resultModel {
        double[] a;
        double[] c;
        double sigma;
        double[] lastQErrors;
        double[] lastPValues;

        resultModel(double[] a, double[] c, double[] lastPErrors, double[] lastPValues, double sigma) {
            this.lastQErrors = lastPErrors;
            this.lastPValues = lastPValues;
            this.a = a;
            this.c = c;
            this.sigma = sigma;
        }
    }

    private static class ARIMAParams {
        int d;
        resultModel resultModel;

        ARIMAParams(resultModel resultModel, int d) {
            this.resultModel = resultModel;
            this.d = d;
        }
    }

    double[] differentiate(double[] x) {
        double[] newX = new double[x.length - 1];
        for (int i = 0; i < newX.length; i++) {
            newX[i] = x[i + 1] - x[i];
        }
        return newX;
    }

    double[] integrate(double[] x, double x0) {
        double[] newX = new double[x.length];
        newX[0] = x0 + x[0];
        for (int i = 1; i < x.length; i++) {
            newX[i] = newX[i - 1] + x[i];
        }
        return newX;
    }

    resultModel findAR(double[] sample, int p) {
        int n = sample.length;

        double[][] zData = new double[p][n - 1];
        double[] X = new double[n - 1];
        for (int t = 0; t < n - 1; t++) {
            X[t] = sample[n - 1 - t];
            for (int i = 1; i <= p; i++) {
                zData[i - 1][t] = (n - t - i - 1 > 0 ? sample[n - t - i - 1] : 0);
            }
        }

        RealMatrix Z = MatrixUtils.createRealMatrix(zData);
        RealMatrix A = Z.multiply(Z.transpose());
        RealVector b = Z.operate(MatrixUtils.createRealVector(X));
        SingularValueDecomposition svd = new SingularValueDecomposition(A);

        return new resultModel(svd.getSolver().solve(b).toArray(), null, null, Arrays.copyOfRange(sample, n - p, n), this.var);
    }

    public resultModel findAC(double[] sample, int p, int q) {
        int n = sample.length;
        double cStep = 0.1;
        resultModel result = new resultModel(null, null, null, null, Double.MAX_VALUE);

        for (double c0 = 0; c0 < 1; c0 += cStep) {
            for (double c1 = 0; c1 < 1; c1 += cStep) {
                for (double c2 = 0; c2 < 1; c2 += cStep) {
                    double[] cCur = {c0, c1, c2};

                    double[] zProcess = new double[n];
                    for (int i = 0; i < n; i++) {
                        zProcess[i] = sample[i];
                        for (int j = 0; j < q && j < i; j++) {
                            zProcess[i] -= cCur[j] * zProcess[i - j - 1];
                        }
                    }

                    double[][] zData = new double[p][n - 1];
                    double[] X = new double[n - 1];
                    for (int t = 0; t < n - 1; t++) {
                        X[t] = zProcess[n - 1 - t];
                        for (int i = 1; i <= p; i++) {
                            zData[i - 1][t] = (n - t - i - 1 > 0 ? zProcess[n - t - i - 1] : 0);
                        }
                    }

                    RealMatrix Z = MatrixUtils.createRealMatrix(zData);
                    RealMatrix A = Z.multiply(Z.transpose());
                    RealVector b = Z.operate(MatrixUtils.createRealVector(X));

                    double[] aCur = new SingularValueDecomposition(A).getSolver().solve(b).toArray();

                    double[] errors = new double[n];
                    double var = 0;
                    for (int i = 0; i < n; i++) {
                        errors[i] = sample[i] - aCur[0];
                        for (int j = 1; j <= i && j < p; j++) {
                            errors[i] -= aCur[j] * sample[i - j];
                        }
                        for (int j = 0; j < i && j < q; j++) {
                            errors[i] -= cCur[j] * errors[i - j - 1];
                        }
                        var += errors[i] * errors[i];
                    }

                    var /= (sample.length - p);
                    if (var < result.sigma) {
                        result.sigma = Math.sqrt(var);
                        result.c = Arrays.copyOfRange(cCur, 0, q);
                        result.a = aCur;
                        result.lastQErrors = Arrays.copyOfRange(errors, n - q, n);
                        result.lastPValues = Arrays.copyOfRange(sample, n - p, n);

                    }
                }
            }
        }
        return result;
    }

    boolean testDW(double[] sample) {
        double numerator = 0;
        double denominator = sample[0] * sample[0];


        for (int i = 1; i < sample.length; i++) {
            numerator += (sample[i] - sample[i - 1]) * (sample[i] - sample[i - 1]);

            denominator += sample[i] * sample[i];
        }
        double dw = numerator / denominator;
        if (dw > 2) {
            dw = 4 - dw;
        }
        return dw > 1.69;
    }

    public void fit(double[] sample, double[] testSample, String estimatesFileName) {
        d = -1;
        double[] dSample = sample.clone();
        double[] E;
        ArrayList<double[]> derivatives = new ArrayList<>();
        double bestMSE = Double.MAX_VALUE;
        int bestp = 0, bestq = 0;
        double[] bestEstimate = new double[0];

        resultModel resultModel;
        do {
            derivatives.add(dSample);
            resultModel = findAR(dSample, 1);
            E = new double[dSample.length];
            E[0] = dSample[0];
            this.var = E[0] * E[0];
            for (int t = 1; t < dSample.length; t++) {
                E[t] = dSample[t] - resultModel.a[0] * dSample[t - 1];
                this.var += E[t];
            }
            this.var /= (dSample.length - 1);
            dSample = differentiate(dSample);
            d++;
        } while (Math.abs(resultModel.a[0]) >= 1 && !testDW(E));
        ARIMAParams arimaParams = new ARIMAParams(new resultModel(null, null, null, null, this.var), d);

        for (int q = 0; q < 3; q++) {
            for (int p = 1; p < 3; p++) {
                if (q == 0) {
                    resultModel = findAR(dSample, p);
                } else {
                    resultModel = findAC(dSample, p, q);
                }
                double[] estimates = estimateData(testSample.length, new ARIMAParams(resultModel, d), derivatives);
                double mse = 0;
                for (int i = 0; i < testSample.length; i++) {
                    mse += (estimates[i] - testSample[i]) * (estimates[i] - testSample[i]);
                }
                if (mse < bestMSE) {
                    bestMSE = mse;
                    arimaParams.resultModel = resultModel;
                    bestp = p;
                    bestq = q;
                    bestEstimate = estimates;
                }
            }
        }
        System.out.println("p: " + bestp);
        System.out.println("d: " + d);
        System.out.println("q: " + bestq);
        System.out.println("a: " + Arrays.toString(arimaParams.resultModel.a));
        System.out.println("c: " + Arrays.toString(arimaParams.resultModel.c));
        System.out.println("s: " + arimaParams.resultModel.sigma);
        System.out.println("bestMse: " + bestMSE);
        if (bestEstimate != null) {
            double[] T = new double[bestEstimate.length];
            for (int i = 0; i < bestEstimate.length; i++) {
                T[i] = i + sample.length;
            }
            GenerationWriter.write(bestEstimate.length, T, bestEstimate, estimatesFileName, true);
        }
    }

    double[] estimateData(int N, ARIMAParams arimaParams, ArrayList<double[]> derivatives) {
        double[] X = new double[N];
        double[] eps = new double[N];
        int p = arimaParams.resultModel.a.length;
        int q = arimaParams.resultModel.c != null ? arimaParams.resultModel.c.length : 0;
        NormalDistribution nd = new NormalDistribution(0, this.var);
        for (int i = 0; i < N; i++) {
            eps[i] = nd.sample();
            X[i] += arimaParams.resultModel.a[0];
            for (int j = 1; j < p; j++) {
                if (j <= i) {
                    X[i] += arimaParams.resultModel.a[j] * X[i - j];
                } else {
                    X[i] += arimaParams.resultModel.a[j] * arimaParams.resultModel.lastPValues[j - 1];
                }
            }
        }

        for (int i = 0; i < arimaParams.d; i++) {
            X = integrate(X, derivatives.get(i)[derivatives.get(i).length - 1]);
        }
        return X;
    }
}
