import main.encoder.ByteCode;
import main.utils.MatConstants;
import org.junit.Assert;
import org.junit.Test;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;

import java.util.Arrays;

/**
 * Created by Magda on 10/07/2017.
 */
public class ByteCodeTest extends BaseTest {

    @Test
    public void allZerosTest() {
        //3 dim scalar because the actual matrices are converted to greyscale from color
        Mat mat = new Mat(4, 4, MatConstants.TYPE, Scalar.all(0));
        ByteCode codeObject = new ByteCode(mat);
        byte[] code = codeObject.getCode();

        byte[] solution = new byte[2];
        solution[0] = (byte) 0;
        solution[1] = (byte) 0;

        Assert.assertEquals(2, code.length);
        Assert.assertTrue(Arrays.equals(code, solution));
    }

    @Test
    public void allSamePositiveTest() {
        Mat mat = new Mat(4, 4, MatConstants.TYPE, Scalar.all(255));
        ByteCode codeObject = new ByteCode(mat);
        byte[] code = codeObject.getCode();

        byte[] solution = new byte[2];
        solution[0] = (byte) -1;
        solution[1] = (byte) -1;

        Assert.assertEquals(2, code.length);
        Assert.assertTrue(Arrays.equals(code, solution));
    }

    @Test
    public void allSameNegativeTest() {
        Mat mat = new Mat(4, 4, MatConstants.TYPE, Scalar.all(-255));
        ByteCode codeObject = new ByteCode(mat);
        byte[] code = codeObject.getCode();

        byte[] solution = new byte[2];
        solution[0] = (byte) 0;
        solution[1] = (byte) 0;

        Assert.assertEquals(2, code.length);
        Assert.assertTrue(Arrays.equals(code, solution));
    }

    @Test
    public void mixedTest() {
/*
        1 0 0 1
        0 0 0 0
        0 0 0 0
        1 0 0 1
*/
        Mat mat = new Mat(4, 4, MatConstants.TYPE, Scalar.all(0));
        double[] one = new double[]{255};
        mat.put(0, 0, one);
        mat.put(0, 3, one);
        mat.put(3, 0, one);
        mat.put(3, 3, one);
        ByteCode codeObject = new ByteCode(mat);
        byte[] code = codeObject.getCode();

        byte[] solution = new byte[2];
        solution[0] = (byte) 9; //row 0 is at the bottom of mat btw
        solution[1] = (byte) 144;

        Assert.assertEquals(2, code.length);
        Assert.assertTrue(Arrays.equals(code, solution));
    }

}
