package com.choranet.omnidior.gesticom

import org.zkoss.zul.*
import org.zkoss.zkplus.databind.*
import org.apache.commons.logging.Log 
import org.apache.commons.logging.LogFactory
import com.choranet.zk.DroitRenderer;
import com.choranet.zk.TreeNodeConverter;


/**
 * GroupeUtilisateur Window Object
 **/
class GroupeUtilisateurWindow extends SuperWindow {
    
    def droitUtilisateurService
    def groupeUtilisateurService
    def utilisateurService
    def droits
    def droitRenderer
    
    /**
     * Logger de la class GroupeUtilisateurWindow
     **/
    private Log logger = LogFactory.getLog(GroupeUtilisateurWindow.class)
	
    /**
     * Constructeur
     **/
    public GroupeUtilisateurWindow (droitUtilisateurService) {
        super(GroupeUtilisateur.class)
        this.droitUtilisateurService = droitUtilisateurService
        specialeInitialisation()
    }   
        
    def delete() {
        def users = utilisateurService.getUtilisateursDuGroupe(objet)
        if(users && users.size() > 0) {
            Messagebox.show("Impossible de supprimer ce groupe : Des utilisateurs appatiennent à ce groupe", "Erreur", Messagebox.OK, Messagebox.ERROR)
        } else {
            super.delete()            
        }
    }
    
    def specialeInitialisation() {
        droitRenderer = new DroitRenderer()
        def droitsParents = droitUtilisateurService.getDroitsPrents()
        droits = new DefaultTreeModel(new DefaultTreeNode(null, construireArbre(droitsParents)))
    }
    def construireArbre(listDroits) {
        def result = []
        for(element in listDroits) {
            def listFils = droitUtilisateurService.getDroitsByParent(element)
            if(listFils == null || listFils.size() == 0) {
                result.add(new DefaultTreeNode(element))
            } else {
                result.add(new DefaultTreeNode(element, construireArbre(listFils)))
            }
        }
        return result
    }
    def afficherValeurAssociation() {
        Tree tree = (Tree) this.getFellow("treedroit")
        tree.clearSelection()
        for(Treeitem item in tree.getItems()) { 
            if(item.getValue() && item.getValue().getData()) {
                for(droit in objet.droits) {
                    if(item.getValue().getData().id == droit.id) {
                        tree.addItemToSelection(item)
                    }
                }
            }
        }
    }
    
    def activerBoutons(visible) {
        if(!visible) {
            Tree tree = (Tree) this.getFellow("treedroit")
            tree.clearSelection() 
        }
        super.activerBoutons(visible)
    }
    def actualiserValeurAssociation() {
        Tree tree = (Tree) this.getFellow("treedroit")
        if(objet.droits) {
            objet.droits.clear()
        } else {
            objet.droits = []
        }
        for(Treeitem item in tree.getItems()) {
            if(item.getValue() && item.getValue().getData()) {
                if(item.isSelected()) {
                    objet.droits.add(item.getValue().getData())
                } else {
                    objet.droits.remove(item.getValue().getData())
                }
            }
        }
       
    }
    
    protected SuperService getService() {
        return groupeUtilisateurService
    }
}

