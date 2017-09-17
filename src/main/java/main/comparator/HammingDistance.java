package main.comparator;

public class HammingDistance {

    public Comparison isSameEye() {
        if (HD == 0.)
            return Comparison.IDENTICAL;
        if (HD >= meanSame - stdDevSame && HD <= meanSame + stdDevSame)
            return Comparison.SAME;
        if (HD >= meanDifferent - stdDevDifferent && HD <= meanDifferent + stdDevDifferent)
            return Comparison.DIFFERENT;
        return Comparison.INCONCLUSIVE;

    }

    //values from Daugman's How Iris Recognition Works
    private static final double meanSame = 0.019;
    private static final double stdDevSame = 0.039;
    private static final double meanDifferent = 0.456;
    private static final double stdDevDifferent = 0.02;
    private double HD;

    public HammingDistance(double HD) {
        this.HD = HD;
    }

    public double getHD() {
        return HD;
    }

    public enum Comparison {
        SAME, DIFFERENT, INCONCLUSIVE, IDENTICAL
    }
}
