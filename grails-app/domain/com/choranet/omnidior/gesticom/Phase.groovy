

package com.choranet.omnidior.gesticom


import java.io.Serializable;

/**
 * Phase Domain Object 
 */
class Phase implements Serializable {	
    private static final long serialVersionUID = 1L;
    
    static auditable = true
    
    String numPhase
    	
    String commentaire	
    	
    Date dateDebut	
    	
    Date dateFin	
    
    Boolean estExpire 	
    
    Boolean estAvecAffectation
    	     	
    Double montantVente	
    		 	
    String intitule
    
    Double totalPaiements
    
    Double trans_totalPaiements 
    
    Double montantReliquat
    
    Date dateContact
    
    String coproprietaireNomPrenom 
    String coproprietaireCIN
    String coproprietaireTel
    
    String idPhaseParent 
    Boolean estHistorique 
    
    static transients = ['trans_totalPaiements']
    static belongsTo = [bien : Bien , commercialResponsable : Utilisateur , notaire : Notaire , projet : Projet , client : Client, sourceInformation : SourceInformation , canal : Canal, statut : Statut]
    static hasMany = [documents: Document, echeances : Echeance]
    
    static mapping = { 
        numPhase index : "phase_numPhase"
        intitule index : "phase_intitule"
        
        bien lazy : false
        commercialResponsable lazy : false
        notaire lazy : false
        projet lazy : false
        client lazy : false
        sourceInformation lazy : false
        canal lazy : false
        statut lazy : false
        documents lazy : false
        echeances lazy : false

        batchSize: 14 
    }
   
    static constraints = {  
        
        numPhase(nullable : false, unique:true)
        
        commentaire(nullable : true, size : 0..3000)
    
        dateDebut(nullable : false)
    
        dateFin(nullable: true)
    
        estExpire()
    
        montantReliquat(min : 0d, nullable : true )
    
        montantVente(min : 0d,nullable : true)
    
        intitule(blank : false, nullable : false)
        
        totalPaiements(nullable : true)
        
        bien(nullable : true)
        
        commercialResponsable(nullable : true)
        
        notaire(nullable : true)
        
        sourceInformation(nullable : true)
        
        canal(nullable : true)
        
        dateContact(nullable : true)
        
        coproprietaireNomPrenom (nullable : true)
        
        coproprietaireCIN (nullable : true)
        
        coproprietaireTel (nullable : true)
        
        idPhaseParent (nullable : true)
        
        estHistorique (nullable : true)
        
        client (nullable : false)
        projet (nullable : false)
        statut (nullable : false)
        
    }

    def getTrans_totalPaiements() { 
//        totalPaiements = Paiement.createCriteria().get {
//            eq("phase", this)
//            statut{
//                eq("code","12")
//            }
//            projections {
//                sum("montant")
//            }
//            
//        }
//        if(totalPaiements == null) {
//            totalPaiements = 0;
//        }
//        return totalPaiements
    }   
    
//    def getTotalPaiements(){
//            totalPaiements = Paiement.createCriteria().get {
//            eq("phase", this)
//            statut{
//                eq("code","12")
//            }
//            projections {
//                sum("montant")
//            }
//            
//        }
//        if(totalPaiements == null) {
//            totalPaiements = 0;
//        }
//        return totalPaiements
//    }
    
    String toString() {
	return numPhase //+ " " + intitule
    }

}

