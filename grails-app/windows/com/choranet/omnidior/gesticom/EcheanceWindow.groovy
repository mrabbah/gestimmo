
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
import com.choranet.omnidior.gesticom.JournalPaiement;

/**
 * Echeance Window Object
 **/
class EcheanceWindow extends SuperWindow {
    /**
     * Service pour la gestion de l'objet Echeance
     **/
    def echeanceService
    def statutService
    def projetService
    def clientService
    def phaseService 
    def paiementService
    def journalPaiementService
    
    def utilisateur = session.utilisateur
    /**
     * Service jasper pour la generation des rapports
     **/
    def jasperService
    /**
     * Logger de la class EcheanceWindow
     **/
    private Log logger = LogFactory.getLog(EcheanceWindow.class)

                
    def statuts	
    def statutSelected
    
    //def projetsFilter
    def projets
    def projetSelected
    
    def clients
    def clientSelected
    
    def phases
    def phaseSelected
    
    def projetsF = new ArrayList()
    def projetSelectedF
    
    def clientsF = new ArrayList()
    def clientSelectedF
    
    def phasesF
    def phaseSelectedF
    
                
    /**
     * Constructeur
     **/
    public EcheanceWindow (statutService,projetService,clientService,phaseService,echeanceService) {
        super(Echeance.class,false)
        this.statutService = statutService
        this.projetService = projetService
        this.clientService = clientService
        this.phaseService = phaseService
        this.echeanceService = echeanceService
        specialInit()
    }  

    protected SuperService getService() {
        return this.echeanceService
    }
    
    /**
     * Generation du rapport pdf
     **/
    def genererRapportPdf() {
        String nomsociete = ChoraClientInfo.findByNomsociete("OMNIDIOR").nomsociete
        String titrerapport = "Rapport des Echeances"
        def listeObjetsExporter = getObjetsToExport()
        def reportDef = new JasperReportDef(name:'rapport_des_Echeances.jrxml',
            fileFormat:JasperExportFormat.PDF_FORMAT,
            reportData : listeObjetsExporter,
            parameters : [  'nomsociete':nomsociete, 'titrerapport':titrerapport ]
        )
        def bit = jasperService.generateReport(reportDef).toByteArray()
        def nom_fichier = "rapport_des_Echeances.pdf"
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
        String titrerapport = "Rapport des Echeances"
        def listeObjetsExporter = getObjetsToExport()
        def reportDef = new JasperReportDef(name:'rapport_des_Echeances.jrxml',
            fileFormat:JasperExportFormat.XLS_FORMAT,
            reportData : listeObjetsExporter,
            parameters : [  'nomsociete':nomsociete, 'titrerapport':titrerapport ]
        )
        def bit = jasperService.generateReport(reportDef).toByteArray()
        def nom_fichier = "rapport_des_Echeances.xls"
        Filedownload.save(bit, "application/file", nom_fichier);
        
    }
    def specialInit(){
        
        filtrer(false)   
        statuts = statutService.getByType("ECHEANCE")
        if(statuts.size() > 0)
        statutSelected = statuts.get(0)
        else
        statutSelected = null

        projets = utilisateur.projets.toList()  
        projets = projets.sort{it.code}
        if(projets.size() > 0)
        projetSelected = projets.get(0)
        else
        projetSelected = null
                
        def tempProjets = projets //utilisateur.projets.toList().sort{it.code}
        for (pj in tempProjets){
            projetsF.add(pj.toString())
        }
        
        def tempClients = utilisateur.clients.toList().sort{it.nom}
        for (cl in tempClients){
            clientsF.add(cl.toString())
        }
        
        phasesF = ['OPTION', 'RESERVATION', 'COMPROMIS','CONTRAT']//phaseService.getPhaseByUtlisateur(utilisateur)        
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
            statuts = statutService.getByType("ECHEANCE")
        }	
        if(statuts.size() > 0)
        statutSelected = statuts.get(0)
        else
        statutSelected = null
        
        //        if(del) {
        //            projets =  utilisateur.projets.toList() //Projet.list()
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
        if (add == true){                
            objet.phase = phaseSelected
        }
        //        objet.phase.projet = projetSelected
        //        objet.phase.client = clientSelected
        
                
        objet.intitulePhaseEcheance = objet.phase.intitule
        objet.projet = objet.phase.projet.toString()
        objet.client = objet.phase.client.toString()
        
        if(objet.phase.bien){
            objet.bien = objet.phase.bien.numero
        }
        else{
            objet.bien = "" 
        }
        
        objet.statut = statutSelected
        if(statuts.size() > 0) {
            def binderstatut = new AnnotateDataBinder(this.getFellow("costatuts"))
            statutSelected = statuts.get(0)
            binderstatut.loadAll()
        }
        else
        statutSelected = null
        
        //        actualiserPhase(false)
        //        if(phases.size() > 0) {
        //            def binderphase = new AnnotateDataBinder(this.getFellow("cophases"))
        //            phaseSelected = phases.get(0)
        //            binderphase.loadAll()
        //        }
        //        else
        //        phaseSelected = null
        
        //        objet.phase.projet = projetSelected
        //        if(projets.size() > 0) {
        //            def binderprojet = new AnnotateDataBinder(this.getFellow("coprojets"))
        //            projetSelected = projets.get(0)
        //            binderprojet.loadAll()
        //        }
        //        else
        //        projetSelected = null
        //        
        //        //        objet.phase.client = clientSelected
        //        ActualiserClientProjet('projet',false)                
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
        
        def binderstatut =  new AnnotateDataBinder(this.getFellow("costatuts"))
        statutSelected = statuts.find{ it.id == objet.statut.id}
        binderstatut.loadAll()
        
        def binderprojet = new AnnotateDataBinder(this.getFellow("coprojets"))
        projetSelected = projets.find{ it.id == objet.phase.projet.id }
        binderprojet.loadAll()
        
        
        //ActualiserClientProjet('projet',false)
        //        clients  = []
        //        clients.add(objet.phase.client)
        //        def binderclient = new AnnotateDataBinder(this.getFellow("coclients"))
        //        clientSelected = clients.find{ it.id == objet.phase.client.id }
        //        binderclient.loadAll()	
        //        
        //        //actualiserPhase(false)
        //        if (objet.phase){
        //
        //            phases = []
        //            phases.add(objet.phase)
        //            def binderphase = new AnnotateDataBinder(this.getFellow("cophases"))        
        //      
        //            phaseSelected = phases.find{ it.id == objet.phase.id }//phaseService.getPhaseByPaiement(phases,objet)
        //            //phaseObjet = phaseSelected
        //            binderphase.loadAll()
        //        }

        this.getFellow("clientText").visible = true  
        new AnnotateDataBinder(this.getFellow("clientText")).loadAll()
         
        this.getFellow("coclients").visible = false  
        
        this.getFellow("phaseText").visible = true 
        new AnnotateDataBinder(this.getFellow("phaseText")).loadAll()
         
        this.getFellow("cophases").visible = false  
                    
    }
    
    def actualiserNumeroEcheance(){
        def num = echeanceService.getEcheanceCountByClientEtProjet(clientSelected,projetSelected)
        if (num != null){
            num = num + 1
        }
        objet.numero = num
        new AnnotateDataBinder(this.getFellow("fieldNumero")).loadAll()
    }
    
    def ActualiserClientProjet(sender,actualiser){
        
        if (sender == "projet"){
            if (projetSelected != null){
                clients  = phaseService.getClientByProjet(projetSelected,utilisateur)
                if(clients &&  clients.size() > 0)
                clientSelected = clients.get(0)
                else
                clientSelected = null
            
                if(actualiser){
                    new AnnotateDataBinder(this.getFellow("filterclient")).loadAll()
                    new AnnotateDataBinder(this.getFellow("coclients")).loadAll()
                }
            }
            else if (projetSelected == null){
                clearAssoc()
            }
        }
        
        else if (sender == "client"){
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
    
    def actualiserPhase(actualiser){
      
        phases = phaseService.getByClientEtProjet(clientSelected,projetSelected)           
      
        
        if(phases != null && phases.size() > 0)
        phaseSelected = phases.get(0)
        else
        phaseSelected = null
        
        if (actualiser){
            new AnnotateDataBinder(this.getFellow("cophases")).loadAll()
        }
    }
    
    def filtrer(Boolean loadliste = true) {        
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
        
    def chargerEcheance(){
        listeObjets = echeanceService.getEcheanceByUtilisateur(utilisateur,ofs,maxNb)
        def ls = echeanceService.getEcheanceByUtilisateur(utilisateur)
        if (ls){
            tailleListe = ls.size()
        }
    }
    
    def calculerDateAlerte(){
        
        Date dateAlerte
        dateAlerte = objet.date.plus(-2)

        if (dateAlerte.day == 6){
            dateAlerte =  objet.date.plus(-3)
        }
        else if(dateAlerte.day == 7){
            dateAlerte =  objet.date.plus(-4)  
        }
        objet.dateAlerte  = dateAlerte
        
    }
    
    def add(){
        //calculerDateAlerte() 
        if (projetSelected && clientSelected && phaseSelected){
            objet.dateAlerte = objet.date
            if (objet.montantPaye == null){
                objet.montantPaye = 0  
            }
            super.add()
        }
        else{
            Messagebox.show("Veuillez vérifier les champs : \n \n * Projet  \n * Client  \n * Phase" , "Erreur", Messagebox.OK, Messagebox.ERROR)            
        }
    }
    
    def update(){
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
    
    def delete(){
        def lstPaiement = paiementService.getPaiementByEcheance(objet)//JournalPaiement.findAllByEcheance(objet)

        //        def lstPaiement = lstJournalPaiement*.paiement

        //        for (jp in lstJournalPaiement){

        ////            Paiement p = jp.paiement
        //            //            p.removeFromEcheances(objet)
        //            //            p = paiementService.update(p)
        //            //            lstPaiement.add(p)            
        //        }
        //       
        //        if (lstPaiement){
        //            for (p in lstPaiement){
        //                p.removeFromEcheances(objet)                
        //                paiementService.onUpdatePaiement(p)
        //            }
        //        }
        super.delete()
    }
    
    def actualiser(){
        chargerEcheance()  
        new AnnotateDataBinder(this.getFellow("lstObjet")).loadAll() 
    }
    
    def load() {
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
        disableProjetClientPhase(false)
        
        this.getFellow("clientText").visible = false  
        this.getFellow("coclients").visible = true  
        
        
        this.getFellow("phaseText").visible = false 
        this.getFellow("cophases").visible = true  
        
        clearAssoc()
        new AnnotateDataBinder(this.getFellow("coprojets")).loadAll()
    }
    
    def annulerSelection() {
        this.getFellow("lstObjet").clearSelection()
        objetSelected = null
        disableProjetClientPhase(false)
    }
    
    def clearAssoc(){
        projetSelected = null
        clientSelected = null
        phaseSelected = null
        new AnnotateDataBinder(this.getFellow("coprojets")).loadAll()
        new AnnotateDataBinder(this.getFellow("coclients")).loadAll()
        new AnnotateDataBinder(this.getFellow("cophases")).loadAll()
    }
    
}
