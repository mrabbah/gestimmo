
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
 * ModeleType Window Object
 **/
class ModeleTypeWindow extends SuperWindow {
    /**
     * Service pour la gestion de l'objet Canal
     **/
    def modeleTypeService
    
    /**
     * Service jasper pour la generation des rapports
     **/
    def jasperService
    
    def types
    def typeSelected
    def modeles
    def modeleSelected
    def nouveauModele
    
    def modeleService
    /**
     * Logger de la class CanalWindow
     **/
    private Log logger = LogFactory.getLog(ModeleTypeWindow.class)
    
    /**
     * Constructeur
     **/
    public ModeleTypeWindow () {
        super(ModeleType.class)
    }  

    protected SuperService getService() {
        return this.modeleTypeService
    }

    
    /**
     * Fonction qui g�re l'initialisation des listes d'associations au niveau du constructeur
     **/
    def initialiserAssociation() {
        nouveauModele = new Modele();
         
        types = Type.list()		
        if(types.size() > 0)
        typeSelected = types.get(0)
        else
        typeSelected = null
        
        modeles = Modele.list()		
        if(modeles.size() > 0)
        modeleSelected = modeles.get(0)
        else
        modeleSelected = null
    }
    /**
     * Fonction qui permet de r�-initaliser l'association au niveau de l'interface
     * @param del si c'est une r�initionalisation apr�s une suppression ou non
     **/
    def reinitialiserAssociation(del) {
        if(del) {
            types = Type.list()
        }	
        if(types.size() > 0)
        typeSelected = types.get(0)
        else
        typeSelected = null
        
        if(del) {
            modeles = Modele.list()
        }	
        if(modeles.size() > 0)
        modeleSelected = modeles.get(0)
        else
        modeleSelected = null
        
        bandvalue()
    }
    /**
     * Fonction qui copie la valeur de l'association � l'�l�ment courant
     **/
    def actualiserValeurAssociation() {
        objet.type = typeSelected
        if(types.size() > 0) {
            def bindertype = new AnnotateDataBinder(this.getFellow("cotypes"))
            typeSelected = types.get(0)
            bindertype.loadAll()
        }
        else
        typeSelected = null
        
        objet.modele = modeleSelected
        if(modeles.size() > 0) {
            def bindermodele = new AnnotateDataBinder(this.getFellow("comodeles"))
            modeleSelected = modeles.get(0)
            bindermodele.loadAll()
        }
        else
        modeleSelected = null
        
        bandvalue()
    }
    /**
     * Fonction qui fait la liaison entre l'association l'�l�ment selectionn� et la liste dans le crud
     **/
    def afficherValeurAssociation() {
        def bindertype = new AnnotateDataBinder(this.getFellow("cotypes"))
        typeSelected = types.find{ it.id == ModeleType.findById(objet.id).type.id }
        bindertype.loadAll()
        
        def bindermodele = new AnnotateDataBinder(this.getFellow("comodeles"))
        modeleSelected = modeles.find{ it.id == ModeleType.findById(objet.id).modele.id }
        bindermodele.loadAll()
        
        bandvalue()
    }
    
    def ajouterModele(){       
        
        modeleSelected = modeleService.save(nouveauModele)   

        
        modeles = Modele.list()                  
        objet.modele = modeleSelected
        
        new AnnotateDataBinder(this.getFellow("comodeles")).loadAll()
        new AnnotateDataBinder(this.getFellow("filtermodele")).loadAll()
        new AnnotateDataBinder(this.getFellow("fieldcomodelesbis")).loadAll()
                        
        def band = this.getFellow("band")
        band.value = modeleSelected.code
        band.close()
        //band.disabled = true;
        new AnnotateDataBinder(band).loadAll()
        this.getFellow("btnSave").visible = true
        nouveauModele = Modele.newInstance()
    }
    
    def bandvalue(){
        def band = this.getFellow("band")
        if (modeleSelected){
            band.value= modeleSelected.code   
        }   
    }
    
    def add(){
        objet.type = typeSelected
        objet.modele = modeleSelected
        def messageVerificationError = modeleTypeService.verifierModeleType(objet)
        if(messageVerificationError != "null"){
            Messagebox.show(messageVerificationError, "Erreur", Messagebox.OK, Messagebox.ERROR)
        }
        else{
            super.add()
        }
    }
    
    def update(){
        objet.type = typeSelected
        objet.modele = modeleSelected
        def messageVerificationError = modeleTypeService.verifierModeleType(objet)
        if(messageVerificationError != "null"){
            Messagebox.show(messageVerificationError, "Erreur", Messagebox.OK, Messagebox.ERROR)
        }
        else{
            super.update()
        }
    }
    
}

