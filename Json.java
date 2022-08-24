import java.util.ArrayList;

/**
 * Just a small and simple json parser.
 * 
 * @author xzxADIxzx
 */
public class Json {

    /** All {@link JsonSerializer}s used to cast objects to json and back. */
    public static final ArrayList<JsonSerializer<?>> serializers = new ArrayList<>();

    /** All values that this {@link Json} object contains. */
    public JsonMap values;

    public static void main(String[] args) {
        Json json = new Json();
        json.put("name", "Karlson");
        json.put("age", 19);
        json.put("abilities", new Json().put("healthy", true).put("fast", false));
        json.put("config", 0.1f);
        System.out.print(json.write(JsonStyle.beautiful));
    }

    public Json() {
        this.values = new JsonMap();
    }

    public Object get(String key) {
        return values.get(key);
    }

    public Json put(String key, Object value) {
        values.put(key, value);
        return this;
    }

    public boolean remove(String key) {
        if (values.contains(key)) {
            values.remove(key);
            return true;
        } else return false;
    }

    public boolean contains(String key) {
        return values.contains(key);
    }

    /** @return a string representation of this {@link Json}. */
    public String write(JsonStyle style) {
        StringBuilder builder = style.create();

        values.each((key, value) -> {
            if (value instanceof String string) value = "\"" + string + "\"";
            if (value instanceof JsonSerializable json) value = json.write();
            // TODO JsonSerializer<T> auto(T value)
            builder.append(style.add(key, value instanceof Json json ? json.write(style) : value.toString()));
        });

        return style.toString(builder);
    }

    /** @return {@link Json} parsed from the given string. */
    public static Json read(String json) {
        return new Json();
    }

    /** Makes the object serializable for the json parser. */
    public interface JsonSerializable {

        /** @return a {@link Json} representation of this object. */
        public Json write();

        /** Recreates an object from its {@link Json} representation. */
        public void read(Json json);
    }

    /** Alternative for {@link JsonSerializable} if you need to parse already existing class into json. */
    public interface JsonSerializer<T> {

        /** @return a {@link Json} representation of the given object. */
        public Json write(T object);

        /** Recreates the given object from its {@link Json} representation. */
        public void read(T object, Json json);
    }

    public enum JsonStyle {
        standard("{", "\"%s\":%s,", "}"), beautiful("{\n", "    \"%s\": %s,\n", "\n}");

        private final String start;
        private final String template;
        private final String end;

        private JsonStyle(String start, String template, String end) {
            this.start = start;
            this.template = template;
            this.end = end;
        }

        public StringBuilder create() {
            return new StringBuilder(start);
        }

        public String add(String name, String value) {
            return template.formatted(name, value);
        }

        public String toString(StringBuilder builder) {
            return builder.replace(builder.lastIndexOf(","), builder.length(), end).toString();
        }
    }

    public class JsonMap {

        private final ArrayList<String> keys = new ArrayList<>();
        private final ArrayList<Object> values = new ArrayList<>();

        public void each(Consumer cons) {
            for (int i = 0; i < keys.size(); i++)
                cons.get(keys.get(i), values.get(i));
        }

        public Object get(String key) {
            int index = keys.indexOf(key);
            return index == -1 ? null : values.get(index);
        }

        public void put(String key, Object value) {
            if (keys.contains(key)) {
                int index = keys.indexOf(key);
                values.set(index, value);
            } else {
                keys.add(key);
                values.add(value);
            }
        }

        public void remove(String key) {
            int index = keys.indexOf(key);
            if (index == -1) return;
            keys.remove(index);
            values.remove(index);
        }

        public boolean contains(String key) {
            return keys.contains(key);
        }

        public interface Consumer {
            public void get(String key, Object value);
        }
    }
}
