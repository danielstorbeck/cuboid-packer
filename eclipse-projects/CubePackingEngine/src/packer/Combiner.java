package packer;

import box.OrdDims;
import box.Container;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collections;

/**
 * Sortiert und vorkombiniert eine Liste von Quadern.
 * Die Menge kann mit hasNext u. next iteriert werden.
 * Sind Kombinationen zu groß, können sie wieder entpackt werden.
 */
public class Combiner implements Enumeration<CombinerBox> {
	
	// Liste kombinierbarer Quader.
    List<CombinerBox> lb = new ArrayList<CombinerBox>();
    // Sortierkriterium.
    BiggestSurfaceAreaOrder cmp = new BiggestSurfaceAreaOrder();
    
    // Konstruktion mit Liste von Quaderausmaßen.
    public Combiner(List<OrdDims> ld) {
    	// Erzeugung kombinierbarer Quader aus Ausmaßen.
        for (OrdDims od : ld) {
        	lb.add(new CombinerBox(od));
        }
        // Sortieren.
        Collections.sort(lb, cmp);
        // Erster Kombinierlauf.
        combine();
    }
    
    public boolean hasMoreElements() {
        return lb.size() > 0 ? true : false;
    }
    
    public CombinerBox nextElement() {
        if (lb.size() < 1) {
        	throw new NoSuchElementException();
        }
        return lb.remove(lb.size() - 1);
    }

    // Zwei Indices, die auf Quader zeigen, die eine
    // geeignete Kombination darstellen.
    // Gezeigt wird in die Quaderliste.
	class PairOfIndices {
		int first;
		int second;

		PairOfIndices(int f, int s) {
			first = f;
			second = s;
		}

		public int getFirst() {
			return first;
		}

		public int getSecond() {
			return second;
		}
	}

    // Sofern mehr als ein Quader in der Liste ist,
    // wird versucht, geeignete Quader zu kombinieren,
    // solange sich geeignete Kombinationen finden lassen.
    void combine() {
        boolean goOn = true;
        PairOfIndices pi;
        if (lb.size() < 2) return;
        while (goOn) {
            pi = findGoodCombination();
            if (pi == null) {
                goOn = false;
                continue;
            }
            else combinePair(pi);
        }
    }
    
    // Kombiniert ein geeignetes Paar Quader.
    void combinePair(PairOfIndices pi) {
    	// Quader heraussuchen.
        CombinerBox b1 = lb.get(pi.getFirst());
        CombinerBox b2 = lb.get(pi.getSecond());
        // Aus der Liste entfernen.
        lb.remove(b1);
        lb.remove(b2);
        // Verpacken.
        CombinerBox cb = new CombinerBox(b1, b2);
        // Kombination in die Liste stecken.
        lb.add(cb);
        // Liste wieder sortieren.
        Collections.sort(lb, cmp);
    }
    
    // Finden einer geeigneten Kombination von Quadern.
    PairOfIndices findGoodCombination() {
    	// Erster, zweiter und letzter Index.
        int fi = 0, si = 0, li = lb.size() - 1;
        // Es wird versucht, möglichst große Werte für zwei
        // Kombinationskriterien zu finden.
        // Volume badness und contact badness sollten
        // eigentlich goodness heißen, weil 0 der schlechteste
        // Grenzwert und 1 der bestmögliche Wert ist.
        float maxVb = 0, maxCb = 0;
        // Für beide Kriterien gibt es ein Indexpaar.
        PairOfIndices vbPair = null, cbPair = null;
        // Sicherlich sollte mehr als ein Quader da sein.
        if (lb.size() < 2) return null;
        // Schlimmstenfalls werden alle möglichen ungeordneten Paare
        // in der Liste durchprobiert. 
        while (fi < li) {
        	// Der zweite Index liegt immer rechts vom ersten.
            si = fi + 1;
            while (si <= li) {
            	// Paar raussuchen.
                CombinerBox fc = lb.get(fi);
                CombinerBox sc = lb.get(si);
                // Versuchen die badness / goodness Werte zu erhöhen.
                float vb = fc.getVolumeBadnessWith(sc);
                float cb = fc.getContactBadnessWith(sc);
                if (vb > maxVb) {
                    maxVb = vb;
                    vbPair = new PairOfIndices(fi, si);
                }
                if (cb > maxCb) {
                    maxCb = cb;
                    cbPair = new PairOfIndices(fi, si);
                }
                // Bei optimaler Kombination vorzeitig abbrechen.
                if (maxVb == 1.0 || maxCb == 1.0) break;
                si = si + 1;
            }
            // Bei optimaler Kombination vorzeitig abbrechen.
            if (maxVb == 1.0 || maxCb == 1.0) break;
            fi = fi + 1;
        }
        // Nichts zurückgeben, wenn beide Werte zu schlecht.
        if (maxVb < 0.75 && maxCb < 0.75) return null;
        // Optimale Kombination zurück geben, falls vorhanden.
        else if (maxVb == 1.0) return vbPair;
        else if (maxCb == 1.0) return cbPair;
        // Ansonsten die bessere der beiden.
        else if (maxVb > maxCb) return vbPair;
        else return cbPair;
    }
    
    // Kombination auspacken.
    public void unpack(CombinerBox c) {
        Container[] bb = c.unpack();
        CombinerBox b1 = (CombinerBox) bb[0];
        CombinerBox b2 = (CombinerBox) bb[1];
        lb.add(b1);
        lb.add(b2);
        Collections.sort(lb, cmp);
    }
    
    // Alle Kombinationen auspacken.
    public void unpackAll() {
    	// Hilfsliste für zu entpackende Kombinationen.
        List<CombinerBox> l = new ArrayList<CombinerBox>();
        // Alle Kombinationen finden und in Hilfsliste stecken.
        for (CombinerBox cb : lb) {
            if (cb.hasContent()) {
                l.add(cb);
            }
        }
        // Alle Kombinationen in der Hilfsliste aus
        // der regulären Liste nehmen.
        for (CombinerBox cb : l) {
            lb.remove(cb);
        }
        while (l.size() > 0) {
            CombinerBox cb = l.remove(l.size() - 1);
            // Einfache Quader kommen zurück in die reguläre Liste. 
            // TODO Warum diese Klausel? Das mach ich im else doch auch.
            if (!cb.hasContent()) {
            	lb.add(cb);
            }
            else {
                Container[] bb = cb.unpack();
                CombinerBox b1 = (CombinerBox) bb[0];
                CombinerBox b2 = (CombinerBox) bb[1];
                if (b1.hasContent()) l.add(b1);
                else lb.add(b1);
                if (b2.hasContent()) l.add(b2);
                else lb.add(b2);
            }
        }
    }
}

// Sortierkriterium, sortiert aufsteigend nach größter Fläche.
class BiggestSurfaceAreaOrder implements Comparator<CombinerBox> {
    @Override
    public int compare(CombinerBox b1, CombinerBox b2) {
    	// Jeweils größte Flächen.
        float a1 = b1.getBiggestSurfaceArea();
        float a2 = b2.getBiggestSurfaceArea();
        // Größer ist, wer die größte Fläche hat.
        if (a1 == a2) return 0;
        else if (a1 < a2) return -1;
        else return 1;
    }
}