

package com.choranet.omnidior.gesticom


import java.io.Serializable;

/**
 * Contenance Domain Object 
 */
class Contenance implements Serializable {	
    private static final long serialVersionUID = 1L;
    	
    static auditable = true
    
    String intitule	
       		   
    
    static belongsTo = [type : Type]
    
    static mapping = { 
        intitule index : "contenance_intitule"
        type lazy : false
        batchSize: 14 
    }
    static constraints = {
    
        intitule(blank : false, nullable : false)
    
    }
	
    String toString() {
        return intitule
    }
}

