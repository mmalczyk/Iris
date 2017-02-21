package main.localiser.misztal;

import pl.edu.uj.JImageStream.api.ImageStream;

import java.awt.image.BufferedImage;

/**
 * Created by Krzysztof on 14.02.2017.
 */
public class ImageStreamEx extends ImageStream {
    public ImageStreamEx(BufferedImage bufferedImage, boolean isParallel) {
        super(bufferedImage, isParallel);
    }
}
