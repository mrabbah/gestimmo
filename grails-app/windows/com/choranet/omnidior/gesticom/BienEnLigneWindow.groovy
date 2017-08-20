
package com.choranet.omnidior.gesticom
    

import org.zkoss.zul.*
import org.zkoss.zkplus.databind.*
import org.apache.commons.logging.Log 
import org.apache.commons.logging.LogFactory
import org.codehaus.groovy.grails.plugins.jasper.JasperExportFormat
import org.codehaus.groovy.grails.plugins.jasper.JasperReportDef

import org.zkoss.util.media.AMedia;
import org.zkoss.util.media.Media;
import java.util.HashMap;
import java.util.Map;
import org.zkoss.zk.ui.Executions


/**
 * SourceInformation Window Object
 **/
class BienEnLigneWindow extends Window {
    /**
     * Service pour la gestion de l'objet SourceInformation
     **/
    def bienService
    def mailService
    def phaseService
    def clientService
    def statutService
    def projetService
    def utilisateurService
    def excelImporterService
    
    Bien bien
    Client client
    
    def nationalites	
    def nationaliteSelected
    def villes	
    def villeSelected
    def sexes = ['F','M']
    Boolean valideClient
    Boolean execDemande = true
    
    
    
    public BienEnLigneWindow (bienService) {
        String codeProjet = Executions.getCurrent().getParameter("cp")
        String codeBien = Executions.getCurrent().getParameter("cb")
        
        if (codeProjet && codeBien ){
            bien = bienService.getBienByProjetAndCode( codeProjet, codeBien)
        }
        //bien = bienService.getBienByProjetAndCode( "BA001", "N2") //: données de test
        initialiserAssociation() 
    }  

    protected SuperService getService() {
        return this.bienService
    }

    def initialiserAssociation() {
        client = new Client()
        client.sexe = sexes[0]
         
        nationalites = Nationalite.list()		
        if(nationalites.size() > 0)
        nationaliteSelected = nationalites.get(0)
        else
        nationaliteSelected = null
                    
        villes = Ville.list()		
        if(villes.size() > 0)
        villeSelected = villes.get(0)
        else
        villeSelected = null           
                                    
    }
    def actualiserValeurAssociation() {        		
        client.nationalite = nationaliteSelected                    		
        client.ville = villeSelected    
    }
    
    def envoiMail(){                
        Boolean lancerExec = true
        execDemande = true
        if (!bien.projet.reservationEnLigne){
            lancerExec = false 
            execDemande = false
            Messagebox.show("La réservation en ligne est desactivée, merci de contacter la direction", "Alerte", Messagebox.OK, Messagebox.INFORMATION)
        }
        Boolean valideClient = validerClient()
        if(!valideClient){
            lancerExec = false       
            execDemande = false
        }
        lancerExec
        if(lancerExec){
            actualiserValeurAssociation()
            def subjectMail = "Réservation en ligne [" + bien.projet.designation + " : " + bien.numero + "]"
            def contentMail = """<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<title>HTML Form</title>

<style type="text/css">

* { margin: 0; padding: 0; }

html { height: 100%; font-size: 62.5% }

body { height: 100%; background-color: #FFFFFF; font: 1.2em Verdana, Arial, Helvetica, sans-serif; }


/* ==================== Form style sheet ==================== */

form { margin: 25px 0 0 29px; width: 450px; padding-bottom: 30px; }

fieldset { margin: 0 0 22px 0; border: 1px solid #2B6600; padding: 12px 17px; background-color: #DAF5C7; }
legend { font-size: 1.1em; background-color: #2B6600; color: #FFFFFF; font-weight: bold; padding: 4px 8px; }

label.float { float: left; display: block; width: 100px; margin: 4px 0 0 0; clear: left; }
label { display: block; width: auto; margin: 0 0 10px 0; }
label.spam-protection { display: inline; width: auto; margin: 0; }

input.inp-text, textarea, input.choose, input.answer { border: 1px solid #909090; padding: 3px; }
input.inp-text { width: 300px; margin: 0 0 8px 0; }
textarea { width: 400px; height: 150px; margin: 0 0 12px 0; display: block; }

input.choose { margin: 0 2px 0 0; }
input.answer { width: 40px; margin: 0 0 0 10px; }
input.submit-button { font: 1.4em Georgia, "Times New Roman", Times, serif; letter-spacing: 1px; display: block; margin: 23px 0 0 0; }

form br { display: none; }

/* ==================== Form style sheet END ==================== */

</style>

<!--[if IE]>
<style type="text/css">

/* ==================== Form style sheet for IE ==================== */

fieldset { padding: 22px 17px 12px 17px; position: relative; margin: 12px 0 34px 0; }
legend { position: absolute; top: -12px; left: 10px; }
label.float { margin: 5px 0 0 0; }
label { margin: 0 0 5px 0; }
label.spam-protection { display: inline; width: auto; position: relative; top: -3px; }
input.choose { border: 0; margin: 0; }
input.submit-button { margin: -10px 0 0 0; }

/* ==================== Form style sheet for IE end ==================== */

</style>
<![endif]-->
</head>


<body>

	<form >
		<span style="font-size: 12pt ;color: rgb(57, 57, 57);">
		Nouvelle option en ligne : 

		</span> 
                <p>_  </p>
		<p>_  </p>
		<!-- ============================== Fieldset 1 ============================== -->
		<fieldset>
			<legend>Information bien:</legend>
			<table>
				<tr valign="center">
				<td>
				<label for="input-one" class="float"><strong>Projet: </strong></label>
				</td>
				<td>
				<input class="inp-text" name="input-one-name" id="input-one" type="text" size="30" value=" """ + bien.projet.designation + """ "/>
				</td>
				</tr>
				<tr valign="center">
				<td >
				<label for="input-two" class="float">Type bien: </label>
				</td>
				<td>
				<input class="inp-text" name="input-two-name"  id="input-two" type="text" size="30" value= " """ + bien.type + """ "/> 
				</td>
				<td align="right">
				<label for="input-two" class="float">Code bien: </label> 
				</td>
				<td>
				<input class="inp-text" name="input-two-name"  id="input-two" type="text" size="30" value= " """ + bien.numero + """ " />
				</td>
				</tr>
				<tr valign="center">
				<td >
				<label for="input-two" class="float">Superficie construite: </label>
				</td>
				<td>
				<input class="inp-text" name="input-two-name"  id="input-two" type="text" size="30" value = " """ + bien.superficieConstruite + """ "/> 
				</td>
				<td align="right">
				<label for="input-two" class="float">Superficie vendable: </label> 
				</td>
				<td>
				<input class="inp-text" name="input-two-name"  id="input-two" type="text" size="30" value=" """ + bien.superficieVendable +""" "/>
				</td>
				</tr>
				<tr valign="center">
				<td >
				<label for="input-two" class="float">Superficie: </label>
				</td>
				<td>
				<input class="inp-text" name="input-two-name"  id="input-two" type="text" size="30" value= " """ + bien.superficie + """ "/> 
				</td>
				<td align="right">
				<label for="input-two" class="float">Prix de vente: </label> 
				</td>
				<td>
				<input class="inp-text" name="input-two-name"  id="input-two" type="text" size="30" value = " """ + bien.prixVente + """ "/>
				</td>
				</tr>
				<tr valign="center">
				<td >
				<label for="input-two" class="float">Contenance: </label>
				</td>
				<td>
				<input class="inp-text" name="input-two-name"  id="input-two" type="text" size="30" value = " """ + bien.contenance + """ " /> 
				</td>
				<td align="right">
				<label for="input-two" class="float">Etat bien: </label> 
				</td>
				<td>
				<input class="inp-text" name="input-two-name"  id="input-two" type="text" size="30" value = " """ + bien.statut + """ "/>
				</td>
				</tr>
				</table>				
		</fieldset>
		<!-- ============================== Fieldset 1 end ============================== -->


		<!-- ============================== Fieldset 2 ============================== -->
		<fieldset>
			<legend>Information client:</legend>		
				<table>
				<tr valign="center">
				<td>
				<label for="input-one" class="float">CIN: </label>
				</td>
				<td>
				<input class="inp-text" name="input-one-name" id="input-one" type="text" size="30" value=" """ + client.cin + """ "/>
				</td>
				</tr>
				<tr valign="center">
				<td >
				<label for="input-two" class="float">Nom: </label>
				</td>
				<td>
				<input class="inp-text" name="input-two-name"  id="input-two" type="text" size="30" value = " """ +client.nom + """ "/> 
				</td>
				<td align="right">
				<label for="input-two" class="float">Prénom: </label> 
				</td>
				<td>
				<input class="inp-text" name="input-two-name"  id="input-two" type="text" size="30" value=" """ + client.prenom + """ "/>
				</td>
				</tr>
				<tr valign="center">
				<td >
				<label for="input-two" class="float">Date de naissance: </label>
				</td>
				<td>
				<input class="inp-text" name="input-two-name"  id="input-two" type="text" size="30" value=" """ + client.dateNaissance + """ "/> 
				</td>
				<td align="right">
				<label for="input-two" class="float">Sexe: </label> 
				</td>
				<td>
				<input class="inp-text" name="input-two-name"  id="input-two" type="text" size="30" value=" """ + client.sexe + """ " />
				</td>
				<tr valign="center">
				<td >
				<label for="input-two" class="float">Fixe: </label>
				</td>
				<td>
				<input class="inp-text" name="input-two-name"  id="input-two" type="text" size="30" value=" """ + client.fixe + """ "/> 
				</td>
				<td align="right">
				<label for="input-two" class="float">GSM: </label> 
				</td>
				<td>
				<input class="inp-text" name="input-two-name"  id="input-two" type="text" size="30" value = " """ + client.gsm + """ "/>
				</td>
				</tr>
				<tr valign="center">
				<td >
				<label for="input-two" class="float">E-mail: </label>
				</td>
				<td>
				<input class="inp-text" name="input-two-name"  id="input-two" type="text" size="30" value=" """ + client.mail + """ "/> 
				</td>
				<td align="right">
				<label for="input-two" class="float">Date contact: </label> 
				</td>
				<td >
				<input class="inp-text" name="input-two-name"  id="input-two" type="text" size="30" value=" """ + client.dateContact + """ "/>
				</td>
				</tr>
				<tr valign="center">
				<td >
				<label for="input-two" class="float">Ville: </label>
				</td>
				<td>
				<input class="inp-text" name="input-two-name"  id="input-two" type="text" size="30" value=" """ + client.ville + """ "/> 
				</td>
				<td align="right">
				<label for="input-two" class="float">Nationalité: </label> 
				</td>
				<td>
				<input class="inp-text" name="input-two-name"  id="input-two" type="text" size="30" value = " """ + client.nationalite + """ "/>
				</td>
				</tr>
				<tr valign="center">
				<td>
				<label for="input-one" class="float">Adresse: </label>
				</td>
				<td colspan="3">
				<input class="inp-text" name="input-one-name" id="input-one" type="text" size="30" value = " """ + client.adresse + """ "/>
				</td>
				</tr>
				<tr valign="center">
				<td>
				<label for="input-one" class="float">Commentaire: </label>
				</td>
				<td colspan="3">
				<input class="inp-text" name="input-one-name" id="input-one" type="text" size="30" value =" """ + client.commentaire + """ "/>
				</td>
				</tr>
				</table>	
			</fieldset>
		<!-- ============================== Fieldset 2 end ============================== -->
		<span style="font-size: 9pt ;color: rgb(57, 57, 57);">
		Ceci est un mail automatique, Merci de ne pas y répondre.
		</span> 
                </br>  
		<span style="font-size: 9pt ;color: rgb(57, 57, 57);">
		--Équipe technique.
		</span> 
	</form>

</body>
</html>
"""
            try{
                def mailTo = genererListeAdresse(bien.projet)
                String mailToAddresse 
                for (it in mailTo){
                    mailToAddresse = '"' + it + '"'
                    envoyerMail (mailToAddresse,subjectMail,contentMail)
                }
            } catch(Exception ex) {
                execDemande = false
                logger.error "Error: ${ex.message}", ex
                Messagebox.show("Echec lors de la transaction\n" , "Erreur", Messagebox.OK, Messagebox.ERROR)
            }
            if(bien.projet.generationOptionEnLigne){
                genererPhase()
            }
            
            //            if (execDemande){
            //                closeWin()
            //            }
            //Messagebox.show("Réservation en ligne terminée avec succès", "Information", Messagebox.OK, Messagebox.INFORMATION)             

        }

    }
    
    def genererPhase (){
        client.statut = statutService.getByCode("23") 
        client = addClient()
        if(valideClient){
            phaseService.genererOptionAutomatique(bien,client)
        }
    }
    
    def addClient(){
        Boolean valideClient  = validerClient()
        if (valideClient){

            try {
                client.dateModification = new Date()
                client = clientService.save(client)     
                def utilisateurs = projetService.getUtilisateurParProjet(bien.projet)
                for (it in utilisateurs){
                    it = it.addToClients(client)
                    it = utilisateurService.update(it)
                }
                return client
            
            } catch(Exception ex) {
                valideClient = false
                logger.error "Error: ${ex.message}", ex
                if (ex.message.contains("com.choranet.omnidior.gesticom.Client.cin.unique.error")){
                    Messagebox.show("Le CIN [" + client.cin + "] existe déjà", "Erreur", Messagebox.OK, Messagebox.ERROR)  
                }
                else{
                    Messagebox.show("Echec lors de la transaction\n" + ex.message, "Erreur", Messagebox.OK, Messagebox.ERROR)
                }
            } finally {
               
            }    
        }

    }
    
    def validerClient(){
        valideClient = true
        def listErreurs = []
        if(!client.nom || client.nom.length() == 0 ){
            valideClient = false
            //Messagebox.show("Veuillez saisir un nom pour le client" , "Erreur", Messagebox.OK, Messagebox.ERROR)
            listErreurs.add("* Saisir un nom pour le client")
        }
        
        if(!client.prenom || client.prenom.length() == 0 ){
            valideClient = false
            //Messagebox.show("Veuillez saisir un prénom pour le client" , "Erreur", Messagebox.OK, Messagebox.ERROR)
            listErreurs.add("* Saisir un prénom pour le client")
        }
        
        if (!minLength(client.gsm,9)){ 
            valideClient = false
            //Messagebox.show("Le numéro de téléphone doit être composé de 9 chiffres [XXXXXXXXX]" , "Erreur", Messagebox.OK, Messagebox.ERROR)
            listErreurs.add("* Saisir un numéro de téléphone composé de 9 chiffres [XXXXXXXXX]")
        
        }
                              
        //        if(client.fixe){
        //            if(client.fixe.length() >0 ){
        //                if(!minLength(client.fixe, 9)){
        //                    valide = false
        //                    Messagebox.show("Le numéro de fixe doit être composé de 9 chiffres [XXXXXXXXX]" , "Erreur", Messagebox.OK, Messagebox.ERROR) 
        //                }
        //            }
        //        }
        
        if (client.dateNaissance){
            if(new Date() - client.dateNaissance < 0 ){
                valideClient = false
                // Messagebox.show("Merci de verifier la date de naissance" , "Erreur", Messagebox.OK, Messagebox.ERROR)   
                listErreurs.add("* Saisir une date de naissance inférieure à la date d'aujourd'hui")
            }
        }
        
        if(client.dateContact){
            if (new Date() - client.dateContact < 0){
                valideClient = false
                //Messagebox.show("La date contact doit être inférieure ou égale à la date d'aujourd'hui " , "Erreur", Messagebox.OK, Messagebox.ERROR)  
                listErreurs.add("* Saisir une date contact inférieure ou égale à la date d'aujourd'hui")
            }
        }
        else{
            client.dateContact = new Date()
        }
        
        if(!valideClient){
            String msgError = "Veuillez corriger les erreurs suivantes :\n\n"
            for (it in listErreurs){
                msgError = msgError + it + "\n"
            }
            Messagebox.show( msgError, "Erreur", Messagebox.OK, Messagebox.ERROR)   
        }
        
        return valideClient
    }
    
    def genererListeAdresse(Projet projet){
        def lstAdresseMail = new ArrayList()
        def utilisateurs = projetService.getUtilisateurParProjet(projet)
        //String listeAdresse = null
        if (utilisateurs){
            for (it in utilisateurs){
                if(it.email){
                    //                    if(listeAdresse){
                    //                        //listeAdresse = listeAdresse + " ," + '"' + it.email + '"' 
                    //                    }
                    //                    else{
                    //                        //listeAdresse =  '"' + it.email + '"'  
                    //                    }
                    lstAdresseMail.add(it.email)
                    
                }
            }
        }
        
        return lstAdresseMail
      
    }
    
    def envoyerMail(String adresseMail, String objetMail, String contenuMail){
        mailService.sendMail {     
            to adresseMail
            subject objetMail     
            html contenuMail
        }
    }
    
    def minLength(String value, Integer min){
        Boolean valide = true
        if (!value || value.length() < min ) {
            valide = false
        }
        return valide
    }
    
    def closeWin(){
        //        Messagebox.show("Réservation en ligne terminée avec succès\n\nVous allez être redirigé vers le plan de masse", "Confirmation", Messagebox.OK , Messagebox.QUESTION)
        //        window.parent.location = "http://www.bassatinekenitra.com/index.php/fr/plan-de-masse";
        //        window.onload=window.parent.jcepopup.close();

    }
    
    def getClientFields(){
        String cinF = this.getFellow("fieldCin").value;
        Messagebox.show("Client cin : " + cinF, "Alerte", Messagebox.OK, Messagebox.INFORMATION)
    }
        
}
