package useful;

import arc.struct.Seq;
import arc.util.Http;
import arc.util.Log;
import arc.util.serialization.Jval;

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
    }
}