import main.display.Display;
import main.settings.ModuleName;
import main.utils.TestDirectory;
import org.opencv.core.Core;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public abstract class BaseTest {

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

    }

    protected Path getResultsDirectory() {
        return FileSystems.getDefault()
                .getPath(TestDirectory.results.toString(), this.getClass().getSimpleName());
    }

    protected void clearResultsDirectory() {
        File directory = new File(getResultsDirectory().toString());
        clearDirectory(directory);
    }

    private void clearDirectory(File directory) {
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) clearDirectory(file);
                    //noinspection ResultOfMethodCallIgnored
                    file.delete();
                }
            }
        } else
            (new java.io.File(directory.toString())).mkdirs();
    }

    protected void makeResultsDirectory() {
        (new java.io.File(getResultsDirectory().toString())).mkdirs();
    }

}
