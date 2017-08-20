package com.choranet.omnidior.gesticom

import java.io.Serializable;

/**
 * Authority domain class.
 */
class GroupeUtilisateur implements Serializable {

    private static final long serialVersionUID = 1L;
    
    static hasMany = [droits: DroitUtilisateur]
    
    static auditable = true
    
    /** description */
    String description
   
    String intitule

    static constraints = {
        intitule(blank: false, unique: true)
        description()
        droits(nullable : true)
        
    }
    
    static mapping = {
        droits lazy : false
    }
        
    String toString() {
        return description
    }
}
