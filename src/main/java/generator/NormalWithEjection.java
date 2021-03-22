package main.java.generator;

import java.util.Random;

public class NormalWithEjection {
    private final double mean;
    private final double var;
    private final long ejectInterval;
    private long currentStep;
    private final double maxEject;
    private final Random random;

    public NormalWithEjection(double mean, double var, double maxEject, int maxEjectInterval) {
        this.mean = mean;
        this.var = var;
        this.maxEject = maxEject;
        this.random = new Random();
        this.ejectInterval = maxEjectInterval != 0 ? random.nextInt() % maxEjectInterval : 0;
        this.currentStep = 0;
    }

    public double nextGaussian() {
        return random.nextGaussian() * var + mean + this.calcInject();
    }

    private double calcInject() {
        return currentStep != 0 && currentStep++ % ejectInterval == 0 ? random.nextGaussian() * maxEject : 0;
    }

}
