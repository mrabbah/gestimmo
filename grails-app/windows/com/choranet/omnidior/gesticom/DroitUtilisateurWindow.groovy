package com.choranet.omnidior.gesticom

import org.zkoss.zul.*
import org.zkoss.zkplus.databind.*
import org.apache.commons.logging.Log 
import org.apache.commons.logging.LogFactory
import com.choranet.omnidior.gesticom.SuperService
import com.choranet.omnidior.gesticom.SuperWindow


/**
 * DroitUtilisateur Window Object
 **/
class DroitUtilisateurWindow extends SuperWindow{
    
    def droitUtilisateurService
    
    def groupeUtilisateurService
    /**
     * Logger de la class DroitUtilisateurWindow
     **/
    private Log logger = LogFactory.getLog(DroitUtilisateurWindow.class)
	
    def groupeUtilisateur
	
    /**
     * Constructeur
     **/
    public DroitUtilisateurWindow () {
        super(DroitUtilisateur.class)
        groupeUtilisateur = new GroupeUtilisateur()
    }   
    /**
     *  Cette fonction est appelï¿½e lorsque un ï¿½lï¿½ment de la liste est selectionnï¿½
     **/
    def select() {       
        groupeUtilisateur = groupeUtilisateurService.getGroupeByAuthority(objetSelected.configAttribute)
        super.select()
        this.getFellow("fieldRole").disabled = true
        this.getFellow("staticValue").visible = false
    }
    /**
     * Fonction qui se charge de sauveguarder un nouveau ï¿½lï¿½ment de droitUtilisateur
     **/
    def add() {
        try {
            groupeUtilisateur.authority = "ROLE_" + groupeUtilisateur.authority
            def tmpgroup = groupeUtilisateurService.getGroupeByAuthority(groupeUtilisateur.authority)
            if(tmpgroup == null) {
                groupeUtilisateurService.save(groupeUtilisateur)
            }
            objet.configAttribute = groupeUtilisateur.authority
            droitUtilisateurService.save(objet)
        } catch(Exception ex) {
            logger.error "Error: ${ex.message}", ex
            Messagebox.show("Echec lors de la transaction", "Erreur", Messagebox.OK, Messagebox.ERROR)
        }finally {
            objet = new DroitUtilisateur()
            groupeUtilisateur = new GroupeUtilisateur()
            rafraichirField()
            rafraichirList()
            activerBoutons(false)
            this.getFellow("fieldRole").disabled = false
            this.getFellow("staticValue").visible = true
        }
    }
    /**
     * Pour annuler la modification ou la supression et pour basculer en mode ajout d'un nouveau ï¿½lï¿½ment
     **/
    def cancel() {        
        groupeUtilisateur = new GroupeUtilisateur()
        super.cancel()
        this.getFellow("fieldRole").disabled = false
        this.getFellow("staticValue").visible = true
    }
    /**
     * Fonction qui se charge de mettre ï¿½ jour un ï¿½lï¿½ment selectionnï¿½ de droitUtilisateur
     **/
    def update() {
        try {
            droitUtilisateurService.update(objet)
            groupeUtilisateurService.update(groupeUtilisateur)
        }
        catch(Exception e) {
            logger.error "Error: ${e.message}", e
            Messagebox.show("Probleme lors de la mise Ã  jour", "Erreur", Messagebox.OK, Messagebox.ERROR)
        }
        finally {
            activerBoutons(false)
            objet = new DroitUtilisateur()                
            rafraichirField()
            rafraichirList()
            this.getFellow("fieldRole").disabled = false
            this.getFellow("staticValue").visible = true
        }
    }
    /**
     * Fonction qui se charge de supprimer un ï¿½lï¿½ment selectinnï¿½ de droitUtilisateur
     **/
    def delete() {
        droitUtilisateurService.delete(objet)
        try {
            groupeUtilisateurService.delete(groupeUtilisateur)
        } catch(Exception ex) {
            logger.info("Groupe utilisateur : " + groupeUtilisateur.authority + " est utilise par un autre Droit utilisateur")
        }
        activerBoutons(false)
        objet = new DroitUtilisateur()
        groupeUtilisateur = new GroupeUtilisateur()
        rafraichirField()
        rafraichirList()
        this.getFellow("fieldRole").disabled = false
        this.getFellow("staticValue").visible = true
    }
    /**
     * Permet d'afficher l'anglet d'ajout d'un nouveau droitUtilisateur
     **/
    def newRecord(){
        this.getFellow("westPanel").open = visible
        this.getFellow("fieldRole").disabled = false
        this.getFellow("staticValue").visible = true
    }
    /**
     * Activer ou dï¿½sactiver les boutons d'ajout, suppression, modfication
     **/
//    def activerBoutons(visible) {
//        this.getFellow("btnUpdate").visible = visible
//        this.getFellow("btnDelete").visible = visible
//        this.getFellow("btnCancel").visible = visible
//        this.getFellow("btnSave").visible = !visible
//        this.getFellow("btnNew").visible = !visible
//        this.getFellow("westPanel").open = visible        
//    }
    
    protected SuperService getService() {
        return droitUtilisateurService
    }
}

