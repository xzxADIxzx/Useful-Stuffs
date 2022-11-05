package useful;

import useful.Json.JsonMap.Consumer;

import java.lang.reflect.Array;
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

    /**
     * @return a value cast to a specific type.
     * @throws ClassCastException value cannot be cast to a specific type.
     */
    public <T> T getAs(String key) {
        return (T) values.get(key);
    }

    /**
     * @return an array cast to a specific type.
     * @throws ClassCastException DO NOT USE INT.CLASS, USE INTEGER INSTEAD!
     */
    public <T> T[] getArray(String key, Class<T> referenced) {
        return new JsonArray(get(key)).cast(referenced);
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

    /** Remove all values from json. */
    public void clear() {
        values.clear();
    }

    /** @return a string representation of this {@link Json}. */
    public String write(JsonStyle style) {
        JsonStyle.indent++;
        StringBuilder builder = style.createJson();

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

        if (object instanceof Json json) return json.write(style);
        if (object instanceof JsonArray array) return array.write(style);

        return object.toString();
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
        return object instanceof Json json ? serializator.deserializeObject(json) : object;
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
        compact(0, "{", "%s:%s,", "%s,", "%s}"), standard(0, "{ ", "%s: %s, ", "%s, ", " %s}"), beautiful(4, "{\n", "%s: %s,\n", "   %s,\n", "\n%s}");

        private static int indent;

        private final int spaces;
        private final String start, end;
        private final String keyvalue, value;

        private JsonStyle(int spaces, String start, String keyvalue, String value, String end) {
            this.spaces = spaces;
            this.start = start;
            this.keyvalue = keyvalue;
            this.value = value;
            this.end = end;
        }

        public StringBuilder createJson() {
            return new StringBuilder(start);
        }

        public StringBuilder createArray() {
            return new StringBuilder(start.replace('{', '['));
        }

        public String add(String key, String value) {
            return indent() + this.keyvalue.formatted(key, value);
        }

        public String add(String value) {
            return indent() + this.value.formatted(value);
        }

        public String toString(StringBuilder builder) {
            boolean json = builder.charAt(0) == '{';
            if (builder.length() == start.length()) return json ? "{}" : "[]";

            builder.setLength(builder.lastIndexOf(","));
            return builder.append((json ? this.end : this.end.replace('}', ']')).formatted(indent())).toString();
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

        public void clear() {
            keys.clear();
            values.clear();
        }

        public interface Consumer {
            public void get(String key, Object value);
        }
    }

    public static class JsonArray {

        private final Object array;

        public JsonArray(Object array) {
            if (!array.getClass().isArray()) throw new IllegalArgumentException("Object is not an array");
            this.array = array;
        }

        public String write(JsonStyle style) {
            int lenght = Array.getLength(array);
            StringBuilder builder = style.createArray();

            for (int i = 0; i < lenght; i++) {
                builder.append(style.add(Json.write(Array.get(array, i), style)));
            }

            return style.toString(builder);
        }

        public static Object read(String array) {
            JsonCutter cutter = JsonCutter.array(array);
            ArrayList<Object> result = new ArrayList<>();

            while (cutter.hasNext()) {
                result.add(Json.readAs(cutter.next()));
            }

            return result.toArray();
        }

        public <T> T[] cast(Class<T> referenced) {
            int lenght = Array.getLength(array);
            ArrayList<T> result = new ArrayList<>();

            for (int i = 0; i < lenght; i++) {
                result.add((T) Array.get(array, i));
            }

            return result.toArray((T[]) Array.newInstance(referenced, lenght));
        }
    }

    /** Iterates over json keys and values as a string. */
    public static class JsonCutter {

        private final String base;
        private int index;

        public JsonCutter(String base) {
            base = base.replaceAll("\n", "").trim();

            if (base.length() < 2) throw new RuntimeException("Minimum json length is 2 characters!");
            if (base.charAt(0) != '{' || base.charAt(base.length() - 1) != '}')
                throw new RuntimeException("Json must start and end with curly braces!");

            this.base = base.substring(1, base.length() - 1);
        }

        public static JsonCutter array(String base) { // hacky
            return new JsonCutter("{" + base.substring(1, base.length() - 1) + "}");
        }

        public String next() {
            int start = next(next -> next != ' ' && next != ':' && next != ',');

            switch (base.charAt(start)) {
                case '"' -> skipString();
                case '{' -> skipJson();
                case '[' -> skipArray();
                default -> skip();
            }

            return base.substring(start, index);
        }

        public int next(Function<Character, Boolean> pred) {
            while (hasNext())
                if (pred.apply(base.charAt(index++))) break;
            return index - 1;
        }

        public boolean hasNext() {
            if (base.isEmpty() || index >= base.length()) return false;

            for (int i = index; i < base.length(); i++)
                if (base.charAt(i) != ' ') return true;

            return false;
        }

        public void skipString() {
            while (hasNext()) {
                if (base.charAt(index) == '\\') {
                    index += 2; // skip \"
                    continue;
                }

                if (base.charAt(index++) == '"') break; // end of string
            }
        }

        public void skipJson() {
            int braces = 1; // number of open curly braces
            while (hasNext()) {
                switch (base.charAt(index++)) {
                    case '"' -> skipString(); // string may contains curly braces
                    case '{' -> braces++;
                    case '}' -> braces--;
                }

                if (braces == 0) break; // closing bracket
            }
        }

        public void skipArray() {
            int braces = 1; // number of open square braces
            while (hasNext()) {
                switch (base.charAt(index++)) {
                    case '"' -> skipString(); // string may contains square braces
                    case '[' -> braces++;
                    case ']' -> braces--;
                }

                if (braces == 0) break; // closing bracket
            }
        }

        public void skip() { // booleans and numbers are separated from the next by any of these characters
            next(next -> next == ' ' || next == ',' || next == '}');
            if (hasNext()) index -= 1; // very important thing
        }
    }
}
