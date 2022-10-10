package useful;

import useful.Json.JsonSerializable;
import useful.Json.JsonSerializer;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

/** 
 * Converts objects to {@link Json} using registered {@link JsonSerializer}s or reflection.
 * 
 * @author xzxADIxzx
 */
@SuppressWarnings("unchecked")
public class JsonSerializator {

    private static ArrayList<Class<?>> unserializable = new ArrayList<>() {{ // ._.
        add(Object.class); add(Byte.class); add(Short.class); add(Integer.class); add(Long.class);
        add(Character.class); add(Boolean.class); add(Float.class); add(Double.class);
    }};

    private ArrayList<ClassSerializerPair<?>> pairs = new ArrayList<>();
    private ArrayList<StringClassPair> tags = new ArrayList<>();

    /**
     * Searches for a {@link JsonSerializer} in {@link JsonSerializator#pairs} and serializes through it if finds it, otherwise through reflection.
     * Only supports objects! If you need to serialize int or float call {@link #serializeField(Object)}.
     */
    public Json serialize(Object object) {
        Class<Object> referenced = (Class<Object>) object.getClass();
        ClassSerializerPair<Object> pair = getSerializer(referenced);

        if (pair != null) return pair.serializer.write(object);
        else {
            Json json = new Json(referenced);
            try {
                for (Field field : referenced.getFields())
                    if (serializable(field.getModifiers())) json.put(field.getName(), field.get(object));
            } catch (Throwable ignored) {} // how is this even possible if we iterate over public fields?!
            return json;
        }
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

        Class<Object> referenced;
        try {
            referenced = (Class<Object>) Class.forName(className);
        } catch (ClassNotFoundException ignored) {
            referenced = (Class<Object>) getByTag(className);
        }

        if (referenced == null) return json;

        Object object;
        try {
            object = referenced.getDeclaredConstructor().newInstance();
        } catch (Throwable ignored) {
            return json;
        }

        ClassSerializerPair<Object> pair = getSerializer(referenced);
        if (pair != null) pair.serializer.read(object, json);
        else try {
            for (Field field : referenced.getFields())
                if (serializable(field.getModifiers())) field.set(object, json.get(field.getName()));
        } catch (Throwable ignored) {}

        return object;
    }

    /** Parses a field into a specific type. */
    public Object deserializeField(String field) throws RuntimeException {
        if (field.equals("null")) return null;

        if (field.equals("true")) return true;
        if (field.equals("false")) return false;

        if (field.startsWith("\"") && field.endsWith("\"")) return deserializeString(field);
        if (field.startsWith("{") && field.endsWith("}")) return Json.read(field);

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

    public boolean serializable(int modifiers) {
        return !Modifier.isStatic(modifiers) && !Modifier.isFinal(modifiers);
    }

    public <T> void addSerializer(Class<T> referenced, JsonSerializer<T> serializer) {
        if (unserializable.contains(referenced)) return; // pls, don't
        pairs.add(new ClassSerializerPair<T>(referenced, serializer));
    }

    public <T> ClassSerializerPair<T> getSerializer(Class<T> referenced) {
        for (ClassSerializerPair<?> pair : pairs) // class cast exception not possible
            if (pair.referenced.isAssignableFrom(referenced)) return (ClassSerializerPair<T>) pair;
        return null; // no serializer found so we will serialize via reflection
    }

    public <T> void addTag(String tag, Class<?> referenced) {
        tags.add(new StringClassPair(tag, referenced));
    }

    public Class<?> getByTag(String tag) {
        for (StringClassPair pair : tags)
            if (pair.tag.equals(tag)) return pair.referenced;
        return null; // no class by tag found
    }

    public record ClassSerializerPair<T>(Class<T> referenced, JsonSerializer<T> serializer) {}

    public record StringClassPair(String tag, Class<?> referenced) {}
}
