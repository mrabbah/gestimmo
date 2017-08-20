

package com.choranet.omnidior.gesticom


import java.io.Serializable;

/**
 * SourceInformation Domain Object 
 */
class SourceInformation implements Serializable {	
    private static final long serialVersionUID = 1L;
    	
    static auditable = true
    
    String intitule	
       		   
    
    static mapping = { 
        intitule index : "sourceinformation_intitule"
        batchSize: 14 
    }
    static constraints = {    
        intitule(blank : false, nullable : false, unique : true)    
    }	
    String toString() {
	return intitule
    }
}

