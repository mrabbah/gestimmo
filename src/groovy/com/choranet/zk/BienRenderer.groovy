package com.choranet.zk;

import org.zkoss.zul.*;
import org.zkoss.zk.ui.event.*;
import com.choranet.omnidior.gesticom.Bien;
import com.choranet.omnidior.gesticom.PhaseWindow;

/**
 * Convert selected items to bean and vice versa.
 *
 */
public class BienRenderer implements TreeitemRenderer, EventListener/*, java.io.Serializable*/{
	
    //private static final long serialVersionUID = 1L;
    private PhaseWindow pw;
    
    public BienRenderer(PhaseWindow pw) {
        this.pw = pw;
    }
    
    public void render(Treeitem item, Object data) throws Exception {
        Bien bien = (Bien)data.getData();        
        Treerow tr = new Treerow();
        item.appendChild(tr);
        //Partie pour que les sous items se déssinne lors de la création
        //Tree tree = item.getTree();
        //tree.renderItem(item);
        //Fin partie
        tr.appendChild(new Treecell(bien.numero));
        tr.appendChild(new Treecell(bien.type.intitule));
        tr.appendChild(new Treecell(bien.statut.intitule));
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
                tree.removeItemFromSelection(item);
            } else {
                Bien b = (Bien) item.getValue().getData();
                pw.actualiserInformationBien(b)
            }
        }
    }
}
