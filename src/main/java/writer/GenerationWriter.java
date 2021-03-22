package main.java.writer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class GenerationWriter {

    public static int write(long size, double[] T, double[] X, String filename, boolean begin) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, !begin))) {
            if (T.length != X.length) {
                return 1;
            }
            if (begin) {
                writer.write(size + System.lineSeparator());
            }
            for (int i = 0; i < X.length; i++) {
                String space = " ";
                writer.write(T[i] + space + X[i] + System.lineSeparator());
            }
            writer.flush();
            return 0;
        } catch (IOException e) {
            return 1;
        }
    }
}
