
package com.choranet.omnidior.gesticom;


/**
 * SourceInformationService Service pour la gestion des opérations
 * transactionnelles pour l'objet SourceInformation
 */
class SourceInformationService extends SuperService {
    def sessionFactory
    static transactional = true

    def list() throws Exception {
        return super.list(SourceInformation.class)
    }
    
    def getSourceInformationByIntitule(String intitule){
        def criteria = SourceInformation.createCriteria()
        def result = criteria.get {          
            eq( "intitule",intitule)
        }
        return result
    }
    
    def getCountSourceInformationByProjet(String codeProjet ){
        def session = sessionFactory.getCurrentSession()
//        session.clear()  
        //def Qry = "select partenaire.raisonSociale as fournisseur,min(ligneProduit.prixDeduit) as prix from Livraison livraison join livraison.partenaire partenaire join livraison.bonLivraisons bl join bl.ligneProduits ligneProduit join ligneProduit.produit produit where bl.type = :typeTran and produit.code = :code group by partenaire.raisonSociale order by min(ligneProduit.prixDeduit)"
        def Qry = "select sourceInformation.intitule, count(distinct phase.id) as Nbre from Phase phase join phase.sourceInformation sourceInformation where phase.projet.code = :codeProjet group by sourceInformation.intitule order by count(distinct phase.id), sourceInformation.intitule asc"
        def q = session.createQuery(Qry)
        
        q.setString("codeProjet",codeProjet)
        //q.setMaxResults(4)
        def results = q.list()
        return results
    }
}