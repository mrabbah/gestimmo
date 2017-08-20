

package com.choranet.omnidior.gesticom


import java.io.Serializable;

/**
 * Ville Domain Object 
 */
class Authentification implements Serializable {	
    private static final long serialVersionUID = 1L;
    	
    static auditable = true
    
    String username
    /** MD5 Password */
    String passwd
    String adresseMac       		           
    
    static mapping = { 
        intitule index : "username"
        batchSize: 14 
    }
    
    static constraints = {
    
        username(username : false, nullable : false)
        passwd(passwd : false, nullable : false)
        adresseMac(adresseMac : false, nullable : false)        
    
    }
	
    String toString() {
        return username + " - " + adresseMac
    }
}

