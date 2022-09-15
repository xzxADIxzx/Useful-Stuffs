package useful;

import useful.Json.JsonSerializable;
import useful.Json.JsonSerializer;

import java.util.ArrayList;

/** Converts objects to {@link Json} using registered {@link JsonSerializer}s or reflection. */
@SuppressWarnings("unchecked")
public class JsonSerializator {

    private static ArrayList<Class<?>> unserializable = new ArrayList<>() {{ // ._.
        add(Object.class); add(Byte.class); add(Short.class); add(Integer.class); add(Long.class);
        add(Character.class); add(Boolean.class); add(Float.class); add(Double.class);
    }};

    private ArrayList<ClassSerializerPair<?>> pairs = new ArrayList<>();

    /**
     * Searches for a {@link JsonSerializer} in {@link JsonSerializator#pairs} and serializes through it if finds it, otherwise through reflection.
     * Only supports objects! If you need to serialize int or float call {@link #serializeField(Object)}.
     */
    public Json serialize(Object object) {
        ClassSerializerPair<Object> pair = getSerializer(object);

        if (pair != null) return pair.serializer.write(object);
        else return new Json(object.getClass()); // TODO serialize through public fields and etc.
    }

    /** @return a {@link Json} or string that represents the field and can be parsed back using {@link #deserializeField(String)}. */
    public Object serializeField(Object field) {
        if (field == null) return "null"; // it is impossible to get string from zero

        if (field instanceof String string) return serializeString(string);
        if (field instanceof JsonSerializable json) return json.write();

        if (serializable(field)) return serialize(field);
        else return field.toString();
    }

    /** Replaces " with \" and adds " on the sides. */
    public String serializeString(String field) {
        return "\"" + field.replaceAll("\"", "\\\\\"") + "\"";
    }

    /** Searches for a {@link JsonSerializer} in {@link JsonSerializator#pairs} and deserializes through it if finds it, otherwise through reflection. */
    public Object deserialize(Json json) {
        String className = json.getAs("class");
        if (className == null) return json;

        Class<?> referenced = null;
        try {
            referenced = Class.forName(className);
        } catch (ClassNotFoundException ignored) {
            referenced = null; // TODO search in TagMap<String, Class<?>>
        }

        if (referenced == null) return json;
        else return null; // TODO deserialize through public fields and class
    }

    /** Parses a field into a specific type. */
    public Object deserializeField(String field) throws RuntimeException {
        if (field.equals("null")) return null;

        if (field.equals("true")) return true;
        if (field.equals("false")) return false;

        if (field.startsWith("\"") && field.endsWith("\"")) return deserializeString(field);
        if (field.startsWith("{") && field.endsWith("}")) return Json.read(field.substring(1, field.length() - 1));

        try {
            return Integer.parseInt(field);
        } catch (Throwable ignored) {}

        try {
            return Float.parseFloat(field);
        } catch (Throwable ignored) {}

        throw new RuntimeException("Unknown field type!");
    }

    /** Removes " on the sides and replaces \" with. " */
    public String deserializeString(String field) {
        return field.substring(1, field.length() - 1).replaceAll("\\\\\"", "\"");
    }

    public boolean serializable(Object object) {
        return !object.getClass().isPrimitive() && !unserializable.contains(object.getClass());
    }

    public <T> void addSerializer(Class<T> referenced, JsonSerializer<T> serializer) {
        if (unserializable.contains(referenced)) return; // pls, don't
        pairs.add(new ClassSerializerPair<T>(referenced, serializer));
    }

    public <T> ClassSerializerPair<T> getSerializer(T referenced) {
        for (ClassSerializerPair<?> pair : pairs) // class cast exception not possible
            if (pair.referenced.isInstance(referenced)) return (ClassSerializerPair<T>) pair;
        return null; // no serializer found so we will serialize via reflection
    }

    public record ClassSerializerPair<T>(Class<T> referenced, JsonSerializer<T> serializer) {}
}
