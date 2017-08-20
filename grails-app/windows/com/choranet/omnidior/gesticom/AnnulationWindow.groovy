
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
 * Annulation Window Object
 **/
class AnnulationWindow extends SuperWindow {
    /**
     * Service pour la gestion de l'objet Annulation
     **/
    def annulationService
    def phaseService
    def projetService
    def clientService
    def statutService
    def bienService
    /**
     * Service jasper pour la generation des rapports
     **/
    def jasperService
    /**
     * Logger de la class AnnulationWindow
     **/
    private Log logger = LogFactory.getLog(AnnulationWindow.class)
    
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
    
    def utilisateur = session.utilisateur
                
    /**
     * Constructeur
     **/
    public AnnulationWindow (projetService,clientService,phaseService,annulationService) {
        super(Annulation.class,false)
        this.projetService = projetService
        this.clientService = clientService
        this.phaseService = phaseService
        this.annulationService = annulationService
        specialInit()
    }  

    protected SuperService getService() {
        return this.annulationService
    }
    
    /**
     * Generation du rapport pdf
     **/
    def genererRapportPdf() {
        String nomsociete = ChoraClientInfo.findByNomsociete("OMNIDIOR").nomsociete
        String titrerapport = "Rapport des Annulations"
        def listeObjetsExporter = getObjetsToExport()
        def reportDef = new JasperReportDef(name:'rapport_des_Annulations.jrxml',
            fileFormat:JasperExportFormat.PDF_FORMAT,
            reportData : listeObjetsExporter,
            parameters : [  'nomsociete':nomsociete, 'titrerapport':titrerapport ]
        )
        def bit = jasperService.generateReport(reportDef).toByteArray()
        def nom_fichier = "rapport_des_Annulations.pdf"
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
        String titrerapport = "Rapport des Annulations"
        def listeObjetsExporter = getObjetsToExport()
        def reportDef = new JasperReportDef(name:'rapport_des_Annulations.jrxml',
            fileFormat:JasperExportFormat.XLS_FORMAT,
            reportData : listeObjetsExporter,
            parameters : [  'nomsociete':nomsociete, 'titrerapport':titrerapport ]
        )
        def bit = jasperService.generateReport(reportDef).toByteArray()
        def nom_fichier = "rapport_des_Annulations.xls"
        Filedownload.save(bit, "application/file", nom_fichier);
        
    }
    
    def specialInit(){
        filtrer(false)
        //projetsFilter = projetService.getProjetByUtilisateur()
        projets = utilisateur.projets.toList().sort{it.code}//projetService.getProjetByUtilisateur(utilisateur)
        //        projets = projets.sort{it.code}
        if(projets.size() > 0)
        projetSelected = projets.get(0)
        else
        projetSelected = null
        
        //ActualiserClientProjet('projet',false,false)
        
    }
    /**
     * Fonction qui g�re l'initialisation des listes d'associations au niveau du constructeur
     **/
    def initialiserAssociation() {
        //        
        //        phases = Phase.list()		
        //        if(phases.size() > 0)
        //        phaseSelected = phases.get(0)
        //        else
        //        phaseSelected = null
                    
    }
    /**
     * Fonction qui permet de r�-initaliser l'association au niveau de l'interface
     * @param del si c'est une r�initionalisation apr�s une suppression ou non
     **/
        
    def reinitialiserAssociation(del) {
        
        //        if(del) {
        //            phases = Phase.list()
        //        }		
        //        if(phases.size() > 0)
        //        phaseSelected = phases.get(0)
        //        else
        //        phaseSelected = null

        if(del) {
            projets =  utilisateur.projets.toList()//projetService.getProjetByUtilisateur(utilisateur)//Projet.list()
        }
	
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
            objet.phase = phaseSelected    
        }
       
        clearAssoc()
                    
    }
    /**
     * Fonction qui fait la liaison entre l'association l'�l�ment selectionn� et la liste dans le crud
     **/
  
    def afficherValeurAssociation() {
        
        def binderprojet = new AnnotateDataBinder(this.getFellow("coprojets"))
        projetSelected = projets.find{ it.id == objet.phase.projet.id }
        binderprojet.loadAll()
        //        ActualiserClientProjet('projet',false,false)
        //        
        //        def binderclient = new AnnotateDataBinder(this.getFellow("coclients"))
        //        clientSelected = clients.find{ it.id == objet.phase.client.id }
        //        binderclient.loadAll()
        //        actualiserPhase(false,false)
        //        def binderphase = new AnnotateDataBinder(this.getFellow("cophases"))
        //        phaseSelected = phases.find{ it.id == objet.phase.id }
        //        binderphase.loadAll()

        this.getFellow("clientText").visible = true  
        new AnnotateDataBinder(this.getFellow("clientText")).loadAll()
         
        this.getFellow("coclients").visible = false  
        
        this.getFellow("phaseText").visible = true 
        new AnnotateDataBinder(this.getFellow("phaseText")).loadAll()
         
        this.getFellow("cophases").visible = false  
                    
    }
    
    def ActualiserClientProjet(sender,actualiser,actualiserMontant){
               
        if (sender == "projet"){
            if (projetSelected != null){
                clients  = phaseService.getClientByProjetAll(projetSelected,utilisateur)
                if(clients && clients.size() > 0){
                    clientSelected = clients.get(0)
                }
                else{
                    clientSelected = null
                }
                
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
        if (projetSelected && clientSelected)     {   
            actualiserPhase(actualiser,actualiserMontant) 
        }
    }    
    
    def actualiserPhase(actualiser,actualiserMontant){
        if (clientSelected && projetSelected){
            phases = phaseService.getByClientEtProjetAll(clientSelected,projetSelected)           
            if(phases != null && phases.size() > 0)
            phaseSelected = phases.get(0)
            else
            phaseSelected = null
            if (phaseSelected){
                actualiserMontantAnnule(actualiser,actualiserMontant)
            }
        }
        else{
            phases = null
            phaseSelected = null
        }
        
        if (actualiser){
            new AnnotateDataBinder(this.getFellow("filterphase")).loadAll()
            new AnnotateDataBinder(this.getFellow("cophases")).loadAll()
        }
    }
    
    def actualiserMontantAnnule(actualiser,exec){
        if(phaseSelected && exec){
            objet.montantAnnule = phaseSelected.trans_totalPaiements 
            
            if(actualiser){
                new AnnotateDataBinder(this.getFellow("fieldMontantAnnule")).loadAll()
            }
        }

    }
    
    def add()
    {
        if (projetSelected && clientSelected && phaseSelected){
            actualiserValeurAssociation()
            try {               
                getService().save(objet)
                objet.phase.statut = statutService.getByCode("10")//getByTypeEtIntitule("PHASE","ANNULEE")
                if (objet.phase.bien){
                    objet.phase.bien.statut = statutService.getByCode("04")//getByTypeEtIntitule("BIEN","LIBRE") 
                    objet.phase.bien = bienService.update(objet.phase.bien)
                }
                objet = phaseService.update(objet.phase)
            
                objet = clazz.newInstance()
                rafraichirField()
                rafraichirList()
                activerBoutons(false)
            } catch(Exception ex) {
                logger.error "Error: ${ex.message}", ex
                Messagebox.show("Echec lors de la transaction\n" + ex.message, "Erreur", Messagebox.OK, Messagebox.ERROR)
            } finally {
            }
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
    
    def delete() {
        
        objet.phase.statut = statutService.getByCode("07")  
        
        if (objet.phase.bien && objet.phase.bien.statut.code == "04"){
            def etatBien = getEtatBien(objet.phase.intitule) 
            objet.phase.bien.statut = statutService.getByCode(etatBien) 
            objet.phase.bien = bienService.update(objet.phase.bien)
        }
        objet.phase = phaseService.update(objet.phase)
        super.delete()
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
    
    def newRecord(){
        super.newRecord()
        this.getFellow("clientText").visible = false  
        this.getFellow("coclients").visible = true  
        
        
        this.getFellow("phaseText").visible = false 
        this.getFellow("cophases").visible = true  
        
        clearAssoc()
    }
    
    def chargerAnnulation(){
        listeObjets = annulationService.getAnnulationByUtilisateur(utilisateur,ofs,maxNb)
        def ls = annulationService.getAnnulationByUtilisateur(utilisateur)
        if (ls){
            tailleListe = ls.size()
        }
    }
    
        
    def getEtatBien(typePhase){
        def etatBien
        switch (typePhase){
            case "OPTION":
            etatBien = "25"//"En option"
            break
            case "RESERVATION":
            etatBien = "05"//"Réservé"
            break
            case "COMPROMIS":
            etatBien = "05"//"Réservé"
            break
            case "CONTRAT":
            etatBien = "06"//"Vendu"
        }        
        return etatBien
    }
    
    def clearAssoc(){
        projetSelected = null
        clientSelected = null
        phaseSelected = null
        new AnnotateDataBinder(this.getFellow("coprojets")).loadAll()
        new AnnotateDataBinder(this.getFellow("coclients")).loadAll()
        new AnnotateDataBinder(this.getFellow("cophases")).loadAll()
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
    }

}

