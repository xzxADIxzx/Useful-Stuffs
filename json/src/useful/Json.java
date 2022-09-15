package useful;

import useful.Json.JsonMap.Consumer;

import java.util.ArrayList;
import java.util.function.Function;

/**
 * Just a small and simple json parser.
 * 
 * @author xzxADIxzx
 */
@SuppressWarnings("unchecked")
public class Json {

    /** All {@link JsonSerializer}s used to cast objects to json and back. */
    public static final JsonSerializator serializator = new JsonSerializator();

    /** All values that this {@link Json} object contains. */
    public JsonMap values;

    public Json() {
        this.values = new JsonMap();
    }

    public Json(Class<?> key) {
        this(); // used to parse objects
        this.put("class", key.getName());
    }

    /** Iterate over all values in json. */
    public void each(Consumer cons) {
        values.each(cons);
    }

    /** @return a value by a key in json. */
    public Object get(String key) {
        return values.get(key);
    }

    public <T> T getAs(String key) {
        return (T) values.get(key);
    }

    /** Puts a value to json by a key. */
    public Json put(String key, Object value) {
        values.put(key, value);
        return this;
    }

    /** Remove a value from json by a key. */
    public boolean remove(String key) {
        if (values.contains(key)) {
            values.remove(key);
            return true;
        } else return false;
    }

    /** @return whether the json contains a key. */
    public boolean contains(String key) {
        return values.contains(key);
    }

    /** @return a string representation of this {@link Json}. */
    public String write(JsonStyle style) {
        JsonStyle.indent++;
        StringBuilder builder = style.create();

        values.each((key, value) -> {
            builder.append(style.add(serializator.serializeString(key), write(value, style)));
        });

        JsonStyle.indent--;
        return style.toString(builder);
    }

    public static String write(Object object, JsonStyle style) {
        // object can initially be json
        if (object instanceof Json json) return json.write(style);

        object = serializator.serializeField(object);
        return object instanceof Json json ? json.write(style) : object.toString();
    }

    /** @return {@link Json} parsed from the given string. */
    public static Json read(String json) {
        JsonCutter cutter = new JsonCutter(json);
        Json result = new Json();

        while (cutter.hasNext()) {
            result.put(serializator.deserializeString(cutter.next()), readAs(cutter.next()));
        }

        return result;
    }

    public static Object readAs(String field) {
        Object object = serializator.deserializeField(field);
        return object instanceof Json json ? serializator.deserialize(json) : object;
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

    /** All json styles that are used during writing. */
    public enum JsonStyle {
        compact(0, "{", "%s:%s,", "%s}"), standard(0, "{ ", "%s: %s, ", " %s}"), beautiful(4, "{\n", "%s: %s,\n", "\n%s}");
        
        private static int indent;

        private final int spaces;
        private final String start;
        private final String template;
        private final String end;

        private JsonStyle(int spaces, String start, String template, String end) {
            this.spaces = spaces;
            this.start = start;
            this.template = template;
            this.end = end;
        }

        public StringBuilder create() {
            return new StringBuilder(start);
        }

        public String add(String name, String value) {
            return indent() + template.formatted(name, value);
        }

        public String toString(StringBuilder builder) {
            if (builder.length() == start.length()) return "{}";
            return builder.replace(builder.lastIndexOf(","), builder.length(), end.formatted(indent())).toString();
        }

        private String indent() {
            return " ".repeat(spaces).repeat(indent);
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

    /** Iterates over json keys and values as a string. */
    public static class JsonCutter {

        private final String base;
        private final int lastIndex;

        private int index;

        public JsonCutter(String base) {
            this.base = base.trim().replaceAll("\n", "");

            index = this.base.length() - 1;
            while (index != 0) {
                char next = this.base.charAt(--index);
                if (next != ' ' && next != ':' && next != ',') break;
            }

            this.lastIndex = index;
            index = 0;
        }

        public String next() {
            int start = next(next -> next != ' ' && next != ':' && next != ',');
            char bracket = base.charAt(start);
            int end = next(bracket == '"' ? next -> next == '"' : bracket == '{' ? next -> next == '}' : next -> next == ' ' || next == ',' || next == '}');

            return base.substring(start, bracket == '"' || bracket == '{' ? ++index : end); // index because brackets
        }

        public int next(Function<Character, Boolean> pred) {
            while (hasNext())
                if (pred.apply(base.charAt(++index))) break;
            return index;
        }

        public boolean hasNext() {
            return index <= lastIndex;
        }
    }
}
