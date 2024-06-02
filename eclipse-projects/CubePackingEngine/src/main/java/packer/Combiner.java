package packer;

import box.OrderedDimensions;
import box.Container;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collections;

/**
 * Sorts and recursively combines cuboids to bigger ones.
 * The set of cuboids can be iterated over with hasNext and next.
 * If combined cuboids are too big, they can be undone.
 */
public class Combiner implements Enumeration<CombinerBox> {

    List<CombinerBox> combinableCuboids = new ArrayList<CombinerBox>();
    BiggestSurfaceAreaOrder sortingCriterion = new BiggestSurfaceAreaOrder();

    /**
     * constructor
     * 
     * @param cuboidDimensions
     */
    public Combiner(List<OrderedDimensions> cuboidDimensions) {
        // create combinable cuboids from cuboid dimensions
        for (OrderedDimensions cuboidDims : cuboidDimensions) {
        	combinableCuboids.add(new CombinerBox(cuboidDims));
        }
        Collections.sort(combinableCuboids, sortingCriterion);
        // first combination run
        combine();
    }

    public boolean hasMoreElements() {
        return combinableCuboids.size() > 0 ? true : false;
    }

    public CombinerBox nextElement() {
        if (combinableCuboids.size() < 1) {
        	throw new NoSuchElementException();
        }
        return combinableCuboids.remove(combinableCuboids.size() - 1);
    }

    /**
     * A pair of indices into a list of cuboids
     * that point to a pair of cuboids
     * that are candidates for combination.
     */
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

    /**
     * Combines cuboids into bigger ones provided that there is more than one cuboid
     * in the list and that candidates for combination can be found.
     */
    void combine() {
        boolean goOn = true;
        PairOfIndices indexPair;
        if (combinableCuboids.size() < 2) return;
        while (goOn) {
            indexPair = findGoodCombination();
            if (indexPair == null) {
                goOn = false;
                continue;
            }
            else combinePair(indexPair);
        }
    }

    /**
     * Combines a candidate pair of cuboids.
     * 
     * @param indexPair candidate pair of cuboids for combination
     */
    void combinePair(PairOfIndices indexPair) {
        // use indices to get candidates from list
        CombinerBox b1 = combinableCuboids.get(indexPair.getFirst());
        CombinerBox b2 = combinableCuboids.get(indexPair.getSecond());
        // remove them from list
        combinableCuboids.remove(b1);
        combinableCuboids.remove(b2);
        // combine them
        CombinerBox cb = new CombinerBox(b1, b2);
        // add combined cuboid to list
        combinableCuboids.add(cb);
        // sort list
        Collections.sort(combinableCuboids, sortingCriterion);
    }

    /**
     * Finds a candidate pair of cuboids for combination.
     * 
     * FIXME The cuboids are compared with two criteria.
     * 1. Degree of space usage inside their outer cuboid.
     * 2. Degree of congruence of the cuboids' greatest sides.
     * As both correlate, one of them should be enough.
     * So throw one of them out.
     * 
     * FIXME java 8: return optional
     * 
     * FIXME This is maximum function over all pairs of a set: parallelize.
     * 
     * FIXME An epsilon is used: make configurable.
     */
    PairOfIndices findGoodCombination() {
        int firstIndex = 0;
        int secondIndex = 0;
        final int lastIndex = combinableCuboids.size() - 1;
        // try to optimize space usage and / or congruence
        float maximumSpaceUsage = 0;
        float maximumCongruence = 0;
        // remember cuboid pairs that have good values
        PairOfIndices spaceUsagePair = null;
        PairOfIndices congruencePair = null;
        // do noting if less than two cuboids available
        // FIXME java 8: optional
        if (combinableCuboids.size() < 2) return null;
        while (firstIndex < lastIndex) {
            secondIndex = firstIndex + 1;
            while (secondIndex <= lastIndex) {
                CombinerBox cuboid1 = combinableCuboids.get(firstIndex);
                CombinerBox cuboid2 = combinableCuboids.get(secondIndex);
                // try to optimize
                float spaceUsage = cuboid1.getDegreeOfSpaceUsage(cuboid2);
                float congruence = cuboid1.getDegreeOfCongruenceWith(cuboid2);
                if (spaceUsage > maximumSpaceUsage) {
                    maximumSpaceUsage = spaceUsage;
                    spaceUsagePair = new PairOfIndices(firstIndex, secondIndex);
                }
                if (congruence > maximumCongruence) {
                    maximumCongruence = congruence;
                    congruencePair = new PairOfIndices(firstIndex, secondIndex);
                }
                // return optimal pair
                if (maximumSpaceUsage == 1.0 || maximumCongruence == 1.0) break;
                secondIndex = secondIndex + 1;
            }
            // return optimal pair
            if (maximumSpaceUsage == 1.0 || maximumCongruence == 1.0) break;
            firstIndex = firstIndex + 1;
        }
        // return nothing if space usage and congruence are both bad
        // FIXME make epsilon configurable
        if (maximumSpaceUsage < 0.75 && maximumCongruence < 0.75) return null;
        // return optimal pair
        else if (maximumSpaceUsage == 1.0) return spaceUsagePair;
        else if (maximumCongruence == 1.0) return congruencePair;
        else if (maximumSpaceUsage > maximumCongruence) return spaceUsagePair;
        else return congruencePair;
    }

    /**
     * Unpack a combined pair of cuboids.
     */
    public void unpack(CombinerBox combinedCuboid) {
        Container[] componentCuboids = combinedCuboid.unpack();
        CombinerBox cuboid1 = (CombinerBox) componentCuboids[0];
        CombinerBox cuboid2 = (CombinerBox) componentCuboids[1];
        combinableCuboids.add(cuboid1);
        combinableCuboids.add(cuboid2);
        Collections.sort(combinableCuboids, sortingCriterion);
    }

    /**
     * Unpack all combined cuboids.
     */
    public void unpackAll() {
        List<CombinerBox> tempList = new ArrayList<CombinerBox>();
        // put all combined cuboids into the temporary list
        for (CombinerBox cb : combinableCuboids) {
            if (cb.hasContent()) {
                tempList.add(cb);
            }
        }
        // remove all combined cuboids from the list of combinable cuboids
        for (CombinerBox cb : tempList) {
            combinableCuboids.remove(cb);
        }
        while (tempList.size() > 0) {
            CombinerBox combinedCuboid = tempList.remove(tempList.size() - 1);
            // simple cuboids go back to the list of combinable cuboids
            // TODO this is probably redundant to what else already does
            if (!combinedCuboid.hasContent()) {
                combinableCuboids.add(combinedCuboid);
            }
            // combined cuboids are unpacked
            else {
                Container[] componentCuboids = combinedCuboid.unpack();
                CombinerBox cuboid1 = (CombinerBox) componentCuboids[0];
                CombinerBox cuboid2 = (CombinerBox) componentCuboids[1];
                if (cuboid1.hasContent()) tempList.add(cuboid1);
                else combinableCuboids.add(cuboid1);
                if (cuboid2.hasContent()) tempList.add(cuboid2);
                else combinableCuboids.add(cuboid2);
            }
        }
    }
}

/**
 * Sorting criterion for cuboids.
 * Sorts ascendingly by greatest area of a cuboids sides.
 */
class BiggestSurfaceAreaOrder implements Comparator<CombinerBox> {
    @Override
    public int compare(CombinerBox cuboid1, CombinerBox cuboid2) {
        float area1 = cuboid1.getGreatestSurfaceArea();
        float area2 = cuboid2.getGreatestSurfaceArea();
        if (area1 == area2) return 0;
        else if (area1 < area2) return -1;
        else return 1;
    }
}