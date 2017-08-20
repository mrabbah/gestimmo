

package com.choranet.omnidior.gesticom


import java.io.Serializable;

/**
 * Notaire Domain Object 
 */
class Notaire implements Serializable {	
    private static final long serialVersionUID = 1L;
    	
    static auditable = true
    
    String adresse	
    	
    String fax	
    	
    String fixe	
    	
    String gsm	
    	
    String idFiscal	
    	    	
    String patente	
    
    String nom	
    	
    String prenom	
    	
    String email	
       		   
    
    static mapping = { 
        idFiscal index : "notaire_id_fiscal"
        patente index : "notaire_patente"
        batchSize: 14 
    }
    static constraints = {
    
        adresse(nullable : true)
    
        fax(nullable : true)
    
        fixe(nullable : true)
    
        gsm(nullable : true)
    
        idFiscal(blank : false, nullable : false, unique : true)
    
        nom(blank : false, nullable : false)
    
        patente(blank : false, nullable : false, unique : true)
    
        prenom(blank : false, nullable : false)
    
        email()
    
    }
	
    String toString() {
	return nom + " " + prenom +" -"+ idFiscal
    }
}

