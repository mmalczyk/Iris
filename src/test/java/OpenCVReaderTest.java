import main.interfaces.IReader;
import main.reader.OpenCVReader;
import main.utils.ImageData;
import main.utils.MatConstants;
import main.utils.TestDirectory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.opencv.core.Mat;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by Magda on 12/07/2017.
 */
public class OpenCVReaderTest extends BaseTest {

    private IReader reader;

    @Before
    public void beforeTest() {
        reader = new OpenCVReader();
    }

    @Test
    public void readLeftImagesTest() {
        int limit = 10;
        for (int i = 0; i < limit; i++) {
            for (int j = 0; j < 10; j++) {
                readImage(i, j, TestDirectory.Eye.Left);
            }
        }
    }

    @Test
    public void readRightImagesTest() {
        int limit = 10;
        for (int i = 0; i < limit; i++) {
            for (int j = 0; j < 10; j++) {
                readImage(i, j, TestDirectory.Eye.Right);
            }
        }
    }

    private void readImage(int i, int j, TestDirectory.Eye side) {
        Path path = getImagePath(i, side, j);
        if (Files.exists(path)) {
            ImageData imageData = reader.read(path);
            imageDataTest(imageData);
            pathTest(imageData);
            matTest(imageData);
        }
    }

    private void imageDataTest(ImageData imageData) {
        Assert.assertNotNull(imageData);
    }

    private void pathTest(ImageData imageData) {
        Assert.assertNotNull(imageData.getPath());
    }

    private void matTest(ImageData imageData) {
        Mat imageMat = imageData.getImageMat();
        Assert.assertNotNull(imageData.getImageMat());
        Assert.assertEquals(MatConstants.TYPE, imageMat.type());
        Assert.assertNotEquals(0, imageMat.width());
        Assert.assertNotEquals(0, imageMat.height());
    }

}
