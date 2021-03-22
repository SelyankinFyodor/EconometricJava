package main.java.main;

import main.java.research.Research;

public class ResearchRunner {
    public static void main(String[] args) {
        final String inDir = "./dataFiles/Samples/";
        final String outDir = "./dataFiles/Result/";
        final String statDir = "./dataFiles/Stat/";
        final String[] filenames = {"Zero", "First", "Second", "Third"};
        final String ext = ".txt";
        final int maxPolynomialDegree = 3;

        for (int i = 0; i <= maxPolynomialDegree; i++) {
            Research.run(inDir + filenames[i] + ext,
                    outDir + filenames[i] + ext,
                    statDir + filenames[i] + ext);
        }
    }
}
