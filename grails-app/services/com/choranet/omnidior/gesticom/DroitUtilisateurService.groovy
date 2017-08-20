package com.choranet.omnidior.gesticom

class DroitUtilisateurService extends SuperService {

    static transactional = true

    def list() throws Exception {
        return super.list(DroitUtilisateur.class)
    }
    
    def getDroitsPrents() {
        def c = DroitUtilisateur.createCriteria()
        def result = c.list {
            isNull("parent")
        }
        return result
    }
    
    def getDroitsByParent(pere) {
        def c = DroitUtilisateur.createCriteria()
        def result = c.list {
            parent {
                eq("id", pere.id)
            }
        }
        return result
    }
    
    def getDroitByLabel(String label) {
        def c = DroitUtilisateur.createCriteria()
        def result = c.list {
            eq('droit',label)
        }
        return result
    }
}
