package useful;

import arc.ApplicationListener;
import arc.Core;
import arc.func.Cons;
import arc.util.Timer;
import fr.xpdustry.javelin.JavelinEvent;
import fr.xpdustry.javelin.JavelinSocket;
import fr.xpdustry.javelin.JavelinSocket.Status;

import java.net.URI;
import java.util.concurrent.TimeUnit;

public class Socket {
    public static JavelinSocketConfig config;
    public static JavelinSocket socket;

    public static void load(JavelinSocketConfig config) {
        Socket.config = config;
        Socket.socket = config.server ?
                JavelinSocket.server(config.port, config.workers, true, ((username, password) -> false)) :
                JavelinSocket.client(URI.create("ws://localhost:" + config.port), config.workers);

        Core.app.addListener(new ApplicationListener() {
            @Override
            public void init() {
                Socket.connect();
            }

            @Override
            public void dispose() {
                Socket.close();
            }
        });

        if (!config.autoReconnect) return;

        Timer.schedule(() -> {
            if (!connected())
                reconnect();
        }, config.timeout, config.timeout);
    }

    // region connect

    public static void connect() {
        socket.start().orTimeout(config.timeout, TimeUnit.SECONDS).join();
    }

    public static void reconnect() {
        socket.restart().orTimeout(config.timeout, TimeUnit.SECONDS).join();
    }

    public static void close() {
        socket.close().orTimeout(config.timeout, TimeUnit.SECONDS).join();
    }

    public static boolean connected() {
        return socket.getStatus() == Status.OPEN;
    }

    // endregion
    // region events

    public static <T extends SocketEvent> void subscribe(Class<T> type, Cons<T> listener) {
        socket.subscribe(type, listener::get);
    }

    public static <T extends SocketEvent> void send(T event) {
        socket.sendEvent(event);
    }

    // endregion

    public static class JavelinSocketConfig {
        public boolean server;
        public boolean autoReconnect;

        public int port;
        public int workers;
        public int timeout;
    }

    public static class SocketEvent implements JavelinEvent {
        public boolean server = config.server;
    }
}