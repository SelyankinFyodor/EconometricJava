package main.java.generator;

import org.apache.commons.math3.distribution.NormalDistribution;

import java.util.Random;

public class NormalWithEjection {
    private final double mean;
    private final double var;
    private final long ejectInterval;
    private long currentStep;
    private final double maxEject;
    private final NormalDistribution random;

    public NormalWithEjection(double mean, double var, double maxEject, int maxEjectInterval) {
        this.mean = mean;
        this.var = var;
        this.maxEject = maxEject;
        this.random = new NormalDistribution();
        this.ejectInterval = maxEjectInterval;
        this.currentStep = 0;
    }

    public double nextGaussian() {
        return random.sample() * var + mean + this.calcInject();
    }

    private double calcInject() {
        return currentStep != 0 && currentStep++ % ejectInterval == 0 ? random.sample() * maxEject : 0;
    }

}
