package main.encoder;

import main.display.DisplayableModule;
import main.encoder.processor.GaborFilterFactory;
import main.encoder.processor.GaborFilterType;
import main.encoder.processor.IGaborFilter;
import main.interfaces.IEncoder;
import main.utils.ImageData;
import org.opencv.core.Mat;

import java.util.List;

/**
 * Created by Magda on 04/07/2017.
 */
public class OpenCVEncoder extends DisplayableModule implements IEncoder {

//    https://cvtuts.wordpress.com/2014/04/27/gabor-filters-a-practical-overview/ -> gabor filter parameters info
//    http://docs.opencv.org/3.0-beta/modules/imgproc/doc/filtering.html

    //TODO learn about phase information
    //    https://cvtuts.wordpress.com/2014/04/27/gabor-filters-a-practical-overview/ -> gabor filter parameters info

    public OpenCVEncoder() {
        super(moduleName);
    }

    private List<Mat> results;

    public List<Mat> getResults() {
        return results;
    }

    @Override
    public ImageData encode(ImageData imageData) {
        assert imageData.getNormMat() != null;
        assert imageData.getGaborFilterType() != null;

        Mat image = imageData.getNormMat();
        display.displayIf(image, displayTitle("original image"), 2);

        GaborFilterType gaborFilterType = imageData.getGaborFilterType();
        IGaborFilter gaborFilter = GaborFilterFactory.getFilter(imageData);
        results = gaborFilter.process(imageData);
/*
        // display intermediate mats
        int rsize = results.size();
        for (int i=0; i<=rsize; ++i) {
            display.displayIf(results.get(i), displayTitle("gabor filter"+('a'+i)), 2);
        }
*/
        display.displayIf(imageData.getCodeMatForm(), displayTitle("gabor filter"), 2);
        return imageData;
    }
}
