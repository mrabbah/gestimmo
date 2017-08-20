
import com.choranet.omnidior.gesticom.*;	
// Place your Spring DSL code here

beans = {
    
    utilisateurWindow(UtilisateurWindow) { bean ->
        bean.scope = "prototype"
        bean.autowire = "byName"
    }
    
    monCompteWindow(MonCompteWindow) { bean ->
        bean.scope = "prototype"
        bean.autowire = "byName"
    }
    
    groupeUtilisateurWindow(GroupeUtilisateurWindow, ref("droitUtilisateurService")) { bean ->
        bean.scope = "prototype"
        bean.autowire = "byName"
    }
    
    loginWindow(LoginWindow) { bean ->
        bean.scope = "prototype"
        bean.autowire = "byName"
    }
    
    mainWindow(MainWindow) { bean ->
        bean.scope = "prototype"
        bean.autowire = "byName"
    }
    
    choraClientInfoWindow(ChoraClientInfoWindow) { bean ->
        bean.scope = "prototype"
        bean.autowire = "byName"
    }
    
    backupRestoreWindow(BackupRestoreWindow, ref("backupRestoreService")) { bean ->
        bean.scope = "prototype"
        bean.autowire = "byName"
    }
    
    parametrageWindow(ParametrageWindow) { bean ->
        bean.scope = "prototype"
        bean.autowire = "byName"
    }
    
    priseInformationWindow(PriseInformationWindow,ref("projetService"),ref("clientService"),ref("priseInformationService")) { bean ->
        bean.scope = "prototype"
        bean.autowire = "byName"
    }
    
    bienWindow(BienWindow,ref("statutService"),ref("typeService"),ref("projetService"),ref("bienService")) { bean ->
        bean.scope = "prototype"
        bean.autowire = "byName"
    }
    
    modePaiementWindow(ModePaiementWindow) { bean ->
        bean.scope = "prototype"
        bean.autowire = "byName"
    }
    
    canalWindow(CanalWindow) { bean ->
        bean.scope = "prototype"
        bean.autowire = "byName"
    }
    
    notaireWindow(NotaireWindow) { bean ->
        bean.scope = "prototype"
        bean.autowire = "byName"
    }
    
    annulationWindow(AnnulationWindow,ref("projetService"),ref("clientService"),ref("phaseService"),ref("annulationService")) { bean ->
        bean.scope = "prototype"
        bean.autowire = "byName"
    }
    
    paysWindow(PaysWindow) { bean ->
        bean.scope = "prototype"
        bean.autowire = "byName"
    }
    
    alerteWindow(AlerteWindow,ref("statutService"),ref("projetService"),ref("clientService"),ref("phaseService"),ref("alerteService")) { bean ->
        bean.scope = "prototype"
        bean.autowire = "byName"
    }
    
    projetWindow(ProjetWindow,ref("statutService"),ref("typeService"),ref("projetService")) { bean ->
        bean.scope = "prototype"
        bean.autowire = "byName"
    }
    
    typeWindow(TypeWindow) { bean ->
        bean.scope = "prototype"
        bean.autowire = "byName"
    }
    
    clientWindow(ClientWindow,ref("clientService"),ref("statutService")) { bean ->
        bean.scope = "prototype"
        bean.autowire = "byName"
    }
    
    
    phaseWindow(PhaseWindow,ref("phaseService"),ref("statutService"),ref("projetService"),ref("clientService"), ref("bienService")) { bean ->
        bean.scope = "prototype"
        bean.autowire = "byName"
    }
    
    sourceInformationWindow(SourceInformationWindow) { bean ->
        bean.scope = "prototype"
        bean.autowire = "byName"
    }
    
    echeanceWindow(EcheanceWindow,ref("statutService"),ref("projetService"),ref("clientService"),ref('phaseService'),ref("echeanceService")) { bean ->
        bean.scope = "prototype"
        bean.autowire = "byName"
    }
    
    contenanceWindow(ContenanceWindow) { bean ->
        bean.scope = "prototype"
        bean.autowire = "byName"
    }
    
    villeWindow(VilleWindow) { bean ->
        bean.scope = "prototype"
        bean.autowire = "byName"
    }
    
    paiementWindow(PaiementWindow,ref("statutService"),ref("echeanceService"),ref("projetService"),ref("clientService"),ref("phaseService"),ref("paiementService")) { bean ->
        bean.scope = "prototype"
        bean.autowire = "byName"
    }
    
    nationaliteWindow(NationaliteWindow) { bean ->
        bean.scope = "prototype"
        bean.autowire = "byName"
    }
    
    modeleTypeWindow(ModeleTypeWindow) { bean ->
        bean.scope = "prototype"
        bean.autowire = "byName"
    }
    
    modeleWindow(ModeleWindow) { bean ->
        bean.scope = "prototype"
        bean.autowire = "byName"
    }
            
    tdbWindow(TdbWindow,ref("projetService"),ref("alerteService"),ref("paiementService"),ref("echeanceService"),ref("phaseService"),ref("statutService"),ref("typeService")) { bean ->
        bean.scope = "prototype"
        bean.autowire = "byName"
    }
    
    utilisateurProjetClientWindow(UtilisateurProjetClientWindow)  { bean ->
        bean.scope = "prototype"
        bean.autowire = "byName"
    }
    
    statutWindow(StatutWindow)  { bean ->
        bean.scope = "prototype"
        bean.autowire = "byName"
    }
    
    auditLogEventWindow(AuditLogEventWindow)  { bean ->
        bean.scope = "prototype"
        bean.autowire = "byName"
    }
    
    bienEnLigneWindow(BienEnLigneWindow,ref("bienService"))  { bean ->
        bean.scope = "prototype"
        bean.autowire = "byName"
    }
    
}