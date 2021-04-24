package main.java.main;

import main.java.generator.NormalWithEjection;
import main.java.generator.XGenerator;
import main.java.writer.GenerationWriter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class ExpDataGenerator {
    public static void main(String[] args) {
        final double[] coefs = {10, -5};

        final String sampleDir = "./dataFiles/lab2/Samples/";
        final String filename = "Expression";
        final String ext = ".txt";

        double timeStart = 0;
        final double timeStep = 0.01;

        final long chunkSize = 50;
        final long nunOfChunk = 4;

        double[] T = new double[(int) chunkSize];
        double[] X = new double[(int) chunkSize];

        NormalWithEjection normalWithEjection = new NormalWithEjection(0, 1, 0, 0);
        if (!Files.exists(Paths.get(sampleDir))) {
            try {
                Files.createDirectories(Paths.get(sampleDir));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        XGenerator generator = new XGenerator(
                (t) -> ExpDataGenerator.exprVal(coefs, t),
                (t) -> normalWithEjection.nextGaussian());

        for (int chunk = 0; chunk < nunOfChunk; chunk++) {
            for (int j = 0; j < chunkSize; j++) {
                T[j] = timeStart + j * timeStep;
                X[j] = generator.getX(T[j]);
            }
            GenerationWriter.write(chunkSize * nunOfChunk, T, X,
                    sampleDir + filename + ext, chunk == 0);
            timeStart += timeStep * chunkSize;
        }
    }

    public static double exprVal(double[] coefs, double t) {
        return coefs[0] * Math.exp(coefs[1] * t);
    }

    public static double exprTaylorVal(double[] coefs, int polyDeg, double t) {
        double term = 1;
        double result = term;
        for (int i = 1; i <= polyDeg; i++) {
            term *= coefs[1] * t / i;
            result += term;
        }
        return result * coefs[0];
    }

    public static double[] TaylorPolyCoefs(double[] coefs, int polyDeg) {
        double[] taylorCoefs = new double[polyDeg + 1];
        double taylorCoef = coefs[0];
        taylorCoefs[0] = taylorCoef;
        for (int i = 1; i <= polyDeg; i++) {
            taylorCoefs[i] = taylorCoef *= coefs[1] / i;
        }
        return taylorCoefs;
    }
}
