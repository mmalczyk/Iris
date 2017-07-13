package main.encoder;


import main.interfaces.IEncoder;
import main.utils.ImageData;
import main.writer.Display;

public class EncoderStub extends Display implements IEncoder {
    @Override
    public ByteCode encode(ImageData image) {
        return null;
    }
}
