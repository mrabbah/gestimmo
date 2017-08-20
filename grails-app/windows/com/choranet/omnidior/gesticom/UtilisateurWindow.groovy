package com.choranet.omnidior.gesticom

import org.zkoss.zul.*
import org.zkoss.zkplus.databind.*
import org.apache.commons.logging.Log 
import org.apache.commons.logging.LogFactory
import cr.co.arquetipos.crypto.Blowfish 


/**
 * Utilisateur Window Object
 **/
class UtilisateurWindow extends SuperWindow {
    
    def utilisateurService
    /**
     * Logger de la class UtilisateurWindow
     **/
    private Log logger = LogFactory.getLog(UtilisateurWindow.class)
	
    /**
     * liste de groupeUtilisateur
     **/
    def groupes
    /**
     * groupeUtilisateur  selectionnï¿½
     **/
    def groupeSelected
	
    /**
     * Constructeur
     **/
    public UtilisateurWindow () {
        super(Utilisateur.class, 14, ["groupe","username", "userRealName", "enabled", "email"])
//        filtre.societe = session.societe
        filtre.enabled = true
    }   
    
    def add() {
        //objet.societe = session.societe
        objet.passwd = Blowfish.encryptBase64(objet.pass, servletContext.password)
        super.add()
    }
    def update() {
        if(objet.pass != '[secret]') {
            objet.passwd = Blowfish.encryptBase64(objet.pass, servletContext.password)
        }
        super.update()
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
    /**
     * Fonction qui gï¿½re l'initialisation des listes d'associations au niveau du constructeur
     **/
    def initialiserAssociation() {
		
        groupes = GroupeUtilisateur.list()
        groupeSelected = groupes[0]
		
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
        objet.groupe = groupeSelected
    }
    /**
     * Fonction qui fait la liaison entre l'association l'ï¿½lï¿½ment selectionnï¿½ et la liste dans le crud
     **/
    def afficherValeurAssociation() {
				
        def bindergroupes = new AnnotateDataBinder(this.getFellow("lstgroupes"))
        groupeSelected = groupes.find{ it.id == Utilisateur.findById(objet.id).groupe.id }
        objetSelected.groupe
        bindergroupes.loadAll()
		
    }
    
    protected SuperService getService() {
        return utilisateurService
    }
    
}

