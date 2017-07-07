package main.encoder;


import main.Utils.ImageData;
import main.interfaces.IEncoder;
import main.writer.Display;

public class EncoderStub extends Display implements IEncoder {
    @Override
    public byte[] encode(ImageData image) {
        return new byte[0];
    }
}
