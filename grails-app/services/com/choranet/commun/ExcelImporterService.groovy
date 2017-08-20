package com.choranet.commun

import org.apache.commons.logging.Log 
import org.apache.commons.logging.LogFactory

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.io.ByteArrayOutputStream
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.*;
import com.choranet.omnidior.gesticom.Notaire

import java.text.SimpleDateFormat;

import com.choranet.omnidior.gesticom.*


class ExcelImporterService {

    private Log logger = LogFactory.getLog(ExcelImporterService.class)    
    
    static transactional = true   
    
    static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-hh-mm");
    
    static String fs = System.getProperty('file.separator');
    static String ls = System.getProperty('line.separator');
    
    def villeService
    def nationaliteService
    def statutService
    def clientService
    def projetService
    def bienService
    def phaseService
    def typeService
    def utilisateurService
    def notaireService
    def paiementService
    
    def creerFichierLogImport(String nomFichier) {
        File filelog = new File(System.getProperty('user.home')+fs+nomFichier+dateFormat.format(new Date()))
        filelog.createNewFile();
        loggerInformations(filelog, System.getProperty('os.name'))
        loggerInformations(filelog, System.getProperty('os.arch'))
        loggerInformations(filelog, System.getProperty('user.dir'))
        loggerInformations(filelog, System.getProperty('user.home'))
        loggerInformations(filelog, System.getProperty('user.language'))
        loggerInformations(filelog, System.getProperty('java.version'))
        loggerInformations(filelog, System.getProperty('java.vendor'))
        return filelog;
    }
        
    def loggerInformations(File filelog, Object info) {
        filelog << info
        filelog << ls
    }
    
    def chargerDonneesLigne(Row row, types) {
        int taille = types.size()
        Map map = [:]        
        def cles = types.keySet().toArray();
        for(int i = 0 ; i < taille ; i++ )  {
            Cell cell = row.getCell(i)
            def cle = cles[i]
            if(cell != null) {
                if(types[cle].equals("String")) {
                    switch(cell.getCellType()) {
                        case Cell.CELL_TYPE_STRING : 
                        map[cle] = cell.getStringCellValue(); 
                        break;
                        case Cell.CELL_TYPE_NUMERIC : 
                        map[cle] = String.valueOf(cell.getNumericCellValue());
                        break;
                        case Cell.CELL_TYPE_BOOLEAN : 
                        map[cle] = String.valueOf(cell.getBooleanCellValue());
                        break;
                    }
                    if(map[cle] != null) {
                        map[cle] = map[cle].trim()
                        if(map[cle].equals("")) {
                            map[cle] = null
                        }
                    }
                } else if (types[cle].equals("Numeric")) {
                    map[cle] = cell.getNumericCellValue();
                } else if (types[cle].equals("Date")) {
                    map[cle] = cell.getDateCellValue();
                } else if (types[cle].equals("Boolean")) {
                    map[cle] = cell.getBooleanCellValue();
                } else {
                    map[cle] = null
                }
            } else {
                map[cle] = null
            }
        }
        return map
    }
    
    def chargerDonneesParDefaut(Map objet, Map valeursParDefaut) {
        valeursParDefaut.entrySet().each {
            if(objet[it.key] == null) {
                objet[it.key] = it.value
            }
        }
    } 
    
    def validerChampsRequis(Map objet, champsrequis) {
        for(champs in champsrequis) {
            if(objet[champs] == null) {
                return champs
            }
        }
        return null
    }
    
    def importerClients(Object media, Utilisateur utilisateur){
        File fileLog = creerFichierLogImport("log_import_clients")
        try {
            Workbook bk = new HSSFWorkbook(media.getStreamData())
            Sheet sheet = bk.getSheetAt(0)
            def first = true
            def compteur = 1;
            def nbImport = 0;
            def nbEchec = 0;
            
            def types = ["cin" : "String"
                , "nom"  : "String"
                , "prenom" : "String"
                , "sexe" : "String"
                , "adresse": "String"
                , "dateNaissance" : "Date"                 
                , "dateContact" : "Date"                                
                , "gsm" : "String"
                , "fixe" : "String"                
                , "mail" : "String"
                , "commentaire" : "String"
                , "nationalite" : "String"
                , "ville" : 'String'
                , "statut" : "String"];
            
            def defaultNationalite = nationaliteService.getDefault() 
            def defaultVille = villeService.getDefault()
            def defaultStatut = statutService.getDefaultStatutClient()
                        
            def valeursParDefaut = ["nationalite" : defaultNationalite, "ville" : defaultVille,
            "statut" : defaultStatut]
            
            def valeursObligatoire = ["nom", "prenom", "sexe", "gsm"]
            //def valeursObligatoire = []
            
            loggerInformations(fileLog, "Début import des clients : " + new Date())
            for (Row row : sheet) {
                try {
                    if(first) {
                        first = false
                        compteur++;
                        continue
                    }
                    
                    def map = chargerDonneesLigne(row, types);
                                        
                    if(map.nationalite != null && !map.nationalite.equals("")) {
                        map.nationalite = nationaliteService.getNationaliteByIntitule(map.nationalite)
                    }
                   
                    if(map.ville != null && !map.ville.equals("")) {
                        map.ville = Ville.findByIntitule(map.ville.toUpperCase())
                    }
                    
                    if(map.statut != null && !map.statut.equals("")) {
                        def codeStatut 
                                                
                        if (map.statut == 'CLIENT'){
                            codeStatut = '24'
                            map.statut = statutService.getByCode(codeStatut)
                        }
                        else if(map.statut == 'PROSPECT'){
                            codeStatut = '23'
                            map.statut = statutService.getByCode(codeStatut)
                        }
                        
                    }                                        
                    
                    chargerDonneesParDefaut(map, valeursParDefaut)
                    
                    def champ = validerChampsRequis(map, valeursObligatoire)
                    if(champ != null) {
                        loggerInformations(fileLog, "Echec import ligne " + compteur + " le champ : " + champ + " est requis")
                        nbEchec++
                    } else {
                        def oldClient = clientService.getClientByCIN(map.cin);
                        if(oldClient != null ) {
                            loggerInformations(fileLog, "Echec import ligne " + compteur + " le client : " + map.cin + " existe déjà")
                            nbEchec++
                        } else {
                            def newClient = new Client(adresse : map.adresse, cin : map.cin, commentaire : map.commentaire, dateContact : map.dateContact
                                ,dateModification : new Date(), dateNaissance : map.dateNaissance, fixe : map.fixe, gsm : map.gsm
                                ,mail : map.mail, nom : map.nom, prenom : map.prenom, sexe : map.sexe
                                ,nationalite : map.nationalite, ville : map.ville, statut : map.statut);
                             
                            
                            newClient = clientService.save(newClient)                           
                            utilisateur = utilisateur.addToClients(newClient)
                            nbImport++
                        }
                    }
            
                    compteur++;
                } catch(Exception ex) {
                    loggerInformations(fileLog, "Echec import ligne " + compteur + " " + ex)
                    nbEchec++
                    compteur++;
                }
                
            }
            loggerInformations(fileLog, "Fin import client : " + new Date())
            loggerInformations(fileLog, "Nombre de lignes importées : " + nbImport)
            loggerInformations(fileLog, "Nombre de lignes échouées : " + nbEchec)
            utilisateurService.update(utilisateur)
            
        } catch(Exception ex) {
            loggerInformations(fileLog, "Fichier invalide : " + ls + ex)
        }
        return "Import terminé voir le fichier de log : " + fileLog.getAbsolutePath();
    }
   
    def importerBiens(Object media){
        File fileLog = creerFichierLogImport("log_import_biens")
        try {
            Workbook bk = new HSSFWorkbook(media.getStreamData())
            Sheet sheet = bk.getSheetAt(0)
            def first = true
            def compteur = 1;
            def nbImport = 0;
            def nbEchec = 0;
            
            def types = [
                "projet" : "String"
                , "numero" : "String"
                , "prixVente" : "Numeric"
                , "superficieConstruite" : "Numeric"
                , "superficieVendable": "Numeric"                
                , "superficie" : "Numeric"
                , "titreFoncier" : "String"
                , "contenance" : "String"                
                , "type" : "String"
                , "statut" : "String"
                , "bienParent" : "String"                            
                , "coordonnees" : "String"
                , "typeCoordonnees" : "String"
                , "client" : "String"
            ];
            
            def defaultStatut = statutService.getDefaultStatutBien();
            
            def valeursParDefaut = ["statut" : defaultStatut]
            
            def valeursObligatoire = ["numero", "projet", "type","statut"]
            
            loggerInformations(fileLog, "Début import des biens : " + new Date())
            for (Row row : sheet) {
                try {
                    if(first) {
                        first = false
                        compteur++;
                        continue
                    }
                    
                                        
                    def map = chargerDonneesLigne(row, types);
                    
                    def numeroBien = map.numero
                    def codeProjet = map.projet   
                    
                    if(map.bienParent != null && !map.bienParent.equals("")) {
                        map.bienParent = bienService.getBienByCodeProjetAndNum(map.projet,map.bienParent)
                    } 
                    
                    if (map.bienParent == null){
                        loggerInformations(fileLog, "* Attention - Le bien : " + numeroBien + " n'est pas lié à un bien parent")  
                    }
                    
                    if(map.projet != null && !map.projet.equals("")) {
                        map.projet = projetService.getProjetByCode(map.projet)
                    }
                    
                    
                    if(map.type != null && !map.type.equals("")) {
                        map.type = typeService.getTypeByIntituleEtType(map.type,"BIEN")
                    }                    
                    
                    if(map.statut != null && !map.statut.equals("")) {
                        map.statut = statutService.getByTypeEtIntitule("BIEN",map.statut)
                    }                                        
                    
                    chargerDonneesParDefaut(map, valeursParDefaut)
                    
                    def champ = validerChampsRequis(map, valeursObligatoire)
                    if(champ != null) {
                        loggerInformations(fileLog, "Echec import ligne " + compteur + " le champ : " + champ + " est requis")
                        nbEchec++
                    } else {
                        def oldBien = bienService.getBienByCodeProjetAndNum(codeProjet,numeroBien);
                        if(oldBien != null ) {
                            loggerInformations(fileLog, "Echec import ligne " + compteur + " le bien : " + numeroBien + " existe déjà dans le projet : [" + codeProjet + "]")
                            nbEchec++
                        } else {
                            def newBien = new Bien(numero : map.numero, prixVente : map.prixVente, superficieConstruite : map.superficieConstruite, superficieVendable : map.superficieVendable, titreFoncier : map.titreFoncier, superficie : map.superficie, contenance : map.contenance, coordonnees : map.coordonnees, typeCoordonnees : map.typeCoordonnees 
                                , projet : map.projet, type : map.type, bienParent : map.bienParent, statut : map.statut);
                                    
                            bienService.save(newBien)
                            nbImport++
                        }
                    }
            
                    compteur++;
                } catch(Exception ex) {
                    loggerInformations(fileLog, "Echec import ligne " + compteur + " " + ex)
                    nbEchec++
                    compteur++;
                }
                
            }
            loggerInformations(fileLog, "Fin import bien : " + new Date())
            loggerInformations(fileLog, "Nombre de lignes importées : " + nbImport)
            loggerInformations(fileLog, "Nombre de lignes échouées : " + nbEchec)
        
            
        } catch(Exception ex) {
            loggerInformations(fileLog, "Fichier invalide : " + ls + ex)
        }
        return "Import terminé voir le fichier de log : " + fileLog.getAbsolutePath();
    }
    
    def importerPhases(Object media) {
       
        File fileLog = creerFichierLogImport("log_import_phases")
        try {
            Workbook bk = new HSSFWorkbook(media.getStreamData())
            Sheet sheet = bk.getSheetAt(0)
            def first = true
            def compteur = 1;
            def nbImport = 0;
            def nbEchec = 0;
            def types = ["numPhase" : "String"
                ,"projet" : "String"
                ,"client" : "String"
                ,"bien" : "String"
                ,"commercialResponsable" : "String"
                ,"dateDebut" : "Date"
                ,"dateFin" : "Date"
                ,"montantVente" : "Numeric"
                ,"intitule" : "String"
                ,"dateContact" : "Date"
                ,"sourceInformation" : "String"
                ,"canal" : "String"
                ,"notaire" : "String"
                ,"statut" : "String"
                ,"commentaire" : "String"
            ]                       
            
            def defaultStatut = statutService.getDefaultStatutPhase();
            
            def valeursParDefaut = []
            def valeursObligatoire = ["numPhase","projet", "client","intitule","commercialResponsable","statut"]
            
            loggerInformations(fileLog, "Début import phases : " + new Date())
            for (Row row : sheet) {
                try {
                    if(first) {
                        first = false
                        compteur++;
                        continue
                    }
                    
                    def map = chargerDonneesLigne(row, types);
                 
                        String codeProjet = map.projet
                    
                        if(map.projet != null && !map.projet.equals("")) {
                            map.projet = projetService.getProjetByCode(map.projet)
                        }
                                       
                        if(map.client != null && !map.client.equals("")) {
                            map.client = Client.findByCin(map.client)
                        }
                        else{
                            loggerInformations(fileLog, "** Attention - Echec import ligne " + compteur + " la phase : [" + map.numPhase + "] n'est pas liée à un client")
                        }
                    
                        if(map.bien != null && !map.bien.equals("")) {
                            map.bien = bienService.getBienByCodeProjetAndNum(codeProjet,map.bien);
                        }
                        else{
                            loggerInformations(fileLog, "* Attention - la phase : [" + map.numPhase + "] n'est pas liée à un bien")
                        }
                    
                        if(map.commercialResponsable != null && !map.commercialResponsable.equals("")) {
                            map.commercialResponsable = Utilisateur.findByUsername(map.commercialResponsable)
                        }
                    
                        if(map.notaire != null && !map.notaire.equals("")) {
                            Notaire tempNotaire
                            tempNotaire = Notaire.findByPatente(map.notaire)
                            if (!tempNotaire){
                                loggerInformations(fileLog, "* Attention - la phase : [" + map.numPhase + "] n'est pas liée à un notaire, impossible de trouver le notaire [" + map.notaire + "]")  
                            }
                            map.notaire = tempNotaire
                        }else
                        {                    
                            loggerInformations(fileLog, "* Attention - la phase : [" + map.numPhase + "] n'est pas liée à un notaire")
                        }
                    


                    
                        if(map.sourceInformation != null && !map.sourceInformation.equals("")) {
                            map.sourceInformation = SourceInformation.findByIntitule(map.sourceInformation)
                        }
                    
                        if(map.canal != null && !map.canal.equals("")) {
                            map.canal = Canal.findByIntitule(map.canal)
                        }
                    
                        String codePhaseStatut 
                        if (map.statut != null && !map.statut.equals("")){
                            switch (map.statut){
                                case "EN COURS":
                                codePhaseStatut = "07"            
                                break                            
            
                                case "SUSPENDUE":
                                codePhaseStatut = "08"   
                                break
                        
                                case "CLOTUREE" :
                                codePhaseStatut =  "09"
                                break
                        
                                case "ANNULEE":
                                codePhaseStatut = "10"            
                                break
                        
                                case "EXPIREE":
                                codePhaseStatut = "27"            
                                break
                        
                            }
                        }
                    
                        if(map.statut != null && !map.statut.equals("")) {
                            map.statut = statutService.getByCode(codePhaseStatut)
                        }
                        
                        //chargerDonneesParDefaut(map, valeursParDefaut)
                        def champ = validerChampsRequis(map, valeursObligatoire)
                    
                        if(champ != null) {
                            loggerInformations(fileLog, "Echec import ligne " + compteur + " le champ : " + champ + " est requis")
                            nbEchec++
                        } else {
                        

                        
                            def oldPh = Phase.findByNumPhase(map.numPhase)
                            if(oldPh != null || oldPh != null) {
                                loggerInformations(fileLog, "Echec import ligne " + compteur + " la phase : " + map.numPhase + " exsite déjà")
                                nbEchec++
                            } else {
                            
                                Boolean estAvecAffectation = false
                                if (map.bien){
                                    estAvecAffectation = true
                                }
                                else{
                                    estAvecAffectation = false
                                }
                            
                                Boolean expire = false
                                if (map.dateFin){
                                    if (new Date() - (Date)map.dateFin < 0){
                                        expire = true 
                                    }
                                }
                        
                                def newPhase = new Phase(
                                    numPhase : map.numPhase
                                    , commentaire : map.commentaire
                                    , dateDebut : map.dateDebut
                                    , dateFin : map.dateFin
                                    , estExpire : expire
                                    , estAvecAffectation : estAvecAffectation
                                    , montantVente : map.montantVente
                                    , intitule : map.intitule
                                    , totalPaiements : 0
                                    , montantReliquat : 0
                                    , dateContact : map.dateContact
                                    , idPhaseParent : null
                                    , estHistorique : false
                                    , bien : map.bien
                                    , commercialResponsable : map.commercialResponsable
                                    , notaire : map.notaire
                                    , projet : map.projet
                                    , client : map.client
                                    , sourceInformation : map.sourceInformation
                                    , canal : map.canal
                                    , statut : map.statut);//.save(flush:true)
                                phaseService.save(newPhase)
                                nbImport++     
                            }                        
                        }
                        compteur++;
                } catch(Exception ex) {
                    loggerInformations(fileLog, "Echec import ligne " + compteur + " " + ex)
                    nbEchec++
                    compteur++;
                }
                
            }
            phaseService.updateBienClient()
            loggerInformations(fileLog, "Fin import phases : " + new Date())
            loggerInformations(fileLog, "Nombre de lignes importées : " + nbImport)
            loggerInformations(fileLog, "Nombre de lignes échouées : " + nbEchec)
        
            
        } catch(Exception ex) {
            loggerInformations(fileLog, "Fichier invalide : " + ls + ex)
            logger.error(ex)
        }
        return "Import terminé voir le fichier de log : " + fileLog.getAbsolutePath();
    }
   
    def importerPaiements(Object media) {
       
        File fileLog = creerFichierLogImport("log_import_paiements")
        def typePaiement = Statut.findByCode("19")
        try {
            Workbook bk = new HSSFWorkbook(media.getStreamData())
            Sheet sheet = bk.getSheetAt(0)
            def first = true
            def compteur = 1;
            def nbImport = 0;
            def nbEchec = 0;
            def types = [ "phase" : "String"
                , "numeroRecu" : "String"
                , "banque" : "String"
                , "datePaiement" : "Date"
                ,"dateEncaissement" : "Date"
                ,"modePaiement" : "String"
                ,"montant" : "Numeric"
                ,"statut" : "String"
                ,"commentaire" : "String"
                , "datePaiement2" : "Date"
                ,"dateEncaissement2" : "Date"
                ,"modePaiement2" : "String"
                ,"montant2" : "Numeric"
                ,"statut2" : "String"
                ,"commentaire2" : "String"
                , "datePaiement3" : "Date"
                ,"dateEncaissement3" : "Date"
                ,"modePaiement3" : "String"
                ,"montant3" : "Numeric"
                ,"statut3" : "String"
                ,"commentaire3" : "String"
            ]
            
            def defaultStatut = statutService.getDefaultStatutPaiement();
            
            //            def valeursParDefaut = ["statut", defaultStatut]
            def valeursObligatoire = ["montant", "datePaiement","phase","modePaiement","statut"]
            
            loggerInformations(fileLog, "Début import paiements : " + new Date())
            for (Row row : sheet) {
                try {
                    if(first) {
                        first = false
                        compteur++;
                        continue
                    }
                    
                    def map = chargerDonneesLigne(row, types);
                    String intitulePhase
                    String intituleProjet
                    String intituleClient
                    String intituleBien
                    
                    if(map.phase != null && !map.phase.equals("")) {
                        String numPhase = map.phase
                        
                        map.phase = Phase.findByNumPhase(map.phase)
                        
                        if (map.phase == null && !map.phase.equals("") ){
                            loggerInformations(fileLog, "Echec import ligne " + compteur + " le code phase [" + numPhase + "] n'existe pas")
                        }
                        else{
                            intitulePhase = map.phase.intitule
                            intituleProjet = map.phase.projet.toString()
                            intituleClient = map.phase.client.toString()
                            intituleBien = map.phase.bien.toString()
                        }
                    }
                    
                    if(map.modePaiement != null && !map.modePaiement.equals("")) {
                        map.modePaiement = ModePaiement.findByType(map.modePaiement)
                    }                    
                    
                    if(map.statut != null && !map.statut.equals("")) {
                        map.statut = statutService.getByTypeEtIntitule("PAIEMENT",map.statut)
                    }
                    
                    
                    
                    def champ = validerChampsRequis(map, valeursObligatoire)
                    
                    if(champ != null) {
                        loggerInformations(fileLog, "Echec import ligne " + compteur + " le champ : " + champ + " est requis")
                        nbEchec++
                    } else {

                        //                        if(oldPar2 != null || oldPar1 != null) {
                        //                            loggerInformations(fileLog, "Echec import ligne " + compteur + " le paiement : " + map.numPhase + " exsite déjà")
                        //                            nbEchec++
                        //                        } else {

                        def newPaiement = new Paiement("phase" : map.phase
                            , "numeroRecu" : map.numeroRecu
                            , "banque" : map.banque
                            , "datePaiement" : map.datePaiement
                            ,"dateEncaissement" : map.dateEncaissement
                            ,"modePaiement" : map.modePaiement
                            ,"type" : typePaiement
                            ,"montant" : map.montant
                            ,"statut" : map.statut
                            ,"commentaire" : map.commentaire
                            ,"numeroBordereau" : null
                            ,"numeroPaiement" : null
                            ,"intitulePhasePaiement" : intitulePhase
                            ,"projet" : intituleProjet
                            ,"client" : intituleClient
                            ,"bien" : intituleBien
                        )
                        paiementService.save(newPaiement) 
                        
                        if (map.datePaiement2 != null && !map.datePaiement2.equals("") && map.modePaiement2 != null && !map.modePaiement2.equals("") && map.montant2 != null && !map.montant2.equals("") && map.statut2 != null && !map.statut2.equals(""))
                        {
                           
                            map.statut2 = statutService.getByTypeEtIntitule("PAIEMENT",map.statut2)
                            map.modePaiement2 = ModePaiement.findByType(map.modePaiement2)
                            //ajout versement 2      
                            def newPaiement2 = new Paiement("phase" : map.phase
                                , "numeroRecu" : map.numeroRecu
                                , "banque" : map.banque
                                , "datePaiement" : map.datePaiement2
                                ,"dateEncaissement" : map.dateEncaissement2
                                ,"modePaiement" : map.modePaiement2
                                ,"type" : typePaiement
                                ,"montant" : map.montant2
                                ,"statut" : map.statut2
                                ,"commentaire" : map.commentaire2
                                ,"numeroBordereau" : null
                                ,"numeroPaiement" : null
                                ,"intitulePhasePaiement" : intitulePhase
                                ,"projet" : intituleProjet
                                ,"client" : intituleClient
                                ,"bien": intituleBien
                            )

                            paiementService.save(newPaiement2)
                        }
                        //ajout versement 3 
                        if (map.datePaiement3 != null && !map.datePaiement3.equals("") && map.modePaiement3 != null && !map.modePaiement3.equals("") && map.montant3 != null && !map.montant3.equals("") && map.statut3 != null && !map.statut3.equals(""))
                        {
                            map.statut3 = statutService.getByTypeEtIntitule("PAIEMENT",map.statut3)
                            map.modePaiement3 = ModePaiement.findByType(map.modePaiement3)
                             
                            def newPaiement3 = new Paiement("phase" : map.phase
                                , "numeroRecu" : map.numeroRecu
                                , "banque" : map.banque
                                , "datePaiement" : map.datePaiement3
                                ,"dateEncaissement" : map.dateEncaissement3
                                ,"modePaiement" : map.modePaiement3
                                ,"type" : typePaiement
                                ,"montant" : map.montant3
                                ,"statut" : map.statut3
                                ,"commentaire" : map.commentaire3
                                ,"numeroBordereau" : null
                                ,"numeroPaiement" : null
                                ,"intitulePhasePaiement" : intitulePhase
                                ,"projet" : intituleProjet
                                ,"client" : intituleClient
                                ,"bien": intituleBien
                            )
                            
                            paiementService.save(newPaiement3)
                        }  
                        //                            partenaireService.save(newPartenaire)
                        nbImport++      
                        //                        }                        
                    }
                    compteur++;
                } catch(Exception ex) {
                    loggerInformations(fileLog, "Echec import ligne " + compteur + " " + ex)
                    nbEchec++
                    compteur++;
                }
                
            }
            loggerInformations(fileLog, "Fin import paiements : " + new Date())
            loggerInformations(fileLog, "Nombre de lignes importées : " + nbImport)
            loggerInformations(fileLog, "Nombre de lignes échouées : " + nbEchec)
        
            
        } catch(Exception ex) {
            loggerInformations(fileLog, "Fichier invalide : " + ls + ex)
            logger.error(ex)
        }
        return "Import terminé voir le fichier de log : " + fileLog.getAbsolutePath();
    }        
    
    def importerProjets(Object media, Utilisateur utilisateur) {
        File fileLog = creerFichierLogImport("log_import_projets")
        try {
            Workbook bk = new HSSFWorkbook(media.getStreamData())
            Sheet sheet = bk.getSheetAt(0)
            def first = true
            def compteur = 1;
            def nbImport = 0;
            def nbEchec = 0;
            
            def types = ["designation" : "String"
                , "code" : "String"
                , "adresse" : "String"
                , "delaiOption" : "Numeric"
                , "delaiReservation" : "Numeric"
                , "delaiCompromis" : "Numeric"
                , "delaiContrat" : "Numeric"
                , "reservationEnLigne" : "String"
                , "generationOptionEnLigne" : "String"
                , "type" : "String" 
                , "ville" : "String"
                , "modele" : "String"
                , "statut": "String"
                , "groupeClient" : "String" ]
        
            // def valeursParDefaut = []
            
            def valeursObligatoire = ["code", "designation","delaiOption","delaiReservation","delaiCompromis","delaiContrat", "type", "ville", "statut","groupeClient"]
            
            loggerInformations(fileLog, "Début import des projets : " + new Date())
            for (Row row : sheet) {
                try {
                    if(first) {
                        first = false
                        compteur++;
                        continue
                    }
                    
                    def map = chargerDonneesLigne(row, types);
                    
                    if(map.type != null && !map.type.equals("")) {
                        map.type = typeService.getTypeByIntituleEtType(map.type,"PROJET")
                    }
                    
                    if(map.ville != null && !map.ville.equals("")) {
                        map.ville = Ville.findByIntitule(map.ville.toUpperCase())
                    }
                    
                    if(map.modele != null && !map.modele.equals("")) {
                        map.modele = Modele.findByCode(map.modele)
                    }
                    
                    if(map.statut != null && !map.statut.equals("")) {
                        map.statut = statutService.getByTypeEtIntitule("PROJET",map.statut)
                    }
                    
                    if(map.groupeClient != null && !map.groupeClient.equals("")) {
                        map.groupeClient = ChoraClientInfo.findByNomsociete(map.groupeClient)
                    }                                        
                    
                    //chargerDonneesParDefaut(map, valeursParDefaut)
                    
                    def champ = validerChampsRequis(map, valeursObligatoire)
                    
                    if(champ != null) {
                        loggerInformations(fileLog, "Echec import ligne " + compteur + " le champ : " + champ + " est requis")
                        nbEchec++
                    } else {
                        def oldProjet = Projet.findByCode(map.code);
                        
                        if(oldProjet != null ) {
                            loggerInformations(fileLog, "Echec import ligne " + compteur + " le projet : [" + map.code + "] existe déjà")
                            nbEchec++
                        } else {
                            
                            Boolean reservationEnLigne
                            Boolean generationOptionEnLigne
                            
                            if ( map.reservationEnLigne == "OUI"){
                                reservationEnLigne = true 
                            }else if (map.reservationEnLigne == "NON"){
                                reservationEnLigne = false 
                            }
                            
                            if ( map.generationOptionEnLigne == "OUI"){
                                generationOptionEnLigne = true
                            }else if (map.generationOptionEnLigne == "NON"){
                                generationOptionEnLigne = false
                            }
                            
                            def newProjet = new Projet(designation : map.designation
                                , code : map.code
                                , adresse : map.adresse
                                , delaiOption : map.delaiOption
                                , delaiReservation : map.delaiReservation
                                , delaiCompromis : map.delaiCompromis
                                , delaiContrat : map.delaiContrat
                                , reservationEnLigne : reservationEnLigne
                                , generationOptionEnLigne : generationOptionEnLigne
                                , type : map.type
                                , ville : map.ville
                                , modele : map.modele
                                , statut: map.statut
                                , groupeClient :map.groupeClient 
                                , visible : true
                                ,plan : null                              
                            )  
                            
                            projetService.save(newProjet)
                            utilisateur = utilisateur.addToProjets(newProjet)
                            nbImport++
                        }
                    }
                    
                    compteur++;
                } catch(Exception ex) {
                    loggerInformations(fileLog, "Echec import ligne " + compteur + " " + ex)
                    nbEchec++
                    compteur++;
                }
                
            }
            loggerInformations(fileLog, "Fin import Projets : " + new Date())
            loggerInformations(fileLog, "Nombre de lignes importées : " + nbImport)
            loggerInformations(fileLog, "Nombre de lignes échouées : " + nbEchec)
            utilisateurService.update(utilisateur)
        
            
        } catch(Exception ex) {
            loggerInformations(fileLog, "Fichier invalide : " + ls + ex)
        }
        return "Import terminé voir le fichier de log : " + fileLog.getAbsolutePath();
    }
    
    def importerNotaires(Object media){
        File fileLog = creerFichierLogImport("log_import_notaires")
        try {
            Workbook bk = new HSSFWorkbook(media.getStreamData())
            Sheet sheet = bk.getSheetAt(0)
            def first = true
            def compteur = 1;
            def nbImport = 0;
            def nbEchec = 0;
            
            def types = ["patente" : "String"
                , "idFiscal"  : "String"
                , "adresse" : "String"
                , "gsm" : "String"      
                , "fixe": "String"                                                     
                , "fax" : "String"
                , "email" : "String"
                , "nom" : "String"
                , "prenom" : 'String'];
            
                        
            //            def valeursParDefaut = []
            
            def valeursObligatoire = ["patente", "idFiscal"]
            //def valeursObligatoire = []
            
            loggerInformations(fileLog, "Début import des Notaire : " + new Date())
            for (Row row : sheet) {
                try {
                    if(first) {
                        first = false
                        compteur++;
                        continue
                    }
                    
                    def map = chargerDonneesLigne(row, types);                                                                           
                    
                    //chargerDonneesParDefaut(map, valeursParDefaut)
                    
                    def champ = validerChampsRequis(map, valeursObligatoire)
                    if(champ != null) {
                        loggerInformations(fileLog, "Echec import ligne " + compteur + " le champ : " + champ + " est requis")
                        nbEchec++
                    } else {
                        def oldNotaire = Notaire.findByIdFiscal(map.idFiscal);
                        if(oldNotaire != null ) {
                            loggerInformations(fileLog, "Echec import ligne " + compteur + " le notaire avec l'IF : " + map.idFiscal + " existe déjà")
                            nbEchec++
                        } else {
                            def newNotaire = new Notaire("patente" : map.patente
                                , "idFiscal"  : map.idFiscal            
                                , "adresse" : map.adresse
                                , "fixe": map.fixe                             
                                , "gsm" : map.gsm              
                                , "fax" : map.fax
                                , "email" : map.email
                                , "nom" : map.nom
                                , "prenom" :map.prenom );
                                                         
                            newNotaire = notaireService.save(newNotaire)                           

                            nbImport++
                        }
                    }
            
                    compteur++;
                } catch(Exception ex) {
                    loggerInformations(fileLog, "Echec import ligne " + compteur + " " + ex)
                    nbEchec++
                    compteur++;
                }
                
            }
            loggerInformations(fileLog, "Fin import client : " + new Date())
            loggerInformations(fileLog, "Nombre de lignes importées : " + nbImport)
            loggerInformations(fileLog, "Nombre de lignes échouées : " + nbEchec)

            
        } catch(Exception ex) {
            loggerInformations(fileLog, "Fichier invalide : " + ls + ex)
        }
        return "Import terminé voir le fichier de log : " + fileLog.getAbsolutePath();
    }
   
}
