package useful;

import arc.func.Floatc2;
import arc.math.Mathf;
import arc.math.geom.*;
import arc.util.Tmp;

public class Utils {

    public static void circle(float step, float radius, Floatc2 cons) {
        circle(360f, step, radius, cons);
    }

    public static void circle(float degrees, float step, float radius, Floatc2 cons) {
        for (float deg = 0f; deg < degrees; deg += step)
            cons.get(Mathf.cosDeg(deg) * radius, Mathf.sinDeg(deg) * radius);
    }

    public static void poly(int sides, float step, float radius, Floatc2 cons) {
        poly(sides, step, 0f, radius, cons);
    }

    public static void poly(int sides, float step, float angle, float radius, Floatc2 cons) {
        Tmp.v1.set(1, 1).setLength(radius);
        Tmp.v2.set(1, 1).setLength(radius);

        for(int i = 0; i < sides; i++) {
            Tmp.v1.setAngle(360f / sides * i + 90f).rotate(angle);
            Tmp.v2.setAngle(360f / sides * (i + 1) + 90f).rotate(angle);

            Geometry.iterateLine(0f, Tmp.v1.x, Tmp.v1.y, Tmp.v2.x, Tmp.v2.y, step, cons);
        }
    }
}