package main.java.research;

import main.java.model.PolynomialRegression;
import org.apache.commons.math3.distribution.FDistribution;

import java.util.ArrayList;
import java.util.Arrays;

public class GCTest {
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
        PolynomialRegression prLeft = new PolynomialRegression();
        PolynomialRegression prFull = new PolynomialRegression();
        double tStep = T[1] - T[0];
        int firstPartSize = 30;
        int secondPartSize = 7;
        double[] sampleLeft = Arrays.copyOfRange(X, 0, firstPartSize);
        double[] Tleft = Arrays.copyOfRange(T, 0, firstPartSize);
        double[] sampleFull = Arrays.copyOfRange(X, 0, firstPartSize + secondPartSize);
        double[] TFull = Arrays.copyOfRange(T, 0, firstPartSize + secondPartSize);
        prLeft.fit(Tleft, sampleLeft, 3);
        prFull.fit(TFull, sampleFull, 3);
        ArrayList<Integer> breaks = new ArrayList<>();
        FDistribution fd = new FDistribution(secondPartSize, firstPartSize - 4 - 1);
        breaks.add(0);
        for (int i = firstPartSize; i < X.length; i++) {
            double mseLeft = prLeft.getMse();
            double mseFull = prFull.getMse();
            double F = ((mseFull - mseLeft) / mseLeft) * ((double) (firstPartSize - 4 - 1) / (double) secondPartSize);
            double a = fd.cumulativeProbability(F);
            if (a > 0.95) {
                i += firstPartSize;
                System.out.println(i);
                breaks.add(i);
            }
            if (i + firstPartSize + secondPartSize>= X.length){
                break;
            }
            sampleLeft = Arrays.copyOfRange(X, i, i + firstPartSize);
            Tleft = Arrays.copyOfRange(T, i, i + firstPartSize);
            sampleFull = Arrays.copyOfRange(X, i, i + firstPartSize + secondPartSize);
            TFull = Arrays.copyOfRange(T, i, i + firstPartSize + secondPartSize);
            prLeft.fit(Tleft, sampleLeft, 3);
            prFull.fit(TFull, sampleFull, 3);
        }
    }
}
