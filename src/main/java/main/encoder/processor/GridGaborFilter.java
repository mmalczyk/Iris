package main.encoder.processor;

import main.encoder.ByteCode;
import main.utils.ImageData;
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
public class GridGaborFilter extends AbstractGaborFilter {

    //TODO how do I test this

    public GridGaborFilter() {
        gaborFilterType = GaborFilterType.GRID;
    }

    private Mat filter2DSelectively(Mat src, Mat kernelReal, Mat kernelImaginary) {
        Mat srcFilteredReal = src.clone();
        Mat srcFilteredImaginary = src.clone();
        filter2D(src, srcFilteredReal, -1, kernelReal);
        filter2D(src, srcFilteredImaginary, -1, kernelImaginary);

        Mat destReal = new Mat(FilterConstants.CODE_SIZE, src.type());
        Mat destImaginary = new Mat(FilterConstants.CODE_SIZE, src.type());
        for (int i = 0; i < FilterConstants.CODE_HEIGHT; i++) {
            for (int j = 0; j < FilterConstants.CODE_WIDTH; j++) {

                double[] resultReal = getMeanFilterResult(srcFilteredReal, i, j);
                double[] resultImaginary = getMeanFilterResult(srcFilteredImaginary, i, j);
                destReal.put(i, j, resultReal);
                destImaginary.put(i, j, resultImaginary);
            }
        }

        return concat(destReal, destImaginary);

    }

    private double[] getMeanFilterResult(Mat mat, int i_height, int j_width) {
        int i = i_height * FILTER_HEIGHT;
        int j = j_width * FILTER_WIDTH;
        int max_i = (i_height + 1) * FILTER_HEIGHT;
        int max_j = (j_width + 1) * FILTER_WIDTH;
        double result = 0;
        for (; i < max_i && i < mat.height(); i++) {
            for (; j < max_j && j < mat.width(); j++) {
                result += mat.get(i, j)[0];
            }
        }
        result = result / (FILTER_HEIGHT * FILTER_WIDTH);
        return new double[]{result};
    }

    @Override
    public List<Mat> process(ImageData imageData) {
        Mat image = imageData.getNormMat();
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
//            ImageUtils.showImage("kernelReal" + i, kernelRealScaled, 5);
//            ImageUtils.showImage("kernelImaginary" + i, kernelImaginaryScaled, 5);

            Mat result = filter2DSelectively(image, kernelReal, kernelImaginary);
            resultSteps.add(result);
        }

        Mat firstResult = resultSteps.get(0);
        Mat enhanced = new Mat(firstResult.size(), firstResult.type());
        Core.addWeighted(enhanced, 0, firstResult, 1, 0, enhanced);
        for (int i = 1; i < resultSteps.size(); i++)
            Core.addWeighted(enhanced, 1, resultSteps.get(i), 1, 0, enhanced);

        // resultSteps.add(enhanced);

        // save result to imageData
        imageData.setCodeMatForm(enhanced);
        imageData.SetCodeMatCount(1);
        imageData.setByteCode(new ByteCode(enhanced));
        return resultSteps;
    }
}
