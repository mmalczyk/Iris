package main.comparator;

import main.display.DisplayableModule;
import main.encoder.ByteCode;
import main.interfaces.IComparator;
import main.utils.ImageData;

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
    public HammingDistance compare(ImageData imageDataA, ImageData imageDataB) {
        ByteCode byteCodeA = imageDataA.getByteCode();
        ByteCode byteCodeB = imageDataB.getByteCode();
        assertArguments(byteCodeA, byteCodeB);

        byte[] codeA = byteCodeA.getCode();
        byte[] codeB = byteCodeB.getCode();
        byte[] maskA = byteCodeA.getMask();
        byte[] maskB = byteCodeB.getMask();

        int length = codeA.length;
        int comparisonHammingWeight = 0;
        int maskHammingWeight = 0;
        byte maskC;

        double hammingDistance = 1.;
        //TODO settings: byteshift or no byteshift
        //outer loop is checking if we find a match after rotation (byte shift)
        for (int j = 0; j < length * 8 && hammingDistance > 0.; j++) {

            //for the time being ignoring last byte with padding
            for (int i = 0; i < length - 1; i++) {
                maskC = (byte) (maskA[i] & maskB[i]);
                maskHammingWeight += Integer.bitCount(maskC);
                comparisonHammingWeight += Integer.bitCount((byte) (codeA[i] ^ codeB[i]) & maskC);
            }
            double distance = (double) comparisonHammingWeight / (double) maskHammingWeight;

            //condition met means that a better match was found after rotation
            if (hammingDistance > distance)
                hammingDistance = distance;

            byteShift(codeA, maskA);
        }

        return new HammingDistance(hammingDistance);
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


    private void assertArguments(ByteCode byteCodeA, ByteCode byteCodeB) {
        byte[] codeA = byteCodeA.getCode();
        byte[] codeB = byteCodeB.getCode();
        byte[] maskA = byteCodeA.getMask();
        byte[] maskB = byteCodeB.getMask();

        if (codeA == null || codeB == null || maskA == null || maskB == null)
            throw new IllegalArgumentException("Null codes");
        if (codeA.length != codeB.length || codeA.length != maskA.length || maskA.length != maskB.length)
            throw new IllegalArgumentException("Uneven code length");
        if (byteCodeA.getPadding() != byteCodeB.getPadding())
            throw new IllegalArgumentException("Uneven code length: padding");
    }
}
