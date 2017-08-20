

package com.choranet.omnidior.gesticom


import java.io.Serializable;

/**
 * Modele Domain Object 
 */
class ModeleType implements Serializable {	
    private static final long serialVersionUID = 1L;
    
    static auditable = true
        
    Modele modele
    Type type
    Integer numOrdre
   
    
    static mapping = {
        numOrdre index : "modeleType_numOrdre"
        batchSize: 14 
        modele lazy : false
        type lazy : false
    }
    static constraints = {
    
        numOrdre(nullable : false)
    
    }
	
    String toString() {
        return modele.code + " " + type.intitule + " " + numOrdre 
    }
}

