package main.interfaces;

import main.PluginFactory;

import java.awt.*;

public interface ILocaliser {
    ILocaliser INSTANCE =
            (ILocaliser) PluginFactory.getPlugin(ILocaliser.class);

    Image localise(Image image);
}
