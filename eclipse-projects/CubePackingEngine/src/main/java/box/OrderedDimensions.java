package box;

import java.util.Arrays;

/**
 * Represents the dimensions of a cuboid.
 * They are sorted for practical purposes: [a, b, c]
 * where a <= b <= c.
 * A copy of the unsorted original dimensions is kept.
 */
public class OrderedDimensions {

    // original dimensions, before sorting
    protected float[] origDims = new float[3];
    // sorted dimensions
    protected float[] sortedDims = new float[3];
    // an ID
    int id = -1;

    /**
     * Constructor
     * 
     * Makes a copy of the dimensions.
     * Then makes another copy which is sorted.
     * 
     * @param orig original triplet of cuboid dimensions
     */
    public OrderedDimensions(float[] orig) {
        // copy original
        System.arraycopy(orig, 0, origDims, 0, 3);
        // another copy to be sorted
        System.arraycopy(orig, 0, sortedDims, 0, 3);
        sortDimensions();
    }

    /**
     * Sorts the copy of the dimensions.
     */
    protected void sortDimensions() {
    	Arrays.sort(sortedDims);
    }

    /**
     * Determines if a cuboid fits into another cuboid.
     *
     * @param that other cuboid
     * @return truth value
     */
    public boolean fitsIn(OrderedDimensions that) {
        if (this.sortedDims[0] <= that.sortedDims[0]
                && this.sortedDims[1] <= that.sortedDims[1]
                && this.sortedDims[2] <= that.sortedDims[2]) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Determines if the volume of a cuboid is greater zero.
     * 
     * @return truth value
     */
    public boolean isNonZero() {
        if (origDims[0] > 0 && origDims[1] > 0 && origDims[2] > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Return a copy of the original dimensions.
     *
     * @return copy of original dimensions
     */
    public float[] getOrigDimsCopy() {
        return new float[] {origDims[0], origDims[1], origDims[2]};
    }

    /**
     * Print original dimensions.
     * 
     * FIXME Implement toString instead?
     * 
     * @return string representation of original dimensions
     */
    public String printOriginalDimensions() {
        return "" + origDims[0] + ", " + origDims[1] + ", " + origDims[2];
    }

    /**
     * Return a copy of the sorted dimensions.
     *
     * @return copy of sorted dimensions
     */
    public float[] getSortedDimsCopy() {
        return new float[] {sortedDims[0], sortedDims[1], sortedDims[2]};
    }

    /**
     * Set ID of cuboid.
     * 
     * @param i given ID
     */
    public void setID(int i) {
        id = i;
    }

    /**
     * Get ID of cuboid.
     * 
     * @return ID of cuboid
     */
    public int getID() {
        return id;
    }
}
