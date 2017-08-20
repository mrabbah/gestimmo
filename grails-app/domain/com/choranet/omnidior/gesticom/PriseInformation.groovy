

package com.choranet.omnidior.gesticom


import java.io.Serializable;

/**
 * PriseInformation Domain Object 
 */
class PriseInformation implements Serializable {	
    private static final long serialVersionUID = 1L;
    	
    static auditable = true
    
    Date date	
       		       
    static belongsTo = [sourceInformation : SourceInformation , canal : Canal , projet : Projet , client : Client]
    
    static mapping = { 
        date index : "priseinformation_date"
        sourceInformation lazy : false
        canal lazy : false
        projet lazy : false
        client lazy : false
        batchSize: 14 
    }
    static constraints = {
    
        date(nullable : false)
    
    }
	
    String toString() {
        return date
    }
}

