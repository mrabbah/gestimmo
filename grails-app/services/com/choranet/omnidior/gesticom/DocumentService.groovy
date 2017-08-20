
package com.choranet.omnidior.gesticom;


/**
 * TypeService Service pour la gestion des opérations
 * transactionnelles pour l'objet Type
 */
class DocumentService extends SuperService {

    static transactional = true

    def list() throws Exception {
        return super.list(Document.class)
    }
        
    def getByType(intitule){
        def criteria = Document.createCriteria()
        def result = criteria.list {
            eq("intitule",intitule)
            
        }
        return result 
    }
}