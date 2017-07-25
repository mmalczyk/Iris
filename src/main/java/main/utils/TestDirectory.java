package main.utils;

import java.nio.file.FileSystems;
import java.nio.file.Path;

/**
 * Created by Magda on 04/07/2017.
 */
public class TestDirectory {
    public static final Path results = FileSystems.getDefault().getPath("./src\\test\\results");

    public static final Path images = FileSystems.getDefault().getPath("./src\\test\\testImages");

    public static final Path CASIA = FileSystems.getDefault().getPath(images.toString(), "CASIA-Iris-Thousand");

    public static Path CASIA_Image(int person, Eye side, int photo) {
        assert photo >= 0 && photo < 10 && person >= 0;
        String formattedPerson = String.format("%03d", person);
        return FileSystems.getDefault().getPath(
                CASIA.toString(),
                formattedPerson,
                side.toString(),
                "S5" + formattedPerson + side.toString() + String.format("%02d", photo) + ".jpg");
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
}
