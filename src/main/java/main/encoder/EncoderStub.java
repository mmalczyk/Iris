package main.encoder;


import main.display.DisplayableModule;
import main.interfaces.IEncoder;
import main.utils.ImageData;

public class EncoderStub extends DisplayableModule implements IEncoder {
    @Override
    public ByteCode encode(ImageData image) {
        return null;
    }
}
