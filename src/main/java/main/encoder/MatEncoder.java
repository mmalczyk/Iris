package main.encoder;

import main.display.DisplayableModule;
import main.encoder.processor.GaborFilterFactory;
import main.encoder.processor.IGaborFilter;
import main.interfaces.IEncoder;
import main.utils.ImageData;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.util.List;

public class MatEncoder extends DisplayableModule implements IEncoder {
    private List<Mat> results;

    public MatEncoder() {
        super(moduleName);
    }

    public List<Mat> getResults() {
        return results;
    }

    @Override
    public ImageData encode(ImageData imageData) {
        assert imageData.getNormMat() != null;
        assert imageData.getFilterConstants() != null;
        assert imageData.getGaborFilterType() != null;

        Mat image = imageData.getNormMat();

        IGaborFilter gaborFilter = GaborFilterFactory.getFilter(imageData);
        results = gaborFilter.process(image);

        display.displayIf(image, displayTitle("original image"), 2);
        Mat lastResult = results.get(results.size() - 1);

        display.displayIf(lastResult, displayTitle("gabor filter"), 2);

        Mat displayMat = new Mat(image.width(), image.cols(), image.type());
        Imgproc.threshold(lastResult, displayMat, 255 / 2, 255, Imgproc.THRESH_BINARY);
        display.displayIf(displayMat, displayTitle("binarised image"), 2);
        imageData.setSimplifiedEncoding(displayMat);

        return imageData;
    }
}
