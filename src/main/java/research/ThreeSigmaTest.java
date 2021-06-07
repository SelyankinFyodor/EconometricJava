package main.java.research;

import main.java.model.PolynomialRegression;

import java.util.ArrayList;
import java.util.Arrays;

public class ThreeSigmaTest {
    public static double polyVal(double[] coefs, double t) {
        double value = 0;
        double pow = 1;

        for (double coef : coefs) {
            value += coef * pow;
            pow *= t;
        }

        return value;
    }

    public void run(double[] T, double[] X) {
        PolynomialRegression polynomialRegression = new PolynomialRegression();
        int initSize = 30;
        double[] sample = Arrays.copyOfRange(X, 0, initSize);
        double[] sampleT = Arrays.copyOfRange(T, 0, initSize);
        polynomialRegression.fit(sampleT, sample, 3);
        double[] beta = polynomialRegression.getBetas();
        ArrayList<Integer> breaks = new ArrayList<>();
        breaks.add(0);
        for (int i = initSize; i < T.length; i++) {
            double err = polyVal(beta, T[i]) - X[i];
            if (polynomialRegression.getSigma() * 3 < Math.abs(err)) {
                breaks.add(i);
                System.out.println(Arrays.toString(beta));
                sample = Arrays.copyOfRange(X, i, i + initSize);
                sampleT = Arrays.copyOfRange(T, i, i + initSize);
                System.out.println(i);
                polynomialRegression.fit(sampleT, sample, 3);
                beta = polynomialRegression.getBetas();
                i += initSize - 1;
            } else {
                sample = Arrays.copyOfRange(X, breaks.get(breaks.size() - 1), i + 1);
                sampleT = Arrays.copyOfRange(T, breaks.get(breaks.size() - 1), i + 1);
                polynomialRegression.fit(sampleT, sample, 3);
                beta = polynomialRegression.getBetas();
            }
        }
    }
}
