package box;

import java.util.Arrays;

/**
 * Stellt die Ausmaße eines Quaders dar.
 * Prüft auf Enthaltensein.
 * Prüft, ob Volumen größer null.
 * Quader hat ID. 
 */
public class OrdDims {
	
	// Seitenlängen in der Folge, wie sie übergeben wurden.
    protected float[] dims = new float[3];
    // Seitenlängen sortiert.
    protected float[] oDims = new float[3];
    
    int id = -1;
    
    // Übergebenes Tripel kopieren.
    public OrdDims(float[] d) {
        System.arraycopy(d, 0, dims, 0, 3);
        // Eine Kopie sortieren.
        System.arraycopy(d, 0, oDims, 0, 3);
        orderDims();
    }
    
    protected void orderDims() {
    	Arrays.sort(oDims);
    }
    
    // Passt der Quader in einen anderen?
    public boolean fitsIn(OrdDims that) {
        if (this.oDims[0] <= that.oDims[0]
                && this.oDims[1] <= that.oDims[1]
                && this.oDims[2] <= that.oDims[2])
            return true;
        else return false;
    }
    
    // Volumen größer null?
    public boolean isNonZero() {
        if (dims[0] > 0 && dims[1] > 0 && dims[2] > 0)
            return true;
        else return false;
    }

    // Kopie des originalen Tripels rausgeben.
    public float[] getDims() {
        return new float[] {dims[0], dims[1], dims[2]};
    }
    
    // Tripel in Druckform.
    // TODO Warum nicht toString implementieren?
    public String printDimensions() {
        return "" + dims[0] + ", " + dims[1] + ", " + dims[2];
    }
    
    // Kopie des sortierten Tripels.
    public float[] getOrdDims() {
        return new float[] {oDims[0], oDims[1], oDims[2]};
    }
    
    public void setID(int i) {
        id = i;
    }
    public int getID() {
        return id;
    }
}
