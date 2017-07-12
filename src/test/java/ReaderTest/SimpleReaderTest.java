package ReaderTest;

import main.reader.SimpleReader;
import org.junit.BeforeClass;

/**
 * Created by Magda on 12/07/2017.
 */
public class SimpleReaderTest extends AbstractReaderTest {
    @BeforeClass
    public static void runBeforeClass() {
        reader = new SimpleReader();
    }
}
