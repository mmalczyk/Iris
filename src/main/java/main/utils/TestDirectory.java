package main.utils;

import java.nio.file.FileSystems;
import java.nio.file.Path;

/**
 * Created by Magda on 04/07/2017.
 */
public class TestDirectory {
    public static final Path results = FileSystems.getDefault().getPath("./src\\test\\results");

    public static final Path images = FileSystems.getDefault().getPath("./src\\test\\testImages");

    public static final Path CASIA = FileSystems.getDefault().getPath(images.toString(), "CASIA_0-9");

    public static Path CASIA_Image(int person, Eye side, int photo) {
        assert person >= 0 && person <= 9;
        assert photo >= 0 && photo <= 9;
        return FileSystems.getDefault().getPath(
                CASIA.toString(),
                "00" + person,
                side.toString(),
                "S500" + person + side.toString() + "0" + photo + ".jpg");
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
