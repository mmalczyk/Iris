/*
  Created by Magda on 22/06/2017.
 */

import main.Utils.ImageData;
import main.interfaces.ILocaliser;
import main.interfaces.IReader;
import main.localiser.JavaCVLocaliser;
import main.reader.SimpleReader;
import org.junit.Test;

import java.nio.file.FileSystems;
import java.nio.file.Path;

public class OpenCVLocaliserTest {

    //TODO later: test on all pictures in CASIA
    @Test
    public void localiserTest(){
        //TODO yeah openCVLocaliser definitely needs improvement
        //TODO some statistics
        //run Localiser
        String src = "./src\\main\\resources\\CASIA-Iris-Thousand\\CASIA-Iris-Thousand\\000\\L\\S5000L0";
        ILocaliser localiser = new JavaCVLocaliser();
        localiser.setShowResults(true);
        IReader reader = new SimpleReader();

        String currentSrc;
        for (int i=0; i<10; i++){
            currentSrc = src + i + ".jpg";

            Path path = FileSystems.getDefault().getPath(currentSrc);
            ImageData imageData = reader.read(path);
            localiser.localise(imageData);
        }

        //run localiser on different pictures
        //show results somehow
    }
}
