package main.interfaces;

import main.PluginFactory;
import main.encoder.ByteCode;

public interface IWriter {
    IWriter INSTANCE =
            (IWriter) PluginFactory.getPlugin(IWriter.class);

    boolean write(ByteCode code);
}
