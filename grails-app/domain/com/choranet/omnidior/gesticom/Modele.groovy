

package com.choranet.omnidior.gesticom


import java.io.Serializable;

/**
 * Modele Domain Object 
 */
class Modele implements Serializable {	
    private static final long serialVersionUID = 1L;
    	
    String code
    
    static auditable = true    		   
    
    static mapping = { 
        code index : "modele_code"
        batchSize: 14 
    }
    static constraints = {
    
        code(blank : false, nullable : false, unique : true)
    
    }
	
    String toString() {
        return code
    }
}

