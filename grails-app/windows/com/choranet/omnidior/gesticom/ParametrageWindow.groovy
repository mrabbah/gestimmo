
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
 * Parametrage Window Object
 **/
class ParametrageWindow extends SuperWindow {
    /**
     * Service pour la gestion de l'objet Parametrage
     **/
    def parametrageService
    
    /**
     * Service jasper pour la generation des rapports
     **/
    def jasperService
    /**
     * Logger de la class ParametrageWindow
     **/
    private Log logger = LogFactory.getLog(ParametrageWindow.class)
    
    /**
     * liste de utilisateur
     **/
    def utilisateurs	
    /**
     * utilisateur  selectionn�
     **/
    def utilisateurSelected	
                
    /**
     * Constructeur
     **/
    public ParametrageWindow () {
        super(Parametrage.class,false)
        def lp = Parametrage.list()
        if (lp && lp.size()>0){
            objet = lp[0]            
        }
        
    }  

    protected SuperService getService() {
        return this.parametrageService
    }
    
    /**
     * Generation du rapport pdf
     **/
    def genererRapportPdf() {
        String nomsociete = ChoraClientInfo.findByNomsociete("OMNIDIOR").nomsociete
        String titrerapport = "Rapport des Parametrages"
        def listeObjetsExporter = getObjetsToExport()
        def reportDef = new JasperReportDef(name:'rapport_des_Parametrages.jrxml',
            fileFormat:JasperExportFormat.PDF_FORMAT,
            reportData : listeObjetsExporter,
            parameters : [  'nomsociete':nomsociete, 'titrerapport':titrerapport ]
        )
        def bit = jasperService.generateReport(reportDef).toByteArray()
        def nom_fichier = "rapport_des_Parametrages.pdf"
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
        String titrerapport = "Rapport des Parametrages"
        def listeObjetsExporter = getObjetsToExport()
        def reportDef = new JasperReportDef(name:'rapport_des_Parametrages.jrxml',
            fileFormat:JasperExportFormat.XLS_FORMAT,
            reportData : listeObjetsExporter,
            parameters : [  'nomsociete':nomsociete, 'titrerapport':titrerapport ]
        )
        def bit = jasperService.generateReport(reportDef).toByteArray()
        def nom_fichier = "rapport_des_Parametrages.xls"
        Filedownload.save(bit, "application/file", nom_fichier);
        
    }
    
    
    /**
     * Fonction qui g�re l'initialisation des listes d'associations au niveau du constructeur
     **/
    def initialiserAssociation() {
        
        utilisateurs = Utilisateur.list()		
        if(utilisateurs.size() > 0)
        utilisateurSelected = utilisateurs.get(0)
        else
        utilisateurSelected = null
                    
    }
    /**
     * Fonction qui permet de r�-initaliser l'association au niveau de l'interface
     * @param del si c'est une r�initionalisation apr�s une suppression ou non
     **/
    def reinitialiserAssociation(del) {
        
        if(del) {
            utilisateurs = Utilisateur.list()
        }		
        if(utilisateurs.size() > 0)
        utilisateurSelected = utilisateurs.get(0)
        else
        utilisateurSelected = null
                    
    }
    /**
     * Fonction qui copie la valeur de l'association � l'�l�ment courant
     **/
    def actualiserValeurAssociation() {
        		
        objet.utilisateur = utilisateurSelected
        if(utilisateurs.size() > 0) {
            def binderutilisateur = new AnnotateDataBinder(this.getFellow("coutilisateurs"))
            utilisateurSelected = utilisateurs.get(0)
            binderutilisateur.loadAll()
        }
        else
        utilisateurSelected = null
                    
    }
    /**
     * Fonction qui fait la liaison entre l'association l'�l�ment selectionn� et la liste dans le crud
     **/
    def afficherValeurAssociation() {
        						
        def binderutilisateur = new AnnotateDataBinder(this.getFellow("coutilisateurs"))
        utilisateurSelected = utilisateurs.find{ it.id == Parametrage.findById(objet.id).utilisateur.id }
        binderutilisateur.loadAll()
                    
    }
    
    def update() {
        try {
            getService().update(objet)
            Messagebox.show("Modification terminée", "Information", Messagebox.OK, Messagebox.INFORMATION)
        }
        catch(Exception e) {
            logger.error "Error: ${e.message}", e
            Messagebox.show("Problème lors de la mise à jour", "Erreur", Messagebox.OK, Messagebox.ERROR)
        }
        finally {

        }
    }
}

