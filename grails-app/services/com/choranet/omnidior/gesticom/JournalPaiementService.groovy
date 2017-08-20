
package com.choranet.omnidior.gesticom;


/**
 * VilleService Service pour la gestion des opérations
 * transactionnelles pour l'objet Ville
 */
class JournalPaiementService extends SuperService {

        static transactional = true

        def list() throws Exception {
            return super.list(JournalPaiementService.class)
        }
}