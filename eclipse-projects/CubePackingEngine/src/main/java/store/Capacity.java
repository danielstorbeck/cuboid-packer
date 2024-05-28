package store;

import box.OrderedDimensions;
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
    
    // Sammelt die z-Koordinaten aller x-y-Flächen ein, aus denen
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
    // ihrer x-y-Flächen in eine Liste, die auf der angegebenen
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
    // Die begrenzenden Geraden der x-y-Flächen der situierten
    // Quader werden eingesammelt. Daraus werden alle möglichen
    // Kombinationen von zwei x- und zwei y-Geraden erzeugt,
    // um Flächen zu erzeugen. Für jede der Flächen wird geprüft,
    // ob sie von einem der situierten Quader blockiert wird.
    List<Face> getFreeFacesOnLevel(float z) {
        // Alle vorhandenen Flächen auf der angegebenen z-Ebene.
        List<Face> lf = getFacesOnLevel(z);
        // TODO Das wird schon in getfacesonlevel aufgerufen.
        // Macht aber wohl keinen Unterschied, weil die
        // begrenzenden Geraden in einem Set landen.
        addZFaceOnLevelTo(z, lf);
        // Sammlung von x- und y-Geraden,
        // die die vorhandenen Flächen begrenzen.
        XYStraights xys = new XYStraights();
        for (Face f : lf) {
            f.addXYStraightsTo(xys);
        }
        // Generieren aller möglichen Flächen,
        // die von diesen Geraden begrenzt werden.
        List<Face> laf = xys.getAllFaces();
        // Liste für freie Flächen, die eine Untermenge
        // der generierten Flächen sind.
        List<Face> lff = new ArrayList<Face>();
        // Test für alle generierten Flächen,
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
    
    // Es wird eine Liste freier Quader zurück gegeben.
    // Die werden erzeugt, indem alle z-Ebenen im Kontainer
    // von unten nach oben durchiteriert werden, und von der
    // untersten der Ebenen aus versucht wird, ausgehend von
    // freien Flächen der untersten Ebene, Quader nach oben
    // hin so weit aufzuspannen, wie diese nicht von belegten
    //  Flächen einer höheren Ebene blockiert werden.
    void addFreeCuboidsAboveLevelTo(SortedSet<Float> zv, List<Cuboid> lc) {
    	// Iteriert wird über die z-Ebenen.
        Iterator<Float> zi = zv.iterator();
        // Jeder der Werte ist einmal der Basis-z-Wert.
        Float bz;
        // Mindestens eine z-Ebene muss es geben.
        if (!zi.hasNext()) return;
        // Initialer Basis-z-Wert.
        bz = zi.next();
        // Es muss mindestens eine höhere z-Ebene geben.
        if (!zi.hasNext()) return;
        // Freie Flächen auf der aktuellen Basis-z-Ebene.
        List<Face> lbf = getFreeFacesOnLevel(bz);
        // Iterieren über die nächsthöheren Ebenen.
        while (zi.hasNext()) {
        	// Aktuelle nächsthöhere Ebene.
            Float cz = zi.next();
            // Blockierende Flächen auf der nächsthöheren Ebene.
            List<Face> czf = getFacesOnLevel(cz);
            // Für jede freie Fläche der Basis-z-Ebene,
            for (Face bf : lbf) {
            	// versuchen, auf der aktuellen nächsthöheren Ebene
            	// eine blockierende Fläche zu finden. Letztendlich
            	// ist das die obere Fläche des Kontainers, sofern
            	// keine anderen dazwischen kommen.
                for (Face cf : czf) {
                	// Sobald eine gefunden wird, wird ein Quader von
                	// der Basisfläche hoch bis zur blockierenden Fläche
                	// aufgespannt und in die übergebene Liste gesteckt.
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
        // Für alle z-Ebenen, in denen sich x-y-Flächen
        // belegter Quader befinden, inkl. x-y-Flächen
        // des Kontainers,
        SortedSet<Float> azv = getAllZValues();
        for (Float z : azv) {
        	// alle Quader generieren, die sich von den freien
        	// Flächen der jeweiligen Ebene nach oben hin
        	// aufspannen lassen, bis sie von darüber liegenden
        	// Flächen oder letztendlich dem Kontainerdeckel
        	// blockiert werden.
            addFreeCuboidsAboveLevelTo(azv.tailSet(z), afc);
        }
        return afc;
    }

    // Vom Packer aufgerufen, um die Ausmaße aller freien
    // Quader zu bekommen.
    public List<OrderedDimensions> getFreeDimensions() {
        List<OrderedDimensions> lfd = new ArrayList<OrderedDimensions>();
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
    public SituatedBox add(OrderedDimensions od) {
    	// Alle freien Quader durchgehen und versuchen,
    	// den übergebenen Quader zu situieren.
        SituatedBox sb = null;
        for (Cuboid fc : lfc) {
            sb = fc.placeBox(od);
            if (sb != null) break;
        }
        // Wenn es nicht möglich ist, den Quader unterzubringen,
        // null zurück geben.
        if (sb == null) return null;
        // Bei Erfolg einen entsprechenden situierten Quader
        // (inkl. Eckpunkten u. Flächen)
        // in der Liste der besetzten Quader speichern,
        // die Liste der freien Quader neu generierten
        // und die SituBox (nur Ausmaße u. Position) zurück geben.
        else {
            float[] dms = sb.getOrigDimsCopy();
            lc.add(Cuboid.createWith(new Point(sb.origin),
                    new Point(new float[] {
                                dms[0] + sb.origin[0],
                                dms[1] + sb.origin[1],
                                dms[2] + sb.origin[2]})));
            updateFreeCuboids();
            return sb;
        }
    }

	// Für jeden neuen freien Quader prüfen, ob er einen
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
