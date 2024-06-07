package store;

import box.Module.Axis;

/**
 * Gerade definiert durch einen Punkt und eine Achse,
 * nämlich die x- oder y-Achse, denn interessant sind
 * nur Geraden, die in einer x-y-Ebene liegen.
 */
public class Straight extends Point {

    Axis a;
    
    // Konstruktion mit Punkt und Achse.
    public Straight(float[] co2, Axis a2) {
        super(co2);
        a = a2;
    }

    // Vergleich mit einer Geraden auf derselben Ebene.
    public boolean isEqualTo(Straight s) {
        if (a != s.a) return false;
        if (a == Axis.X && co[1] == s.co[1]) return true;
        if (a == Axis.Y && co[0] == s.co[0]) return true;
        return false;
    }

    // Punkt, in dem sich diese mit einer anderen
    // Geraden schneidet.
    public Point intersectionWith(Straight s) {
        if (a == Axis.X && s.a == Axis.Y)
            return new Point(new float[] {s.co[0], co[1], co[2]});
        if (a == Axis.Y && s.a == Axis.X)
            return new Point(new float[] {co[0], s.co[1], co[2]});
        return null;
    }
}
