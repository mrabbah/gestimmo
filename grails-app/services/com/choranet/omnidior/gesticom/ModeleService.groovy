/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.choranet.omnidior.gesticom

/**
 *
 * @author kamal
 */
class ModeleService  extends SuperService{
    static transactional = true

    def list() throws Exception {
        return super.list(Modele.class)
    }
    
}

