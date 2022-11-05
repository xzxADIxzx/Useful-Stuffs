package useful;

import arc.func.Cons;
import arc.graphics.Color;
import arc.struct.Seq;
import arc.util.Strings;
import mindustry.gen.Player;

public class Prefixes {

    public static Seq<PrefixData> datas = new Seq<>();

    // region data

    public static PrefixData get(Player player) {
        PrefixData data = datas.find(d -> d.player == player);
        return data == null ? datas.add(new PrefixData(player)).peek() : data;
    }

    public static void add(Player player, Prefix prefix) {
        get(player).with(pfs -> pfs.add(prefix)).apply();
    }

    public static void remove(Player player, Prefix prefix) {
        get(player).with(pfs -> pfs.remove(prefix)).apply();
    }

    public static boolean contains(Player player, Prefix prefix) {
        return get(player).prefixes.contains(prefix);
    }

    public static void clear(Player player) {
        get(player).with(Seq::clear).apply();
    }

    // endregion
    // region global

    public static void clear() {
        datas.clear();
    }

    public static void removeDisconnected() {
        datas.filter(data -> data.player.con.isConnected());
    }

    // endregion

    public static class Prefix {

        public String icon;
        public String full;

        public Prefix(String icon, String color) {
            this.icon = icon;
            this.full = Strings.format("[#@]<@>[]", color, icon);
        }

        public Prefix(String icon, Color color) {
            this(icon, color.toString());
        }

        public void apply(Player player) {
            player.name = Strings.format("@ @", full, player.getInfo().lastName);
        }
    }

    public static class PrefixData {

        public final Player player;
        public final Seq<Prefix> prefixes = new Seq<>();

        public PrefixData(Player player) {
            this.player = player;
        }

        public void apply() {
            if (prefixes.any()) prefixes.peek().apply(player);
            else player.name = player.getInfo().lastName;
        }

        public PrefixData with(Cons<Seq<Prefix>> cons) {
            cons.get(prefixes);
            return this;
        }
    }
}
