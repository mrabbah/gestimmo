
package com.choranet.omnidior.gesticom;
import com.choranet.omnidior.gesticom.JournalPaiement;


/**
 * PaiementService Service pour la gestion des opérations
 * transactionnelles pour l'objet Paiement
 */
class PaiementService extends SuperService {

    static transactional = true
    def statutService
    def echeanceService
    def journalPaiementService 
    def sessionFactory
    def listStatut = ["07","08","27"]

    def list() throws Exception {
        return super.list(Paiement.class)
    }
        
    def getPaiementNonValide(idds,iddsp)
    {
        
        if(idds.size() > 0 && iddsp.size() > 0) {
            def criteria = Paiement.createCriteria()
            def result = criteria.list {
                statut{
                    eq("code", "13")
                }
                phase{                                  
                    client{
                 "in"("id", idds)
                    }
                    projet{
                 "in"("id", iddsp)
                    }
                    statut{
                        eq("code","07")
                    }
                }
                order ("datePaiement","asc")
            }
        }
        
        else {
            return []
        }
        //return result
    }
    
    def getPaiementByPhase(Phase p)
    {
  
        def criteria = Paiement.createCriteria()
        def result = criteria.list {
 
            eq('phase',p)
            order ("datePaiement","asc")
        }
        return result
    }
     
    def updateListeEcheance(Paiement objetPaiement){
        
        def lst = echeanceService.getEcheanceByPaiement(objetPaiement)

        if(lst){
            lst = lst.sort{it.date}
            def statutPaye = statutService.getByCode("16")//getByTypeEtIntitule("ECHEANCE","PAYEE")  
            def statutEncours = statutService.getByCode("15")//getByTypeEtIntitule("ECHEANCE","EN COURS") 
            
            //            Double sommeEcheance = 0
            //            for (lm in lst){
            //                sommeEcheance += lm.montantPaye
            //            }
        
            Double RestmontantPaiement = objetPaiement.montant
            // RestmontantPaiement -= sommeEcheance
            Double montantEcart = 0
          
            lst.each{ln ->

                if (RestmontantPaiement >= ln.montantEcheance - ln.montantPaye )
                {
                    montantEcart = ln.montantEcheance - ln.montantPaye
                
                    ln.montantPaye += montantEcart
                    ln.statut = statutPaye
                
                    RestmontantPaiement -= montantEcart                        
                    
                    ln = echeanceService.update(ln)
                    JournalPaiement jp = new JournalPaiement()
                    jp.montantPaye = montantEcart
                    jp.paiement = objetPaiement
                    jp.echeance = ln 
                    jp = journalPaiementService.update(jp)
                        
                    montantEcart = 0                        
                }
                else if(RestmontantPaiement < ln.montantEcheance - ln.montantPaye && RestmontantPaiement > 0 ){
                    ln.montantPaye += RestmontantPaiement                        
                    ln.statut = statutEncours
                        
                    ln = echeanceService.update(ln)
                    JournalPaiement jp = new JournalPaiement()
                    jp.montantPaye = RestmontantPaiement
                    jp.paiement = objetPaiement
                    jp.echeance = ln 
                    jp = journalPaiementService.update(jp)
                    RestmontantPaiement = 0
                }
                else{
                    objetPaiement.removeFromEcheances(ln)                        
                }              
            }
            objetPaiement = update(objetPaiement)
            
        }
        return objetPaiement
    }
    
    def onDeletePaiement(Paiement objetPaiement) {
        def statutEncours = statutService.getByTypeEtIntitule("ECHEANCE","EN COURS")
        def lstJournalPaiement = JournalPaiement.findAllByPaiement(objetPaiement)
        if (lstJournalPaiement && lstJournalPaiement.size()>0){
            for (jp in lstJournalPaiement){
                Echeance e = jp.echeance
                e.montantPaye = e.montantPaye - jp.montantPaye
                e.statut = statutEncours
                e = echeanceService.update(e)
                journalPaiementService.delete(jp)
            }
        }

    }
    
    def onDeleteEcheance (Echeance objetEcheance) {        
        def lstJournalPaiement = JournalPaiement.findAllByEcheance(objetEcheance)
        if (lstJournalPaiement && lstJournalPaiement.size()>0){
            for (jp in lstJournalPaiement){
                journalPaiementService.delete(jp)
            }
        }

    }
    
    def onUpdatePaiement(Paiement objetPaiement){        
        onDeletePaiement(objetPaiement)
        objetPaiement = updateListeEcheance(objetPaiement) 
    }
    
    def genererNumeroPaiement(Phase p){
        Integer num = 1 
        def paiements = getPaiementByPhase(p)
        if(paiements && paiements.size()>0 ) {
            num =  paiements.size() + 1
        }
        //        else{
        //             num = 1;
        //        }
        return num
    }    
    
    def filtrer(clazz, filtre, sortedHeader, sortedDirection, ofs, maxNb,utilisateur) throws Exception {
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
                                        ilike(nomAttribut, valeurAttribut+"%")                                
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
                    phase{
                        client {
                    "in"("id", idds)
                        }
                        projet {
                    "in"("id", iddsp)
                        }
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
                                        ilike(nomAttribut, valeurAttribut+"%")                                
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
                    phase{
                        client {
                    "in"("id", idds)
                        }
                        projet {
                    "in"("id", iddsp)
                        }
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
                                    ilike(nomAttribut, valeurAttribut+"%")                                
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
                    phase{
                        client {
                    "in"("id", idds)
                        }
                        projet {
                    "in"("id", iddsp)
                        }
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
                                    ilike(nomAttribut, valeurAttribut+"%")                                
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
                    phase{
                        client {
                    "in"("id", idds)
                        }
                        projet {
                    "in"("id", iddsp)
                        }
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
    
    def getPaiementByUtlisateur(utilisateur,ofs = null,maxNb = null){
       
       
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
            def criteria = Paiement.createCriteria()
            def result = criteria.list {
                phase{
                    client {
                    "in"("id", idds)
                    }
                    projet {
                    "in"("id", iddsp)
                    }
                }
                order("datePaiement","asc")
                if(ofs)
                firstResult(ofs)
                if(maxNb)
                maxResults(maxNb)
            }

        } else {
            return []
        }
        
    }
    
    def getPaiementByUtlisateurEtProjetEtType(utilisateur,projet,typePaiement = null, intitulePhase = null,ofs = null,maxNb = null){
       
        def criteria = Paiement.createCriteria()
        def result = criteria.list {
            if (typePaiement){
                eq("modePaiement",typePaiement)
            }

            phase{
                statut{
                 "in"("code", listStatut)
                }
                if(intitulePhase){
                    eq("intitule",intitulePhase)  
                }
            }
            order ("datePaiement","asc")
            if(ofs)
            firstResult(ofs)
            if(maxNb)
            maxResults(maxNb)
        }
            
        return result

        //        } else {
        //            return []
        //        }
        
    }
    
    def getPaiementByEcheance(Echeance ech){
      
        def criteria = JournalPaiement.createCriteria()
        def result = criteria.list { 
            eq('echeance',ech)            
        }
        return result*.paiement
    }
    
    def getCountPaiementByProjet(String codeProjet = null ){
        def session = sessionFactory.getCurrentSession()
        //        session.clear()  
        //def Qry = "select partenaire.raisonSociale as fournisseur,min(ligneProduit.prixDeduit) as prix from Livraison livraison join livraison.partenaire partenaire join livraison.bonLivraisons bl join bl.ligneProduits ligneProduit join ligneProduit.produit produit where bl.type = :typeTran and produit.code = :code group by partenaire.raisonSociale order by min(ligneProduit.prixDeduit)"
        String Qry
        if(codeProjet){
            Qry  = "select paiement.modePaiement.type, count(*) as Nbre from Paiement paiement join paiement.phase phase where phase.projet.code = :codeProjet group by paiement.modePaiement.type order by count(*) desc, paiement.modePaiement.type asc"
        }
        else{
            Qry  = "select paiement.modePaiement.type, count(*) as Nbre from Paiement paiement group by paiement.modePaiement.type order by count(*) desc, paiement.modePaiement.type asc"
        }
        def q = session.createQuery(Qry)
        
        if(codeProjet){
            q.setString("codeProjet",codeProjet)
        }
        //q.setMaxResults(4)
        def results = q.list()
        return results
    }
    
}