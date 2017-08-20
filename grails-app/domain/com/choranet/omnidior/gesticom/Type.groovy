

package com.choranet.omnidior.gesticom


import java.io.Serializable;

/**
 * Type Domain Object 
 */
class Type implements Serializable {	
    private static final long serialVersionUID = 1L;
    	
    static auditable = true
    
    String intitule	   
    String type
    
    static mapping = { 
        intitule index : "type_intitule"
        type index : "type_type"
        batchSize: 14 
    }
    static constraints = {
    
        intitule(blank : false, nullable : false)
    
    }
	
    String toString() {
        return intitule
    }
}

