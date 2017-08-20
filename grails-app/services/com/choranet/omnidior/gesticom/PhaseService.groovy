
package com.choranet.omnidior.gesticom;


import org.apache.commons.logging.Log 
import org.apache.commons.logging.LogFactory
import com.choranet.omnidior.gesticom.Phase
import com.choranet.omnidior.gesticom.Statut
import groovy.sql.Sql
/**
 * PhaseService Service pour la gestion des opérations
 * transactionnelles pour l'objet Phase
 */
class PhaseService extends SuperService {

    def statutService
    def sourceInformationService
    def canalService
    def bienService
    
    def sessionFactory
    
    def listStatut = ["07","08","09","27"]
    static transactional = true
    
    private Log logger = LogFactory.getLog(PhaseService.class)    

    def list() throws Exception {
        return super.list(Phase.class)
    }
    
    def filtrer(clazz, filtre, sortedHeader, sortedDirection, ofs, maxNb, utilisateur) throws Exception {
        try {
            def listeObjets 
            def tailleListe = 0
            def fields = clazz.getDeclaredFields()
            def requete = clazz.createCriteria()
            def attributToSort
            def selectionnerDistinctResultat = false
            def ids = utilisateur.clients*.id
            def idds = []
            ids.each {
                idds.addAll(it)
            }
            
            def idsp = utilisateur.projets*.id
            def iddsp = []
            idsp.each {
                iddsp.addAll(it)
            }
        
            if(idds.size() > 0 && iddsp.size() > 0) {
                listeObjets = requete.list {
                    for(field in fields) {
                        def nomAttribut = field.getName()
                        def modifier = field.getModifiers()

                        if(!nomAttribut.startsWith("trans_") && 
                            !(modifier > 2)) { // eviter les attributs transients: qui ne sont pas persister et les attributs static
                            def typeAttribut = field.getType()
                            //def valeurAttribut = field.get(filtre)
                            def valeurAttribut = filtre."$nomAttribut"
                    
                            if(sortedHeader == "h"+nomAttribut) {
                                attributToSort = nomAttribut 
                            }
                            if(valeurAttribut != null) {
                                if(valeurAttribut instanceof String) {
                                    if(valeurAttribut != "") {
                                        ilike(nomAttribut, "%"+valeurAttribut+"%")                                
                                    }
                                } else if(valeurAttribut instanceof Boolean || valeurAttribut == boolean.class) {
                                    if(valeurAttribut) {
                                        eq(nomAttribut, true)
                                    } else {
                                        eq(nomAttribut, false)
                                    }
                                } else if(valeurAttribut instanceof java.util.Collection) {
                                    "$nomAttribut" {"in"("id",valeurAttribut.id)}
                                    selectionnerDistinctResultat = true
                                } else {
                                    eq(nomAttribut, valeurAttribut)
                                }
                            }
                        }
                    }
                
                    client {
                    "in"("id", idds)
                    }
                    projet {
                    "in"("id", iddsp)
                    }
                
                    if(attributToSort != null) {
                        order(attributToSort, sortedDirection)
                    }
                    firstResult(ofs)
                    maxResults(maxNb)
                    if(selectionnerDistinctResultat) {
                        resultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
                        //Cette solution n est pas clean puisque ce travail est fait
                        //cote client et non pas database une deuxieme solution es la
                        //suivante reste a affiner voir: 
                        //https://forum.hibernate.org/viewtopic.php?t=941669
                        //                    projections {
                        //                        clazz.getSimpleName()
                        //                        distinct("id")
                        //                    }
                    }
                }
            } else {
                listeObjets = null
            }
            
            if(listeObjets != null && listeObjets.size() > 0) {
                def criteria = clazz.createCriteria()
                tailleListe = criteria.count{
                    for(field in fields) {
                        def nomAttribut = field.getName()
                        def modifier = field.getModifiers()
                        if(!nomAttribut.startsWith("trans_") && 
                            !(modifier > 2)) { // eviter les attributs transients: qui ne sont pas persister et les attributs static
                            def typeAttribut = field.getType()
                            //def valeurAttribut = field.get(filtre)
                            def valeurAttribut = filtre."$nomAttribut"
                    
                            if(valeurAttribut != null) {
                                if(valeurAttribut instanceof String) {
                                    if(valeurAttribut != "") {
                                        ilike(nomAttribut, "%"+valeurAttribut+"%")                                
                                    }
                                } else if(valeurAttribut instanceof Boolean || valeurAttribut == boolean.class) {
                                    if(valeurAttribut) {
                                        eq(nomAttribut, true)
                                    }
                                } else if(valeurAttribut instanceof java.util.Collection) {
                                    "$nomAttribut" {"in"("id",valeurAttribut.id)}
                                } else {
                                    eq(nomAttribut, valeurAttribut)
                                }
                            }
                        }
                    }
                    
                    client {
                    "in"("id", idds)
                    }
                    projet {
                    "in"("id", iddsp)
                    }
                    
                    if(selectionnerDistinctResultat) {
                        resultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
                        //Cette solution n est pas clean puisque ce travail est fait
                        //cote client et non pas database une deuxieme solution es la
                        //suivante reste a affiner voir: 
                        //https://forum.hibernate.org/viewtopic.php?t=941669
                        //                    projections {
                        //                        clazz.getSimpleName()
                        //                        distinct("id")
                        //                    }
                    }    
                }
            } else {
                tailleListe = 0
                listeObjets = null
            }
            
            return [tailleListe:tailleListe,listeObjets:listeObjets]
        }
        catch(Exception e) {
            logger.error(e)
            throw e
        }
    }

    def filtrer(clazz, filtre, attributsAFiltrer, sortedHeader, sortedDirection, ofs, maxNb,utilisateur) throws Exception {
        try {
            def listeObjets 
            def tailleListe = 0
            def requete = Bien.createCriteria()
            def attributToSort
            def selectionnerDistinctResultat = false
            def ids = utilisateur.clients*.id
            def idds = []
            ids.each {
                idds.addAll(it)
            }
            def idsp = utilisateur.projets*.id
            def iddsp = []
            idsp.each {
                iddsp.addAll(it)
            }
            
            if(idds.size() > 0 && iddsp.size() > 0) {
                listeObjets = requete.list {
                    for(nomAttribut in attributsAFiltrer) {
                        def valeurAttribut = filtre."$nomAttribut"
                    
                        if(sortedHeader == "h"+nomAttribut) {
                            attributToSort = nomAttribut 
                        }
                        if(valeurAttribut != null) {
                            if(valeurAttribut instanceof String) {
                                if(valeurAttribut != "") {
                                    ilike(nomAttribut, "%"+valeurAttribut+"%")                                
                                }
                            } else if(valeurAttribut instanceof Boolean || valeurAttribut == boolean.class) {
                                if(valeurAttribut) {
                                    eq(nomAttribut, true)
                                } else {
                                    eq(nomAttribut, false)
                                }
                            } else if(valeurAttribut instanceof java.util.Collection) {
                            "$nomAttribut" {"in"("id",valeurAttribut.id)}
                                selectionnerDistinctResultat = true
                            } else {
                                eq(nomAttribut, valeurAttribut)
                            }
                        }
                    }
                
                    client {
                    "in"("id", idds)
                    }
                    projet {
                    "in"("id", iddsp)
                    }
               
                    if(attributToSort != null) {
                        order(attributToSort, sortedDirection)
                    }
                    firstResult(ofs)
                    maxResults(maxNb) 
                    if(selectionnerDistinctResultat) {
                        resultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
                        //Cette solution n est pas clean puisque ce travail est fait
                        //cote client et non pas database une deuxieme solution es la
                        //suivante reste a affiner voir: 
                        //https://forum.hibernate.org/viewtopic.php?t=941669
                        //                    projections {
                        //                        clazz.getSimpleName()
                        //                        distinct("id")
                        //                    }
                    }
                }
            
            } else {
                listeObjets = null
            }
            if(listeObjets != null && listeObjets.size() > 0) {
                def criteria = clazz.createCriteria()
                tailleListe = criteria.count {
                    for(nomAttribut in attributsAFiltrer) {
                        def valeurAttribut = filtre."$nomAttribut"
                        
                        if(valeurAttribut != null) {
                            if(valeurAttribut instanceof String) {
                                if(valeurAttribut != "") {
                                    ilike(nomAttribut, "%"+valeurAttribut+"%")                                
                                }
                            } else if(valeurAttribut instanceof Boolean || valeurAttribut == boolean.class) {
                                if(valeurAttribut) {
                                    eq(nomAttribut, true)
                                }
                            }else if(valeurAttribut instanceof java.util.Collection) {
                                "$nomAttribut" {"in"("id",valeurAttribut.id)}
                            } else {
                                eq(nomAttribut, valeurAttribut)
                            }
                        }
                    }
                    
                    client {
                    "in"("id", idds)
                    }
                    projet {
                    "in"("id", iddsp)
                    }
                    
                    if(selectionnerDistinctResultat) {
                        resultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
                        //Cette solution n est pas clean puisque ce travail est fait
                        //cote client et non pas database une deuxieme solution es la
                        //suivante reste a affiner voir: 
                        //https://forum.hibernate.org/viewtopic.php?t=941669
                        //                    projections {
                        //                        clazz.getSimpleName()
                        //                        distinct("id")
                        //                    }
                    }
                }
            }else {
                tailleListe = 0
                listeObjets = null
            }
            
            return [tailleListe:tailleListe,listeObjets:listeObjets]
            
        }
        catch(Exception e) {
            logger.error(e)
            throw e
        }
    }
      
    
    def getPaiement(Phase p){
        def criteria = Paiement.createCriteria()
        def result = criteria.list {
            eq("phase", p)
        }        
        return result
    }
    
    def getTotalPaiementParPhase(Phase p){
        
        //        def criteria = Paiement.createCriteria()
        //        def result = criteria.get {
        //            phase{
        //                eq( numPhase, p.numPhase)
        //            }
        //            projections {
        //                sum("montant")
        //            }
        //        }

        def paiements = getPaiement(p)//p.paiements
        Double result  = 0 
        for(pm in paiements){
            if(pm.statut.code == "12"){                           
                result += pm.montant  
            }
        }
        
        if(result == null) {
            result = 0;
        }
        return result
    }
    
    def getPaiementByProjetClient(projet,client){
        
        def criteria = Paiement.createCriteria()
        def result = criteria.list {
    
            eq("projet",projet)
            eq("client",client)
            order("datePaiement","asc")
        }
        
        return result  
    }
    
    def getByClientEtProjet(client,projet){
        
        def criteria = Phase.createCriteria()
        def result = criteria.list {
            eq("client",client)
            eq("projet",projet)
            statut {
                 "in"("code", listStatut)
            }
            order ("numPhase","asc")
        }
        
        return result 
    }
    
    def getByClientEtProjetAll(client,projet){
        
        def criteria = Phase.createCriteria()
        def result = criteria.list {
            eq("client",client)
            eq("projet",projet)
            statut {
                             "in"("code", ["07","27"])
               
            }
            order ("numPhase","asc")
        }
        
        return result 
    }
    def getByClientEtProjetAllCount (client,projet){
        
        def criteria = Phase.createCriteria()
        def result = criteria.get {
            projections {
                count('id')
            }
            eq("client",client)
            eq("projet",projet)
        }
        if (result){
            return result 
        }
        else{
            return 0 
        }
        
    }
    def getPhaseByDate(date,idds,iddsp){
                
        if(idds.size() > 0 && iddsp.size() > 0) {
            def criteria = Phase.createCriteria()
            def result = criteria.list {
                le("dateFin",date)
                
                statut{
                    "in"("code",["07","27"])
                }                    
                
                client{
                        "in"("id", idds)
                }
                
                projet{
                        "in"("id", iddsp)
                }
                order("numPhase","asc")
            }
        }
        
        else {
            return []
        }
        // return result 
    }
    
    def getClientByProjet(projet,utilisateur){
        def ids = utilisateur*.clients*.id
        def idds = []
        ids.each {
            idds.addAll(it)
        }
        
        if (idds.size() >0 ){
            def criteria = Phase.createCriteria()
            def result = criteria.list {
                eq("projet",projet)
                client{
                "in"("id", idds)
                    order ("nom","asc")
                    order ("prenom","asc")
                    order ("cin","asc")
                }
                statut {
                    eq("code","07")
                } 


            }
        
            def listResult = result*.client 
            def lstR = []
            for (r in listResult) {
                if (!lstR.contains(r)){
                    lstR.add(r)
                }
            }
        
            return lstR
        } else{
            return null
        }
        
    }
    
    def getClientByProjetAll(projet,utilisateur){
        def ids = utilisateur*.clients*.id
        def idds = []
        ids.each {
            idds.addAll(it)
        }
        
        if(idds.size()> 0 ){
            def criteria = Phase.createCriteria()
            def result = criteria.list {
                eq("projet",projet)
                client{
                "in"("id", idds)
                    order ("nom","asc")
                    order ("prenom","asc")
                    order ("cin","asc")
                }
                //            statut {
                //                eq("code","07")
                //            }
            }
        
            def listResult = result*.client 
            def lstR = []
            for (r in listResult) {
                if (!lstR.contains(r)){
                    lstR.add(r)
                }
            }
        
            return lstR
        } 
        else {
            return null  
        }
    }
        
    def getProjetByClient(client){
        
        def criteria = Phase.createCriteria()
        def result = criteria.list {
            eq("client",client)
            statut {
                eq("code","07")
            }
            projet{
                order("code","asc")
            }
        }
        
        return result*.projet 
    }
    
    def getProjetByClientAll(client){
        
        def criteria = Phase.createCriteria()
        def result = criteria.list {
            eq("client",client)
            //            statut {
            //                eq("code","07")
            //                or{
            //                    eq("intitule","ANNULEE")
            //                }
            //            }
            projet{
                order("code","asc")
            }
        }
        
        return result*.projet 
    }    
    
    def passerEtapeSuivante(Phase phase){
       
        String etapeSuivante 
        Integer intervalDate

        switch (phase.intitule){
            case "OPTION":
            etapeSuivante = "RESERVATION"   
            intervalDate = phase.projet.delaiReservation
            break
            
            case "RESERVATION":
            etapeSuivante = "COMPROMIS"  
            intervalDate = phase.projet.delaiCompromis
            break
            
            case "COMPROMIS":
            etapeSuivante = "CONTRAT"  
            intervalDate = phase.projet.delaiContrat
        }
        
        phase.statut = Statut.findByCode("07") //statutService.getByTypeEtIntitule("PHASE","EN COURS")    
        
        Phase BackupEtape = new Phase()       
        BackupEtape.numPhase = phase.numPhase //+ "_" + phase.intitule
        BackupEtape.commentaire = phase.commentaire
        BackupEtape.dateDebut	= phase.dateDebut
        BackupEtape.dateFin	= phase.dateFin
        BackupEtape.estExpire	=  phase.estExpire
        BackupEtape.estAvecAffectation =  phase.estAvecAffectation
        BackupEtape.montantReliquat =	phase.montantReliquat
        BackupEtape.montantVente = phase.montantVente	
        BackupEtape.bien = phase.bien
        BackupEtape.commercialResponsable = phase.commercialResponsable
        BackupEtape.notaire = phase.notaire
        BackupEtape.projet = phase.projet
        BackupEtape.client = phase.client
        //BackupEtape.documents = phase.documents
        BackupEtape.intitule = phase.intitule
        BackupEtape.statut = Statut.findByCode("09")//statutService.getByTypeEtIntitule("PHASE","CLOTUREE")             

        BackupEtape.sourceInformation = phase.sourceInformation
        BackupEtape.canal = phase.canal
        BackupEtape.dateContact = phase.dateContact
        BackupEtape.idPhaseParent = phase.id
        BackupEtape.estHistorique = true

        
        def DateF = new Date()
        DateF = DateF.plus(intervalDate)
        phase.dateFin = DateF          
        def num = phase.numPhase.split("_")
        
        String newCode = num[0] + "_" + (String)num[1]+ "_" + (String)num[2] + "_" + etapeSuivante
        
        // phase.numPhase = 
        
        String autoNum = genererNumPhase(newCode)
        
        if (autoNum != "00"){
            newCode = newCode + "_" + autoNum
        }
        
        phase.intitule = etapeSuivante
        phase.numPhase = newCode
        phase = update(phase)

        
        
        save(BackupEtape)   

        
        return phase
    }        
        
    def existePhaseActive (Phase phase, Bien b){
        Boolean existePhase
        def criteria = Phase.createCriteria()
        def result = criteria.list {
            ne('id',phase.id)
            eq("bien",b)
            bien{
                statut{
                    ne('intitule','Libre')
                }
            }
            statut{
                ne('intitule','CLOTUREE')
                ne('intitule','ANNULEE')
            }
        }
        
        if (result && result.size()>0){
            existePhase = true
        }
        else{
            existePhase = false 
        }
        
        return existePhase
    }
    
    def getPhaseByIntitule(intitule,utilisateur){
        def ids = utilisateur*.clients*.id
        def idds = []
        ids.each {
            idds.addAll(it)
        }
        
        def criteria = Phase.createCriteria()
        def result = criteria.list {
            eq("intitule",intitule)
            client{
                "in"("id", idds)
            }
            order ("numPhase","asc")
        }
        
        return result
    }
        
    def genererRecu(Phase){
        def bien = phase.bien                
    }
    
    def updatePhaseExpiree(){       
        
        def criteria = Phase.createCriteria()
        def result = criteria.list {
            lt("dateFin",new Date())
            statut{
                eq("intitule", "EN COURS")
                
            }
    
        }
        def statutExpiree = statutService.getByCode("27")
        if (result != null && result.size()>0){
            for(rst in result) {
                rst.estExpire = true                
                rst.statut = statutExpiree//.getByTypeEtIntitule("PHASE","EXPIREE")  
                rst = update(rst)

            }
        }
    }
    
    def getPhaseByEcheance(echeance){
        def criteria = Echeance.createCriteria()
        def result = criteria.get {          
            eq( "phase",echeance.phase)
        }
        return result
    }

    def getPhaseByPaiement(phases,Paiement pm){
        //        def phase
        //
        //        for (l in phases){
        //            for (ln in l.paiements){
        //                if (ln.id == pm.id){
        //                    phase = l

        //                }  
        //            }
        //            //            if(l.paiements.contains(pm))
        //            //            {

        //            //                phase = l 
        //            //            }
        //        }
        return pm.phase
    }
    
    def getPhaseByUtlisateur(utilisateur, intitulePhase = null,ofs = null,maxNb = null, encours = null){
       
        def ids = utilisateur.clients*.id
        def idds = []
        ids.each {
            idds.addAll(it)
        }
        def idsp = utilisateur.projets*.id
        def iddsp = []
        idsp.each {
            iddsp.addAll(it)
        }
        
        if(idds.size() > 0 && iddsp.size() > 0) {
            def criteria = Phase.createCriteria()
            def result = criteria.list {
                if (intitulePhase){
                    eq("intitule",intitulePhase)
                }
                client {
                    "in"("id", idds)
                }
                projet {
                    "in"("id", iddsp)
                }
                if (encours){
                    statut {
                        eq("code","07")
                    }
                }
                order("numPhase","asc")
                if(ofs)
                firstResult(ofs)
                if(maxNb)
                maxResults(maxNb)
            }

        } else {
            return []
        }
        
    }
    
    def getPhaseByUtlisateurCount(utilisateur, intitulePhase = null, encours = null){
       
        def ids = utilisateur.clients*.id
        def idds = []
        ids.each {
            idds.addAll(it)
        }
        def idsp = utilisateur.projets*.id
        def iddsp = []
        idsp.each {
            iddsp.addAll(it)
        }
        
        if(idds.size() > 0 && iddsp.size() > 0) {
            def criteria = Phase.createCriteria()
            def result = criteria.get {
                projections {
                    count("id")
                }
                if (intitulePhase){
                    eq("intitule",intitulePhase)
                }
                client {
                    "in"("id", idds)
                }
                projet {
                    "in"("id", iddsp)
                }
                if (encours){
                    statut {
                        eq("code","07")
                    }
                }                
            }

        } else {
            return 0
        }        
        
    }    
    
    def getPhaseByUtlisateurReport(utilisateur, intitulePhase = null, encours = null,ofs = null,maxNb = null){
       
        //        def ids = utilisateur.clients*.id
        //        def idds = []
        //        ids.each {
        //            idds.addAll(it)
        //        }
        //        def idsp = utilisateur.projets*.id
        //        def iddsp = []
        //        idsp.each {
        //            iddsp.addAll(it)
        //        }
        //        
        //        if(idds.size() > 0 && iddsp.size() > 0) {
        def criteria = Phase.createCriteria()
        def result = criteria.list {
            if (intitulePhase){
                eq("intitule",intitulePhase)
            }
            //                client {
            //                    "in"("id", idds)
            //                }
            //                projet {
            //                    "in"("id", iddsp)
            //                }
            if (encours){
                statut {
                     "in"("code", listStatut)
                }
            }
            order("numPhase","asc")
            if(ofs)
            firstResult(ofs)
            if(maxNb)
            maxResults(maxNb)
        }
        return result
        //
        //        } else {
        //            return []
        //        }z
        
    }   
    
    def getPhaseByUtlisateurExpiree(utilisateur, intitulePhase = null,ofs = null,maxNb = null){
        
        def criteria = Phase.createCriteria()
        def result = criteria.list {
            if(intitulePhase){
                eq("intitule",intitulePhase)            
            }
            eq("estHistorique" , false)
            statut {
                eq("code", "27")
            }
       
            order("numPhase","asc")
            if(ofs)
            firstResult(ofs)
            if(maxNb)
            maxResults(maxNb)
        }
            
        return result

        
    }
    
    def getPhaseByUtlisateurEtTypeBienEtSuperficie(utilisateur, intitulePhase,typeBien,sup1,sup2,ofs = null,maxNb = null){
       
        //        def ids = utilisateur.clients*.id
        //        def idds = []
        //        ids.each {
        //            idds.addAll(it)
        //        }
        //        def idsp = utilisateur.projets*.id
        //        def iddsp = []
        //        idsp.each {
        //            iddsp.addAll(it)
        //        }
        
        //        if(idds.size() > 0 && iddsp.size() > 0) {
        def criteria = Phase.createCriteria()
        def result = criteria.list {
            if (intitulePhase){
                eq("intitule",intitulePhase)                
            }
            eq("estHistorique" , false)
            //                client {
            //                    "in"("id", idds)
            //                }
            //                projet {
            //                    "in"("id", iddsp)
            //                }
            bien{
                between("superficie",sup1,sup2)
                eq("type",typeBien)
            }
                
            statut{
                 "in"("code", listStatut)
            }
            order("numPhase","asc")
            if(ofs)
            firstResult(ofs)
            if(maxNb)
            maxResults(maxNb)
        }
        //
        //        } else {
        //            return []
        //        }
        
    }   
    
    def getPhaseByUtlisateurEtTypeBienEtSuperficieEtProjet(utilisateur, intitulePhase,typeBien,Double sup1,Double sup2,projet,ofs = null,maxNb = null){
         
        //        def ids = utilisateur.clients*.id
        //        def idds = []
        //        ids.each {
        //            idds.addAll(it)
        //        }
        //        def idsp = utilisateur.projets*.id
        //        def iddsp = []
        //        idsp.each {
        //            iddsp.addAll(it)
        //        }
        //        
        //        if(idds.size() > 0) {
        def criteria = Phase.createCriteria()
        def result = criteria.list {
            if(intitulePhase){
                eq("intitule",intitulePhase)
            }
            eq("estHistorique" , false)
            eq("projet",projet)
            //                client {
            //                    "in"("id", idds)
            //                }
            //                projet {
            //                    eq()
            //                    //"in"("id", iddsp)
            //                }
            bien{                
                between("superficie", sup1, sup2)
                //                ge("superficie", sup1)
                //                le("superficie", sup2)
                eq("type",typeBien)
            }
            statut{
                 "in"("code", listStatut)
            }
            if(ofs)
            firstResult(ofs)
            if(maxNb)
            maxResults(maxNb)
        }
        //
        //        } else {
        //            return []
        //        }
        
    }   
    
    def getPhaseByUtlisateurEtProjet(utilisateur, intitulePhase = null,projet,ofs = null,maxNb = null){
       
        //        def ids = utilisateur.clients*.id
        //        def idds = []
        //        ids.each {
        //            idds.addAll(it)
        //        }
        //        def idsp = utilisateur.projets*.id
        //        def iddsp = []
        //        idsp.each {
        //            iddsp.addAll(it)
        //        }
        
        //        if(idds.size() > 0) {
        def criteria = Phase.createCriteria()
        def result = criteria.list {
            if (intitulePhase){
                eq("intitule",intitulePhase)
            }
            eq("estHistorique" , false)
            eq("projet",projet)
            //                client {
            //                    "in"("id", idds)
            //                }
            statut{
                 "in"("code", listStatut)
            }
            if(ofs)
            firstResult(ofs)
            if(maxNb)
            maxResults(maxNb)
        }

        //        } else {
        //            return []
        //        }
        
    }

    def getPhaseByUtlisateurEtProjetEtVille(utilisateur, intitulePhase = null,projet,ville,ofs = null,maxNb = null){
       
        //        def ids = utilisateur.clients*.id
        //        def idds = []
        //        ids.each {
        //            idds.addAll(it)
        //        }
        //        def idsp = utilisateur.projets*.id
        //        def iddsp = []
        //        idsp.each {
        //            iddsp.addAll(it)
        //        }
        
        //        if(idds.size() > 0) {
        def criteria = Phase.createCriteria()
        def result = criteria.list {
            if (intitulePhase){
                eq("intitule",intitulePhase)
            }
            eq("estHistorique" , false)
            eq("projet",projet)
            client {
                eq("ville", ville)
            }
            //                client {
            //                    "in"("id", idds)
            //                }
            statut{
                 "in"("code", listStatut)
            }
            if(ofs)
            firstResult(ofs)
            if(maxNb)
            maxResults(maxNb)
        }

        //        } else {
        //            return []
        //        }
        
    }
    
    def getPhaseByUtlisateurEtNotaire(utilisateur, intitulePhase = null,notaire,ofs = null,maxNb = null){
       
        def criteria = Phase.createCriteria()
        def result = criteria.list {
            if(intitulePhase){
                eq("intitule",intitulePhase)
            }
            eq("estHistorique" , false)
            eq("notaire",notaire)

            statut{
                 "in"("code", listStatut)
            }
            if(ofs)
            firstResult(ofs)
            if(maxNb)
            maxResults(maxNb)
        }
        return result

        
    }
    
    def getPhaseByUtlisateurEtCommercial(intitulePhase,commercial,ofs = null,maxNb = null){
       
        //        def ids = utilisateur.clients*.id
        //        def idds = []
        //        ids.each {
        //            idds.addAll(it)
        //        }
        //        def idsp = utilisateur.projets*.id
        //        def iddsp = []
        //        idsp.each {
        //            iddsp.addAll(it)
        //        }
        //        
        //        if(idds.size() > 0 && iddsp.size() > 0) {
        def criteria = Phase.createCriteria()
        def result = criteria.list {
            if(intitulePhase){
                eq("intitule",intitulePhase)
            }
            eq("estHistorique" , false)
            eq("commercialResponsable",commercial)
            //                client {
            //                    "in"("id", idds)
            //                }
            //                projet {
            //                    "in"("id", iddsp)
            //                }
            statut{
                 "in"("code", listStatut)
            }
            order("projet","asc")
            order("client","asc")
            order("numPhase","asc")
            if(ofs)
            firstResult(ofs)
            if(maxNb)
            maxResults(maxNb)
        }
            
        return result
        //
        //        } else {
        //            return []
        //        }
        
    }
    
    def existePhaseClient(Client client){
        println 'enter existePhaseClient'
        def criteria = Phase.createCriteria()
        def result = criteria.get {
            projections{
                count("id")
            }
            eq( "client",client)
        }
         println 'end existePhaseClient'
        if(result){
            return result 
        }
        else{
            return 0
        }
    }
    
    def genererNumPhase(Projet projet,Client client){
        def cpt = getByClientEtProjetAllCount(client,projet)
        String code = "0001"
        if (cpt> 0){            
            cpt +=1
            if (cpt < 10) {
                code = "000" + cpt
            }            
            else if (cpt >= 10 && cpt < 100 ){
                code = "00" + cpt
            }            
            else if( cpt >= 100 && cpt < 1000 ){
                code = "0" + cpt
            }                        
        }                   
        code = projet.code + "-" + code 
        return code
    }
    
    def genererNumPhase(String numPhase){
        //def cpt = Phase.countByNumPhase(numPhase) //getByCodeAllCount(codePhase)
        def c = Phase.createCriteria()
        String likeC = numPhase + "%"
        def cpt = c.get {
            like("numPhase", likeC)
            projections {
                countDistinct("id")
            }
            
        }
        

        String code = "0"
        if (cpt){
            if (cpt> 0){            
                cpt +=1
                code =  cpt
            }
        }

        return code
    }
    
    def genererOptionAutomatique (Bien b, Client c){
          
        def phase = new Phase ()
        phase.projet = b.projet
        phase.client = c
        phase.bien = b           
        phase.numPhase = genererNumPhase (b.projet, c)
        phase.commentaire = ""
        Date dateJ = new Date()
        phase.dateDebut = dateJ
        phase.dateFin = dateJ.plus(b.projet.delaiOption)
        phase.estExpire = false
        phase.estAvecAffectation = true 
        phase.montantVente = b.prixVente
        phase.intitule = "OPTION"
        phase.totalPaiements = 0
        phase.montantReliquat = b.prixVente
        phase.dateContact = dateJ
        //phase.commercialResponsable = "?"
        phase.sourceInformation = sourceInformationService.getSourceInformationByIntitule('Site web')
        phase.canal = canalService.getCanalByIntitule('Visite site web')
        phase.statut = statutService.getByCode("07")//getByTypeEtIntitule("PHASE","EN COURS")    
        phase = save(phase) 
        
               
        b.statut = statutService.getByCode("25") 
        b =  bienService.update(b)
        if (b.bienParent){
            bienService.updateEtatBienParent(b.bienParent)
        }
    
    }
    
    def getCountPhase(Boolean estExpiree){
        def session = sessionFactory.getCurrentSession()
        //        session.clear()  
        //def Qry = "select partenaire.raisonSociale as fournisseur,min(ligneProduit.prixDeduit) as prix from Livraison livraison join livraison.partenaire partenaire join livraison.bonLivraisons bl join bl.ligneProduits ligneProduit join ligneProduit.produit produit where bl.type = :typeTran and produit.code = :code group by partenaire.raisonSociale order by min(ligneProduit.prixDeduit)"
        
        String Qry 
        if (estExpiree){
            Qry = "select phase.intitule, count(distinct phase.id) as Nbre from Phase phase where phase.estExpire = 'true' and phase.estHistorique = 'false' group by phase.intitule order by count(distinct phase.id) desc "
        }
        else
        {
            Qry = "select phase.intitule, count(distinct phase.id) as Nbre from Phase phase group by phase.intitule order by count(distinct phase.id) desc "
        }
        def q = session.createQuery(Qry)
        
        def results = q.list()
        return results
    }
    
    def getCountPhaseByProjet(){
        def session = sessionFactory.getCurrentSession()
        //        session.clear()  
        //def Qry = "select partenaire.raisonSociale as fournisseur,min(ligneProduit.prixDeduit) as prix from Livraison livraison join livraison.partenaire partenaire join livraison.bonLivraisons bl join bl.ligneProduits ligneProduit join ligneProduit.produit produit where bl.type = :typeTran and produit.code = :code group by partenaire.raisonSociale order by min(ligneProduit.prixDeduit)"
        
        String Qry  = "select projet.code, count(distinct phase.id) as Nbre from Phase phase join phase.projet projet where phase.estHistorique = 'false' group by projet.code order by count(distinct phase.id) desc"
        
        def q = session.createQuery(Qry)
        //        q.setString("codeProjet",codeProjet)
        def results = q.list()
        return results
    }
    
    def getCountPhaseByProjetEtVille(String codeProjet){
        def session = sessionFactory.getCurrentSession()
        //        session.clear()  
        //def Qry = "select partenaire.raisonSociale as fournisseur,min(ligneProduit.prixDeduit) as prix from Livraison livraison join livraison.partenaire partenaire join livraison.bonLivraisons bl join bl.ligneProduits ligneProduit join ligneProduit.produit produit where bl.type = :typeTran and produit.code = :code group by partenaire.raisonSociale order by min(ligneProduit.prixDeduit)"
        
        String Qry  = "select ville.intitule, count(distinct client.id) as Nbre from Phase phase join phase.client client join phase.projet projet join client.ville ville where phase.estHistorique = 'false' and projet.code = :codeProjet group by ville.intitule order by count(distinct client.id) desc"
        
        def q = session.createQuery(Qry)
        q.setString("codeProjet",codeProjet)
        def results = q.list()
        return results
    }    
    
    def getCountPhaseByNotaire() {
        def session = sessionFactory.getCurrentSession()
        //        session.clear()  
        //def Qry = "select partenaire.raisonSociale as fournisseur,min(ligneProduit.prixDeduit) as prix from Livraison livraison join livraison.partenaire partenaire join livraison.bonLivraisons bl join bl.ligneProduits ligneProduit join ligneProduit.produit produit where bl.type = :typeTran and produit.code = :code group by partenaire.raisonSociale order by min(ligneProduit.prixDeduit)"
        
        String Qry  = "select concat(notaire.nom,' ',notaire.prenom,' ',notaire.idFiscal), count(distinct phase.id) as Nbre from Phase phase join phase.notaire notaire where phase.estHistorique = 'false' group by concat(notaire.nom,' ',notaire.prenom,' ',notaire.idFiscal) order by count(distinct phase.id) desc"
        
        def q = session.createQuery(Qry)
        //        q.setString("codeProjet",codeProjet)
        def results = q.list()
        return results
    }
    
    def getCountPhaseByCommercialResponsable() {
        def session = sessionFactory.getCurrentSession()
        //        session.clear()  
        //def Qry = "select partenaire.raisonSociale as fournisseur,min(ligneProduit.prixDeduit) as prix from Livraison livraison join livraison.partenaire partenaire join livraison.bonLivraisons bl join bl.ligneProduits ligneProduit join ligneProduit.produit produit where bl.type = :typeTran and produit.code = :code group by partenaire.raisonSociale order by min(ligneProduit.prixDeduit)"
        
        String Qry  = "select commercialResponsable.username, count(distinct phase.id) as Nbre from Phase phase join phase.commercialResponsable commercialResponsable where phase.estHistorique = 'false' group by commercialResponsable.username order by count(distinct phase.id) desc"
        
        def q = session.createQuery(Qry)
        //        q.setString("codeProjet",codeProjet)
        def results = q.list()
        return results
    }
    
    def getpaimentservice(Phase phase)
    {
        
        def criteria = Paiement.createCriteria()
        def result = criteria.list {
            eq("phase",phase) 
            statut{
                ne("code","12")
            }
            
        }
        
        return result
    
    
    }
    
    def getTotalPaiements( def idPhase) { 
        def criteria = Paiement.createCriteria()
        def totalPaiements = criteria.get {
            phase{
                eq("id",idPhase)
            }
            statut{
                eq("code","12")
            }
            projections {
                sum("montant")
            }
            
        }
        if(totalPaiements == null) {
            totalPaiements = 0;
        }

        return totalPaiements
    }
    
    def updateBienClient(){
        //        def session = sessionFactory.getCurrentSession()
        //        String qry  = "update Bien set Bien.client = Phase.client.nom + ' ' + Phase.client.prenom + ' -' + Phase.client.cin"
        //        def query = session.createQuery(qry)
        //        def updatedRows = query.executeUpdate()
        def phases = Phase.list()
        for (phase in  phases){
            if (phase.bien ){
                Bien bien = phase.bien 
                bien.client = phase.client.toString()
                bienService.update(bien)
            }
        }
    }
    
    def getClientsByListeProjetAll(projets){
        
        def criteria = Phase.createCriteria()
        def result = criteria.list {

            projet{
                "in"("id", projets)
            }
            client{
                order ("nom","asc")
                order ("prenom","asc")
                order ("cin","asc")
            }

        }
                
        return  result*.client //lstR

    }
    
    def updateTotalPaiements(){
        //change connection url
        String qry= "update phase set total_paiements = table_total_paiement.sum_paiement FROM (select paiement.phase_id, sum(montant) as sum_paiement from paiement join statut on statut.id= paiement.statut_id where statut.code  = '12' group by paiement.phase_id ) as table_total_paiement where phase.id =table_total_paiement.phase_id "
        def sql = Sql.newInstance("jdbc:postgresql://localhost/gestimmo", "root", "Omnidior2012", "org.postgresql.Driver")    
        println(qry)
        int rowsAffected = sql.executeUpdate(qry)
        //println "+++ rowsAffected : " + rowsAffected

    }
       
}