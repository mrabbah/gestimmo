

package com.choranet.omnidior.gesticom


import java.io.Serializable;

/**
 * Pays Domain Object 
 */
class Statut implements Serializable {	
    private static final long serialVersionUID = 1L;
    
    static auditable = true
    
    String code
    String intitule	
    String type
       		   
    
    static mapping = { 
        code index : "statut_code"
        intitule index : "statut_intitule"
        type index : "statut_type"
        batchSize: 14 
    }
    static constraints = {
    
        code(unique : true, nullable : false)
        intitule(blank : false, nullable : false)
        type(blank : false, nullable : false)
    
    }
	
    String toString() {
	return intitule
    }
}

