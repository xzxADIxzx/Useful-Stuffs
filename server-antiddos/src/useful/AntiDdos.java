package useful;

import arc.struct.Seq;
import arc.util.Http;
import arc.util.Log;
import arc.util.Time;
import arc.util.Timer;
import arc.util.serialization.Jval;
import mindustry.gen.Groups;

import static mindustry.Vars.*;

/** Adds GitHub Actions IPs to blacklist. */
public class AntiDdos {

    public static final String actionsURL = "https://api.github.com/meta";
    public static final Seq<String> ips = new Seq<>();

    public static void load() {
        Http.get(actionsURL, result -> {
            var json = Jval.read(result.getResultAsString());
            json.get("actions").asArray().each(element -> {
                String ip = element.asString();
                if (ip.charAt(4) != ':') ips.add(ip); // skip IPv6
            });

            netServer.admins.dosBlacklist.addAll(ips);
            Log.info("Added @ GitHub Actions IPs to blacklist.", ips.size);
        }, error -> Log.err("Failed to fetch GitHub Actions IPs", error));

        Timer.schedule(() -> Groups.player.each(
            player -> Time.timeSinceMillis(player.con.connectTime) > 10000 && Time.timeSinceMillis(player.con.lastReceivedClientTime) <= 0,
            player -> {
                Log.warn("Blacklisting IP '@' as potential DOS attack - no snapshots received.", player.con.address);
                player.con.close();
                netServer.admins.blacklistDos(player.con.address);
            }
        ), 0f, 3f);
    }
}