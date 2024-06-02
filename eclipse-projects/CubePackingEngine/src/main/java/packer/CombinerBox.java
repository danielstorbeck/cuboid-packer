package packer;

import box.OrderedDimensions;
import box.Container;

/**
 * A cuboid that combines two smaller cuboids.
 * It can be queried for two values that show how efficiently
 * the two component cuboids use the space of the combining cuboid.
 */
public class CombinerBox extends Container {

    /**
     * Constructor that creates a cuboid container from a simple cuboid.
     * 
     * @param cuboidDimensions dimensions of the simple cuboid
     */
    public CombinerBox(OrderedDimensions cuboidDimensions) {
        super(cuboidDimensions.getOrigDimsCopy());
    }

    /**
     * Constructor that combines two simple or combined cuboids.
     * 
     * @param cuboid1
     * @param cuboid2
     */
    public CombinerBox(CombinerBox cuboid1, CombinerBox cuboid2) {
        super(cuboid1, cuboid2);
    }

    /**
     * Get the dimensions of the greatest side.
     * 
     * @return pair of dimensions
     */
    public float[] getGreatestSurfaceDimensions() {
        // dimensions are ordered ascendingly, so we take the last two
        return new float[] {sortedDims[1], sortedDims[2]};
    }

    /**
     * Get the area of the greatest side.
     * 
     * @param area
     */
    public float getGreatestSurfaceArea() {
        // dimensions are ordered ascendingly, so we take the last two
        return sortedDims[1] * sortedDims[2];
    }

    /**
     * Two cuboids are joined by putting them side by side using
     * their greatest sides. Here the degree of congruence of these
     * sides is calculated. A value of 1 is complete congruence.
     * A value approximating 0 approximates no overlap at all.
     */
    public float getDegreeOfCongruenceWith(CombinerBox that) {
        float greaterArea = Math.max(this.getGreatestSurfaceArea(),
                             that.getGreatestSurfaceArea());
        float[] greatestDimensions1 = this.getGreatestSurfaceDimensions();
        float[] greatestDimensions2 = that.getGreatestSurfaceDimensions();
        float intersection = 
            Math.min(greatestDimensions1[0], greatestDimensions2[0]) 
            * Math.min(greatestDimensions1[1], greatestDimensions2[1]);
        return intersection / greaterArea;
    }

    /**
     * Two cuboids are joined by putting them side by side using their
     * greatest sides. They are put inside a container that is just big
     * enough so they both fit. Here the degree of space usage inside
     * the container is calculated. For example if one cuboid is small
     * but the other one is large, then free space remains in the container.
     * A value of 1 means no free space, i.e. optimal space usage.
     * A value approximating 0 means suboptimal space usage.
     */
    public float getDegreeOfSpaceUsage(CombinerBox that) {
        float[] dimensions1 = this.getSortedDimsCopy();
        float[] dimensions2 = that.getSortedDimsCopy();
        float[] containerDimensions = new float[] {
          Math.max(dimensions1[1], dimensions2[1]),
          Math.max(dimensions1[2], dimensions2[2]),
          dimensions1[0] + dimensions2[0]
        };
        float volume1 = dimensions1[0] * dimensions1[1] * dimensions1[2];
        float volume2 = dimensions2[0] * dimensions2[1] * dimensions2[2];
        float containerVolume = 
            containerDimensions[0] * containerDimensions[1] * containerDimensions[2];
        return (volume1 + volume2) / containerVolume;
    }

    public void acceptVisitor(Visitor v, float[] orig) {
        v.processBox(this, orig);
    }

    public void delegateVisitor(Visitor v, float[] orig) {
        ((CombinerBox)c1).acceptVisitor(v, new float[] {
                    orig[0] + v1[0], orig[1] + v1[1], orig[2] + v1[2]});
        ((CombinerBox)c2).acceptVisitor(v, new float[] {
                    orig[0] + v2[0], orig[1] + v2[1], orig[2] + v2[2]});
    }
}
