package main.encoder;


import main.display.DisplayableModule;
import main.interfaces.IEncoder;
import main.utils.ImageData;

public class EncoderStub extends DisplayableModule implements IEncoder {

    public EncoderStub() {
        super(moduleName);
    }

    @Override
    public ImageData encode(ImageData image) {
        return null;
    }
}
