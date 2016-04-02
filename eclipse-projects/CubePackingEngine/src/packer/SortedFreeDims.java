package packer;

import box.OrdDims;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;

/**
 * Sortiert eine Liste von Quaderausmaßen.
 * Kann verschiedene Sortierweisen benutzen.
 * Kann iteriert werden mit hasNext und next.
 */
public class SortedFreeDims implements Enumeration<OrdDims> {
	
	// Aufzählung der möglichen Sortierweisen.
	// Elemente entsprechen weiter unten implementierten Klassen.
	// (Derzeit nur eine.)
    public static enum Order {SUM_OF_EDGES};
    
    // Liste der Quaderausmaße.
    List<OrdDims> lod;
    // Anzahl Elemente und Zeiger auf aktuelles Element.
    int len;
    int idx;
    
    // Konstruktion mit Liste von Quaderausmaßen und Sortierweise.
    // Unmittelbares Ausführen des Sortierens.
    public SortedFreeDims(List<OrdDims> ld, Order so) {
        lod = ld;
        len = lod.size();
        idx = -1;
        // Einstellen der Sortierweise.
        Comparator<OrdDims> cmp;
        if (so == Order.SUM_OF_EDGES) {
        	cmp = new SumOfEdgesOrder();
        }
        else {
        	cmp = new SumOfEdgesOrder();
        }
        // Sortieren.
        Collections.sort(lod, cmp);
    }
    
    public void resetIndex() {
        idx = -1;
    }
    
    public boolean hasMoreElements() {
        if (idx < len - 1) return true;
        else return false;
    }
    
    public OrdDims nextElement() {
        if (idx >= len - 1) {
        	throw new NoSuchElementException();
        }
        else {
            idx++;
            return lod.get(idx);
        }
    }
}

// Eine Sortierweise.
// Ein Quader ist größer, wenn die Summe seiner
// x-, y- und z-Ausmaße größer ist.
class SumOfEdgesOrder implements Comparator<OrdDims> {
    @Override
    public int compare(OrdDims od1, OrdDims od2) {
        float[] dm1 = od1.getDims();
        float[] dm2 = od2.getDims();
        float sd1 = dm1[0] + dm1[1] + dm1[2];
        float sd2 = dm2[0] + dm2[1] + dm2[2];
        // Sort in descending order.
        if (sd1 == sd2) return 0;
        else if (sd1 < sd2) return 1;
        else return -1;
    }
}
