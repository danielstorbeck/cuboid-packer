package store;

import box.OrdDims;

/**
 * Stellt einen situierten bzw. im Kontainer positionierten
 * Quader dar. Gebildet aus einem situierten Punkt und
 * den Dimensionen.
 */
public class SituBox extends OrdDims {
	
    float[] ori = new float[3];
    
    public SituBox(float[] dm, float[] og) {
        super(dm);
        System.arraycopy(og, 0, ori, 0, 3);
    }
    
    public float[] getOrigin() {
        return ori;
    }
    
    public String printOrigin() {
        return "" + ori[0] + ", " + ori[1] + ", " + ori[2];
    }
}
