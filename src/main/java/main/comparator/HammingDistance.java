package main.comparator;

public class HammingDistance {
    private static final double meanSame = 0.11;
    private static final double stdDevSame = 0.065;
    private static final double meanDifferent = 0.458;
    private static final double stdDevDifferent = 0.0197;
    private double HD;

    public HammingDistance(double HD) {
        this.HD = HD;
    }

    public double getHD() {
        return HD;
    }

    public Boolean sameEye() {
        if (HD >= meanSame - stdDevSame && HD <= meanSame + stdDevSame)
            return true;
        if (HD >= meanDifferent - stdDevDifferent && HD <= meanDifferent + stdDevDifferent)
            return false;
        return null; //inconclusive result;

    }
}
