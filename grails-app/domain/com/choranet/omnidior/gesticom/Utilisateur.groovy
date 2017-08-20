package com.choranet.omnidior.gesticom

import java.io.Serializable;

/**
 * User domain class.
 */
class Utilisateur implements Serializable {
    private static final long serialVersionUID = 1L;
    
    static auditable = true
    
    static transients = ['pass']
    
    /** Username */
    String username
    /** User Real Name*/
    String userRealName
    /** MD5 Password */
    String passwd
    /** enabled */
    boolean enabled

    String email

    /** plain password to create a MD5 password */
    String pass = '[secret]'

    GroupeUtilisateur groupe
    
    static hasMany = [projets : Projet,clients :Client]
    
    static constraints = {
        username(blank: false, unique: true)
        userRealName(blank: false)
        passwd(blank: false)
        enabled()
        groupe(nullable : false)
    }

    static mapping = {
        groupe lazy : false
        projets lazy : false
        clients lazy : false
//        projets cascade:'all-delete-orphan'
//        clients cascade:'all-delete-orphan'
        groupeClients : false
        batchSize: 14
    }
    String toString() {
        return username
    }
}
