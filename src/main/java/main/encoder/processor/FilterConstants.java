package main.encoder.processor;

import org.opencv.core.Size;

public class FilterConstants {
    public static int WAVELET_COUNT = 8;
    public static int FILTER_WIDTH = 10;
    public static int FILTER_HEIGHT = 10;
    public static int CODE_WIDTH = 128;
    public static int CODE_HEIGHT = 8;
    public static Size CODE_SIZE = new Size(CODE_WIDTH, CODE_HEIGHT);
    public static int NORMALISED_WIDTH = CODE_WIDTH * FILTER_WIDTH;
    public static int NORMALISED_HEIGHT = CODE_HEIGHT * FILTER_HEIGHT;
}
