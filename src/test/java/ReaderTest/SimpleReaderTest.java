package ReaderTest;

import main.display.Display;
import main.reader.SimpleReader;
import org.junit.BeforeClass;

/**
 * Created by Magda on 12/07/2017.
 */
public class SimpleReaderTest extends AbstractReaderTest {

    static {
        if (Display.moduleNotInDictionary(SimpleReader.class))
            Display.displayModule(SimpleReader.class, false);
    }


    @BeforeClass
    public static void runBeforeClass() {
        reader = new SimpleReader();
    }
}
