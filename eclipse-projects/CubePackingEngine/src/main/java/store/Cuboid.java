package store;

import box.OrderedDimensions;
import box.RotatingBox;
import java.util.Set;
import java.util.List;

/**
 * Die Eckpunkte und Flächen eines Quaders.
 * Stellt einen Freiraum im Kontainer dar,
 * in den ein rotierbarer Quader gesteckt werden soll.
 * Hat eine Position innerhalb des Kontainers, d.h. die
 * Punkte, die ihn aufspannen sind absolute Koordinaten
 * ausgehend vom Kontainer.
 */
public class Cuboid extends OrderedDimensions {

    // Punkte, die am nächsten bzw. am weitesten vom Ursprung
    // weg liegen bzw. irgendwo dazwischen. Fünf reichen, um alle
    // Flächen zu konstruieren.
    Point min, b, c, d, max;

    // Die Flächen.
    Face f1, f2, f3, f4, f5, f6;

    // Factory-Methoden, die die Benutzung des Konstruktors vereinfacht.
    // Denn die Dimensionen werden aus den Punkten abgeleitet.
    static Cuboid createWith(Point p1, Point p2) {
        Point min = Point.createMinimumFrom(p1, p2);
        Point max = Point.createMaximumFrom(p1, p2);
        Cuboid c = new Cuboid(new float[]{
                    max.co[0] - min.co[0],
                    max.co[1] - min.co[1],
                    max.co[2] - min.co[2]},
                    min, max);
        return c;
    }

    // Konstruktion mit Dimensionen und zwei Punkten.
    Cuboid(float[] dm, Point min, Point max) {
        // Dimensionen
        super(dm);
        // Zwei Punkte, die den Quader aufspannen.
        this.min = min;
        this.max = max;
        // Weitere Punkte, mit denen alle Flächen erzeugt werden können.
        b = new Point(new float[] {min.co[0], max.co[1], max.co[2]});
        c = new Point(new float[] {max.co[0], max.co[1], min.co[2]});
        d = new Point(new float[] {max.co[0], min.co[1], max.co[2]});
        // Die Flächen.
        f1 = new Face(min, b);
        f2 = new Face(min, c); // unten
        f3 = new Face(min, d);
        f4 = new Face(b, c);
        f5 = new Face(b, d); // oben
        f6 = new Face(c, d);
    }

    public OrderedDimensions getOrdDimsBox() {
        return new OrderedDimensions(origDims);
    }

    // Gibt die z-Koordinaten, an denen die beiden x-y-Flächen liegen,
    // in eine übergebene Sammlung.
    public void addZValuesTo(Set<Float> sf) {
        sf.add(new Float(min.co[2]));
        sf.add(new Float(max.co[2]));
    }

    // f2 ist unten.
    public Face getBottomFace() {
        return new Face(f2.min, f2.max);
    }

    // Falls die obere oder untere Fläche den übergebenen z-Wert hat,
    // in eine übergebene Sammlung geben.
    public void addZFaceOnLevelTo(float z, List<Face> lf) {
        if (f2.min.co[2] == z) lf.add(new Face(f2.min, f2.max)); // unten
        if (f5.min.co[2] == z) lf.add(new Face(f5.min, f5.max)); // oben
    }
    
    // Gehört der Punkt zum Quader?
    public boolean hasPoint(Point p) {
        return (p.co[0] >= min.co[0] && p.co[0] <= max.co[0])
                && (p.co[1] >= min.co[1] && p.co[1] <= max.co[1])
                && (p.co[2] >= min.co[2] && p.co[2] <= max.co[2]);
    }

    // Liegt der Punkt auf einer der Flächen?
    public boolean hasPointOnFace(Point p) {
        if (p.co[0] == min.co[0] || p.co[0] == max.co[0]) return true;
        else if (p.co[1] == min.co[1] || p.co[1] == max.co[1]) return true;
        else if (p.co[2] == min.co[2] || p.co[2] == max.co[2]) return true;
        else return false;
    }

    // Liegt der Punkt auf einer der Kanten?
    public boolean hasPointOnEdge(Point p) {
        // f2-5 enthalten alle Kanten.
        return f2.hasPointOnEdge(p)
                || f3.hasPointOnEdge(p)
                || f4.hasPointOnEdge(p)
                || f5.hasPointOnEdge(p);
    }

    // Ist der Punkt einer der Eckpunkte?
    public boolean hasPointOnVertex(Point p) {
        // f1 and f6 are coplanar and contain all vertices.
        return f1.hasPointOnVertex(p)
                || f6.hasPointOnVertex(p);
    }

    // Schneiden sich Quader und übergebene Fläche?
    // Ausgenommen ist die obere Fläche.
    public boolean blocks(Face f) {
        float fz = f.getZLevel();
        if (fz >= min.co[2] && fz < max.co[2] && f2.intersectsXYWith(f)) {
            return true;
        } else {
            return false;
        }
    }

    // Passt der übergebene rotierbare Quader in diesen Freiraum?
    // Vergleich der Längen der raumdiagonalen Vektoren, weil der
    // übergebene Quader positionslos ist.
    public boolean canPlaceBox(RotatingBox rb) {
        float[] bd = rb.getDiagonal();
        return bd[0] <= origDims[0] && bd[1] <= origDims[1] && bd[2] <= origDims[2];
    }

    // Passt der übergebene Quader in diesen Freiraum?
    // Der Quader wird gedreht und gewendet, und sobald
    // er passt, als situierter Quader zurück gegeben.
    // Wenn er nicht passt, null zurück geben.
    public SituatedBox placeBox(OrderedDimensions od) {
        RotatingBox rb = new RotatingBox(od);
        if (canPlaceBox(rb)) return new SituatedBox(rb.getDiagonal(), min.co);
        rb.invertYZ();
        if (canPlaceBox(rb)) return new SituatedBox(rb.getDiagonal(), min.co);
        rb.invertXY();
        if (canPlaceBox(rb)) return new SituatedBox(rb.getDiagonal(), min.co);
        rb.invertYZ();
        if (canPlaceBox(rb)) return new SituatedBox(rb.getDiagonal(), min.co);
        rb.invertXY();
        if (canPlaceBox(rb)) return new SituatedBox(rb.getDiagonal(), min.co);
        rb.invertYZ();
        if (canPlaceBox(rb)) return new SituatedBox(rb.getDiagonal(), min.co);
        return null;
    }

    // Ist der übergebene Freiraum in diesem enthalten?
    // Es geht um räumliches, positionsbezogenes Enthaltensein.
    public boolean contains(Cuboid c2) {
        return c2.min.co[0] >= min.co[0]
                && c2.min.co[1] >= min.co[1]
                && c2.min.co[2] >= min.co[2]
                && c2.max.co[0] <= max.co[0]
                && c2.max.co[1] <= max.co[1]
                && c2.max.co[2] <= max.co[2];
    }

    // Schneidet sich dieser Freiraum mit dem übergebenen.
    public boolean intersectsWith(Cuboid c2) {
        // Eigene Koordinaten.
        float[] mm1 = new float[]{
            min.co[0], max.co[0],
            min.co[1], max.co[1],
            min.co[2], max.co[2]
        };
        // Fremde Koordinaten.
        float[] mm2 = new float[]{
            c2.min.co[0], c2.max.co[0],
            c2.min.co[1], c2.max.co[1],
            c2.min.co[2], c2.max.co[2]
        };
        // Zeiger in die Koordinaten-Arrays.
        // g steht für greater, l für lesser.
        float ig0, /*ig1, il0,*/ il1;
        // Iterieren über x-, y- und z-Koordinaten.
        for (int i = 2; i <= 6; i = i + 2) {
            // Fall, dass eigene maximale x-, y- bzw. z-Koordinate
            // größer/gleich der fremden ist.
            if (mm1[i - 1] >= mm2[i - 1]) {
                ig0 = mm1[i - 2];
                //ig1 = mm1[i - 1];
                //il0 = mm2[i - 2];
                il1 = mm2[i - 1];
            }
            // Fall, dass eigene maximale x-, y- bzw. z-Koordinate
            // kleiner als die fremde ist.
            else {
                //il0 = mm1[i - 2];
                il1 = mm1[i - 1];
                ig0 = mm2[i - 2];
                //ig1 = mm2[i - 1];
            }
            // Wenn sich die Quader schneiden, dann müssen sich ihre
            // ihre x-Kanten überlappen und ihre y-Kanten überlappen
            // und ihre z-Kanten überlappen. Hinreichend für nicht Schneiden
            // ist also bereits, wenn sich eine Sorte davon nicht überlappt.
            // Wenn z.B. die größte eigene x-Koordinate größer als die größte
            // fremde ist und die größte fremde kleiner-geich der kleinsten
            // eigenen ist, dann überlappen sich die x-Kanten nicht und
            // somit die ganzen Quader nicht.
            if (il1 <= ig0) {
                return false;
            }
        }
        // Ansonsten doch.
        return true;
    }
}
