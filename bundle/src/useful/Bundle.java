package useful;

import arc.files.Fi;
import arc.func.Boolf;
import arc.struct.ObjectMap;
import arc.struct.Seq;
import arc.util.Log;
import arc.util.TextFormatter;
import mindustry.gen.Call;
import mindustry.gen.Groups;
import mindustry.gen.Player;
import mindustry.mod.Mod;

import static mindustry.Vars.*;

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
        load(main, "en");
    }

    public static void load(Fi directory) {
        load(directory, "en");
    }

    public static void load(Class<? extends Mod> main, String defaultLocaleCode) {
        load(mods.getMod(main).root.child("bundles"), defaultLocaleCode);
    }

    public static void load(Fi directory, String defaultLocaleCode) {
        directory.walk(fi -> {
            if (!fi.extEquals("properties")) return;

            var codes = fi.nameWithoutExtension().split("_");
            supported.add(codes.length == 2 ? new Locale(codes[1]) : new Locale(codes[1], codes[2]));
        });

        supported.each(locale -> bundles.put(locale, ResourceBundle.getBundle("bundles.bundle", locale)));
        supported.add(new Locale("router")); // :3

        defaultLocale = supported.find(locale -> locale.toString().equals(defaultLocaleCode));

        Log.info("Loaded @ locales, default is @.", supported.size, defaultLocale);
    }

    public static Locale locale(Player player) {
        return locale(player.locale);
    }

    public static Locale locale(String code) {
        var locale = supported.find(loc -> code.startsWith(loc.toString())); // toString because Mindustry uses _
        return locale == null ? defaultLocale : locale;
    }

    public static String get(String key, Player player) {
        return get(key, key, locale(player));
    }

    public static String get(String key, LocaleProvider provider) {
        return get(key, key, provider.locale());
    }

    public static String get(String key, String locale) {
        return get(key, key, locale(locale));
    }

    public static String get(String key, Locale locale) {
        return get(key, key, locale);
    }

    public static String get(String key, String defaultValue, Player player) {
        return get(key, defaultValue, locale(player));
    }

    public static String get(String key, String defaultValue, Locale locale) {
        if (locale.toString().equals("router")) return "router";
        try {
            var bundle = bundles.get(locale, bundles.get(defaultLocale));
            return bundle.containsKey(key) ? bundle.getString(key) : defaultValue;
        } catch (Throwable ignored) {
            return defaultValue; // null pointer from containsKey
        }
    }

    public static String format(String key, Player player, Object... values) {
        return format(key, key, locale(player), values);
    }

    public static String format(String key, LocaleProvider provider, Object... values) {
        return format(key, key, provider.locale(), values);
    }

    public static String format(String key, String locale, Object... values) {
        return format(key, key, locale(locale), values);
    }

    public static String format(String key, Locale locale, Object... values) {
        return format(key, key, locale, values);
    }

    public static String format(String key, String defaultValue, Player player, Object... values) {
        return format(key, defaultValue, locale(player), values);
    }

    public static String format(String key, String defaultValue, Locale locale, Object... values) {
        if (locale.toString().equals("router")) return "router";

        var pattern = get(key, defaultValue, locale);
        if (values.length == 0) return pattern;

        return new TextFormatter(locale, true).format(pattern, values);
    }

    // region single

    public static void send(Player player, String key) {
        player.sendMessage(get(key, player));
    }

    public static void send(Player player, String key, Object... values) {
        player.sendMessage(format(key, player, values));
    }

    public static void send(Player player, Player from, String text, String key) {
        player.sendMessage(get(key, player), from, text);
    }

    public static void send(Player player, Player from, String text, String key, Object... values) {
        player.sendMessage(format(key, player, values), from, text);
    }

    public static void infoMessage(Player player, String key) {
        Call.infoMessage(player.con, get(key, player));
    }

    public static void infoMessage(Player player, String key, Object... values) {
        Call.infoMessage(player.con, format(key, player, values));
    }

    public static void setHud(Player player, String key) {
        Call.setHudText(player.con, get(key, player));
    }

    public static void setHud(Player player, String key, Object... values) {
        Call.setHudText(player.con, format(key, player, values));
    }

    public static void announce(Player player, String key) {
        Call.announce(player.con, get(key, player));
    }

    public static void announce(Player player, String key, Object... values) {
        Call.announce(player.con, format(key, player, values));
    }

    public static void toast(Player player, int icon, String key) {
        Call.warningToast(player.con, icon, get(key, player));
    }

    public static void toast(Player player, int icon, String key, Object... values) {
        Call.warningToast(player.con, icon, format(key, player, values));
    }

    public static void label(Player player, float duration, float x, float y, String key) {
        Call.label(player.con, get(key, player), duration, x, y);
    }

    public static void label(Player player, float duration, float x, float y, String key, Object... values) {
        Call.label(player.con, format(key, player, values), duration, x, y);
    }

    public static void popup(Player player, float duration, int align, int top, int left, int bottom, int right, String key) {
        Call.infoPopup(player.con, get(key, player), duration, align, top, left, bottom, right);
    }

    public static void popup(Player player, float duration, int align, int top, int left, int bottom, int right, String key, Object... values) {
        Call.infoPopup(player.con, format(key, player, values), duration, align, top, left, bottom, right);
    }

    // endregion
    // region group

    public static void send(String key, Object... values) {
        Groups.player.each(player -> send(player, key, values));
    }

    public static void send(Boolf<Player> filter, String key, Object... values) {
        Groups.player.each(filter, player -> send(player, key, values));
    }

    public static void send(Player from, String text, String key, Object... values) {
        Groups.player.each(player -> send(player, from, text, key, values));
    }

    public static void send(Boolf<Player> filter, Player from, String text, String key, Object... values) {
        Groups.player.each(filter, player -> send(player, from, text, key, values));
    }

    public static void infoMessage(String key, Object... values) {
        Groups.player.each(player -> infoMessage(player, key, values));
    }

    public static void infoMessage(Boolf<Player> filter, String key, Object... values) {
        Groups.player.each(filter, player -> infoMessage(player, key, values));
    }

    public static void setHud(String key, Object... values) {
        Groups.player.each(player -> setHud(player, key, values));
    }

    public static void setHud(Boolf<Player> filter, String key, Object... values) {
        Groups.player.each(filter, player -> setHud(player, key, values));
    }

    public static void announce(String key, Object... values) {
        Groups.player.each(player -> announce(player, key, values));
    }

    public static void announce(Boolf<Player> filter, String key, Object... values) {
        Groups.player.each(filter, player -> announce(player, key, values));
    }

    public static void toast(int icon, String key, Object... values) {
        Groups.player.each(player -> toast(player, icon, key, values));
    }

    public static void toast(Boolf<Player> filter, int icon, String key, Object... values) {
        Groups.player.each(filter, player -> toast(player, icon, key, values));
    }

    public static void label(float duration, float x, float y, String key, Object... values) {
        Groups.player.each(player -> label(player, duration, x, y, key, values));
    }

    public static void label(Boolf<Player> filter, float duration, float x, float y, String key, Object... values) {
        Groups.player.each(filter, player -> label(player, duration, x, y, key, values));
    }

    public static void popup(float duration, int align, int top, int left, int bottom, int right, String key, Object... values) {
        Groups.player.each(player -> popup(player, duration, align, top, left, bottom, right, key, values));
    }

    public static void popup(Boolf<Player> filter, float duration, int align, int top, int left, int bottom, int right, String key, Object... values) {
        Groups.player.each(filter, player -> popup(player, duration, align, top, left, bottom, right, key, values));
    }

    // endregion

    /** Used in some player data classes to shorten code. */
    public interface LocaleProvider {
        Locale locale();
    }
}