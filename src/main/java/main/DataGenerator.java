package main.java.main;

import main.java.generator.*;
import main.java.writer.GenerationWriter;

public class DataGenerator {
    public static void main(String[] args) {
        final double[] coefs = {10, 20, 30, 40};
        final double notLeadMul = 0.01;
        final double maxPolyDeg = 3;

        final String sampleDir = "./dataFiles/Samples/";
        final String[] filenames = {"Zero", "First", "Second", "Third"};
        final String ext = ".txt";

        double timeStart = 0;
        final double timeStep = 0.001;

        final long chunkSize = 50;
        final long nunOfChunk = 200000;

        double[] T = new double[(int) chunkSize];
        double[] X = new double[(int) chunkSize];

        NormalWithEjection normalWithEjection = new NormalWithEjection(0, 1, 3, 25);
        for (int deg = 0; deg <= maxPolyDeg; deg++) {
            int finalDeg = deg;

            XGenerator xGenerator = new XGenerator(
                    (t) -> DataGenerator.polyVal(coefs, finalDeg, notLeadMul, t),
                    (t) -> normalWithEjection.nextGaussian()
            );

            for (int chunk = 0; chunk < nunOfChunk; chunk++) {
                for (int j = 0; j < chunkSize; j++) {
                    T[j] = timeStart + j * timeStep;
                    X[j] = xGenerator.getX(T[j]);
                }
                GenerationWriter.write(chunkSize*nunOfChunk, T, X, sampleDir + filenames[deg] + ext, chunk == 0);
                timeStart += timeStep * chunkSize;
            }
            timeStart = 0.0;
        }
    }

    public static double polyVal(double[] coefs, int leadCoef, double notLeadMul, double t) {
        double value = 0;
        double pow = 1;

        for (int i = 0, len = coefs.length; i < len; i++) {
            value += coefs[i] * pow * (leadCoef == i ? 1 : notLeadMul);
            pow *= t;
        }

        return value;
    }
}
