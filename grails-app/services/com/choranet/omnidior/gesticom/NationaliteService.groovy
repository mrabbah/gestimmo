
package com.choranet.omnidior.gesticom;


/**
 * NationaliteService Service pour la gestion des opérations
 * transactionnelles pour l'objet Nationalite
 */
class NationaliteService extends SuperService {

    static transactional = true

    def list() throws Exception {
        return super.list(Nationalite.class)
    }
        
    def getDefault(){
       return Nationalite.findByIntitule("Marocaine") 
    }
        
    def getNationaliteByIntitule(String intitule){
       return Nationalite.findByIntitule(intitule)
    }
}