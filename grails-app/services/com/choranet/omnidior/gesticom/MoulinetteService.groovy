package com.choranet.omnidior.gesticom

import org.apache.commons.logging.Log 
import org.apache.commons.logging.LogFactory

class MoulinetteService {

    private Log logger = LogFactory.getLog(MoulinetteService.class)
    static transactional = true

    def utilisateurService
    def clientService
    def bienService
    
    def nettoyerAffectationClientUtilisateur() {
        def projets = Projet.list()
        for(projet in projets) {
            def utilisateurs = utilisateurService.getUtilisateursParProjet(projet) 
            def clients = clientService.getClientsProjet(projet)
            for(utilisateur in utilisateurs) {
                for(client in clients) {
                    utilisateur.addToClients(client)
                }                
                utilisateurService.update(utilisateur)
            }
        }
    }
    
    def getEtatBien(typePhase){
        def etatBien
        switch (typePhase){
            case "OPTION":
            etatBien = "25"//"En option"
            break
            case "RESERVATION":
            etatBien = "05"//"Réservé"
            break
            case "COMPROMIS":
            etatBien = "05"//"Réservé"
            break
            case "CONTRAT":
            etatBien = "06"//"Vendu"
            case "LIBRE" :
            etatBien = "04" // Libre
            break
        }        
        return Statut.findByCode(etatBien)
    }
    
    def miseAjourStatusBienPhase() {
        def biens = Bien.list()
        def biensParents = []
        for(bien in biens) {
            def biensFils = Bien.findAllByBienParent(bien)
            if(biensFils.size() == 0) {
                if(bien.statut.code.equals("30")) {
                    continue;    
                }
                def phases = Phase.findAllByBien(bien)
                def size =  phases.size()
                if(size > 0) {
                    def ambigue = false
                    def phase_retenu = phases[0]
                    for(int compt = 1; compt < size; compt++) {
                        if(phases[compt].dateDebut > phase_retenu.dateDebut) {
                            phase_retenu = phases[compt]
                        } else if(phases[compt].dateDebut == phase_retenu.dateDebut) {
                            ambigue = true
                            break
                        }
                    }
                    if(!ambigue) {
                        def annulation = Annulation.findByPhase(phase_retenu)
                        if(annulation) {
                            bien.statut = getEtatBien("LIBRE")
                        } else {
                            bien.statut = getEtatBien(phase_retenu.intitule)
                        }
                    }
                } else {
                    bien.statut = getEtatBien("LIBRE")                
                }
                bienService.update(bien)
            } else { 
                biensParents.add(bien)
            }           
        }
        //hona updateEtatBienParent
        for(bp in biensParents) {
            bienService.updateEtatBienParent(bp)
        }
    }
}
