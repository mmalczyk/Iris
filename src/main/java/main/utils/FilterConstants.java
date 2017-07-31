package main.utils;

/**
 * Created by Magda on 11/07/2017.
 */
public class FilterConstants {

    public final int FILTER_WIDTH = 3;
    public final int FILTER_HEIGHT = 9;
    public final int CODE_HEIGHT = 8; //code height
    public final int CODE_WIDTH = 128; //code width
    public final int WAVELET_COUNT = 16;

    public FilterConstants() {
        //noinspection ConstantConditions
        assert FILTER_WIDTH % 2 == 1;
        //noinspection ConstantConditions
        assert FILTER_HEIGHT % 2 == 1;
        assert getTotalHeight() % CODE_HEIGHT == 0;
        assert getTotalWidth() % CODE_WIDTH == 0;
    }

    public double getTotalHeight() {
        return CODE_HEIGHT * FILTER_HEIGHT;
    }

    public double getTotalWidth() {
        return CODE_WIDTH * FILTER_WIDTH;
    }

}
