
package com.choranet.omnidior.gesticom;


/**
 * ParametrageService Service pour la gestion des opérations
 * transactionnelles pour l'objet Parametrage
 */
class ParametrageService extends SuperService {

        static transactional = true

        def list() throws Exception {
            return super.list(Parametrage.class)
        }
}