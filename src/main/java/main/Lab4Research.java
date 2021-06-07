package main.java.main;

import main.java.research.GCTest;
import main.java.research.ThreeSigmaTest;
import main.java.writer.GenerationWriter;
import org.apache.commons.math3.distribution.NormalDistribution;

import java.util.function.DoubleUnaryOperator;

public class Lab4Research {
    public static void main(String[] args) {
        NormalDistribution nd = new NormalDistribution(0, 0.1);
        int N = 500;
        double[] X = new double[N];
        double[] T = new double[N];
        double tStep = 0.1;

        DoubleUnaryOperator[] G = {
                (t) -> 2 * t * t - 40 * t + 210,
                (t) -> 3 * t - 20,
                (t) -> 0.2 * t * t - 40,
                (t) -> 140,
                (t) -> -2 * t + 220
        };
        for (int i = 0; i < N; i++) {
            T[i] = i * tStep;
            X[i] = G[i / 100].applyAsDouble(i * tStep) + nd.sample();
        }
        GenerationWriter.write(N, T, X, "./dataFiles/lab4/estimate.txt", true);
        System.out.println("3 sigm");
        new ThreeSigmaTest().run(T, X);
        System.out.println("Chou");
        new GCTest().run(T, X);

    }
}
