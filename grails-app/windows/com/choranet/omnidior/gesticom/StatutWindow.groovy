
package com.choranet.omnidior.gesticom
    

import org.zkoss.zul.*
import org.zkoss.zkplus.databind.*
import org.apache.commons.logging.Log 
import org.apache.commons.logging.LogFactory
import org.codehaus.groovy.grails.plugins.jasper.JasperExportFormat
import org.codehaus.groovy.grails.plugins.jasper.JasperReportDef

import org.zkoss.util.media.AMedia;
import org.zkoss.util.media.Media;
import java.util.HashMap;
import java.util.Map;
import org.zkoss.zk.ui.Executions


/**
 * Statut Window Object
 **/
class StatutWindow extends SuperWindow {
    /**
     * Service pour la gestion de l'objet Statut
     **/
    def StatutService
    
    /**
     * Service jasper pour la generation des rapports
     **/
    def jasperService
    /**
     * Logger de la class StatutWindow
     **/
    private Log logger = LogFactory.getLog(StatutWindow.class)
                    
    /**
     * Constructeur
     **/
    public StatutWindow () {
        super(Statut.class)
    }  

    protected SuperService getService() {
        return this.StatutService
    }
    
    /**
     * Generation du rapport pdf
     **/
    def genererRapportPdf() {
        String nomsociete = ChoraClientInfo.findByNomsociete("OMNIDIOR").nomsociete
        String titrerapport = "Rapport des Statuts"
        def listeObjetsExporter = getObjetsToExport()
        def reportDef = new JasperReportDef(name:'rapport_des_Statuts.jrxml',
            fileFormat:JasperExportFormat.PDF_FORMAT,
            reportData : listeObjetsExporter,
            parameters : [  'nomsociete':nomsociete, 'titrerapport':titrerapport ]
        )
        def bit = jasperService.generateReport(reportDef).toByteArray()
        def nom_fichier = "rapport_des_Statuts.pdf"
        //Filedownload.save(bit, "application/pdf", nom_fichier);
        Media amedia = new AMedia(nom_fichier, "pdf", "application/pdf", bit);
        Map map = new HashMap();
        map.put("content", amedia);
        Executions.createComponents("/zul/util/pdfviewer.zul", null, map).doModal();
    }
    /**
     * Generation du rapport excel
     **/
    def genererRapportExcel() {
        String nomsociete = ChoraClientInfo.findByNomsociete("OMNIDIOR").nomsociete
        String titrerapport = "Rapport des Statuts"
        def listeObjetsExporter = getObjetsToExport()
        def reportDef = new JasperReportDef(name:'rapport_des_Statuts.jrxml',
            fileFormat:JasperExportFormat.XLS_FORMAT,
            reportData : listeObjetsExporter,
            parameters : [  'nomsociete':nomsociete, 'titrerapport':titrerapport ]
        )
        def bit = jasperService.generateReport(reportDef).toByteArray()
        def nom_fichier = "rapport_des_Statuts.xls"
        Filedownload.save(bit, "application/file", nom_fichier);
        
    }
    
    /**
     * Fonction qui g�re l'initialisation des listes d'associations au niveau du constructeur
     **/
    def initialiserAssociation() {
        
    }
    /**
     * Fonction qui permet de r�-initaliser l'association au niveau de l'interface
     * @param del si c'est une r�initionalisation apr�s une suppression ou non
     **/
    def reinitialiserAssociation(del) {
        
    }
    /**
     * Fonction qui copie la valeur de l'association � l'�l�ment courant
     **/
    def actualiserValeurAssociation() {
        
    }
    /**
     * Fonction qui fait la liaison entre l'association l'�l�ment selectionn� et la liste dans le crud
     **/
    def afficherValeurAssociation() {
        
    }
    
    def activerBoutons(visible) {
        try {
            this.getFellow("btnUpdate").visible = visible
        } catch(Exception ex) { /**/ }
        try {
            this.getFellow("btnDelete").visible = visible
        } catch(Exception ex) { /**/ }
        //        try {
        //            this.getFellow("btnCancel").visible = visible
        //        } catch(Exception ex) { /**/ }
        try {
            this.getFellow("btnPdf").visible = !visible
        } catch(Exception ex) { /**/ }
        try {
            this.getFellow("btnExcel").visible = !visible
        } catch(Exception ex) { /**/ }
        try {
            this.getFellow("btnSave").visible = !visible
        } catch(Exception ex) { /**/ }
        try {
            this.getFellow("btnNew").visible = !visible
        } catch(Exception ex) { /**/ }
        try {
            this.getFellow("westPanel").open = visible        
        } catch(Exception ex) { /**/ }
        
        //interdire la modification du code d'un statut existant
        try {
            this.getFellow("fieldCode").readonly = visible        
        } catch(Exception ex) { /**/ }
                try {
            this.getFellow("fieldcodebis").readonly = visible        
        } catch(Exception ex) { /**/ }
        
    }
    
    def delete(){
        if ( objet.code.isInteger() && objet.code.toInteger() <= 28){
            Messagebox.show("Impossible de supprimer les statuts basiques" , "Erreur", Messagebox.OK, Messagebox.ERROR)            
        }
        else{
            super.delete()
        }
    }
}

