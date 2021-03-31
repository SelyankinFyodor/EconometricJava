package main.java.writer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class SigmaWriter {
    public static int write(int chunk, double[] sigmas, String filename) {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, chunk != 0))) {
            writer.write(chunk + "");
            for (double s : sigmas) {
                writer.write(" " + s);
            }
            writer.write(System.lineSeparator());
            writer.flush();
            return 0;
        } catch (IOException e) {
            return 1;
        }
    }
}
