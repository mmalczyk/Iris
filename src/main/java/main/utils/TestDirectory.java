package main.utils;

import java.nio.file.FileSystems;
import java.nio.file.Path;

/**
 * Created by Magda on 04/07/2017.
 */
public class TestDirectory {
    public static final Path results = FileSystems.getDefault().getPath("./src\\test\\results");

    public static final Path images = FileSystems.getDefault().getPath("./src\\test\\testImages");

    public static final Path CASIA_IRIS_THOUSAND = FileSystems.getDefault().getPath(images.toString(), "CASIA-Iris-Thousand");
    public static final Path CASIA_IRIS_INTERVAL = FileSystems.getDefault().getPath(images.toString(), "CASIA-Iris-Interval");


    public static Path CASIA_Image(int person, Eye side, int photo, Path path) {
        assert photo >= 0 && photo < 10 && person >= 0;
        String formattedPerson = String.format("%03d", person);
        return FileSystems.getDefault().getPath(
                CASIA_IRIS_THOUSAND.toString(),
                formattedPerson,
                side.toString(),
                "S5" + formattedPerson + side.toString() + String.format("%02d", photo) + ".jpg");
    }

    public static Path CASIA_IRIS_THOUSAND_Image(int person, Eye side, int photo) {
        return CASIA_Image(person, side, photo, CASIA_IRIS_THOUSAND);
    }

    public static Path CASIA_IRIS_INTERVAL_Image(int person, Eye side, int photo) {
        return CASIA_Image(person, side, photo, CASIA_IRIS_INTERVAL);
    }

    public enum Eye {
        Left, Right;

        public String toString() {
            if (this == Left)
                return "L";
            if (this == Right)
                return "R";
            else throw new UnsupportedOperationException("No string available for this enum");
        }
    }

    public static String resultFile(String file) {
        return results.toString() + "\\" + file;
    }
}
