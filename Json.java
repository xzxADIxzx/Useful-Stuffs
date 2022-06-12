import java.util.ArrayList;

/**
 * Just a small and simple json parser.
 * 
 * @author xzxADIxzx
 */
public class Json {

    /** All values that this {@link Json} object contains. */
    public JsonMap values;

    public static void main(String[] args) {
        Json json = new Json();
        json.add("name", "Karlson");
        json.add("age", 19);
        json.add("abilities", new Json().add("healthy", true).add("fast", false));
        json.add("config", 0.1f);
        System.out.print(json.write(JsonStyle.beautiful));
    }

    public Json() {
        this.values = new JsonMap();
    }

    public Json add(String name, Object object) {
        values.put(name, object);
        return this;
    }

    public String write(JsonStyle style) {
        StringBuilder builder = style.create();

        values.each((key, value) -> {
            if (value instanceof String string) value = "\"" + string + "\"";
            if (value instanceof JsonSerializable json) value = json.write();
            builder.append(style.add(key, value instanceof Json json ? json.write(style) : value.toString()));
        });

        return style.toString(builder);
    }

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
        standard("{", "\"%s\":%s,", "\"}"), beautiful("{\n", "    \"%s\": %s,\n", "\n}");

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
            int index = builder.length();
            return builder.replace(index - 2, index, end).toString();
        }
    }

    public class JsonMap {

        private ArrayList<String> keys = new ArrayList<>();
        private ArrayList<Object> values = new ArrayList<>();

        public void each(Consumer cons) {
            for (int i = 0; i < keys.size(); i++)
                cons.get(keys.get(i), values.get(i));
        }

        public Object get(Object key) {
            int index = keys.indexOf(key);
            return index == -1 ? null : values.get(index);
        }

        public void put(String key, Object value) {
            if (keys.contains(key)) {
                int index = keys.indexOf(key);
                values.remove(index);
                values.add(index, value);
            } else {
                keys.add(key);
                values.add(value);
            }
        }

        public interface Consumer {

            public abstract void get(String key, Object value);
        }
    }
}
