
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
import org.zkoss.zk.ui.Executions;
import com.choranet.omnidior.gesticom.ChoraClientInfo;


/**
 * Alerte Window Object
 **/
class AlerteWindow extends SuperWindow {
    /**
     * Service pour la gestion de l'objet Alerte
     **/
    def alerteService
    
    /**
     * Service jasper pour la generation des rapports
     **/
    def jasperService
    def statutService
    def projetService
    def clientService
    def phaseService
    def utilisateur= session.utilisateur
    /**
     * Logger de la class AlerteWindow
     **/
    private Log logger = LogFactory.getLog(AlerteWindow.class)
    
    /**
     * liste de phase
     **/	
    def phases	
    /**
     * phase  selectionn�
     **/
    def phaseSelected
    
    def projetsFilter
    def projets
    def projetSelected
    
    def clients
    def clientSelected
    
    def statuts
    def statutSelected
    
    def phasesF
    def projetsF
    def clientsF
                
    /**
     * Constructeur
     **/
    public AlerteWindow (statutService,projetService,clientService,phaseService,alerteService) {
        super(Alerte.class,false)
        this.statutService = statutService
        this.projetService = projetService 
        this.clientService = clientService
        this.phaseService = phaseService
        this.alerteService = alerteService
        specialInit()
    }  

    protected SuperService getService() {
        return this.alerteService
    }
    
    /**
     * Generation du rapport pdf
     **/
    def genererRapportPdf() {
        String nomsociete = ChoraClientInfo.findByNomsociete("OMNIDIOR").nomsociete
        String titrerapport = "Rapport des Alertes"
        def listeObjetsExporter = getObjetsToExport()
        def reportDef = new JasperReportDef(name:'rapport_des_Alertes.jrxml',
            fileFormat:JasperExportFormat.PDF_FORMAT,
            reportData : listeObjetsExporter,
            parameters : [  'nomsociete':nomsociete, 'titrerapport':titrerapport ]
        )
        def bit = jasperService.generateReport(reportDef).toByteArray()
        def nom_fichier = "rapport_des_Alertes.pdf"
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
        String titrerapport = "Rapport des Alertes"
        def listeObjetsExporter = getObjetsToExport()
        def reportDef = new JasperReportDef(name:'rapport_des_Alertes.jrxml',
            fileFormat:JasperExportFormat.XLS_FORMAT,
            reportData : listeObjetsExporter,
            parameters : [  'nomsociete':nomsociete, 'titrerapport':titrerapport ]
        )
        def bit = jasperService.generateReport(reportDef).toByteArray()
        def nom_fichier = "rapport_des_Alertes.xls"
        Filedownload.save(bit, "application/file", nom_fichier);
        
    }    
    def specialInit(){
        filtrer(false)
        statuts = statutService.getByType("ALERTE")
        if(statuts.size() > 0)
        statutSelected = statuts.get(0)
        else
        statutSelected = null

        // objet.numero = 1
                
        projets = utilisateur.projets.toList()//projetService.getProjetByUtilisateur(utilisateur)
        if(projets.size() > 0)
        projetSelected = projets.get(0)
        else
        projetSelected = null
        
        //        clients = clientService.getClientByUtilisateur()
        //        if(clients.size() > 0)
        //        clientSelected = clients.get(0)
        //        else
        //        clientSelected = null
        
        projetsF = utilisateur.projets.toList()
        clientsF = utilisateur.clients.toList()
        //phasesF = phaseService.getPhaseByUtlisateur(utilisateur)
        //ActualiserClientProjet('projet',false)
        
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
                 
        if(del) {
            statuts = statutService.getByType("ALERTE")
        }	
        if(statuts.size() > 0)
        statutSelected = statuts.get(0)
        else
        statutSelected = null
        
        //        if(del) {
        //            projets =  utilisateur.projets.toList()//projetService.getProjetByUtilisateur(utilisateur)//Projet.list()
        //        }	
        //        if(projets.size() > 0)
        //        projetSelected = projets.get(0)
        //        else
        //        projetSelected = null
        clearAssoc()
                               
    }
    /**
     * Fonction qui copie la valeur de l'association � l'�l�ment courant
     **/
    def actualiserValeurAssociation(Boolean add = true) {
        if (add){
            objet.projet = projetSelected
            objet.client = clientSelected
        }
        
        //        if(projets.size() > 0) {
        //            def binderprojet = new AnnotateDataBinder(this.getFellow("coprojets"))
        //            projetSelected = projets.get(0)
        //            binderprojet.loadAll()
        //        }
        //        else
        //        projetSelected = null
        //        
        //        
        //        ActualiserClientProjet('projet',false)  
        //        if(clients.size() > 0) {
        //            def binderclient = new AnnotateDataBinder(this.getFellow("coclients"))
        //            clientSelected = clients.get(0)
        //            binderclient.loadAll()
        //        }
        //        else
        //        clientSelected = null        
        
        objet.statut = statutSelected
        if(statuts.size() > 0) {
            def binderstatut = new AnnotateDataBinder(this.getFellow("costatuts"))
            statutSelected = statuts.get(0)
            binderstatut.loadAll()
        }
        else
        statutSelected = null
        
        clearAssoc()
        
    }
    /**
     * Fonction qui fait la liaison entre l'association l'�l�ment selectionn� et la liste dans le crud
     **/
    
    def afficherValeurAssociation() {
        
        //        def binderprojet = new AnnotateDataBinder(this.getFellow("coprojets"))
        //        projetSelected = projets.find{ it.id == objet.projet.id }
        //        
        //        binderprojet.loadAll()
        //        ActualiserClientProjet('projet',false)
        //        
        //        def binderclient = new AnnotateDataBinder(this.getFellow("coclients"))
        //        clientSelected = clients.find{ it.id == objet.client.id }
        //        binderclient.loadAll()
        
        this.getFellow("projetText").visible = true  
        new AnnotateDataBinder(this.getFellow("projetText")).loadAll()
         
        this.getFellow("coprojets").visible = false  

        this.getFellow("clientText").visible = true  
        new AnnotateDataBinder(this.getFellow("clientText")).loadAll()
         
        this.getFellow("coclients").visible = false  
       
        
        def binderstatut =  new AnnotateDataBinder(this.getFellow("costatuts"))
        statutSelected = statuts.find{ it.id == objet.statut.id}
        binderstatut.loadAll()       
                    		                   
    }
    
    def ActualiserClientProjet(sender,actualiser){
        
        if (sender == "projet"){
            if (projetSelected != null){
                clients  = phaseService.getClientByProjet(projetSelected,utilisateur)
                
                if(clients && clients.size() > 0){
                    clients = clients.sort{it.nom}
                    clientSelected = clients.get(0)
                }
                else
                clientSelected = null
            
                if(actualiser){
                    new AnnotateDataBinder(this.getFellow("filterclient")).loadAll()
                    new AnnotateDataBinder(this.getFellow("coclients")).loadAll()
                    new AnnotateDataBinder(this.getFellow("fieldcoclientsbis")).loadAll()  
                }
            } else if (projetSelected == null){
                
                clientSelected = null
                
                new AnnotateDataBinder(this.getFellow("coprojets")).loadAll()
                new AnnotateDataBinder(this.getFellow("coclients")).loadAll()                
            }
        }
        
        else if (sender == "client"){
            //            projets =  phaseService.getProjetByClient(clientSelected)
            //            
            //            new AnnotateDataBinder(this.getFellow("filterprojet")).loadAll()
            //            new AnnotateDataBinder(this.getFellow("coprojets")).loadAll()
            //            new AnnotateDataBinder(this.getFellow("fieldcoprojetsbis")).loadAll()   
        }


    }    
    
    def actualiserPhase(actualiser){
      
        phases = phaseService.getByClientEtProjet(clientSelected,projetSelected)           
        phases = phases.sort{it.numPhase}
        
        if(phases != null && phases.size() > 0)
        phaseSelected = phases.get(0)
        else
        phaseSelected = null
        
        if (actualiser){
            new AnnotateDataBinder(this.getFellow("filterphase")).loadAll()
            new AnnotateDataBinder(this.getFellow("cophases")).loadAll()
            new AnnotateDataBinder(this.getFellow("fieldcophasesbis")).loadAll()  
        }
    }
    
    def filtrer(Boolean loadliste = true) {
        try {            
            utilisateur = Utilisateur.findById(utilisateur.id)        
            session.utilisateur = utilisateur
            def map
            if(attributsAFiltrer == null) {
                map = getService().filtrer(clazz, filtre, sortedHeader, sortedDirection, ofs, maxNb,utilisateur)
            } else {
                map = getService().filtrer(clazz, filtre, attributsAFiltrer, sortedHeader, sortedDirection, ofs, maxNb,utilisateur)
            }
            tailleListe = map["tailleListe"]
            listeObjets = map["listeObjets"]
            
            if (loadliste){
                def binder = new AnnotateDataBinder(this.getFellow("lstObjet"))
                binder.loadAll()
        
                def binder2 = new AnnotateDataBinder(this.getFellow("paging"))
                binder2.loadAll()    
            }
        } catch (Exception ex) {
            logger.error(ex)
        }        
    }    
    
    def chargerAlerte(){
        listeObjets = alerteService.getAlerteByUtlisateur(utilisateur,ofs,maxNb)
        def ls = alerteService.getAlerteByUtlisateur(utilisateur)
        if (ls){
            tailleListe = ls.size()
        }
    }
    
    def add(){        

        if (projetSelected && clientSelected ){
            super.add()
        }
        else{
            Messagebox.show("Veuillez vérifier les champs : \n \n * Projet  \n * Client" , "Erreur", Messagebox.OK, Messagebox.ERROR)            
        }
    }
    
    def update(){
        //        objet.messagePersonalise = this.getFellow("fieldMessagePersonalise").value
        actualiserValeurAssociation(false)
        try {
            getService().update(objet)
            activerBoutons(false)
            objet = clazz.newInstance()
            rafraichirField()
            rafraichirList()
        }
        catch(Exception e) {
            logger.error "Error: ${e.message}", e
            Messagebox.show("Problème lors de la mise à jour", "Erreur", Messagebox.OK, Messagebox.ERROR)
        }
        finally {
            //            activerBoutons(false)
            //            objet = clazz.newInstance()
            //            rafraichirField()
            //            rafraichirList()
        }
    }
        
    def newRecord(){
        super.newRecord()
        
        this.getFellow("clientText").visible = false  
        this.getFellow("coclients").visible = true  
        
        this.getFellow("projetText").visible = false  
        this.getFellow("coprojets").visible = true  
        
        clearAssoc ()
    }
    
    def clearAssoc (){
        projetSelected = null
        clientSelected = null
        new AnnotateDataBinder(this.getFellow("coprojets")).loadAll()
        new AnnotateDataBinder(this.getFellow("coclients")).loadAll()  
    }
}
