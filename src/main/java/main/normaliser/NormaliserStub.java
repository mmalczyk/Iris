package main.normaliser;

import main.display.DisplayableModule;
import main.interfaces.INormaliser;
import main.utils.ImageData;

public class NormaliserStub extends DisplayableModule implements INormaliser {
    @Override
    public ImageData normalize(ImageData imageData) {
        return imageData;
    }
}
