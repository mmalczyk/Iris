package main.interfaces;

import main.PluginFactory;
import main.encoder.ByteCode;
import main.utils.ImageData;

public interface IEncoder {
    IEncoder INSTANCE =
            (IEncoder) PluginFactory.getPlugin(IEncoder.class);

    ByteCode encode(ImageData image);
}
