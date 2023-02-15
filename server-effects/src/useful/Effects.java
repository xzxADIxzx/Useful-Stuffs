package useful;

import arc.func.*;
import arc.graphics.Color;
import arc.math.Angles;
import arc.math.Mathf;
import arc.math.geom.Position;
import arc.util.*;
import arc.util.Timer.Task;
import mindustry.entities.Effect;
import mindustry.gen.Call;
import mindustry.gen.Player;

public class Effects {

    // region at

    public static void at(Effect effect, float x, float y) {
        Call.effect(effect, x, y, Mathf.random(360f), Tmp.c1.rand());
    }

    public static void at(Effect effect, float x, float y, Color color) {
        Call.effect(effect, x, y, Mathf.random(360f), color);
    }

    public static void at(Effect effect, float x, float y, float rotation) {
        Call.effect(effect, x, y, rotation, Tmp.c1.rand());
    }

    public static void at(Effect effect, float x, float y, float rotation, Color color) {
        Call.effect(effect, x, y, rotation, color);
    }

    public static void at(Effect effect, Position position) {
        Call.effect(effect, position.getX(), position.getY(), Mathf.random(360f), Tmp.c1.rand());
    }

    public static void at(Effect effect, Position position, Color color) {
        Call.effect(effect, position.getX(), position.getY(), Mathf.random(360f), color);
    }

    public static void at(Effect effect, Position position, float rotation) {
        Call.effect(effect, position.getX(), position.getY(), rotation, Tmp.c1.rand());
    }

    public static void at(Effect effect, Position position, float rotation, Color color) {
        Call.effect(effect, position.getX(), position.getY(), rotation, color);
    }

    // endregion
    // region at (with data)

    public static void atData(Effect effect, float x, float y, Object data) {
        Call.effect(effect, x, y, Mathf.random(360f), Tmp.c1.rand(), data);
    }

    public static void atData(Effect effect, float x, float y, Color color, Object data) {
        Call.effect(effect, x, y, Mathf.random(360f), color, data);
    }

    public static void atData(Effect effect, float x, float y, float rotation, Object data) {
        Call.effect(effect, x, y, rotation, Tmp.c1.rand(), data);
    }

    public static void atData(Effect effect, float x, float y, float rotation, Color color, Object data) {
        Call.effect(effect, x, y, rotation, color, data);
    }

    public static void atData(Effect effect, Position position, Object data) {
        Call.effect(effect, position.getX(), position.getY(), Mathf.random(360f), Tmp.c1.rand(), data);
    }

    public static void atData(Effect effect, Position position, Color color, Object data) {
        Call.effect(effect, position.getX(), position.getY(), Mathf.random(360f), color, data);
    }

    public static void atData(Effect effect, Position position, float rotation, Object data) {
        Call.effect(effect, position.getX(), position.getY(), rotation, Tmp.c1.rand(), data);
    }

    public static void atData(Effect effect, Position position, float rotation, Color color, Object data) {
        Call.effect(effect, position.getX(), position.getY(), rotation, color, data);
    }

    // endregion
    // region circle

    public static void circle(Effect effect, float x, float y, float step, float radius) {
        Utils.circle(step, radius, (cx, cy) -> at(effect, x + cx, y + cy));
    }

    public static void circle(Effect effect, float x, float y, float step, float radius, Color color) {
        Utils.circle(step, radius, (cx, cy) -> at(effect, x + cx, y + cy, color));
    }

    public static void circle(Effect effect, float x, float y, float step, float radius, float rotation) {
        Utils.circle(step, radius, (cx, cy) -> at(effect, x + cx, y + cy, rotation));
    }

    public static void circle(Effect effect, float x, float y, float step, float radius, float rotation, Color color) {
        Utils.circle(step, radius, (cx, cy) -> at(effect, x + cx, y + cy, rotation, color));
    }

    public static void circle(Effect effect, Position position, float step, float radius) {
        Utils.circle(step, radius, (cx, cy) -> at(effect, position.getX() + cx, position.getY() + cy));
    }

    public static void circle(Effect effect, Position position, float step, float radius, Color color) {
        Utils.circle(step, radius, (cx, cy) -> at(effect, position.getX() + cx, position.getY() + cy, color));
    }

    public static void circle(Effect effect, Position position, float step, float radius, float rotation) {
        Utils.circle(step, radius, (cx, cy) -> at(effect, position.getX() + cx, position.getY() + cy, rotation));
    }

    public static void circle(Effect effect, Position position, float step, float radius, float rotation, Color color) {
        Utils.circle(step, radius, (cx, cy) -> at(effect, position.getX() + cx, position.getY() + cy, rotation, color));
    }

    // endregion
    // region circle (with data)

    public static void circleData(Effect effect, float x, float y, float step, float radius, Object data) {
        Utils.circle(step, radius, (cx, cy) -> atData(effect, x + cx, y + cy, data));
    }

    public static void circleData(Effect effect, float x, float y, float step, float radius, Color color, Object data) {
        Utils.circle(step, radius, (cx, cy) -> atData(effect, x + cx, y + cy, color, data));
    }

    public static void circleData(Effect effect, float x, float y, float step, float radius, float rotation, Object data) {
        Utils.circle(step, radius, (cx, cy) -> atData(effect, x + cx, y + cy, rotation, data));
    }

    public static void circleData(Effect effect, float x, float y, float step, float radius, float rotation, Color color, Object data) {
        Utils.circle(step, radius, (cx, cy) -> atData(effect, x + cx, y + cy, rotation, color, data));
    }

    public static void circleData(Effect effect, Position position, float step, float radius, Object data) {
        Utils.circle(step, radius, (cx, cy) -> atData(effect, position.getX() + cx, position.getY() + cy, data));
    }

    public static void circleData(Effect effect, Position position, float step, float radius, Color color, Object data) {
        Utils.circle(step, radius, (cx, cy) -> atData(effect, position.getX() + cx, position.getY() + cy, color, data));
    }

    public static void circleData(Effect effect, Position position, float step, float radius, float rotation, Object data) {
        Utils.circle(step, radius, (cx, cy) -> atData(effect, position.getX() + cx, position.getY() + cy, rotation, data));
    }

    public static void circleData(Effect effect, Position position, float step, float radius, float rotation, Color color, Object data) {
        Utils.circle(step, radius, (cx, cy) -> atData(effect, position.getX() + cx, position.getY() + cy, rotation, color, data));
    }

    // endregion
    // region poly

    public static void poly(Effect effect, float x, float y, int sides, float step, float radius) {
        Utils.poly(sides, step, radius, (cx, cy) -> at(effect, x + cx, y + cy, Angles.angle(cx, cy)));
    }

    public static void poly(Effect effect, float x, float y, int sides, float step, float radius, Color color) {
        Utils.poly(sides, step, radius, (cx, cy) -> at(effect, x + cx, y + cy, Angles.angle(cx, cy), color));
    }

    public static void poly(Effect effect, float x, float y, int sides, float step, float radius, float rotation) {
        Utils.poly(sides, step, radius, (cx, cy) -> at(effect, x + cx, y + cy, rotation));
    }

    public static void poly(Effect effect, float x, float y, int sides, float step, float radius, float rotation, Color color) {
        Utils.poly(sides, step, radius, (cx, cy) -> at(effect, x + cx, y + cy, rotation, color));
    }

    public static void poly(Effect effect, Position position, int sides, float step, float radius) {
        Utils.poly(sides, step, radius, (cx, cy) -> at(effect, position.getX() + cx, position.getY() + cy, Angles.angle(cx, cy)));
    }

    public static void poly(Effect effect, Position position, int sides, float step, float radius, Color color) {
        Utils.poly(sides, step, radius, (cx, cy) -> at(effect, position.getX() + cx, position.getY() + cy, Angles.angle(cx, cy), color));
    }

    public static void poly(Effect effect, Position position, int sides, float step, float radius, float rotation) {
        Utils.poly(sides, step, radius, (cx, cy) -> at(effect, position.getX() + cx, position.getY() + cy, rotation));
    }

    public static void poly(Effect effect, Position position, int sides, float step, float radius, float rotation, Color color) {
        Utils.poly(sides, step, radius, (cx, cy) -> at(effect, position.getX() + cx, position.getY() + cy, rotation, color));
    }

    public static void rotatedPoly(Effect effect, float x, float y, int sides, float step, float angle, float radius) {
        Utils.poly(sides, step, angle, radius, (cx, cy) -> at(effect, x + cx, y + cy, Angles.angle(cx, cy)));
    }

    public static void rotatedPoly(Effect effect, float x, float y, int sides, float step, float angle, float radius, Color color) {
        Utils.poly(sides, step, angle, radius, (cx, cy) -> at(effect, x + cx, y + cy, Angles.angle(cx, cy), color));
    }

    public static void rotatedPoly(Effect effect, float x, float y, int sides, float step, float angle, float radius, float rotation) {
        Utils.poly(sides, step, angle, radius, (cx, cy) -> at(effect, x + cx, y + cy, rotation));
    }

    public static void rotatedPoly(Effect effect, float x, float y, int sides, float step, float angle, float radius, float rotation, Color color) {
        Utils.poly(sides, step, angle, radius, (cx, cy) -> at(effect, x + cx, y + cy, rotation, color));
    }

    public static void rotatedPoly(Effect effect, Position position, int sides, float step, float angle, float radius) {
        Utils.poly(sides, step, angle, radius, (cx, cy) -> at(effect, position.getX() + cx, position.getY() + cy, Angles.angle(cx, cy)));
    }

    public static void rotatedPoly(Effect effect, Position position, int sides, float step, float angle, float radius, Color color) {
        Utils.poly(sides, step, angle, radius, (cx, cy) -> at(effect, position.getX() + cx, position.getY() + cy, Angles.angle(cx, cy), color));
    }

    public static void rotatedPoly(Effect effect, Position position, int sides, float step, float angle, float radius, float rotation) {
        Utils.poly(sides, step, angle, radius, (cx, cy) -> at(effect, position.getX() + cx, position.getY() + cy, rotation));
    }

    public static void rotatedPoly(Effect effect, Position position, int sides, float step, float angle, float radius, float rotation, Color color) {
        Utils.poly(sides, step, angle, radius, (cx, cy) -> at(effect, position.getX() + cx, position.getY() + cy, rotation, color));
    }

    // endregion
    // region poly (with data)

    public static void polyData(Effect effect, float x, float y, int sides, float step, float radius, Object data) {
        Utils.poly(sides, step, radius, (cx, cy) -> atData(effect, x + cx, y + cy, Angles.angle(cx, cy), data));
    }

    public static void polyData(Effect effect, float x, float y, int sides, float step, float radius, Color color, Object data) {
        Utils.poly(sides, step, radius, (cx, cy) -> atData(effect, x + cx, y + cy, Angles.angle(cx, cy), color, data));
    }

    public static void polyData(Effect effect, float x, float y, int sides, float step, float radius, float rotation, Object data) {
        Utils.poly(sides, step, radius, (cx, cy) -> atData(effect, x + cx, y + cy, rotation, data));
    }

    public static void polyData(Effect effect, float x, float y, int sides, float step, float radius, float rotation, Color color, Object data) {
        Utils.poly(sides, step, radius, (cx, cy) -> atData(effect, x + cx, y + cy, rotation, color, data));
    }

    public static void polyData(Effect effect, Position position, int sides, float step, float radius, Object data) {
        Utils.poly(sides, step, radius, (cx, cy) -> atData(effect, position.getX() + cx, position.getY() + cy, Angles.angle(cx, cy), data));
    }

    public static void polyData(Effect effect, Position position, int sides, float step, float radius, Color color, Object data) {
        Utils.poly(sides, step, radius, (cx, cy) -> atData(effect, position.getX() + cx, position.getY() + cy, Angles.angle(cx, cy), color, data));
    }

    public static void polyData(Effect effect, Position position, int sides, float step, float radius, float rotation, Object data) {
        Utils.poly(sides, step, radius, (cx, cy) -> atData(effect, position.getX() + cx, position.getY() + cy, rotation, data));
    }

    public static void polyData(Effect effect, Position position, int sides, float step, float radius, float rotation, Color color, Object data) {
        Utils.poly(sides, step, radius, (cx, cy) -> atData(effect, position.getX() + cx, position.getY() + cy, rotation, color, data));
    }

    public static void rotatedPolyData(Effect effect, float x, float y, int sides, float step, float angle, float radius, Object data) {
        Utils.poly(sides, step, angle, radius, (cx, cy) -> atData(effect, x + cx, y + cy, Angles.angle(cx, cy), data));
    }

    public static void rotatedPolyData(Effect effect, float x, float y, int sides, float step, float angle, float radius, Color color, Object data) {
        Utils.poly(sides, step, angle, radius, (cx, cy) -> atData(effect, x + cx, y + cy, Angles.angle(cx, cy), color, data));
    }

    public static void rotatedPolyData(Effect effect, float x, float y, int sides, float step, float angle, float radius, float rotation, Object data) {
        Utils.poly(sides, step, angle, radius, (cx, cy) -> atData(effect, x + cx, y + cy, rotation, data));
    }

    public static void rotatedPolyData(Effect effect, float x, float y, int sides, float step, float angle, float radius, float rotation, Color color, Object data) {
        Utils.poly(sides, step, angle, radius, (cx, cy) -> atData(effect, x + cx, y + cy, rotation, color, data));
    }

    public static void rotatedPolyData(Effect effect, Position position, int sides, float step, float angle, float radius, Object data) {
        Utils.poly(sides, step, angle, radius, (cx, cy) -> atData(effect, position.getX() + cx, position.getY() + cy, Angles.angle(cx, cy), data));
    }

    public static void rotatedPolyData(Effect effect, Position position, int sides, float step, float angle, float radius, Color color, Object data) {
        Utils.poly(sides, step, angle, radius, (cx, cy) -> atData(effect, position.getX() + cx, position.getY() + cy, Angles.angle(cx, cy), color, data));
    }

    public static void rotatedPolyData(Effect effect, Position position, int sides, float step, float angle, float radius, float rotation, Object data) {
        Utils.poly(sides, step, angle, radius, (cx, cy) -> atData(effect, position.getX() + cx, position.getY() + cy, rotation, data));
    }

    public static void rotatedPolyData(Effect effect, Position position, int sides, float step, float angle, float radius, float rotation, Color color, Object data) {
        Utils.poly(sides, step, angle, radius, (cx, cy) -> atData(effect, position.getX() + cx, position.getY() + cy, rotation, color, data));
    }

    // endregion
    // region schedule

    public static void schedule(Player player, float interval, int repeat, Runnable runnable) {
        schedule(player, 0f, interval, repeat, runnable);
    }

    public static void schedule(Player player, float delay, float interval, int repeat, Runnable runnable) {
        Timer.schedule(new Task() {
            @Override
            public void run() {
                if (player == null || !player.con.isConnected()) {
                    cancel();
                    return;
                }

                runnable.run();
            }
        }, delay, interval, repeat);
    }

    public static void schedule(Player player, float interval, int repeat, Floatc cons) {
        schedule(player, 0f, interval, repeat, cons);
    }

    public static void schedule(Player player, float delay, float interval, int repeat, Floatc cons) {
        Timer.schedule(new Task() {
            @Override
            public void run() {
                if (player == null || !player.con.isConnected()) {
                    cancel();
                    return;
                }

                cons.get(Time.time);
            }
        }, delay, interval, repeat);
    }

    public static void schedule(Player player, float interval, int repeat, Cons2<Player, Float> cons) {
        schedule(player, 0f, interval, repeat, cons);
    }

    public static void schedule(Player player, float delay, float interval, int repeat, Cons2<Player, Float> cons) {
        Timer.schedule(new Task() {
            @Override
            public void run() {
                if (player == null || !player.con.isConnected()) {
                    cancel();
                    return;
                }

                cons.get(player, Time.time);
            }
        }, delay, interval, repeat);
    }

    // endregion
    // region sequence

    public static void sequence(float delay, Runnable... values) {
        for (int i = 0; i < values.length; i++) {
            var runnable = values[i];
            Time.run(delay * i, runnable);
        }
    }

    public static void sequence(float delay, Floatc... values) {
        for (int i = 0; i < values.length; i++) {
            var cons = values[i];
            Time.run(delay * i, () -> cons.get(Time.time));
        }
    }

    // endregion
}