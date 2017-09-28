package main.comparator;

public class HammingDistance {

    private static final double sameLowerInterval = 0.;

    //values from Daugman's How Iris Recognition Works
    private static final double meanSame = 0.019;
    private static final double stdDevSame = 0.039;
    private static final double meanDifferent = 0.456;
    private static final double stdDevDifferent = 0.02;
    private static final double sameUpperInterval = 0.3;
    private static final double differentLowerInterval = 0.36;
    private static final double differentUpperInterval = 0.50;
    private double HD;

    public Comparison isSameEye() {
/*
        if (HD == 0.)
            return Comparison.IDENTICAL;
        if (HD >= meanSame - stdDevSame && HD <= meanSame + stdDevSame)
            return Comparison.SAME;
        if (HD >= meanDifferent - stdDevDifferent && HD <= meanDifferent + stdDevDifferent)
            return Comparison.DIFFERENT;
        return Comparison.INCONCLUSIVE;
*/
        if (HD == 0.)
            return Comparison.IDENTICAL;
        if (HD >= sameLowerInterval && HD <= sameUpperInterval)
            return Comparison.SAME;
        if (HD >= differentLowerInterval && HD <= differentUpperInterval)
            return Comparison.DIFFERENT;
        return Comparison.INCONCLUSIVE;

    }

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
