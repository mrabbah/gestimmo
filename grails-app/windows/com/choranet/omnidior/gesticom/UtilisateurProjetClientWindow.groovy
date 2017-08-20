
package com.choranet.omnidior.gesticom
    

import org.zkoss.zul.*
import org.zkoss.zkplus.databind.*
import org.apache.commons.logging.Log 
import org.apache.commons.logging.LogFactory
import cr.co.arquetipos.crypto.Blowfish 


/**
 * Utilisateur Window Object
 **/
class UtilisateurProjetClientWindow extends SuperWindow{
    
    def utilisateurService
    def phaseService
    /**
     * Logger de la class UtilisateurWindow
     **/
    private Log logger = LogFactory.getLog(UtilisateurProjetClientWindow.class)

    def utilisateurs
    def utilisateurSelected
    
    def projets
    def projetsSelected
    def projetsUtilisateur
    def projetsUtilisateurSelected
    
    def clients
    def clientsSelected
    def clientsUtilisateur
    def clientsUtilisateurSelected
    
    def groupes
    def groupeSelected
    
    public UtilisateurProjetClientWindow() {
        super(Utilisateur.class, 14, ["groupe","username", "userRealName", "enabled", "email"])

        filtre.enabled = true

        projets = Projet.list()
        utilisateurs = Utilisateur.list()
        clients = Client.list()
    }   
    
    /**
     * Activer ou dï¿½sactiver les boutons d'ajout, suppression, modfication
     **/
    //    def activerBoutons(visible) {
    //        this.getFellow("btnUpdate").visible = visible
    //        //this.getFellow("btnDelete").visible = visible
    //        this.getFellow("btnCancel").visible = visible
    //        this.getFellow("btnSave").visible = !visible
    //        this.getFellow("btnNew").visible = !visible
    //        this.getFellow("westPanel").open = visible        
    //    }

    def initialiserAssociation() {
		
        groupes = GroupeUtilisateur.list()
        //        groupeSelected = groupes[0]
		
    }
    /**
     * Fonction qui permet de rï¿½-initaliser l'association au niveau de l'interface
     * @param del si c'est une rï¿½initionalisation aprï¿½s une suppression ou non
     **/
    def reinitialiserAssociation(del) {
			
        if(del) {
            groupes = GroupeUtilisateur.list()
        }        
        	
    }
    /**
     * Fonction qui copie la valeur de l'association ï¿½ l'ï¿½lï¿½ment courant
     **/
    def actualiserValeurAssociation() {

    }
    
    def select() {                    
        super.select() 
        new AnnotateDataBinder(this.getFellow("lstprojetsUtilisateur")).loadAll()
        new AnnotateDataBinder(this.getFellow("lstclientsUtilisateur")).loadAll()
    }
    
    def afficherValeurAssociation() {				
        def binderutilisateur = new AnnotateDataBinder(this.getFellow("coutilisateurs"))
        utilisateurSelected = utilisateurs.find{ it.id == objet.id }
        binderutilisateur.loadAll()	
    }
    
    def add() {

    }
    
    def update() {

        actualiserValeurAssociation()
        try {
            objet = getService().update(objet)
            // ce code genere une erreur lors de l'enregistrement et je n'arrive pas à detecter son utilité
//            if (session.utilisateur.id == objet.id){
//                session.utilisateur = objet
//            }
        }
        catch(Exception e) {
            logger.error "Error: ${e.message}", e
            Messagebox.show("Problème lors de la mise à jour", "Erreur", Messagebox.OK, Messagebox.ERROR)
        }
        finally {
            activerBoutons(false)
            objet = clazz.newInstance()
            rafraichirField()
            rafraichirList()
        }
        
    }
    
    def newRecord(){
        super.newRecord()
        activerBoutons(true)
    }
    
    def utilisateurChoisi() {
       
        objet = utilisateurSelected	
        
        rafraichirField()
        activerBoutons(true)
       
        new AnnotateDataBinder(this.getFellow("lstprojetsUtilisateur")).loadAll()
        new AnnotateDataBinder(this.getFellow("lstclientsUtilisateur")).loadAll()
    }
    
    def choisirTout() {
        if (objet){
            Boolean lpExisteDeja
            for(lp in projets) {
                lpExisteDeja = false
                for(nlp in objet.projets) {
                    if(lp.code.equals(nlp.code)) {
                        lpExisteDeja = true
                        break;
                    }
            
                }

                if(!lpExisteDeja) {
                    objet.addToProjets(lp)

                }
 
            
            }
            def lstP = this.getFellow("lstprojets")
            def lstPU = this.getFellow("lstprojetsUtilisateur")
            lstP.clearSelection()
            new AnnotateDataBinder(lstP).loadAll()
            lstPU.clearSelection()
            new AnnotateDataBinder(lstPU).loadAll()
        
            //        if(objet.projets.size() > 0) {
            if(this.getFellow("btnDelete").visible) {
                this.getFellow("btnUpdate").visible = true
            } else {
                //  this.getFellow("btnSave").visible = true
            }
            //        } else {
            //            this.getFellow("btnSave").visible = false
            //            this.getFellow("btnUpdate").visible = false
            //        }
            
            //affectation automatique des client des projets 
            affecterClientParProjet()
        }
    }
    
    def choisirDesElements() {
        if (objet && projetsSelected && projetsSelected.size()>0){
            Boolean lpExisteDeja
            for(lp in projetsSelected) {
                lpExisteDeja = false
                for(nlp in objet.projets) {
                    if(lp.code.equals(nlp.code)) {
                        lpExisteDeja = true
                        break;
                    }
            
                }

                if(!lpExisteDeja) {
                    objet.addToProjets(lp)

                }
 
            
            }
            def lstP = this.getFellow("lstprojets")
            def lstPU = this.getFellow("lstprojetsUtilisateur")
            lstP.clearSelection()
            new AnnotateDataBinder(lstP).loadAll()
            lstPU.clearSelection()
            new AnnotateDataBinder(lstPU).loadAll()
        
            //        if(objet.projets.size() > 0) {
            if(this.getFellow("btnDelete").visible) {
                this.getFellow("btnUpdate").visible = true
            } else {
                //this.getFellow("btnSave").visible = true
            }
            //        } else {
            //            this.getFellow("btnSave").visible = false
            //            this.getFellow("btnUpdate").visible = false
            //        }
            
            //affectation automatique des client des projets 
            affecterClientParProjet()
        }
    }
    
    def enleverDesElements() {
        if (objet && projetsUtilisateurSelected && projetsUtilisateurSelected.size()>0){
            objet.projets.removeAll(projetsUtilisateurSelected)
        
            def lstP = this.getFellow("lstprojets")
            def lstPU = this.getFellow("lstprojetsUtilisateur")
            lstP.clearSelection()
            new AnnotateDataBinder(lstP).loadAll()
            lstPU.clearSelection()
            new AnnotateDataBinder(lstPU).loadAll()       
        
            //        if(objet.projets.size() > 0) {
            if(this.getFellow("btnDelete").visible) {
                this.getFellow("btnUpdate").visible = true
            } else {
                //this.getFellow("btnSave").visible = true
            }
            //        } else {
            //            this.getFellow("btnSave").visible = false
            //            this.getFellow("btnUpdate").visible = false
            //        }
        }
    } 

    def enleverTout() {
        if (objet){
            objet.projets = null
            objet.projets = new ArrayList<Projet>()
            def lstP = this.getFellow("lstprojets")
            def lstPU = this.getFellow("lstprojetsUtilisateur")
            lstP.clearSelection()
            new AnnotateDataBinder(lstP).loadAll()
            lstPU.clearSelection()
            new AnnotateDataBinder(lstPU).loadAll()
        
            //        if(objet.projets.size() > 0) {
            if(this.getFellow("btnDelete").visible) {
                this.getFellow("btnUpdate").visible = true
            } else {
                //  this.getFellow("btnSave").visible = true
            }
            //        } else {
            //            this.getFellow("btnSave").visible = false
            //            this.getFellow("btnUpdate").visible = false
            //        }
        }
    }
    
    def choisirToutClient() {
        if (objet){
            Boolean lpExisteDeja
            for(lp in clients) {
                lpExisteDeja = false
                for(nlp in objet.clients) {
                    if(lp.id.equals(nlp.id)) {
                        lpExisteDeja = true
                        break;
                    }
            
                }

                if(!lpExisteDeja) {
                    objet.addToClients(lp)

                }
 
            
            }
            def lstP = this.getFellow("lstclients")
            def lstPU = this.getFellow("lstclientsUtilisateur")
            lstP.clearSelection()
            new AnnotateDataBinder(lstP).loadAll()
            lstPU.clearSelection()
            new AnnotateDataBinder(lstPU).loadAll()
        
            //        if(objet.clients.size() > 0) {
            if(this.getFellow("btnDelete").visible) {
                this.getFellow("btnUpdate").visible = true
            } else {
                //  this.getFellow("btnSave").visible = true
            }
            //        } else {
            //            this.getFellow("btnSave").visible = false
            //            this.getFellow("btnUpdate").visible = false
            //        }
        }
    
    }
    
    def choisirDesElementsClient() {
        if (objet && clientsSelected && clientsSelected.size()>0){
            Boolean lpExisteDeja
            for(lp in clientsSelected) {
                lpExisteDeja = false
                for(nlp in objet.clients) {
                    if(lp.id.equals(nlp.id)) {
                        lpExisteDeja = true
                        break;
                    }
            
                }

                if(!lpExisteDeja) {
                    objet.addToClients(lp)

                }
 
            
            }
            def lstP = this.getFellow("lstclients")
            def lstPU = this.getFellow("lstclientsUtilisateur")
            lstP.clearSelection()
            new AnnotateDataBinder(lstP).loadAll()
            lstPU.clearSelection()
            new AnnotateDataBinder(lstPU).loadAll()
        
            //        if(objet.clients.size() > 0) {
            if(this.getFellow("btnDelete").visible) {
                this.getFellow("btnUpdate").visible = true
            } else {
                //this.getFellow("btnSave").visible = true
            }
            //        } else {
            //            this.getFellow("btnSave").visible = false
            //            this.getFellow("btnUpdate").visible = false
            //        }
        }

    }
    
    def enleverDesElementsClient() {
        if (objet && clientsUtilisateurSelected && clientsUtilisateurSelected.size()>0){
            objet.clients.removeAll(clientsUtilisateurSelected)
        
            def lstP = this.getFellow("lstclients")
            def lstPU = this.getFellow("lstclientsUtilisateur")
            lstP.clearSelection()
            new AnnotateDataBinder(lstP).loadAll()
            lstPU.clearSelection()
            new AnnotateDataBinder(lstPU).loadAll()       
        
            //        if(objet.clients.size() > 0) {
            if(this.getFellow("btnDelete").visible) {
                this.getFellow("btnUpdate").visible = true
            } else {
                //this.getFellow("btnSave").visible = true
            }
            //        } else {
            //            this.getFellow("btnSave").visible = false
            //            this.getFellow("btnUpdate").visible = false
            //        }
        }

    } 

    def enleverToutClient() {
        if (objet){
            objet.clients = null
            objet.clients = new ArrayList<Client>()
            def lstP = this.getFellow("lstclients")
            def lstPU = this.getFellow("lstclientsUtilisateur")
            lstP.clearSelection()
            new AnnotateDataBinder(lstP).loadAll()
            lstPU.clearSelection()
            new AnnotateDataBinder(lstPU).loadAll()
        
            //        if(objet.clients.size() > 0) {
            if(this.getFellow("btnDelete").visible) {
                this.getFellow("btnUpdate").visible = true
            } else {
                //  this.getFellow("btnSave").visible = true
            }
            //        } else {
            //            this.getFellow("btnSave").visible = false
            //            this.getFellow("btnUpdate").visible = false
            //        }
        }
    }
        
    protected SuperService getService() {
        return utilisateurService
    }
    
    def activerBoutons(visible) {
        try {
            this.getFellow("btnUpdate").visible = visible
        } catch(Exception ex) { /**/ }

        try {
            this.getFellow("btnSave").visible = !visible
        } catch(Exception ex) { /**/ }
        //        try {
        //            this.getFellow("btnNew").visible = !visible
        //        } catch(Exception ex) { /**/ }
        try {
            this.getFellow("westPanel").open = visible        
        } catch(Exception ex) { /**/ }
    }
    
    def affecterClientParProjet(){
        def listeProjets = new ArrayList()
        
        if(objet.projets && objet.projets.size()>0){
        
            for (it in objet.projets){
                listeProjets.add(it.id)
            } 
        
     
        
            def listeClients = phaseService.getClientsByListeProjetAll(listeProjets)
            
            if (listeClients && listeClients.size()>0){
            
      
                Boolean lpExisteDeja
                for(lp in listeClients) {
                    lpExisteDeja = false
                    for(nlp in objet.clients) {
                        if(lp.id.equals(nlp.id)) {
                            lpExisteDeja = true
                            break;
                        }
            
                    }

                    if(!lpExisteDeja) {
                        objet.addToClients(lp)

                    }
 
            
                }
                def lstP = this.getFellow("lstclients")
                def lstPU = this.getFellow("lstclientsUtilisateur")
                lstP.clearSelection()
                new AnnotateDataBinder(lstP).loadAll()
                lstPU.clearSelection()
                new AnnotateDataBinder(lstPU).loadAll()                
        
                if(this.getFellow("btnDelete").visible) {
                    this.getFellow("btnUpdate").visible = true
                } else {
                    
                }

            }
        
        }
    }
    
}
