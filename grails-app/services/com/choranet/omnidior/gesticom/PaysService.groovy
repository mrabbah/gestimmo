
package com.choranet.omnidior.gesticom;


/**
 * PaysService Service pour la gestion des opérations
 * transactionnelles pour l'objet Pays
 */
class PaysService extends SuperService {

    static transactional = true

    def list() throws Exception {
        return super.list(Pays.class)
    }
        
    def getByIntitule(String intitule) {
        def criteria = Pays.createCriteria()
        def result = criteria.get {
            eq("intitule", intitule)
        }
        return result   
    }
}