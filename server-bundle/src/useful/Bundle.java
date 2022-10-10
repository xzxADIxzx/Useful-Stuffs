package useful;

import arc.func.Boolf;
import arc.struct.ObjectMap;
import arc.struct.Seq;
import arc.util.Log;
import mindustry.gen.Groups;
import mindustry.gen.Player;
import mindustry.mod.Mod;

import static mindustry.Vars.*;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Simple L10N bundle for Mindustry plugins.
 * 
 * @author xzxADIxzx
 */
public class Bundle {

    public static final Seq<Locale> supported = new Seq<>();
    public static final ObjectMap<Locale, ResourceBundle> bundles = new ObjectMap<>();

    public static Locale defaultLocale;

    public static void load(Class<? extends Mod> main) {
        supported.add(new Locale("router")); // :3
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

    public static String get(String key, Player player) {
        return get(key, key, locale(player));
    }

    public static String get(String key, Locale locale) {
        return get(key, key, locale);
    }

    public static String get(String key, String defaultValue, Locale locale) {
        if (locale.toString().equals("router")) return "router";
        try {
            ResourceBundle bundle = bundles.get(locale, bundles.get(defaultLocale));
            return bundle.getString(key);
        } catch (Throwable ignored) {
            return defaultValue; // missing resource
        }
    }

    public static String format(String key, Player player, Object... values) {
        return format(key, locale(player), values);
    }

    public static String format(String key, Locale locale, Object... values) {
        if (locale.toString().equals("router")) return "router";

        var pattern = get(key, locale);
        if (values.length == 0) return pattern;

        return MessageFormat.format(pattern, values);
    }

    public static void bundled(Player player, String key) {
        player.sendMessage(get(key, player));
    }

    public static void bundled(Player player, String key, Object... values) {
        player.sendMessage(format(key, player, values));
    }

    public static void sendToChat(String key, Object... values) {
        Groups.player.each(player -> bundled(player, key, values));
    }

    public static void sendToChat(Boolf<Player> filter, String key, Object... values) {
        Groups.player.each(filter, player -> bundled(player, key, values));
    }
}