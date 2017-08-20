package com.choranet.omnidior.gesticom


import java.io.Serializable;

/**
 * Alerte Domain Object 
 */
class Alerte implements Serializable {	
    private static final long serialVersionUID = 1L;
    
    static auditable = true
    
    Date date	
    	
    Date dateDeclenchement	
    	
    Date dateModification	
    	
    String messagePersonalise		
       		   
    
    static belongsTo = [projet : Projet, client : Client,statut : Statut]
    
    static mapping = { 
        date index : "alerte_date"
        dateDeclenchement index : "alerte_date_declenchement"
        projet lazy : false
        client lazy : false
        statut lazy : false
        batchSize: 14 
    }
    static constraints = {
    
        date(nullable : false)
    
        dateDeclenchement(nullable : false)
    
        dateModification(nullable : true)
    
        messagePersonalise(nullable : true)
    
    }
	
    String toString() {
        return super.toString()
    }
}

