package main.encoder;

import main.Utils.ImageUtils;
import main.Utils.MatConstants;
import org.opencv.core.Mat;

/**
 * Created by Magda on 10/07/2017.
 */
public class ByteCode {
    private final static int BYTE_SIZE = 8;
    //TODO get rid of width and cols
    //those two just to estimate how to display the code
    private final int cols;
    private final int rows;
    private byte[] code;
    private Mat display; //lazy init
//    private Scalar checksum;

    public ByteCode(Mat mat) {
        //TODO assert greyscale
        cols = mat.width();
        rows = mat.height();
//        checksum = Core.sumElems(mat);
        generateCode(mat);
    }

    public byte[] getCode() {
        return code;
    }

    public void display() {
        if (display == null) {
            display = new Mat(rows, cols, MatConstants.TYPE);
            byte b;
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    b = getBit(getPos(i, j, cols /*step*/));
                    assert b == 0 || b == 1;
                    if (b == 0)
                        display.put(i, j, 0, 0, 0);
                    if (b == 1)
                        display.put(i, j, 255, 255, 255);
                }
            }
            //TODO this assertion doesn't make sense make a display test
/*
            Scalar checkDisplay = Core.sumElems(display);
            assert checksum.equals(checkDisplay);
*/
        }
        ImageUtils.showBufferedImage(display, "CODE_MAT");
    }

    private void generateCode(Mat mat) {
        ImageUtils.showBufferedImage(mat, "ORG_MAT");
        code = new byte[getCodeSize(mat)];
        int step = mat.cols();
        double[] pixel;
        for (int i = 0; i < mat.rows(); i++) {
            for (int j = 0; j < mat.cols(); j++) {
                pixel = mat.get(i, j);
                assert pixel.length == 3; //assuming greyscale
                if (pixel[0] <= 0)  //threshold at 0
                    setBit((byte) 0, getPos(i, j, step));
                else
                    setBit((byte) 1, getPos(i, j, step));
            }
        }
    }

    private int getPos(int row, int col, int step) {
        return row * step + col;
    }

    private int getCodeSize(Mat mat) {
        int matSize = mat.cols() * mat.rows();
        int codeSize = matSize / BYTE_SIZE;
        if (matSize % BYTE_SIZE != 0)
            codeSize += 1;
        return codeSize;
    }


    //TODO move these to inner class
    //https://stackoverflow.com/questions/4674006/set-specific-bit-in-byte
    private byte setBit1(byte b, int pos) {
        return (byte) (b | (byte) (1 << pos));
    }

    private byte setBit0(byte b, int pos) {
        return (byte) (b & ~(byte) (1 << pos));
    }

    private byte getBit(byte b, int pos) {
        return (byte) ((b >> pos) & (byte) 1);
    }

    private void setBit(int value, int pos) {
        assert value == 0 || value == 1;
        int posInArr = pos / BYTE_SIZE;
        int posInByte = pos % BYTE_SIZE;
        if (value == 0)
            code[posInArr] = setBit0(code[posInArr], posInByte);
        else
            code[posInArr] = setBit1(code[posInArr], posInByte);
    }

    private byte getBit(int pos) {
        return getBit(code[pos / BYTE_SIZE], pos % BYTE_SIZE);
    }


}
