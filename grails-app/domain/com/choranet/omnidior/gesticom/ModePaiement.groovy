

package com.choranet.omnidior.gesticom


import java.io.Serializable;

/**
 * modePaiement Domain Object 
 */
class ModePaiement implements Serializable {	
    private static final long serialVersionUID = 1L;
    	
    String type	
       
    static auditable = true
    
    static mapping = { 
        type index : "modepaiement_type"
        batchSize: 14 
    }
    static constraints = {
    type (nullable : false,unique : true)
        //type(inList:["CHEQUE", "VERSEMENT", "ESPECE"])
    
    }
	
    String toString() {
        return type
    }
}

