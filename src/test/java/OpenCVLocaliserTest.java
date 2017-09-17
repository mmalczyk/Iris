/*
  Created by Magda on 22/06/2017.
 */

import main.interfaces.ILocaliser;
import main.interfaces.IReader;
import main.localiser.OpenCVLocaliser;
import main.reader.OpenCVReader;
import main.utils.ImageData;
import main.utils.ImageUtils;
import main.utils.StatMap;
import main.utils.TestDirectory;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OpenCVLocaliserTest extends BaseTest {

    public OpenCVLocaliserTest() {
        clearResultsDirectory();
        makeResultsDirectory();
    }

    @Test
    public void shortLocaliserTest() {
        localiserTest(5, "shortLocaliserTest");
    }

/*
    @Test
    public void longLocaliserTest()
    {
        localiserTest(10, "longLocaliserTest");
    }
*/

    public void localiserTest(int limit, String testName) {
        assert limit > 0;
        Path directoryPath = FileSystems.getDefault().getPath(getResultsDirectory().toString(), testName);
        (new java.io.File(directoryPath.toString())).mkdirs();

        StatMap irisStatMap = new StatMap("iris stats");
        StatMap pupilStatMap = new StatMap("pupil stats");
        runLocalise(irisStatMap, pupilStatMap, TestDirectory.Eye.Left, limit, directoryPath);
        runLocalise(irisStatMap, pupilStatMap, TestDirectory.Eye.Right, limit, directoryPath);

        Path filePath = FileSystems.getDefault().getPath(TestDirectory.results.toString(), testName + ".txt");
        writeCurrentDate(filePath, true);
        irisStatMap.writeToFile(filePath, true);
        pupilStatMap.writeToFile(filePath, true);
    }

    private void runLocalise(StatMap irisStatMap, StatMap pupilStatMap, TestDirectory.Eye side, int limit,
                             Path directoryPath) {
        ILocaliser localiser = new OpenCVLocaliser();
        IReader reader = new OpenCVReader();
        for (int i = 0; i < limit; i++) {
            for (int j = 0; j < 5; j++) {
                Path path = getImagePath(i, side, j);
                if (Files.exists(path)) {
                    ImageData imageData = reader.read(path);
                    imageData = localiser.localise(imageData);

                    int irises = imageData.irisesFound();
                    int pupils = imageData.pupilsFound();
                    irisStatMap.increment(irises);
                    pupilStatMap.increment(pupils);

                    System.out.println(path.getFileName().toString() + " I: " + irises + " P: " + pupils);
                    if (irises > 0 && pupils > 0) {
                        ImageUtils.writeToFile(
                                imageData.getImageWithMarkedCircles(),
                                directoryPath,
                                path.getFileName().toString());
                    }
                }
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
