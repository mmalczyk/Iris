import main.encoder.OpenCVEncoder;
import main.encoder.processor.GaborFilterType;
import main.interfaces.IReader;
import main.reader.OpenCVReader;
import main.utils.FilterConstants;
import main.utils.ImageData;
import main.utils.ImageUtils;
import main.utils.TestDirectory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.opencv.core.Mat;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.List;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class GaborFilterTest extends BaseTest {

    private IReader reader;
    private FilterConstants filterConstants = new FilterConstants();

    public GaborFilterTest() {
        clearResultsDirectory();
        makeResultsDirectory();
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
        Path directory = FileSystems.getDefault().getPath(getResultsDirectory().toString(), filterType.toString());
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
        GaborFilterType filterType = GaborFilterType.GRID;
        writeResultsToFile(lenaTest(filterType), filterType);
    }

    @Test
    public void countFullResultsTest() {
        List<Mat> results = lenaTest(GaborFilterType.FULL);
        Assert.assertEquals(filterConstants.WAVELET_COUNT, results.size());
    }

    @Test
    public void countSelectiveResultsTest() {
        List<Mat> results = lenaTest(GaborFilterType.GRID);
        Assert.assertEquals(filterConstants.WAVELET_COUNT, results.size());
    }


}
