package useful;

import arc.files.Fi;
import arc.util.serialization.Json;
import arc.util.serialization.JsonWriter.OutputType;
import arc.util.serialization.Jval;
import arc.util.serialization.Jval.Jformat;

import static mindustry.Vars.dataDirectory;

public class ConfigLoader {

    public static final Json json = new Json(OutputType.json) {{
        setUsePrototypes(false);
    }};

    public static <T> T load(Class<T> type, String name) {
        return load(type, dataDirectory.child(name));
    }

    public static <T> T load(Class<T> type, Fi file) {
        try {
            if (file.exists())
                return json.fromJson(type, file);

            var config = type.getDeclaredConstructor().newInstance();
            file.writeString(Jval.read(json.toJson(config)).toString(Jformat.formatted));

            return config;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}