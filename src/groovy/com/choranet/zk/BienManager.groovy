
package com.choranet.zk;

import com.choranet.omnidior.gesticom.*;
import java.util.List;
import org.zkoss.zk.ui.HtmlMacroComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zul.*;
import org.codehaus.groovy.grails.plugins.jasper.JasperExportFormat
import org.codehaus.groovy.grails.plugins.jasper.JasperReportDef

import org.zkoss.util.media.AMedia;
import org.zkoss.util.media.Media;
import java.util.HashMap;
import java.util.Map;
import org.zkoss.zk.ui.Executions;

/**
 *
 * @author rabbah
 */
public class BienManager extends HtmlMacroComponent {

    Button btnSave
    Button btnUpdate
    Button btnDelete
    Button btnCancel
    
    Textbox fieldNumero;
    Textbox fieldTitreFoncier;
    Label fieldStatut;
    Doublebox fieldPrixVente;
    Doublebox fieldSuperficieConstruite;
    Doublebox fieldSuperficieVendable;
    Doublebox fieldSuperficie;
    Textbox fieldContenance; 
    //Combobox cocontenances;
    Combobox cobienParents;
    Listbox lstBien;
    Textbox filterNumero;
    Textbox filterTitreFoncier;
    Textbox filterStatut;
    Textbox filterContenance;
    Doublebox filterPrixVente;
    Doublebox filterSuperficieConstruite;
    Doublebox filterSuperficieVendable;
    Doublebox filterSuperficie;
    
    Listheader hnumero;
    Listheader htitreFoncier;
    Listheader hcontenance;
    Listheader hstatut;
    Listheader hprixVente;
    Listheader hsuperficieConstruite;
    Listheader hsuperficieVendable;
    Listheader hsuperficie;
    //Données
    List<Bien> biens;
    Bien bienParentSelected
    //    List<Contenance> contenances;
    List<Bien> bienParents;
    List<Bien> bienParentsSelected;
    Projet projet;
    Type type;
    Type typeParent;
    //Service
    ContenanceService cs;
    BienService bs;
    StatutService ss;
        
    String sortedDirection;
    String attributToSort;

    def numero
    def contenance
    def prixVente
    def superficieConstruite
    def SuperficieVendable
    def superficie
    def titreFoncier
    def statut
        
    BienManager bmFils
    
    def exportFormat
    String extension
    String strAppExt  
    def jasperService
    
    public BienManager(Projet pj, Type type, Type typeParent, ContenanceService cs, BienService bs, StatutService ss, js = null) throws Exception {
        this.projet = pj;
        this.type = type;
        this.typeParent = typeParent;
        this.cs = cs;
        this.bs = bs;
        this.ss = ss;
        if (js){
            this.jasperService = js
        }
        //
        compose();
        //chargement des données
        // contenances = (List<Contenance>) cs.getContenancecByType(type);
        biens = (List<Bien>) bs.getBiensByProjetAndType(projet, type);
        bienParents = (List<Bien>) bs.getBiensByProjetAndType(projet, typeParent);

        //cocontenances.setModel(new ListModelList(contenances));
        cobienParents.setModel(new ListModelList(bienParents));  
        lstBien.setItemRenderer(new BienListRenderer());
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
        fieldNumero.setValue(b.getNumero());
        fieldPrixVente.setValue(b.getPrixVente());
        fieldStatut.setValue(b.statut.toString());
        fieldSuperficie.setValue(b.getSuperficie());
        fieldSuperficieConstruite.setValue(b.getSuperficieConstruite());
        fieldSuperficieVendable.setValue(b.getSuperficieVendable());
        fieldTitreFoncier.setValue(b.getTitreFoncier());
        fieldContenance.setValue(b.getContenance());
        if (b.bienParent) {
            def index = 0
            def existBien = false
            for(bi in bienParents) {
                if(bi.id == b.bienParent.id) {
                    existBien = true
                    break;
                }
                index++
            }
            if(existBien) {
                cobienParents.setSelectedIndex(index);                
            } else {
                cobienParents.setSelectedItem(null)
            }
        } else {
            cobienParents.setSelectedItem(null)
        }
        //        if(b.contenance) {
        //            def index = 0
        //            def existCont = false
        //            for(c in contenances) {
        //                if(c.id == b.contenance.id) {
        //                    existCont = true
        //                    break;
        //                }
        //                index++
        //            }
        //            if(existCont) {
        ////                cocontenances.setSelectedIndex(index);                
        //            } else {
        ////                cocontenances.setSelectedItem(null)
        //            }
        //        } else {
        ////            cocontenances.setSelectedItem(null)
        //        }
        btnSave.setVisible(false)
        btnUpdate.setVisible(true)
        btnDelete.setVisible(true)
        btnCancel.setVisible(true)
        
        //Filtrer selon le bien parent selectionner
        if(bmFils) {
            bmFils.bienParentSelected = b
            bmFils.filtrer(true)
        }        
    }

    public void onClick$btnSave() throws InterruptedException {

        Bien b = new Bien();
        b.setNumero(fieldNumero.getValue());
        b.setPrixVente(fieldPrixVente.getValue());
        b.setSuperficie(fieldSuperficie.getValue());
        b.setSuperficieConstruite(fieldSuperficieConstruite.getValue());
        b.setSuperficieVendable(fieldSuperficieVendable.getValue());
        b.setTitreFoncier(fieldTitreFoncier.getValue());
        b.setContenance(fieldContenance.getValue());
        //        Contenance c = null;
        //        if (cocontenances.getSelectedIndex() >= 0) {
        //            c = contenances.get(cocontenances.getSelectedIndex());
        //        }
        Bien bienParent = null;
        if (cobienParents.getSelectedIndex() >= 0) {
            bienParent = bienParents.get(cobienParents.getSelectedIndex());
        }
                
        Boolean exist = bs.existeBienCode (b,projet.code)
        if (!exist){   
            b = (Bien) bs.partialSave(b, projet, type, bienParent, ss.getByCode("04"));
            biens.add(b);
            lstBien.setModel(new ListModelList(biens));
            nettoyerChamps();
            if(bmFils) {
                bmFils.rafraichirBiensParent();
                bmFils.bienParentsSelected = biens;        
            }
        }
        else{
            Messagebox.show("Le code "+ b.numero + " existe dans le projet " + projet.toString(), "Erreur", Messagebox.OK, Messagebox.EXCLAMATION)
        }
    }

    public void onClick$btnUpdate() throws InterruptedException {
        Bien b = (Bien) lstBien.getSelectedItem().getValue();
        b.setNumero(fieldNumero.getValue());
        b.setPrixVente(fieldPrixVente.getValue());
        b.setSuperficie(fieldSuperficie.getValue());
        b.setSuperficieConstruite(fieldSuperficieConstruite.getValue());
        b.setSuperficieVendable(fieldSuperficieVendable.getValue());
        b.setTitreFoncier(fieldTitreFoncier.getValue());
        b.setContenance(fieldContenance.getValue());
        Boolean exist =  bs.existeBienCode (b,projet.code,true)
        if (!exist){
            

            //            Contenance c = null;
            //            if (cocontenances.getSelectedIndex() >= 0) {
            //                c = contenances.get(cocontenances.getSelectedIndex());
            //            }
            Bien bienParent = null;
            if (cobienParents.getSelectedIndex() >= 0) {
                bienParent = bienParents.get(cobienParents.getSelectedIndex());
            }
            b.setBienParent(bienParent) 
            b = (Bien) bs.update(b);
            lstBien.setModel(new ListModelList(biens));        
            nettoyerChamps();
            if(bmFils) {
                bmFils.rafraichirBiensParent();
            }
        }
        else{
            Messagebox.show("Le code "+ b.numero + " existe dans le projet " + projet.toString(), "Erreur", Messagebox.OK, Messagebox.EXCLAMATION)
        }
    }

    public void onClick$btnDelete() throws InterruptedException {        
        Bien b = (Bien) lstBien.getSelectedItem().getValue();
        biens.remove(b)
        lstBien.setModel(new ListModelList(biens));
        bs.delete(b);
        nettoyerChamps();
        if(bmFils) {
            bmFils.rafraichirBiensParent();
            bmFils.bienParentsSelected = biens;        
        }
    }
    
    public void onClick$btnCancel() throws InterruptedException {        
        nettoyerChamps();
        if(bmFils) {
            bmFils.bienParentSelected = null
            bmFils.filtrer(true)
        }  
    }

    def chargerValeurFiltre() {
        numero = filterNumero.getValue();
        contenance = filterContenance.getValue();   
        if(filterPrixVente.getValue() && !filterPrixVente.getValue().equals(""))
        prixVente = Double.valueOf(filterPrixVente.getValue());
        if(superficieConstruite.getValue() && !superficieConstruite.getValue().equals(""))
        superficieConstruite = Double.valueOf(filterSuperficieConstruite.getValue());
        if(SuperficieVendable.getValue() && !SuperficieVendable.getValue().equals(""))
        SuperficieVendable = Double.valueOf(filterSuperficieVendable.getValue());
        if(superficie.getValue() && !superficie.getValue().equals(""))
        superficie = Double.valueOf(filterSuperficie.getValue());
        titreFoncier = filterTitreFoncier.getValue()
        statut = filterStatut.getValue();
    }
    
    def filtrer(recurcivement = false) {
        biens = bs.filtrerBiens(bienParentSelected,bienParentsSelected, numero, contenance, prixVente, superficieConstruite
            , SuperficieVendable, superficie, titreFoncier, statut, attributToSort, sortedDirection,
            projet, type)
        lstBien.setModel(new ListModelList(biens));  
        nettoyerChamps();
        if(recurcivement && bmFils) {
            bmFils.bienParentsSelected = biens;
            bmFils.filtrer(true)
        }
    }
    
    public void onOK$filterNumero(Event evt) throws InterruptedException {
        //chargerValeurFiltre();
        numero = filterNumero.getValue()
        filtrer(); 
    }

    public void onOK$filterTitreFoncier(Event evt) throws InterruptedException {
        //chargerValeurFiltre();
        titreFoncier = filterTitreFoncier.value
        filtrer(); 
    }

    public void onOK$filterStatut(Event evt) throws InterruptedException {
        //chargerValeurFiltre();
        statut = filterStatut.value
        filtrer(); 
    }

    public void onOK$filterPrixVente(Event evt) throws InterruptedException {        
        //chargerValeurFiltre();
        if(filterPrixVente.value && !filterPrixVente.value.equals("")) {
            prixVente = Double.valueOf(filterPrixVente.value)
        } else {
            prixVente = null
        }
        filtrer(); 
    }

    public void onOK$filterSuperficieConstruite(Event evt) throws InterruptedException {
        //chargerValeurFiltre();
        if(filterSuperficieConstruite.value && !filterSuperficieConstruite.value.equals("")) {
            superficieConstruite = Double.valueOf(filterSuperficieConstruite.value)
        } else {
            superficieConstruite = null
        }
        filtrer(); 
    }

    public void onOK$filterSuperficieVendable(Event evt) throws InterruptedException {
        //chargerValeurFiltre();
        if(filterSuperficieVendable.value && !filterSuperficieVendable.value.equals("")) {
            SuperficieVendable = Double.valueOf(filterSuperficieVendable.value)
        } else {
            SuperficieVendable = null
        }
        filtrer(); 
    }

    public void onOK$filterSuperficie(Event evt) throws InterruptedException {
        //chargerValeurFiltre();
        if(filterSuperficie.value && !filterSuperficie.value.equals("")) {
            superficie = Double.valueOf(filterSuperficie.value)
        } else {
            superficie = null
        }
        filtrer(); 
    }

    public void onOK$filterContenance(Event evt) throws InterruptedException {
        //chargerValeurFiltre();
        contenance = filterContenance.value
        filtrer(); 
    }

    def trier(ForwardEvent evt) {
        String sortedHeader = evt.origin.target.id
        attributToSort = sortedHeader.substring(1)
        def sortDirection =  evt.origin.target.sortDirection
        sortedDirection = "asc"
        if(sortDirection == "natural" || sortDirection == "descending") {
            sortedDirection = "desc"
        }
        filtrer(); 
    }
    public void onSort$hnumero(ForwardEvent evt) throws InterruptedException {
        trier(evt)
    }

    public void onSort$htitrefoncier(ForwardEvent evt) throws InterruptedException {
        trier(evt)
    }

    public void onSort$hstatut(ForwardEvent evt) throws InterruptedException {
        trier(evt)
    }

    public void onSort$hprixVente(ForwardEvent evt) throws InterruptedException {
        trier(evt)
    }

    public void onSort$hsuperficieConstruite(ForwardEvent evt) throws InterruptedException {
        trier(evt)
    }

    public void onSort$hsuperficieVendable(ForwardEvent evt) throws InterruptedException {
        trier(evt)
    }

    public void onSort$hsuperficie(ForwardEvent evt) throws InterruptedException {
        trier(evt)
    }

    public void onSort$hcontenance(ForwardEvent evt) throws InterruptedException {
        trier(evt)
    }

    private void nettoyerChamps() {
        //Constraint c = fieldNumero.getConstraint();
        fieldNumero.setConstraint(null);
        fieldNumero.setValue(null);
        //fieldNumero.setConstraint(c);
        fieldPrixVente.setValue(null);
        fieldStatut.setValue(null);
        fieldSuperficie.setValue(null);
        fieldSuperficieConstruite.setValue(null);
        fieldSuperficieVendable.setValue(null);
        fieldTitreFoncier.setValue(null);
        fieldContenance.setValue(null);
        
        btnSave.setVisible(true)
        btnUpdate.setVisible(false)
        btnDelete.setVisible(false)
        btnCancel.setVisible(false)
        lstBien.clearSelection();
        
        cobienParents.setSelectedItem(null)
        //        cocontenances.setSelectedItem(null)
    }
    
    public void onClick$btnPdf() throws InterruptedException{
        genererRapportBiens("PDF")
    }
    
    public void onClick$btnExcel() throws InterruptedException{
        genererRapportBiens("EXCEL")
    }
    
    def genererRapportBiens(String formatToExport){
        setParamExport(formatToExport)
        
        
        String nomsociete = this.projet.groupeClient.nomsociete
        String titrerapport = "Liste des biens du projet [" + this.projet.toString() + "]"
       
        
        def reportDef = new JasperReportDef(name:'rapport_des_BiensL.jasper',
            fileFormat:exportFormat,
            reportData : biens,
            parameters : ['nomsociete':nomsociete, 'titrerapport':titrerapport]
        )        
        def bit = jasperService.generateReport(reportDef).toByteArray()
        def nom_fichier = "Liste_des_biens_" + this.projet.toString() +"." + extension
        //Filedownload.save(bit, "application/pdf", nom_fichier);
        genererRapport(nom_fichier,bit)
    }
    
    def setParamExport(String formatToExport){
        if (formatToExport == "PDF"){
            exportFormat = JasperExportFormat.PDF_FORMAT
            extension = "pdf"
            strAppExt = "application/pdf"
        }
        else if (formatToExport == "EXCEL"){
            exportFormat = JasperExportFormat.XLS_FORMAT
            extension = "xls"
            strAppExt = "application/file" 
        }
    }
    
    def genererRapport(String nom_fichier, bit){
        Media amedia = new AMedia(nom_fichier, extension, strAppExt, bit);
        Map map = new HashMap();
        map.put("content", amedia);
        Executions.createComponents("/zul/util/pdfviewer.zul", null, map).doModal();
    }
    
}
