package main.interfaces;

import main.PluginFactory;

import java.awt.*;

public interface IEncoder {
    IEncoder INSTANCE =
            (IEncoder) PluginFactory.getPlugin(IEncoder.class);


    byte[] encode(Image image);
}
