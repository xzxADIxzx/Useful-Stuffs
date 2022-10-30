package useful;

import arc.Events;
import arc.func.Intc;
import arc.struct.IntMap;
import mindustry.game.EventType.*;
import mindustry.gen.Call;
import mindustry.gen.Player;
import mindustry.ui.Menus.MenuListener;

public class DynamicMenus {

    public static int id;
    public static IntMap<MenuListener> listeners = new IntMap<>();

    public static void load() {
        Events.on(MenuOptionChooseEvent.class, event -> {
            if (event.option >= 0) return; // option selected on the menu that has been registered via Menus

            var listener = listeners.remove(event.menuId);
            if (listener != null) listener.get(event.player, event.option);
        });
    }

    /** Clear all listeners. Can be useful in case of game over or world loading. */
    public static void clear() {
        listeners.clear();
    }

    /** Register a temporary menu listener that will be lost after choosing an option. */
    public static int register(MenuListener listener) {
        listeners.put(--id, listener);
        return id;
    }

    /** Register a temporary menu listener that doesn't return a player. */
    public static int register(Intc listener) {
        return register((player, option) -> listener.get(option));
    }

    public static void menu(String title, String message, String[][] options, MenuListener listener) {
        Call.menu(register(listener), title, message, options);
    }

    public static void menu(String title, String message, String[][] options, Intc listener) {
        Call.menu(register(listener), title, message, options);
    }

    public static void menu(Player player, String title, String message, String[][] options, MenuListener listener) {
        Call.menu(player.con, register(listener), title, message, options);
    }

    public static void menu(Player player, String title, String message, String[][] options, Intc listener) {
        Call.menu(player.con, register(listener), title, message, options);
    }
}
