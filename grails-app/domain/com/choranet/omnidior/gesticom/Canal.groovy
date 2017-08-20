

package com.choranet.omnidior.gesticom


import java.io.Serializable;

/**
 * Canal Domain Object 
 */
class Canal implements Serializable {	
    private static final long serialVersionUID = 1L;
    	
    String intitule	
       		
    static auditable = true
    
    static mapping = { 
        intitule index : "canal_intitule"
        batchSize: 14 
    }
    static constraints = {
    
        intitule(blank : false, nullable : false)
    
    }
	
    String toString() {
        return intitule
    }
}

