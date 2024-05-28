package box;

import box.Module.Axis;
import store.SituatedBox;
import java.util.ArrayList;
import java.util.List;

/**
 * Quader, der in einen anderen eingebettet werden kann.
 * Hat Rotationshistorie.
 * Rotiert in eine optimale Ausgangslage.
 */
public class PackBox extends RotatingBox {
	
	// Enthaltender Quader.
    PackBox parent = null;
    // Rotationshistorie
    List<Axis> rHist = new ArrayList<Axis>();
    
    public PackBox(float[] d) {
        super(d);
    }

    // Rotationsmethoden überschrieben, so dass sie die Folge
    // der ausgeführten Rotationen archivieren.
    @Override
    void rotX() {
        super.rotX(); rHist.add(Axis.X);
    }
    @Override
    void rotY() {
        super.rotY(); rHist.add(Axis.Y);
    }
    @Override
    void rotZ() {
        super.rotZ(); rHist.add(Axis.Z);
    }
    
    // Rotation in die optimierte Ausgangsposition.
    // Die größte Fläche soll parallel zur x-y-Ebene sein.
    // Die längste Kante soll parallel zur x-Achse sein.
    // Wird von enthaltendem Quader benutzt, um die größten
    // Flächen der enthaltenen Quader aneinander liegen zu lassen.
    public void optimizeOrientation() {
        float area1, area2, len1, len2;
        area1 = diag[0] * diag[1];
        // Versuch, durch y-Rotation zu optimieren.
        rotY(); area2 = diag[0] * diag[1];
        if (area2 < area1) rotY();
        else area1 = area2;
        // Versuch, durch x-Rotation zu optimieren.
        rotX(); area2 = diag[0] * diag[1];
        if (area2 < area1) rotX();
        // Ist die längste Kante parallel zur x-Achse?
        len1 = diag[0];
        rotZ(); len2 = diag[0];
        if (len2 < len1) rotZ();
    }
    
    // Komplette Rotationshistorie zusammenstellen.
    List<List<Axis>> getRotationHistory() {
        List<List<Axis>> rhl = new ArrayList<List<Axis>>();
        rhl.add(rHist);
        PackBox p = parent;
        // Einbettungshierarchie hochsteigen und Historien einsammeln.
        while (p != null) {
            rhl.add(p.rHist); p = p.parent;
        }
        return rhl;
    }
    
    // Fehlende Rotationen der kompletten Historie nachholen.
    public void performAncestorRotations() {
        if (parent == null) return;
        List<List<Axis>> rhl = parent.getRotationHistory();
        for (List<Axis> rh : rhl)
            for (Axis a : rh)
            	// Rotationsmethoden der Oberklasse verwenden,
            	// damit die Historie nicht geändert wird.
                switch (a) {
                    case X:
                        super.rotX();
                        break;
                    case Y:
                        super.rotY();
                        break;
                    case Z:
                        super.rotZ();
                        break;
                }
    }
    
    public void rotateTo(SituatedBox sb) {
        float[] sbd = sb.getOrigDimsCopy();
        if (diag[0] != sbd[0])
            rotY();
        if (diag[0] != sbd[0]) {
            rotX(); rotY();
        }
        if (diag[1] != sbd[1])
            rotX();
    }
}
