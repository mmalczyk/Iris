package main.encoder.processor;

import main.utils.ImageData;
import org.opencv.core.Mat;

import java.util.List;

/**
 * Created by Magda on 11/07/2017.
 */
public interface IGaborFilter {
    List<Mat> process(ImageData imageData);

    GaborFilterType getType();
}
