package main.reader;


import main.interfaces.IReader;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;

/**
 * Created by Magda on 13.02.2017.
 */

//example program args:
//"D:\Projects\Java\Iris\CASIA-Iris-Thousand\CASIA-Iris-Thousand\000\L\S5000L00.jpg"
public class SimpleReader implements IReader {
    @Override
    public Image read(Path filePath) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(filePath.toFile());
        } catch (IOException e) {
            System.out.println("Error while reading file: "+e.getMessage());
        }
        return img;
    }
}
