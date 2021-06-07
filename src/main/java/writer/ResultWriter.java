package main.java.writer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class ResultWriter {
    public static int write(String line, String filename, boolean append) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, append))) {
            writer.write(line + System.lineSeparator());
            writer.flush();
            return 0;
        } catch (IOException e) {
            return 1;
        }
    }

    public static String prepareResult(double[] betas, double sigma, double pValue) {
        StringBuilder result = new StringBuilder("\tbetas: [ ");
        for (double beta : betas) {
            result.append(String.format("%.4f ", beta));
        }
        return result + "], sigma: " + String.format("%.4f ", sigma) + ", pValue: " + String.format("%.4f ", pValue);
    }
}
