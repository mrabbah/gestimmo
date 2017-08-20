package com.choranet.omnidior.gesticom

import org.zkoss.zul.*
import org.zkoss.zk.ui.Executions
import org.zkoss.zkplus.databind.*
import org.apache.commons.logging.Log 
import org.apache.commons.logging.LogFactory

/**
 * Backup restore Window
 **/
//@Secured(['ROLE_ROOT'])
class BackupRestoreWindow extends Window {
    
    def backupRestoreService
    def listeDesSauveguardes
    def sauveguardeSelectionee
    /**
     * Logger de la class ArticleWindow
     **/
    private Log logger = LogFactory.getLog(BackupRestoreWindow.class)

    /**
     * Constructeur
     **/
    public BackupRestoreWindow (backupRestoreService) throws Exception {
        this.backupRestoreService = backupRestoreService
        listeDesSauveguardes = backupRestoreService.getListeDesSauveguardes()
    }   
    
    def restaurerDb() {
        if(sauveguardeSelectionee != null) {
            try {
                backupRestoreService.restaurerDb(sauveguardeSelectionee)
                Executions.sendRedirect("/logout.zul"); 
            } catch(Exception ex) {

            }
            
        }
    }
    
    def sauveguarderBdD() {
        backupRestoreService.sauveguarderBdD()
        listeDesSauveguardes = backupRestoreService.getListeDesSauveguardes()
        def binder = new AnnotateDataBinder(this.getFellow("lbsauveguardes"))
        binder.loadAll()
    }
    
}

