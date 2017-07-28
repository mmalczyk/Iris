package main.interfaces;


import main.encoder.ByteCode;
import main.settings.ModuleName;
import main.settings.PluginFactory;

public interface IComparator {

    ModuleName moduleName = ModuleName.Comparator;
    IComparator INSTANCE =
            (IComparator) PluginFactory.getPlugin(moduleName);

    double compare(ByteCode byteCodeA, ByteCode byteCodeB);
}