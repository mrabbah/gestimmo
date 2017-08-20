

package com.choranet.omnidior.gesticom


import java.io.Serializable;

/**
 * Pays Domain Object 
 */
class Pays implements Serializable {	
	private static final long serialVersionUID = 1L;
    
    static auditable = true
    
    String intitule	
       		   
    
    static mapping = { 
intitule index : "pays_intitule"
 batchSize: 14 
}
    static constraints = {
    
        intitule(blank : false, nullable : false, unique : true)
    
    }
	
	String toString() {
	return intitule
	}
}

