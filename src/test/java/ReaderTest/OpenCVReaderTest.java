package ReaderTest;

import main.reader.OpenCVReader;
import org.junit.BeforeClass;

/**
 * Created by Magda on 12/07/2017.
 */
public class OpenCVReaderTest extends AbstractReaderTest {
    @BeforeClass
    public static void runBeforeClass() {
        reader = new OpenCVReader();
    }
}
