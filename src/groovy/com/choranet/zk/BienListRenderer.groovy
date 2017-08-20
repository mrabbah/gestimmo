/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.choranet.zk

import com.choranet.omnidior.gesticom.Bien
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.ListitemRenderer;
import java.text.NumberFormat;
import java.text.DecimalFormat;

/**
 *
 * @author rabbah
 */
class BienListRenderer implements ListitemRenderer {
    
    public void render(Listitem listitem, Object obj) throws Exception {
        Bien bien = (Bien) obj;
        Listcell lc = new Listcell();
        lc.setImage("/images/skin/icon_selection.png");
        listitem.appendChild(lc);
        listitem.appendChild(new Listcell(bien.numero));
        if(bien.contenance) {
            listitem.appendChild(new Listcell(bien.contenance.toString()));            
        } else {
            listitem.appendChild(new Listcell());
        }
        NumberFormat nf = new DecimalFormat("#,##0.##");
        if(bien.prixVente) {
            listitem.appendChild(new Listcell(nf.format(bien.prixVente)));
        } else {
            listitem.appendChild(new Listcell());
        }
        if(bien.superficieConstruite) {
            listitem.appendChild(new Listcell(nf.format(bien.superficieConstruite)));            
        } else {
            listitem.appendChild(new Listcell());
        }
        if(bien.superficieVendable) {
            listitem.appendChild(new Listcell(nf.format(bien.superficieVendable)));            
        } else {
            listitem.appendChild(new Listcell());
        }
        if(bien.superficie) {
            listitem.appendChild(new Listcell(nf.format(bien.superficie)));
        } else {
            listitem.appendChild(new Listcell());
        }
        listitem.appendChild(new Listcell(bien.titreFoncier));
        if(bien.statut) {
            listitem.appendChild(new Listcell(bien.statut.toString()));            
        } else {
            listitem.appendChild(new Listcell());
        }
        listitem.setValue(obj);
    }
}

