
package com.choranet.omnidior.gesticom;


/**
 * TypeService Service pour la gestion des opérations
 * transactionnelles pour l'objet Type
 */
class TypeService extends SuperService {

    static transactional = true

    def list() throws Exception {
        return super.list(Type.class)
    }
        
    def getByType(tp){
        def criteria = Type.createCriteria()
        def result = criteria.list {
            eq("type",tp)
            order("intitule","asc")  
        }

        return result 
    }
    
    def getByTypeEtIntitule(tp,intitule){
        def criteria = Type.createCriteria()
        def result = criteria.get {
            eq("type",tp)
            eq("intitule",intitule)
        }
        return result 
    }
    
    def getByCode(String code){
        def criteria = Type.createCriteria()
        def result = criteria.get {
            eq("code",code)
        }
        return result 
    }
    
    def getTypeByIntituleEtType(String intitule, String type){
        //return Type.findByIntitule(intitule)
        def criteria = Type.createCriteria()
        def result = criteria.get {
            eq("intitule",intitule)
            eq("type", type)
        }
        return result 
    }
    
}