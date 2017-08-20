
package com.choranet.zk;

import com.choranet.omnidior.gesticom.*;
import java.util.List;
import org.zkoss.zk.ui.HtmlMacroComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zul.*;
import com.choranet.zk.BienManager;

/**
 *
 * @author rabbah
 */
public class MiniBienManager extends HtmlMacroComponent {

  

    Combobox lstBien;
   
    //Données
    List<Bien> biens;
    Bien bienParentSelected

    List<Bien> bienParents;
    List<Bien> bienParentsSelected;
    Projet projet;
    Type type;
    Type typeParent;
    //Service

    BienService bs;
    StatutService ss;
        

    MiniBienManager bmFils
    BienManager bBmFils
    
    public MiniBienManager(Projet pj, Type type, Type typeParent, BienService bs, StatutService ss) throws Exception {
        
        this.projet = pj;
        this.type = type;
        this.typeParent = typeParent;
        this.bs = bs;
        this.ss = ss;
        //
        compose();
        
        //chargement des données
        // contenances = (List<Contenance>) cs.getContenancecByType(type);
        biens = (List<Bien>) bs.getBiensByProjetAndType(projet, type);

        //bienParents = (List<Bien>) bs.getBiensByProjetAndType(projet, typeParent);

        //cocontenances.setModel(new ListModelList(contenances));
        //cobienParents.setModel(new ListModelList(bienParents));
        //lstBien.setItemRenderer(new BienListRenderer());
        lstBien.setModel(new ListModelList(biens));

        if(bmFils) {
            bmFils.bienParentsSelected = biens;
        }
    }
    
    public void rafraichirBiensParent() {
        bienParents = (List<Bien>) bs.getBiensByProjetAndType(projet, typeParent);
        cobienParents.setModel(new ListModelList(bienParents));
    }
    public void onSelect$lstBien() throws InterruptedException {
        Bien b = (Bien) lstBien.getSelectedItem().getValue();

        //        if (b.bienParent) {
        //            def index = 0
        //            def existBien = false
        //            for(bi in bienParents) {
        //                if(bi.id == b.bienParent.id) {
        //                    existBien = true
        //                    break;
        //                }
        //                index++
        //            }
        //            if(existBien) {
        //                cobienParents.setSelectedIndex(index);                
        //            } else {
        //                cobienParents.setSelectedItem(null)
        //            }
        //        } else {
        //            cobienParents.setSelectedItem(null)
        //        }
        
        //Filtrer selon le bien parent selectionner
        if(bmFils) {
            bmFils.bienParentSelected = b
            bmFils.filtrer(true)
        }

        if (bBmFils){
            bBmFils.bienParentSelected = b
            bBmFils.filtrer(true)
        }        
    }
   
    
    def filtrer(recurcivement = false) {
        biens = bs.filtrerBiens(bienParentSelected,bienParentsSelected,"libre",
            projet, type)
        lstBien.setModel(new ListModelList(biens));  
        nettoyerChamps();
        if(recurcivement && bmFils) {
            bmFils.bienParentsSelected = biens;
            bmFils.filtrer(true)
        }
    }
       
    private void nettoyerChamps() {
        //lstBien.clearSelection();        
        //cobienParents.setSelectedItem(null)
    }
    
}
