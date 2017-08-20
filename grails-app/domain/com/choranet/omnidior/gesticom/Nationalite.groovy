

package com.choranet.omnidior.gesticom


import java.io.Serializable;

/**
 * Nationalite Domain Object 
 */
class Nationalite implements Serializable {	
	private static final long serialVersionUID = 1L;
    	
    String intitule	
       		   
    static auditable = true
    
    static mapping = { 
intitule index : "nationalite_intitule"
 batchSize: 14 
}
    static constraints = {
    
        intitule(blank : false, nullable : false, unique : true)
    
    }
	
	String toString() {
		return intitule
	}
}

