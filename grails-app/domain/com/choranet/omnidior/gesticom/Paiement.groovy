

package com.choranet.omnidior.gesticom


import java.io.Serializable;

/**
 * Paiement Domain Object 
 */
class Paiement implements Serializable {	
    private static final long serialVersionUID = 1L;
    	
    static auditable = true
    
    String commentaire	
    	
    Date dateEncaissement	
    	
    Date datePaiement	
    	
    Double montant	
    	
    String numeroRecu	
    	
    String banque	
    	
    String numeroBordereau	
    
    Integer numeroPaiement  
    
    String intitulePhasePaiement 
    
    String projet
    String client
    String bien
    
    static belongsTo = [modePaiement : ModePaiement, phase : Phase ,type : Statut, statut : Statut]
    
    static hasMany = [echeances : Echeance]
    
    static mapping = { 
        datePaiement index : "paiement_date_paiement"
        numeroPaiement index :"paiement_numeroPaiement"
        intitulePhasePaiement index : "paiement_intitulePhasePaiement"

        modePaiement lazy : false        
        phase lazy : false
        type lazy : false
        statut lazy : false
        echeances lazy : false
        batchSize: 14 
    }
    
    static constraints = {
    
        commentaire(nullable : true, size : 0..3000)
    
        dateEncaissement(nullable : true)
    
        datePaiement(nullable : false)
    
        montant(min : 0d)
    
        numeroRecu(nullable : true)
    
        banque(nullable : true)
    
        numeroBordereau(nullable : true)
        
        numeroPaiement(nullable : true)
        
        intitulePhasePaiement (nullable : false)
        
        projet(nullable : true)
        
        client(nullable : true)
        
        bien (nullable : true)
        
        modePaiement(nullable : false)
        phase(nullable : false)
        type(nullable : false)
        statut (nullable : false)
    }
	
    String toString() {
        super.toString()
    }
    
}

