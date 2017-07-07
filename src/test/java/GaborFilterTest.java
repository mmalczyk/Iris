import main.Utils.ImageData;
import main.Utils.ImageUtils;
import main.interfaces.IReader;
import main.reader.OpenCVReader;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.DoubleUnaryOperator;
import java.util.stream.DoubleStream;

import static org.opencv.core.CvType.CV_32F;

/**
 * Created by Magda on 05/07/2017.
 */
public class GaborFilterTest {

    static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }

    //    https://cvtuts.wordpress.com/2014/04/27/gabor-filters-a-practical-overview/ -> gabor filter parameters info

    public DoubleStream getRange(double start, double end, double step) {
        DoubleUnaryOperator f = operand -> operand + Math.PI / step;
        return DoubleStream.iterate(start, f).limit((int) end);
    }

    public List<Mat> buildFilters() {
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

    public Mat process(Mat image, List<Mat> filters) {
        Mat accumulator = new Mat(image.size(), image.type(), new Scalar(0, 0, 0));
        Mat filteredImage = accumulator.clone();
        for (Mat kernel : filters) {
            Imgproc.filter2D(image, filteredImage, CvType.CV_8UC3, kernel);
            Core.max(accumulator, filteredImage, accumulator);
        }
        return accumulator;
    }

    public static void main(String[] args) {
        GaborFilterTest gabor = new GaborFilterTest();

        Path path = Paths.get(TestDirectory.images.toString(), "norm.jpg");
        IReader reader = new OpenCVReader();
        ImageData imageData = reader.read(path);
        Mat image = imageData.getImageMat();

        List<Mat> filters = gabor.buildFilters();
        Mat result = gabor.process(image, filters);

        ImageUtils.showBufferedImage(ImageUtils.matToBufferedImage(image), "original");
        ImageUtils.showBufferedImage(ImageUtils.matToBufferedImage(result), "gabor filter");

        Imgproc.threshold(result, result, 0, 255, Imgproc.THRESH_BINARY);

        ImageUtils.showBufferedImage(ImageUtils.matToBufferedImage(result), "binarised");

    }
}