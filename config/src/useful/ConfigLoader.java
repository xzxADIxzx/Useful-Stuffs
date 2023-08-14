package useful;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.nio.file.Files;
import java.nio.file.Paths;

public class ConfigLoader {

    public static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .serializeNulls()
            .disableHtmlEscaping()
            .create();

    public static <T> T load(Class<T> type, String file) {
        try {
            var path = Paths.get(file);
            if (Files.exists(path))
                return gson.fromJson(Files.readString(path), type);

            var config = type.getDeclaredConstructor().newInstance();
            Files.writeString(path, gson.toJson(config));

            return config;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}