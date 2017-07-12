package main.encoder.processor;

import org.opencv.core.Mat;

/**
 * Created by Magda on 11/07/2017.
 */
public interface IGaborFilter {
    Mat process(Mat image);

    GaborFilterType getType();
}
