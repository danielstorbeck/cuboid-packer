/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package packer;

import store.SituBox;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author daniel
 */
public class Visitor {
    Map<SituBox, CombinerBox> mb;
    List<SituBox> lsb = new ArrayList<SituBox>();
    public Visitor(Map<SituBox, CombinerBox> m) {
        mb = m;
    }
    public List<SituBox> getSituatedBoxes() {
        for (SituBox sb : mb.keySet()) {
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
            SituBox sb = new SituBox(cb.getRelativeVector(), aOrig);
            lsb.add(sb);
        }
    }
}
