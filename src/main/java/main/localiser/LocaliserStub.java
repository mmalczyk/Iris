package main.localiser;

import main.interfaces.ILocaliser;
import main.utils.ImageData;
import main.writer.Display;

public class LocaliserStub extends Display implements ILocaliser {
    @Override
    public ImageData localise(ImageData imageData) {
        return imageData;
    }
}
