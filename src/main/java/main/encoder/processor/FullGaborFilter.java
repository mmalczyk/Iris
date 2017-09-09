package main.encoder.processor;

import main.utils.ImageUtils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;

import java.util.ArrayList;
import java.util.List;

import static org.opencv.imgproc.Imgproc.filter2D;

/**
 * Created by Magda on 11/07/2017.
 */
public class FullGaborFilter extends AbstractGaborFilter {

    public FullGaborFilter() {
        gaborFilterType = GaborFilterType.FULL;
    }

    public List<Mat> process(Mat image) {
        image.convertTo(image, CvType.CV_32F); //gabor kernels work with this type

        List<Mat> realFilters = buildFiltersReal();
        List<Mat> imaginaryFilters = buildFiltersImaginary();
        List<Mat> resultSteps = new ArrayList<>();

        assert realFilters.size() == imaginaryFilters.size();
        int size = realFilters.size();

        for (int i = 0; i < size; i++) {
            Mat kernelReal = realFilters.get(i);
            Mat kernelImaginary = imaginaryFilters.get(i);

            Mat kernelRealScaled = new Mat(kernelReal.size(), kernelReal.type());
            Mat kernelImaginaryScaled = new Mat(kernelImaginary.size(), kernelImaginary.type());
            Core.multiply(kernelReal, new Scalar(100), kernelRealScaled);
            Core.multiply(kernelImaginary, new Scalar(100), kernelImaginaryScaled);
            ImageUtils.showImage("kernelReal" + i, kernelRealScaled, 5);
            ImageUtils.showImage("kernelImaginary" + i, kernelImaginaryScaled, 5);

            Mat result = filter2DComplex(image, kernelReal, kernelImaginary);
            resultSteps.add(result);
        }

        Mat firstResult = resultSteps.get(0);
        Mat enhanced = new Mat(firstResult.size(), firstResult.type());
        Core.addWeighted(enhanced, 0, firstResult, 1, 0, enhanced);
        for (int i = 1; i < resultSteps.size(); i++)
            Core.addWeighted(enhanced, 1, resultSteps.get(i), 1, 0, enhanced);
        resultSteps.add(enhanced);

        return resultSteps;
    }

    private Mat filter2DComplex(Mat src, Mat kernelReal, Mat kernelImaginary) {
        //TODO filter selected areas
        Mat srcFilteredReal = src.clone();
        Mat srcFilteredImaginary = src.clone();
        filter2D(src, srcFilteredReal, -1, kernelReal);
        filter2D(src, srcFilteredImaginary, -1, kernelImaginary);

        return concat(srcFilteredReal, srcFilteredImaginary);
    }

}
