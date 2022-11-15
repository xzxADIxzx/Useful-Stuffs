package useful;

import arc.func.Cons;
import arc.graphics.Color;
import arc.struct.Seq;
import arc.util.Reflect;
import arc.util.Strings;
import mindustry.gen.Player;
import mindustry.mod.Mod;

public class Prefixes {

    /** All player prefixes datas. */
    public static Seq<PrefixData> datas;

    /** Creates new {@link #datas} or loads already created from another class loader. */
    public static void load(Class<? extends Mod> main) {
        try {
            Class<?> singleton = main.getClassLoader().getParent().loadClass("useful.Prefixes");
            datas = Reflect.get(singleton, "datas"); // use already create datas
        } catch (Throwable ignored) {
            datas = new Seq<>(); // singleton not found
        }
    }

    /** Iterate over all values in {@link #datas}. */
    public static void each(Cons<PrefixData> cons) {
        datas.each(cons);
    }

    /** Returns the player's data or creates a new one if it doesn't exist. */
    public static PrefixData get(Player player) {
        PrefixData data = datas.find(d -> d.player == player);
        return data == null ? datas.add(new PrefixData(player)).peek() : data;
    }

    /** Removes all prefixes from player nicknames and clears {@link #datas}. */
    public static void clear() {
        datas.each(data -> data.clear().apply());
        datas.clear();
    }

    /** Removes disconnected players from the {@link #datas}. */
    public static void removeDisconnected() {
        datas.filter(data -> data.player.con.isConnected());
    }

    public static class Prefix {

        public String icon;
        public String full;

        /** Monocolor prefix with hex or string color. */
        public Prefix(String icon, String color) {
            this.icon = icon;
            this.full = Strings.format("[@]<@>[]", color, icon);
        }

        /** Monocolor prefix. */
        public Prefix(String icon, Color color) {
            this(icon, '#' + color.toString());
        }

        /** Multicolor prefix with hex or string color. */
        public Prefix(String icon, String bracketsColor, String iconColor) {
            this.icon = icon;
            this.full = Strings.format("[@]<[@]@[]>[]", bracketsColor, iconColor, icon);
        }

        /** Multicolor prefix. */
        public Prefix(String icon, Color bracketsColor, Color iconColor) {
            this(icon, '#' + bracketsColor.toString(), '#' + iconColor.toString());
        }

        /** Applies prefix to the player's nickname. */
        public void apply(Player player) {
            player.name = Strings.format("@ @", full, player.getInfo().lastName);
        }

        /** Applies prefix to the given string. */
        public String apply(String base) {
            return Strings.format("@ @", full, base);
        }

        @Override
        public String toString() {
            return super.toString() + " # " + full;
        }
    }

    public static class PrefixData {

        public final Player player;
        public final Seq<Prefix> prefixes = new Seq<>();

        public PrefixData(Player player) {
            this.player = player;
        }

        /** Applies a top prefix to the player's nickname. */
        public void apply() {
            if (prefixes.any()) prefixes.peek().apply(player);
            else player.name = player.getInfo().lastName;
        }

        /** Adds the prefix and returns itself to further change or {@link #apply()} changes. */
        public PrefixData add(Prefix prefix) {
            prefixes.addUnique(prefix);
            return this;
        }

        /** Removes the prefix and returns itself to further change or {@link #apply()} changes. */
        public PrefixData remove(Prefix prefix) {
            prefixes.remove(prefix);
            return this;
        }

        /** @return whether the data contains this prefix. */
        public boolean contains(Prefix prefix) {
            return prefixes.contains(prefix);
        }

        /** Clears all prefixes and returns itself to further change or {@link #apply()} changes. */
        public PrefixData clear() {
            prefixes.clear();
            return this;
        }
    }
}
