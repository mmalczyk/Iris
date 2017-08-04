import main.comparator.HammingDistance;
import main.comparator.SimpleComparator;
import main.encoder.ByteCode;
import main.interfaces.IComparator;
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
        comparator = new SimpleComparator();
    }

    @Test
    public void testMinDistance() {
        HammingDistance HD = comparator.compare(new ByteCode(whiteMat), new ByteCode(whiteMat));
        Assert.assertTrue(HD.getHD() == 0);
        HD = comparator.compare(new ByteCode(blackMat), new ByteCode(blackMat));
        Assert.assertTrue(HD.getHD() == 0);

    }

    @Test
    public void testMaxDistance() {
        //TODO result is not 1
        HammingDistance HD = comparator.compare(new ByteCode(blackMat), new ByteCode(whiteMat));
        Assert.assertTrue(HD.getHD() == 1);
        HD = comparator.compare(new ByteCode(whiteMat), new ByteCode(blackMat));
        Assert.assertTrue(HD.getHD() == 1);

    }

    @Test
    public void testDistance() {
        //TODO write this test
    }
}
