
package com.choranet.omnidior.gesticom
    

import org.zkoss.zul.*
import org.zkoss.zkplus.databind.*
import org.apache.commons.logging.Log 
import org.apache.commons.logging.LogFactory
import org.codehaus.groovy.grails.plugins.jasper.JasperExportFormat
import org.codehaus.groovy.grails.plugins.jasper.JasperReportDef
import org.zkoss.zk.ui.event.*;

import org.zkoss.util.media.AMedia;
import org.zkoss.util.media.Media;
import java.util.HashMap;
import java.util.Map;
import org.zkoss.zk.ui.Executions
import com.choranet.omnidior.gesticom.util.*;
import com.choranet.omnidior.gesticom.util.BienData
import com.choranet.zk.BienRenderer;
import com.choranet.zk.TreeNodeConverter;
import com.choranet.omnidior.gesticom.util.*
import java.io.*;


/**
 * Phase Window Object
 **/
class PhaseWindow extends SuperWindow implements EventListener {
    /**
     * Service pour la gestion de l'objet Phase
     **/
    def bienService
    def phaseService
    def statutService
    def projetService
    def clientService
    def paiementService
    def bienRenderer
    def documentService
    def utilisateurService
    def excelImporterService
    
    def listDocuments = new ArrayList()
    //Date datePI
    
    /**
     * Service jasper pour la generation des rapports
     **/
    def jasperService
    /**
     * Logger de la class PhaseWindow
     **/
    private Log logger = LogFactory.getLog(PhaseWindow.class)    
                
    /**
     * liste de bien
     **/	
    def biens	
    /**
     * bien  selectionn�
     **/
    def bienSelected
                
    /**
     * liste de commercialResponsable
     **/	
    def commercialResponsables	
    /**
     * commercialResponsable  selectionn�
     **/
    def commercialResponsableSelected
                
    /**
     * liste de notaire
     **/	
    def notaires	
    /**
     * notaire  selectionn�
     **/
    def notaireSelected
                
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
                
    /**
     * paiements
     **/
       
    def villes
    def villeSelected
    
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
    
    def modePaiements
    def modePaiementSelected
    
    DefaultTreeModel treeProjet
    
    def mtr

    def statuts
    def statutSelected
                
    def statutPaiements
    def statutPaiementSelected
    
    def typePaiements
    def typePaiementSelected
    
    def sexes = ['F','M']
    def sexeSelected    
    
    def lstPaiements
    def paiementSelected
    def nouveauPaiement
    
    def typePhase = session.typePhase
    
    def nationalites
    def nationaliteSelected
    
    def utilisateur = session.utilisateur
    
    def nouveauDocument
    def documentSelected
    def documents
    
    def nouveauClient  
    Boolean isNew
    def utilisat       
    String idClient
    
    def moulinetteService
    
    /**
     * Constructeur
     **/
        
    public PhaseWindow (phaseService,statutService,projetService,clientService, bienService) {

        super(Phase.class,false)
        //this.typePhase = typePhase
        this.bienService= bienService
        this.phaseService = phaseService
        this.statutService = statutService
        this.projetService = projetService
        this.clientService = clientService
        specialInit()
        
    }  

    protected SuperService getService() {
        return this.phaseService
    }
    
    /**
     * Generation du rapport pdf
     **/
    def genererRapportPdf() {
        String nomsociete = ChoraClientInfo.findByNomsociete("OMNIDIOR").nomsociete
        String titrerapport = "Rapport des Phases"
        def listeObjetsExporter = getObjetsToExport()
        def reportDef = new JasperReportDef(name:'rapport_des_Phases.jrxml',
            fileFormat:JasperExportFormat.PDF_FORMAT,
            reportData : listeObjetsExporter,
            parameters : [  'nomsociete':nomsociete, 'titrerapport':titrerapport ]
        )
        def bit = jasperService.generateReport(reportDef).toByteArray()
        def nom_fichier = "rapport_des_Phases.pdf"
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
        String titrerapport = "Rapport des Phases"
        def listeObjetsExporter = getObjetsToExport()
        def reportDef = new JasperReportDef(name:'rapport_des_Phases.jrxml',
            fileFormat:JasperExportFormat.XLS_FORMAT,
            reportData : listeObjetsExporter,
            parameters : [  'nomsociete':nomsociete, 'titrerapport':titrerapport ]
        )
        def bit = jasperService.generateReport(reportDef).toByteArray()
        def nom_fichier = "rapport_des_Phases.xls"
        Filedownload.save(bit, "application/file", nom_fichier);
        
    }
    
    def actualiserInformationBien(b) {
        bienSelected = b;
        objet.montantVente = bienSelected.prixVente
        new AnnotateDataBinder(this.getFellow("fieldMontantVente")).loadAll()
        new AnnotateDataBinder(this.getFellow("fieldTypebien")).loadAll()
        new AnnotateDataBinder(this.getFellow("fieldNumero")).loadAll()
        new AnnotateDataBinder(this.getFellow("fieldSuperficie")).loadAll()
        new AnnotateDataBinder(this.getFellow("fieldPrixVente")).loadAll()
        new AnnotateDataBinder(this.getFellow("fieldContenance")).loadAll()
    }    
    
    def specialInit(){
        
        //String codeMenu = Executions.getCurrent().getParameter("cm")
        idClient = Executions.getCurrent().getParameter("ic")
        Boolean initClient = false
        
        
        if ( idClient ){
            initClient = true
            objet = clazz.newInstance()
            clients = clientService.getClientById(Long.parseLong(idClient))
            clientSelected = clients.get(0)//clients.find{it.id == idClient }
            //newRecord()
            
            objet.dateContact = new Date() 
            
            //            this.getFellow("westPanel").open = true 
            //            this.getFellow("coclients").disabled= false
            //            this.getFellow("coprojets").disabled= false
                
            
        
        }
        
        if (!initClient){
            clients = utilisateur.clients.toList()//.sort{it.nom}
            filtrer(false)   
            
            if(clients.size() > 0){            
                clientSelected = clients.get(0)
            }        
            else{
                clientSelected = null        
            }
        }
                        
        bienRenderer = new BienRenderer(this)
        statuts = statutService.getByType("PHASE")
        //statuts = statuts.sort{it.code}
        if(statuts.size() > 0)
        statutSelected = statuts.get(0)
        else
        statutSelected = null
        
         
        projets = utilisateur.projets.toList()

        statutPaiements = statutService.getByType("PAIEMENT")
        if(statutPaiements.size() > 0)
        statutPaiementSelected = statutPaiements.get(0)
        else
        statutPaiementSelected = null
        
        typePaiements = statutService.getByType("PAIEMENTTYPE")
        if(typePaiements.size() > 0)
        typePaiementSelected = typePaiements.get(0)
        else
        typePaiementSelected = null
        
        nouveauPaiement = new Paiement()
        nouveauPaiement.datePaiement = new Date()
        nouveauPaiement.montant = 0
       
        sexeSelected = sexes[0]
                
        //objet.dateDebut = new Date()        
        // clientSelected.dateContact = new Date()  
        
        controllerPhase()
               
    }
    
    def controllerPhase(){
        typePhase = session.typePhase
        nouveauPaiement.intitulePhasePaiement = typePhase
    }
    
    /**
     * Fonction qui g�re l'initialisation des listes d'associations au niveau du constructeur
     **/
   
    def initialiserAssociation() {
                    
        commercialResponsables = Utilisateur.list()		
        if(commercialResponsables.size() > 0)
        commercialResponsableSelected = commercialResponsables.find {it.id.equals(session.utilisateur.id)}
        else
        commercialResponsableSelected = null
                    
        notaires = Notaire.list().sort{it.nom}		
        if(notaires.size() > 0)
        notaireSelected = notaires.get(0)
        else
        notaireSelected = null
        
        modePaiements = ModePaiement.list()		
        if(modePaiements.size() > 0)
        modePaiementSelected = modePaiements.get(0)
        else
        modePaiementSelected = null
        
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
        
        bienSelected = null
                    
    }
    /**
     * Fonction qui permet de r�-initaliser l'association au niveau de l'interface
     * @param del si c'est une r�initionalisation apr�s une suppression ou non
     **/
    def reinitialiserAssociation(del) {
                    	
        bienSelected = null
                    	
        //        if(del) {
        //            commercialResponsables = Utilisateur.list()
        //        }	
        //        if(commercialResponsables.size() > 0)
        //        commercialResponsableSelected = commercialResponsables.get(0)
        //        else
        //        commercialResponsableSelected = null
                    	
        if(del) {
            notaires = Notaire.list().sort{it.nom}
        }	
        if(notaires.size() > 0)
        notaireSelected = notaires.get(0)
        else
        notaireSelected = null
                    	
        if(del) {
            projets = utilisateur.projets.toList().sort{it.code}
        }	
        if(projets.size() > 0){
            projetSelected = projets.get(0)
            //genererHierarchieBien(true)
        }
        else{
            projetSelected = null
        }   	
                
        if(del) {
            modePaiements = ModePaiement.list()
        }	
        if(modePaiements.size() > 0)
        modePaiementSelected = modePaiements.get(0)
        else
        modePaiementSelected = null               
        
        if(del) {
            statuts = statutService.getByType("PHASE")
        }	
        if(statuts.size() > 0)
        statutSelected = statuts.get(0)
        else
        statutSelected = null
        
        if(del) {
            statutPaiements = statutService.getByType("PAIEMENT")
        }	
        if(statutPaiements.size() > 0)
        statutPaiementSelected = statutPaiements.get(0)
        else
        statutPaiementSelected = null
        
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
            typePaiements = typeService.getByType("PAIEMENTTYPE")
        }	
        if(typePaiements.size() > 0)
        typePaiementSelected = typePaiements.get(0)
        else
        typePaiementSelected = null
                            	                    
    }
    /**
     * Fonction qui copie la valeur de l'association � l'�l�ment courant
     **/
  
    def actualiserValeurAssociation(isAdd = null) {        
        
        if(bienSelected){                  
            objet.bien = bienSelected
        }
        bienSelected = null
                    		
        objet.commercialResponsable = commercialResponsableSelected
        commercialResponsableSelected = utilisateur

                    		
        objet.notaire = notaireSelected
        if(notaires.size() > 0) {
            def bindernotaire = new AnnotateDataBinder(this.getFellow("conotaires"))
            notaireSelected = notaires.get(0)
            bindernotaire.loadAll()
        }
        else
        notaireSelected = null
                
        objet.projet = projetSelected        
                    		
        objet.client = clientSelected                                                
        
        objet.statut = statutSelected
        if(statuts.size() > 0) {
            def binderstatut = new AnnotateDataBinder(this.getFellow("costatuts"))
            statutSelected = statuts.get(0)
            binderstatut.loadAll()
        }
        else
        statutSelected = null
        /*   
        nouveauPaiement.modePaiement = modePaiementSelected
        if(modePaiements.size() > 0) {
        def bindermodePaiement = new AnnotateDataBinder(this.getFellow("comodePaiements"))
        modePaiementSelected = modePaiements.get(0)
        bindermodePaiement.loadAll()
        }
        else
        modePaiementSelected = null
        
        nouveauPaiement.type = modePaiementSelected
        if(typePaiements.size() > 0) {
        def bindertypePaiement = new AnnotateDataBinder(this.getFellow("cotypePaiementsbis"))
        typePaiementSelected = typePaiements.get(0)
        bindertypePaiement.loadAll()
        }
        else
        modePaiementSelected = null
        
        nouveauPaiement.statut = statutPaiementSelected
        if(statutPaiements.size() > 0) {
        def binderstatutPaiement = new AnnotateDataBinder(this.getFellow("costatutPaiementbis"))
        statutPaiementSelected = statutPaiements.get(0)
        binderstatutPaiement.loadAll()
        }
        else
        modePaiementSelected = null
         */
                          
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

        if (!objet.dateDebut){
            objet.dateDebut = new Date()
            new AnnotateDataBinder(this.getFellow("fieldDateDebut")).loadAll()
            actualiserDateExpiration() 
        }
    }
    /**
     * Fonction qui fait la liaison entre l'association l'�l�ment selectionn� et la liste dans le crud
     **/
 
    def afficherValeurAssociation() {
        //this.getFellow("coprojets").disabled = true; 	      
        this.getFellow("coprojets").visible = false

        bienSelected = objet.bien
        //binderbien.loadAll()
        
        def bindercommercialResponsable = new AnnotateDataBinder(this.getFellow("cocommercialResponsables"))
        commercialResponsableSelected = commercialResponsables.find{it.id == objet.commercialResponsable.id}
        bindercommercialResponsable.loadAll()

        notaireSelected = null        
        def bindernotaire = new AnnotateDataBinder(this.getFellow("conotaires"))
        if (objet.notaire){
            notaireSelected = notaires.find{ it.id == objet.notaire.id }        
        }
        bindernotaire.loadAll()
        
        def binderprojet = new AnnotateDataBinder(this.getFellow("coprojets"))
        projetSelected = projets.find{ it.id == objet.projet.id }        
        binderprojet.loadAll()
                    		        
        clientSelected = objet.client //clients.find{it.id == objet.client.id }  
        new AnnotateDataBinder(this.getFellow("clientGrid")).loadAll()
 
        sourceInformationSelected = null
        def bindersourceInformation = new AnnotateDataBinder(this.getFellow("cosourceInformations"))
        
        if(objet.sourceInformation){
            sourceInformationSelected = sourceInformations.find{ it.id == objet.sourceInformation.id }             
        }
        bindersourceInformation.loadAll()
         
        canalSelected = null
        def bindercanal = new AnnotateDataBinder(this.getFellow("cocanals"))
        if(objet.canal){
            canalSelected = canals.find{ it.id == objet.canal.id }                         
        }
        bindercanal.loadAll()
                
        def binderstatut =  new AnnotateDataBinder(this.getFellow("costatuts"))
        statutSelected = statuts.find{ it.id == objet.statut.id}        
        binderstatut.loadAll()
        
        lstPaiements = phaseService.getPaiement(objet)
        new AnnotateDataBinder(this.getFellow("lstpaiements")).loadAll()
        calculerMontantReliquat()
        activerChampsPhase() 
        
        loadPhaseDocuments()
    }
    
    def genererHierarchieBien(test){
        if(projetSelected) {            
            def biensParents = bienService.getBienRootProjet(projetSelected)
            biens = new DefaultTreeModel(new DefaultTreeNode(null, construireArbre(biensParents)))
            if(test) {
                new AnnotateDataBinder(this.getFellow("treebien")).loadAll()
                this.getFellow("treebienRow").visible = true
                new AnnotateDataBinder(this.getFellow("treebienRow")).loadAll()
            }
        }
    }
   
    def construireArbre(listDroits) {
        def result = []
        for(element in listDroits) {
            def listFils = bienService.getBienByBienParent (element)
            if(listFils == null || listFils.size() == 0) {
                result.add(new DefaultTreeNode(element))
            } else {
                result.add(new DefaultTreeNode(element, construireArbre(listFils)))
            }
        }
        return result
    }
    
    def etapeSuivante()
    {
        Boolean valide = true 
        if (!verifierClient("OTHER")){
            Messagebox.show("Le champs CIN est obligatoire" , "Erreur", Messagebox.OK, Messagebox.ERROR)            
            valide= false
        }
            
        
        if (objet.statut.intitule == "CLOTUREE"){
            Messagebox.show("La phase a déjà été clôturée, impossible de passer à l'étape suivante" , "Erreur", Messagebox.OK, Messagebox.ERROR)
            valide= false  
        }
        
        if(!verifierPaiements())
        {
            Messagebox.show("Un ou plusieurs paiements ne sont pas en statut [Encaissé]" , "Erreur", Messagebox.OK, Messagebox.ERROR)
            valide= false 
            
        }
        if (objet.statut.intitule == "ANNULEE"){
            Messagebox.show("La phase a été annulée, impossible de passer à l'étape suivante" , "Erreur", Messagebox.OK, Messagebox.ERROR)
            valide= false  
        }
        
        
        if (valide){
            actualiserValeurAssociation()
            //objet.bien = bienSelected        

            phaseService.passerEtapeSuivante(objet)  

            typePhase = objet.intitule
            session.typePhase = typePhase
            nouveauPaiement.intitulePhasePaiement = typePhase   
            new AnnotateDataBinder(this.getFellow("rowPaiement")).loadAll()
            def etatBien = getEtatBien(typePhase)                            
            if(objet.bien){
                objet.bien.statut = Statut.findByCode(etatBien)                
                objet.bien =  bienService.update(objet.bien)
                if (objet.bien.bienParent){
                    bienService.updateEtatBienParent(objet.bien.bienParent)
                }
            }
           
            projetService.updateEtatProjet(objet.projet)
            Messagebox.show("Passage à la phase[ " + typePhase + "] terminé avec succès" , "Confirmation", Messagebox.OK, Messagebox.INFORMATION)
            activerBoutons(false)
            objet = clazz.newInstance() 
            objet.montantVente = 0
            objet.dateContact = new Date()
            rafraichirField()
            rafraichirList()
        
            listeObjets = phaseService.getPhaseByIntitule(typePhase,utilisateur)
            new AnnotateDataBinder(this.getFellow("lstObjet")).loadAll()  
            
            this.getFellow("westPanel").title = "PHASE : " + typePhase
            this.getFellow("PHASETITLElst").value = "PHASE : " + typePhase
            
            //new AnnotateDataBinder(this.getFellow("")).loadAll()  
        }                
    }    
    
    def afficherAffectation(visible){
        this.getFellow("rowInfosAffectation").visible = visible
        new AnnotateDataBinder(this.getFellow("rowInfosAffectation")).loadAll()
    }

    def addPaiement(){

        if(nouveauPaiement.montant == 0)
        {
            Messagebox.show("Le montant du paiement est obligatoire ", "Erreur", Messagebox.OK, Messagebox.ERROR)   
        }
        else{
            def dateDiff = 1 
            if (nouveauPaiement.dateEncaissement){
                dateDiff   = nouveauPaiement.dateEncaissement - nouveauPaiement.datePaiement
            }
            if (dateDiff >= 0 ){
                  
                nouveauPaiement.type = typePaiementSelected
                nouveauPaiement.modePaiement = modePaiementSelected  
                
                if(modePaiementSelected.type.contains('ESPECE')){
                    nouveauPaiement.statut = statutService.getByCode("12")  
                }
                else{
                    nouveauPaiement.statut = statutPaiementSelected
                }
                nouveauPaiement.phase = objet
                nouveauPaiement.intitulePhasePaiement = objet.intitule
                if (nouveauPaiement.numeroPaiement == 0){                            
                    nouveauPaiement.numeroPaiement = paiementService.genererNumeroPaiement(objet)
                }
                
                nouveauPaiement.projet = objet.projet.toString()
                nouveauPaiement.client = objet.client.toString()
                if(objet.bien){
                    nouveauPaiement.bien = objet.bien.toString()
                }
                else{
                    nouveauPaiement.bien = "" 
                }
                
                paiementService.save(nouveauPaiement)                       
                lstPaiements = phaseService.getPaiement(objet)//ByProjetClient(projetSelected,clientSelected)
                new AnnotateDataBinder(this.getFellow("lstpaiements")).loadAll()
                nouveauPaiement = Paiement.newInstance()               
                calculerMontantReliquat()
                rafraichirPaiement()
                nouveauPaiement.datePaiement = new Date()
                nouveauPaiement.montant = 0
            }
            else if(dateDiff < 0){
                Messagebox.show("La date d'encaissement doit être inférieure ou égale à la date du paiement ", "Erreur", Messagebox.OK, Messagebox.ERROR)
            }
           
        }
    }
    
    def rafraichirPaiement() {

        
        if(modePaiements.size() > 0) {
            def bindermodePaiement = new AnnotateDataBinder(this.getFellow("comodePaiementsbis"))
            modePaiementSelected = modePaiements.get(0)
            bindermodePaiement.loadAll()
        }
        else
        modePaiementSelected = null
        
       
        if(typePaiements.size() > 0) {
            // def bindertypePaiement = new AnnotateDataBinder(this.getFellow("cotypePaiementsbis")) 
            typePaiementSelected = typePaiements.get(0)
            // bindertypePaiement.loadAll()
        }
        else
        typePaiementSelected = null
        
        
        if(statutPaiements.size() > 0) {
            def binderstatutPaiement = new AnnotateDataBinder(this.getFellow("costatutPaiementbis"))
            statutPaiementSelected = statutPaiements.get(0)
            binderstatutPaiement.loadAll()
        }
        else
        statutPaiementSelected = null 
        
        this.getFellow("fieldPaiementdatePaiementbis").value = new Date()
        this.getFellow("fieldPaiementdateEncaissementbis").value = null
        this.getFellow("fieldPaiementmontantbis").value = 0
    }
        
    def selectPaiement(){
   
        nouveauPaiement = paiementSelected
        
        //def bindertypePaiement =  new AnnotateDataBinder(this.getFellow("cotypePaiementsbis"))
        typePaiementSelected = typePaiements.find{ it.id == nouveauPaiement.type.id}
        //bindertypePaiement.loadAll()
        
        def bindermodePaiement =  new AnnotateDataBinder(this.getFellow("comodePaiementsbis"))
        modePaiementSelected = modePaiements.find{ it.id == nouveauPaiement.modePaiement.id}
        bindermodePaiement.loadAll()
        
        def binderstatutPaiement =  new AnnotateDataBinder(this.getFellow("costatutPaiementbis"))
        statutPaiementSelected = statutPaiements.find{ it.id == nouveauPaiement.statut.id}
        binderstatutPaiement.loadAll()
        
        new AnnotateDataBinder(this.getFellow("gridPaiementbis")).loadAll()
        
    }
    
    def verifierPhase(String action){
        
        Boolean valide = true 
        
        if (action.equals("add")){
            
            if (!clientSelected ){
                Messagebox.show("Veuillez sélectionner un client pour la phase " , "Erreur", Messagebox.OK, Messagebox.ERROR)            
                valide= false
            }
       
            
            if (!projetSelected){
                Messagebox.show("Veuillez sélectionner un projet pour la phase " , "Erreur", Messagebox.OK, Messagebox.ERROR)            
                valide= false
            }
            
            if(bienSelected){

                if( !verifierBien(bienSelected)){
                    valide = false //phaseService.existePhaseActive(objet.bien) 
                    Messagebox.show("Le bien selectionné n'est pas en statut [libre] ", "Echec Ajout", Messagebox.OK, Messagebox.ERROR)
                }

            }
        }
        else if (action.equals("update")){
            if(bienSelected){
                if (objet.bien != bienSelected){
                    if( !verifierBien(bienSelected)){
                        valide = false //phaseService.existePhaseActive(objet.bien) 
                        Messagebox.show("Le bien selectionné n'est pas en statut [libre] ", "Echec Ajout", Messagebox.OK, Messagebox.ERROR)
                    }
                }

            }
        }

                        
        if (!verifierClient(typePhase)){
            Messagebox.show("Le champs CIN est obligatoire" , "Erreur", Messagebox.OK, Messagebox.ERROR)            
            valide= false
        }         
        
        if  (objet.dateFin && objet.dateDebut >= objet.dateFin) {
            Messagebox.show("La date fin doit être inférieure à la date début" , "Erreur", Messagebox.OK, Messagebox.ERROR)            
            valide= false  
        }
                             
        if (listDocuments && listDocuments.size()> 5 ){
            valide = false 
            Messagebox.show("Vous avez dépassé la limie [5] des documents joints", "Echec Ajout", Messagebox.OK, Messagebox.ERROR)
                
        }
        
        return valide
    } 
    
    def update(){
        
        Boolean valide = verifierPhase("update")   
        if (valide){
            def idBien = clientBien()
            actualiserValeurAssociation()
            if (objet.intitule != "OPTION" && objet.client.statut != "CLIENT"){
                objet.client.statut = statutService.getByCode("24")//getByTypeEtIntitule("CLIENT","CLIENT")
                
            }            
   
            try {
                
                calculerMontantReliquat()
                modifierStatutPhase() // modification statut phase en expiré si date fin < date j ou l'inverse 
                objet = phaseService.update(objet) 
                ajouterDocuments()
                
                objet.projet = projetService.updateEtatProjet(objet.projet)
                def etatBien = getEtatBien(objet.intitule) 
                if(objet.bien){
                    objet.bien.statut = statutService.getByCode(etatBien)//statutService.getByTypeEtIntitule("BIEN",etatBien)
                    objet.bien.client = objet.client.toString()
                    objet.bien = bienService.update(objet.bien)
                    if (objet.bien.bienParent){
                        bienService.updateEtatBienParent(objet.bien.bienParent)
                    }
                    if (idBien){
                        def objetBien = Bien.findById(idBien)
                        objetBien.statut = statutService.getByCode("04")//changer statut en bien libre
                        objetBien.client = ""
                        bienService.update(objetBien)
                    }
                }

                Messagebox.show("Phase enregistrée", "Sauvegarde", Messagebox.OK, Messagebox.INFORMATION)
                activerBoutons(false)
                objet = clazz.newInstance()  
                objet.montantVente = 0
                objet.dateContact = new Date()

                lstPaiements = null
                new AnnotateDataBinder(this.getFellow("lstpaiements")).loadAll()
                
                rafraichirField()
                rafraichirList()
                
            }
            catch(Exception e) {
                logger.error "Error: ${e.message}", e
                if (e.message.contains("com.choranet.omnidior.gesticom.Phase.numPhase.unique.error")){
                    Messagebox.show("Le code [" + objet.numPhase + "] existe déjà", "Erreur", Messagebox.OK, Messagebox.ERROR)  
                }
                else{
                    Messagebox.show("Problème lors de la mise à jour", "Erreur", Messagebox.OK, Messagebox.ERROR)
                }
            }
            finally {

            }
        }
       
    }
    
    def add(){     
        
        Boolean valide = verifierPhase("add")   
        
        if (valide){
             
            actualiserValeurAssociation()    
            if (typePhase != 'OPTION' && objet.client.statut!="CLIENT"){
                objet.client.statut = statutService.getByCode("24")//getByTypeEtIntitule("CLIENT","CLIENT")              
            }
                          
            try{                                              

                def avecAffectation = this.getFellow("fieldAvecaffectation").isChecked()
                if (avecAffectation != null){
                    objet.estAvecAffectation = avecAffectation
                }
                else{
                    objet.estAvecAffectation = false 
                }      
                   
                objet.estExpire = false
                objet.intitule = typePhase
                objet.estAvecAffectation = false  
                objet.estHistorique = false
                String codeBien = "" 
                if(objet.bien){
                    codeBien = objet.bien.numero
                    def etatBien = getEtatBien(typePhase) 
                    objet.bien.statut = statutService.getByCode(etatBien) 
                    objet.bien.client = objet.client.toString()
                    objet.bien = bienService.update(objet.bien)
                    if (objet.bien.bienParent){
                        bienService.updateEtatBienParent(objet.bien.bienParent)
                    }
                    objet.estAvecAffectation = true
                }
                String newCode = objet.projet.code + "_" + codeBien + "_" + objet.client.cin + "_" + objet.intitule
                                
                String autoNum = phaseService.genererNumPhase(newCode)
        
                if (autoNum != "0"){
                    newCode = newCode + "_" + autoNum
                }
                objet.numPhase = newCode //phaseService.genererNumPhase(objet.projet,objet.client)
                modifierStatutPhase() // modification statut phase en expiré si date fin < date j ou l'inverse 
                objet = phaseService.save(objet)
               
                //System.out.println(utilisateur.list())
                projetService.updateEtatProjet(objet.projet)
                ajouterDocuments()

                Messagebox.show("Phase enregistrée avec le code [" + objet.numPhase + "]" , "Sauvegarde", Messagebox.OK, Messagebox.INFORMATION)
                
                
                //////////affectation automatique du client au utlisateurs liees au projet ////////////// 
             
                utilisat=utilisateurService.getUtilisateursParProjet(projetSelected) 
                for(it in utilisat)
                {
                    def utilisateurcourant=(Utilisateur)it
                    if(!(utilisateurcourant.username).equals(utilisateur.username))
                    {
                        it.addToClients(clientSelected)
                        utilisateurService.update(it)
                    }
                }
         
         
                ////////////////////////
                objet = clazz.newInstance()  
                objet.montantVente = 0
                objet.dateContact = new Date()
                lstPaiements = null
                new AnnotateDataBinder(this.getFellow("lstpaiements")).loadAll()
                
                rafraichirField()
                rafraichirList()                
                activerBoutons(false)
                activerBoutonsNew(false)
        
                
            } catch(Exception ex) {
                logger.error "Error: ${ex.message}", ex
                if (ex.message.contains("com.choranet.omnidior.gesticom.Phase.numPhase.unique.error")){
                    Messagebox.show("Le code [" + objet.numPhase + "] existe déjà", "Erreur", Messagebox.OK, Messagebox.ERROR)  
                }
                else
                Messagebox.show("Echec lors de la transaction\n" + ex.message, "Erreur", Messagebox.OK, Messagebox.ERROR)
            } finally {

            }   
        
        }
    }
    
    def modifierStatutPhase(){
        Date dateJ = new Date()
        if (objet.statut.code == "07" && objet.dateFin < dateJ) {
            objet.statut = statutService.getByCode("27")
        }
        
        if (objet.statut.code == "27" && objet.dateFin >= dateJ){
            objet.statut = statutService.getByCode("07")
        }
    }
                
    def cancel() {        
        objet = clazz.newInstance()
        objet.montantVente = 0
        objet.dateContact = new Date()
        lstPaiements = null
        new AnnotateDataBinder(this.getFellow("lstpaiements")).loadAll()
        reinitialiserAssociation(false)
        rafraichirField()
        activerBoutons(false)
        annulerSelection()
    }
    
    def newRecord(){ 
        clearFlist()
        
        if (clients && clients.size()<2){                    
            clients = utilisateur.clients.toList()
            if(clients.size() > 0){            
                clientSelected = clients.get(0)
            }        
            else{
                clientSelected = null        
            }
            new AnnotateDataBinder(this.getFellow("clientGrid")).loadAll()
        }
        
        this.getFellow("coprojets").visible = true
        projetSelected = null
        new AnnotateDataBinder(this.getFellow("coprojets")).loadAll()
        
        objet.dateContact = new Date()
        new AnnotateDataBinder(this.getFellow("fieldDatePrisecontact")).loadAll()
        this.getFellow("westPanel").open = visible 
        this.getFellow("coclients").disabled= false
        this.getFellow("coprojets").disabled= false
                
        activerChampsPhase()
        activerBoutonsNew(true)
        
    }
        
    def activerChampsPhase(){
        
        switch (typePhase){
            
            case "OPTION":
            this.getFellow("gridPhaseCrudRowAvecAffectation").visible = false
            this.getFellow("gridPhaseCrudRowNotaire").visible = false
            this.getFellow("filternotaire").visible = false
            this.getFellow("fieldMontantReliquat").visible = false
            this.getFellow("labelMontantReliquat").visible = false
            this.getFellow("fieldMontantReel").visible = false
            this.getFellow("btnCloturer").visible = false
           
            break
            case "RESERVATION":
            this.getFellow("gridPhaseCrudRowAvecAffectation").visible = true
            this.getFellow("gridPhaseCrudRowNotaire").visible = false
            this.getFellow("filternotaire").visible = false
            this.getFellow("fieldMontantReliquat").visible = true
            this.getFellow("labelMontantReliquat").visible = true
            this.getFellow("fieldMontantReel").visible = false
            this.getFellow("btnCloturer").visible = false          
            break
            case "COMPROMIS":
            this.getFellow("gridPhaseCrudRowAvecAffectation").visible = true
            this.getFellow("gridPhaseCrudRowNotaire").visible = true
            this.getFellow("filternotaire").visible = true
            this.getFellow("fieldMontantReliquat").visible = true
            this.getFellow("labelMontantReliquat").visible = true
            this.getFellow("fieldMontantReel").visible = false
            this.getFellow("btnCloturer").visible = false          
            break
                    
            case "CONTRAT":
            this.getFellow("gridPhaseCrudRowAvecAffectation").visible = true
            this.getFellow("gridPhaseCrudRowNotaire").visible = true
            this.getFellow("filternotaire").visible = true
            this.getFellow("btnetapesuivante").visible = false
            this.getFellow("fieldMontantReliquat").visible = true
            this.getFellow("fieldMontantReel").visible = true
            Boolean cl = session.GESTION_CONTRATS
            if(cl){
                this.getFellow("btnCloturer").visible = true
            }
            //this.getFellow("conotaires").disabled = true
            break

        }
        this.getFellow("btnDelete").visible = false
        
        if(objet.statut){
            if(objet.statut.intitule == 'ANNULEE' || objet.statut.intitule == 'CLOTUREE'){                
                this.getFellow("btnUpdate").disabled = true
                //                this.getFellow("band").disabled = true
                this.getFellow("btnetapesuivante").disabled = true
                this.getFellow("btnDelete").disabled = true                
                this.getFellow("gridPaiementbis").visible = false                                
            }
            else{
                this.getFellow("btnUpdate").disabled = false
                //                this.getFellow("band").disabled = false
                this.getFellow("btnetapesuivante").disabled = false
                this.getFellow("btnDelete").disabled = false                
                this.getFellow("gridPaiementbis").visible = session.GESTION_PAIEMENTS
            }
        }
        
    }    
       
    def calculerMontantReliquat(){
        def montantVente = 0
        if(objet.montantVente){
            montantVente = objet.montantVente
        }
        
        def mt = phaseService.getTotalPaiementParPhase(objet)
        if(montantVente - mt >0 ){
            objet.montantReliquat = montantVente - mt
                    
        }
        else{
            objet.montantReliquat = 0
        }

        
        objet.totalPaiements =  mt
        new AnnotateDataBinder(this.getFellow("fieldMontantReliquat")).loadAll() 
        new AnnotateDataBinder(this.getFellow("fieldMontantReel")).loadAll() 
        new AnnotateDataBinder(this.getFellow("fieldTotalpaiement")).loadAll()
        
        
    }
    
    def genererRecu(pm){
        def numpaiement
        if (pm.phase.bien){
            numpaiement=pm.phase.projet.code + pm.phase.bien.trans_code + "_" + pm.id
        }
        else{
            numpaiement = ""
        }
        
        String nomsociete = pm.phase.projet.groupeClient
        String titrerapport = "Reçu de paiement"
        String montantCaractere = FrenchNumberToWords.convert((int)pm.montant) 
        def reportDef = new JasperReportDef(name:'rapport_recu_paiement.jasper',
            fileFormat:JasperExportFormat.PDF_FORMAT,
            reportData : [pm],
            parameters : ['nomsociete':nomsociete, 'titrerapport':titrerapport, 'montantCaractere' : montantCaractere, 'numpaiement' : numpaiement ]
        )
        def bit = jasperService.generateReport(reportDef).toByteArray()
        def nom_fichier = "recu_paiement" + numpaiement +".pdf"
        //Filedownload.save(bit, "application/pdf", nom_fichier);
        Media amedia = new AMedia(nom_fichier, "pdf", "application/pdf", bit);
        Map map = new HashMap();
        map.put("content", amedia);
        Executions.createComponents("/zul/util/pdfviewer.zul", null, map).doModal();
    }    
           
    def filtrer(Boolean loadliste = true) {    
        utilisateur = Utilisateur.findById(utilisateur.id)        
        session.utilisateur = utilisateur
        
        if (typePhase){
            this.filtre.intitule = typePhase
        }
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
    
    def chargerPhase(chargerClient = null){
        
        listeObjets = phaseService.getPhaseByUtlisateur(utilisateur,typePhase,ofs,maxNb)        
        def lsCount = phaseService.getPhaseByUtlisateurCount(utilisateur,typePhase)
        
        if (lsCount){
            tailleListe = lsCount
        }
        else{
            tailleListe = 0 
        }

    }
        
    def doModalDialogDocument(){
        this.getFellow("modalDialogDocument").visible = true
        this.getFellow("modalDialogDocument").doModal() 
    }
    
    def actualiserDateExpiration(){
        Integer plusToAdd = 0 
        if (objet.projet || projetSelected) {
            if (objet.projet){
                plusToAdd  = getIntervalDate(typePhase,objet.projet)
            }
            else{
                plusToAdd  = getIntervalDate(typePhase,projetSelected)
            }
            objet.dateFin = objet.dateDebut.plus(plusToAdd)        
            new AnnotateDataBinder(this.getFellow("fieldDateFin")).loadAll()
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
    
    def getIntervalDate(typePhase,Projet projet){
        Integer interval        
        
        switch (typePhase){
            case "OPTION":
            interval = projet.delaiOption
            break
            case "RESERVATION":
            interval = projet.delaiReservation
            break
            case "COMPROMIS":
            interval = projet.delaiCompromis
            break
            case "CONTRAT":
            interval = projet.delaiContrat
        }        
        return interval
    }

    def processMedia(org.zkoss.util.media.Media[] medias) {
        if (medias != null) {
            Integer indx = 0 
            //            listDocuments.clear()
            //            listDocuments  = objet.documents.toList()

            if (listDocuments && listDocuments.size()> 0 ){
                for (dc in listDocuments ){
                    if (dc.tempCode > indx){
                        indx = dc.tempCode
                    }
                }
            }
            
            for (media in medias) {
                indx += 1
                Document dcm = new Document()
                dcm.intitule = media.getName();
                dcm.tempCode = indx
                dcm.documentNumerise = media.getByteData();                                 
                listDocuments.add(dcm)
            }
            
        }       
        afficherDocumentsG()
    }
    
    def clearFlist(){
        def binderfile =  this.getFellow("flist")
        
        def tempChild = new ArrayList()
        for (child in binderfile.getChildren()){
            tempChild.add(child.getId())
        }
        
        if (tempChild.size()> 0 ) {
            for (l in tempChild){
                this.getFellow(l).detach();
            }
            
        }
        
        new AnnotateDataBinder(binderfile).loadAll() 
    }
   
    def afficherDocumentsG(){
        clearFlist()
        def binderfile =  this.getFellow("flist")
        if (listDocuments && listDocuments.size()>0){
            //Integer idH = 0 
            for (dc in listDocuments){   
                //idH +=1
                Hlayout hl = new Hlayout();                
                hl.setSpacing("6px");
                hl.setClass("newFile"); 
                hl.setId('h' + dc.tempCode)
                
                A fl = new A(dc.intitule);
                fl.setId('f'+ dc.tempCode) 
                fl.addEventListener("onClick", this)
                hl.appendChild(fl);

                A rm = new A("Supprimer");
                rm.setId('a'+ dc.tempCode)                
                rm.addEventListener("onClick", this)
                hl.appendChild(rm);
                
                binderfile.appendChild(hl);
            }
        }
                
        new AnnotateDataBinder(binderfile).loadAll() 
    }
    
    def loadPhaseDocuments(){
        listDocuments.clear()
        listDocuments  = objet.documents.toList()
        afficherDocumentsG()
    }
    
    def minLength(String value, Integer min){
        Boolean valide = true
        if (!value || value.length() < min ) {
            valide = false
        }
        return valide
    }
        
    def actualiser(){
        chargerPhase()
        new AnnotateDataBinder(this.getFellow("lstObjet")).loadAll() 
        //phaseService.updatePhaseExpiree()
    }       
    
    def cloturerContrat(){
        def st = statutService.getByCode("09") //statut phase cloturée
        objet.statut = st
        try {
            objet = phaseService.update(objet) 
        
            rafraichirField()
            rafraichirList()
            
            Messagebox.show("Le contrat est cloturé", "Sauvegarde", Messagebox.OK, Messagebox.INFORMATION)
        }
        catch(Exception e) {
            logger.error "Error: ${e.message}", e
            if (e.message.contains("com.choranet.omnidior.gesticom.Phase.numPhase.unique.error")){
                Messagebox.show("Le code [" + objet.numPhase + "] existe déjà", "Erreur", Messagebox.OK, Messagebox.ERROR)  
            }
            else{
                Messagebox.show("Problème lors de la mise à jour", "Erreur", Messagebox.OK, Messagebox.ERROR)
            }
        }
        finally {

        }
    }
    
    def telechargerDocument(Document dc){
        String fileTemp = "tempFile";
        String fileName = "temp.pdf" ;
        OutputStream out = new FileOutputStream(fileName);
        out.write(dc.documentNumerise);
        out.close();
            
        File f = new File(fileName)     
        //
        //        File file2 = new File(fileTemp + "/" + dc.intitule);
        //        if(file2.exists()){
        //         file2.delete()
        //        }
        //        f.renameTo(file2)
        Filedownload.save(f, dc.intitule);
        //f.delete();
    }
    
    def ajouterDocuments() {
        if (objet){
            //objet.documents = null
            objet.documents = new ArrayList<Document>()
            if (listDocuments && listDocuments.size()>0){
                for (dc in listDocuments){ 
                    dc = documentService.save(dc)    
                    objet = objet.addToDocuments(dc)           
                }     
            }            

        }
        objet = phaseService.update(objet)
    }
    
    public void onEvent(Event e) throws Exception {
        //ON CLICK
        if (e.getName().equals("onClick")) {
            A bl = (A) e.getTarget();
            String idH =  bl.id
            if(idH.substring(0,1) == 'a') {
                idH =  idH.replaceFirst('a','h')
                this.getFellow(idH).detach()
            
                def ind = idH.replaceFirst('h','')
                Integer indexToDel = -1
            
                Integer cpt = 0  
                if (listDocuments && listDocuments.size()> 0){
                    for(dc in listDocuments){
                        String code = dc.tempCode
                        if (code.trim() == ind.trim() ){

                            indexToDel = cpt
                            //listDocuments.remove(cpt) 
                        }
                        cpt += 1
                    }
                                
                    if(indexToDel >= 0 ){
                        listDocuments.remove(indexToDel) 
                    }
                    
                }
           
                new AnnotateDataBinder(this.getFellow("flist")).loadAll()

            }
            
            else if (idH.substring(0,1) == 'f'){
                if (listDocuments && listDocuments.size()> 0){
                    def ind = idH.replaceFirst('f','')
                    for(dc in listDocuments){
                        
                        String code = dc.tempCode
                        if (code.trim() == ind.trim() ){

                            telechargerDocument(dc)
                        }
                    }
                }
            }
        }
  
    }

    def load() {
        try {
            if (typePhase){
                this.filtre.intitule = typePhase
            }            
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
    
    def verifierClient(Client client){
        
        Boolean valideClient = true
        
        def listErreurs = []
        
        if(!client.nom || client.nom.length() == 0 ){
            valideClient = false
            //Messagebox.show("Veuillez saisir un nom pour le client" , "Erreur", Messagebox.OK, Messagebox.ERROR)
            listErreurs.add("* Saisir un nom pour le client")
        }
        
        if(!client.prenom || client.prenom.length() == 0 ){
            valideClient = false
            //Messagebox.show("Veuillez saisir un prénom pour le client" , "Erreur", Messagebox.OK, Messagebox.ERROR)
            listErreurs.add("* Saisir un prénom pour le client")
        }
        
        if (!minLength(client.gsm,9)){ 
            valideClient = false
            //Messagebox.show("Le numéro de téléphone doit être composé de 9 chiffres [XXXXXXXXX]" , "Erreur", Messagebox.OK, Messagebox.ERROR)
            listErreurs.add("* Saisir un numéro de téléphone composé de 9 chiffres [XXXXXXXXX]")
        
        }
                                      
        if (!client.dateNaissance){
            valideClient = false
            listErreurs.add("* La date de naissance est obligatoire pour le type [Client]")
        }
        else{
            if(new Date() - client.dateNaissance < 0 ){
                valideClient = false
                // Messagebox.show("Merci de verifier la date de naissance" , "Erreur", Messagebox.OK, Messagebox.ERROR)   
                listErreurs.add("* Saisir une date de naissance inférieure à la date d'aujourd'hui")
            }
        }
        
        if(client.dateContact){
            if (new Date() - client.dateContact < 0){
                valideClient = false
                //Messagebox.show("La date contact doit être inférieure ou égale à la date d'aujourd'hui " , "Erreur", Messagebox.OK, Messagebox.ERROR)  
                listErreurs.add("* Saisir une date contact inférieure ou égale à la date d'aujourd'hui")
            }
        }
        else{
            client.dateContact = new Date()
        }
        
        if(!valideClient){
            String msgError = "Veuillez corriger les erreurs suivantes :\n\n"
            for (it in listErreurs){
                msgError = msgError + it + "\n"
            }
            Messagebox.show( msgError, "Erreur", Messagebox.OK, Messagebox.ERROR)   
        }
        
        return valideClient
    }
    
    def verifierClient(String intitulePhase){
        Boolean estValide = true
        
        if (intitulePhase != "OPTION" && (!clientSelected.cin) ){            
            estValide = false
        }
        return estValide
    }
    
    def activerBoutonsForAdd(visible) {
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

    }
    
    def nouvellePhase(){
        objet = clazz.newInstance()
        objet.montantVente = 0
        objet.dateContact = new Date()
        reinitialiserAssociation(false)
        rafraichirField()
        activerBoutonsForAdd(false)
        annulerSelection()
        clearFlist()
        //        new AnnotateDataBinder(this.getFellow("flist")).loadAll()
        
        lstPaiements = null
        new AnnotateDataBinder(this.getFellow("lstpaiements")).loadAll()
        activerBoutonsNew(true)
        this.getFellow("coclients").disabled= false
        this.getFellow("coprojets").disabled= false
        this.getFellow("coprojets").visible = true
        projetSelected = null
        new AnnotateDataBinder(this.getFellow("coprojets")).loadAll()
    }
    
    def activerBoutonsNew(Boolean visible){
        this.getFellow("btnetapesuivante").disabled = visible
        this.getFellow("gridPaiementbis").visible = !visible       
    }
    
    def verifierPaiements(){
        Boolean valide = true 
        def resultat=phaseService.getpaimentservice(objet)
        if(resultat)
        {
            valide=false;
            
        }
        return valide;
    }
    
    private Boolean verifierBien(Bien b){
        Boolean valide = true
        if (b.statut){
            if (b.statut.code != '04'){
                valide = false
            }
        }
        
        return valide
    }
    
    def annulerSelection() {
        this.getFellow("lstObjet").clearSelection()
        objetSelected = null
        this.getFellow("coclients").disabled= false
        this.getFellow("treebienRow").visible = false
    }
    
    def select() {                    
        objet = objetSelected	
        afficherValeurAssociation()
        //article.lock()  //Ne peut etre utilisï¿½ que pour le base de donnï¿½e qui accepte le veruillage des enregisterments
        rafraichirField()
        activerBoutons(true)
        this.getFellow("coclients").disabled= true
    }
    
    def clientBien(){
        def idObjet = null
        if (objet.bien && bienSelected){
            if(objet.bien.id != bienSelected.id){
                idObjet =  objet.bien.id  
            }
        }
        
        return idObjet
    }
    
    def importation(media) {
        String resultat = excelImporterService.importerPhases(media)
        Messagebox.show(resultat, "Notification" ,  Messagebox.OK, Messagebox.INFORMATION)
        rafraichirList()
    }
    
    def nettoyerAffectationClientsUtilisateurs() {
        moulinetteService.nettoyerAffectationClientUtilisateur()
    }
}
