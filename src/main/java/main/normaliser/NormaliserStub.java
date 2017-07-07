package main.normaliser;

import main.Utils.ImageData;
import main.interfaces.INormaliser;
import main.writer.Display;

public class NormaliserStub extends Display implements INormaliser {
    @Override
    public ImageData normalize(ImageData imageData) {
        return imageData;
    }
}
