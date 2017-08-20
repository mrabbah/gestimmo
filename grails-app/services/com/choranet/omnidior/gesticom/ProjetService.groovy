
package com.choranet.omnidior.gesticom;

import com.choranet.omnidior.gesticom.util.ProjectTreeNodeCollection;
import com.choranet.omnidior.gesticom.util.BienData;
import com.choranet.omnidior.gesticom.util.ProjectTreeNode;
import org.hibernate.ScrollableResults
import org.springframework.web.context.request.RequestContextHolder

import org.zkoss.zul.DefaultTreeModel;
import org.zkoss.zul.TreeModel;
import org.zkoss.zul.TreeNode;

/**
 * ProjetService Service pour la gestion des opérations
 * transactionnelles pour l'objet Projet
 */
class ProjetService extends SuperService {

    static transactional = true

    def bienService 
    def statutService
    def modeleTypeService
   
    def sessionFactory
    
    def session
    
    def list() throws Exception {
        return super.list(Projet.class)
    }

    def getProjetTreeRoot(projetcode) {
        
        def biens = bienService.getBienByProjet(projetcode)
        def result = addNode(biens)
        return result
    }
    
    def addNode(biens){
        
        def biensCollection = new ProjectTreeNodeCollection<BienData>() 
                                    
        biens.each
        { bien ->
            biensCollection.add(new ProjectTreeNode<BienData>(new BienData(bien.numero, bien.statut.intitule)));
        }
                        
        ProjectTreeNode<BienData> root;
        root = new ProjectTreeNode<BienData>(null,biensCollection, true)
        
        //        
        //        ProjectTreeNode root = new ProjectTreeNode()
        //        
        //        biens.each
        //        { bien ->
        //            root.childern.add(new BienData(bien.numero, bien.statut))
        //        }
        //        ProjectTreeNode mtm = new ProjectTreeNode(root)
        
        return root
    }
    
    def dernierNiveauProjet(Projet projet){
        def modele = projet.modele
        def listmodeleType = modeleTypeService.getTypeByModele(modele)
        if (listmodeleType && listmodeleType.size()>0){            
            return listmodeleType[listmodeleType.size() - 1]
        }
        else{
            return null
        }
    }
    
    def getSituationProjet(projet){
        def session = sessionFactory.getCurrentSession()        
        session.clear()  
        
        ModeleType mt = dernierNiveauProjet(projet)
        Long idType = mt.type.id        
               
        def Qry = "select bien.statut.intitule, count(*) as Nbre from Bien bien where bien.projet.code = :code and bien.type.id = :idType group by bien.statut.intitule"
        def q = session.createQuery(Qry)
        
        q.setString("code",projet.code)
        q.setLong("idType",idType)
        //q.setMaxResults(5)
        def results = q.list()
        return results
    }
    
    def getSituationProjetPhase(projet){
        def session = sessionFactory.getCurrentSession()
        session.clear()  
        //def Qry = "select partenaire.raisonSociale as fournisseur,min(ligneProduit.prixDeduit) as prix from Livraison livraison join livraison.partenaire partenaire join livraison.bonLivraisons bl join bl.ligneProduits ligneProduit join ligneProduit.produit produit where bl.type = :typeTran and produit.code = :code group by partenaire.raisonSociale order by min(ligneProduit.prixDeduit)"
        def Qry = "select phase.intitule, count(*) as Nbre from Phase phase where phase.projet.code = :code group by phase.intitule"
        def q = session.createQuery(Qry)
        
        q.setString("code",projet.code)
        //q.setMaxResults(5)
        def results = q.list()
        return results
    }    
    
    def updateEtatProjet(Projet p){
        def biens = bienService.getAllBienByProjet(p.code)
        def estEnExec  = false
        def estCloture = true
        
        if (biens && biens.size()> 0)
        {
        for(it in biens){
            if (it.statut.intitule != "Vendu"){
                estCloture = false
            }
            else if(it.statut.intitule == "Réservé" || it.statut.intitule == "En option" ){
                estEnExec = true
            }
        }
        }
        
        if(estCloture){
            //p.statut = statutService.getByTypeEtIntitule("PROJET","Clôturé")
             p.statut = statutService.getByCode('03')//("PROJET","Clôturé")
        }
        else if(!estCloture && estEnExec){
            //p.statut = statutService.getByTypeEtIntitule("PROJET","En execution")
            p.statut = statutService.getByCode('02')//("PROJET","En execution")
        }
        
        update(p)
        
    }
    
    def getNombreClientProspectParProjet(Long idProjet,String intituleClient){
        def session = sessionFactory.getCurrentSession()
        session.clear()  
        def Qry = "select count(client) as nombre from Phase phase join phase.projet projet join phase.client client  where projet.id = :idProjet and  client.statut.intitule = :intituleClient"
        def q = session.createQuery(Qry)
        q.setLong("idProjet",idProjet)
        q.setString("intituleClient",intituleClient)
        q.setMaxResults(1)
        def results = q.list()        
        return results
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
            

            if(idds.size() > 0 ) {
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
            def ids = utilisateur.projets*.id
            def idds = []
            ids.each {
                idds.addAll(it)
            }
            
            if(idds.size() > 0 ) {
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
    
    def getProjetByUtilisateur(utilisateur,ofs = null,maxNb = null){
       
        def ids = utilisateur.projets*.id
        def idds = []
        ids.each {
            idds.addAll(it)
        }
        if(idds.size() > 0) {
            def criteria = Projet.createCriteria()
            def result = criteria.list {
                
                    "in"("id", idds)
                order("code","asc")
                if(ofs)
                firstResult(ofs)
                if(maxNb)
                maxResults(maxNb)
            }

        } else {
            return []
        }
    }    
    
    def removeFromProjetCrit(idProjet){

        def session = sessionFactory.getCurrentSession()
        session.clear()  
        //def Qry = "select partenaire.raisonSociale as fournisseur,min(ligneProduit.prixDeduit) as prix from Livraison livraison join livraison.partenaire partenaire join livraison.bonLivraisons bl join bl.ligneProduits ligneProduit join ligneProduit.produit produit where bl.type = :typeTran and produit.code = :code group by partenaire.raisonSociale order by min(ligneProduit.prixDeduit)"
        def Qry = "delete from utilisateur_projet where projet_id= :idProjet "
        def q = session.createQuery(Qry)        
        q.setString("idProjet",idProjet)
        q.executeUpdate()
        
    } 
    
    def getUtilisateurParProjet(Projet projet){
    
        def lstUtilisateur = []
        def utilisateurs = Utilisateur.list()
        for (it in utilisateurs){
            if (it.projets*.id.contains(projet.id)){
                lstUtilisateur.add(it) 
            }
        }
    
        return lstUtilisateur
    }
    
    def getProjetByCode(String code){
        return Projet.findByCode(code)
    }

}