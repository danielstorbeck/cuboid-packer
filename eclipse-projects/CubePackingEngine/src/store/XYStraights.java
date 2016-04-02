package store;

import java.util.List;
import java.util.ArrayList;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Comparator;
import box.Module.Axis;

public class XYStraights {
	
	// Sortierte Mengen (unterschiedlicher) Geraden parallel zur x- bzw. y-Achse.
	// Sortierkriterien sind in den beiden Comparator-Klassen
	// weiter unten definiert.
    SortedSet<Straight> xs = new TreeSet<Straight>(new XStraightComparator());
    SortedSet<Straight> ys = new TreeSet<Straight>(new YStraightComparator());
    
    public void add(Straight s) {
        if (s.a == Axis.X) xs.add(s);
        else if (s.a == Axis.Y) ys.add(s);
    }
    
    // Erzeugen aller Flächen, die sich durch Schneiden der
    // enthaltenen Geraden und den Rändern der Grundfläche
    // innerhalb dieser Grundfläche bilden lassen.
    public List<Face> getAllFaces() {
    	// Sammlung gebildeter Flächen.
        List<Face> lf = new ArrayList<Face>();
        // Die sortierten Geradenmengen als Listen zum Iterieren.
        List<Straight> lx = new ArrayList<Straight>(xs);
        List<Straight> ly = new ArrayList<Straight>(ys);
        // Anzahl von x- und y-Geraden.
        int nx = lx.size();
        int ny = ly.size();
        // Anzahl von x- und y-Geraden minus 1.
        int px = nx - 1;
        int py = ny - 1;
        // Indices, die auf zwei x-Geraden zeigen, zwischen denen
        // Flächen gebildet werden.
        int ix = 0;
        int jx = 0;
        // Indices, die auf zwei y-Geraden zeigen, usw.
        int iy = 0;
        int jy = 0;
        // Zwischen allen Paaren verschiedener x-Geraden werden alle
        // Kombinationen zweier verschiedener y-Geraden erzeugt
        // und die Flächen, die dabei abgegrenzt werden, gesammelt.
        while (ix <= px) {
            jx = ix + 1;
            while (jx < nx) {
                iy = 0;
                while (iy <= py) {
                    jy = iy + 1;
                    while (jy < ny) {
                    	// Beschaffen der Geraden.
                        Straight sx1 = lx.get(ix);
                        Straight sx2 = lx.get(jx);
                        Straight sy1 = ly.get(iy);
                        Straight sy2 = ly.get(jy);
                        // Finden zweier Schnittpunkte,
                        // die die Fläche aufspannen.
                        Point p1 = sx1.intersectionWith(sy1);
                        Point p2 = sx2.intersectionWith(sy2);
                        lf.add(new Face(p1, p2));
                        jy = jy + 1;
                    }
                    iy = iy + 1;
                }
                jx = jx + 1;
            }
            ix = ix + 1;
        }
        return lf;
    }
}

//Annahme, dass beide Geraden parallel zur x-Achse sind
//und auf derselben x-y-Ebene liegen.
class XStraightComparator implements Comparator<Straight> {
    public int compare(Straight s1, Straight s2) {
        if (s1.co[1] == s2.co[1]) return 0;
        else if (s1.co[1] < s2.co[1]) return -1;
        else return 1;
    }
}

// Annahme, dass beide Geraden parallel zur y-Achse sind
// und auf derselben x-y-Ebene liegen.
class YStraightComparator implements Comparator<Straight> {
    public int compare(Straight s1, Straight s2) {
        if (s1.co[0] == s2.co[0]) return 0;
        else if (s1.co[0] < s2.co[0]) return -1;
        else return 1;
    }
}
