
package com.choranet.omnidior.gesticom;


/**
 * BienService Service pour la gestion des opérations
 * transactionnelles pour l'objet Bien
 */
class BienService extends SuperService {

    static transactional = true
    def statutService
    def list() throws Exception {
        return super.list(Bien.class)
    }
        
    def partialSave(bien, projet, type, bienParent, statut) {
        bien.projet = projet
        bien.type = type
        bien.bienParent = bienParent
        //        bien.contenance = contenance
        bien.statut = statut
        return save(bien)
    }
    
    def getBienRootProjet(pj) { 
        def criteria = Bien.createCriteria()
        def result = criteria.list {
            isNull("bienParent")
            projet {
                eq("code", pj.code)
            }
        }
        return result
    }
    
    def getBienByProjet(projetcode){
    
        def criteria = Bien.createCriteria()
        def result = criteria.list {
            
            isNull("bienParent")
            projet {
                eq("code",projetcode)
            }
        }
        return result 
    }
    
    def getBienByBienParent (bienparent){
        def criteria = Bien.createCriteria()
        def result = criteria.list {
            eq("bienParent",bienparent)
        }
        return result 
    }
    
    def getBienByProjetType(projetcode, intituleType){
    
        def criteria = Bien.createCriteria()
        def result = criteria.list {
            
            
            projet {
                eq("code",projetcode)
            }
            type {
                eq("intitule",intituleType)
            }
        }
        return result 
    }
    
    def getAllBienByProjet(projetcode){
    
        def criteria = Bien.createCriteria()
        def result = criteria.list {
            
            projet {
                eq("code",projetcode)
            }
        }
        return result 
    }

    def getBienByProjets(projets){
       
        def ids = projets*.id
        def idds = []
        ids.each {
            idds.addAll(it)
        }
        if(idds.size() > 0) {
            def criteria = Bien.createCriteria()
            def result = criteria.list {
                projet {
                    "in"("id", idds)
                }
            }

        } else {
            return []
        }
        
    }
    
    def getBienByUtilisateur(utilisateur, ofs =null,maxNb = null){
       
        def ids = utilisateur.projets*.id
        def idds = []
        ids.each {
            idds.addAll(it)
        }
        if(idds.size() > 0) {
            def criteria = Bien.createCriteria()
            def result = criteria.list {
                projet {
                    "in"("id", idds)
                }
                if(ofs)
                firstResult(ofs)
                if(maxNb)
                maxResults(maxNb)
            }

        } else {
            return []
        }
        
    }
        
    def filtrer(clazz, filtre, sortedHeader, sortedDirection, ofs, maxNb,utilisateur) throws Exception {
        try {
            def listeObjets 
            def tailleListe = 0
            def fields = clazz.getDeclaredFields()
            def requete = clazz.createCriteria()
            def attributToSort
            def selectionnerDistinctResultat = false
            def ids = utilisateur.projets*.id
            def idds = []
            ids.each {
                idds.addAll(it)
            }
        
            if(idds.size() > 0) {
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
                                        ilike(nomAttribut, "%" + valeurAttribut)                                
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
                    projet {
                    "in"("id", idds)
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
                                        ilike(nomAttribut, "%" + valeurAttribut)                                
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
                    projet {
                    "in"("id", idds)
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
            def ids = utilisateur.projets*.id
            def idds = []
            ids.each {
                idds.addAll(it)
            }
            
            if(idds.size() > 0) {
                listeObjets = requete.list {
                    for(nomAttribut in attributsAFiltrer) {
                        def valeurAttribut = filtre."$nomAttribut"
                    
                        if(sortedHeader == "h"+nomAttribut) {
                            attributToSort = nomAttribut 
                        }
                        if(valeurAttribut != null) {
                            if(valeurAttribut instanceof String) {
                                if(valeurAttribut != "") {
                                    ilike(nomAttribut, "%" + valeurAttribut)                                
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
                    projet {
                    "in"("id", idds)
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
                                    ilike(nomAttribut, "%" + valeurAttribut)                                
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
                    projet {
                    "in"("id", idds)
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
    
    def getBiensByProjetAndType(projet, type) {
        def c = Bien.createCriteria()
        def r = c.list {
            eq("projet", projet)
            eq("type", type)
            order('bienParent','asc')
            order('numero','asc')
            
        }
        return r
    }
    
    def existeBienCode(Bien bien,projetCode,update = null){

        def criteria = Bien.createCriteria()
        def result = criteria.get {
            projections{
                count("id")
            }
            eq("numero",bien.numero)
            if(update){
                ne("id",bien.id)
            }
            projet{
                eq("code",projetCode)
            }
        }

        if(!result || result == 0 ){
            return false
        }
        else if(result > 0){
            return true
        }
    }
    
    def updateEtatBienParent(bp){
        def criteria = Bien.createCriteria()
        def result = criteria.list {
            eq("bienParent",bp)
        }
        def cptLibre = 0
        def cptOption = 0 
        def cptReserve = 0 
        def cptVendu = 0 
        def s;
        if(result && result.size()> 0){
            for (b in result){
                switch (b.statut.intitule){
                    case "LIBRE":
                    cptLibre +=1
                    break
                    
                    case "EN OPTION" : 
                    cptOption +=1
                    break 
                    
                    case "RESERVE" : 
                    cptReserve +=1
                    break 
                    
                    case "VENDU" : 
                    cptVendu +=1
                    break 
                }
            }
            if(cptLibre > 0) {
                s = "LIBRE"
            } else if(cptOption > 0) {
                s = "EN OPTION"
            } else if(cptReserve > 0) {
                s = "RESERVE"
            } else if(cptVendu > 0) {
                s = "VENDU"
            }

            if(!bp.statut.intitule.equals(s)) {
                bp.statut = statutService.getByTypeEtIntitule("BIEN",s)
                bp = update(bp)
                if(bp.bienParent != null) {
                    updateEtatBienParent(bp.bienParent)
                }
            }
            
        }
    }
    
    def existeBienProjet(Projet objetProjet){
        def criteria = Bien.createCriteria()
        def result = criteria.get {
            projections{
                count("id")
            }
            eq("projet",objetProjet)
        }
        if (result){
            return result
        }
        else
        return 0
    }
    
    def getBienByProjetAndCode(String codeProjet, String codeBien){
        def criteria = Bien.createCriteria()
        def result = criteria.get {           
            projet{
                eq("code",codeProjet)
            }       
            eq("numero",codeBien)
        }

        return result
    }
    
    def filtrerBiens(bParent,bienParentsSelected, 
        stat,
        projet, type) {
        def c = Bien.createCriteria()
        def idds = []        
        if(bienParentsSelected) {
            def ids = bienParentsSelected*.id
            ids.each {
                idds.addAll(it)
            }
        }        
        def r = c.list {
            if(bParent) {
                eq("bienParent", bParent)
            } else if(bienParentsSelected) {
                bienParent {
                    "in"("id", idds)
                }                
            }
            eq("projet", projet)
            eq("type", type)
            
            if(stat) {
                statut {
                    ilike("intitule", "%" + stat + "%")
                }
            }

        }
        return r
    }   
    
    def filtrerBiens(bParent,bienParentsSelected, numero, cont, prixVente, superficieConstruite
        , SuperficieVendable, superficie, titreFoncier, stat, attributToSort
        , sortedDirection, projet, type) {
        def c = Bien.createCriteria()
        def idds = []        
        if(bienParentsSelected) {
            def ids = bienParentsSelected*.id
            ids.each {
                idds.addAll(it)
            }
        }        
        def r = c.list {
            if(bParent) {
                eq("bienParent", bParent)
            } else if(bienParentsSelected) {
                bienParent {
                    "in"("id", idds)
                }                
            }
            eq("projet", projet)
            eq("type", type)
            if(numero) {
                ilike("numero", "%" + numero + "%")
            }
            if(cont) {
                contenance {
                    ilike("intitule", "%" + cont + "%")
                }
            }
            if(prixVente) {
                eq("prixVente", prixVente)
            }
            if(superficieConstruite) {
                eq("superficieConstruite", superficieConstruite)
            }
            if(SuperficieVendable) {
                eq("superficieVendable", SuperficieVendable)
            }
            if(superficie) {
                eq("superficie", superficie)
            }
            if(titreFoncier) {
                ilike("titreFoncier", "%" + titreFoncier + "%")
            }
            if(stat) {
                statut {
                    ilike("intitule", "%" + stat + "%")
                }
            }
            if(attributToSort != null) {
                order(attributToSort, sortedDirection)
            }
        }
        return r
    }   
    
    def getBienByCodeProjetAndNum(String codeProjet, String numeroBien){
        def criteria = Bien.createCriteria()
        def result = criteria.get {
            eq("numero", numeroBien)
            projet{
                eq("code",codeProjet)
            }
        }
        
        return result
    }
        
}    
