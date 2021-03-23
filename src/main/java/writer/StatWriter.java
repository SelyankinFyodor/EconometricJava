package main.java.writer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class StatWriter {
    public static int write(int numOfData, int[] stats, String filename, boolean append) {
        final String[] polyNames = {"Zero", "First", "Second", "Third"};

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, append))) {
            writer.write("num of data: " + numOfData + System.lineSeparator());
            for (int i = 0; i < stats.length; i++) {
                writer.write(polyNames[i] + ":" + System.lineSeparator() +
                        "\tratio: " + (double) stats[i] / (double) numOfData + System.lineSeparator() +
                        "\tnum of approved: " + stats[i] + System.lineSeparator());
            }
            writer.write(System.lineSeparator());
            writer.flush();
            return 0;
        } catch (IOException e) {
            return 1;
        }
    }
}
