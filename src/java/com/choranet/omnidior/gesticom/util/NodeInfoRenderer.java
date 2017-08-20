/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.choranet.omnidior.gesticom.util;

import org.zkoss.zul.*;

/**
 *
 * @author kamal
 */
public class NodeInfoRenderer implements TreeitemRenderer {

    public void render(Treeitem trtm, Object o) throws Exception {
        BienData dt = (BienData) o;//.getData();
        Treerow tr = new Treerow();
        trtm.appendChild(tr);
        tr.appendChild(new Treecell(dt.bien));
        tr.appendChild(new Treecell(dt.etat));
        throw new UnsupportedOperationException("Not supported yet.");
    }

//    public void render(Treeitem item, Object data) throws Exception {
//        item.setValue(data);
//
//        // Construct treecells
//        Treecell tcNamn = new Treecell(data.toString());
//        Treerow tr = null;
//
//        if (item.getTreerow() == null) {
//            tr = new Treerow();
//            tr.setParent(item);
//        } else {
//            tr = item.getTreerow();
//            tr.getChildren().clear();
//        }
//        // Attach treecells to treerow
//        tcNamn.setParent(tr);
//
//
//    }
}
