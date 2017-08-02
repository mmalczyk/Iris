import main.display.Display;
import main.settings.ModuleName;
import main.utils.TestDirectory;
import org.opencv.core.Core;

import java.lang.invoke.MethodHandles;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public abstract class BaseTest {

    protected static final Path resultsDirectory;

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        if (Display.moduleNotInDictionary(ModuleName.Reader))
            Display.displayModule(ModuleName.Reader, false);
        if (Display.moduleNotInDictionary(ModuleName.Localiser))
            Display.displayModule(ModuleName.Localiser, false);
        if (Display.moduleNotInDictionary(ModuleName.Normaliser))
            Display.displayModule(ModuleName.Normaliser, false);
        if (Display.moduleNotInDictionary(ModuleName.Encoder))
            Display.displayModule(ModuleName.Encoder, false);
        if (Display.moduleNotInDictionary(ModuleName.Comparator))
            Display.displayModule(ModuleName.Comparator, false);
        if (Display.moduleNotInDictionary(ModuleName.Writer))
            Display.displayModule(ModuleName.Writer, false);

        resultsDirectory = FileSystems.getDefault()
                .getPath(TestDirectory.results.toString(), MethodHandles.lookup().lookupClass().getSimpleName());

    }

}
