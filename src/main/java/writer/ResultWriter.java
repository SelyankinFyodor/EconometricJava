package writer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
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
        return "\tbetas: " + Arrays.toString(betas) + ", sigma: " + sigma + ", pValue: " + pValue;
    }
}
