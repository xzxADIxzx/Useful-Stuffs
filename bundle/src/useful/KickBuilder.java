package useful;

import mindustry.net.NetConnection;

public class KickBuilder {

    public final NetConnection con;
    public final String locale;

    public final StringBuilder reason = new StringBuilder();

    public KickBuilder(NetConnection con, String locale) {
        this.con = con;
        this.locale = locale;
    }

    public KickBuilder add(String key) {
        this.reason.append(Bundle.get(key, locale));
        return this;
    }

    public KickBuilder add(String key, Object... values) {
        this.reason.append(Bundle.format(key, locale, values));
        return this;
    }

    public KickBuilder add(boolean value, String key) {
        if (value)
            this.reason.append(Bundle.get(key, locale));
        return this;
    }

    public KickBuilder add(boolean value, String key, Object... values) {
        if (value)
            this.reason.append(Bundle.format(key, locale, values));
        return this;
    }

    public void kick() {
        con.kick(reason.toString(), 0L);
    }

    public void kick(long duration) {
        con.kick(reason.toString(), duration);
    }
}