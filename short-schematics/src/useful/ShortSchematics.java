package useful;

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

    /** List of all characters that are used to store integers. */
    public static final String symbols = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwYyXxZz" // i know it's a bad idea
                                       + "АаБбВвГгДдЕеЁёЖжЗзИиЙйКкЛлМмНнОоПпРрСсТтУуФфХхЦцЧчШшЩщЪъЫЫЬьЭэЮюЯя";

    /** Maximum value that can be stored in one character. */
    public static final int max = symbols.length() - 1;

    public static String write(String base) {
        return write(Schematics.readBase64(base));
    }

    public static String write(Schematic schem) {
        schem.tiles.sort(st -> st.x + st.y * schem.width);

        Seq<Short> out = new Seq<>();
        out.add((short) (schem.width),
                (short) (schem.height));

        Stile last = schem.tiles.get(0);
        for (Stile st : schem.tiles) {
            if (subsequence(last, st)) { // tiles are in sequence one after another
                if (out.get(out.size - 2) == -1) out.add((short) (out.pop() + 1));
                else out.add((short) -1, (short) 1);
            } else {
                out.add((short) (st.block.id + max), st.x, st.y);
                if (st.block.rotate) out.add((short) st.rotation);
            }
            last = st;
        }

        StringBuilder result = new StringBuilder("#" + symbols.charAt(version));
        for (Short value : out)
            if (value <= max) result.append(value == -1 ? "^" : symbols.charAt(value));
            else result.append(symbols.charAt(value / max)).append(symbols.charAt(value % max));
        return result.toString();
    }

    /** Returns whether the second matches the first, but one tile to the right of the first. */
    private static boolean subsequence(Stile first, Stile second) {
        return first.block == second.block && first.x + 1 == second.x && first.y == second.y && first.rotation == second.rotation;
    }

    public static Schematic read(String base) {
        return read(new Strinter(base));
    }

    public static Schematic read(Strinter i) {
        Seq<Stile> out = new Seq<>();
        int width = i.next();
        int height = i.next();

        Stile last = null;
        while (i.hasNext()) {
            if (i.hasSeq()) {
                int amount = i.nextSeq();
                for (int j = 0; j < amount; j++) out.add(last = subsequence(last));
            } else {
                Block block = content.block(i.nextTwo() - max);
                out.add(last = new Stile(block, i.next(), i.next(), null, (byte) (block.rotate ? i.next() : 0)));
            }
        }

        return new Schematic(out, new StringMap(), width, height);
    }

    private static Stile subsequence(Stile from) {
        Stile st = from.copy();
        st.x++;
        return st;
    }

    /** Yes it is an iterator but it is very necessary. */
    public static class Strinter {

        private final String base;
        private int index;

        public Strinter(String base) {
            if (!base.startsWith("#")) throw new RuntimeException("All short schematics start with #");
            this.base = base.substring(1); // skip # char
            if (next() != version) throw new VerifyError("The schematic version does not match the script version");
        }

        public int next() {
            return symbols.indexOf(base.charAt(index++));
        }

        public int nextTwo() {
            return next() * max + next();
        }

        public int nextSeq() {
            index++; // skip ^ char
            return next();
        }

        public boolean hasNext() {
            return index < base.length();
        }

        public boolean hasSeq() {
            return base.charAt(index) == '^';
        }
    }
}
