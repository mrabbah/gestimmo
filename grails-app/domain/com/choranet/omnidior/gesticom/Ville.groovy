

package com.choranet.omnidior.gesticom


import java.io.Serializable;

/**
 * Ville Domain Object 
 */
class Ville implements Serializable {	
    private static final long serialVersionUID = 1L;
    	
    static auditable = true
    
    String intitule	
       		   
    
    static belongsTo = [pays : Pays]
    
    static mapping = { 
        intitule index : "ville_intitule"
        pays lazy : false
        batchSize: 14 
    }
    static constraints = {
    
        intitule(blank : false, nullable : false)
    
    }
	
    String toString() {
        return intitule
    }
}

