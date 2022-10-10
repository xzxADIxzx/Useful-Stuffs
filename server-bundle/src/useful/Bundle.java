package useful;

import arc.struct.ObjectMap;
import arc.struct.Seq;
import arc.util.Log;
import mindustry.gen.Player;
import mindustry.mod.Mod;

import static mindustry.Vars.*;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class Bundle {

    public static final Seq<Locale> supported = new Seq<>();
    public static final ObjectMap<Locale, ResourceBundle> bundles = new ObjectMap<>();

    public static Locale defaultLocale;

    public static void load(Class<? extends Mod> main) {
        mods.getMod(main).root.child("bundles").walk(fi -> {
            if (!fi.extEquals("properties")) return;

            String[] codes = fi.nameWithoutExtension().split("_");
            supported.add(codes.length == 2 ? new Locale(codes[1]) : new Locale(codes[1], codes[2]));
        });

        supported.each(locale -> bundles.put(locale, ResourceBundle.getBundle("bundles.bundle", locale)));
        defaultLocale = supported.find(locale -> locale.toString().equals("en"));

        Log.info("Loaded @ locales, default is @.", supported.size, defaultLocale);
    }

    public static Locale locale(Player player) {
        Locale locale = supported.find(loc -> player.locale.startsWith(loc.toString())); // toString because Mindustry uses _
        return locale == null ? defaultLocale : locale;
    }

    public static String get(String key, String defaultValue, Locale locale) {
        try {
            ResourceBundle bundle = bundles.get(locale, bundles.get(defaultLocale));
            return bundle.getString(key);
        } catch (Throwable ignored) {
            return defaultValue; // missing resource
        }
    }

    public static String format(String key, Locale locale, Object... values) {
        var pattern = get(key, locale);
        if (values.length == 0) return pattern;

        return MessageFormat.format(pattern, values);
    }
}
