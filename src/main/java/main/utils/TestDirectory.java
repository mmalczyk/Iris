package main.utils;

import java.nio.file.FileSystems;
import java.nio.file.Path;

/**
 * Created by Magda on 04/07/2017.
 */
public class TestDirectory {
    public static final Path results = FileSystems.getDefault().getPath("./src\\test\\resultImages");

    public static final Path images = FileSystems.getDefault().getPath("./src\\test\\testImages");
}
