/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.choranet.omnidior.gesticom

import org.zkoss.zul.*
import org.zkoss.zkplus.databind.*
import org.apache.commons.logging.Log 
import org.apache.commons.logging.LogFactory
import org.springframework.web.context.request.RequestContextHolder
import org.zkoss.zk.ui.Executions
import cr.co.arquetipos.crypto.Blowfish 

/**
 *
 * @author rabbah
 */
class MonCompteWindow extends Window {
    
    private Log logger = LogFactory.getLog(MonCompteWindow.class)
    
    def utilisateurService
    
    def utilisateur
    
    def session
    
    def servletContext
    
    public MonCompteWindow() {
        servletContext = Executions.getCurrent().getDesktop().getWebApp()
        session = RequestContextHolder.currentRequestAttributes().getSession()
        utilisateur = session.utilisateur
    }
    
    def update() {
        try {
            if(utilisateur.pass != '[secret]') {
                utilisateur.passwd = Blowfish.encryptBase64(utilisateur.pass, servletContext.password)
            }
            utilisateur = utilisateurService.update(utilisateur)
            session.utilisateur = utilisateur
            Messagebox.show("Modifications effectuées avec succès")
        } catch(Exception ex) {
            logger.error(ex)
            Messagebox.show("Problème lors de la mise à jour, veillez contactez votre administrateur")
        }
        
    }
}

