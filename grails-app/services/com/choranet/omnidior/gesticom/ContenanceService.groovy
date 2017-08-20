
package com.choranet.omnidior.gesticom;


/**
 * ContenanceService Service pour la gestion des opérations
 * transactionnelles pour l'objet Contenance
 */
class ContenanceService extends SuperService {

        static transactional = true

        def list() throws Exception {
            return super.list(Contenance.class)
        }
        
        def getContenancecByType(type) {
            //return Contenance.findAllByType(type)
        }
}