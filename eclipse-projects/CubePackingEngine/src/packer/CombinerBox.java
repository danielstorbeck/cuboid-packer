package packer;

import box.OrdDims;
import box.Container;

/**
 * Ist ein box.Container Quader.
 * Beurteilt mit anderem Quader gebildeten Au�enquader.
 */
public class CombinerBox extends Container {
	
    public CombinerBox(OrdDims od) {
        super(od.getDims());
    }
    
    public CombinerBox(CombinerBox b1, CombinerBox b2) {
        super(b1, b2);
    }
    
    // Ausma�e der gr��ten Fl�che.
    public float[] getBiggestSurface() {
        return new float[] {oDims[1], oDims[2]};
    }
    
    // Inhalt der gr��ten Fl�che.
    public float getBiggestSurfaceArea() {
        return oDims[1] * oDims[2];
    }
    
    // Verh�ltnis der Schnittfl�che und der gr��ten Kontaktfl�che.
    // 1 bei Kongruenz. Kleinere Werte f�r gr��ere Verschwendung.
    public float getContactBadnessWith(CombinerBox that) {
    	// Der gr��ere der gr��ten Fl�cheninhalte der beiden Quader.
        float bsa = Math.max(this.getBiggestSurfaceArea(),
                             that.getBiggestSurfaceArea());
        // Die jeweils gr��ten Fl�chen.
        float[] s1 = this.getBiggestSurface();
        float[] s2 = that.getBiggestSurface();
        // Fl�cheninhalt der Schnittfl�che der jeweils gr��ten Fl�chen.
        float csa = Math.min(s1[0], s2[0]) * Math.min(s1[1], s2[1]);
        // 1 optimal, ansonsten je kleiner, desto schlechter.
        return csa / bsa;
    }
    
    // Verh�ltnis der Volumensumme und des Volumens des Au�enquaders.
    public float getVolumeBadnessWith(CombinerBox that) {
        float[] od1 = this.getOrdDims();
        float[] od2 = that.getOrdDims();
        // Ausma�e des Au�enquaders.
        float[] cd = new float[] {Math.max(od1[1], od2[1]),
                                  Math.max(od1[2], od2[2]),
                                  od1[0] + od2[0]};
        // Volumensumme der beiden Quader durch Volumen des Au�enquaders.
        // 1 optimal, ansonsten je kleiner, desto schlechter.
        return ((od1[0] * od1[1] * od1[2]) + (od2[0] * od2[1] * od2[2]))
                / (cd[0] * cd[1] * cd[2]);
    }
    
    public void acceptVisitor(Visitor v, float[] orig) {
        v.processBox(this, orig);
    }
    
    public void delegateVisitor(Visitor v, float[] orig) {
        ((CombinerBox)c1).acceptVisitor(v, new float[] {
                    orig[0] + v1[0], orig[1] + v1[1], orig[2] + v1[2]});
        ((CombinerBox)c2).acceptVisitor(v, new float[] {
                    orig[0] + v2[0], orig[1] + v2[1], orig[2] + v2[2]});
    }
}
