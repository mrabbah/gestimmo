
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
 * Pays Window Object
 **/
class PaysWindow extends SuperWindow {
    /**
     * Service pour la gestion de l'objet Pays
     **/
    def paysService
    
    /**
     * Service jasper pour la generation des rapports
     **/
    def jasperService
    /**
     * Logger de la class PaysWindow
     **/
    private Log logger = LogFactory.getLog(PaysWindow.class)
    
    /**
     * Constructeur
     **/
    public PaysWindow () {
        super(Pays.class)
    }  

    protected SuperService getService() {
        return this.paysService
    }
    
    /**
     * Generation du rapport pdf
     **/
    def genererRapportPdf() {
        String nomsociete = ChoraClientInfo.findByNomsociete("OMNIDIOR").nomsociete
        String titrerapport = "Rapport des Payss"
        def listeObjetsExporter = getObjetsToExport()
        def reportDef = new JasperReportDef(name:'rapport_des_Payss.jrxml',
            fileFormat:JasperExportFormat.PDF_FORMAT,
            reportData : listeObjetsExporter,
            parameters : [  'nomsociete':nomsociete, 'titrerapport':titrerapport ]
        )
        def bit = jasperService.generateReport(reportDef).toByteArray()
        def nom_fichier = "rapport_des_Payss.pdf"
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
        String titrerapport = "Rapport des Payss"
        def listeObjetsExporter = getObjetsToExport()
        def reportDef = new JasperReportDef(name:'rapport_des_Payss.jrxml',
            fileFormat:JasperExportFormat.XLS_FORMAT,
            reportData : listeObjetsExporter,
            parameters : [  'nomsociete':nomsociete, 'titrerapport':titrerapport ]
        )
        def bit = jasperService.generateReport(reportDef).toByteArray()
        def nom_fichier = "rapport_des_Payss.xls"
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
    	actualiserValeurAssociation()
        try {     
            getService().save(objet)
            objet = clazz.newInstance()
            rafraichirField()
            rafraichirList()
            activerBoutons(false)
        } catch(Exception ex) {
            logger.error "Error: ${ex.message}", ex
            if (ex.message.contains("com.choranet.omnidior.gesticom.Pays.intitule.unique.error.com.choranet.omnidior.gesticom.Pays.intitule")){
                Messagebox.show("Le pays [" + objet.intitule + "] existe déjà", "Erreur", Messagebox.OK, Messagebox.ERROR)  
            }
            else{
                Messagebox.show("Echec lors de la transaction\n" + ex.message, "Erreur", Messagebox.OK, Messagebox.ERROR)
            }
        } finally {
            //            objet = clazz.newInstance()
            //            rafraichirField()
            //            rafraichirList()
            //            activerBoutons(false)
        }    
    }
    def update(){
        actualiserValeurAssociation()
        try {
            getService().update(objet)
            activerBoutons(false)
            objet = clazz.newInstance()
            rafraichirField()
            rafraichirList()
        }
        catch(Exception e) {
            logger.error "Error: ${e.message}", e
            if (e.message.contains("com.choranet.omnidior.gesticom.Pays.intitule.unique.error.com.choranet.omnidior.gesticom.Pays.intitule")){
                Messagebox.show("Le pays [" + objet.intitule + "] existe déjà", "Erreur", Messagebox.OK, Messagebox.ERROR)  
            }
            else{
                Messagebox.show("Echec lors de la transaction\n" + ex.message, "Erreur", Messagebox.OK, Messagebox.ERROR)
            }
        }
        finally {
            //            activerBoutons(false)
            //            objet = clazz.newInstance()
            //            rafraichirField()
            //            rafraichirList()
        }
        
    }
    
}

