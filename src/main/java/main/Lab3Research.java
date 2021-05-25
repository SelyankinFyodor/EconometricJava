package main.java.main;

import main.java.research.ResearchArima;

public class Lab3Research {
    public static void main(String[] args) {
        String inDir = "./dataFiles/lab3/";
        String[] filenames = {"Expression"};
        String ext = ".txt";
        String estimateFileName = "./dataFiles/lab3/estimate.txt";

        ResearchArima.run(inDir + filenames[0] + ext, estimateFileName);
    }
}
