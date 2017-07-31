import main.display.Display;
import main.encoder.OpenCVEncoder;
import main.encoder.processor.GaborFilterType;
import main.interfaces.IReader;
import main.reader.OpenCVReader;
import main.settings.ModuleName;
import main.utils.FilterConstants;
import main.utils.ImageData;
import main.utils.ImageUtils;
import main.utils.TestDirectory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opencv.core.Core;
import org.opencv.core.Mat;

import java.io.File;
import java.lang.invoke.MethodHandles;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.List;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class GaborFilterTest {

    private static Path resultsDirectory;
    private IReader reader;
    private FilterConstants filterConstants = new FilterConstants();

    @BeforeClass
    public static void beforeClass() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        if (Display.moduleNotInDictionary(ModuleName.Encoder))
            Display.displayModule(ModuleName.Encoder, false);
        if (Display.moduleNotInDictionary(ModuleName.Reader))
            Display.displayModule(ModuleName.Reader, false);

        //TODO move this thing to TestDirectory
        resultsDirectory = FileSystems.getDefault()
                .getPath(TestDirectory.results.toString(), MethodHandles.lookup().lookupClass().getSimpleName());

        clearResultsDirectory(new File(resultsDirectory.toString()));
    }

    private static void clearResultsDirectory(File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) clearResultsDirectory(file);
                //noinspection ResultOfMethodCallIgnored
                file.delete();
            }
        }
    }

    @Before
    public void beforeTest() {
        reader = new OpenCVReader();
    }


    private List<Mat> lenaTest(GaborFilterType filterType) {
        Path fileName = FileSystems.getDefault()
                .getPath(TestDirectory.images.toString(), "lena.jpg");

        ImageData imageData = reader.read(fileName);
        imageData.setNormMat(imageData.getImageMat()); //OpenCVEncoder works on normalised mat
        imageData.setGaborFilterType(filterType);
        imageData.setFilterConstants(new FilterConstants());

        OpenCVEncoder encoder = new OpenCVEncoder();
        encoder.encode(imageData);
        return encoder.getResults();
    }

    private void writeResultsToFile(List<Mat> results, GaborFilterType filterType) {
        Path directory = FileSystems.getDefault().getPath(resultsDirectory.toString(), filterType.toString());
        (new java.io.File(directory.toString())).mkdirs();
        for (Mat result : results)
            ImageUtils.writeToFile(result, directory, "lena" + results.indexOf(result) + ".jpg");
    }

    @Test
    public void fullFilterLenaTest() {

        GaborFilterType filterType = GaborFilterType.FULL;
        writeResultsToFile(lenaTest(filterType), filterType);
    }

    @Test
    public void selectiveFilterLenaTest() {
        GaborFilterType filterType = GaborFilterType.SELECTIVE;
        writeResultsToFile(lenaTest(filterType), filterType);
    }

    @Test
    public void countFullResultsTest() {
        List<Mat> results = lenaTest(GaborFilterType.FULL);
        Assert.assertEquals(filterConstants.WAVELET_COUNT, results.size());
    }

    @Test
    public void countSelectiveResultsTest() {
        List<Mat> results = lenaTest(GaborFilterType.SELECTIVE);
        Assert.assertEquals(filterConstants.WAVELET_COUNT, results.size());
    }


}
