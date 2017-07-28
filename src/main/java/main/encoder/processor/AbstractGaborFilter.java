package main.encoder.processor;

import main.utils.FilterConstants;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
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

    protected final FilterConstants filterConstants;
    protected GaborFilterType gaborFilterType;

    public AbstractGaborFilter(FilterConstants filterConstants) {
        this.filterConstants = filterConstants;
    }

    private DoubleStream getRange(double divisor) {
        //divisor = how many parts to divide a circle into
        double step = 360. / divisor;
        DoubleUnaryOperator f = operand -> operand + step;
        return DoubleStream.iterate(0, f).limit(360);
    }

    protected List<Mat> buildFiltersReal() {
        //TODO I'm computing only the real gabor filter for now; Daugman included the imaginary part in the code
        ArrayList<Mat> filters = new ArrayList<>();

        DoubleStream kernelStream = getRange(16.); //step is in how many parts to divide a circle
        kernelStream.forEach(theta -> {
            Mat kernel = Imgproc.getGaborKernel(
                    new Size(filterConstants.FILTER_WIDTH, filterConstants.FILTER_HEIGHT),
                    6.0,
                    theta, //orientation
                    2.0,
                    0.5,
                    0,
                    CV_32F
            );
            //TODO that was the suggested normalisation in the link but what's a good way
            Core.divide(kernel, Core.sumElems(kernel).mul(new Scalar(1.5)), kernel); //kern /= 1.5*kern.sum()
            filters.add(kernel);
        });
        return filters;
    }

    @Override
    public GaborFilterType getType() {
        return gaborFilterType;
    }
}
