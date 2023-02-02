package useful;

import arc.func.*;
import arc.util.Strings;
import mindustry.gen.Player;

public class MenuFormatter {

    public static Func3<String, Player, Object[], String> formatter = (text, player, values) -> Strings.format(text, values);

    public static void setFormatter(Func3<String, Player, Object[], String> formatter) {
        MenuFormatter.formatter = formatter;
    }

    public static String format(String text, Player player, Object... values) {
        return formatter.get(text, player, values);
    }

    public static String[][] format(String[][] texts, Player player) {
        for (int x = 0; x < texts.length; x++)
            for (int y = 0; y < texts[x].length; y++)
                texts[x][y] = format(texts[x][y], player);

        return texts;
    }
}