

package com.choranet.omnidior.securite

import java.io.Serializable;

/**
 * Security domain class
 */
class ChoraBarrage implements Serializable {
    	
    private static final long serialVersionUID = 1L;
    
    Date dateInstanciation;
    
    Date dernierAcces;
    
    boolean demo = true
    
    boolean premierAcces = true
    
    String raisonSocial
    
    String idInstance
    
    String produit
    
    String versionProduit
    
//    static mapping = { 
//        
//    }
    static constraints = {  
        dateInstanciation(nullable : false)
        dernierAcces(nullable : false)
        idInstance(nullable : false, blank: false)
        raisonSocial(nullable : false, blank: false)
        produit(nullable : false, blank: false)
        versionProduit(nullable : false, blank: false)
    }

}

