import arc.struct.Seq;
import arc.struct.StringMap;
import mindustry.game.Schematic;
import mindustry.game.Schematics;
import mindustry.game.Schematic.Stile;
import mindustry.world.Block;

import static mindustry.Vars.*;

/**
 * This exists for more compact storage of schematics than via base64,
 * therefore does not support any block configuration.
 * Best suited for storing terrain schematics for plugins or game modes.
 * 
 * @author xzxADIxzx
 */
public class ShortSchematics {

    /** Version of the script, used to avoid errors when decompiling the string. */
    public static final int version = 0;

    /** List of all characters that are used to store integers */
    public static final String symbols = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwYyXxZz" // i know it's a bad idea
                                       + "АаБбВвГгДдЕеЁёЖжЗзИиЙйКкЛлМмНнОоПпРрСсТтУуФфХхЦцЧчШшЩщЪъЫЫЬьЭэЮюЯя";

    /** Maximum value that can be stored in one character. */
    public static final int max = symbols.length() - 1;

    public static String write(String base) {
        return write(Schematics.readBase64(base));
    }

    public static String write(Schematic schem) {
        Seq<Short> out = new Seq<>();
        out.add((short) (schem.width),
                (short) (schem.height));

        schem.tiles.each(st -> {
            out.add((short) (st.block.id + max), st.x, st.y);
            if (st.block.rotate) out.add((short) st.rotation);
        });

        StringBuilder result = new StringBuilder("#" + symbols.charAt(version));
        for (Short value : out)
            if (value <= max) result.append(symbols.charAt(value));
            else result.append(symbols.charAt(value / max)).append(symbols.charAt(value % max));
        return result.toString();
    }

    public static Schematic read(String base) {
        return read(new Strinter(base));
    }

    public static Schematic read(Strinter i) {
        Seq<Stile> out = new Seq<>();
        int width = i.next();
        int height = i.next();

        while (i.hasNext()) {
            Block block = content.block(i.nextTwo() - max);
            out.add(new Stile(block, i.next(), i.next(), null, (byte) (block.rotate ? i.next() : 0)));
        }

        return new Schematic(out, new StringMap(), width, height);
    }

    /** Yes it is an iterator but it is very necessary */
    public static class Strinter {

        private final String base;
        private int index;

        public Strinter(String base) {
            if (!base.startsWith("#")) throw new RuntimeException("All short schematics start with #");
            this.base = base.replace("#", "");
            if (next() != version) throw new VerifyError("The schematic version does not match the script version");
        }

        public int next() {
            return symbols.indexOf(base.charAt(index++));
        }

        public int nextTwo() {
            return next() * max + next();
        }

        public boolean hasNext() {
            return index < base.length();
        }
    }
}
