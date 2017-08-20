package com.choranet.omnidior.gesticom

class PlanController {

    Projet projet
    List biens

    def index = {
        def codeProjet = params.cp
        if(codeProjet) {
            projet = Projet.findByCode(codeProjet)
            if(projet) {                
                biens = Bien.findAllByProjet(projet)                
            } else {
                flash.message = "Projet avec code : ${params.cp} inexistant!"
            }
        } else {
            flash.message = "Vous devriez passer le code projet!"
        }
       
    }
    
    def reserver = {
        def codeProjet = params.cp
        def codeBien = params.cb
        def clientInstance = new Client()
        //clientInstance.properties = params
        return [clientInstance: clientInstance]
    }
    
    def save = {
        def clientInstance = new Client(params)
        if (clientInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'client.label', default: 'Client'), clientInstance.id])}"
            redirect(action: "show", id: clientInstance.id)
        }
        else {
            render(view: "create", model: [clientInstance: clientInstance])
        }
    }
}
