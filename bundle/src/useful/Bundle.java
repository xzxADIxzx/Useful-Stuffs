package useful;

import arc.files.Fi;
import arc.func.Boolf;
import arc.struct.*;
import arc.util.*;
import mindustry.gen.*;
import mindustry.mod.Mod;
import mindustry.net.NetConnection;
import net.time4j.*;
import net.time4j.format.TextWidth;
import net.time4j.scale.TimeScale;
import net.time4j.tz.Timezone;

import java.text.*;
import java.time.*;
import java.time.Duration;
import java.time.temporal.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static mindustry.Vars.*;

/**
 * Simple L10N bundle for Mindustry plugins.
 *
 * @author xzxADIxzx
 */
public class Bundle {

    public static final Seq<Locale> supported = new Seq<>();
    public static final ObjectMap<Locale, StringMap> bundles = new ObjectMap<>();

    public static Locale defaultLocale;
    public static StringMap defaultBundle;

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
            supported.addUnique(codes.length == 2 ? new Locale(codes[1]) : new Locale(codes[1], codes[2]));
        });

        supported.each(locale -> {
            if (locale.toString().equals("router")) return;

            var bundle = ResourceBundle.getBundle("bundles.bundle", locale);
            bundle.keySet().forEach(key -> bundles.get(locale, StringMap::new).put(key, bundle.getString(key)));
        });

        supported.addUnique(new Locale("router")); // :3

        defaultLocale = supported.find(locale -> locale.toString().equals(defaultLocaleCode));
        defaultBundle = bundles.get(defaultLocale);

        Log.info("Loaded @ locales, default is @.", supported.size, defaultLocale);
    }

    // region locale

    public static Locale locale(Player player) {
        return locale(player.locale);
    }

    public static Locale locale(String code) {
        var locale = supported.find(loc -> code.startsWith(loc.toString()));
        return locale == null ? defaultLocale : locale;
    }

    // endregion
    // region has

    public static boolean has(String key) {
        return has(key, defaultLocale);
    }

    public static boolean has(String key, Player player) {
        return has(key, locale(player));
    }

    public static boolean has(String key, String locale) {
        return has(key, locale(locale));
    }

    public static boolean has(String key, Locale locale) {
        if (locale.toString().equals("router")) return true;
        return bundles.containsKey(locale) && bundles.get(locale).containsKey(key);
    }

    // endregion
    // region get

    public static String getDefault(String key) {
        return get(key, defaultLocale);
    }

    public static String getDefault(String key, String defaultValue) {
        return get(key, defaultValue, defaultLocale);
    }

    public static String get(String key, Player player) {
        return get(key, key, locale(player));
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

        var bundle = bundles.get(locale, defaultBundle);
        return bundle.get(key, defaultBundle.get(key, defaultValue));
    }

    // endregion
    // region format

    public static String formatDefault(String key, Object... values) {
        return format(key, key, defaultLocale, values);
    }

    public static String formatDefault(String key, String defaultValue, Object... values) {
        return format(key, defaultValue, defaultLocale, values);
    }

    public static String format(String key, Player player, Object... values) {
        return format(key, key, locale(player), values);
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

    // endregion
    // region date/time

    public static String formatDate(Object date) {
        return formatDate(defaultLocale, date);
    }

    public static String formatDate(Player player, Object date) {
        return formatDate(locale(player), date);
    }

    public static String formatDate(String locale, Object date) {
        return formatDate(locale(locale), date);
    }

    public static String formatDate(Locale locale, Object date) {
        return formatDate(locale, DateFormat.LONG, date);
    }

    public static String formatDate(int dateStyle, Object date) {
        return formatDate(defaultLocale, dateStyle, date);
    }

    public static String formatDate(Player player, int dateStyle, Object date) {
        return formatDate(locale(player), dateStyle, date);
    }

    public static String formatDate(String locale, int dateStyle, Object date) {
        return formatDate(locale(locale), dateStyle, date);
    }

    public static String formatDate(Locale locale, int dateStyle, Object date) {
        return SimpleDateFormat.getDateInstance(dateStyle, locale).format(date);
    }

    public static String formatTime(Object date) {
        return formatTime(defaultLocale, date);
    }

    public static String formatTime(Player player, Object date) {
        return formatTime(locale(player), date);
    }

    public static String formatTime(String locale, Object date) {
        return formatTime(locale(locale), date);
    }

    public static String formatTime(Locale locale, Object date) {
        return formatTime(locale, DateFormat.SHORT, date);
    }

    public static String formatTime(int timeStyle, Object date) {
        return formatTime(defaultLocale, timeStyle, date);
    }

    public static String formatTime(Player player, int timeStyle, Object date) {
        return formatTime(locale(player), timeStyle, date);
    }

    public static String formatTime(String locale, int timeStyle, Object date) {
        return formatTime(locale(locale), timeStyle, date);
    }

    public static String formatTime(Locale locale, int timeStyle, Object date) {
        return SimpleDateFormat.getTimeInstance(timeStyle, locale).format(date);
    }

    public static String formatDateTime(Object date) {
        return formatDateTime(defaultLocale, date);
    }

    public static String formatDateTime(Player player, Object date) {
        return formatDateTime(locale(player), date);
    }

    public static String formatDateTime(String locale, Object date) {
        return formatDateTime(locale(locale), date);
    }

    public static String formatDateTime(Locale locale, Object date) {
        return formatDateTime(locale, DateFormat.LONG, DateFormat.SHORT, date);
    }

    public static String formatDateTime(int dateStyle, int timeStyle, Object date) {
        return formatDateTime(defaultLocale, dateStyle, timeStyle, date);
    }

    public static String formatDateTime(Player player, int dateStyle, int timeStyle, Object date) {
        return formatDateTime(locale(player), dateStyle, timeStyle, date);
    }

    public static String formatDateTime(String locale, int dateStyle, int timeStyle, Object date) {
        return formatDateTime(locale(locale), dateStyle, timeStyle, date);
    }

    public static String formatDateTime(Locale locale, int dateStyle, int timeStyle, Object date) {
        return SimpleDateFormat.getDateTimeInstance(dateStyle, timeStyle, locale).format(date);
    }

    // endregion
    // region duration

    public static String formatDuration(long duration) {
        return formatDuration(duration, TimeUnit.SECONDS);
    }

    public static String formatDuration(Player player, long duration) {
        return formatDuration(player, duration, TimeUnit.SECONDS);
    }

    public static String formatDuration(String locale, long duration) {
        return formatDuration(locale, duration, TimeUnit.SECONDS);
    }

    public static String formatDuration(Locale locale, long duration) {
        return formatDuration(locale, duration, TimeUnit.SECONDS);
    }

    public static String formatDuration(long duration, TimeUnit precision) {
        return formatDuration(Duration.ofMillis(duration).truncatedTo(precision.toChronoUnit()));
    }

    public static String formatDuration(Player player, long duration, TimeUnit precision) {
        return formatDuration(player, Duration.ofMillis(duration).truncatedTo(precision.toChronoUnit()));
    }

    public static String formatDuration(String locale, long duration, TimeUnit precision) {
        return formatDuration(locale, Duration.ofMillis(duration).truncatedTo(precision.toChronoUnit()));
    }

    public static String formatDuration(Locale locale, long duration, TimeUnit precision) {
        return formatDuration(locale, Duration.ofMillis(duration).truncatedTo(precision.toChronoUnit()));
    }

    public static String formatDuration(TemporalAmount duration) {
        return formatDuration(defaultLocale, duration);
    }

    public static String formatDuration(Player player, TemporalAmount duration) {
        return formatDuration(locale(player), duration);
    }

    public static String formatDuration(String locale, TemporalAmount duration) {
        return formatDuration(locale(locale), duration);
    }

    public static String formatDuration(Locale locale, TemporalAmount duration) {
        return PrettyTime.of(locale).print(duration);
    }

    public static String formatDuration(TemporalAmount duration, TextWidth width) {
        return formatDuration(defaultLocale, duration, width);
    }

    public static String formatDuration(Player player, TemporalAmount duration, TextWidth width) {
        return formatDuration(locale(player), duration, width);
    }

    public static String formatDuration(String locale, TemporalAmount duration, TextWidth width) {
        return formatDuration(locale(locale), duration, width);
    }

    public static String formatDuration(Locale locale, TemporalAmount duration, TextWidth width) {
        return PrettyTime.of(locale).print(duration, width);
    }

    // endregion
    // region relative

    public static String formatRelative(long timestamp) {
        return formatRelative(defaultLocale, timestamp);
    }

    public static String formatRelative(Player player, long timestamp) {
        return formatRelative(locale(player), timestamp);
    }

    public static String formatRelative(String locale, long timestamp) {
        return formatRelative(locale(locale), timestamp);
    }

    public static String formatRelative(Locale locale, long timestamp) {
        return formatRelative(locale, timestamp, TimeUnit.SECONDS);
    }

    public static String formatRelative(long timestamp, TimeUnit precision) {
        return formatRelative(defaultLocale, timestamp, precision);
    }

    public static String formatRelative(Player player, long timestamp, TimeUnit precision) {
        return formatRelative(locale(player), timestamp, precision);
    }

    public static String formatRelative(String locale, long timestamp, TimeUnit precision) {
        return formatRelative(locale(locale), timestamp, precision);
    }

    public static String formatRelative(Locale locale, long timestamp, TimeUnit precision) {
        return PrettyTime.of(locale).printRelative(Moment.of(Math.floorDiv(timestamp, 1000L), TimeScale.POSIX), Timezone.ofSystem(), precision);
    }

    // endregion
    // region single

    public static void send(Player player, String key) {
        player.sendMessage(get(key, player));
    }

    public static void send(Player player, String key, Object... values) {
        player.sendMessage(format(key, player, values));
    }

    public static void sendFrom(Player player, Player from, String text, String key) {
        player.sendMessage(format(key, player, text), from, text);
    }

    public static void sendFrom(Player player, Player from, String text, String key, Object... values) {
        player.sendMessage(format(key, player, Structs.add(values, text)), from, text);
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

    public static KickBuilder kick(Player player, String key) {
        return new KickBuilder(player.con, player.locale).add(key);
    }

    public static KickBuilder kick(Player player, String key, Object... values) {
        return new KickBuilder(player.con, player.locale).add(key, values);
    }

    public static KickBuilder kick(NetConnection connection, String locale, String key) {
        return new KickBuilder(connection, locale).add(key);
    }

    public static KickBuilder kick(NetConnection connection, String locale, String key, Object... values) {
        return new KickBuilder(connection, locale).add(key, values);
    }

    public static void kick(Player player, long duration, String key) {
        new KickBuilder(player.con, player.locale).add(key).kick(duration);
    }

    public static void kick(Player player, long duration, String key, Object... values) {
        new KickBuilder(player.con, player.locale).add(key, values).kick(duration);
    }

    public static void kick(NetConnection connection, String locale, long duration, String key) {
        new KickBuilder(connection, locale).add(key).kick(duration);
    }

    public static void kick(NetConnection connection, String locale, long duration, String key, Object... values) {
        new KickBuilder(connection, locale).add(key, values).kick(duration);
    }

    // endregion
    // region group

    public static void send(String key, Object... values) {
        Groups.player.each(player -> send(player, key, values));
    }

    public static void send(Boolf<Player> filter, String key, Object... values) {
        Groups.player.each(filter, player -> send(player, key, values));
    }

    public static void sendFrom(Player from, String text, String key, Object... values) {
        Groups.player.each(player -> sendFrom(player, from, text, key, values));
    }

    public static void sendFrom(Boolf<Player> filter, Player from, String text, String key, Object... values) {
        Groups.player.each(filter, player -> sendFrom(player, from, text, key, values));
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
}