package main.reader;

import main.interfaces.IReader;
import main.utils.ImageData;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by Magda on 13.02.2017.
 */

//example program args:
//"000\L\S5000L00.jpg"
public class CASIAReader implements IReader {
    private final SimpleReader reader = new SimpleReader();

    @Override
    public ImageData read(Path filePath) {
        filePath = Paths.get("./src\\main\\resources\\CASIA-Iris-Thousand\\CASIA-Iris-Thousand", filePath.toString());
        return reader.read(filePath);
    }

}
