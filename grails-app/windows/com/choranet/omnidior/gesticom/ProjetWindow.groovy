
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
import com.choranet.zk.BienManager


/**
 * Projet Window Object
 **/
class ProjetWindow extends SuperWindow {
    /**
     * Service pour la gestion de l'objet Projet
     **/
    def projetService
    def statutService
    def typeService
    def bienService
    def contenanceService
    def utilisateurService
    def excelImporterService
  
    def jasperService
    private Log logger = LogFactory.getLog(ProjetWindow.class)
    def types	
    def typeSelected
    def villes	
    def villeSelected    
    def modeleTypeService
    def modeles
    def modeleSelected
    def statuts
    def statutSelected
    def utilisateur = session.utilisateur
    def groupeClients
    def groupeClientSelected     
                
    /**
     * Constructeur
     **/
   

    public ProjetWindow (statutService,typeService,projetService) {
    
        super(Projet.class,false)
        this.statutService = statutService
        this.typeService = typeService
        this.projetService = projetService
        
        specialInit()
    }  
    protected SuperService getService() {
        return this.projetService
    }
    
    /**
     * Generation du rapport pdf
     **/
    def genererRapportPdf() {
        String nomsociete = ChoraClientInfo.findByNomsociete("OMNIDIOR").nomsociete
        String titrerapport = "Rapport des Projets"
        def listeObjetsExporter = getObjetsToExport()
        def reportDef = new JasperReportDef(name:'rapport_des_Projets.jrxml',
            fileFormat:JasperExportFormat.PDF_FORMAT,
            reportData : listeObjetsExporter,
            parameters : [  'nomsociete':nomsociete, 'titrerapport':titrerapport ]
        )
        def bit = jasperService.generateReport(reportDef).toByteArray()
        def nom_fichier = "rapport_des_Projets.pdf"
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
        String titrerapport = "Rapport des Projets"
        def listeObjetsExporter = getObjetsToExport()
        def reportDef = new JasperReportDef(name:'rapport_des_Projets.jrxml',
            fileFormat:JasperExportFormat.XLS_FORMAT,
            reportData : listeObjetsExporter,
            parameters : [  'nomsociete':nomsociete, 'titrerapport':titrerapport ]
        )
        def bit = jasperService.generateReport(reportDef).toByteArray()
        def nom_fichier = "rapport_des_Projets.xls"
        Filedownload.save(bit, "application/file", nom_fichier);
        
    }    
    /**
     * Fonction qui g�re l'initialisation des listes d'associations au niveau du constructeur
     **/
    def initialiserAssociation() {        
        modeles = Modele.list()		
        villes = Ville.list()		
        groupeClients = ChoraClientInfo.list()
        
        if (groupeClients && groupeClients.size()>0){
            groupeClientSelected =  groupeClients.get(0)
        }
        else{
            groupeClientSelected = null
        }        
        
        
        if (modeles && modeles.size()>0){
            modeleSelected =  modeles.get(0)
        }
        else{
            modeleSelected = null
        } 
        
        if (villes && villes.size()>0){
            villeSelected =  villes.get(0)
        }
        else{
            villeSelected = null
        }
        
    }
    
    def specialInit(){
        filtrer(false)
        statuts = statutService.getByType("PROJET")
        statutSelected = statuts.find {it.code.equals("01")}
        types = typeService.getByType("PROJET") 
        

        if (types && types.size()>0){
            typeSelected =  types.get(0)
        }
        else{
            typeSelected = null
        } 
                        
    }    
    /**
     * Fonction qui permet de r�-initaliser l'association au niveau de l'interface
     * @param del si c'est une r�initionalisation apr�s une suppression ou non
     **/
    def reinitialiserAssociation(del) {
        modeleSelected = null
        typeSelected = null
        villeSelected = null
        statutSelected = statuts.find {it.code.equals("01")}
        
        if(modeles && modeles.size()>0){
            modeleSelected = modeles.get(0)
        }
        else{
            modeleSelected = null 
        }
        
        if(types && types.size()>0){
            typeSelected = types.get(0)
        }
        else{
            typeSelected = null 
        }
        
        if(groupeClients && groupeClients.size()>0){
            groupeClientSelected = groupeClients.get(0)
        }
        else{
            groupeClientSelected = null 
        }
        
        if(villes && villes.size()>0){
            villeSelected = villes.get(0)
        }
        else{
            villeSelected = null 
        }
        
        new AnnotateDataBinder(this.getFellow("comodeles")).loadAll()
        new AnnotateDataBinder(this.getFellow("cotypes")).loadAll()
        new AnnotateDataBinder(this.getFellow("costatuts")).loadAll()
        new AnnotateDataBinder(this.getFellow("covilles")).loadAll()
        
        desactiverChamps(false)
    }
    /**
     * Fonction qui copie la valeur de l'association � l'�l�ment courant
     **/
    def actualiserValeurAssociation() {
        objet.modele = modeleSelected
        objet.type = typeSelected
        objet.ville = villeSelected
        objet.statut = statutSelected
        objet.groupeClient = groupeClientSelected
        //        statutSelected = statuts.find {it.code.equals("01")}
        new AnnotateDataBinder(this.getFellow("costatuts")).loadAll()
    }
    /**
     * Fonction qui fait la liaison entre l'association l'�l�ment selectionn� et la liste dans le crud
     **/
    def afficherValeurAssociation() {
        		
        def bindertype = new AnnotateDataBinder(this.getFellow("cotypes"))
        typeSelected = types.find{ it.id == Projet.findById(objet.id).type.id }
        bindertype.loadAll()
                    		
        def binderville = new AnnotateDataBinder(this.getFellow("covilles"))
        villeSelected = villes.find{ it.id == Projet.findById(objet.id).ville.id }
        binderville.loadAll()
        
        def binderstatut =  new AnnotateDataBinder(this.getFellow("costatuts"))
        statutSelected = statuts.find{ it.id == Projet.findById(objet.id).statut.id}
        binderstatut.loadAll()
        
        def bindermodele = new AnnotateDataBinder(this.getFellow("comodeles"))
        modeleSelected = modeles.find{ it.id == Projet.findById(objet.id).modele.id }
        bindermodele.loadAll()
        
        def bindergroupeClient = new AnnotateDataBinder(this.getFellow("cogroupeClients"))
        groupeClientSelected = groupeClients.find{ it.id == Projet.findById(objet.id).groupeClient.id }
        bindergroupeClient.loadAll()
        
        if (modeleSelected != null){
            genererArchitectureBiens()
        }
        
        desactiverChamps (true)
                    
    }
    
    def genererArchitectureBiens(){
        this.getFellow("tabboxCrudBien").visible = true
        def listModeleType = modeleTypeService.getTypeByModele(objet.modele)
       
        Tabs TabsParent = this.getFellow("tabsCrudBien")
        for (Iterator it = TabsParent.getChildren().iterator(); it.hasNext();) {
            it.next();
            it.remove();
        }

        Tabpanels TabpanelsParent = this.getFellow("tabpanelsCrudBien")
        for (Iterator it = TabpanelsParent.getChildren().iterator(); it.hasNext();) {
            it.next();
            it.remove();
        }
            
        if (listModeleType != null && listModeleType.size() > 0){
            def compt = 0
            BienManager bmParent;
            listModeleType.each
            { tm ->
                Tab tb = new Tab()
                tb.setLabel(tm.type.intitule)
                
                TabsParent.appendChild(tb)
                
                Tabpanel tp = new Tabpanel()
                if(compt == 0) {
                    BienManager bm = new BienManager(objet, tm.type, null, contenanceService, bienService, statutService)
                    bm.setParent(tp);
                    bmParent = bm;
                } else {
                    BienManager bm = new BienManager(objet, tm.type, listModeleType[compt-1].type, contenanceService, bienService, statutService)
                    bm.setParent(tp);
                    bmParent.bmFils = bm;
                    bmParent = bm;
                }
                TabpanelsParent.appendChild(tp)
                compt++
            }
            
            new AnnotateDataBinder(this.getFellow("tabboxCrudBien")).loadAll()
        }

    }
    
    def add(){
	actualiserValeurAssociation()
        try {     
            objet = getService().update(objet)           
            
            utilisateur.addToProjets(objet)
            utilisateur = utilisateurService.update(utilisateur)
            session.utilisateur  = utilisateur  
            
            genererArchitectureBiens()
            Messagebox.show("Enregistrement terminé", "Sauvegarde", Messagebox.OK, Messagebox.INFORMATION)
            //filtrer()
            rafraichirList()
            activerBoutons(true)
        } catch(Exception ex) {
            logger.error "Error: ${ex.message}", ex
            if(ex.message.contains("com.choranet.omnidior.gesticom.Projet.code.unique")){
                Messagebox.show("Echec lors de la transaction\n" + "Le code [" + objet.code +"] existe dans la liste des projets", "Erreur", Messagebox.OK, Messagebox.EXCLAMATION)
            }
            else
            Messagebox.show("Echec lors de la transaction\n" + ex.message, "Erreur", Messagebox.OK, Messagebox.ERROR)
        } finally {

        }
    }
    
    def update() {
        actualiserValeurAssociation()
        try {
            objet = getService().update(objet)
            Messagebox.show("Modification terminée", "Sauvegarde", Messagebox.OK, Messagebox.INFORMATION)
            //filtrer()
            rafraichirList()
        }
        catch(Exception e) {
            logger.error "Error: ${e.message}", e
            if(e.message.contains("com.choranet.omnidior.gesticom.Projet.code.unique")){
                Messagebox.show("Echec lors de la transaction\n" + "Le code [" + objet.code +"] existe dans la liste des projets", "Erreur", Messagebox.OK, Messagebox.EXCLAMATION)
            }
            else
            Messagebox.show("Problème lors de la mise à jour", "Erreur", Messagebox.OK, Messagebox.ERROR)
        }
        finally {
    
        }
    }
    
    def delete() {
        if (bienService.existeBienProjet(objet) == 0 ){
            def pjts = []
            for (it in utilisateur.projets){
                if(!it.code.equals(objet.code)) {
                    pjts.add(it)
                } 
            }
            utilisateur.projets = pjts
            utilisateur = utilisateurService.update(utilisateur)


            getService().delete(objet)  
            cancel()
            filtrer()
            
            new AnnotateDataBinder(this.getFellow("lstObjet")).loadAll() 
        }
        else {
            Messagebox.show("Il existe des biens pour ce projet", "Erreur", Messagebox.OK, Messagebox.ERROR) 
        }

    }
    
    def cancel() {        
        objet = clazz.newInstance()
	reinitialiserAssociation(false)
        rafraichirField()
        activerBoutons(false)
        annulerSelection()
        Tabs TabsParent = this.getFellow("tabsCrudBien")
        for (Iterator it = TabsParent.getChildren().iterator(); it.hasNext();) {
            it.next();
            it.remove();
        }

        Tabpanels TabpanelsParent = this.getFellow("tabpanelsCrudBien")
        for (Iterator it = TabpanelsParent.getChildren().iterator(); it.hasNext();) {
            it.next();
            it.remove();
        }
        this.getFellow("westPanel").open = false  
    }
    
    def select() {                    
        super.select()
        genererArchitectureBiens()
        this.getFellow("westPanel").open = true
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
    
    def chargerProjet(){
        listeObjets = projetService.getProjetByUtilisateur(utilisateur,ofs,maxNb)
        def ls = utilisateur.projets.toList()//projetService.getProjetByUtilisateur(utilisateur)
        if (ls){
            tailleListe = ls.size()
        }
    }
    
    def actualiser(){
        chargerProjet() 
        new AnnotateDataBinder(this.getFellow("lstObjet")).loadAll() 
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
    
    def nouveauProjet(){
        objet = clazz.newInstance()
	reinitialiserAssociation(false)
        rafraichirField()
        activerBoutonsForAdd(false)
        annulerSelection()
        this.getFellow("tabboxCrudBien").visible = false
        desactiverChamps(false)
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
    
    def importation(media) {
        String resultat = excelImporterService.importerProjets(media,utilisateur)
        Messagebox.show(resultat, "Notification" ,  Messagebox.OK, Messagebox.INFORMATION)
        rafraichirList()
    }
    
    def desactiverChamps(Boolean desactiver){
        this.getFellow("fieldCode").disabled = desactiver
        this.getFellow("comodeles").disabled = desactiver
        this.getFellow("fieldDesignation").disabled = desactiver
    }
}

