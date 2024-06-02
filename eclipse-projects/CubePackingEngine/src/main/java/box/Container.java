package box;

/**
 * Quader, der zwei andere Quader enthalten kann.
 */
public class Container extends PackBox {

    // Haben wir Inhalt?
    boolean hasContent = false;
    // Die enthaltenen Quader.
    protected Container c1, c2;
    // Vektoren, die auf die Ursprünge der eingebetteten Quader
    // zeigen, vom Ursprung dieses Quaders aus gesehen.
    protected float[] v1, v2;

    public Container(float[] d) {
        super(d);
    }

    // Konstruktion mit zwei eingebetteten Quadern.
    public Container(Container a, Container b) {
        // Initialisieren der originalen u. sortierten Dimensionen
        // sowie des Vektors.
        this(new float[] {0.0f, 0.0f, 0.0f});
        hasContent = true;

        // Integration der enthaltenen Quader und Optimieren ihrer
        // Rotation, so dass ihre größten Flächen aneinander liegen.
        // So wird das Volumen ihres Außenquaders minimiert.
        c1 = a; c1.parent = this; c1.optimizeOrientation();
        c2 = b; c2.parent = this; c2.optimizeOrientation();

        // Dieser Quader ist der Außenquader der enthaltenen.
        // Anpassen seiner Dimensionen u. seines Vektors.
        float[] d1 = c1.getDiagonal();
        float[] d2 = c2.getDiagonal();
        origDims[0] = Math.max(d1[0], d2[0]);
        origDims[1] = Math.max(d1[1], d2[1]);
        origDims[2] = d1[2] + d2[2];
        System.arraycopy(origDims, 0, sortedDims, 0, 3);
        sortDimensions();
        System.arraycopy(origDims, 0, diag, 0, 3);

        // Ursprünge der enthaltenen Quader.
        v1 = new float[] {0.0f, 0.0f, 0.0f};
        v2 = new float[] {0.0f, 0.0f, d1[2]};
    }

    // Bei Rotation des enthaltenden Quaders den Ursprung
    // des zweiten enthaltenen Quaders anpassen.
    @Override
    public void invertYZ() {
        super.invertYZ();
        if (hasContent) {
            float t = v2[1]; v2[1] = v2[2]; v2[2] = t;
        }
    }
    @Override
    public void invertXZ() {
        super.invertXZ();
        if (hasContent) {
            float t = v2[0]; v2[0] = v2[2]; v2[2] = t;
        }
    }
    @Override
    public void invertXY() {
        super.invertXY();
        if (hasContent) {
            float t = v2[1]; v2[1] = v2[0]; v2[0] = t;
        }
    }

    public boolean hasContent() {
        return hasContent;
    }

    // Enthaltene Quader auspacken.
    public Container[] unpack() {
        c1.parent = null; c2.parent = null;
        return new Container[] {c1, c2};
    }
}
