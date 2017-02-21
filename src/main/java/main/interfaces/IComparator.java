package main.interfaces;


import main.PluginFactory;

public interface IComparator {
    IComparator INSTANCE =
            (IComparator) PluginFactory.getPlugin(IComparator.class);

    double compare(byte[] codeA, byte[] codeB, byte[] maskA, byte[] maskB);
}
