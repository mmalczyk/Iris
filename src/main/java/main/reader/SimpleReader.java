package main.reader;


import main.Utils.ImageData;
import main.interfaces.IReader;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

/**
 * Created by Magda on 13.02.2017.
 */

//example program args:
//"D:\Projects\Java\Iris\CASIA-Iris-Thousand\CASIA-Iris-Thousand\000\L\S5000L00.jpg"
public class SimpleReader implements IReader {
    @Override
    public ImageData read(Path filePath) {
        //TODO this won't work, period
        BufferedImage img = null;

        try {
            File file = filePath.toFile();
            img = ImageIO.read(file);

        } catch (IOException e) {
            System.out.println("Error while reading file: " + e.getMessage());
        }

        ImageData imageData = new ImageData();
        imageData.setPath(filePath);
        //TODO update this reader
        //imageData.setBuffImage(img);
        return imageData;
    }
}
