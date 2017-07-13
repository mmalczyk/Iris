package main.localiser;

import main.display.DisplayableModule;
import main.interfaces.ILocaliser;
import main.utils.ImageData;

public class LocaliserStub extends DisplayableModule implements ILocaliser {
    @Override
    public ImageData localise(ImageData imageData) {
        return imageData;
    }
}
