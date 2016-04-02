package store;

import box.Module.Axis;

/**
 * Fl�che eines Quaders.
 */
public class Face {
	
	// Senkrechte auf der Fl�che.
    Axis normale;
    // Indices in die Punktkoordinatenarrays.
    // Index ia ist immer kleiner als Index ib.
    // Index icnst zeigt immer auf einen konstanten Wert.
    int ia, ib, icnst;
    // Zwei Punkte, die die Fl�che definieren.
    Point min, max;
    
    // Konstruktion mit zwei Punkten.
    public Face(Point p1, Point p2) {
    	// Ist die x-Koordinate der Punkte gleich,
        if (p1.co[0] == p2.co[0]) {
        	// dann ist die x-Achse die Normale der Fl�che.
            normale = Axis.X;
            // icnst zeigt auf die x-Koordinate.
            icnst = 0;
            // ia und ib auf die anderen beiden Koordinaten,
            // wobei ia < ib f�r die Indices gilt,
            // nicht f�r die Werte, auf die sie zeigen.
            ia = 1;
            ib = 2;
        } else if (p1.co[1] == p2.co[1]) {
            normale = Axis.Y;
            icnst = 1;
            ia = 0;
            ib = 2;
        } else if (p1.co[2] == p2.co[2]) {
            normale = Axis.Z;
            icnst = 2;
            ia = 0;
            ib = 1;
        }
        // Zwei Punkte finden, die die Fl�che definieren,
        // so dass min n�her am Ursprung liegt als max.
        min = Point.createMinimumFrom(p1, p2);
        max = Point.createMaximumFrom(p1, p2);
    }
    
    // Enth�lt die Fl�che den Punkt?
    public boolean hasPoint(Point p) {
        return ((p.co[icnst] == min.co[icnst])
                && (p.co[ia] >= min.co[ia] && p.co[ia] <= max.co[ia])
                && (p.co[ib] >= min.co[ib] && p.co[ib] <= max.co[ib]));
    }
    
    // Liegt der Punkt am Rand der Fl�che?
    public boolean hasPointOnEdge(Point p) {
        return p.co[icnst] == min.co[icnst]
                && (((p.co[ia] == min.co[ia] || p.co[ia] == max.co[ia])
                        && (p.co[ib] >= min.co[ib] && p.co[ib] <= max.co[ib]))
                    || ((p.co[ib] == min.co[ib] || p.co[ib] == max.co[ib])
                            && (p.co[ia] >= min.co[ia] && p.co[ia] <= max.co[ia])));
    }
    
    // Ist der Punkt ein Eckpunkt der Fl�che?
    public boolean hasPointOnVertex(Point p) {
        return p.co[icnst] == min.co[icnst]
                && (p.co[ia] == min.co[ia] || p.co[ia] == max.co[ia])
                && (p.co[ib] == min.co[ib] || p.co[ib] == max.co[ib]);
    }
    
    // Liegt der Punkt in der Fl�che, aber nicht am Rand?
    public boolean hasPointInside(Point p) {
        return hasPoint(p) && !hasPointOnEdge(p);
    }
    
    // TODO equals, hash?
    public boolean isEqualTo(Face f) {
        return min.isEqualTo(f.min) && max.isEqualTo(f.max);
    }
    
    // Auf welcher H�he schneidet die Fl�che die z-Achse?
    public float getZLevel() {
        if (normale == Axis.Z) return min.co[icnst];
        else throw new WrongPlaneException("Face is not in the x-y-plane!");
    }
    
    // Schneidet diese eine andere Fl�che,
    // wenn beide auf dieselbe x-y-Ebene projiziert werden?
    public boolean intersectsXYWith(Face f){
        if (isEqualTo(f)) return true;
        else return ((((min.co[ia] > f.min.co[ia] && min.co[ia] < f.max.co[ia])
	  || (max.co[ia] > f.min.co[ia] && max.co[ia] < f.max.co[ia]))
	 || ((f.min.co[ia] > min.co[ia] && f.min.co[ia] < max.co[ia])
	     || (f.max.co[ia] > min.co[ia] && f.max.co[ia] < max.co[ia]))
	 || (min.co[ia] == f.min.co[ia] && max.co[ia] == f.max.co[ia]))
	&& (((min.co[ib] > f.min.co[ib] && min.co[ib] < f.max.co[ib])
	     || (max.co[ib] > f.min.co[ib] && max.co[ib] < f.max.co[ib]))
	    || ((f.min.co[ib] > min.co[ib] && f.min.co[ib] < max.co[ib])
		|| (f.max.co[ib] > min.co[ib] && f.max.co[ib] < max.co[ib]))
	    || (min.co[ib] == f.min.co[ib] && max.co[ib] == f.max.co[ib])));
    }
    
    // Liegen beide Fl�chen auf derselben Ebene
    // und schneiden sich?
    public boolean intersectsWith(Face f) {
        boolean res = false;
        try {
            res = getZLevel() == f.getZLevel() && intersectsXYWith(f);
        }
        catch (WrongPlaneException e) {
            System.out.println(e);
            System.exit(1);
        }
        return res;
    }
    
    // Kopie dieser Fl�che, die auf einer bestimmten x-y-Ebene liegt.
    public Face copyToLevel(float z) {
        if (normale != Axis.Z)
            throw new WrongPlaneException("Face is not in the x-y-plane!");
        return new Face(new Point(new float[] {min.co[0], min.co[1], z}),
                        new Point(new float[] {max.co[0], max.co[1], z}));
    }
    
    // Die vier Geraden, die diese Fl�che begrenzen,
    // in eine Geradensammlung geben.
    public void addXYStraightsTo(XYStraights xys) {
        xys.add(new Straight(new float[] {min.co[0], min.co[1], min.co[2]}, Axis.X));
        xys.add(new Straight(new float[] {max.co[0], max.co[1], max.co[2]}, Axis.X));
        xys.add(new Straight(new float[] {min.co[0], min.co[1], min.co[2]}, Axis.Y));
        xys.add(new Straight(new float[] {max.co[0], max.co[1], max.co[2]}, Axis.Y));
    }
}

@SuppressWarnings("serial")
class WrongPlaneException extends RuntimeException {
    WrongPlaneException(String m) {
        super(m);
    }
}
