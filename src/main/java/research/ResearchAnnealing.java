package main.java.research;

import main.java.model.Annealing;
import main.java.model.Boltzmann;
import main.java.model.Cauchy;
import main.java.writer.SigmaIterWriter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class ResearchAnnealing {

    public static void run(String inFileName, String ISTFileName) {
        int maxResearchIteration = 60000;
        int stepIter = 50;
        final String SPACE = " ";
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(inFileName))) {
            int N = Integer.decode(bufferedReader.readLine());

            double[] T = new double[N];
            double[] X = new double[N];

            for (int j = 0; j < N; j++) {
                String[] dots = bufferedReader.readLine().split(SPACE);
                T[j] = Double.parseDouble(dots[0]);
                X[j] = Double.parseDouble(dots[1]);
            }


            for (Annealing annealing : new Annealing[]{
                    new Cauchy(T, X, new double[][]{{-15, 15}, {-15, 15}}),
                    new Boltzmann(T, X, new double[][]{{-15, 15}, {-15, 15}})
            }) {

                double[] betas = annealing.fit(new double[]{0, 0}, stepIter, 5, 0.0001, maxResearchIteration);

                SigmaIterWriter.write(annealing.getIterations().size(), annealing.getIterations(), annealing.getSigma(), annealing.getTemperatures(), ISTFileName);
                System.out.println(Arrays.toString(betas));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
