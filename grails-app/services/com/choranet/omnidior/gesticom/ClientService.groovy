
package com.choranet.omnidior.gesticom;

import org.springframework.web.context.request.RequestContextHolder
import groovy.sql.Sql
/**
 * ClientService Service pour la gestion des opérations
 * transactionnelles pour l'objet Client
 */

class ClientService extends SuperService {
    
    static transactional = true
    def sessionFactory
    
    def list() throws Exception {
        return super.list(Client.class)    
    }
   
    //    def getClientByUtilisateur(){
    //        session = RequestContextHolder.currentRequestAttributes().getSession()
    //        def utilisateur = session.utilisateur 
    //        return  utilisateur.clients.toList()
    //    }
    //    
    def getClientByUtilisateur(utilisateur,ofs = null,maxNb = null){
       
        def ids = utilisateur.clients*.id
        def idds = []
        ids.each {
            idds.addAll(it)
        }
        if(idds.size() > 0) {
            def criteria = Client.createCriteria()
            def result = criteria.list {
                
                    "in"("id", idds)
                
                if(ofs)
                firstResult(ofs)
                if(maxNb){
                    maxResults(maxNb)
                }
                order("nom","asc")
                order("prenom","asc")
                order("cin","asc")
            }

        } else {
            return []
        }
    }

    def getClientByUtilisateurEtType(type,ofs = null,maxNb = null){
       
        //        def ids = utilisateur.clients*.id
        //        def idds = []
        //        ids.each {
        //            idds.addAll(it)
        //        }
        //        if(idds.size() > 0) {
        def criteria = Client.createCriteria()
        def result = criteria.list {
                
            //                    "in"("id", idds)
            statut{
                eq("code",type.code)
            }
                
            if(ofs)
            firstResult(ofs)
            if(maxNb){
                maxResults(maxNb)
            }
            order("nom","asc")
            order("prenom","asc")
            order("cin","asc")
        }
            
        return result

        //        } else {
        //            return []
        //        }
    }
    
    def getClientByUtilisateurEtProjet(projet,ofs = null,maxNb = null){
       
        //        def ids = utilisateur.clients*.id
        //        def idds = []
        //        ids.each {
        //            idds.addAll(it)
        //        }
        //        if(idds.size() > 0) {
        def criteria = Phase.createCriteria()
        def result = criteria.list {
            eq("projet",projet)
            //                client{
            //                    "in"("id", idds)
            //                }
            if(ofs)
            firstResult(ofs)
            if(maxNb){
                maxResults(maxNb)
            }
                
            client{
                order("nom","asc")
                order("prenom","asc")
                order("cin","asc")
            }
        }
        result = result*.client.unique() 

        //        } else {
        //            return []
        //        }
    }   
    
    def getClientsProjet(projet) {
        def criteria = Phase.createCriteria()
        def result = criteria.list {
            eq("projet",projet)
        }
        return result*.client.unique() 
    }
    
    def getCilentsNonAffectesPhase() {
        def result = []
        def clients = Client.list()
//        def criteria = Phase.createCriteria()
        for(client in clients) {
            def nbPhase = Phase.findAllByClient(client)
//            criteria.list {
//                eq("client", client)
//            }  
            if(nbPhase.size() == 0) {
                result.add(client)
            }
        } 
        println result
        println result.size()
        return result
    }
    
    def getClientByUtilisateurEtProjetEtType(projet,statut,ofs = null,maxNb = null){
       
        def criteria = Phase.createCriteria()
        def result = criteria.list {
            eq("projet",projet)
            client {
                eq("statut",statut)
            }
            if(ofs)
            firstResult(ofs)
            if(maxNb){
                maxResults(maxNb)
            }
                
            client{
                order("nom","asc")
                order("prenom","asc")
                order("cin","asc")
            }
        }
        result = result*.client.unique() 

        //        } else {
        //            return []
        //        }
    }       
    
    def getClientByUtilisateurEtProjetEtClient(projet,client,ofs = null,maxNb = null){

        def criteria = Phase.createCriteria()
        def result = criteria.list {
            eq("projet",projet)
            eq("client",client)
            
                
            if(ofs)
            firstResult(ofs)
            if(maxNb){
                maxResults(maxNb)
            }

        }
        result = result*.client.unique()  
        return result
   
    }      
    
    def getClientByUtilisateurEtProjetEtSource(projet,sourceInformation,ofs = null,maxNb = null){
       
        //        def ids = utilisateur.clients*.id
        //        def idds = []
        //        ids.each {
        //            idds.addAll(it)
        //        }

        //        if(idds.size() > 0) {
        def criteria = Phase.createCriteria()
        def result = criteria.list {
            eq("projet",projet)
  
               
            eq("sourceInformation",sourceInformation)
                
                
            if(ofs)
            firstResult(ofs)
            if(maxNb){
                maxResults(maxNb)
            }
                
            client{               
                order("nom","asc")
                order("prenom","asc")
                order("cin","asc")
            }
        }
        result = result*.client.unique()  

        //        } else {
        //            return []
        //        }
    }   
    
    def getClientByUtilisateurEtAgeEtSexe(Double age1, Double age2, String sexe){
        def clients = Client.list()
        Date ajourdhui = new Date()
        Boolean valide = true
        Double ageCl
        def result = []
        if (clients && clients.size()>0){

            for (it in clients){
                
                if(it.sexe != sexe ){
                    valide = false
                }
                
                if (it.dateNaissance){
                    ageCl = (ajourdhui - it.dateNaissance)/365
                    if(ageCl < age1 || ageCl > age2){
                        valide = false
                    }
                }
                else{
                    valide = false
                }
                
                if (valide){
                    result.add(it)
                }

                valide = true
            }
          
        }

        return result
    }    
    
    def getClientBySexe(String sexe){
        def criteria = Client.createCriteria()
        def result = criteria.list {          
            eq( "sexe",sexe)
        }
        return result

    }    
        
    def getClientByUtilisateurEtVille(utilisateur,ville){
        
        def criteria = Client.createCriteria()
        def result = criteria.list {
            
            eq("ville",ville)
        }
        
        return result  
  
        
        //        def clients = Client.list() //utilisateur.clients.toList()
        //        def result = []
        //        if (clients && clients.size()>0){
        //
        //            for (it in clients){                
        //
        //                if (it.ville.intitule == ville.intitule){
        //                    result.add(it)
        //                }
        //
        //            }
        //          
        //        }
        //
        //        return result
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

                "in"("id", idds)
     
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

                    "in"("id", idds)

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

                    "in"("id", idds)

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

                    "in"("id", idds)

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
    
    def getCountClientVille(Long idVille){
        def session = sessionFactory.getCurrentSession()

        String Qry = "select client.ville.intitule, count(distinct client.id) as Nbre from Client client group by client.ville.intitule order by count(distinct client.id) desc"

        def q = session.createQuery(Qry)
        //q.setLong("idVille",idVille)
        def results = q.list()
        return results
    }
    
    def getClientByCIN(String CIN){
        //return Client.findByCin(CIN)
        def criteria = Client.createCriteria()
        def result = criteria.get {
            
            eq("cin",CIN)
        }
        
        return result  
    }
    
    def getClientById(Long id){
        //return Client.findByCin(CIN)
        def criteria = Client.createCriteria()
        def result = criteria.list {
            
            eq("id",id)
        }
        
        return result  
    }
    
        def deleteClientAssociation(clientId){
        //change connection url
        String qry= "DELETE FROM utilisateur_client WHERE client_id = " + clientId
        def sql = Sql.newInstance("jdbc:postgresql://localhost/gestimmo", "root", "Omnidior2012", "org.postgresql.Driver")    
        //def sql = Sql.newInstance("jdbc:postgresql://localhost/gestimmo20131105", "root", "root", "org.postgresql.Driver")    
        println(qry)
        int rowsAffected = sql.executeUpdate(qry)


    }
}