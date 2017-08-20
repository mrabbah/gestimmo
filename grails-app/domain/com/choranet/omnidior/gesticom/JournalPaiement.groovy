

package com.choranet.omnidior.gesticom


import java.io.IOException
import org.zkoss.image.AImage
import org.zkoss.image.Image
import com.choranet.omnidior.gesticom.util.Utilitaire
import java.io.Serializable;

/**
 * Type Domain Object 
 */
class JournalPaiement implements Serializable {	
    private static final long serialVersionUID = 1L;
    	
    Double montantPaye	  
    Paiement paiement
    Echeance echeance
    
    static mapping = { 
        montantPaye index : "journalPaiement_montantPaye"   
        paiement index : "journalPaiement_paiement"
        echeance index : "journalPaiement_echeance"
        batchSize: 14 
    }
   
    static constraints = {
        //    
        montantPaye()
        paiement()
        echeance()
        
    }
	
    String toString() {
        return paiement + "- " + echeance + "- " + " Montant : " +  montantPaye
    }
    
}