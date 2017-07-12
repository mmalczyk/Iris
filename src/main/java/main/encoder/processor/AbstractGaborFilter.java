package main.encoder.processor;

import main.Utils.FilterConstants;
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
public abstract class AbstractGaborFilter implements IGaborFilter {

    protected FilterConstants filterConstants;
    protected GaborFilterType gaborFilterType;

    public AbstractGaborFilter(FilterConstants filterConstants) {
        this.filterConstants = filterConstants;
    }

    protected DoubleStream getRange(double start, double end, double step) {
        //step is in how many parts to divide a circle
//        DoubleUnaryOperator f = operand -> operand + 2*Math.PI / step;
        DoubleUnaryOperator f = operand -> operand + Math.PI / step;
        return DoubleStream.iterate(start, f).limit((int) end);
    }

    protected List<Mat> buildFiltersReal() {
        //TODO I'm computing only the real gabor filter for now; Daugman included the imaginary part in the code
        ArrayList<Mat> filters = new ArrayList<>();
//        int kSize = 31;
        int kSize = filterConstants.FILTER_SIZE;

//        DoubleStream kernelStream = getRange(0, Math.PI, 16.);
        DoubleStream kernelStream = getRange(0, Math.PI, 64.); //step is in how many parts to divide a circle
        kernelStream.forEach(theta -> {
            Mat kernel = Imgproc.getGaborKernel(new Size(kSize, kSize), 6.0, theta, 10.0, 0.5, 0, CV_32F);
//            Mat kernel = Imgproc.getGaborKernel(new Size(kSize, kSize), 4.0, theta, 10.0, 0.5, 0, CV_32F);
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
