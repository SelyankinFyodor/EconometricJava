package main.java.research;

import main.java.model.ARIMADetector;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ResearchArima {
    public static void run(String inFilename,String estimatesFileName) {
        final String SPACE = " ";

        double[] trainX;
        double[] testX;
        double[] trainT;
        double[] testT;
        int N;
        double trainRatio = 0.8;
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(inFilename))) {
            N = Integer.decode(bufferedReader.readLine());

            trainT = new double[(int) (trainRatio * N)];
            trainX = new double[(int) (trainRatio * N)];
            testT = new double[(int) ((1 - trainRatio) * N)];
            testX = new double[(int) ((1 - trainRatio) * N)];

            for (int j = 0; j < (trainRatio * N); j++) {
                String[] dots = bufferedReader.readLine().split(SPACE);
                trainT[j] = Double.parseDouble(dots[0]);
                trainX[j] = Double.parseDouble(dots[1]);
            }
            for (int j = 0; j < (int) ((1 - trainRatio) * N); j++) {
                String[] dots = bufferedReader.readLine().split(SPACE);
                testT[j] = Double.parseDouble(dots[0]);
                testX[j] = Double.parseDouble(dots[1]);
            }
            new ARIMADetector().fit(trainX, testX, estimatesFileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
