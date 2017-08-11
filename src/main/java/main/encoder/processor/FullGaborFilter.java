package main.encoder.processor;

import main.utils.MatConstants;
import org.opencv.core.Core;
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
        List<Mat> filters = buildFiltersReal();
        List<Mat> resultSteps = new ArrayList<>();

        Mat accumulator = new Mat(image.size(), image.type(), new Scalar(0));
        Mat filteredImage = accumulator.clone();
        for (Mat kernel : filters) {
            filter2D(image, filteredImage, MatConstants.TYPE, kernel);
            Core.max(accumulator, filteredImage, accumulator);
            resultSteps.add(accumulator.clone());
        }

//        return accumulator;
        return resultSteps;
    }

}
