
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
import org.codehaus.groovy.grails.orm.hibernate.*;

/**
 * Paiement Window Object
 **/
class PaiementWindow extends SuperWindow {
    /**
     * Service pour la gestion de l'objet Paiement
     **/
    def paiementService
    def echeanceService
    def projetService
    def clientService
    def phaseService
    def excelImporterService
    
    /**
     * Service jasper pour la generation des rapports
     **/
    def jasperService
    /**
     * Logger de la class PaiementWindow
     **/
    private Log logger = LogFactory.getLog(PaiementWindow.class)
    
    /**
     * liste de modePaiement
     **/	
    def modePaiements	
    /**
     * modePaiement  selectionn�
     **/
    def modePaiementSelected
                
    /**
     * liste de echeances
     **/
    def echeances
    /**
     * echeances selectionn�
     **/
    def echeancesSelected
    
    def statutService
    def statuts
    def statutSelected
    
    def projets
    def projetSelected
    
    def clients
    def clientSelected
    
    def types
    def typeSelected
    
    def utilisateur = session.utilisateur
    
    def phases	
    def phaseSelected
       
    def phasesF
    def projetsF = new ArrayList()
    def clientsF = new ArrayList()
    
       
    /**
     * Constructeur
     **/
  
    public PaiementWindow (statutService,echeanceService,projetService,clientService,phaseService,paiementService) {
        super(Paiement.class,false)
        this.statutService = statutService
        this.echeanceService = echeanceService
        this.projetService = projetService
        this.clientService = clientService
        this.phaseService = phaseService
        this.paiementService = paiementService
        specialInit()
    }  

    protected SuperService getService() {
        return this.paiementService
    }
    
    /**
     * Generation du rapport pdf
     **/
    def genererRapportPdf() {
        String nomsociete = ChoraClientInfo.findByNomsociete("OMNIDIOR").nomsociete
        String titrerapport = "Rapport des Paiements"
        def listeObjetsExporter = getObjetsToExport()
        def reportDef = new JasperReportDef(name:'rapport_des_Paiements.jrxml',
            fileFormat:JasperExportFormat.PDF_FORMAT,
            reportData : listeObjetsExporter,
            parameters : [  'nomsociete':nomsociete, 'titrerapport':titrerapport ]
        )
        def bit = jasperService.generateReport(reportDef).toByteArray()
        def nom_fichier = "rapport_des_Paiements.pdf"
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
        String titrerapport = "Rapport des Paiements"
        def listeObjetsExporter = getObjetsToExport()
        def reportDef = new JasperReportDef(name:'rapport_des_Paiements.jrxml',
            fileFormat:JasperExportFormat.XLS_FORMAT,
            reportData : listeObjetsExporter,
            parameters : [  'nomsociete':nomsociete, 'titrerapport':titrerapport ]
        )
        def bit = jasperService.generateReport(reportDef).toByteArray()
        def nom_fichier = "rapport_des_Paiements.xls"
        Filedownload.save(bit, "application/file", nom_fichier);
        
    }
    
    /**
     * Fonction qui permet de r�-initaliser l'association au niveau de l'interface
     * @param del si c'est une r�initionalisation apr�s une suppression ou non
     **/
    
    def specialInit(){
        filtrer(false)
        modePaiements = ModePaiement.list()		
        if(modePaiements.size() > 0)
        modePaiementSelected = modePaiements.get(0)
        else
        modePaiementSelected = null           
        
        statuts = statutService.getByType("PAIEMENT")
        if(statuts.size() > 0)
        statutSelected = statuts.get(0)
        else
        statutSelected = null
                
        types = statutService.getByType("PAIEMENTTYPE")
        if(types.size() > 0)
        typeSelected = types.get(0)
        else
        typeSelected = null
        
        projets = utilisateur.projets.toList()//.sort{it.code}//projetService.getProjetByUtilisateur(utilisateur)
        if(projets.size() > 0)
        projetSelected = projets.get(0)
        else
        projetSelected = null
        
        //        clients = clientService.getClientByUtilisateur()
        //        if(clients.size() > 0)
        //        clientSelected = clients.get(0)
        //        else
        //        clientSelected = null
        
        //        ActualiserClientProjet('projet',false)
        
        //actualiserEcheance(false)
       
        
        def tempProjets = projets //utilisateur.projets.toList()//.sort{it.code}
        for (pj in tempProjets){
            projetsF.add(pj.toString())
        }
        
        def tempClients = utilisateur.clients.toList()//.sort{it.nom}
        for (cl in tempClients){
            clientsF.add(cl.toString())
        }

        phasesF = ['OPTION', 'RESERVATION', 'COMPROMIS','CONTRAT']//phaseService.getPhaseByUtlisateur(utilisateur)
        
    }
    
    def reinitialiserAssociation(del) {
                                   	
        //        if(del) {
        //            projets =  utilisateur.projets.toList() //Projet.list()
        //        }	
        //
        //        if(projets.size() > 0)
        //        projetSelected = projets.get(0)
        //        else
        //        projetSelected = null
        //                    	
        //        if(del) {
        //            clients = clientService.getClientByUtilisateur() 
        //        }	
        //        if(clients.size() > 0)
        //        clientSelected = clients.get(0)
        //        else
        //        clientSelected = null

        //        ActualiserClientProjet('projet',true)    
        //        this.getFellow("lstecheances").clearSelection()
        //        echeancesSelected = null// = new ArrayList()
                
        if(statuts.size() > 0)
        statutSelected = statuts.get(0)
        	
        if(types.size() > 0)
        typeSelected = types.get(0)
        clearAssoc()
                 
    }
    /**
     * Fonction qui copie la valeur de l'association � l'�l�ment courant
     **/
    def actualiserValeurAssociation(Boolean add = true) {
        		
        objet.modePaiement = modePaiementSelected
        objet.echeances = echeancesSelected
        objet.statut = statutSelected
        objet.type = typeSelected
        
        if (add){
            objet.phase = phaseSelected
            //            objet.phase.projet = projetSelected
            //            objet.phase.client = clientSelected  
        }
        
        objet.projet = objet.phase.projet.toString()
        objet.client = objet.phase.client.toString()
        if(objet.phase.bien){
            objet.bien = objet.phase.bien.toString()
        }
        else{
            objet.bien = "" 
        }
        //actualiserPhase(false)
        //ActualiserClientProjet('projet',false)                
        
        if(modePaiements.size() > 0) {
            def bindermodePaiement = new AnnotateDataBinder(this.getFellow("comodePaiements"))
            modePaiementSelected = modePaiements.get(0)
            bindermodePaiement.loadAll()
        }
        else
        modePaiementSelected = null                      
         
        if(statuts.size() > 0) {
            def binderstatut = new AnnotateDataBinder(this.getFellow("costatuts"))
            statutSelected = statuts.get(0)
            binderstatut.loadAll()
        }
        else
        statutSelected = null
                
        if(types.size() > 0) {
            def bindertype = new AnnotateDataBinder(this.getFellow("cotypes"))
            typeSelected = types.get(0)
            bindertype.loadAll()
        }
        else
        typeSelected = null
         
        //        this.getFellow("lstecheances").clearSelection()
        //        echeancesSelected = null// = new ArrayList()        
                
        //                if(phases.size() > 0) {
        //                    def binderphase = new AnnotateDataBinder(this.getFellow("cophases"))
        //                    phaseSelected = phases.get(0)
        //                    binderphase.loadAll()
        //                }
        //                else
        //                phaseSelected = null
        
        //            objet.phase.projet = projetSelected
        //            if(projets.size() > 0) {
        //                def binderprojet = new AnnotateDataBinder(this.getFellow("coprojets"))
        //                projetSelected = projets.get(0)
        //                binderprojet.loadAll()
        //            }
        //            else
        //            projetSelected = null
        
        //        objet.phase.client = clientSelected
        //        
        //        if(clients.size() > 0) {
        //            def binderclient = new AnnotateDataBinder(this.getFellow("coclients"))
        //            clientSelected = clients.get(0)
        //            binderclient.loadAll()
        //        }
        //        else
        //        clientSelected = null
          
        clearAssoc()
    }
    /**
     * Fonction qui fait la liaison entre l'association l'�l�ment selectionn� et la liste dans le crud
     **/ 
    def afficherValeurAssociation() {
        		
        def bindermodePaiement = new AnnotateDataBinder(this.getFellow("comodePaiements"))
        modePaiementSelected = modePaiements.find{ it.id == objet.modePaiement.id }
        bindermodePaiement.loadAll()
        
        def binderprojet = new AnnotateDataBinder(this.getFellow("coprojets"))
        projetSelected = projets.find{ it.id == objet.phase.projet.id }
        binderprojet.loadAll()
        
                       
        //        clients  = []
        //        clients = Client.findAllById(objet.phase.client.id)
        //        def binderclient = new AnnotateDataBinder(this.getFellow("coclients"))        
        //        clientSelected = clients.find{ it.id == objet.phase.client.id }
        //        binderclient.loadAll()	
        //        def binderclient = new AnnotateDataBinder(this.getFellow("coclients")) 
        //        binderclient.visible = false 
        //        binderclient.loadAll()
        
        this.getFellow("clientText").visible = true  
        new AnnotateDataBinder(this.getFellow("clientText")).loadAll()
         
        this.getFellow("coclients").visible = false  
        
        this.getFellow("phaseText").visible = true 
        new AnnotateDataBinder(this.getFellow("phaseText")).loadAll()
         
        this.getFellow("cophases").visible = false  
        
        

        //        phases = []
        //        phases = Phase.findAllById(objet.phase.id)
        //        def binderphase = new AnnotateDataBinder(this.getFellow("cophases"))
        //        phaseSelected = phases.find{ it.id == objet.phase.id }//phaseService.getPhaseByPaiement(phases,objet)
        //        binderphase.loadAll()
              
        actualiserEcheance(true,objet.phase)
        def binderecheances = new AnnotateDataBinder(this.getFellow("lstecheances"))
        echeancesSelected = objet.echeances
        binderecheances.loadAll()	

        def binderstatut =  new AnnotateDataBinder(this.getFellow("costatuts"))
        statutSelected = statuts.find{ it.id == objet.statut.id}
        binderstatut.loadAll()
        
        def bindertype =  new AnnotateDataBinder(this.getFellow("cotypes"))
        typeSelected = types.find{ it.id == objet.type.id}
        bindertype.loadAll()                               
    }
        
    def add(){
        if (projetSelected && clientSelected && phaseSelected){
            actualiserValeurAssociation()
            try {   
                if (verifierDatePE()){
                    objet.intitulePhasePaiement = objet.phase.intitule
                    objet = getService().update(objet)          
                    objet = paiementService.updateListeEcheance(objet)                                
                }
                else{
                    Messagebox.show("La date d'encaissement doit être inférieure ou égale à la date du paiement ", "Erreur", Messagebox.OK, Messagebox.ERROR)   
            
                }
            } catch(Exception ex) {
                logger.error "Error: ${ex.message}", ex
                Messagebox.show("Echec lors de la transaction\n" + ex.message, "Erreur", Messagebox.OK, Messagebox.ERROR)
            } finally {
                objet = clazz.newInstance()
                rafraichirField()
                rafraichirList()
                activerBoutons(false)
                //actualiserEcheance(true)
            
            } 
        
        }
        else{
            Messagebox.show("Veuillez vérifier les champs : \n \n * Projet  \n * Client  \n * Phase" , "Erreur", Messagebox.OK, Messagebox.ERROR)            
        }
    }
    
    def update(){
       	actualiserValeurAssociation(false)
        try {
            if (verifierDatePE()){
                objet = getService().update(objet) 
                paiementService.onUpdatePaiement(objet)
            }
            else{
                Messagebox.show("La date d'encaissement doit être inférieure ou égale à la date du paiement ", "Erreur", Messagebox.OK, Messagebox.ERROR)   
            
            }
        }
        catch(Exception e) {
            logger.error "Error: ${e.message}", e
            Messagebox.show("", "Erreur", Messagebox.OK, Messagebox.ERROR)
        }
        finally {
            activerBoutons(false)
            objet = clazz.newInstance()
            rafraichirField()
            rafraichirList()
            //actualiserEcheance(true)
        } 
    }
        
    def verifierEcheancesPaiement(){
        //        def lst = objet.echeances
        //        Double montantEcheances =0
        //        if (lst != null && lst.size() >0){
        //            lst.each{ln ->
        //                montantEcheances += ln.montantEcheance
        //            }
        //            
        //        }
        //        if (montantEcheances <= objet.montant){
        //            return false
        //        }
        //        else{
        //            return true
        //        }
        return true
    }
    
    def ActualiserClientProjet(sender,actualiser){
        
        if (sender == "projet"){
            if (projetSelected != null){
                clients  = phaseService.getClientByProjet(projetSelected,utilisateur)

                if(clients && clients.size() > 0)
                clientSelected = clients.get(0)
                else
                clientSelected = null
            
                if(actualiser){                  
                    new AnnotateDataBinder(this.getFellow("coclients")).loadAll()
                }
            }
            else if (projetSelected == null){
                clearAssoc()
            }
        } else if (sender == "client"){
            //            projets =  phaseService.getProjetByClient(clientSelected)
            //            
            //            new AnnotateDataBinder(this.getFellow("filterprojet")).loadAll()
            //            new AnnotateDataBinder(this.getFellow("coprojets")).loadAll()
            //            new AnnotateDataBinder(this.getFellow("fieldcoprojetsbis")).loadAll()   
        }
        
        if(projetSelected && clientSelected){
            actualiserPhase(actualiser)
        }

    }        
    
    def actualiserPhase(Boolean actualiser){
      
        phases = phaseService.getByClientEtProjet(clientSelected,projetSelected)                 
        
        if(phases != null && phases.size() > 0)
        phaseSelected = phases.get(0)
        else
        phaseSelected = null
        
        objet.phase = phaseSelected
        if (actualiser){
            new AnnotateDataBinder(this.getFellow("cophases")).loadAll()
            new AnnotateDataBinder(this.getFellow("infoBien")).loadAll()
        }
     
        actualiserEcheance(actualiser,phaseSelected)
        
    }
    
    def onSelectPhase(){
        objet.phase = phaseSelected  
    }
    
    def actualiserEcheance(Boolean actualiser, Phase phaseSelected){
        echeances = echeanceService.getEcheanceByPhase(phaseSelected)        
        def idd = echeances.id
        if (objet.echeances){
            for(e in objet.echeances){
                if (!idd.contains(e.id))
                {
                    echeances.add(e)      
                }     
                
            }
        }
        
        echeances = echeances.sort{it.date}
        
        
        //        if (phaseSelected){
        //            echeances = phaseSelected.echeances
        //        }
        //        echeancesSelected = null// = new ArrayList()
        if(actualiser){
            new AnnotateDataBinder(this.getFellow("lstecheances")).loadAll() 
        }
    }
        
    def delete(){
        paiementService.onDeletePaiement(objet)
        super.delete()        
    }        
    
    Boolean verifierDatePE(){
        def valide = true
        def dateDiff = 1 
        if (objet.dateEncaissement){
            dateDiff   = objet.dateEncaissement - objet.datePaiement
        }
        if (dateDiff < 0 ){
            valide = false
        }
        return valide
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
    
    def load() {
        utilisateur = Utilisateur.findById(utilisateur.id)        
        session.utilisateur = utilisateur
        try {            
            def map
            if(attributsAFiltrer == null) {
                map = getService().filtrer(clazz, filtre, sortedHeader, sortedDirection, ofs, maxNb,utilisateur)
            } else {
                map = getService().filtrer(clazz, filtre, attributsAFiltrer, sortedHeader, sortedDirection, ofs, maxNb,utilisateur)
            }
            tailleListe = map["tailleListe"]
            listeObjets = map["listeObjets"]

        } catch (Exception ex) {
            logger.error(ex)
        }        
    }
    
    def disableProjetClientPhase(Boolean disabled){
        
        this.getFellow("coprojets").disabled = disabled 
        this.getFellow("coclients").disabled = disabled 
        this.getFellow("cophases").disabled = disabled 
        
    }
    
    def select() {                    
        objet = objetSelected	
        afficherValeurAssociation()
        //article.lock()  //Ne peut etre utilisï¿½ que pour le base de donnï¿½e qui accepte le veruillage des enregisterments
        rafraichirField()
        activerBoutons(true)
        this.getFellow("coclients").disabled= true
        disableProjetClientPhase(true)
    }    

    def newRecord(){
        
        this.getFellow("westPanel").open = visible  
        
        this.getFellow("clientText").visible = false  
        this.getFellow("coclients").visible = true  
        
        
        this.getFellow("phaseText").visible = false 
        this.getFellow("cophases").visible = true 
        
        clearAssoc()
        new AnnotateDataBinder(this.getFellow("coprojets")).loadAll()
        
        disableProjetClientPhase(false)
        
    }
    
    def annulerSelection() {
        this.getFellow("lstObjet").clearSelection()
        objetSelected = null
        disableProjetClientPhase(false)
    }
    
    def importation(media) {
        String resultat = excelImporterService.importerPaiements(media)
        Messagebox.show(resultat, "Notification" ,  Messagebox.OK, Messagebox.INFORMATION)
        rafraichirList()
    }
    
    def clearAssoc(){
        projetSelected = null
        clientSelected = null
        phaseSelected = null
        new AnnotateDataBinder(this.getFellow("coprojets")).loadAll()
        new AnnotateDataBinder(this.getFellow("coclients")).loadAll()
        new AnnotateDataBinder(this.getFellow("cophases")).loadAll()
        this.getFellow("lstecheances").clearSelection()
        echeancesSelected = null// = new ArrayList()
    }
    
}
