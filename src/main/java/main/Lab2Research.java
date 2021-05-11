package main.java.main;

import main.java.research.ResearchAnnealing;
import main.java.research.ResearchExpTaylor;

public class Lab2Research {
    public static void main(String[] args) {
        String inDir = "./dataFiles/lab2/Samples/";
        String[] filenames = {"Expression", "Taylor"};
        String ext = ".txt";
        String ISTFileName = "IterSigmaTempFileName";

//        ResearchExpTaylor.researchOnRegularizationParam(inDir + filenames[0] + ext);
        double bestRegularizationParam = 0.3;
//        ResearchExpTaylor.researchBetas(inDir + filenames[0] + ext, bestRegularizationParam);
        ResearchAnnealing.run(inDir + filenames[0] + ext, inDir + ISTFileName + ext);
    }
}
