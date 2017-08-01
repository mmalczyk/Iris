import main.display.Display;
import main.interfaces.IReader;
import main.reader.OpenCVReader;
import main.settings.ModuleName;
import main.utils.TestDirectory;
import org.junit.BeforeClass;
import org.opencv.core.Core;

import java.lang.invoke.MethodHandles;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class IrisComparisonTest {

    private static Path resultsDirectory;

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        if (Display.moduleNotInDictionary(ModuleName.Reader))
            Display.displayModule(ModuleName.Reader, false);

        resultsDirectory = FileSystems.getDefault()
                .getPath(TestDirectory.results.toString(), MethodHandles.lookup().lookupClass().getSimpleName());

    }

    @BeforeClass
    public void runBeforeTestMethod() {
        IReader reader = new OpenCVReader();
        Path path = TestDirectory.CASIA_Image(0, TestDirectory.Eye.Left, 0);
        //imageData = reader.read(path);

        //need to prep everything, I'm checking the sum of all components:

    }

}
