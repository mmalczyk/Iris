package main.localiser;

import main.Utils.ImageData;
import main.Utils.ImageUtils;
import main.interfaces.ILocaliser;

import java.awt.image.BufferedImage;

import main.localiser.misztal.OtsuBinarization;
import pl.edu.uj.JImageStream.collectors.BufferedImageCollector;
import pl.edu.uj.JImageStream.model.StreamableImage;

/**
 * Created by Magda on 27/05/2017.
 */
public class BasicLocaliser implements ILocaliser {

    private BufferedImage binarise(BufferedImage image){
        StreamableImage streamableImage = new StreamableImage(image);
        BufferedImage bufferedImageResult = streamableImage.stream()
                .apply(new OtsuBinarization(true))
                .collect(new BufferedImageCollector());

        ImageUtils.showBufferedImage(bufferedImageResult, "binarised iris");


        return bufferedImageResult;

    }

    @Override
    public ImageData localise(ImageData imageData) {
        BufferedImage image = imageData.getBuffImage();
        BufferedImage binarisedImage = binarise(image);
        imageData.setBuffImage(binarisedImage);
        return imageData;
    }

    public boolean getShowResults() {
        return showResults;
    }

    public void setShowResults(boolean showResults) {
        this.showResults = showResults;
    }

    private boolean showResults;
}

