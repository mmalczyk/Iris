import main.display.Display;
import main.interfaces.IReader;
import main.reader.OpenCVReader;
import main.utils.ImageData;
import main.utils.MatConstants;
import main.utils.TestDirectory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.opencv.core.Core;
import org.opencv.core.Mat;

import java.nio.file.Path;

/**
 * Created by Magda on 12/07/2017.
 */
public class OpenCVReaderTest {

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        if (Display.moduleNotInDictionary(OpenCVReader.class))
            Display.displayModule(OpenCVReader.class, false);
    }

    private IReader reader;

    @Before
    public void beforeTest() {
        reader = new OpenCVReader();
    }

    @Test
    public void readImagesTest() {
        for (int i = 0; i < 10; i++) {
            Path path = TestDirectory.CASIA_Image(0, TestDirectory.Eye.Left, i);
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
