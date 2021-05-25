package main.java.main;

import main.java.generator.ARIMAGenerator;
import main.java.writer.GenerationWriter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ARIMADataGenerator {
    public static void main(String[] args) {
        final String sampleDir = "./dataFiles/lab3/";
        final String filename = "Expression";
        final String ext = ".txt";

        if (!Files.exists(Paths.get(sampleDir))) {
            try {
                Files.createDirectories(Paths.get(sampleDir));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        ARIMAGenerator arimaGenerator = new ARIMAGenerator(
                new double[]{.5, .3},
                new double[]{1, .2},
                0.1,
                1,
                1,
                1
        );

        int N = 125;
        double[] realization = arimaGenerator.generate(N);

        double[] T = new double[N];
        for (int i = 0; i < N; i++) {
            T[i] = i;
        }
        GenerationWriter.write(N, T, realization, sampleDir + filename + ext, true);
    }
}
