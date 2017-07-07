package main.interfaces;

import main.PluginFactory;
import main.Utils.ImageData;

public interface IEncoder extends IDisplay{
    IEncoder INSTANCE =
            (IEncoder) PluginFactory.getPlugin(IEncoder.class);


    byte[] encode(ImageData image);
}
