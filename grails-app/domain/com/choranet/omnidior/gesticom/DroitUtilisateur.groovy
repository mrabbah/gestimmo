package com.choranet.omnidior.gesticom

import java.io.Serializable;

/**
 * Request Map domain class.
 */
class DroitUtilisateur implements Serializable {

    private static final long serialVersionUID = 1L;
    
    static auditable = true
    
    String droit
    String description
    DroitUtilisateur parent


    static constraints = {
        droit(blank: false, unique: true)
        description(nullable : true)
        parent(nullable: true)
    }
        
    static mapping = {
        parent lazy : false
    }
    
    String toString() {
        return description
    }

}
