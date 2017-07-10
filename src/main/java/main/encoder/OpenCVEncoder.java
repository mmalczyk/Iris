package main.encoder;

import main.Utils.ImageData;
import main.interfaces.IEncoder;
import main.writer.Display;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import java.util.*;
import java.util.function.DoubleUnaryOperator;
import java.util.stream.DoubleStream;

import static org.opencv.core.CvType.CV_32F;

/**
 * Created by Magda on 04/07/2017.
 */
public class OpenCVEncoder extends Display implements IEncoder{

    //TODO are my mat types okay?

//    https://cvtuts.wordpress.com/2014/04/27/gabor-filters-a-practical-overview/ -> gabor filter parameters info
//    http://docs.opencv.org/3.0-beta/modules/imgproc/doc/filtering.html

    static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }

    //TODO learn about phase information
    //    https://cvtuts.wordpress.com/2014/04/27/gabor-filters-a-practical-overview/ -> gabor filter parameters info

    private DoubleStream getRange(double start, double end, double step) {
        DoubleUnaryOperator f = operand -> operand + Math.PI / step;
        return DoubleStream.iterate(start, f).limit((int) end);
    }

    private java.util.List<Mat> buildFilters() {
        ArrayList<Mat> filters = new ArrayList<>();
        int kSize = 31;

//        DoubleStream kernelStream = getRange(0, Math.PI, 16.);
        DoubleStream kernelStream = getRange(0, Math.PI, 64.);
        kernelStream.forEach(theta -> {
            Mat kernel = Imgproc.getGaborKernel(new Size(kSize, kSize), 6.0, theta, 10.0, 0.5, 0, CV_32F);
//            Mat kernel = Imgproc.getGaborKernel(new Size(kSize, kSize), 4.0, theta, 10.0, 0.5, 0, CV_32F);
            Core.divide(kernel, Core.sumElems(kernel).mul(new Scalar(1.5)), kernel); //kern /= 1.5*kern.sum()
            filters.add(kernel);
        });
        return filters;
    }

    private Mat processImage(Mat image, java.util.List<Mat> filters) {
        //TODO run filter2d only on particular points? or not?

        Mat accumulator = new Mat(image.size(), image.type(), new Scalar(0, 0, 0));
        Mat filteredImage = accumulator.clone();
        for (Mat kernel : filters) {
            Imgproc.filter2D(image, filteredImage, CvType.CV_8UC3, kernel);
            Core.max(accumulator, filteredImage, accumulator);
        }
        return accumulator;
    }



    @Override
    public byte[] encode(ImageData imageData)
    {
        Mat image = imageData.getNormMat();

        java.util.List<Mat> filters = buildFilters();
        Mat result = processImage(image, filters);

        displayIf(image, "original");
        displayIf(result, "gabor filter");

        ByteCode code = new ByteCode(result);

        Mat display = new Mat(image.width(), image.cols(), image.type());
        Imgproc.threshold(result, display, 0, 255, Imgproc.THRESH_BINARY);

        displayIf(display, "binarised");

        code.display();

        //TODO return value
        return null;
    }
}
