package store;

import box.OrderedDimensions;

/**
 * A cuboid that is situated in a container.
 * It has an origin (a point with x, y and z coordinates)
 * and a set of dimensions.
 */
public class SituatedBox extends OrderedDimensions {

    float[] origin = new float[3];

    /**
     * Constructor
     * 
     * Makes a copy of origin and dimensions
     * 
     * @param dimensions original dimensions
     * @param origin point of origin
     */
    public SituatedBox(float[] dimensions, float[] origin) {
        super(dimensions);
        System.arraycopy(origin, 0, this.origin, 0, 3);
    }

    /**
     * Get the point of origin.
     * 
     * FIXME Why not return a copy?
     * The base class returns a copy of the dimensions.
     * 
     * @return point of origin
     */
    public float[] getOrigin() {
        return origin;
    }

    /**
     * Print the point of oirigin.
     * 
     * FIXME Why not implement toString?
     * 
     * @return string representing the point of origin
     */
    public String printOrigin() {
        return "" + origin[0] + ", " + origin[1] + ", " + origin[2];
    }
}
