package main.encoder.processor;

import main.Utils.FilterConstants;
import main.Utils.MatConstants;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;

import java.util.List;

import static org.opencv.imgproc.Imgproc.filter2D;

/**
 * Created by Magda on 11/07/2017.
 */
public class SelectiveGaborFilter extends AbstractGaborFilter {

    //TODO how do I test this

    public SelectiveGaborFilter(FilterConstants filterConstants) {
        super(filterConstants);
        gaborFilterType = GaborFilterType.SELECTIVE;
    }

    private Mat filter2DSelectively(Mat src, Mat kernel) {
        //TODO filter selected areas
        Mat srcFiltered = src.clone();
        filter2D(src, srcFiltered, MatConstants.TYPE, kernel);

        Mat dest = new Mat(filterConstants.FILTERS_IN_COL, filterConstants.FILTERS_IN_ROW, src.type());


        for (int i = filterConstants.FILTER_SIZE / 2; i < src.rows(); i += filterConstants.FILTER_SIZE) {
            for (int j = filterConstants.FILTER_SIZE / 2; j < src.cols(); j += filterConstants.FILTER_SIZE) {

                double[] result = srcFiltered.get(i, j);
                assert result.length == 1; //greyscale
                dest.put(i / filterConstants.FILTER_SIZE, j / filterConstants.FILTER_SIZE, result);

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
        dest = new Mat (filterConstants.FILTERS_IN_ROW, filterConstants.FILTERS_IN_COL, src.type());

        for (int i=filterConstants.FILTER_SIZE/2; i<src.rows(); i += filterConstants.FILTER_SIZE)
        {
            for (int j=filterConstants.FILTER_SIZE/2; j<src.cols(); j += filterConstants.FILTER_SIZE){
            }
        }
*/
    }

    @Override
    public Mat process(Mat image) {
        List<Mat> filters = buildFiltersReal();

        Mat accumulator = new Mat(
                new Size(filterConstants.FILTERS_IN_ROW, filterConstants.FILTERS_IN_COL),
                image.type(),
                new Scalar(0, 0, 0));
        Mat filteredImage;
        for (Mat kernel : filters) {
            filteredImage = filter2DSelectively(image, kernel);
            Core.max(accumulator, filteredImage, accumulator);
        }

        return accumulator;
    }
}
