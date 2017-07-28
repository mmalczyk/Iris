package main.interfaces;

import main.encoder.ByteCode;
import main.settings.ModuleName;
import main.settings.PluginFactory;

public interface IWriter {
    ModuleName moduleName = ModuleName.Writer;
    IWriter INSTANCE =
            (IWriter) PluginFactory.getPlugin(moduleName);

    boolean write(ByteCode code);
}
