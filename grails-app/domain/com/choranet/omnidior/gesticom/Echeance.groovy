

package com.choranet.omnidior.gesticom


import java.io.Serializable;
import java.text.SimpleDateFormat  

/**
 * Echeance Domain Object 
 */
class Echeance implements Serializable {	
    private static final long serialVersionUID = 1L;
    	
    static auditable = true
    
    Date date	
    
    Date dateAlerte 
    	
    Double montantEcheance	
    	
    Double montantPaye	
    	
    String commentaire	
    
    String intitulePhaseEcheance
    
    String projet
    String client
    String bien
    
    static belongsTo = [phase : Phase, statut : Statut]
    
    static mapping = { 
        date index : "echeance_date"
        intitulePhaseEcheance index : "echeance_intitulePhaseEcheance"
        phase lazy : false
        statut lazy : false
        batchSize: 14 
    }
    static constraints = {
    
        date(nullable : false)
        dateAlerte (nullable : false)
        statut()    
        montantEcheance(min : 0d)    
        montantPaye(nullab : true, min : 0d)    
        commentaire(nullable : true, size : 0..3000)
        intitulePhaseEcheance(nullable : true)
        projet(nullable : true)        
        client(nullable : true)
        bien(nullable : true)
    
    }
	
    String toString() {
        def sdf = new SimpleDateFormat("dd/MM/yyyy")  
        return sdf.format(date) + "- " + montantEcheance
    }
    
}

