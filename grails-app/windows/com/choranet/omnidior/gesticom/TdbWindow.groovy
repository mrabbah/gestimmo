
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
import com.choranet.zk.MiniBienManager;
import com.choranet.zk.BienManager;

/**
 * Pays Window Object
 **/
class TdbWindow extends Window {
            
    def jasperService
    def projetService
    def clientService
    def phaseService
    def paiementService
    def sourceInformationService
    def modeleTypeService
    def contenanceService
    
    def etatprojetmdel
    def etatclientmdel
    
    def rappelPaiements
    def listePhases
    def listeAlertes
    def rappelEcheances
    
    def existListeAlertes = false
    def existRappelPaiements = false
    def existListePhases = false
    def existRappelEcheances = false
    
    def projets
    def projetSelected
    
    def projetsBiens
    
    def projetDateDebut
    def projetDateFin
    def session = RequestContextHolder.currentRequestAttributes().getSession()
    def utilisateur = session.utilisateur
    def typeClients 
    def typeClientSelected
    
    def clients
    def clientRProjetProspectSelected
    Projet projetRapportSelected
    Projet projetRProspectSelected 
    Projet projetRSourceSelected
    Projet projetTypeSelected
    def villes
    def villeSelected
    def villeSelected2
    
    Integer age1
    Integer age2
    def sexes
    def sexeSelected
    def sourceInformations
    def sourceInformationSelected 
    
    def phases
    def phaseSelected
    
    def typeBiens
    def typeBienSelected
    Double sup1
    Double sup2
    Projet projetPhaseSelected 
    Projet projetPhase2Selected
    Projet projetPhase3Selected
    
    def notaires
    def notaireSelected
    
    def commercials 
    def commercialSelected
    
    def typePaiements
    def typePaiementSelected
    ModePaiement mpTout = new ModePaiement(type : 'Tous les modes')
       
    
    def projetPaiementSelected
    
    def etatprojet = new SimplePieModel() 
    def etatprojetPhase = new SimplePieModel()  
    /**
     * Logger de la class PaysWindow
     **/
    
    def alerteService
    def statutService
    def bienService
    
    Boolean estPDF = false
    Boolean estEXCEL = false
        
    def exportFormat
    String extension
    String strAppExt  
    
    def modeleSelected
    Projet projetBienSelected
    Statut typeClientProjetSelected
        
    
    private Log logger = LogFactory.getLog(TdbWindow.class)    
    /**
     * Constructeur
     **/
    public TdbWindow (projetService,alerteService,paiementService,echeanceService,phaseService,statutService,typeService){
               
        this.projetService = projetService        
        this.phaseService = phaseService
        this.paiementService = paiementService
        this.alerteService
        
        
        projets = utilisateur.projets.toList().sort{it.id} //projetService.getProjetByUtilisateur(utilisateur)        
        projetsBiens = projets  //utilisateur.projets.toList().sort{it.code}
        
        def ids = utilisateur.clients.id
        def idds = [] //liste code clients de l'utilisateur 
        ids.each {
            idds.addAll(it)
        }
        
        def idsp = utilisateur.projets.id
        def iddsp = [] //liste code projets de l'utilisateur
        idsp.each {
            iddsp.addAll(it)
        }
        
        if(projets.size() > 0){
            projetSelected = projets.get(0)
            projetRapportSelected = projets.get(0)
            projetRProspectSelected = projets.get(0)
            projetRSourceSelected =  projets.get(0)
            projetPhaseSelected = projets.get(0)
            projetPhase2Selected = projets.get(0)
            projetPhase3Selected = projets.get(0)
            projetPaiementSelected = projets.get(0)
            projetTypeSelected = projets.get(0)
            genererChart(false)
        }
        else
        {
            projetSelected = null 
            projetRapportSelected = null
            projetRProspectSelected = null 
            projetRSourceSelected = null
            projetPhaseSelected = null
            projetPhase2Selected = null
            projetPaiementSelected = null
            projetTypeSelected = null
        }
        
        sourceInformations = SourceInformation.list()		
        if(sourceInformations.size() > 0)
        sourceInformationSelected = sourceInformations.get(0)
        else
        sourceInformationSelected = null

        listeAlertes = alerteService.getAlerteByDate(new Date(),idds,iddsp)
        rappelPaiements = paiementService.getPaiementNonValide(idds,iddsp)
        rappelEcheances = echeanceService.getEcheanceBydateEtUtilisateur(new Date(),idds,iddsp)//echeanceService.getEcheanceBydateEtUtilisateur(new Date(),utilisateur)
        listePhases  = phaseService.getPhaseByDate(new Date(),idds,iddsp)
        
        if (rappelPaiements != null && rappelPaiements.size() > 0){
            existRappelPaiements = true
        } 
                                  
        if (rappelEcheances != null && rappelEcheances.size() > 0){
            existRappelEcheances = true
        }                
                  
        if (listeAlertes != null && listeAlertes.size() > 0){
            existListeAlertes = true
        }                              
                
        if (listePhases != null && listePhases.size() > 0){
            existListePhases = true
        }
        
        //        clients = utilisateur.clients.toList().sort{it.nom} //clientService.getClientByUtilisateur(utilisateur)
        //        if(clients.size() > 0){
        //            clientRProjetProspectSelected = clients.get(0)
        //        }
        //        else 
        //        clientRProjetProspectSelected = null 
        
        typeClients = statutService.getByType("CLIENT")
        if(typeClients.size() > 0){
            typeClientSelected = typeClients.get(0)
            typeClientProjetSelected = typeClients.get(0)
        }
        else{
            typeClientSelected = null 
            typeClientProjetSelected = null
        }
        
        sexes = ['F','M']
        sexeSelected = sexes[0]
        
        phases = ['Option','Réservation','Compromis','Contrat','Toutes les phases']
        phaseSelected = phases[4]
        
        villes = Ville.list()		
        if(villes.size() > 0)
        {
            villeSelected = villes.get(0)
            villeSelected2 = villes.get(0)
        }
        else
        {
            villeSelected = null    
            villeSelected2 = null
        }
        
        typeBiens = typeService.getByType("BIEN")
        if(typeBiens.size() > 0)     
        typeBienSelected = typeBiens.get(0)
        else
        typeBienSelected = null  
        
        notaires = Notaire.list()
        if(notaires.size() > 0)
        notaireSelected = notaires.get(0)
        else
        notaireSelected = null 
        
        commercials = Utilisateur.list()
        if(commercials.size() > 0)
        commercialSelected = commercials.get(0)
        else
        commercialSelected = null 
        
        typePaiements = ModePaiement.list()//statutService.getByType("PAIEMENTTYPE")
        
        typePaiements.add(mpTout)
        if(typePaiements.size() > 0)
        typePaiementSelected = typePaiements.get(0)
        else
        typePaiementSelected = null
    }
    
    def genererChartProjet(Boolean actualiser){
        etatprojet =  new SimplePieModel()
        def situation = projetService.getSituationProjet(projetSelected)
        if (situation != null && situation.size() > 0)
        {
            situation.each {
                etatprojet.setValue(it[0],it[1]);
                
            }
        }
        if (actualiser){
            new AnnotateDataBinder(this.getFellow("etatprojet")).loadAll()
        }
    }
    
    def genererChartPhase (Boolean actualiser){
        etatprojetPhase =  new SimplePieModel()
        def situation = projetService.getSituationProjetPhase(projetSelected)
        if (situation != null && situation.size() > 0)
        {
            situation.each {
                etatprojetPhase.setValue(it[0],it[1]);
                
            }
        }
        if (actualiser){
            new AnnotateDataBinder(this.getFellow("etatPhase")).loadAll()
        }
        
    }
    
    def genererChart(Boolean actualiser){
        genererChartProjet(actualiser)
        genererChartPhase (actualiser)
    }
    
    def setParamExport(String formatToExport){
        if (formatToExport == "PDF"){
            estEXCEL = true 
            exportFormat = JasperExportFormat.PDF_FORMAT
            extension = "pdf"
            strAppExt = "application/pdf"
        }
        else if (formatToExport == "EXCEL"){
            estPDF = true 
            exportFormat = JasperExportFormat.XLS_FORMAT
            extension = "xls"
            strAppExt = "application/file" 
        }
    }
    
    def genererRapport(String nom_fichier, bit){
        Media amedia = new AMedia(nom_fichier, extension, strAppExt, bit);
        Map map = new HashMap();
        map.put("content", amedia);
        Executions.createComponents("/zul/util/pdfviewer.zul", null, map).doModal();
    }
    
    def genererRapportClientProspect(String formatToExport){
        
        setParamExport(formatToExport)
        
        def clientsListe = clientService.getClientByUtilisateurEtType(typeClientSelected)
        String nomsociete = ChoraClientInfo.list().get(0).nomsociete
        String titrerapport = "LISTE DES " + typeClientSelected.intitule + "S"
       
        def reportDef
        if (formatToExport == "PDF"){
            reportDef = new JasperReportDef(name:'rapport_des_ClientsProspects.jrxml',
                fileFormat:exportFormat,
                reportData : clientsListe,
                parameters : ['nomsociete':nomsociete, 'titrerapport':titrerapport]
            )     
        }
        else if (formatToExport == "EXCEL"){
            reportDef = new JasperReportDef(name:'rapport_des_ClientsProspects.jrxml',
                fileFormat:exportFormat,
                reportData : clientsListe,
                parameters : ['nomsociete':nomsociete, 'titrerapport':titrerapport, 'IS_IGNORE_PAGINATION' : Boolean.TRUE]
            )     
        }
            
        def bit = jasperService.generateReport(reportDef).toByteArray()
        def nom_fichier = "Liste_des_" + typeClientSelected.intitule+ "S_" + utilisateur.username +"." + extension
        //Filedownload.save(bit, "application/pdf", nom_fichier);
        genererRapport(nom_fichier,bit)
    }
        
    def genererRapportClientProspectParProjetEtType(String formatToExport){
        
        setParamExport(formatToExport)
        
        def clientsListe = clientService.getClientByUtilisateurEtProjetEtType(projetTypeSelected,typeClientProjetSelected)
        String nomsociete = projetTypeSelected.groupeClient.nomsociete
        String titrerapport = "liste des clients/prospects par projet"
        String intitule_projet = projetTypeSelected.code + "-" + projetTypeSelected.designation

        def reportDef
        if (formatToExport == "PDF"){
            reportDef = new JasperReportDef(name:'rapport_des_ClientsProspectsParProjet.jasper',
                fileFormat:exportFormat,
                reportData : clientsListe,
                parameters : ['nomsociete':nomsociete, 'titrerapport':titrerapport,'champ1' : 'Projet' ,'valeur1': intitule_projet]
            )
        } else if (formatToExport == "EXCEL"){
            reportDef = new JasperReportDef(name:'rapport_des_ClientsProspectsParProjet.jasper',
                fileFormat:exportFormat,
                reportData : clientsListe,
                parameters : ['nomsociete':nomsociete, 'titrerapport':titrerapport,'champ1' : 'Projet' ,'valeur1': intitule_projet, 'IS_IGNORE_PAGINATION' : Boolean.TRUE]
            )
        }
        def bit = jasperService.generateReport(reportDef).toByteArray()
        def nom_fichier = "Liste_des_clients_prospects_" + projetTypeSelected.code + "_" + projetTypeSelected.designation + "_" + utilisateur.username +"." + extension
        //Filedownload.save(bit, "application/pdf", nom_fichier);

        genererRapport(nom_fichier,bit)
    }
        
    def genererRapportClientProspectParProjet(String formatToExport){
        
        setParamExport(formatToExport)
        
        def clientsListe = clientService.getClientByUtilisateurEtProjet(projetRapportSelected)
        String nomsociete = projetRProspectSelected.groupeClient.nomsociete
        String titrerapport = "liste des clients/prospects par projet"
        String intitule_projet = projetRapportSelected.code + "-" + projetRapportSelected.designation
        //        def nombreCl = projetService.getNombreClientProspectParProjet(projetRapportSelected.id,"CLIENT")
        //        def nombrePl = projetService.getNombreClientProspectParProjet(projetRapportSelected.id,"PROSPECT")

        Integer cl = 0  
        Integer pr = 0

        for (l in clientsListe){
            if(l.statut.intitule == "CLIENT"){
                cl +=1  
            }
            else if (l.statut.intitule == "PROSPECT")
            {
                pr += 1 
            }           
           
        }

        def reportDef
        if (formatToExport == "PDF"){
            reportDef = new JasperReportDef(name:'rapport_des_ClientsProspectsParProjetC.jasper',
                fileFormat:exportFormat,
                reportData : clientsListe,
                parameters : ['nomsociete':nomsociete, 'titrerapport':titrerapport,'champ1' : 'Projet' ,'valeur1': intitule_projet, 'nombreClient' :cl, 'nombreProspect':pr]
            )
        } else if (formatToExport == "EXCEL"){
            reportDef = new JasperReportDef(name:'rapport_des_ClientsProspectsParProjetC.jasper',
                fileFormat:exportFormat,
                reportData : clientsListe,
                parameters : ['nomsociete':nomsociete, 'titrerapport':titrerapport,'champ1' : 'Projet' ,'valeur1': intitule_projet, 'nombreClient' :cl, 'nombreProspect':pr, 'IS_IGNORE_PAGINATION' : Boolean.TRUE]
            )
        }
        
        def bit = jasperService.generateReport(reportDef).toByteArray()
        def nom_fichier = "Liste_des_clients_prospects_" + projetRapportSelected.code + "_" + projetRapportSelected.designation + "_" + utilisateur.username +"." + extension
        //Filedownload.save(bit, "application/pdf", nom_fichier);

        genererRapport(nom_fichier,bit)
    }
        
    
    def genererRapportClientProspectParProjetProspect(String formatToExport){
        setParamExport(formatToExport)
        def clientsListe = clientService.getClientByUtilisateurEtProjetEtClient(projetRProspectSelected,clientRProjetProspectSelected)
        String nomsociete = projetRProspectSelected.groupeClient.nomsociete
        String titrerapport = "Liste par client par projet"
        String intitule_projet = projetRProspectSelected.code + "-" + projetRProspectSelected.designation
        
        def reportDef
        if (formatToExport == "PDF"){
            reportDef = new JasperReportDef(name:'rapport_des_ClientsProspectsParProjet.jasper',
                fileFormat:exportFormat,
                reportData : clientsListe,
                parameters : ['nomsociete':nomsociete, 'titrerapport':titrerapport, 'champ1' : 'Projet' ,'valeur1': intitule_projet]
            )
        } else if (formatToExport == "EXCEL"){
            reportDef = new JasperReportDef(name:'rapport_des_ClientsProspectsParProjet.jasper',
                fileFormat:exportFormat,
                reportData : clientsListe,
                parameters : ['nomsociete':nomsociete, 'titrerapport':titrerapport, 'champ1' : 'Projet' ,'valeur1': intitule_projet, 'IS_IGNORE_PAGINATION' : Boolean.TRUE]
            )
        }
        
        def bit = jasperService.generateReport(reportDef).toByteArray()
        def nom_fichier = "Liste_client_prospect_" + projetRProspectSelected.code + "_" + projetRProspectSelected.designation + "_" + utilisateur.username +"." + extension
        //Filedownload.save(bit, "application/pdf", nom_fichier);

        genererRapport(nom_fichier,bit)
    }
      
    def genererRapportClientProspectParProjetSourceInfo(String formatToExport){
        setParamExport(formatToExport)
        def clientsListe = clientService.getClientByUtilisateurEtProjetEtSource(projetRSourceSelected,sourceInformationSelected)
        def statistques = sourceInformationService.getCountSourceInformationByProjet (projetRSourceSelected.code)
        
        String staNom1 = ""
        String staNom2 = ""
        String staNom3 = ""
        String staNom4 = ""
        String staNom5 = "Autre"
        
        Integer staValeur1 = 0 
        Integer staValeur2 = 0 
        Integer staValeur3 = 0 
        Integer staValeur4 = 0 
        Integer staValeur5 = 0 
        
        Integer i = 1 
        for (it in statistques){
            switch (i){
                case "1":
                staNom1 = it[0]
                staValeur1 =  it[1]
                break
                case "2":
                staNom2 = it[0]
                staValeur2 =  it[1]
                break
                case "3":
                staNom3 = it[0]
                staValeur3 =  it[1]
                break
                case "4":
                staNom4 = it[0]
                staValeur4 =  it[1]
                break
                
            }
            i +=1
            staValeur5 +=  it[1]
        }
        staValeur5 = staValeur5 - (staValeur1 + staValeur2 +staValeur3 +staValeur4)
        String nomsociete = projetRSourceSelected.groupeClient.nomsociete
        String titrerapport = "Liste par client par projet et source d'information"
        String intitule_projet = projetRSourceSelected.code + "-" + projetRSourceSelected.designation
        
        def reportDef
        if (formatToExport == "PDF"){
            reportDef = new JasperReportDef(name:'rapport_des_ClientsProspectsParProjetSIC.jasper',
                fileFormat:exportFormat,
                reportData : clientsListe,
                parameters : ['nomsociete':nomsociete, 'titrerapport':titrerapport, 'champ1' : 'Projet' ,'valeur1': intitule_projet, 'champ2': 'Source d information', 'valeur2' :sourceInformationSelected.intitule,'staNom1' : staNom1, 'staNom2' : staNom2,'staNom3' : staNom3, 'staNom4' : staNom4,'staNom5' : staNom5,'staValeur1' : staValeur1, 'staValeur2' : staValeur2,'staValeur3' : staValeur3, 'staValeur4' : staValeur4,'staValeur5' : staValeur5 ]
            )
        } else if (formatToExport == "EXCEL"){
            reportDef = new JasperReportDef(name:'rapport_des_ClientsProspectsParProjetSIC.jasper',
                fileFormat:exportFormat,
                reportData : clientsListe,
                parameters : ['nomsociete':nomsociete, 'titrerapport':titrerapport, 'champ1' : 'Projet' ,'valeur1': intitule_projet, 'champ2': 'Source d information', 'valeur2' :sourceInformationSelected.intitule,'staNom1' : staNom1, 'staNom2' : staNom2,'staNom3' : staNom3, 'staNom4' : staNom4,'staNom5' : staNom5,'staValeur1' : staValeur1, 'staValeur2' : staValeur2,'staValeur3' : staValeur3, 'staValeur4' : staValeur4,'staValeur5' : staValeur5 , 'IS_IGNORE_PAGINATION' : Boolean.TRUE]
            )
        }
        
        def bit = jasperService.generateReport(reportDef).toByteArray()
        def nom_fichier = "Liste_client_prospect_" + projetRSourceSelected.code + "_" + projetRSourceSelected.designation + "_" + sourceInformationSelected.intitule + utilisateur.username +"."+ extension
        //Filedownload.save(bit, "application/pdf", nom_fichier);

        genererRapport(nom_fichier,bit)
    }
    
    def genererRapportClientProspectParAgeSexe(String formatToExport) {
        setParamExport(formatToExport)
        def clientsListe = clientService.getClientByUtilisateurEtAgeEtSexe(age1,age2,sexeSelected)
        def clientsAll = clientService.getClientBySexe(sexeSelected)
        Date ajourdhui = new Date()
        
        String staNom1 = "[0-18]"
        String staNom2 = "[19-39]"
        String staNom3 = "[40-59]"
        String staNom4 = "[60-74]"
        String staNom5 = "[75+]"
        
        Integer staValeur1 = 0 
        Integer staValeur2 = 0 
        Integer staValeur3 = 0 
        Integer staValeur4 = 0 
        Integer staValeur5 = 0 
        Double ageCl
        
        for (it in clientsAll){
            if (it.dateNaissance){
                ageCl = (ajourdhui - it.dateNaissance)/365
            
                if (ageCl < 19 ) {
                    staValeur1 +=1
                }
                else if (ageCl>= 19 && ageCl < 40){
                    staValeur2 +=1
                }
                else if (ageCl>= 40 && ageCl < 60){
                    staValeur3 +=1
                }
                else if (ageCl>= 60 && ageCl< 75 ){
                    staValeur4 +=1
                }
                else if (ageCl>= 75 ){
                    staValeur5 +=1
                }
            
            }
        }
        
        String nomsociete = ChoraClientInfo.list().get(0).nomsociete
        String titrerapport = "Liste par client par age et sexe"
        String age = "Entre "+ age1 + " et "+ age2 + " ans"
        
        def reportDef
        if (formatToExport == "PDF"){
            reportDef = new JasperReportDef(name:'rapport_des_ClientsProspectsParProjetAgeC.jasper',
                fileFormat:exportFormat,
                reportData : clientsListe,
                parameters : ['nomsociete':nomsociete, 'titrerapport':titrerapport, 'champ1' : 'Age :' ,'valeur1': age, 'champ2': 'Sexe :', 'valeur2': sexeSelected,'staNom1' : staNom1, 'staNom2' : staNom2,'staNom3' : staNom3, 'staNom4' : staNom4,'staNom5' : staNom5,'staValeur1' : staValeur1, 'staValeur2' : staValeur2,'staValeur3' : staValeur3, 'staValeur4' : staValeur4,'staValeur5' : staValeur5 ]
            )
        } else if (formatToExport == "EXCEL"){
            reportDef = new JasperReportDef(name:'rapport_des_ClientsProspectsParProjetAgeC.jasper',
                fileFormat:exportFormat,
                reportData : clientsListe,
                parameters : ['nomsociete':nomsociete, 'titrerapport':titrerapport, 'champ1' : 'Age :' ,'valeur1': age, 'champ2': 'Sexe :', 'valeur2': sexeSelected,'staNom1' : staNom1, 'staNom2' : staNom2,'staNom3' : staNom3, 'staNom4' : staNom4,'staNom5' : staNom5,'staValeur1' : staValeur1, 'staValeur2' : staValeur2,'staValeur3' : staValeur3, 'staValeur4' : staValeur4,'staValeur5' : staValeur5 , 'IS_IGNORE_PAGINATION' : Boolean.TRUE]
            )
        }
        
        def bit = jasperService.generateReport(reportDef).toByteArray()
        def nom_fichier = "Liste_client_prospect_" + age1 + "_" + age2 + "_" + sexeSelected + "_" + utilisateur.username +"."+ extension
        //Filedownload.save(bit, "application/pdf", nom_fichier);

        genererRapport(nom_fichier,bit)
    }
    
    def genererRapportClientProspectParVille(String formatToExport) {
        setParamExport(formatToExport)
        def clientsListe = clientService.getClientByUtilisateurEtVille(utilisateur,villeSelected)
        String nomsociete = ChoraClientInfo.list().get(0).nomsociete
        String titrerapport = "Liste des clients/prospects par ville"
        
        def statistiques = clientService.getCountClientVille(villeSelected.id)
        
        String staNom1 = ""
        String staNom2 = ""
        String staNom3 = ""
        String staNom4 = ""
        String staNom5 = "Autre"
        
        Integer staValeur1 = 0 
        Integer staValeur2 = 0 
        Integer staValeur3 = 0 
        Integer staValeur4 = 0 
        Integer staValeur5 = 0 
        
        Integer i = 1 
        for (it in statistiques){
            switch (i){
                case "1":
                staNom1 = it[0]
                staValeur1 =  it[1]
                break
                case "2":
                staNom2 = it[0]
                staValeur2 =  it[1]
                break
                case "3":
                staNom3 = it[0]
                staValeur3 =  it[1]
                break
                case "4":
                staNom4 = it[0]
                staValeur4 =  it[1]
                break
                
            }
            i +=1
            staValeur5 +=  it[1]
        }
        staValeur5 = staValeur5 - (staValeur1 + staValeur2 +staValeur3 +staValeur4)
        
        def reportDef
        if (formatToExport == "PDF"){
            reportDef = new JasperReportDef(name:'rapport_des_ClientsProspectsParProjetVille.jasper',
                fileFormat:exportFormat,
                reportData : clientsListe,
                parameters : ['nomsociete':nomsociete, 'titrerapport':titrerapport, 'champ1' : 'Ville :' ,'valeur1': villeSelected.intitule,'staNom1' : staNom1, 'staNom2' : staNom2,'staNom3' : staNom3, 'staNom4' : staNom4,'staNom5' : staNom5,'staValeur1' : staValeur1, 'staValeur2' : staValeur2,'staValeur3' : staValeur3, 'staValeur4' : staValeur4,'staValeur5' : staValeur5 ]
            )
        } else if (formatToExport == "EXCEL"){
            reportDef = new JasperReportDef(name:'rapport_des_ClientsProspectsParProjetVille.jasper',
                fileFormat:exportFormat,
                reportData : clientsListe,
                parameters : ['nomsociete':nomsociete, 'titrerapport':titrerapport, 'champ1' : 'Ville :' ,'valeur1': villeSelected.intitule,'staNom1' : staNom1, 'staNom2' : staNom2,'staNom3' : staNom3, 'staNom4' : staNom4,'staNom5' : staNom5,'staValeur1' : staValeur1, 'staValeur2' : staValeur2,'staValeur3' : staValeur3, 'staValeur4' : staValeur4,'staValeur5' : staValeur5 , 'IS_IGNORE_PAGINATION' : Boolean.TRUE]
            )
        }
        
        def bit = jasperService.generateReport(reportDef).toByteArray()
        def nom_fichier = "Liste_client_prospect_" + villeSelected.intitule + "_" + utilisateur.username +"."+ extension
        //Filedownload.save(bit, "application/pdf", nom_fichier);

        genererRapport(nom_fichier,bit)
    }
    
    def genererRapportPhase(String formatToExport){
        setParamExport(formatToExport)
        def clientsListe
        if ( phaseSelected != 'Toutes les phases'){
            def ph = getPhase()
            clientsListe  = phaseService.getPhaseByUtlisateurReport(utilisateur,ph,true)
        }           
        else{
            clientsListe  = phaseService.getPhaseByUtlisateurReport(utilisateur,null,true)
        }
        
        String nomsociete = ChoraClientInfo.list().get(0).nomsociete
        String titrerapport = "Liste des phases [" + phaseSelected + "]" 
        
        String staNom1 = "Option"
        String staNom2 = "Reservation"
        String staNom3 = "Compromis"
        String staNom4 = "Contrat"
        String staNom5 = ""

        
        Integer staValeur1 = 0 
        Integer staValeur2 = 0 
        Integer staValeur3 = 0 
        Integer staValeur4 = 0 
        Integer staValeur5 = 0 

        
        def statistiques = phaseService.getCountPhase(false)
        
        for (it in statistiques){
            switch (it[0]){
                case "OPTION":               
                staValeur1 =  it[1]
                break
                
                case "RESERVATION":               
                staValeur2 =  it[1]
                break
                
                case "COMPROMIS":               
                staValeur3 =  it[1]
                break
                
                case "CONTRAT":               
                staValeur4 =  it[1]
                break
            }
        
        }
        
        def reportDef
        if (formatToExport == "PDF"){
            reportDef = new JasperReportDef(name:'rapport_des_Phases_param_PHASEC.jasper',
                fileFormat:exportFormat,
                reportData : clientsListe,
                parameters : ['nomsociete':nomsociete, 'titrerapport':titrerapport, 'champ1' : 'Phase' ,'valeur1': phaseSelected, 'champ2': '', 'valeur2' :'','staNom1' : staNom1, 'staNom2' : staNom2,'staNom3' : staNom3, 'staNom4' : staNom4,'staNom5' : staNom5,'staValeur1' : staValeur1, 'staValeur2' : staValeur2,'staValeur3' : staValeur3, 'staValeur4' : staValeur4,'staValeur5' : staValeur5 ]
            )
        } else if (formatToExport == "EXCEL"){
            reportDef = new JasperReportDef(name:'rapport_des_Phases_param_PHASEC.jasper',
                fileFormat:exportFormat,
                reportData : clientsListe,
                parameters : ['nomsociete':nomsociete, 'titrerapport':titrerapport, 'champ1' : 'Phase' ,'valeur1': phaseSelected, 'champ2': '', 'valeur2' :'','staNom1' : staNom1, 'staNom2' : staNom2,'staNom3' : staNom3, 'staNom4' : staNom4,'staNom5' : staNom5,'staValeur1' : staValeur1, 'staValeur2' : staValeur2,'staValeur3' : staValeur3, 'staValeur4' : staValeur4,'staValeur5' : staValeur5 , 'IS_IGNORE_PAGINATION' : Boolean.TRUE]
            )
        }
        
        def bit = jasperService.generateReport(reportDef).toByteArray()
        def nom_fichier = "Liste_phases_" + utilisateur.username +"."+ extension
        //Filedownload.save(bit, "application/pdf", nom_fichier);

        genererRapport(nom_fichier,bit)
        
    }
    
    def genererRapportPhaseExpiree(String formatToExport){
        setParamExport(formatToExport)
        phaseService.updatePhaseExpiree()
        def clientsListe
        
        if ( phaseSelected != 'Toutes les phases'){
            def ph = getPhase()
            clientsListe  = phaseService.getPhaseByUtlisateurExpiree(utilisateur,ph)
        }           
        else{
            clientsListe  = phaseService.getPhaseByUtlisateurExpiree(utilisateur,null)
        }
        
        String nomsociete = ChoraClientInfo.list().get(0).nomsociete
        String titrerapport = "Liste des phases expirées [" + phaseSelected + "]" 

        String staNom1 = "Option"
        String staNom2 = "Reservation"
        String staNom3 = "Compromis"
        String staNom4 = "Contrat"
        String staNom5 = ""

        
        Integer staValeur1 = 0 
        Integer staValeur2 = 0 
        Integer staValeur3 = 0 
        Integer staValeur4 = 0 
        Integer staValeur5 = 0 

        
        def statistiques = phaseService.getCountPhase(true)
        
        for (it in statistiques){
            switch (it[0]){
                case "OPTION":               
                staValeur1 =  it[1]
                break
                
                case "RESERVATION":               
                staValeur2 =  it[1]
                break
                
                case "COMPROMIS":               
                staValeur3 =  it[1]
                break
                
                case "CONTRAT":               
                staValeur4 =  it[1]
                break
            }
        
        }
        
        def reportDef
        if (formatToExport == "PDF"){
            reportDef = new JasperReportDef(name:'rapport_des_Phases_param_PHASEC.jasper',
                fileFormat:exportFormat,
                reportData : clientsListe,
                parameters : ['nomsociete':nomsociete, 'titrerapport':titrerapport, 'champ1' : 'Phase' ,'valeur1': phaseSelected, 'champ2': '', 'valeur2' :'','staNom1' : staNom1, 'staNom2' : staNom2,'staNom3' : staNom3, 'staNom4' : staNom4,'staNom5' : staNom5,'staValeur1' : staValeur1, 'staValeur2' : staValeur2,'staValeur3' : staValeur3, 'staValeur4' : staValeur4,'staValeur5' : staValeur5 ]
            )
        } else if (formatToExport == "EXCEL"){
            reportDef = new JasperReportDef(name:'rapport_des_Phases_param_PHASEC.jasper',
                fileFormat:exportFormat,
                reportData : clientsListe,
                parameters : ['nomsociete':nomsociete, 'titrerapport':titrerapport, 'champ1' : 'Phase' ,'valeur1': phaseSelected, 'champ2': '', 'valeur2' :'','staNom1' : staNom1, 'staNom2' : staNom2,'staNom3' : staNom3, 'staNom4' : staNom4,'staNom5' : staNom5,'staValeur1' : staValeur1, 'staValeur2' : staValeur2,'staValeur3' : staValeur3, 'staValeur4' : staValeur4,'staValeur5' : staValeur5 , 'IS_IGNORE_PAGINATION' : Boolean.TRUE]
            )
        }
        
  
        def bit = jasperService.generateReport(reportDef).toByteArray()
        def nom_fichier = "Liste_phases_" + utilisateur.username +"."+ extension
        //Filedownload.save(bit, "application/pdf", nom_fichier);

        genererRapport(nom_fichier,bit)
        
    }
    
    def genererRapportPhasePArTypeBienEtSuperficie(String formatToExport){
        setParamExport(formatToExport)
        def ph = getPhase()
        def clientsListe 
        if ( phaseSelected != 'Toutes les phases'){
            clientsListe = phaseService.getPhaseByUtlisateurEtTypeBienEtSuperficie(utilisateur,ph,typeBienSelected,sup1,sup2)
        }
        else{
            clientsListe = phaseService.getPhaseByUtlisateurEtTypeBienEtSuperficie(utilisateur,null,typeBienSelected,sup1,sup2)
        }
        
        String nomsociete = ChoraClientInfo.list().get(0).nomsociete
        String titrerapport = "Liste des phases [" + phaseSelected + "] Type de bien : [" +typeBienSelected+ "] Superficie [" +sup1 + "," + sup2 +"]" 
       
        def reportDef
        if (formatToExport == "PDF"){
            reportDef = new JasperReportDef(name:'rapport_des_Phases_param.jasper',
                fileFormat:exportFormat,
                reportData : clientsListe,
                parameters : ['nomsociete':nomsociete, 'titrerapport':titrerapport, 'champ1' : 'Phase' ,'valeur1': '', 'champ2': '', 'valeur2' :'']
            )
        } else if (formatToExport == "EXCEL"){
            reportDef = new JasperReportDef(name:'rapport_des_Phases_param.jasper',
                fileFormat:exportFormat,
                reportData : clientsListe,
                parameters : ['nomsociete':nomsociete, 'titrerapport':titrerapport, 'champ1' : 'Phase' ,'valeur1': '', 'champ2': '', 'valeur2' :'', 'IS_IGNORE_PAGINATION' : Boolean.TRUE]
            )
        }
        
        def bit = jasperService.generateReport(reportDef).toByteArray()
        def nom_fichier = "Liste_phases_" + utilisateur.username +"."+ extension
        //Filedownload.save(bit, "application/pdf", nom_fichier);

        genererRapport(nom_fichier,bit)
        
    }
       
    def genererRapportPhasePArTypeBienEtSuperficieProjet(String formatToExport){
        setParamExport(formatToExport)
        def ph = getPhase()
        def clientsListe = phaseService.getPhaseByUtlisateurEtTypeBienEtSuperficieEtProjet(utilisateur,ph,typeBienSelected,sup1,sup2,projetPhaseSelected)       
        
        String nomsociete = ChoraClientInfo.list().get(0).nomsociete
        String titrerapport = "Liste des phases [" + phaseSelected + "] Type de bien : [" +typeBienSelected+ "] Superficie [" + sup1 + "," + sup2 +"]" 
       
        def reportDef
        if (formatToExport == "PDF"){
            reportDef = new JasperReportDef(name:'rapport_des_Phases_param.jasper',
                fileFormat:exportFormat,
                reportData : clientsListe,
                parameters : ['nomsociete':nomsociete, 'titrerapport':titrerapport, 'champ1' : 'Projet' ,'valeur1': projetPhaseSelected.toString(), 'champ2': '', 'valeur2' :'']
            )
        } else if (formatToExport == "EXCEL"){
            reportDef = new JasperReportDef(name:'rapport_des_Phases_param.jasper',
                fileFormat:exportFormat,
                reportData : clientsListe,
                parameters : ['nomsociete':nomsociete, 'titrerapport':titrerapport, 'champ1' : 'Projet' ,'valeur1': projetPhaseSelected.toString(), 'champ2': '', 'valeur2' :'', 'IS_IGNORE_PAGINATION' : Boolean.TRUE]
            )
        }
        
        def bit = jasperService.generateReport(reportDef).toByteArray()
        def nom_fichier = "Liste_phases_" + utilisateur.username +"."+ extension
        //Filedownload.save(bit, "application/pdf", nom_fichier);

        genererRapport(nom_fichier,bit)
        
    }
    
    def genererRapportPhaseParProjet(String formatToExport){
        phaseService.updateTotalPaiements()
        setParamExport(formatToExport)
        def ph = null
        
        if ( phaseSelected != 'Toutes les phases'){
            ph = getPhase()
        }
        
        def clientsListe = phaseService.getPhaseByUtlisateurEtProjet(utilisateur,ph,projetPhase2Selected)
        String nomsociete = ChoraClientInfo.list().get(0).nomsociete
        String titrerapport = "Liste des phases [" + phaseSelected + "]"
        
        def statistiques = phaseService. getCountPhaseByProjet()
        
        String staNom1 = ""
        String staNom2 = ""
        String staNom3 = ""
        String staNom4 = ""
        String staNom5 = "Autre"
        
        Integer staValeur1 = 0 
        Integer staValeur2 = 0 
        Integer staValeur3 = 0 
        Integer staValeur4 = 0 
        Integer staValeur5 = 0 
        
        Integer i = 1 
        for (it in statistiques){
            switch (i){
                case "1":
                staNom1 = it[0]
                staValeur1 =  it[1]
                break
                case "2":
                staNom2 = it[0]
                staValeur2 =  it[1]
                break
                case "3":
                staNom3 = it[0]
                staValeur3 =  it[1]
                break
                case "4":
                staNom4 = it[0]
                staValeur4 =  it[1]
                break
                
            }
            i +=1
            staValeur5 +=  it[1]
        }
        staValeur5 = staValeur5 - (staValeur1 + staValeur2 +staValeur3 +staValeur4)
       
        def reportDef
        if (formatToExport == "PDF"){
            reportDef = new JasperReportDef(name:'rapport_des_Phases_param_PROJETC.jasper',
                fileFormat:exportFormat,
                reportData : clientsListe,
                parameters : ['nomsociete':nomsociete, 'titrerapport':titrerapport, 'champ1' : 'Projet' ,'valeur1': projetPhase2Selected.toString(), 'champ2': '', 'valeur2' :'','staNom1' : staNom1, 'staNom2' : staNom2,'staNom3' : staNom3, 'staNom4' : staNom4,'staNom5' : staNom5,'staValeur1' : staValeur1, 'staValeur2' : staValeur2,'staValeur3' : staValeur3, 'staValeur4' : staValeur4,'staValeur5' : staValeur5 ]
            )
        } else if (formatToExport == "EXCEL"){
            reportDef = new JasperReportDef(name:'rapport_des_Phases_param_PROJETC.jasper',
                fileFormat:exportFormat,
                reportData : clientsListe,
                parameters : ['nomsociete':nomsociete, 'titrerapport':titrerapport, 'champ1' : 'Projet' ,'valeur1': projetPhase2Selected.toString(), 'champ2': '', 'valeur2' :'','staNom1' : staNom1, 'staNom2' : staNom2,'staNom3' : staNom3, 'staNom4' : staNom4,'staNom5' : staNom5,'staValeur1' : staValeur1, 'staValeur2' : staValeur2,'staValeur3' : staValeur3, 'staValeur4' : staValeur4,'staValeur5' : staValeur5 , 'IS_IGNORE_PAGINATION' : Boolean.TRUE]
            )
        }
        
        def bit = jasperService.generateReport(reportDef).toByteArray()
        def nom_fichier = "Liste_phases_" + utilisateur.username +"."+ extension
        //Filedownload.save(bit, "application/pdf", nom_fichier);

        genererRapport(nom_fichier,bit)
        
    }
    
    def genererRapportPhaseParProjetEtVille(String formatToExport){
        setParamExport(formatToExport)
        def ph = null
        
        if ( phaseSelected != 'Toutes les phases'){
            ph = getPhase()
        }
        
        def clientsListe = phaseService.getPhaseByUtlisateurEtProjetEtVille(utilisateur,ph,projetPhase3Selected,villeSelected2)
        String nomsociete = ChoraClientInfo.list().get(0).nomsociete
        String titrerapport = "Liste des phases [" + phaseSelected + "]"
        
        def statistiques = phaseService. getCountPhaseByProjetEtVille(projetPhase3Selected.code)
        
        String staNom1 = ""
        String staNom2 = ""
        String staNom3 = ""
        String staNom4 = ""
        String staNom5 = "Autre"
        
        Integer staValeur1 = 0 
        Integer staValeur2 = 0 
        Integer staValeur3 = 0 
        Integer staValeur4 = 0 
        Integer staValeur5 = 0 
        
        Integer i = 1 
        for (it in statistiques){
            switch (i){
                case "1":
                staNom1 = it[0]
                staValeur1 =  it[1]
                break
                case "2":
                staNom2 = it[0]
                staValeur2 =  it[1]
                break
                case "3":
                staNom3 = it[0]
                staValeur3 =  it[1]
                break
                case "4":
                staNom4 = it[0]
                staValeur4 =  it[1]
                break
                
            }
            i +=1
            staValeur5 +=  it[1]
        }
        staValeur5 = staValeur5 - (staValeur1 + staValeur2 +staValeur3 +staValeur4)
       
        def reportDef
        if (formatToExport == "PDF"){
            reportDef = new JasperReportDef(name:'rapport_des_Phases_param_PROJETC.jasper',
                fileFormat:exportFormat,
                reportData : clientsListe,
                parameters : ['nomsociete':nomsociete, 'titrerapport':titrerapport, 'champ1' : 'Projet' ,'valeur1': projetPhase2Selected.toString(), 'champ2': 'Ville', 'valeur2' :villeSelected2.toString(),'staNom1' : staNom1, 'staNom2' : staNom2,'staNom3' : staNom3, 'staNom4' : staNom4,'staNom5' : staNom5,'staValeur1' : staValeur1, 'staValeur2' : staValeur2,'staValeur3' : staValeur3, 'staValeur4' : staValeur4,'staValeur5' : staValeur5 ]
            )
        } else if (formatToExport == "EXCEL"){
            reportDef = new JasperReportDef(name:'rapport_des_Phases_param_PROJETC.jasper',
                fileFormat:exportFormat,
                reportData : clientsListe,
                parameters : ['nomsociete':nomsociete, 'titrerapport':titrerapport, 'champ1' : 'Projet' ,'valeur1': projetPhase2Selected.toString(), 'champ2': 'Ville', 'valeur2' :villeSelected2.toString(),'staNom1' : staNom1, 'staNom2' : staNom2,'staNom3' : staNom3, 'staNom4' : staNom4,'staNom5' : staNom5,'staValeur1' : staValeur1, 'staValeur2' : staValeur2,'staValeur3' : staValeur3, 'staValeur4' : staValeur4,'staValeur5' : staValeur5 , 'IS_IGNORE_PAGINATION' : Boolean.TRUE]
            )
        }
        
        def bit = jasperService.generateReport(reportDef).toByteArray()
        def nom_fichier = "Liste_phases_" + utilisateur.username +"."+ extension
        //Filedownload.save(bit, "application/pdf", nom_fichier);

        genererRapport(nom_fichier,bit)
        
    }
    
    def genererRapportPhaseParNotaire(String formatToExport){
        setParamExport(formatToExport)
        def ph = null
        
        if ( phaseSelected != 'Toutes les phases'){
            ph = getPhase()
        }
        
        def clientsListe = phaseService.getPhaseByUtlisateurEtNotaire(utilisateur,ph,notaireSelected)
        String nomsociete = ChoraClientInfo.list().get(0).nomsociete
        String titrerapport = "Liste des phases [" + phaseSelected + "] Notaire [" + notaireSelected + "]"
        
        def statistiques = phaseService. getCountPhaseByNotaire()
        
        String staNom1 = ""
        String staNom2 = ""
        String staNom3 = ""
        String staNom4 = ""
        String staNom5 = "Autre"
        
        Integer staValeur1 = 0 
        Integer staValeur2 = 0 
        Integer staValeur3 = 0 
        Integer staValeur4 = 0 
        Integer staValeur5 = 0 
        
        Integer i = 1 
        for (it in statistiques){
            switch (i){
                case "1":
                staNom1 = it[0]
                staValeur1 =  it[1]
                break
                case "2":
                staNom2 = it[0]
                staValeur2 =  it[1]
                break
                case "3":
                staNom3 = it[0]
                staValeur3 =  it[1]
                break
                case "4":
                staNom4 = it[0]
                staValeur4 =  it[1]
                break
                
            }
            i +=1
            staValeur5 +=  it[1]
        }
        staValeur5 = staValeur5 - (staValeur1 + staValeur2 +staValeur3 +staValeur4)
        
        String champ2 = "Notaire"
        String valeur2 = notaireSelected.toString()
       
        def reportDef
        if (formatToExport == "PDF"){
            reportDef = new JasperReportDef(name:'rapport_des_Phases_param_PHASEC.jasper',
                fileFormat:exportFormat,
                reportData : clientsListe,
                parameters : ['nomsociete':nomsociete, 'titrerapport':titrerapport, 'champ1' : 'Phase' ,'valeur1': phaseSelected, 'champ2': champ2, 'valeur2' :valeur2,'staNom1' : staNom1, 'staNom2' : staNom2,'staNom3' : staNom3, 'staNom4' : staNom4,'staNom5' : staNom5,'staValeur1' : staValeur1, 'staValeur2' : staValeur2,'staValeur3' : staValeur3, 'staValeur4' : staValeur4,'staValeur5' : staValeur5 ]
            )
        } else if (formatToExport == "EXCEL"){
            reportDef = new JasperReportDef(name:'rapport_des_Phases_param_PHASEC.jasper',
                fileFormat:exportFormat,
                reportData : clientsListe,
                parameters : ['nomsociete':nomsociete, 'titrerapport':titrerapport, 'champ1' : 'Phase' ,'valeur1': phaseSelected, 'champ2': champ2, 'valeur2' :valeur2,'staNom1' : staNom1, 'staNom2' : staNom2,'staNom3' : staNom3, 'staNom4' : staNom4,'staNom5' : staNom5,'staValeur1' : staValeur1, 'staValeur2' : staValeur2,'staValeur3' : staValeur3, 'staValeur4' : staValeur4,'staValeur5' : staValeur5 , 'IS_IGNORE_PAGINATION' : Boolean.TRUE]
            )
        }
        
        def bit = jasperService.generateReport(reportDef).toByteArray()
        def nom_fichier = "Liste_phases_" + utilisateur.username +"."+ extension
        //Filedownload.save(bit, "application/pdf", nom_fichier);

        genererRapport(nom_fichier,bit)
        
    }
    
    def genererRapportPhaseParCommercial(String formatToExport){
        setParamExport(formatToExport)
        def ph = null
        
        if ( phaseSelected != 'Toutes les phases'){
            ph = getPhase()
        }
        
        def clientsListe = phaseService.getPhaseByUtlisateurEtCommercial(ph,commercialSelected)
        String nomsociete = ChoraClientInfo.list().get(0).nomsociete
        String titrerapport = "Liste des phases [" + phaseSelected + "] Commercial responsable [" +commercialSelected + "]"
        
        def statistiques = phaseService.getCountPhaseByCommercialResponsable()
        
        String staNom1 = ""
        String staNom2 = ""
        String staNom3 = ""
        String staNom4 = ""
        String staNom5 = "Autre"
        
        Integer staValeur1 = 0 
        Integer staValeur2 = 0 
        Integer staValeur3 = 0 
        Integer staValeur4 = 0 
        Integer staValeur5 = 0 
        
        Integer i = 1 
        for (it in statistiques){
            switch (i){
                case "1":
                staNom1 = it[0]
                staValeur1 =  it[1]
                break
                case "2":
                staNom2 = it[0]
                staValeur2 =  it[1]
                break
                case "3":
                staNom3 = it[0]
                staValeur3 =  it[1]
                break
                case "4":
                staNom4 = it[0]
                staValeur4 =  it[1]
                break
                
            }
            i +=1
            staValeur5 +=  it[1]
        }
        staValeur5 = staValeur5 - (staValeur1 + staValeur2 +staValeur3 +staValeur4)
       
        String champ2 = "Commercial responsable"
        String valeur2 = commercialSelected.toString()
        
        def reportDef
        if (formatToExport == "PDF"){
            reportDef = new JasperReportDef(name:'rapport_des_Phases_param_PHASEC.jasper',
                fileFormat:exportFormat,
                reportData : clientsListe,
                parameters : ['nomsociete':nomsociete, 'titrerapport':titrerapport, 'champ1' : 'Phase' ,'valeur1': '', 'champ2': champ2, 'valeur2' :valeur2,'staNom1' : staNom1, 'staNom2' : staNom2,'staNom3' : staNom3, 'staNom4' : staNom4,'staNom5' : staNom5,'staValeur1' : staValeur1, 'staValeur2' : staValeur2,'staValeur3' : staValeur3, 'staValeur4' : staValeur4,'staValeur5' : staValeur5 ]
            )
        } else if (formatToExport == "EXCEL"){
            reportDef = new JasperReportDef(name:'rapport_des_Phases_param_PHASEC.jasper',
                fileFormat:exportFormat,
                reportData : clientsListe,
                parameters : ['nomsociete':nomsociete, 'titrerapport':titrerapport, 'champ1' : 'Phase' ,'valeur1': '', 'champ2': champ2, 'valeur2' :valeur2,'staNom1' : staNom1, 'staNom2' : staNom2,'staNom3' : staNom3, 'staNom4' : staNom4,'staNom5' : staNom5,'staValeur1' : staValeur1, 'staValeur2' : staValeur2,'staValeur3' : staValeur3, 'staValeur4' : staValeur4,'staValeur5' : staValeur5 , 'IS_IGNORE_PAGINATION' : Boolean.TRUE]
            )
        }        

        def bit = jasperService.generateReport(reportDef).toByteArray()
        def nom_fichier = "Liste_phases_" + utilisateur.username +"."+ extension
        //Filedownload.save(bit, "application/pdf", nom_fichier);

        genererRapport(nom_fichier,bit)
        
    }
    
    def genererRapportpaiement(String formatToExport){
        setParamExport(formatToExport)
        def ph = null
        def typePm = null 
        def paiementListe
        
        if ( phaseSelected != 'Toutes les phases'){
            ph = getPhase()
        }
        
        
        if (typePaiementSelected != mpTout){
            typePm= typePaiementSelected            
        }
        
        paiementListe = paiementService.getPaiementByUtlisateurEtProjetEtType(utilisateur,projetPaiementSelected,typePm,ph) 
        
        String nomsociete = ChoraClientInfo.list().get(0).nomsociete
        String titrerapport = "Liste des paiements"
        
        def statistiques 

        statistiques = paiementService.getCountPaiementByProjet()        
        
        String staNom1 = ""
        String staNom2 = ""
        String staNom3 = ""
        String staNom4 = ""
        String staNom5 = "Autre"
        
        Integer staValeur1 = 0 
        Integer staValeur2 = 0 
        Integer staValeur3 = 0 
        Integer staValeur4 = 0 
        Integer staValeur5 = 0 
        
        Integer i = 1 
        for (it in statistiques){
            switch (i){
                case "1":
                staNom1 = it[0]
                staValeur1 =  it[1]
                break
                case "2":
                staNom2 = it[0]
                staValeur2 =  it[1]
                break
                case "3":
                staNom3 = it[0]
                staValeur3 =  it[1]
                break
                case "4":
                staNom4 = it[0]
                staValeur4 =  it[1]
                break
                
            }
            i +=1
            staValeur5 +=  it[1]
        }
        staValeur5 = staValeur5 - (staValeur1 + staValeur2 +staValeur3 +staValeur4)
       
        def reportDef
        if (formatToExport == "PDF"){
            reportDef = new JasperReportDef(name:'rapport_des_VersementsC.jasper',
                fileFormat:exportFormat,
                reportData : paiementListe,
                parameters : ['nomsociete':nomsociete, 'titrerapport':titrerapport, 'champ1' : 'Projet' ,'valeur1': projetPaiementSelected.code + "-" + projetPaiementSelected.designation, 'champ2': 'Type de paiement ', 'valeur2' :typePaiementSelected.type,'staNom1' : staNom1, 'staNom2' : staNom2,'staNom3' : staNom3, 'staNom4' : staNom4,'staNom5' : staNom5,'staValeur1' : staValeur1, 'staValeur2' : staValeur2,'staValeur3' : staValeur3, 'staValeur4' : staValeur4,'staValeur5' : staValeur5 ]
            )
        } else if (formatToExport == "EXCEL"){
            reportDef = new JasperReportDef(name:'rapport_des_VersementsC.jasper',
                fileFormat:exportFormat,
                reportData : paiementListe,
                parameters : ['nomsociete':nomsociete, 'titrerapport':titrerapport, 'champ1' : 'Projet' ,'valeur1': projetPaiementSelected.code + "-" + projetPaiementSelected.designation, 'champ2': 'Type de paiement ', 'valeur2' :typePaiementSelected.type,'staNom1' : staNom1, 'staNom2' : staNom2,'staNom3' : staNom3, 'staNom4' : staNom4,'staNom5' : staNom5,'staValeur1' : staValeur1, 'staValeur2' : staValeur2,'staValeur3' : staValeur3, 'staValeur4' : staValeur4,'staValeur5' : staValeur5 , 'IS_IGNORE_PAGINATION' : Boolean.TRUE]
            )
        }
        
        def bit = jasperService.generateReport(reportDef).toByteArray()
        def nom_fichier = "Liste_paiements_"+projetPaiementSelected + "_" + typePaiementSelected + "_" + utilisateur.username +"."+ extension
        //Filedownload.save(bit, "application/pdf", nom_fichier);

        genererRapport(nom_fichier,bit)
        
    }    
    
    def getPhase(){
        String  ph
        switch (phaseSelected){
            case "Option":
            ph = "OPTION"
            
            break
            case "Réservation":
            ph = "RESERVATION"
            
            break
            case "Compromis":
            ph = "COMPROMIS"  
            
            break
            
            case "Contrat":
            ph = "CONTRAT"
            
            break 
            case "Toutes les phases" : 
            ph = null
        }
        
        return ph
    }
       
    def alerteMail(){
        def utilisateurs = Utilisateur.list()
        for (u in utilisateurs){
            alerteService.alerteMail(u)
        }
    }
    
    def genererArchitectureBiens(){
        //this.getFellow("gridArchitectureBiens").visible = true
        def listModeleType = modeleTypeService.getTypeByModele(projetBienSelected.modele)       
       
        Tabs TabsParent = this.getFellow("tabBien")
        for (Iterator it = TabsParent.getChildren().iterator(); it.hasNext();) {
            it.next();
            it.remove();
        }

        Tabpanels TabpanelsParent = this.getFellow("tabpanelsBien")
        for (Iterator it = TabpanelsParent.getChildren().iterator(); it.hasNext();) {
            it.next();           
            it.remove();
        }

        
        if (listModeleType != null && listModeleType.size() > 0){
            def compt = 0
            MiniBienManager bmParent;
            listModeleType.each
            { tm ->
                Tab tb = new Tab()
                tb.setLabel(tm.type.intitule)
                
                TabsParent.appendChild(tb)
                
                Tabpanel tp = new Tabpanel()
                if(compt == 0) {
                    
                    MiniBienManager bm = new MiniBienManager(projetBienSelected, tm.type, null, bienService, statutService)
                    bm.setParent(tp);
                    bmParent = bm;
                }
                else if (compt == listModeleType.size()-1 ){
                    BienManager bm = new BienManager(projetBienSelected, tm.type, listModeleType[compt-1].type, contenanceService, bienService, statutService, jasperService)
                    //BienManager bm = new BienManager(projetBienSelected, tm.type, null, contenanceService, bienService, statutService)
                    bm.setParent(tp);
                    bmParent.bBmFils = bm;
                    //bmParent = bm;
                }
                
                else {
                    MiniBienManager bm = new MiniBienManager(projetBienSelected, tm.type, listModeleType[compt-1].type, bienService, statutService)
                    bm.setParent(tp);
                    bmParent.bmFils = bm;
                    bmParent = bm;
                }
                TabpanelsParent.appendChild(tp)
                compt++
            }
        }
    }
}