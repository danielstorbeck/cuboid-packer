package store;

public class Point {
	
	//Koordinaten
    float[] co = new float[3];
    
    // Konstruktion mit Koordinaten
    public Point(float[] co2) {
        System.arraycopy(co2, 0, co, 0, 3);
    }
    
    // TODO equals und hash?
    public boolean isEqualTo(Point p) {
        return (co[0] == p.co[0] && co[1] == p.co[1] && co[2] == p.co[2]);
    }
    
    // Neuer Punkt aus den jeweils kleinsten Koordinaten
    // der beiden Ausgangspunkte.
    public static Point createMinimumFrom(Point p1, Point p2) {
        return new Point(new float[]{
                    Math.min(p1.co[0], p2.co[0]),
                    Math.min(p1.co[1], p2.co[1]),
                    Math.min(p1.co[2], p2.co[2])});
    }

    // Neuer Punkt aus den jeweils größten Koordinaten
    // der beiden Ausgangspunkte.
    public static Point createMaximumFrom(Point p1, Point p2) {
        return new Point(new float[]{
                    Math.max(p1.co[0], p2.co[0]),
                    Math.max(p1.co[1], p2.co[1]),
                    Math.max(p1.co[2], p2.co[2])});
    }
}
