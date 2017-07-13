package ReaderTest;

import main.display.Display;
import main.reader.OpenCVReader;
import org.junit.BeforeClass;

/**
 * Created by Magda on 12/07/2017.
 */
public class OpenCVReaderTest extends AbstractReaderTest {


    static {
        if (Display.moduleNotInDictionary(OpenCVReader.class))
            Display.displayModule(OpenCVReader.class, false);
    }


    @BeforeClass
    public static void runBeforeClass() {
        reader = new OpenCVReader();
    }
}
