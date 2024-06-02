package packer;

import box.OrderedDimensions;
import store.SituatedBox;
import store.Capacity;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class Packer {
    Combiner combiner;
    Capacity capacity;
    Map<SituatedBox, CombinerBox> situatedBoxes = new HashMap<SituatedBox, CombinerBox>();
    List<OrderedDimensions> ignoredBoxes = new ArrayList<OrderedDimensions>();

    /**
     * Constructor
     * 
     * @param containerDimensions
     * @param cuboids
     */
    public Packer(OrderedDimensions containerDimensions, List<OrderedDimensions> cuboids) {
        combiner = new Combiner(cuboids);
        capacity = new Capacity(containerDimensions.getOrigDimsCopy());
    }

    void cancelPacking() {
        combiner.unpackAll();
        while (combiner.hasMoreElements()) {
            ignoredBoxes.add(combiner.nextElement());
        }

    }
    public List<SituatedBox> getSituatedBoxes() {
        Visitor v;
        boolean fit;
        OrderedDimensions bfd;
        CombinerBox cb;
        while (combiner.hasMoreElements()) {
            SortedFreeDims sfd = new SortedFreeDims(capacity.getFreeDimensions(),
                    SortedFreeDims.Order.SUM_OF_EDGES);
            if (!sfd.hasMoreElements()) {
                cancelPacking();
                break;
            }
            cb = combiner.nextElement();
            fit = false;
            while (sfd.hasMoreElements()) {
                bfd = sfd.nextElement();
                if (cb.fitsIn(bfd)) {
                    fit = true;
                    break;
                }
            }
            if (fit) {
                SituatedBox sb = capacity.add(cb);
                if (sb == null) throw new RuntimeException(
                        "Box fits in free dimensions but Capacity can't place it!");
                cb.rotateTo(sb);
                situatedBoxes.put(sb, cb);
            }
            else if (cb.hasContent()) combiner.unpack(cb);
            else ignoredBoxes.add(cb);
        }
        v = new Visitor(situatedBoxes);
        return v.getSituatedBoxes();
    }
    public List<OrderedDimensions> getIgnoredBoxes() {
        return ignoredBoxes;
    }
}
