package useful.menu;

import arc.func.Func3;
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
}