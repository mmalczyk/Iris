/*
  Created by Magda on 22/06/2017.
 */

import main.display.Display;
import main.interfaces.ILocaliser;
import main.interfaces.IReader;
import main.localiser.OpenCVLocaliser;
import main.reader.OpenCVReader;
import main.utils.ImageData;
import org.junit.Test;
import org.opencv.core.Core;

import java.nio.file.FileSystems;
import java.nio.file.Path;

public class OpenCVLocaliserTest {

    //TODO later: test on pictures in CASIA

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        if (Display.moduleNotInDictionary(OpenCVLocaliser.class))
            Display.displayModule(OpenCVLocaliser.class, false);
    }

    @Test
    public void localiserTest() {
        //TODO yeah openCVLocaliser definitely needs improvement
        //TODO some statistics
        //run Localiser
        String src = "./src\\main\\resources\\CASIA-Iris-Thousand\\000\\L\\S5000L0";
        ILocaliser localiser = new OpenCVLocaliser();
//        localiser.showResults(true);
        IReader reader = new OpenCVReader();

        String currentSrc;
        for (int i = 0; i < 10; i++) {
            currentSrc = src + i + ".jpg";

            Path path = FileSystems.getDefault().getPath(currentSrc);
            ImageData imageData = reader.read(path);
            localiser.localise(imageData);
        }

        //run localiser on different pictures
        //show results somehow
    }
}
