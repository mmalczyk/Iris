package main.encoder.processor;

import main.utils.FilterConstants;
import main.utils.MatConstants;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;

import java.util.ArrayList;
import java.util.List;

import static org.opencv.imgproc.Imgproc.filter2D;

/**
 * Created by Magda on 11/07/2017.
 */
public class SelectiveGaborFilter extends AbstractGaborFilter {

    //TODO how do I test this

    public SelectiveGaborFilter(FilterConstants filterConstants) {
        super(filterConstants);
        gaborFilterType = GaborFilterType.GRID;
    }

    private Mat filter2DSelectively(Mat src, Mat kernel) {
        //TODO filter selected areas
        Mat srcFiltered = src.clone();
        filter2D(src, srcFiltered, MatConstants.TYPE, kernel);

        Mat dest = new Mat(filterConstants.CODE_HEIGHT, filterConstants.CODE_WIDTH, src.type());


        for (int i = filterConstants.FILTER_HEIGHT / 2; i < src.height(); i += filterConstants.FILTER_HEIGHT) {
            for (int j = filterConstants.FILTER_WIDTH / 2; j < src.width(); j += filterConstants.FILTER_WIDTH) {

                double[] result = srcFiltered.get(i, j);
                assert result.length == 1; //greyscale
                dest.put(i / filterConstants.FILTER_HEIGHT, j / filterConstants.FILTER_WIDTH, result);

                //TODO filter selected areas
/*
                Mat onePixelSrcROI = new Mat(
                        src,
                        new Rect(new Point (j, i), new Size(1, 1) ));

                double[] sourcePixel = onePixelSrcROI.get(0,0);

                filter2D(onePixelSrcROI,
                        onePixelSrcROI,
                        -1,
                        kernel);

                double[] result = onePixelSrcROI.get(0,0);
                assert result.length == 3;
                dest.put(i/filterConstants.FILTER_SIZE, j/filterConstants.FILTER_SIZE, result);
*/
            }
        }


        return dest;
        /*
        dest = new Mat (filterConstants.CODE_WIDTH, filterConstants.CODE_HEIGHT, src.type());

        for (int i=filterConstants.FILTER_SIZE/2; i<src.rows(); i += filterConstants.FILTER_SIZE)
        {
            for (int j=filterConstants.FILTER_SIZE/2; j<src.cols(); j += filterConstants.FILTER_SIZE){
            }
        }
*/
    }

    @Override
    public List<Mat> process(Mat image) {
        List<Mat> filters = buildFiltersReal();
        List<Mat> resultSteps = new ArrayList<>();

        Mat accumulator = new Mat(
                new Size(filterConstants.CODE_WIDTH, filterConstants.CODE_HEIGHT),
                image.type(),
                new Scalar(0, 0, 0));
        Mat filteredImage;
        for (Mat kernel : filters) {
            filteredImage = filter2DSelectively(image, kernel);
            Core.max(accumulator, filteredImage, accumulator);
            resultSteps.add(accumulator.clone());
        }

//        return accumulator;
        return resultSteps;
    }
}
