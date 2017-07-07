package main.localiser;

import main.Utils.ImageData;
import main.interfaces.ILocaliser;
import main.writer.Display;

public class LocaliserStub extends Display implements ILocaliser {
    @Override
    public ImageData localise(ImageData imageData) {
        return imageData;
    }
}
