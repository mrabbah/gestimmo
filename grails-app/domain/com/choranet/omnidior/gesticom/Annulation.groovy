

package com.choranet.omnidior.gesticom


import java.io.Serializable;

/**
 * Annulation Domain Object 
 */
class Annulation implements Serializable {	
    
    private static final long serialVersionUID = 1L;
    
    static auditable = true
    
    String cause	
    	
    String commentaire	
    	
    Date date	
    	
    Double montantAnnule 	
    	
    Double montanPenalite 
                       
    static belongsTo = [phase : Phase]
    
    static mapping = { 
        date index : "annulation_date"
        phase lazy : false
        batchSize: 14 
    }
   
    static constraints = {
    
        cause(blank : false, nullable : false)
    
        commentaire(nullable : true, size : 0..3000)
    
        date(nullable : false)
    
        montantAnnule(min : 0d)
    
        montanPenalite(min : 0d)
        
        phase(nullable : false)
    
    }
	
    String toString() {
        return phase 
    }

}

