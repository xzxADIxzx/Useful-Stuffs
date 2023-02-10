package useful;

import arc.func.Floatc2;
import arc.math.geom.Geometry;

public class Utils {

    public static void poly(int sides, float step, float radius, Floatc2 cons) {
        var poly = Geometry.regPoly(sides, radius);

        for (int i = 0; i < sides - 1; i++)
            Geometry.iterateLine(0f, poly[i * 2], poly[i * 2 + 1], poly[i * 2 + 2], poly[i * 2 + 3], step, cons);

        Geometry.iterateLine(0f, poly[poly.length - 2], poly[poly.length - 1], poly[0], poly[1], step, cons);
    }
}