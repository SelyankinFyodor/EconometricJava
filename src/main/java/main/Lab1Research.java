package main.java.main;

import main.java.research.ResearchLinear;

public class Lab1Research {
    public static void main(String[] args) {
        final String inDir = "./dataFiles/lab1/dataFiles/Samples/";
        final String outDir = "./dataFiles/lab1/dataFiles/Result/";
        final String statDir = "./dataFiles/lab1/dataFiles/Stat/";
        final String sigmaDir = "./dataFiles/lab1/dataFiles/Sigma/";
        final String[] filenames = {"Zero", "First", "Second", "Third"};
        final String ext = ".txt";
        final int maxPolynomialDegree = 3;

        for (int i = 0; i <= maxPolynomialDegree; i++) {
            ResearchLinear.run(inDir + filenames[i] + ext,
                    outDir + filenames[i] + ext,
                    statDir + filenames[i] + ext,
                    sigmaDir + filenames[i] + ext);
        }
    }
}
