

package com.choranet.omnidior.gesticom


import java.io.Serializable;

/**
 * Projet Domain Object 
 */
class Projet implements Serializable {	
    private static final long serialVersionUID = 1L;
    	
    static auditable = true
    
    String adresse	
    	
    String designation	
    
    String code
    		
    Boolean visible = true
        
    Integer delaiOption
    Integer delaiReservation 
    Integer delaiCompromis 
    Integer delaiContrat 
    
    String plan
    
    Boolean reservationEnLigne 
    
    Boolean generationOptionEnLigne 
    
    static belongsTo = [type : Type, ville : Ville, modele : Modele, statut: Statut, groupeClient : ChoraClientInfo]    
    
    static mapping = { 
        code index : "projet_code"

        type lazy : false
        ville lazy : false
        modele lazy : false
        statut lazy : false
        groupeClient lazy : false

        batchSize: 14 
    }
   
    static constraints = {
    
        adresse(nullable : true)
    
        designation(blank : false, nullable : false)
        
        code(blank : false, nullable : false, unique : true)
        
        visible(nullable : false)
        
        delaiOption(nullable : true) 
        delaiReservation(nullable : true) 
        delaiCompromis(nullable : true) 
        delaiContrat(nullable : true) 
        
        plan(nullable : true) 
        
        reservationEnLigne (nullable : false)
        
        generationOptionEnLigne (nullable : false)
                    
    }
	
    String toString() {
        return code + "-" + designation
    }
}

