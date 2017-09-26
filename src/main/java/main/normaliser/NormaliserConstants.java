package main.normaliser;

public class NormaliserConstants {

    public static int NORMALISED_IRIS_WIDTH = 512;
    public static int NORMALISED_IRIS_HEIGHT = 64;

/*
    public static int NORMALISED_IRIS_WIDTH = 256;
    public static int NORMALISED_IRIS_HEIGHT = 32;
*/

    public static int PUPIL_RADIUS = 40;
    public static int IRIS_RADIUS = 90;

    // rectangles for filtering eyelids
    public static int EXCLUDED_WIDTH_1 = 7 * NORMALISED_IRIS_WIDTH / 16;              //224
    public static int EXCLUDED_COL_1 = (NORMALISED_IRIS_WIDTH - EXCLUDED_WIDTH_1) / 2;
    public static int EXCLUDED_HEIGHT_1 = 3 * NORMALISED_IRIS_HEIGHT / 8;             //24
    public static int EXCLUDED_ROW_1 = NORMALISED_IRIS_HEIGHT - EXCLUDED_HEIGHT_1;

    public static int EXCLUDED_WIDTH_2 = NORMALISED_IRIS_WIDTH / 8;                 //64
    public static int EXCLUDED_COL_2 = 0;
    public static int EXCLUDED_HEIGHT_2 = 3 * NORMALISED_IRIS_HEIGHT / 8;             //24
    public static int EXCLUDED_ROW_2 = NORMALISED_IRIS_HEIGHT - EXCLUDED_HEIGHT_2;

    public static int EXCLUDED_WIDTH_3 = NORMALISED_IRIS_WIDTH / 8;                 //64
    public static int EXCLUDED_COL_3 = NORMALISED_IRIS_WIDTH - EXCLUDED_WIDTH_3;
    public static int EXCLUDED_HEIGHT_3 = 3 * NORMALISED_IRIS_HEIGHT / 8;             //24
    public static int EXCLUDED_ROW_3 = NORMALISED_IRIS_HEIGHT - EXCLUDED_HEIGHT_3;

    public static int EXCLUDED_WIDTH_4 = 5 * NORMALISED_IRIS_WIDTH / 8;               //320
    public static int EXCLUDED_COL_4 = (NORMALISED_IRIS_WIDTH - EXCLUDED_WIDTH_4) / 2;
    public static int EXCLUDED_HEIGHT_4 = NORMALISED_IRIS_HEIGHT / 4;               //16
    public static int EXCLUDED_ROW_4 = 0;

}

