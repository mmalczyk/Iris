package main.normaliser;

import main.interfaces.INormaliser;
import main.utils.ImageData;
import main.writer.Display;

public class NormaliserStub extends Display implements INormaliser {
    @Override
    public ImageData normalize(ImageData imageData) {
        return imageData;
    }
}
