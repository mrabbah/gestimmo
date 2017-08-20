
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
 * Notaire Window Object
 **/
class NotaireWindow extends SuperWindow {
    /**
     * Service pour la gestion de l'objet Notaire
     **/
    def notaireService
    def excelImporterService
    
    /**
     * Service jasper pour la generation des rapports
     **/
    def jasperService
    /**
     * Logger de la class NotaireWindow
     **/
    private Log logger = LogFactory.getLog(NotaireWindow.class)
    
    /**
     * Constructeur
     **/
    public NotaireWindow () {
        super(Notaire.class)
    }  

    protected SuperService getService() {
        return this.notaireService
    }
    
    /**
     * Generation du rapport pdf
     **/
    def genererRapportPdf() {
        String nomsociete = ChoraClientInfo.findByNomsociete("OMNIDIOR").nomsociete
        String titrerapport = "Rapport des Notaires"
        def listeObjetsExporter = getObjetsToExport()
        def reportDef = new JasperReportDef(name:'rapport_des_Notaires.jrxml',
            fileFormat:JasperExportFormat.PDF_FORMAT,
            reportData : listeObjetsExporter,
            parameters : [  'nomsociete':nomsociete, 'titrerapport':titrerapport ]
        )
        def bit = jasperService.generateReport(reportDef).toByteArray()
        def nom_fichier = "rapport_des_Notaires.pdf"
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
        String titrerapport = "Rapport des Notaires"
        def listeObjetsExporter = getObjetsToExport()
        def reportDef = new JasperReportDef(name:'rapport_des_Notaires.jrxml',
            fileFormat:JasperExportFormat.XLS_FORMAT,
            reportData : listeObjetsExporter,
            parameters : [  'nomsociete':nomsociete, 'titrerapport':titrerapport ]
        )
        def bit = jasperService.generateReport(reportDef).toByteArray()
        def nom_fichier = "rapport_des_Notaires.xls"
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
    def add(){
        Boolean valide = validerNotaire()
        
        if (valide){
            super.add()
        }
    }
    def update(){
        Boolean valide = validerNotaire()
        
        if (valide){
            super.update()
        }
    }
    
    def minLength(String value, Integer min, String errorMsg){
        Boolean valide = true
        if (!value || value.length() != min ) {
            valide = false
            Messagebox.show(errorMsg , "Erreur", Messagebox.OK, Messagebox.ERROR)
        }
        return valide
    }
    
    def validerNotaire(){
        Boolean valide = true
        String msg = "Le numéro Tel1 doit être composé au minimum de 9 chiffres [6xxxxxxxx] " 
        
        if(objet.gsm != null && !objet.gsm.equals("")){
            valide = minLength(objet.gsm, 9, msg)
        }
        
        //        msg = "Le numéro de fixe doit être composé au minimum de 9 chiffres [5xxxxxxxx] " 
        //        
        //        if(objet.fixe && !objet.fixe.equals("")){
        //            if(objet.fixe.length() >0){
        //                valide = minLength(objet.fixe, 9, msg)
        //            }
        //        }
        
        msg = "Le numéro de fax doit être composé au minimum de 9 chiffres [5xxxxxxxx] " 
        if(objet.fax != null && !objet.fax.equals("") ){
            valide = minLength(objet.fax, 9, msg)            
        }
        return valide
    }
    
    def importation(media) {
        String resultat = excelImporterService.importerNotaires(media)
        Messagebox.show(resultat, "Notification" ,  Messagebox.OK, Messagebox.INFORMATION)
        rafraichirList()
    }
    
}
