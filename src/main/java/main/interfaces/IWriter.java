package main.interfaces;

import main.PluginFactory;

public interface IWriter {
    IWriter INSTANCE =
            (IWriter) PluginFactory.getPlugin(IWriter.class);

    @SuppressWarnings("UnusedReturnValue")
    boolean write(byte[] code);
}
