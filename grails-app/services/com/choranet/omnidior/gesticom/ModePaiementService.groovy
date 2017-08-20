
package com.choranet.omnidior.gesticom;


/**
 * modePaiementService Service pour la gestion des opérations
 * transactionnelles pour l'objet modePaiement
 */
class ModePaiementService extends SuperService {

        static transactional = true

        def list() throws Exception {
            return super.list(ModePaiement.class)
        }
}