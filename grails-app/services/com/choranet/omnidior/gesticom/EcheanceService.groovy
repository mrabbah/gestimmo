
package com.choranet.omnidior.gesticom;


/**
 * EcheanceService Service pour la gestion des opérations
 * transactionnelles pour l'objet Echeance
 */
class EcheanceService extends SuperService {

    static transactional = true

    def list() throws Exception {
        return super.list(Echeance.class)
    }
        
    def getEcheanceBydateEtUtilisateur(dateD,idds,iddsp){
        
        def lp = Parametrage.list()
        Date dateEchPar = new Date()
        
        if (lp && lp.size()>0){
            Parametrage p = lp[0]
            dateEchPar =  new Date() + p.echeanceDelaiJours              
        }
               
        if(idds.size() > 0 && iddsp.size() > 0 ) {
            def criteria = Echeance.createCriteria()
            def result = criteria.list {
                
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
                //and {
                statut{
                    eq("code", "15")
                    //"in"("intitule",["EN COURS", "EXPIREE"])
                    //  }
                }
                
                le("date",dateEchPar)
                
                //                le("dateAlerte",dateD)
                //                or {
                //                    le("date",dateEchPar)
                //                }
                
                order("date","asc")
            
            }
            //  return result
        } else {
            return []
        }
    }
    
    def getEcheanceByClientEtProjet(client,projet){
        def criteria = Echeance.createCriteria()
        def result = criteria.list {
            phase{
                eq("client",client)
                eq("projet",projet)
            }
            statut {
                ne("intitule","ANNULEE")
            }
            order("date","asc")
        }
        return result
    }
    
    def getEcheanceCountByClientEtProjet(client,projet){
        def criteria = Echeance.createCriteria()
        def result = criteria.get {
            projections {
                //count "id"
                phase{
                    rowCount()
                    eq("client",client)
                    eq("projet",projet)
                }
                statut {
                    eq("intitule","EN COURS")
                }
            }
        }
        
        return result
    }
    
    def getEcheanceByPhase(Phase p){
        def criteria = Echeance.createCriteria()
        def result = criteria.list {
           
            eq("phase",p)                
            
            statut {
                not { 'in'("intitule",["PAYEE","ANNULEE"]) }
            }
            order("date","asc")
        }
        return result  
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
        
            if(idds.size() > 0 && iddsp.size() > 0 ) {
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
    
    def getEcheanceByUtilisateur(utilisateur,ofs = null,maxNb = null){
       
       
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
            def criteria = Echeance.createCriteria()
            def result = criteria.list {
                phase{
                    client {
                    "in"("id", idds)
                    }
                    projet {
                    "in"("id", iddsp)
                    }
                    //                    statut{
                    //                        eq("code","07")
                    //                    }
                }
                order("date","asc")
                if(ofs)
                firstResult(ofs)
                if(maxNb)
                maxResults(maxNb)
            }

        } else {
            return []
        }
        
    }
    
    def getEcheanceByPaiement(objetPaiement){
        def ids = objetPaiement.echeances*.id
        def idds = []

        ids.each {
            idds.addAll(it)
        }
        
        if(idds.size() > 0){
            def criteria = Echeance.createCriteria()
            def result = criteria.list {
                    "in"("id", idds)  
                order ("date","asc")
            }
        } else {
            return []
        }
    }
    
}