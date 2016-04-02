package packer;

import box.OrdDims;
import store.SituBox;
import store.Capacity;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class Packer {
    Combiner cmb;
    Capacity cap;
    Map<SituBox, CombinerBox> msb = new HashMap<SituBox, CombinerBox>(); // map of situated boxes
    List<OrdDims> lib = new ArrayList<OrdDims>(); // list of ignored boxes
    // Take container dimensions.
    // Take list of box dimensions.
    public Packer(OrdDims cd, List<OrdDims> ld) {
        cmb = new Combiner(ld);
        cap = new Capacity(cd.getDims());
    }
    void cancelPacking() {
        cmb.unpackAll();
        while (cmb.hasMoreElements()) {
            lib.add(cmb.nextElement());
        }

    }
    public List<SituBox> getSituatedBoxes() {
        Visitor v;
        boolean fit;
        OrdDims bfd;
        CombinerBox cb;
        while (cmb.hasMoreElements()) {
            SortedFreeDims sfd = new SortedFreeDims(cap.getFreeDimensions(),
                    SortedFreeDims.Order.SUM_OF_EDGES);
            if (!sfd.hasMoreElements()) {
                cancelPacking();
                break;
            }
            cb = cmb.nextElement();
            fit = false;
            while (sfd.hasMoreElements()) {
                bfd = sfd.nextElement();
                if (cb.fitsIn(bfd)) {
                    fit = true;
                    break;
                }
            }
            if (fit) {
                SituBox sb = cap.add(cb);
                if (sb == null) throw new RuntimeException(
                        "Box fits in free dimensions but Capacity can't place it!");
                cb.rotateTo(sb);
                msb.put(sb, cb);
            }
            else if (cb.hasContent()) cmb.unpack(cb);
            else lib.add(cb);
        }
        v = new Visitor(msb);
        return v.getSituatedBoxes();
    }
    public List<OrdDims> getIgnoredBoxes() {
        return lib;
    }
}
