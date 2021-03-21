package main.java.main;

import main.java.research.Research;

public class ResearchRunner {
    public static void main(String[] args) {
        final String inDir = "./dataFiles/Samples/";
        final String outDir = "./dataFiles/Result/";
        final String[] filenames = {"Zero", "First", "Second", "Third"};
        final String ext = ".txt";

        for (int i = 0; i < 4; i++) {
            Research.run(inDir + filenames[i] + ext, outDir + filenames[i] + ext);
        }
    }
}
