
package com.choranet.omnidior.gesticom


import org.zkoss.zul.*
import org.zkoss.zkplus.databind.*
import org.apache.commons.logging.Log 
import org.apache.commons.logging.LogFactory

import org.zkoss.image.Image
import org.zkoss.zk.ui.Executions
import com.choranet.commun.*

/**
 * ClientInfo Window Object
 **/
class ChoraClientInfoWindow extends SuperWindow {
        
    /**
     * Service pour la gestion de l'objet Voiture
     **/
    def choraClientInfoService
    
    def choraBarrageService
    /**
     * Logger de la class VoitureWindow
     **/
    private Log logger = LogFactory.getLog(ChoraClientInfoWindow.class)
	

    /**
     * Constructeur
     **/
    public ChoraClientInfoWindow () {
        //objet = ChoraClientInfo.get(1)
        super(ChoraClientInfo.class)
    }  
    
    protected SuperService getService() {
        return this.choraClientInfoService
    }
   
    def updateLogo(Object media) {
        if (media != null) {                   
            
            if (media instanceof Image) {
                this.getFellow("appercuLogo").setContent(media);
                objet.setTrans_logo((Image) media);
            } else {
                Messagebox.show("Le fichier choisi n'est pas une image: " + media, "Erreur",
                    Messagebox.OK, Messagebox.ERROR);
                //break; //not to show too many errors
            }

        } else {
            Messagebox.show("Impossible d'envoyer le fichier choisi: " + media, "Erreur",
                Messagebox.OK, Messagebox.ERROR);
        }
    }
    
    /**
     * Fonction qui se charge de mettre ? jour un ?l?ment selectionn? de article
     **/
//    def update() {
//        try {
//            choraClientInfoService.update(objet)
//        }
//        catch(Exception e) {
//            logger.error "Error: ${e.message}", e
//            Messagebox.show("Probleme lors de la mise � jour", "Erreur", Messagebox.OK, Messagebox.ERROR)
//        }
//        finally {
//            rafraichirField()
//        }  
//
//    }
    
    /**
     * Fonction qui se charge de mettre ? jour un ?l?ment selectionn? de article
     **/
    def enregistrer() {
        try {
            choraClientInfoService.update(objet)
            choraBarrageService.premierAccessEffectue(objet)
            Executions.sendRedirect("/")
        }
        catch(Exception e) {
            logger.error "Error: ${e.message}", e
            Messagebox.show("Probleme lors de l'enregistrement", "Erreur", Messagebox.OK, Messagebox.ERROR)
        }
        finally {
            rafraichirField()
        }  

    }
    
    /**
     * R?initialiser les champs du formulaire
     **/
    def rafraichirField() {
        this.getFellows().each { co ->
            if(co.getId() != null && co.getId().startsWith("field")) {
                def binder = new AnnotateDataBinder(co)
                binder.loadAll()
            }
        }               
    }
    
   
}
