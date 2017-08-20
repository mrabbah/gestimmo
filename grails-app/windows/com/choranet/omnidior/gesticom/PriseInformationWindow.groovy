
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
 * PriseInformation Window Object
 **/
class PriseInformationWindow extends SuperWindow {
    /**
     * Service pour la gestion de l'objet PriseInformation
     **/
    def priseInformationService
    def clientService
    def projetService
    
    /**
     * Service jasper pour la generation des rapports
     **/
    def jasperService
    /**
     * Logger de la class PriseInformationWindow
     **/
    private Log logger = LogFactory.getLog(PriseInformationWindow.class)
    
    /**
     * liste de sourceInformation
     **/	
    def sourceInformations	
    /**
     * sourceInformation  selectionn�
     **/
    def sourceInformationSelected
                
    /**
     * liste de canal
     **/	
    def canals	
    /**
     * canal  selectionn�
     **/
    def canalSelected
                
    /**
     * liste de projet
     **/	
    def projets	
    /**
     * projet  selectionn�
     **/
    def projetSelected
                
    /**
     * liste de client
     **/	
    def clients	
    /**
     * client  selectionn�
     **/
    def clientSelected
    def utilisateur= session.utilisateur
                
    /**
     * Constructeur
     **/
    public PriseInformationWindow (projetService,clientService,priseInformationService) {
        super(PriseInformation.class)
        this.projetService = projetService
        this.clientService = clientService
        this.priseInformationService = priseInformationService
        specialInit()
    }  

    protected SuperService getService() {
        return this.priseInformationService
    }
    
    /**
     * Generation du rapport pdf
     **/
    def genererRapportPdf() {
        String nomsociete = ChoraClientInfo.findByNomsociete("OMNIDIOR").nomsociete
        String titrerapport = "Rapport des PriseInformations"
        def listeObjetsExporter = getObjetsToExport()
        def reportDef = new JasperReportDef(name:'rapport_des_PriseInformations.jrxml',
            fileFormat:JasperExportFormat.PDF_FORMAT,
            reportData : listeObjetsExporter,
            parameters : [  'nomsociete':nomsociete, 'titrerapport':titrerapport ]
        )
        def bit = jasperService.generateReport(reportDef).toByteArray()
        def nom_fichier = "rapport_des_PriseInformations.pdf"
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
        String titrerapport = "Rapport des PriseInformations"
        def listeObjetsExporter = getObjetsToExport()
        def reportDef = new JasperReportDef(name:'rapport_des_PriseInformations.jrxml',
            fileFormat:JasperExportFormat.XLS_FORMAT,
            reportData : listeObjetsExporter,
            parameters : [  'nomsociete':nomsociete, 'titrerapport':titrerapport ]
        )
        def bit = jasperService.generateReport(reportDef).toByteArray()
        def nom_fichier = "rapport_des_PriseInformations.xls"
        Filedownload.save(bit, "application/file", nom_fichier);
        
    }
    
    
    /**
     * Fonction qui g�re l'initialisation des listes d'associations au niveau du constructeur
     **/
    def initialiserAssociation() {
        
        sourceInformations = SourceInformation.list()		
        if(sourceInformations.size() > 0)
        sourceInformationSelected = sourceInformations.get(0)
        else
        sourceInformationSelected = null
                    
        canals = Canal.list()		
        if(canals.size() > 0)
        canalSelected = canals.get(0)
        else
        canalSelected = null                    
                    
    }
    
    def specialInit(){
        
        projets = utilisateur.projets.toList()//projetService.getProjetByUtilisateur(utilisateur)
        if(projets.size() > 0)
        projetSelected = projets.get(0)
        else
        projetSelected = null
        
        clients = utilisateur.clients.toList()//clientService.getClientByUtilisateur()
        if(clients.size() > 0)
        clientSelected = clients.get(0)
        else
        clientSelected = null
        objet.date = new Date()
        chargerPriseInformation()
    }
    /**
     * Fonction qui permet de r�-initaliser l'association au niveau de l'interface
     * @param del si c'est une r�initionalisation apr�s une suppression ou non
     **/
    def reinitialiserAssociation(del) {
        	
        if(del) {
            sourceInformations = SourceInformation.list()
        }	
        if(sourceInformations.size() > 0)
        sourceInformationSelected = sourceInformations.get(0)
        else
        sourceInformationSelected = null
                    	
        if(del) {
            canals = Canal.list()
        }	
        if(canals.size() > 0)
        canalSelected = canals.get(0)
        else
        canalSelected = null
                    	
        if(del) {
            projets = utilisateur.projets.toList()//projetService.getProjetByUtilisateur(utilisateur)
        }	
        if(projets.size() > 0)
        projetSelected = projets.get(0)
        else
        projetSelected = null
                    	
        if(del) {
            clients = utilisateur.clients.toList()//clientService.getClientByUtilisateur()
        }	
        if(clients.size() > 0)
        clientSelected = clients.get(0)
        else
        clientSelected = null
                    
    }
    /**
     * Fonction qui copie la valeur de l'association � l'�l�ment courant
     **/
    def actualiserValeurAssociation() {
        		
        objet.sourceInformation = sourceInformationSelected
        if(sourceInformations.size() > 0) {
            def bindersourceInformation = new AnnotateDataBinder(this.getFellow("cosourceInformations"))
            sourceInformationSelected = sourceInformations.get(0)
            bindersourceInformation.loadAll()
        }
        else
        sourceInformationSelected = null
                    		
        objet.canal = canalSelected
        if(canals.size() > 0) {
            def bindercanal = new AnnotateDataBinder(this.getFellow("cocanals"))
            canalSelected = canals.get(0)
            bindercanal.loadAll()
        }
        else
        canalSelected = null
                    		
        objet.projet = projetSelected
        if(projets.size() > 0) {
            def binderprojet = new AnnotateDataBinder(this.getFellow("coprojets"))
            projetSelected = projets.get(0)
            binderprojet.loadAll()
        }
        else
        projetSelected = null
                    		
        objet.client = clientSelected
        if(clients.size() > 0) {
            def binderclient = new AnnotateDataBinder(this.getFellow("coclients"))
            clientSelected = clients.get(0)
            binderclient.loadAll()
        }
        else
        clientSelected = null
                    
    }
    /**
     * Fonction qui fait la liaison entre l'association l'�l�ment selectionn� et la liste dans le crud
     **/
    def afficherValeurAssociation() {
        		
        def bindersourceInformation = new AnnotateDataBinder(this.getFellow("cosourceInformations"))
        sourceInformationSelected = sourceInformations.find{ it.id == PriseInformation.findById(objet.id).sourceInformation.id }
        bindersourceInformation.loadAll()
                    		
        def bindercanal = new AnnotateDataBinder(this.getFellow("cocanals"))
        canalSelected = canals.find{ it.id == PriseInformation.findById(objet.id).canal.id }
        bindercanal.loadAll()
                    		
        def binderprojet = new AnnotateDataBinder(this.getFellow("coprojets"))
        projetSelected = projets.find{ it.id == PriseInformation.findById(objet.id).projet.id }
        binderprojet.loadAll()
                    		
        def binderclient = new AnnotateDataBinder(this.getFellow("coclients"))
        clientSelected = clients.find{ it.id == PriseInformation.findById(objet.id).client.id }
        binderclient.loadAll()
                    
    }
    def filtrer() {
        try {            

            def map
            if(attributsAFiltrer == null) {
                map = getService().filtrer(clazz, filtre, sortedHeader, sortedDirection, ofs, maxNb,utilisateur)
            } else {
                map = getService().filtrer(clazz, filtre, attributsAFiltrer, sortedHeader, sortedDirection, ofs, maxNb,utilisateur)
            }
            tailleListe = map["tailleListe"]
            listeObjets = map["listeObjets"]
            def binder = new AnnotateDataBinder(this.getFellow("lstObjet"))
            binder.loadAll()
        
            def binder2 = new AnnotateDataBinder(this.getFellow("paging"))
            binder2.loadAll()    
        } catch (Exception ex) {
            logger.error(ex)
        }        
    }    
    
    def chargerPriseInformation(){
        listeObjets = priseInformationService.getPriseInformationByUtlisateur(utilisateur,ofs,maxNb)
        def ls = priseInformationService.getPriseInformationByUtlisateur(utilisateur)
        if (ls){
            tailleListe = ls.size()
        }
    }
}

