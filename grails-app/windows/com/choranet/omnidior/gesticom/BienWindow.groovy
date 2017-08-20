
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
 * Bien Window Object
 **/
class BienWindow extends SuperWindow {
    /**
     * Service pour la gestion de l'objet Bien
     **/
    def bienService
    def statutService
    def projetService
    def typeService
    /**
     * Service jasper pour la generation des rapports
     **/
    def jasperService
    def excelImporterService
    /**
     * Logger de la class BienWindow
     **/
    private Log logger = LogFactory.getLog(BienWindow.class)
                
    /**
     * liste de type
     **/	
    def types	
    /**
     * type  selectionné
     **/
    def typeSelected
                
    /**
     * liste de bienParent
     **/	
    def bienParents	
    /**
     * bienParent  selectionné
     **/
    def bienParentSelected
                
    /**
     * liste de contenance
     **/	
    //    def contenances	
    //    /**
    //     * contenance  selectionné
    //     **/
    //    def contenanceSelected
                
    /**
     * liste  projets
     **/
    def projets 
    /**
     * projets selectionné
     **/
    def projetSelected
    def projetsFilter
    
    def statuts 
    def statutSelected
    def utilisateur = session.utilisateur
    
    def phaseService
    def moulinetteService
    /**
     * Constructeur
     **/
    public BienWindow (statutService,typeService, projetService,bienService) {
        super(Bien.class,false)
        this.statutService = statutService
        this.typeService = typeService
        this.projetService = projetService
        this.bienService = bienService
        specialInit()
    }  

    protected SuperService getService() {
        return this.bienService
    }
    
    /**
     * Generation du rapport pdf
     **/
    def genererRapportPdf() {
        String nomsociete = ChoraClientInfo.findByNomsociete("OMNIDIOR").nomsociete
        String titrerapport = "Rapport des Biens"
        def listeObjetsExporter = getObjetsToExport()
        def reportDef = new JasperReportDef(name:'rapport_des_Biens.jrxml',
            fileFormat:JasperExportFormat.PDF_FORMAT,
            reportData : listeObjetsExporter,
            parameters : [  'nomsociete':nomsociete, 'titrerapport':titrerapport ]
        )
        def bit = jasperService.generateReport(reportDef).toByteArray()
        def nom_fichier = "rapport_des_Biens.pdf"
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
        String titrerapport = "Rapport des Biens"
        def listeObjetsExporter = getObjetsToExport()
        def reportDef = new JasperReportDef(name:'rapport_des_Biens.jrxml',
            fileFormat:JasperExportFormat.XLS_FORMAT,
            reportData : listeObjetsExporter,
            parameters : [  'nomsociete':nomsociete, 'titrerapport':titrerapport ]
        )
        def bit = jasperService.generateReport(reportDef).toByteArray()
        def nom_fichier = "rapport_des_Biens.xls"
        Filedownload.save(bit, "application/file", nom_fichier);
        
    }
    
    def specialInit(){
          
        filtrer(false)
        statuts = statutService.getByType("BIEN")
        if(statuts.size() > 0)
        statutSelected = statuts.get(0)
        else
        statutSelected = null
        
        types = typeService.getByType("BIEN")
        if(types.size() > 0)
        typeSelected = types.get(0)
        else
        typeSelected = null
        
        projets = utilisateur.projets.toList()//projetService.getProjetByUtilisateur(utilisateur)
        projets = projets.sort{it.code}
        if(projets.size() > 0)
        projetSelected = projets.get(0)
        else
        projetSelected = null
        //        
        //        listeObjets = bienService.getBienByUtilisateur(utilisateur,ofs,maxNb)
        //        

    }    
    
    def chargerBien(){
        projets = utilisateur.projets.toList()        
        bienParents = bienService.getBienByUtilisateur(utilisateur)    
        bienParents = bienParents.sort{it.projet.code}
        tailleListe = bienParents.size()        
    }            
    /**
     * Fonction qui gére l'initialisation des listes d'associations au niveau du constructeur
     **/
    def initialiserAssociation() {
        
        //    bienParents = Bien.list()		
              
        //        contenances = Contenance.list()		
        //        if(contenances.size() > 0)
        //        contenanceSelected = contenances.get(0)
        //        else
        //        contenanceSelected = null
                    
    }
    /**
     * Fonction qui permet de ré-initaliser l'association au niveau de l'interface
     * @param del si c'est une réinitionalisation aprés une suppression ou non
     **/
    def reinitialiserAssociation(del) {
        
        if(del) {
            types = typeService.getByType("BIEN")
        }	
        if(types.size() > 0)
        typeSelected = types.get(0)
        else
        typeSelected = null
                
        if(del) {
            projets = utilisateur.projets.toList()//projetService.getProjetByUtilisateur(utilisateur)
        }	
        if(projets.size() > 0)
        projetSelected = projets.get(0)
        else
        projetSelected = null
                    	
        if(del) {
            bienParents = projets*.bien //Bien.list()
        }	
        if(bienParents.size() > 0)
        bienParentSelected = bienParents.get(0)
        else
        bienParentSelected = null
                    	
        //        if(del) {
        //            contenances = Contenance.list()
        //        }	
        //        if(contenances.size() > 0)
        //        contenanceSelected = contenances.get(0)
        //        else
        //        contenanceSelected = null

        if(del) {
            statuts = statutService.getByType("BIEN")
        }	
        if(statuts.size() > 0)
        statutSelected = statuts.get(0)
        else
        statutSelected = null
                    
    }
    /**
     * Fonction qui copie la valeur de l'association é l'élément courant
     **/
    def actualiserValeurAssociation() {
           		
        //        objet.type = typeSelected
        //        if(types.size() > 0) {
        //            def bindertype = new AnnotateDataBinder(this.getFellow("cotypes"))
        //            typeSelected = types.get(0)
        //            bindertype.loadAll()
        //        }
        //        else
        //        typeSelected = null
        //        
        //        objet.projet = projetSelected
        //        objet.bienParent = bienParentSelected
        //        
        //        if(projets.size() > 0) {
        //            def bindercontenance = new AnnotateDataBinder(this.getFellow("coprojets"))
        //            projetSelected = projets.get(0)
        //            bindercontenance.loadAll()
        //        }
        //        else
        //        projetSelected = null
        //                
        //        if(bienParents.size() > 0) {
        //            def binderbienParent = new AnnotateDataBinder(this.getFellow("cobienParents"))
        //            bienParentSelected = bienParents.get(0)
        //            binderbienParent.loadAll()
        //        }
        //        else
        //        bienParentSelected = null
        //                    		
        //        objet.contenance = contenanceSelected
        //        if(contenances.size() > 0) {
        //            def bindercontenance = new AnnotateDataBinder(this.getFellow("cocontenances"))
        //            contenanceSelected = contenances.get(0)
        //            bindercontenance.loadAll()
        //        }
        //        else
        //        contenanceSelected = null
        //                
        //        objet.statut = statutSelected
        //        if(statuts.size() > 0) {
        //            def binderstatut = new AnnotateDataBinder(this.getFellow("costatuts"))
        //            statutSelected = statuts.get(0)
        //            binderstatut.loadAll()
        //        }
        //        else
        //        statutSelected = null
                    
    }
    /**
     * Fonction qui fait la liaison entre l'association l'élément selectionné et la liste dans le crud
     **/    
    def afficherValeurAssociation() {
        
        //        if(objet.type){
        //            def bindertype = new AnnotateDataBinder(this.getFellow("cotypes"))
        //            typeSelected = types.find{ it.id == Bien.findById(objet.id).type.id }
        //            bindertype.loadAll()
        //        }
        //        else{
        //            typeSelected = null 
        //        }
        //        
        //        if (objet.bienParent){
        //            def binderbienParent = new AnnotateDataBinder(this.getFellow("cobienParents"))
        //            bienParentSelected = bienParents.find{ it.id == Bien.findById(objet.id).bienParent.id }
        //            binderbienParent.loadAll()
        //        } 
        //        else{
        //            bienParentSelected = null
        //        }
        //        
        //        if (objet.contenance){
        //            def bindercontenance = new AnnotateDataBinder(this.getFellow("cocontenances"))
        //            contenanceSelected = contenances.find{ it.id == Bien.findById(objet.id).contenance.id }
        //            bindercontenance.loadAll()
        //        }
        //        else{
        //            contenanceSelected = null 
        //        }
        //        
        //        if(objet.projet){
        //            def binderprojet = new AnnotateDataBinder(this.getFellow("coprojets"))
        //            projetSelected = projets.find{ it.id == Bien.findById(objet.id).projet.id }
        //            binderprojet.loadAll()
        //        }
        //        else{
        //            projetSelected = null
        //        }
        //       
        //        if (objet.statut){            
        //            def binderstatut =  new AnnotateDataBinder(this.getFellow("costatuts"))
        //            statutSelected = statuts.find{ it.id == Bien.findById(objet.id).statut.id}
        //            binderstatut.loadAll()
        //        }
        //        else{
        //            statutSelected = null 
        //        }
    
    }
    
    def actualiserBienParent(){
        bienParents = bienService.getAllBienByProjet(projetSelected.code)
        new AnnotateDataBinder(this.getFellow("cobienParents")).loadAll()
        
      
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
    
    def add(){
        Boolean exist = bienService.existeBienCode (objet,projetSelected.code)
        if (!exist){
            super.add()
        }
        else{
            Messagebox.show("Le code "+ objet.numero + " existe dans le projet " + projetSelected.toString(), "Erreur", Messagebox.OK, Messagebox.EXCLAMATION)
        }
    }
    
    def update(){
        Boolean exist =  bienService.existeBienCode (objet,projetSelected.code,true)
        if (!exist){
            super.update()
        }
        else{
            Messagebox.show("Le code "+ objet.numero + " existe dans le projet " + projetSelected.toString(), "Erreur", Messagebox.OK, Messagebox.EXCLAMATION)
        }
    }
    
    def actualiser(){
        chargerBien()
        new AnnotateDataBinder(this.getFellow("lstObjet")).loadAll() 
    }
    
    def select(){
        objet = objetSelected	                
        rafraichirField()
        activerBoutons(true)
        
        String label
        if (objet.statut){
            if (objet.statut.code == "04" || objet.statut.code == "30"){
                this.getFellow("btnBloquer").visible = true 
          
                if (objet.statut.code == "04"){
            
                    label = "Bloquer"
                }
                else if (objet.statut.code == "30"){
            
                    label = "Débloquer"
                }
            
                this.getFellow("btnBloquer").label = label 
            }
            else 
            {
                this.getFellow("btnBloquer").visible = false   
            }
        }
    }
    
    def importation(media) {
        String resultat = excelImporterService.importerBiens(media)
        Messagebox.show(resultat, "Notification" ,  Messagebox.OK, Messagebox.INFORMATION)
        rafraichirList()
    }
    
    def updateBienClient(){
        phaseService.updateBienClient()
    }
    
    def nettoyerStatutBien() {
        moulinetteService.miseAjourStatusBienPhase()
    }
    
    def bloquerBien()
    {
        String codeBien = null
        String label
        
        if (objet.statut.code == "04"){
            codeBien = "30"
            label = "Débloquer"
        }
        else if (objet.statut.code == "30"){
            codeBien = "04"
            label = "Bloquer"
        }
        
        if (codeBien){
            objet.statut = statutService.getByCode(codeBien)
            super.update()
        }                
        
    }
    
    def cancel() {        
        objet = clazz.newInstance()
        rafraichirField()
        activerBoutons(false)
        annulerSelection()
    }
}
