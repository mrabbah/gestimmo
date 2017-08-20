
package com.choranet.omnidior.gesticom;
import com.choranet.omnidior.gesticom.util.*

/**
 * NotaireService Service pour la gestion des opérations
 * transactionnelles pour l'objet Notaire
 */
class NotaireService extends SuperService {

    static transactional = true

    def list() throws Exception {
        return super.list(Notaire.class)
    }
}