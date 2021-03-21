package main.java.research;

import main.java.model.PolynomialRegression;
import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import writer.ResultWriter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class Research {
    public static void run(String inFileName, String outFileName) {
        final String SPACE = " ";

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(inFileName))) {
            long N = Long.decode(bufferedReader.readLine());
            int chunkSize = 200;

            for (int chunk = 0, chunks = (int) (N / chunkSize); chunk < chunks; chunk++) {
                double[] T = new double[chunkSize];
                double[] X = new double[chunkSize];

                for (int j = 0; j < chunkSize; j++) {
                    String[] dots = bufferedReader.readLine().split(SPACE);
                    T[j] = Double.parseDouble(dots[0]);
                    X[j] = Double.parseDouble(dots[1]);
                }
                ResultWriter.write("interval: " + T[0] + " ," + T[chunkSize - 1], outFileName, chunk != 0);
                PolynomialRegression polyRegr = new PolynomialRegression();
                for (int i = 0; i < 4; i++) {
                    polyRegr.fit(T, X, i);

                    double[] diff = new double[chunkSize];

                    for (int j = 0; j < chunkSize; j++) {
                        diff[j] = X[j] - polyRegr.getValue(T[j]);
                    }
                    double[] firstPart = Arrays.copyOfRange(diff, 0, chunkSize / 2);
                    double[] secondPart = Arrays.copyOfRange(diff, chunkSize / 2, chunkSize);
                    MannWhitneyUTest MWTest = new MannWhitneyUTest();
                    double pValue = MWTest.mannWhitneyUTest(firstPart, secondPart);
                    String resultLine = ResultWriter.prepareResult(polyRegr.getBetas(), polyRegr.getSigma(), pValue);
                    ResultWriter.write(resultLine, outFileName, true);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
