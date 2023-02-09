package useful;

import arc.graphics.Color;
import arc.math.Mathf;
import arc.math.geom.Position;
import arc.util.Tmp;
import mindustry.entities.Effect;
import mindustry.gen.Call;

public class Effects {

    // region at

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
    // region at with data

    public static void at(Effect effect, Position position, Object data) {
        Call.effect(effect, position.getX(), position.getY(), Mathf.random(360f), Tmp.c1.rand(), data);
    }

    public static void at(Effect effect, Position position, Color color, Object data) {
        Call.effect(effect, position.getX(), position.getY(), Mathf.random(360f), color, data);
    }

    public static void at(Effect effect, Position position, float rotation, Object data) {
        Call.effect(effect, position.getX(), position.getY(), rotation, Tmp.c1.rand(), data);
    }

    public static void at(Effect effect, Position position, float rotation, Color color, Object data) {
        Call.effect(effect, position.getX(), position.getY(), rotation, color, data);
    }

    // endregion
}