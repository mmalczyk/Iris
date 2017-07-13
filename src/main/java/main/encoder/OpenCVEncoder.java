package main.encoder;

import main.display.DisplayableModule;
import main.encoder.processor.GaborFilterFactory;
import main.encoder.processor.IGaborFilter;
import main.interfaces.IEncoder;
import main.utils.ImageData;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

/**
 * Created by Magda on 04/07/2017.
 */
public class OpenCVEncoder extends DisplayableModule implements IEncoder {

//    https://cvtuts.wordpress.com/2014/04/27/gabor-filters-a-practical-overview/ -> gabor filter parameters info
//    http://docs.opencv.org/3.0-beta/modules/imgproc/doc/filtering.html

    //TODO learn about phase information
    //    https://cvtuts.wordpress.com/2014/04/27/gabor-filters-a-practical-overview/ -> gabor filter parameters info

    @Override
    public ByteCode encode(ImageData imageData) {
        Mat image = imageData.getNormMat();

        IGaborFilter gaborFilter = GaborFilterFactory.getFilter(imageData);
        Mat result = gaborFilter.process(image);

        display.displayIf(image, displayTitle("original image"), 2);
        display.displayIf(result, displayTitle("gabor filter"), 2);

        ByteCode code = new ByteCode(result);

        Mat displayMat = new Mat(image.width(), image.cols(), image.type());
        Imgproc.threshold(result, displayMat, 0, 255, Imgproc.THRESH_BINARY);

        display.displayIf(displayMat, displayTitle("binarised image"), 2);
        display.displayIf(code.toDisplayableMat(), displayTitle("code"), 2);

        return code;
    }
}
