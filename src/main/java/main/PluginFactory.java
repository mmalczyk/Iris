package main;

import java.util.Properties;

/**
 * Created by Magda on 07.02.2017.
 * Based on the Plugin pattern by Martin Fowler
 */
public class PluginFactory {
    private static final Properties props = new Properties();

    static {
        try {
            props.load(System.class.getResourceAsStream("/plugins.properties"));
        } catch (Exception ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static Object getPlugin(Class iface) {
        String implName = props.getProperty(iface.getName());
        if (implName == null) {
            throw new RuntimeException("object not specified for " +
                    iface.getName() + " in PluginFactory propeties.");
        }
        try {
            return Class.forName(implName).newInstance();
        } catch (Exception ex) {
            throw new RuntimeException("factory unable to construct instance of " +
                    iface.getName());
        }
    }
}