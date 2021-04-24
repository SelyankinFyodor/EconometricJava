package main.java.main;

import main.java.research.ResearchExpTaylor;

public class Lab2Research {
    public static void main(String[] args) {
        String inDir = "./dataFiles/lab2/Samples/";
        String[] filenames = {"Expression", "Taylor"};
        String ext = ".txt";

//        ResearchExpTaylor.researchOnRegularizationParam(inDir + filenames[0] + ext);
        double bestRegularizationParam = 0.;
        ResearchExpTaylor.researchBetas(inDir + filenames[0] + ext, bestRegularizationParam);
    }
}
