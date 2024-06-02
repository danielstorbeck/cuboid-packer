package box;

/**
 * Stellt Quader als Vektor dar.
 * Quader ist in dieser Darstellung als Vektor rotierbar.
 * Rotation in alle Richtungen in 90 Grad-Schritten.
 * (Durch Fummeln drauf gekommen.) 
 */
public class RotatingBox extends OrderedDimensions {

    // Vektor, relativ zu Ursprung
    // eines imaginären Koordinatensystems.
    float[] diag = new float[3];

    // Derzeitige Rotation als Einheitsvektor dargestellt.
    int[] rot = {1, 1, 1};

    // Rotationsmasken für die Änderung der
    // Orientierung der Vektorkomponenten.
    // Z.B. wandert der Ursprung des Vektors bei einer
    // Rotation um die x-Achse auf den Ecken der y-z-Fläche,
    // auf der er derzeit liegt.
    // Weiter dokumentiert am Fall der x-Rotation, siehe unten.
    int[] xRotMask = {1, -1};
    int[] yRotMask = {1, -1};
    int[] zRotMask = {1, -1};

    // Konstruktion aus Ausmaßen.
    public RotatingBox(float[] d) {
        super(d);
        // Originale Ausmaße sind auch originaler Vektor.
        System.arraycopy(d, 0, diag, 0, 3);
    }

    // Konstruktion aus Ausmaß-Objekt.
    public RotatingBox(OrderedDimensions od) {
        super(od.origDims);
        System.arraycopy(od.origDims, 0, diag, 0, 3);
    }

    // Rotation um x-Achse.
    void rotX() {
        // Invertieren der x-Rotationsmaske.
        xRotMask[0] = xRotMask[0] * -1;
        xRotMask[1] = xRotMask[1] * -1;
        // Damit invertieren der y- und z-Komponenten
        // des Einheitsvektors.
        rot[1] = rot[1] * xRotMask[0];
        rot[2] = rot[2] * xRotMask[1];
        // Vertauschen der Längen der y- und z-Komponenten.
        invertYZ();
    }

    // Rotation um y-Achse.
    void rotY() {
        yRotMask[0] = yRotMask[0] * -1;
        yRotMask[1] = yRotMask[1] * -1;
        rot[0] = rot[0] * yRotMask[0];
        rot[2] = rot[2] * yRotMask[1];
        invertXZ();
    }

    // Rotation um z-Achse.
    void rotZ() {
        zRotMask[0] = zRotMask[0] * -1;
        zRotMask[1] = zRotMask[1] * -1;
        rot[0] = rot[0] * zRotMask[0];
        rot[1] = rot[1] * zRotMask[1];
        invertXY();
    }

    // Vertauschen der Längen der y- und z-Komponenten.
    public void invertYZ() {
        float t = diag[1]; diag[1] = diag[2]; diag[2] = t;
    }

    // Vertauschen der Längen der x- und z-Komponenten.
    public void invertXZ() {
        float t = diag[0]; diag[0] = diag[2]; diag[2] = t;
    }

    // Vertauschen der Längen der y- und x-Komponenten.
    public void invertXY() {
        float t = diag[0]; diag[0] = diag[1]; diag[1] = t;
    }

    public float[] getDiagonal() {
        return new float[] {diag[0], diag[1], diag[2]};
    }

    public String printDiagonal() {
        return "" + diag[0] + ", " + diag[1] + ", " + diag[2];
    }

    float[] getOrigin() {
        int[] mask = new int[] {0, 0, 0};
        if (rot[0] == -1) mask[0] = 1;
        if (rot[1] == -1) mask[1] = 1;
        if (rot[2] == -1) mask[2] = 1;
        return new float[] {mask[0] * diag[0],
                            mask[1] * diag[1],
                            mask[2] * diag[2]};
    }

    public float[] getAbsoluteOrigin(float[] start) {
        float[] orig = getOrigin();
        return new float[] {start[0] + orig[0],
                            start[1] + orig[1],
                            start[2] + orig[2]};
    }

    public float[] getRelativeVector() {
        return new float[] {rot[0] * diag[0],
                            rot[1] * diag[1],
                            rot[2] * diag[2]};
    }

    public String printRelVect() {
        float[] rv = getRelativeVector();
        return "" + rv[0] + ", " + rv[1] + ", " + rv[2];
    }
}
