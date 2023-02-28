package useful;

import arc.ApplicationListener;
import arc.Events;
import arc.func.Cons;
import fr.xpdustry.javelin.JavelinEvent;
import fr.xpdustry.javelin.JavelinSocket;
import fr.xpdustry.javelin.JavelinSocket.Status;

import java.net.URI;
import java.util.concurrent.TimeUnit;

import static arc.Core.app;

public class Socket {
    public static JavelinSocket socket;
    public static boolean server;

    public static void connect(int port, boolean server) {
        connect(port, 1, server);
    }

    public static void connect(int port, int workers, boolean server) {
        connect(server ?
                JavelinSocket.server(port, workers, true, (username, password) -> false) :
                JavelinSocket.client(URI.create("ws://localhost:" + port), workers), server);
    }

    public static void connect(JavelinSocket socket, boolean server) {
        Socket.socket = socket;
        Socket.server = server;

        app.addListener(new ApplicationListener() {
            @Override
            public void init() {
                socket.start().orTimeout(15L, TimeUnit.SECONDS).join();;
            }

            @Override
            public void dispose() {
                socket.close().orTimeout(15L, TimeUnit.SECONDS).join();
            }

            @Override
            public void update() {
                if (socket.getStatus() == Status.CLOSED)
                    socket.restart();
            }
        });
    }

    // Subscribe to an event on client
    public static <T extends JavelinEvent> void onClient(Class<T> type, Cons<T> listener) {
        if (server)
            Events.on(type, listener);
        else
            socket.subscribe(type, listener::get);
    }

    // Subscribe to an event on server
    public static <T extends JavelinEvent> void onServer(Class<T> type, Cons<T> listener) {
        if (!server)
            Events.on(type, listener);
        else
            socket.subscribe(type, listener::get);
    }

    // Send an event from client to server
    public static <T extends JavelinEvent> void sendClient(T event) {
        if (server)
            Events.fire(event);
        else
            socket.sendEvent(event);
    }

    // Send an event from server to clients
    public static <T extends JavelinEvent> void sendServer(T event) {
        if (!server)
            Events.fire(event);
        else
            socket.sendEvent(event);
    }
}