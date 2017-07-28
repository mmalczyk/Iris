package main.settings;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AbstractSettings {

    static final Properties properties = new Properties();

    static {
        try {
            InputStream stream = System.class.getResourceAsStream("/plugins.properties");
            properties.load(stream);
            stream.close();
        } catch (IOException ex) {
            throw new UnsupportedOperationException(ex.getMessage());
        }
    }
}
