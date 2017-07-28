package main.comparator;

import main.display.DisplayableModule;
import main.encoder.ByteCode;
import main.interfaces.IComparator;

/**
 * Created by Magda on 15.02.2017.
 */
public class SimpleComparator extends DisplayableModule implements IComparator {

    //TODO comparatorTest
    //TODO OpenCVComparator
    public SimpleComparator() {
        super(moduleName);
    }

    @Override
    public double compare(ByteCode byteCodeA, ByteCode byteCodeB) {

        byte[] codeA = byteCodeA.getCode();
        byte[] codeB = byteCodeB.getCode();
        byte[] maskA = byteCodeA.getMask();
        byte[] maskB = byteCodeB.getMask();

        assertArguments(codeA, codeB, maskA, maskB);

        int length = codeA.length;
        int comparisonHammingWeight = 0;
        int maskHammingWeight = 0;
        byte maskC;

        double hammingDistance = 2.;
        for (int j = 0; j < length * 8 && hammingDistance > 0.; j++) {

            for (int i = 0; i < length; i++) {
                maskC = (byte) (maskA[i] & maskB[i]);
                maskHammingWeight += Integer.bitCount(maskC);
                comparisonHammingWeight += Integer.bitCount((byte) (codeA[i] ^ codeB[i]) & maskC);
            }
            double distance = (double) comparisonHammingWeight / (double) maskHammingWeight;
            if (hammingDistance > distance)
                hammingDistance = distance;

            byteShift(codeA, maskA);
        }

        return hammingDistance;
    }

    private void byteShift(byte[] codeA, byte[] maskA) {
        byteShift(codeA);
        byteShift(maskA);
    }

    private void byteShift(byte[] codeA) {
        byte b0 = codeA[0];
        System.arraycopy(codeA, 1, codeA, 0, codeA.length - 1);
        codeA[codeA.length - 1] = b0;
    }


    private void assertArguments(byte[] codeA, byte[] codeB, byte[] maskA, byte[] maskB) {
        if (codeA == null || codeB == null || maskA == null || maskB == null)
            throw new IllegalArgumentException("Null codes");
        if (codeA.length != codeB.length || codeA.length != maskA.length || maskA.length != maskB.length)
            throw new IllegalArgumentException("Uneven code length");
    }
}
