package com.choranet.zk;

import org.zkoss.zul.*;
import org.zkoss.zk.ui.event.*;
//import com.choranet.securite.DroitUtilisateur;
import com.choranet.omnidior.gesticom.DroitUtilisateur;
//
///**
// * Convert selected items to bean and vice versa.
// *
// */
public class DroitRenderer implements TreeitemRenderer, EventListener/*, java.io.Serializable*/{
	
    //private static final long serialVersionUID = 1L;
    
    public void render(Treeitem item, Object data) throws Exception {
        DroitUtilisateur du = (DroitUtilisateur)data.getData();        
        Treerow tr = new Treerow();
        item.appendChild(tr);
        //Partie pour que les sous items se déssinne lors de la création
        Tree tree = item.getTree();
        tree.renderItem(item);
        //Fin partie
        tr.appendChild(new Treecell(du.description));
        item.setValue(data)
        item.addEventListener("onClick", this)
    }
    
    public void onEvent(Event e) throws Exception {
        //ON CLICK
        if (e.getName().equals("onClick")) {
            Treeitem item = (Treeitem) e.getTarget();
            Tree tree = item.getTree();
            
            Treechildren tc = item.getTreechildren();
            if(tc) {
                Collection<Treeitem> treeitems = tc.getItems()
                for(Treeitem ti in treeitems) {
                    if(item.isSelected()) {
                        tree.addItemToSelection(ti)
                    } else {
                        tree.removeItemFromSelection(ti)
                    }
                }
            }
            
        }
    }
}
