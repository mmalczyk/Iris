package main.encoder.processor;

import main.utils.FilterConstants;
import main.utils.MatConstants;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;

import java.util.List;

import static org.opencv.imgproc.Imgproc.filter2D;

/**
 * Created by Magda on 11/07/2017.
 */
public class FullGaborFilter extends AbstractGaborFilter {

    public FullGaborFilter(FilterConstants filterConstants) {
        super(filterConstants);
        gaborFilterType = GaborFilterType.FULL;
    }

    @Override
    public Mat process(Mat image) {
        List<Mat> filters = buildFiltersReal();

        Mat accumulator = new Mat(image.size(), image.type(), new Scalar(0, 0, 0));
        Mat filteredImage = accumulator.clone();
        for (Mat kernel : filters) {
            filter2D(image, filteredImage, MatConstants.TYPE, kernel);
            Core.max(accumulator, filteredImage, accumulator);
        }

        return accumulator;
    }

}
