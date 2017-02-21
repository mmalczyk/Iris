package main.reader;

import main.interfaces.IReader;

import java.awt.*;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by Magda on 13.02.2017.
 */

//example program args:
//"000\L\S5000L00.jpg"
public class CASIAReader implements IReader {
    private SimpleReader reader = new SimpleReader();

    @Override
    public Image read(Path filePath) {
        filePath = Paths.get("D:\\Projects\\Java\\Iris_Malczyk\\src\\main\\resources\\CASIA-Iris-Thousand\\CASIA-Iris-Thousand", filePath.toString());
        return reader.read(filePath);
    }

}
