package ReaderTest;

import main.interfaces.IReader;
import main.utils.ImageData;
import main.utils.MatConstants;
import main.utils.TestDirectory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by Magda on 12/07/2017.
 */
public abstract class AbstractReaderTest {

    private final static Path path = Paths.get(TestDirectory.images.toString(), "S5000L00.jpg");
    static IReader reader;
    private ImageData imageData;

    @Before
    public void runBeforeTestMethod() {
        imageData = reader.read(path);
    }

    @Test
    public void imageDataTest() {
        Assert.assertNotNull(imageData);
    }

    @Test
    public void pathTest() {
        Assert.assertNotNull(imageData.getPath());
    }


    @Test
    public void matTest() {
        Assert.assertNotNull(imageData.getImageMat());
        Assert.assertEquals(MatConstants.TYPE, imageData.getImageMat().type());
    }

}
