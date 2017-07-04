package main;

import java.util.Properties;

/**
 * Created by Magda on 07.02.2017.
 * Based on the Plugin pattern by Martin Fowler
 */
public class PluginFactory {
    private static final Properties properties = new Properties();

    static {
        try {
            properties.load(System.class.getResourceAsStream("/plugins.properties"));
        } catch (Exception ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static Object getPlugin(Class interFace) {
        String implName = properties.getProperty(interFace.getName());
        if (implName == null) {
            throw new RuntimeException("object not specified for " +
                    interFace.getName() + " in PluginFactory properties.");
        }
        try {
            return Class.forName(implName).newInstance();
        } catch (Exception ex) {
            throw new RuntimeException("factory unable to construct instance of " +
                    interFace.getName());
        }
    }
}