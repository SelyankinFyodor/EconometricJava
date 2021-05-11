package main.java.writer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class SigmaIterWriter {
    public static int write(int size, ArrayList<Integer> iterations, ArrayList<Double> sigmas, ArrayList<Double> temperatures, String filename) {
        if (!Files.exists(Paths.get(filename))) {
            try {
                Files.createFile(Paths.get(filename));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, false))) {
            writer.write(size + "\n");
            for (int i = 0; i < size; i++) {
                writer.write(" " + iterations.get(i) + " " + sigmas.get(i) + " " + temperatures.get(i) + System.lineSeparator());
            }
            writer.flush();
            return 0;
        } catch (IOException e) {
            return 1;
        }
    }
}
