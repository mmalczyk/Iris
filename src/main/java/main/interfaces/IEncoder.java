package main.interfaces;

import main.encoder.ByteCode;
import main.settings.ModuleName;
import main.settings.PluginFactory;
import main.utils.ImageData;

public interface IEncoder {
    ModuleName moduleName = ModuleName.Encoder;
    IEncoder INSTANCE =
            (IEncoder) PluginFactory.getPlugin(moduleName);

    ByteCode encode(ImageData image);
}
