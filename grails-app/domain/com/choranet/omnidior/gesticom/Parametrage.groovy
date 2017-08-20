

package com.choranet.omnidior.gesticom


import java.io.Serializable;

/**
 * Parametrage Domain Object 
 */
class Parametrage implements Serializable {	
    private static final long serialVersionUID = 1L;
    	
    static auditable = true
        
    Integer echeanceDelaiJours	
    	       		                              
    static mapping = { 
        batchSize: 14 
    }
    static constraints = {
            
        echeanceDelaiJours(min : 1)  
        
    }
	
    String toString() {
        return super.toString()
    }
}

