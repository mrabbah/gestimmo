package com.choranet.commun

import com.choranet.omnidior.gesticom.SuperService
import com.choranet.omnidior.gesticom.ChoraClientInfo
import java.util.Date
import cr.co.arquetipos.password.PasswordTools
import cr.co.arquetipos.crypto.Blowfish 
import java.text.SimpleDateFormat



class ChoraBarrageService extends SuperService {

    static transactional = true
    
    def grailsApplication
    
    def list() throws Exception {
        return super.list(ChoraBarrage.class)
    }
   
    def isDemo() {
        def cb = getChoraBarrage()
        if(!cb.demo) {
            return false
        }
        def today = new Date()
        def nbJoursDemo = today - cb.dateInstanciation
        if(nbJoursDemo > 30) {
            cb.demo = false  
            update(cb);
            return false
        }
        return true
    }

    def getChoraBarrage() {
        return ChoraBarrage.list()[0]
    }
}
