
package com.choranet.omnidior.gesticom;
import java.text.SimpleDateFormat;  

/**
 * AlerteService Service pour la gestion des opérations
 * transactionnelles pour l'objet Alerte
 */
class AlerteService extends SuperService {

    static transactional = true
    def statutService
    def mailService
    
    def phaseService
    def paiementService
    def echeanceService
     
    def list() throws Exception {
        return super.list(Alerte.class)
    }
        
    def getAlerteByDate(dateD,idds,iddsp){
        
        if(idds.size() > 0 && iddsp.size() > 0) {
            def criteria = Alerte.createCriteria()
            def result = criteria.list {
                le("dateDeclenchement",dateD)
              
                client{
                        "in"("id", idds)
                }
                
                projet{
                        "in"("id", iddsp)
                }
                statut{
                 "in"("code",["20", "21"])
                }
                order ("dateDeclenchement","asc")
            }
        }else {
            return []
        }
        //   return result
    }
    
    def updateAlerteExpiree(){
        
        def statutExpire = statutService.getByCode("21")//.getByTypeEtIntitule("ALERTE","EXPIREE")
        
        def criteria = Alerte.createCriteria()
        def result = criteria.list {
            lt("date",new Date())
            statut{
                eq("intitule", "ENATTENTE")
                
            }
    
        }
        
        if (result != null && result.size()>0){
            for(rst in result) {
                rst.statut = statutExpire
                update(rst)
            }
        }
    }
    
    def filtrer(clazz, filtre, sortedHeader, sortedDirection, ofs, maxNb,utilisateur) throws Exception {
        try {
            def listeObjets 
            def tailleListe = 0
            def fields = clazz.getDeclaredFields()
            def requete = clazz.createCriteria()
            def attributToSort
            def selectionnerDistinctResultat = false
            def ids = utilisateur.clients*.id
            def idds = []
            ids.each {
                idds.addAll(it)
            }
            
            def idsp = utilisateur.projets*.id
            def iddsp = []
            idsp.each {
                iddsp.addAll(it)
            }
        
            if(idds.size() > 0 && iddsp.size() > 0) {
                listeObjets = requete.list {
                    for(field in fields) {
                        def nomAttribut = field.getName()
                        def modifier = field.getModifiers()
                        if(!nomAttribut.startsWith("trans_") && 
                            !(modifier > 2)) { // eviter les attributs transients: qui ne sont pas persister et les attributs static
                            def typeAttribut = field.getType()
                            //def valeurAttribut = field.get(filtre)
                            def valeurAttribut = filtre."$nomAttribut"
                    
                            if(sortedHeader == "h"+nomAttribut) {
                                attributToSort = nomAttribut 
                            }
                            if(valeurAttribut != null) {
                                if(valeurAttribut instanceof String) {
                                    if(valeurAttribut != "") {
                                        ilike(nomAttribut, valeurAttribut+"%")                                
                                    }
                                } else if(valeurAttribut instanceof Boolean || valeurAttribut == boolean.class) {
                                    if(valeurAttribut) {
                                        eq(nomAttribut, true)
                                    } else {
                                        eq(nomAttribut, false)
                                    }
                                } else if(valeurAttribut instanceof java.util.Collection) {
                                    "$nomAttribut" {"in"("id",valeurAttribut.id)}
                                    selectionnerDistinctResultat = true
                                } else {
                                    eq(nomAttribut, valeurAttribut)
                                }
                            }
                        }
                    }
                
                    client {
                    "in"("id", idds)
                    }
                    projet {
                    "in"("id", iddsp)
                    }
                
                    if(attributToSort != null) {
                        order(attributToSort, sortedDirection)
                    }
                    firstResult(ofs)
                    maxResults(maxNb)
                    if(selectionnerDistinctResultat) {
                        resultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
                        //Cette solution n est pas clean puisque ce travail est fait
                        //cote client et non pas database une deuxieme solution es la
                        //suivante reste a affiner voir: 
                        //https://forum.hibernate.org/viewtopic.php?t=941669
                        //                    projections {
                        //                        clazz.getSimpleName()
                        //                        distinct("id")
                        //                    }
                    }
                }
            } else {
                listeObjets = null
            }
            
            if(listeObjets != null && listeObjets.size() > 0) {
                def criteria = clazz.createCriteria()
                tailleListe = criteria.count{
                    for(field in fields) {
                        def nomAttribut = field.getName()
                        def modifier = field.getModifiers()
                        if(!nomAttribut.startsWith("trans_") && 
                            !(modifier > 2)) { // eviter les attributs transients: qui ne sont pas persister et les attributs static
                            def typeAttribut = field.getType()
                            //def valeurAttribut = field.get(filtre)
                            def valeurAttribut = filtre."$nomAttribut"
                    
                            if(valeurAttribut != null) {
                                if(valeurAttribut instanceof String) {
                                    if(valeurAttribut != "") {
                                        ilike(nomAttribut, valeurAttribut+"%")                                
                                    }
                                } else if(valeurAttribut instanceof Boolean || valeurAttribut == boolean.class) {
                                    if(valeurAttribut) {
                                        eq(nomAttribut, true)
                                    }
                                } else if(valeurAttribut instanceof java.util.Collection) {
                                    "$nomAttribut" {"in"("id",valeurAttribut.id)}
                                } else {
                                    eq(nomAttribut, valeurAttribut)
                                }
                            }
                        }
                    }
                    
                    client {
                    "in"("id", idds)
                    }
                    projet {
                    "in"("id", iddsp)
                    }
                    
                    if(selectionnerDistinctResultat) {
                        resultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
                        //Cette solution n est pas clean puisque ce travail est fait
                        //cote client et non pas database une deuxieme solution es la
                        //suivante reste a affiner voir: 
                        //https://forum.hibernate.org/viewtopic.php?t=941669
                        //                    projections {
                        //                        clazz.getSimpleName()
                        //                        distinct("id")
                        //                    }
                    }    
                }
            } else {
                tailleListe = 0
                listeObjets = null
            }
            
            return [tailleListe:tailleListe,listeObjets:listeObjets]
        }
        catch(Exception e) {
            logger.error(e)
            throw e
        }
    }

    def filtrer(clazz, filtre, attributsAFiltrer, sortedHeader, sortedDirection, ofs, maxNb,utilisateur) throws Exception {
        try {
            def listeObjets 
            def tailleListe = 0
            def requete = Bien.createCriteria()
            def attributToSort
            def selectionnerDistinctResultat = false
            def ids = utilisateur.clients*.id
            def idds = []
            ids.each {
                idds.addAll(it)
            }
            def idsp = utilisateur.projets*.id
            def iddsp = []
            idsp.each {
                iddsp.addAll(it)
            }
            
            if(idds.size() > 0 && iddsp.size() > 0) {
                listeObjets = requete.list {
                    for(nomAttribut in attributsAFiltrer) {
                        def valeurAttribut = filtre."$nomAttribut"
                    
                        if(sortedHeader == "h"+nomAttribut) {
                            attributToSort = nomAttribut 
                        }
                        if(valeurAttribut != null) {
                            if(valeurAttribut instanceof String) {
                                if(valeurAttribut != "") {
                                    ilike(nomAttribut, valeurAttribut+"%")                                
                                }
                            } else if(valeurAttribut instanceof Boolean || valeurAttribut == boolean.class) {
                                if(valeurAttribut) {
                                    eq(nomAttribut, true)
                                } else {
                                    eq(nomAttribut, false)
                                }
                            } else if(valeurAttribut instanceof java.util.Collection) {
                            "$nomAttribut" {"in"("id",valeurAttribut.id)}
                                selectionnerDistinctResultat = true
                            } else {
                                eq(nomAttribut, valeurAttribut)
                            }
                        }
                    }
                
                    client {
                    "in"("id", idds)
                    }
                    projet {
                    "in"("id", iddsp)
                    }
                
                    if(attributToSort != null) {
                        order(attributToSort, sortedDirection)
                    }
                    firstResult(ofs)
                    maxResults(maxNb) 
                    if(selectionnerDistinctResultat) {
                        resultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
                        //Cette solution n est pas clean puisque ce travail est fait
                        //cote client et non pas database une deuxieme solution es la
                        //suivante reste a affiner voir: 
                        //https://forum.hibernate.org/viewtopic.php?t=941669
                        //                    projections {
                        //                        clazz.getSimpleName()
                        //                        distinct("id")
                        //                    }
                    }
                }
            } else {
                listeObjets = null
            }
            if(listeObjets != null && listeObjets.size() > 0) {
                def criteria = clazz.createCriteria()
                tailleListe = criteria.count {
                    for(nomAttribut in attributsAFiltrer) {
                        def valeurAttribut = filtre."$nomAttribut"
                        
                        if(valeurAttribut != null) {
                            if(valeurAttribut instanceof String) {
                                if(valeurAttribut != "") {
                                    ilike(nomAttribut, valeurAttribut+"%")                                
                                }
                            } else if(valeurAttribut instanceof Boolean || valeurAttribut == boolean.class) {
                                if(valeurAttribut) {
                                    eq(nomAttribut, true)
                                }
                            }else if(valeurAttribut instanceof java.util.Collection) {
                                "$nomAttribut" {"in"("id",valeurAttribut.id)}
                            } else {
                                eq(nomAttribut, valeurAttribut)
                            }
                        }
                    }
                    
                    client {
                    "in"("id", idds)
                    }
                    projet {
                    "in"("id", iddsp)
                    }
                    
                    if(selectionnerDistinctResultat) {
                        resultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
                        //Cette solution n est pas clean puisque ce travail est fait
                        //cote client et non pas database une deuxieme solution es la
                        //suivante reste a affiner voir: 
                        //https://forum.hibernate.org/viewtopic.php?t=941669
                        //                    projections {
                        //                        clazz.getSimpleName()
                        //                        distinct("id")
                        //                    }
                    }
                }
            }else {
                tailleListe = 0
                listeObjets = null
            }
            
            return [tailleListe:tailleListe,listeObjets:listeObjets]
            
        }
        catch(Exception e) {
            logger.error(e)
            throw e
        }
    }
    
    def getAlerteByUtlisateur(utilisateur,ofs = null,maxNb = null){
       
        def ids = utilisateur.clients*.id
        def idds = []
        ids.each {
            idds.addAll(it)
        }
        def idsp = utilisateur.projets*.id
        def iddsp = []
        idsp.each {
            iddsp.addAll(it)
        }
        if(idds.size() > 0 && iddsp.size() > 0) {
            def criteria = Alerte.createCriteria()
            def result = criteria.list {
               
                client {
                    "in"("id", idds)
                }
                projet {
                    "in"("id", iddsp)
                }
                order ("dateDeclenchement","asc")
                if(ofs)
                firstResult(ofs)
                if(maxNb)
                maxResults(maxNb)
            }

        } else {
            return []
        }
        
    }
    
    def alerteMail(Utilisateur utilisateur){
        Date dt = new Date()
        def sdf = new SimpleDateFormat("dd/MM/yyyy") 
        
        def listeAlertes = getAlerteByDate(dt,utilisateur)
        def rappelPaiements = paiementService.getPaiementNonValide(utilisateur)
        def rappelEcheances = echeanceService.getEcheanceBydateEtUtilisateur(dt,utilisateur)//echeanceService.getEcheanceBydateEtUtilisateur(new Date(),utilisateur)
        def listePhases  = phaseService.getPhaseByDate(dt,utilisateur)
        
        Boolean envoyerMailB = false              
            
        String subjectMail = "Alertes du " + sdf.format(dt)
        String contentMailHeader = """
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Notification </title>
<style type="text/css">
<!--
body
{
	line-height: 1.6em;
}

#hor-minimalist-a
{
	font-family: "Lucida Sans Unicode", "Lucida Grande", Sans-Serif;
	font-size: 12px;
	background: #fff;
	margin-left: 45px;
	width: 95%;
	border-collapse: collapse;
	text-align: left;
}
#hor-minimalist-a th
{
	font-size: 14px;
	font-weight: normal;
	color: #039;
	padding: 10px 8px;
	border-bottom: 2px solid #6678b1;
}
#hor-minimalist-a td
{
	color: #669;
	padding: 9px 8px 0px 8px;
}
#hor-minimalist-a tbody tr:hover td
{
	color: #009;
}
#par
{
	font-family: "Lucida Sans Unicode", "Lucida Grande", Sans-Serif;
	font-size: 14px;
	font-weight: normal;
	color: #B1221C;	
	margin-top: 45px;
	margin-left: 45px;
	margin-bottom: 10px;
}
#norep
{
	font-family: "Lucida Sans Unicode", "Lucida Grande", Sans-Serif;
	font-size: 12px;
	font-weight: normal;
	color: #3E4342;	
	margin-top: 45px;
	margin-left: 45px;
	margin-bottom: 10px;
	font-style:italic;
}
-->
</style>
</head>
<body>
<div id ="par">Bonjour, Merci de bien vouloir traiter les notifications : </div>
""" 
        String tdAlertes = ""
        String tdPaiements = ""
        String tdEcheances = ""
        String tdPhases = ""
     
        if (listeAlertes && listeAlertes.size()> 0 ) {  
            envoyerMailB = true
            tdAlertes = """<div id ="par"> Liste des alertes</div>
    
            <table id="hor-minimalist-a" summary="Liste des alertes">

            <tbody> 
               
            <tr>
            <th width="20%" scope="col" align="left">Projet</th>
            <th width="20%" scope="col" align="left">Client</th>
            <th width="10%" scope="col" align="left">Exécution</th>
            <th width="10%" scope="col" align="left">Déclenchement</th>
            <th width="40%" scope="col" align="left">Contenu</th>
            </tr>"""
    
            String tdAlerteLigne
        
            for (it in listeAlertes){
                tdAlerteLigne = """<tr> 
        <td>""" + it.projet + """</td> 
        <td>""" + it.client + """</td>
        <td>""" + sdf.format(it.date) + """</td> 
        <td>""" + sdf.format(it.dateDeclenchement) + """</td>
        <td>""" + it.messagePersonalise + """</td> 
        </tr>"""
        
                tdAlertes = tdAlertes + tdAlerteLigne
            }            
        
            tdAlertes = tdAlertes + "</table>"
        }
    
        if (rappelPaiements && rappelPaiements.size()>0){
            envoyerMailB = true
            tdPaiements = """<br /> <div id ="par"> Liste des retours impayés</div>
            <table id="hor-minimalist-a" summary="Liste des retours impayés">

            <tbody> 
               
            <tr>
            <th width="20%" scope="col" align="left">Projet</th>
            <th width="20%" scope="col" align="left">Client</th>
            <th width="10%" scope="col" align="left">Paiement</th>
            <th width="10%" scope="col" align="left">Encaissement</th>
            <th width="40%" scope="col" align="left">Montant</th>
            </tr>"""
    
            String tdPaiementLigne
        
            for (it in rappelPaiements){
                tdPaiementLigne = """<tr> 
        <td>""" + it.phase.projet + """</td> 
        <td>""" + it.phase.client + """</td>
        <td>""" + sdf.format(it.datePaiement) + """</td> 
        <td>""" + sdf.format(it.dateEncaissement) + """</td>
        <td>""" + it.montant + """</td> 
        </tr>"""
        
                tdPaiements = tdPaiements + tdPaiementLigne
            }            
        
            tdPaiements = tdPaiements + "</table>"
        }
        
        if (rappelEcheances && rappelEcheances.size()>0){
            envoyerMailB = true
            tdEcheances = """<br /> <div id ="par"> Liste des écheances</div>
            <table id="hor-minimalist-a" summary="Liste des echeances">

            <tbody> 
               
            <tr>
            <th width="20%" scope="col" align="left">Projet</th>
            <th width="20%" scope="col" align="left">Client</th>
            <th width="20%" scope="col" align="left">Date</th>
            <th width="40%" scope="col" align="left">Montant</th>
            </tr>"""
    
            String tdEcheanceLigne
        
            for (it in rappelEcheances){
                tdEcheanceLigne = """<tr> 
        <td>""" + it.phase.projet + """</td> 
        <td>""" + it.phase.client + """</td>
        <td>""" + sdf.format(it.date) + """</td> 
        <td>""" + it.montantEcheance + """</td> 
        </tr>"""
        
                tdEcheances = tdEcheances + tdEcheanceLigne
            }            
        
            tdEcheances = tdEcheances + "</table>"
        }
        
        if (listePhases && listePhases.size()>0){
            envoyerMailB = true
            tdPhases = """<br /> <div id ="par"> Liste des phases expirées</div>
            <table id="hor-minimalist-a" summary="Liste des phases expirées">

            <tbody> 
               
            <tr>
            <th width="20%" scope="col" align="left">Projet</th>
            <th width="20%" scope="col" align="left">Client</th>
            <th width="20%" scope="col" align="left">Expiration</th>
            <th width="40%" scope="col" align="left">Phase</th>
            </tr>"""
    
            String tdPhaseLigne
        
            for (it in listePhases){
                tdPhaseLigne = """<tr> 
        <td>""" + it.projet + """</td> 
        <td>""" + it.client + """</td>
        <td>""" + sdf.format(it.dateFin) + """</td> 
        <td>""" + it.intitule + """</td> 
        </tr>"""
        
                tdPhases = tdPhases + tdPhaseLigne
            }            
        
            tdPhases = tdPhases + "</table>"
        }
    
    
        String contentMailFooter = """
    <p id ="norep">Ceci est un message automatique, *merci de ne pas y répondre*.</p>
    </body> </html>"""  

        String contenuMail = contentMailHeader + tdAlertes + tdPaiements +tdEcheances+tdPhases+ contentMailFooter 
        
            
        try{
            //            def mailTo = genererListeAdresse(bien.projet)
            //            String mailToAddresse 
            //            for (it in mailTo){
            //                mailToAddresse = '"' + it + '"'
            //                envoyerMail (mailToAddresse,subjectMail,contentMail)
            //            }
            String mailToAddresse = "kabertoun@choranet.com"
            if (envoyerMailB){
                envoyerMail (mailToAddresse,subjectMail,contenuMail)
            }
        } catch(Exception ex) {
                
            //logger.error "Error: ${ex.message}", ex
                
                
        }
        
       
    }
    
    def envoyerMail(String adresseMail, String objetMail, String contenuMail){
        mailService.sendMail {     
            to adresseMail
            subject objetMail     
            html contenuMail
        }
    }
    
    def jobMailExecute(){
        def utilisateurs = Utilisateur.list()
        for (u in utilisateurs){
            alerteMail(u)
        }
    }
}
