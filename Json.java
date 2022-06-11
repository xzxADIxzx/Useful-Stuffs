import java.util.ArrayList;

/**
 * Just a small and simple json parser.
 * 
 * @author xzxADIxzx
 */
public class Json {

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
            if (value instanceof JsonSerializable json)
                value = json.write();
            builder.append(style.add(key, value instanceof Json json ? json.write(style) : value.toString()));
        });

        return style.toString(builder);
    }

    public static Json read(String json) {
        return new Json();
    }

    public interface JsonSerializable {

        public Json write();

        public void read(Json json);
    }

    public enum JsonStyle {
        standard("{", "\"%s\":\"%s\",", "\"}"),
        beautiful("{\n", "    \"%s\": \"%s\",\n", "\n}");

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
