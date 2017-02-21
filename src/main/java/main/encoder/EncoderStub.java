package main.encoder;


import main.interfaces.IEncoder;

import java.awt.*;

public class EncoderStub implements IEncoder {
    @Override
    public byte[] encode(Image image) {
        return new byte[0];
    }
}
