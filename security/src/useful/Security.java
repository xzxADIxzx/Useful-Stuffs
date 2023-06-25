package useful;

import arc.struct.Seq;
import arc.util.*;
import arc.util.serialization.Jval;

import static mindustry.Vars.*;

// TODO (дарк, ади) -> добавить anti vpn
public class Security {

    public static final String githubMetaURL = "https://api.github.com/meta";
    public static final String datacentersURL = "https://raw.githubusercontent.com/X4BNet/lists_vpn/main/output/datacenter/ipv4.txt";
    public static final Seq<String> ips = new Seq<>();

    public static void loadGithubActionIPs() {
        Http.get(githubMetaURL, result -> {
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