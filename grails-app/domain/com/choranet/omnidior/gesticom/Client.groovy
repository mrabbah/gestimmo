

package com.choranet.omnidior.gesticom


import java.io.Serializable;

/**
 * Client Domain Object 
 */
class Client implements Serializable {	
    private static final long serialVersionUID = 1L;
    	
    static auditable = true
    
    String adresse	
    	
    String cin	
    	
    String commentaire	
    	
    Date dateContact	
    	
    Date dateModification	
    	
    Date dateNaissance	
    	
    String fixe	
    	
    String gsm	
    
    String mail
    	
    String nom	
    	
    String prenom	
    	
    String sexe	
       		   
    static belongsTo = [nationalite : Nationalite , ville : Ville, statut : Statut]
    
    static mapping = { 
        cin index : "client_cin"
        nationalite lazy : false
        ville lazy : false
        statut lazy : false
        batchSize: 14 
    }
    
    static constraints = {
    
        adresse(nullable : true)
    
        cin(unique : true,nullable : true)
    
        commentaire(nullable : true, size : 0..3000)
    
        dateContact(nullable : true)
    
        dateModification()
    
        dateNaissance(nullable : true)
    
        fixe(nullable : true)
    
        gsm(blank : false, nullable : false)
        
        mail(nullable : true)
    
        nom(blank : false, nullable : false)
    
        prenom(blank : false, nullable : false)
    
        sexe(inList:["F","M","INCONNU"])
        
        statut(nullable : false)
    
    }
	
    String toString() {
        return nom + " " + prenom + " -" + cin
    }

}

