
package com.choranet.omnidior.gesticom
    

import org.zkoss.zul.*
import org.zkoss.zkplus.databind.*
import org.apache.commons.logging.Log 
import org.apache.commons.logging.LogFactory
import org.codehaus.groovy.grails.plugins.jasper.JasperExportFormat
import org.codehaus.groovy.grails.plugins.jasper.JasperReportDef
import org.springframework.web.context.request.RequestContextHolder

import org.zkoss.util.media.AMedia;
import org.zkoss.util.media.Media;
import java.util.HashMap;
import java.util.Map;
import org.zkoss.zk.ui.Executions


/**
 * Client Window Object
 **/
class ClientWindow extends SuperWindow {
    /**
     * Service pour la gestion de l'objet Client
     **/
    def clientService
    def statutService
    def utilisateurService
    def phaseService 
    def excelImporterService
    /**
     * Service jasper pour la generation des rapports
     **/
    def jasperService
    /**
     * Logger de la class ClientWindow
     **/
    private Log logger = LogFactory.getLog(ClientWindow.class)
    
    /**
     * liste de nationalite
     **/	
    def nationalites	
    /**
     * nationalite  selectionn�
     **/
    def nationaliteSelected
                
    /**
     * liste de ville
     **/	
    def villes	
    /**
     * ville  selectionn�
     **/
    def villeSelected
    
    def statuts
    def statutSelected
    
    def sexes = ['F','M']
    def sexeSelected 
    
    def utilisateur = session.utilisateur
                
    /**
     * Constructeur
     **/
    
    public ClientWindow (clientService,statutService) {               
        super(Client.class,false)        
        this.clientService = clientService
        this.statutService = statutService    
        specialInit()
    }  

    protected SuperService getService() {
        return this.clientService
    }
    
    /**
     * Generation du rapport pdf
     **/
    def genererRapportPdf() {
        String nomsociete = ChoraClientInfo.findByNomsociete("OMNIDIOR").nomsociete
        String titrerapport = "Rapport des Clients"
        //def listeObjetsExporter = getObjetsToExport()
        def listeObjetsExporter = Client.list()
        //def reportDef = new JasperReportDef(name:'rapport_des_Clients.jrxml',
        def reportDef = new JasperReportDef(name:'reportclient.jrxml',
            fileFormat:JasperExportFormat.PDF_FORMAT,
            reportData : listeObjetsExporter,
            parameters : [  'nomsociete':nomsociete, 'titrerapport':titrerapport ]
        )
        def bit = jasperService.generateReport(reportDef).toByteArray()
        def nom_fichier = "rapport_des_Clients.pdf"
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
        String titrerapport = "Rapport des Clients"
        def listeObjetsExporter = getObjetsToExport()
        def reportDef = new JasperReportDef(name:'rapport_des_Clients.jrxml',
            fileFormat:JasperExportFormat.XLS_FORMAT,
            reportData : listeObjetsExporter,
            parameters : [  'nomsociete':nomsociete, 'titrerapport':titrerapport ]
        )
        def bit = jasperService.generateReport(reportDef).toByteArray()
        def nom_fichier = "rapport_des_Clients.xls"
        Filedownload.save(bit, "application/file", nom_fichier);
        
    }
    
    def specialInit(){
        filtrer(false)
        statuts = statutService.getByType("CLIENT")
        if(statuts.size() > 0)
        statutSelected = statuts.get(0)
        else
        statutSelected = null
        objet.dateContact = new Date()        
        objet.sexe = sexes[0]                
    }    
    
    /**
     * Fonction qui g�re l'initialisation des listes d'associations au niveau du constructeur
     **/
    def initialiserAssociation() {
        
        nationalites = Nationalite.list()		
        if(nationalites.size() > 0)
        nationaliteSelected = nationalites.get(0)
        else
        nationaliteSelected = null
                    
        villes = Ville.list()		
        if(villes.size() > 0)
        villeSelected = villes.get(0)
        else
        villeSelected = null           
                     
    }
    /**
     * Fonction qui permet de r�-initaliser l'association au niveau de l'interface
     * @param del si c'est une r�initionalisation apr�s une suppression ou non
     **/
    def reinitialiserAssociation(del) {
        	
        if(del) {
            nationalites = Nationalite.list()
        }	
        if(nationalites.size() > 0)
        nationaliteSelected = nationalites.get(0)
        else
        nationaliteSelected = null        

        //        sexeSelected = sexes[0]
                            	
        if(del) {
            villes = Ville.list()
        }	
        if(villes.size() > 0)
        villeSelected = villes.get(0)
        else
        villeSelected = null
        
        if(del) {
            statuts = statutService.getByType("CLIENT")
        }	
        if(statuts.size() > 0)
        statutSelected = statuts.get(0)
        else
        statutSelected = null
        
        objet.sexe = sexes[0]
                    
    }
    /**
     * Fonction qui copie la valeur de l'association � l'�l�ment courant
     **/
    def actualiserValeurAssociation() {
        		
        objet.nationalite = nationaliteSelected
        if(nationalites.size() > 0) {
            def bindernationalite = new AnnotateDataBinder(this.getFellow("conationalites"))
            nationaliteSelected = nationalites.get(0)
            bindernationalite.loadAll()
        }
        else
        nationaliteSelected = null
                    		
        objet.ville = villeSelected
        if(villes.size() > 0) {
            def binderville = new AnnotateDataBinder(this.getFellow("covilles"))
            villeSelected = villes.get(0)
            binderville.loadAll()
        }
        else
        villeSelected = null
        
                
        objet.statut = statutSelected
        //        if(statuts.size() > 0) {
        //            def binderstatut = new AnnotateDataBinder(this.getFellow("costatuts"))
        //            statutSelected = statuts.get(0)
        //            binderstatut.loadAll()
        //        }
        //        else
        //        statutSelected = null
        

        //        objet.sexe = sexeSelected        
        //        def bindersexe = new AnnotateDataBinder(this.getFellow("cosexes"))
        //        sexeSelected = sexes[0]
        //        bindersexe.loadAll()                   
    }
    /**
     * Fonction qui fait la liaison entre l'association l'�l�ment selectionn� et la liste dans le crud
     **/
    
    def afficherValeurAssociation() {
        

        def bindersexe = new AnnotateDataBinder(this.getFellow("cosexes"))
        //        if (objet.sexe == 'M'){
        //            sexeSelected = sexes[1]
        //        }
        //        else{
        //            sexeSelected = sexes[0]
        //        }
        bindersexe.loadAll()
        
        def bindernationalite = new AnnotateDataBinder(this.getFellow("conationalites"))
        nationaliteSelected = nationalites.find{ it.id == objet.nationalite.id }
        bindernationalite.loadAll()
                    		
        def binderville = new AnnotateDataBinder(this.getFellow("covilles"))
        villeSelected = villes.find{ it.id == objet.ville.id }
        binderville.loadAll()
        
        def binderstatut =  new AnnotateDataBinder(this.getFellow("costatuts"))
        statutSelected = statuts.find{ it.id == objet.statut.id}
        binderstatut.loadAll()
                    
    }
    
    def add(){
        Boolean valide = verifierClient(objet)      
        String idClient
        if (valide){
            actualiserValeurAssociation()
            try {

                objet.dateModification = new Date()
                objet =getService().update(objet)    
                idClient = objet.id
                //                        utilisateur = Utilisateur.list().find{ it.id == session.utilisateur.id }
                utilisateur.addToClients(objet)
                utilisateur = utilisateurService.update(utilisateur)
                def gpComm = GroupeUtilisateur.findByIntitule("COMMERCIAL_D")
                def gpRoot = GroupeUtilisateur.findByIntitule("ROOT")
                def utilisateursAdmin = Utilisateur.findAllByGroupeOrGroupe(gpComm, gpRoot)
                //println utilisateursAdmin
                for(usr in utilisateursAdmin) {
                    if (utilisateur.id != usr.id)
                    {            
                        usr.addToClients(objet)
                        utilisateurService.update(usr)                    
                    }
                }
                session.utilisateur  = utilisateur 
                session.typePhase = "OPTION"
                String url = "/zul/main.zul?ic=" + idClient
                
                Executions.sendRedirect(url); 
                //desactivé aprés redirection                 
                //                objet = clazz.newInstance()
                //                rafraichirField()
                //                rafraichirList()
                //                activerBoutons(false)                       
            
            } catch(Exception ex) {
                logger.error "Error: ${ex.message}", ex
                if (ex.message.contains("com.choranet.omnidior.gesticom.Client.cin.unique.error")){
                    Messagebox.show("Le CIN [" + objet.cin + "] existe déjà", "Erreur", Messagebox.OK, Messagebox.ERROR)  
                }
                else{
                    Messagebox.show("Echec lors de la transaction\n" + ex.message, "Erreur", Messagebox.OK, Messagebox.ERROR)
                }
            } finally {

            }    
        }
    }
    
    def update(){
                     
        Boolean valide = verifierClient(objet) 

        if (valide){                
            actualiserValeurAssociation()
            try {
                objet.dateModification = new Date()   
                getService().update(objet)
                activerBoutons(false)
                objet = clazz.newInstance()
                rafraichirField()
                rafraichirList()
            }
            catch(Exception e) {
                logger.error "Error: ${e.message}", e
                if (e.message.contains("com.choranet.omnidior.gesticom.Client.cin.unique.error")){
                    Messagebox.show("Le CIN [" + objet.cin + "] existe déjà", "Erreur", Messagebox.OK, Messagebox.ERROR)  
                }
                else
                Messagebox.show("Problème lors de la mise à jour", "Erreur", Messagebox.OK, Messagebox.ERROR)
            }
            finally {

            }
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
    
    def chargerClient(){
        session.utilisateur=Utilisateur.findById(utilisateur.id)
        utilisateur=session.utilisateur
        listeObjets = clientService.getClientByUtilisateur(utilisateur,ofs,maxNb)
        def ls = clientService.getClientByUtilisateur(utilisateur)
        if (ls){
            tailleListe = ls.size()
        }else{
            tailleListe = 0
        }
    }
        
    def minLength(String value, Integer min){
        Boolean valide = true
        if (!value || value.length() != min ) {
            valide = false
        }
        return valide
    }
    
    def actualiser(){
        chargerClient() 
        new AnnotateDataBinder(this.getFellow("lstObjet")).loadAll() 
    }
    
    def delete(){

        if (phaseService.existePhaseClient(objet) == 0 ){

            def clts = []
            for (it in utilisateur.clients){
                if(!it.id.equals(objet.id)) {
                    clts.add(it)
                } 
            }
            utilisateur.clients = clts
            utilisateur = utilisateurService.update(utilisateur)
            clientService.deleteClientAssociation(objet.id)
            super.delete()
        }
        else {
            Messagebox.show("Il existe des phases pour ce client", "Erreur", Messagebox.OK, Messagebox.ERROR) 
        }
    }
    
    def verifierClient_old(Client objet){
        Boolean valide = true 
        
        if (statutSelected.intitule == "CLIENT" && (!objet.cin)  ){
            valide = false
            Messagebox.show("Le CIN est obligatoire pour le type [Client]", "Erreur", Messagebox.OK, Messagebox.ERROR)
        }
        
        if (statutSelected.intitule == "CLIENT" && (!objet.dateNaissance)  ){
            valide = false
            Messagebox.show("La date naissance obligatoire pour le type [Client]", "Erreur", Messagebox.OK, Messagebox.ERROR)
        }
                        
        if (!minLength(objet.gsm,9)){ 
            valide = false
            Messagebox.show("Le numéro de téléphone doit être composé de 9 chiffres [XXXXXXXXX]" , "Erreur", Messagebox.OK, Messagebox.ERROR)
        }
                              
        //        if(objet.fixe){
        //            if(objet.fixe.length() >0 ){
        //                if(!minLength(objet.fixe, 9)){
        //                    valide = false
        //                    Messagebox.show("Le numéro de fixe doit être composé de 9 chiffres [5XXXXXXXX]" , "Erreur", Messagebox.OK, Messagebox.ERROR) 
        //                }
        //            }
        //        }
        
        if(objet.dateContact){
            if (new Date() - objet.dateContact < 0){
                valide = false
                Messagebox.show("La date contact doit être inférieure ou égale à la date d'aujourd'hui " , "Erreur", Messagebox.OK, Messagebox.ERROR)  
            }
        }
        else{
            objet.dateContact = new Date()
        }
        
        if(new Date() - objet.dateNaissance < 0 ){
            valide = false
            Messagebox.show("Merci de verifier la date de naissance" , "Erreur", Messagebox.OK, Messagebox.ERROR)   
        }
        return valide
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
    
    def verifierClient(Client client){
        
        Boolean valideClient = true
        
        def listErreurs = []
        
                
        if (statutSelected.intitule == "CLIENT" && (!objet.cin)  ){
            valideClient = false
            listErreurs.add("* Le CIN est obligatoire pour le type [Client]")
        }
        
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
                                      
        if (statutSelected.intitule == "CLIENT" && (!client.dateNaissance)  ){
            valideClient = false
            listErreurs.add("* La date de naissance est obligatoire pour le type [Client]")
        }
        
        if (client.dateNaissance){
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
                listErreurs.add("* La date de contact doit être inférieure ou égale à la date d'aujourd'hui")
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

    def importation(media) {
        String resultat = excelImporterService.importerClients(media,utilisateur)
        Messagebox.show(resultat, "Notification" ,  Messagebox.OK, Messagebox.INFORMATION)
        rafraichirList()
    }
    
    def getClientsNonAffectes() {
        listeObjets = clientService.getCilentsNonAffectesPhase()
        tailleListe =    listeObjets.size()     
        def binder = new AnnotateDataBinder(this.getFellow("lstObjet"))
        binder.loadAll()
        
        def binder2 = new AnnotateDataBinder(this.getFellow("paging"))
        binder2.loadAll() 
    }
}
