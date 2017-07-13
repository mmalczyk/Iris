package main.interfaces;


import main.PluginFactory;
import main.encoder.ByteCode;

public interface IComparator {
    IComparator INSTANCE =
            (IComparator) PluginFactory.getPlugin(IComparator.class);

    double compare(ByteCode byteCodeA, ByteCode byteCodeB);
}