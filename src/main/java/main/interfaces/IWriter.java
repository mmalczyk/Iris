package main.interfaces;

import main.PluginFactory;

public interface IWriter {
    IWriter INSTANCE =
            (IWriter) PluginFactory.getPlugin(IWriter.class);

    boolean write(byte[] code);
}
