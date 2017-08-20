
package com.choranet.omnidior.gesticom;


/**
 * CanalService Service pour la gestion des opérations
 * transactionnelles pour l'objet Canal
 */
class CanalService extends SuperService {

    static transactional = true

    def list() throws Exception {
        return super.list(Canal.class)
    }
        
    def getCanalByIntitule(String intitule){
        def criteria = Canal.createCriteria()
        def result = criteria.get {          
            eq( "intitule",intitule)
        }
        return result
    }
    
}