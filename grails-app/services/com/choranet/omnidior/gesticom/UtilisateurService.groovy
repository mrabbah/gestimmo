package com.choranet.omnidior.gesticom

import org.apache.commons.logging.Log 
import org.apache.commons.logging.LogFactory

class UtilisateurService extends SuperService {

    private Log logger = LogFactory.getLog(UtilisateurService.class)
    
    static transactional = true

    
    def list() throws Exception {
        return super.list(Utilisateur.class)
    }
    
    def chercherUtilisateur(login, psswd) {
        def c = Utilisateur.createCriteria()
        def user = c.get {
            eq('username', login)
            eq('passwd', psswd)
            eq('enabled', true)
        }
        return user
    } 

    def getUtilisateursDuGroupe(grp) {
        def c = Utilisateur.createCriteria()
        def result = c.list {
            groupe {
                eq("id", grp.id)
            }
        }
        return result
    }
    
    def getUtilisateursParProjet(grp) {
        //def idd=Utilisateur.projets()
        def c = Utilisateur.createCriteria()
        def result = c.list {
            projets {
                eq("id", grp.id)
            }
        }
        return result
    }
}
