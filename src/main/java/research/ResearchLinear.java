package main.java.research;

import main.java.model.PolynomialRegression;
import main.java.writer.SigmaWriter;
import main.java.writer.StatWriter;
import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import main.java.writer.ResultWriter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class ResearchLinear {
    public static void run(String inFileName, String outFileName, String statisticFileName, String sigmaFileName) {
        final int maxDegree = 3;
        int[] statistic = new int[maxDegree + 1];
        double a = 0.05;
        final String SPACE = " ";

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(inFileName))) {
            int N = Integer.decode(bufferedReader.readLine());
            int chunkSize = 50;

            double[] T = new double[N];
            double[] X = new double[N];

            for (int j = 0; j < N; j++) {
                String[] dots = bufferedReader.readLine().split(SPACE);
                T[j] = Double.parseDouble(dots[0]);
                X[j] = Double.parseDouble(dots[1]);
            }
            ResultWriter.write("interval: " + T[0] + " ," + T[chunkSize - 1], outFileName, false);
            PolynomialRegression polyRegr = new PolynomialRegression();
            double[] Sigmas = new double[maxDegree + 1];
            for (int i = 0; i <= maxDegree; i++) {
                polyRegr.fit(T, X, i);

                double[] diff = new double[chunkSize];

                for (int j = 0; j < chunkSize; j++) {
                    diff[j] = X[j] - polyRegr.getValue(T[j]);
                }
                double[] firstPart = Arrays.copyOfRange(diff, 0, chunkSize / 2);
                double[] secondPart = Arrays.copyOfRange(diff, chunkSize / 2, chunkSize);
                double pValue = new MannWhitneyUTest().mannWhitneyUTest(firstPart, secondPart);
                if (pValue > a) {
                    statistic[i] += 1;
                }
                String resultLine = ResultWriter.prepareResult(polyRegr.getBetas(), polyRegr.getSigma(), pValue);
                ResultWriter.write(resultLine, outFileName, true);
                Sigmas[i] = polyRegr.getSigma();
            }
            SigmaWriter.write(0, Sigmas, sigmaFileName);
            StatWriter.write(N, statistic, statisticFileName, false);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
