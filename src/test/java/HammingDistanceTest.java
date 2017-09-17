import main.comparator.ByteArrayComparator;
import main.comparator.HammingDistance;
import main.encoder.ByteCode;
import main.interfaces.IComparator;
import main.utils.ImageData;
import main.utils.MatConstants;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;

public class HammingDistanceTest extends BaseTest {

    private final static Mat whiteMat = new Mat(10, 10, MatConstants.TYPE, new Scalar(255));
    private final static Mat blackMat = new Mat(10, 10, MatConstants.TYPE, new Scalar(0));
    private static IComparator comparator;

    @BeforeClass
    public static void beforeClass() {
        comparator = new ByteArrayComparator();
    }

    @Test
    public void testMinDistance() {
        ImageData whiteData = new ImageData();
        ImageData blackData = new ImageData();
        whiteData.setByteCode(new ByteCode(whiteMat));
        blackData.setByteCode(new ByteCode(blackMat));
        HammingDistance HD = comparator.compare(whiteData, whiteData);
        Assert.assertTrue(HD.getHD() == 0);
        HD = comparator.compare(blackData, blackData);
        Assert.assertTrue(HD.getHD() == 0);

    }

    @Test
    public void testMaxDistance() {
        //TODO result is not 1
        ImageData whiteData = new ImageData();
        ImageData blackData = new ImageData();
        whiteData.setByteCode(new ByteCode(whiteMat));
        blackData.setByteCode(new ByteCode(blackMat));

        HammingDistance HD = comparator.compare(blackData, whiteData);
        Assert.assertTrue(HD.getHD() == 1);
        HD = comparator.compare(whiteData, blackData);
        Assert.assertTrue(HD.getHD() == 1);

    }

    @Test
    public void testDistance() {
        //TODO write this test
    }
}
