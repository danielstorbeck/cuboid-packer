/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package packer;

import store.SituatedBox;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author daniel
 */
public class Visitor {
    Map<SituatedBox, CombinerBox> mb;
    List<SituatedBox> lsb = new ArrayList<SituatedBox>();
    public Visitor(Map<SituatedBox, CombinerBox> m) {
        mb = m;
    }
    public List<SituatedBox> getSituatedBoxes() {
        for (SituatedBox sb : mb.keySet()) {
            CombinerBox cb = mb.get(sb);
            cb.acceptVisitor(this, sb.getOrigin());
        }
        return lsb;
    }
    void processBox(CombinerBox cb, float[] orig) {
        cb.performAncestorRotations();
        float[] aOrig = cb.getAbsoluteOrigin(orig);
        if (cb.hasContent()) {
            cb.delegateVisitor(this, orig);
        }
        else {
            SituatedBox sb = new SituatedBox(cb.getRelativeVector(), aOrig);
            lsb.add(sb);
        }
    }
}
