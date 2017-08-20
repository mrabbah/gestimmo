
package com.choranet.omnidior.gesticom;


/**
 * VilleService Service pour la gestion des opérations
 * transactionnelles pour l'objet Ville
 */
class VilleService extends SuperService {

    static transactional = true

    def list() throws Exception {
        return super.list(Ville.class)
    }
        
    def getDefault(){
        return Ville.findByIntitule("Inconnue")
    }
    
    def getVilleByIntitule(String ville){
        def criteria = Ville.createCriteria()
        def result = criteria.get {
            
            eq("intitule",ville)
        }
        
        return result  
    }
}