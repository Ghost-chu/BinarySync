package studio.potatocraft.binarysync.util;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.io.IOException;

public class BinaryTypeAdapter extends TypeAdapter<ConfigurationSerializable> {
    /**
     * Writes one JSON value (an array, object, string, number, boolean or null)
     * for {@code value}.
     *
     * @param out
     * @param value the Java object to write. May be null.
     */
    @Override
    public void write(JsonWriter out, ConfigurationSerializable value) throws IOException {
        YamlConfiguration yamlConfiguration = new YamlConfiguration();
        yamlConfiguration.set(value.getClass().getName(), value);
        out.name(value.getClass().getName()).value(yamlConfiguration.saveToString());
        out.endObject();
    }

    /**
     * Reads one JSON value (an array, object, string, number, boolean or null)
     * and converts it to a Java object. Returns the converted object.
     *
     * @param in
     * @return the converted Java object. May be null.
     */
    @Override
    public ConfigurationSerializable read(JsonReader in) throws IOException {
        if(in.hasNext()){
           String className = in.nextName();
           String yaml = in.nextString();
           in.endObject();
           YamlConfiguration yamlConfiguration = new YamlConfiguration();
            try {
                yamlConfiguration.loadFromString(yaml);
                return (ConfigurationSerializable) yamlConfiguration.get(className);
            } catch (InvalidConfigurationException e) {
                throw new IOException(e.getMessage());
            }
        }

        return null;
    }
}
