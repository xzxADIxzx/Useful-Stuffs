package useful.menu;

import arc.func.Func3;
import arc.util.Strings;
import mindustry.gen.Player;

public class MenuFormatter {

    public static Func3<Player, String, Object[], String> formatter = (player, text, values) -> Strings.format(text, values);

    public static void setFormatter(Func3<Player, String, Object[], String> formatter) {
        MenuFormatter.formatter = formatter;
    }

    public static String format(Player player, String text, Object... values) {
        return formatter.get(player, text, values);
    }
}