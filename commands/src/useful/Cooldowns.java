package useful;

import arc.util.*;
import mindustry.gen.Player;

public class Cooldowns {

    public static final ExtendedMap<String, ExtendedMap<String, Long>> cooldowns = new ExtendedMap<>();

    public static long defaultCooldown = 1000L;   // 1 second
    public static boolean restrictAdmins = false; // By default, admins are not restricted

    public static void defaultCooldown(long cooldown) {
        defaultCooldown = cooldown;
    }

    public static void restrictAdmins(boolean admins) {
        restrictAdmins = admins;
    }

    static {
        Timer.schedule(() -> {
            cooldowns.eachValue(commands -> commands.removeAll((command, time) -> Time.timeSinceMillis(time) > 0));
            cooldowns.removeAll((uuid, commands) -> commands.isEmpty());
        }, 60f, 60f);
    }

    public static boolean canRun(Player player, String command) {
        if (player.admin && !restrictAdmins)
            return true;

        return Time.timeSinceMillis(cooldowns.get(player.uuid(), ExtendedMap::new).get(command, 0L)) > 0;
    }

    public static void run(Player player, String command, long cooldown) {
        if (player.admin && !restrictAdmins)
            return;

        cooldowns.get(player.uuid(), ExtendedMap::new).put(command, Time.millis() + cooldown);
    }
}