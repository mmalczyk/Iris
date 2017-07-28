package LocaliserTest;/*
  Created by Magda on 22/06/2017.
 */

import main.display.Display;
import main.interfaces.ILocaliser;
import main.interfaces.IReader;
import main.localiser.OpenCVLocaliser;
import main.reader.OpenCVReader;
import main.settings.ModuleName;
import main.utils.ImageData;
import main.utils.TestDirectory;
import org.junit.Test;
import org.opencv.core.Core;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OpenCVLocaliserTest {

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        if (Display.moduleNotInDictionary(ModuleName.Reader))
            Display.displayModule(ModuleName.Reader, false);
        if (Display.moduleNotInDictionary(ModuleName.Localiser))
            Display.displayModule(ModuleName.Localiser, false);
    }

    @Test
    public void shortLocaliserTest() {
        localiserTest(10, "shortLocaliserTest.txt");
    }

    @Test
    public void longLocaliserTest() {
        localiserTest(50, "longLocaliserTest.txt");
    }

    public void localiserTest(int limit, String filename) {
        assert limit > 0;
        StatMap irisStatMap = new StatMap("iris stats");
        StatMap pupilStatMap = new StatMap("pupil stats");
        runLocalise(irisStatMap, pupilStatMap, TestDirectory.Eye.Left, limit);
        runLocalise(irisStatMap, pupilStatMap, TestDirectory.Eye.Right, limit);

        Path filePath = FileSystems.getDefault().getPath(TestDirectory.results.toString(), filename);
        writeCurrentDate(filePath, true);
        irisStatMap.writeToFile(filePath, true);
        pupilStatMap.writeToFile(filePath, true);
    }

    private void runLocalise(StatMap irisStatMap, StatMap pupilStatMap, TestDirectory.Eye side, int limit) {
        ILocaliser localiser = new OpenCVLocaliser();
        IReader reader = new OpenCVReader();
        for (int i = 0; i < limit; i++) {
            for (int j = 0; j < 10; j++) {
                Path path = TestDirectory.CASIA_Image(i, side, j);
                assert Files.exists(path);
                ImageData imageData = reader.read(path);
                imageData = localiser.localise(imageData);

                int irises = imageData.irisesFound();
                int pupils = imageData.pupilsFound();
                irisStatMap.increment(irises);
                pupilStatMap.increment(pupils);

                System.out.println(path.getFileName().toString() + " I: " + irises + " P: " + pupils);
            }
        }
    }

    private void writeCurrentDate(Path path, boolean append) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        try {
            //TODO findBugs is complaining about this line
            BufferedWriter out = new BufferedWriter(new FileWriter(path.toString(), append));
            out.write(dateFormat.format(new Date()) + "\n");
            out.close();
        } catch (IOException e) {
            //TODO log it
            System.out.println(e.getMessage());
        }
    }
}
