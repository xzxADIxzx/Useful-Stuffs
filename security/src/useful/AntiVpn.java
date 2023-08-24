package useful;

import arc.struct.Seq;
import arc.util.Http;
import arc.util.Log;
import lombok.SneakyThrows;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class AntiVpn {
    public static final Seq<Subnet> subnets = new Seq<>();

    public static void load() {
        Http.get("https://raw.githubusercontent.com/X4BNet/lists_vpn/main/output/datacenter/ipv4.txt")
                .timeout(0)
                .error(error -> Log.err("Failed to fetch datacenter subnets", error))
                .submit(response -> {
                    var result = response.getResultAsString().split("\n");
                    for (var address : result)
                        subnets.add(parseSubnet(address));

                    Log.info("Fetched @ datacenter subnets.", subnets.size);
                });
    }

    /**
     * @return true if this address is suspicious, false otherwise
     **/
    public static boolean checkAddress(String address) {
        var ip = parseSubnet(address).ip;

        for (var subnet : subnets)
            if ((ip & subnet.mask) == subnet.ip) return true;

        return false;
    }

    @SneakyThrows(UnknownHostException.class)
    private static Subnet parseSubnet(String address) {
        var parts = address.split("/");
        if (parts.length > 2)
            throw new IllegalArgumentException("Invalid IP address: " + address);

        int ip = 0;
        int mask = -1;

        for (var token : InetAddress.getByName(parts[0]).getAddress()) {
            ip = (ip << 8) + (token & 0xFF);
        }

        if (parts.length == 2) {
            mask = Integer.parseInt(parts[1]);
            if (mask > 32)
                throw new IllegalArgumentException("Invalid IP address: " + address);

            mask = 0xFFFFFFFF << (32 - mask);
        }

        return new Subnet(ip, mask);
    }

    private record Subnet(int ip, int mask) {
    }
}