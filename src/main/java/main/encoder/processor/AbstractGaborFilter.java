package main.encoder.processor;

import main.encoder.gabor_kernel.Gabor;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;
import java.util.function.DoubleUnaryOperator;
import java.util.stream.DoubleStream;

import static org.opencv.core.CvType.CV_32F;

/**
 * Created by Magda on 11/07/2017.
 */
@SuppressWarnings("WeakerAccess")
abstract class AbstractGaborFilter implements IGaborFilter {

    protected int WAVELET_COUNT = 1;
    protected int FILTER_WIDTH = 5;
    protected int FILTER_HEIGHT = 5;

/*
    protected int FILTER_WIDTH = 31;
    protected int FILTER_HEIGHT = 31;
*/

/*
    protected int FILTER_WIDTH = 5;
    protected int FILTER_HEIGHT = 5;
*/

    protected GaborFilterType gaborFilterType;

    private DoubleStream getRange(int divisor) {
        //divisor = how many parts to divide a circle into
        double step = 360. / divisor;
        DoubleUnaryOperator f = operand -> operand + step;
        return DoubleStream.iterate(0, f).limit(divisor);
    }

    protected List<Mat> buildFiltersReal() {
        ArrayList<Mat> filters = new ArrayList<>();

        DoubleStream kernelStream = getRange(WAVELET_COUNT); //step is in how many parts to divide a circle
        kernelStream.forEach(theta -> {
/*
            Mat kernel = Imgproc.getGaborKernel(
                    new Size(FILTER_WIDTH, FILTER_HEIGHT),
                    6.0,
                    theta, //orientation
                    2.0,
                    0.5,
                    0,
                    CV_32F
            );
*/
            Mat kernel = Imgproc.getGaborKernel(
                    new Size(FILTER_WIDTH, FILTER_HEIGHT),
                    24.,
                    theta, //orientation
                    30,
                    1,
                    0,
                    CV_32F
            );

            //TODO that was the suggested normalisation in the link but what's a good way
//            Core.divide(kernel, Core.sumElems(kernel).mul(new Scalar(1.5)), kernel); //kern /= 1.5*kern.sum()
            filters.add(kernel);
        });
        return filters;
    }

    protected List<Mat> buildFiltersImaginary() {
        ArrayList<Mat> filters = new ArrayList<>();

        DoubleStream kernelStream = getRange(WAVELET_COUNT); //step is in how many parts to divide a circle
        kernelStream.forEach(theta -> {
/*
            Mat kernel = Gabor.getRealGaborKernel(
                    new Size(FILTER_WIDTH, FILTER_HEIGHT),
                    24.,
                    theta, //orientation
                    30,
                    1,
                    0,
                    CV_32F
            );
*/

            Mat kernel = Gabor.getImaginaryGaborKernel(
                    new Size(FILTER_WIDTH, FILTER_HEIGHT),
                    24.,
                    theta, //orientation
                    30,
                    1,
                    0,
                    CV_32F
            );

            //TODO that was the suggested normalisation in the link but what's a good way
//            Core.divide(kernel, Core.sumElems(kernel).mul(new Scalar(1.5)), kernel); //kern /= 1.5*kern.sum()
            filters.add(kernel);
        });
        return filters;

    }

    @Override
    public GaborFilterType getType() {
        return gaborFilterType;
    }

    protected Mat concat(Mat matA, Mat matB) {
        assert matA.width() == matB.width();
        assert matA.height() == matB.height();
        assert matA.type() == matB.type();
        int width = matA.width();
        int height = matA.height();
        int type = matA.type();
        Mat matC = new Mat(height, width * 2, type);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                double[] pixel = matA.get(i, j);
                matC.put(i, 2 * j, pixel);
                pixel = matB.get(i, j);
                matC.put(i, 2 * j + 1, pixel);
            }
        }
        return matC;
    }
}
