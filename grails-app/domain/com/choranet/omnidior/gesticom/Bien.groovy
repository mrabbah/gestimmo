

package com.choranet.omnidior.gesticom


import java.io.Serializable;

/**
 * Bien Domain Object 
 */
class Bien implements Serializable {	
    private static final long serialVersionUID = 1L;
    	
    static auditable = true    
    	
    String numero
    
    String trans_code
    	
    Double prixVente	
    	
    Double superficieConstruite	
    	
    Double superficieVendable	
    	
    String titreFoncier	
    	
    Double superficie	
    
    String contenance
       		   
    String coordonnees
    
    String typeCoordonnees
    
    String client
    
    static transients = ['trans_code']
    
    static belongsTo = [projet : Projet, type : Type , bienParent : Bien , statut : Statut]
    
    static mapping = { 
        numero index : "bien_numero"
        type lazy : false
        bienParent lazy : false
        projet lazy : false
        statut lazy : false
        batchSize: 14 
    }
    static constraints = {
        numero(nullable : false, blank : false)
        prixVente(min : 0d, nullable : true)
        superficieConstruite(nullable : true)
        superficieVendable(nullable : true)
        titreFoncier(nullable : true)
        contenance(nullable : true)
        bienParent(nullable : true)
        superficie(min : 0d, nullable : true)
        projet(nullable: false)
        type(nullable: false)
        statut(nullable: false)
        coordonnees(nullable: true)
        typeCoordonnees(nullable: true)
        client(nullable: true)
    }
	
    String toString() {
//        String toString = type.intitule + ":" + numero
//        if (bienParent){
//            toString = bienParent.type.intitule + " : " + bienParent.numero + " - "  + toString 
//        }
        return numero //toString
    }

    public String getTrans_code() {
        if(!bienParent) {
            return numero
        } else {
            return (bienParent.trans_code + numero)
        }
    }
}

