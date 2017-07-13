package main.encoder;

import main.encoder.processor.GaborFilterFactory;
import main.encoder.processor.IGaborFilter;
import main.interfaces.IEncoder;
import main.utils.ImageData;
import main.writer.Display;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

/**
 * Created by Magda on 04/07/2017.
 */
public class OpenCVEncoder extends Display implements IEncoder {

//    https://cvtuts.wordpress.com/2014/04/27/gabor-filters-a-practical-overview/ -> gabor filter parameters info
//    http://docs.opencv.org/3.0-beta/modules/imgproc/doc/filtering.html

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    //TODO learn about phase information
    //    https://cvtuts.wordpress.com/2014/04/27/gabor-filters-a-practical-overview/ -> gabor filter parameters info

    @Override
    public ByteCode encode(ImageData imageData) {
        Mat image = imageData.getNormMat();

        IGaborFilter gaborFilter = GaborFilterFactory.getFilter(imageData);
        Mat result = gaborFilter.process(image);

        displayIf(image, "original");
        displayIf(result, "gabor filter", 3);

        ByteCode code = new ByteCode(result);

        Mat display = new Mat(image.width(), image.cols(), image.type());
        Imgproc.threshold(result, display, 0, 255, Imgproc.THRESH_BINARY);

        displayIf(display, "binarised", 3);

        //TODO make use of Display here
        code.display();

        return code;
    }
}
