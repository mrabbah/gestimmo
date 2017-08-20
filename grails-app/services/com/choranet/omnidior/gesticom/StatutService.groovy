
package com.choranet.omnidior.gesticom;


/**
 * PaysService Service pour la gestion des opérations
 * transactionnelles pour l'objet Pays
 */
class StatutService extends SuperService {

    static transactional = true

    def list() throws Exception {
        return super.list(Statut.class)
    }
        
    def getByType(t){
        
        def criteria = Statut.createCriteria()
        def result = criteria.list {
            eq("type", t)
            
        }
        return result 
    }
    
    def getByTypeEtIntitule(type,intitule){
             
        def criteria = Statut.createCriteria()
        def result = criteria.get {
            eq("type", type)
            eq("intitule",intitule)
        }
        return result   
        
    }
    
    def getByCode(String code) {

        return Statut.findByCode(code)
    }
    
    def getDefaultStatutClient(){
        return Statut.findByCode("23")
    }
    
    def getDefaultStatutPhase(){
        return Statut.findByCode("07") //phase encours
    }
    
    def getDefaultStatutBien(){
        return Statut.findByCode("04")
    }
    
    def getDefaultStatutPaiement(){
        return Statut.findByCode("11")
    }
        
}