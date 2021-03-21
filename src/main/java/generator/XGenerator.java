package main.java.generator;

import java.util.function.DoubleUnaryOperator;

public class XGenerator {
    private DoubleUnaryOperator g, e;

    public XGenerator(DoubleUnaryOperator g, DoubleUnaryOperator e) {
        this.g = g;
        this.e = e;
    }

    public double getX(double t) {
        return g.applyAsDouble(t) +e.applyAsDouble(t);
    }
}
