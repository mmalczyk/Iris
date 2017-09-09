import main.encoder.processor.FullGaborFilter;
import main.interfaces.IReader;
import main.reader.OpenCVReader;
import main.utils.ImageUtils;
import org.junit.Before;
import org.junit.Test;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;

import java.util.List;

public class GaborKernelTest extends BaseTest {

    private IReader reader;
    private FullGaborFilter gaborFilter;

    @Before
    public void beforeTest() {
        clearResultsDirectory();
        makeResultsDirectory();
        reader = new OpenCVReader();
        gaborFilter = new FullGaborFilter();

    }


    @Test
    public void parameterTest() {
        int WAVELET_COUNT = 1;
        int FILTER_WIDTH = 5;
        int FILTER_HEIGHT = 5;
        for (double lambda = 2.; lambda < 100.; lambda += 5) {
            double gamma = 2.;
            double psi = 0.0;
            double sigma = 2.;
            singleRun(WAVELET_COUNT, FILTER_WIDTH, FILTER_HEIGHT, sigma, lambda, gamma, psi);
        }
    }

    @Test
    public void evenFilterSizeTest() {
        int wavelet_count = 1;
        int filter_width = 10;
        int filter_height = 10;
        int lambda = 50;
        double gamma = 2.;
        double psi = 0.5;
        double sigma = 2.;

        List<Mat> realFilters = gaborFilter.buildFiltersReal(wavelet_count, filter_width, filter_height, sigma, lambda, gamma, psi);
        List<Mat> imaginaryFilters = gaborFilter.buildFiltersImaginary(wavelet_count, filter_width, filter_height, sigma, lambda, gamma, psi);

        int sizeReal = realFilters.size();
        int sizeImaginary = imaginaryFilters.size();
        assert sizeReal == sizeImaginary;
        assert realFilters.get(0).size().equals(imaginaryFilters.get(0).size());
    }

    private void singleRun(int wavelet_count, int filter_width, int filter_height, double sigma, double lambda, double gamma, double psi) {
        List<Mat> realFilters = gaborFilter.buildFiltersReal(wavelet_count, filter_width, filter_height, sigma, lambda, gamma, psi);
        List<Mat> imaginaryFilters = gaborFilter.buildFiltersImaginary(wavelet_count, filter_width, filter_height, sigma, lambda, gamma, psi);

        assert realFilters.size() == imaginaryFilters.size();
        int size = realFilters.size();
        for (int i = 0; i < size; i++) {
            Mat kernelReal = realFilters.get(i);
            Mat kernelImaginary = imaginaryFilters.get(i);


            Mat kernelRealScaled = new Mat(kernelReal.size(), kernelReal.type());
            Mat kernelImaginaryScaled = new Mat(kernelImaginary.size(), kernelImaginary.type());
            Core.multiply(kernelReal, new Scalar(100), kernelRealScaled);
            Core.multiply(kernelImaginary, new Scalar(100), kernelImaginaryScaled);


            ImageUtils.writeToFile(kernelRealScaled, getResultsDirectory(), "kR_TH" + i + "sgm" + sigma + "lm" + lambda + "gm" + gamma + "psi" + psi + ".jpg");
            ImageUtils.writeToFile(kernelImaginaryScaled, getResultsDirectory(), "kI_TH" + i + "sgm" + sigma + "lm" + lambda + "gm" + gamma + "psi" + psi + ".jpg");

        }

    }

}
