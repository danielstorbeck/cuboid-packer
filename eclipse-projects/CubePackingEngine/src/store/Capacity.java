package store;

import box.OrdDims;
import java.util.List;
import java.util.ArrayList;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Iterator;

/**
 * Stellt den Kontainer dar, in dem Quader platziert werden.
 * Hat Liste mit freien Quadern.
 * Hat Liste mit besetzten Quadern.
 * Besetzte Quader waren ehemals frei.
 * Es kann eine Volumendifferenz zw. besetzten Quadern
 * und den darin enthaltenen Quadern geben.
 */
public class Capacity extends Cuboid {
	
	// Freie Quader.
	// Liste wird nach jeder Platzierung neu generiert.
	List<Cuboid> lfc;
	// Besetzte, ehemals freie Quader.
    List<Cuboid> lc = new ArrayList<Cuboid>();
    
    public Capacity(float[] dm) {
        super(dm, new Point(new float[] {0.0f, 0.0f, 0.0f}),
                new Point(dm));
        updateFreeCuboids();
    }
    
    // Sammelt die z-Koordinaten aller x-y-Fl�chen ein, aus denen
    // der Kontainer besteht sowie die aller enthaltenen belegten
    // Quader.
    SortedSet<Float> getAllZValues() {
        SortedSet<Float> zv = new TreeSet<Float>();
        addZValuesTo(zv);
        for (Cuboid cb : lc) {
        	cb.addZValuesTo(zv);
        }
        return zv;
    }
    
    // Der Kontainer und alle belegten Quader geben diejenigen
    // ihrer x-y-Fl�chen in eine Liste, die auf der angegebenen
    // z-Ebene liegen.
    List<Face> getFacesOnLevel(float z) {
        List<Face> lf = new ArrayList<Face>();
        addZFaceOnLevelTo(z, lf);
        for (Cuboid cb : lc) {
        	cb.addZFaceOnLevelTo(z, lf);
        }
        return lf;
    }
    
    // Aufwendigste Funktion des Programms:
    // Die begrenzenden Geraden der x-y-Fl�chen der situierten
    // Quader werden eingesammelt. Daraus werden alle m�glichen
    // Kombinationen von zwei x- und zwei y-Geraden erzeugt,
    // um Fl�chen zu erzeugen. F�r jede der Fl�chen wird gepr�ft,
    // ob sie von einem der situierten Quader blockiert wird.
    List<Face> getFreeFacesOnLevel(float z) {
        // Alle vorhandenen Fl�chen auf der angegebenen z-Ebene.
        List<Face> lf = getFacesOnLevel(z);
        // TODO Das wird schon in getfacesonlevel aufgerufen.
        // Macht aber wohl keinen Unterschied, weil die
        // begrenzenden Geraden in einem Set landen.
        addZFaceOnLevelTo(z, lf);
        // Sammlung von x- und y-Geraden,
        // die die vorhandenen Fl�chen begrenzen.
        XYStraights xys = new XYStraights();
        for (Face f : lf) {
            f.addXYStraightsTo(xys);
        }
        // Generieren aller m�glichen Fl�chen,
        // die von diesen Geraden begrenzt werden.
        List<Face> laf = xys.getAllFaces();
        // Liste f�r freie Fl�chen, die eine Untermenge
        // der generierten Fl�chen sind.
        List<Face> lff = new ArrayList<Face>();
        // Test f�r alle generierten Fl�chen,
        for (Face f : laf) {
            boolean blocked = false;
            // ob sie von irgendeinem der situierten Quader
            // blockiert werden.
            for (Cuboid cb : lc) {
                if (cb.blocks(f)) {
                    blocked = true;
                    break;
                }
            }
            if (!blocked) lff.add(f);
        }
        return lff;
    }
    
    // Es wird eine Liste freier Quader zur�ck gegeben.
    // Die werden erzeugt, indem alle z-Ebenen im Kontainer
    // von unten nach oben durchiteriert werden, und von der
    // untersten der Ebenen aus versucht wird, ausgehend von
    // freien Fl�chen der untersten Ebene, Quader nach oben
    // hin so weit aufzuspannen, wie diese nicht von belegten
    //  Fl�chen einer h�heren Ebene blockiert werden.
    void addFreeCuboidsAboveLevelTo(SortedSet<Float> zv, List<Cuboid> lc) {
    	// Iteriert wird �ber die z-Ebenen.
        Iterator<Float> zi = zv.iterator();
        // Jeder der Werte ist einmal der Basis-z-Wert.
        Float bz;
        // Mindestens eine z-Ebene muss es geben.
        if (!zi.hasNext()) return;
        // Initialer Basis-z-Wert.
        bz = zi.next();
        // Es muss mindestens eine h�here z-Ebene geben.
        if (!zi.hasNext()) return;
        // Freie Fl�chen auf der aktuellen Basis-z-Ebene.
        List<Face> lbf = getFreeFacesOnLevel(bz);
        // Iterieren �ber die n�chsth�heren Ebenen.
        while (zi.hasNext()) {
        	// Aktuelle n�chsth�here Ebene.
            Float cz = zi.next();
            // Blockierende Fl�chen auf der n�chsth�heren Ebene.
            List<Face> czf = getFacesOnLevel(cz);
            // F�r jede freie Fl�che der Basis-z-Ebene,
            for (Face bf : lbf) {
            	// versuchen, auf der aktuellen n�chsth�heren Ebene
            	// eine blockierende Fl�che zu finden. Letztendlich
            	// ist das die obere Fl�che des Kontainers, sofern
            	// keine anderen dazwischen kommen.
                for (Face cf : czf) {
                	// Sobald eine gefunden wird, wird ein Quader von
                	// der Basisfl�che hoch bis zur blockierenden Fl�che
                	// aufgespannt und in die �bergebene Liste gesteckt.
                    if (cf.intersectsXYWith(bf)) {
                        lc.add(Cuboid.createWith(bf.min,
                                new Point(new float[] {bf.max.co[0], bf.max.co[1], cz})));
                        break;
                    }
                }
            }
        }
    }
    
    // Alle freien Quader des Kontainers generieren.
    List<Cuboid> getAllFreeCuboids() {
        List<Cuboid> afc = new ArrayList<Cuboid>();
        // F�r alle z-Ebenen, in denen sich x-y-Fl�chen
        // belegter Quader befinden, inkl. x-y-Fl�chen
        // des Kontainers,
        SortedSet<Float> azv = getAllZValues();
        for (Float z : azv) {
        	// alle Quader generieren, die sich von den freien
        	// Fl�chen der jeweiligen Ebene nach oben hin
        	// aufspannen lassen, bis sie von dar�ber liegenden
        	// Fl�chen oder letztendlich dem Kontainerdeckel
        	// blockiert werden.
            addFreeCuboidsAboveLevelTo(azv.tailSet(z), afc);
        }
        return afc;
    }

    // Vom Packer aufgerufen, um die Ausma�e aller freien
    // Quader zu bekommen.
    public List<OrdDims> getFreeDimensions() {
        List<OrdDims> lfd = new ArrayList<OrdDims>();
        for (Cuboid fc : lfc)
            lfd.add(fc.getOrdDimsBox());
        return lfd;
    }

    // Liste freier Quader neu generieren.
    void updateFreeCuboids() {
    	lfc = getAllFreeCuboids();
    }
    
    // Wird vom Packer aufgerufen, um einen Quader zu platzieren.
    // Es gibt eine Liste mit freien Quadern, die durchprobiert werden.
    // Die freien Quader werden nach jeder Platzierung neu generiert,
    // erstmalig jedoch im Konstruktor.
    public SituBox add(OrdDims od) {
    	// Alle freien Quader durchgehen und versuchen,
    	// den �bergebenen Quader zu situieren.
        SituBox sb = null;
        for (Cuboid fc : lfc) {
            sb = fc.placeBox(od);
            if (sb != null) break;
        }
        // Wenn es nicht m�glich ist, den Quader unterzubringen,
        // null zur�ck geben.
        if (sb == null) return null;
        // Bei Erfolg einen entsprechenden situierten Quader
        // (inkl. Eckpunkten u. Fl�chen)
        // in der Liste der besetzten Quader speichern,
        // die Liste der freien Quader neu generierten
        // und die SituBox (nur Ausma�e u. Position) zur�ck geben.
        else {
            float[] dms = sb.getDims();
            lc.add(Cuboid.createWith(new Point(sb.ori),
                    new Point(new float[] {
                                dms[0] + sb.ori[0],
                                dms[1] + sb.ori[1],
                                dms[2] + sb.ori[2]})));
            updateFreeCuboids();
            return sb;
        }
    }

	// F�r jeden neuen freien Quader pr�fen, ob er einen
	// der besetzten Quader schneidet.
//	private void checkNewFreeCuboidIntersection(List<Cuboid> afc) {
//		for (Cuboid fc : afc) {
//			for (Cuboid oc : lc) {
//				if (oc.intersectsWith(fc)) {
//					System.out.println("Neuer freier Quader: "
//							+ fc.toString()
//							+ ", schneidet besetzten Quader: "
//							+ oc.toString());
//				}
//			}
//		}
//	}
    
}
