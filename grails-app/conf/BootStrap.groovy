import com.choranet.omnidior.gesticom.*
import com.choranet.securite.*
import cr.co.arquetipos.crypto.Blowfish 
import com.choranet.omnidior.gesticom.BienService
import com.choranet.omnidior.gesticom.StatutService
import com.choranet.omnidior.gesticom.TypeService
import com.choranet.omnidior.gesticom.ProjetService
import com.choranet.omnidior.gesticom.DroitUtilisateur
class BootStrap {

    def authenticateService
    def bienService
    def statutService
    def typeService
    def projetService
    def paysService
    def droitUtilisateurService
    def utilisateurService
    
    def init = { servletContext ->
        environments {
            development {
                
                //def d1 = new DroitUtilisateur(droit : 'GESTION_CLIENTS', description : 'Gestion des clients', parent : null).save()
                def d2 = new DroitUtilisateur(droit : 'GESTION_PHASES', description : 'Gestion des phases', parent : null).save()
                def d26 = new DroitUtilisateur(droit : 'CHANGER_DATE_EXPIRATION', description : 'Changement date expiration', parent : d2).save()
                def d21 = new DroitUtilisateur(droit : 'GESTION_OPTIONS', description : 'Gestion des options', parent : d2).save()
                def d22 = new DroitUtilisateur(droit : 'GESTION_RESERVATIONS', description : 'Gestion des réservations', parent : d2).save()
                def d23 = new DroitUtilisateur(droit : 'GESTION_COMPROMIS', description : 'Gestion des compromis', parent : d2).save()
                def d24 = new DroitUtilisateur(droit : 'GESTION_CONTRATS', description : 'Gestion des contrats', parent : d2).save()
                def d25 = new DroitUtilisateur(droit : 'GESTION_ANNULATIONS', description : 'Gestion des annulations', parent : d2).save()
                def d3 = new DroitUtilisateur(droit : 'GESTION_ECHEANCES', description : 'Gestion des échéances', parent : null).save()
                def d4 = new DroitUtilisateur(droit : 'GESTION_PAIEMENTS', description : 'Gestion des paiements', parent : null).save()
                def d41 = new DroitUtilisateur(droit : 'GESTION_PAIEMENTS_STATUT', description : 'Gestion des statuts paiement', parent : d4).save()
                def d5 = new DroitUtilisateur(droit : 'PARAMETRAGE', description : 'Paramétrage du système', parent : null).save()
                def d501 = new DroitUtilisateur(droit : 'GESTION_MODELES', description : 'Gestion des modèles', parent : d5).save()
                def d502 = new DroitUtilisateur(droit : 'GESTION_MODES_PAIEMENT', description : 'Gestion des modes de paiement', parent : d5).save()
                def d503 = new DroitUtilisateur(droit : 'GESTION_CANAUX', description : 'Gestion des canaux', parent : d5).save()
                def d504 = new DroitUtilisateur(droit : 'GESTION_SOURCES_INFORMATION', description : 'Gestion des sources d\'information', parent : d5).save()
                def d505 = new DroitUtilisateur(droit : 'GESTION_PAYS', description : 'Gestion des pays', parent : d5).save()
                def d506 = new DroitUtilisateur(droit : 'GESTION_VILLES', description : 'Gestion des villes', parent : d5).save()
                def d507 = new DroitUtilisateur(droit : 'GESTION_NATIONALITES', description : 'Gestion des nationalités', parent : d5).save()
                def d508 = new DroitUtilisateur(droit : 'GESTION_TYPES', description : 'Gestion des types', parent : d5).save()
                def d509 = new DroitUtilisateur(droit : 'GESTION_STATUTS', description : 'Gestion des statuts', parent : d5).save()
                def d510 = new DroitUtilisateur(droit : 'GESTION_CONTENANCES', description : 'Gestion des contenances', parent : d5).save()
                def d511 = new DroitUtilisateur(droit : 'GESTION_AFFECTATIONS', description : 'Gestion des affectations', parent : d5).save()
                def d6 = new DroitUtilisateur(droit : 'ADMINISTRATION', description : 'Administration du système', parent : null).save()
                def d61 = new DroitUtilisateur(droit : 'GESTION_UTILISATEURS', description : 'Gestion des utilisateurs', parent : d6).save()
                def d62 = new DroitUtilisateur(droit : 'GESTION_GROUPES', description : 'Gestion des groupes d\'utilisateurs', parent : d6).save()
                def d63 = new DroitUtilisateur(droit : 'AUDIT_SYSTEM', description : 'Suivi de l\'audit du système', parent : d6).save()
                def d64 = new DroitUtilisateur(droit : 'BACKUP', description : 'Sauveguarde et restauration du système', parent : d6).save()
                def d65 = new DroitUtilisateur(droit : 'INFORMATIONS_SOCIETE', description : 'Gestion des informations de la société', parent : d6).save()
                def d66 = new DroitUtilisateur(droit : 'IMPORT_EXCEL', description : 'Importation des fichiers excel', parent : d6).save()
                def d7 = new DroitUtilisateur(droit : 'GESTION_PROJETS', description : 'Gestion des projets', parent : null).save()
                def d71 = new DroitUtilisateur(droit : 'GESTION_BIENS', description : 'Gestion des biens', parent : d7).save()
                def d1 = new DroitUtilisateur(droit : 'GESTION_CLIENTS', description : 'Gérer les clients', parent : null).save()
                def d11 = new DroitUtilisateur(droit : 'PRISES_CONTACT', description : 'Prise des contacts', parent : d1).save()
                def d8 = new DroitUtilisateur(droit : 'TDB', description : 'Accès aux tableaux de bord', parent : null).save()
                def d9 = new DroitUtilisateur(droit : 'GESTION_ALERTS', description : 'Gérer des alerts', parent : null).save()
                def d10 = new DroitUtilisateur(droit : 'GESTION_NOTAIRES', description : 'Gérer les notaires', parent : null).save()
                def d100 = new DroitUtilisateur(droit : 'IMPORT_DONNEES', description : 'Import des données', parent : null).save()
                def d110 = new DroitUtilisateur(droit : 'ACCES_RAPPORTS', description : 'Accès aux rapports', parent : null).save()
                
                
                def groupeRoot = new GroupeUtilisateur(intitule:'ROOT', description:'Administration de l\'application')
                .addToDroits(d1)
                .addToDroits(d11)
                .addToDroits(d2)
                .addToDroits(d21)
                .addToDroits(d22)
                .addToDroits(d23)
                .addToDroits(d24)
                .addToDroits(d25)
                .addToDroits(d26)
                .addToDroits(d3)
                .addToDroits(d4)
                .addToDroits(d41)
                .addToDroits(d5)
                .addToDroits(d501)
                .addToDroits(d502)
                .addToDroits(d503)
                .addToDroits(d504)
                .addToDroits(d505)
                .addToDroits(d506)
                .addToDroits(d507)
                .addToDroits(d508)
                .addToDroits(d509)
                .addToDroits(d510)
                .addToDroits(d511)
                .addToDroits(d6)
                .addToDroits(d61)
                .addToDroits(d62)
                .addToDroits(d63)
                .addToDroits(d64)
                .addToDroits(d65)
                .addToDroits(d66)
                .addToDroits(d7)
                .addToDroits(d71)
                .addToDroits(d8)
                .addToDroits(d9)
                .addToDroits(d10)
                .addToDroits(d100)
                .addToDroits(d110)
                .save()
                
                def groupeUser = new GroupeUtilisateur(intitule:'USER', description:'Utilisation de l\'application')
                .addToDroits(d1)
                .addToDroits(d11)
                .addToDroits(d2)
                .addToDroits(d21)
                .addToDroits(d22)
                .addToDroits(d23)
                .addToDroits(d24)
                .addToDroits(d25)
                .addToDroits(d3)
                .addToDroits(d4)
                .save()
                
                def userRoot = new Utilisateur(username:'admin',
                    userRealName:'Administrateur du système',
                    enabled: true,
                    email: 'test@test.com',
                    passwd: Blowfish.encryptBase64('admin', '#Fadz987@g.com'),
                    groupe : groupeRoot).save() 
                
                def userUser = new Utilisateur(username:'user',
                    userRealName:'Utilisateur du système',
                    enabled: true,
                    email: 'test@test.com',
                    passwd: Blowfish.encryptBase64('admin', '#Fadz987@g.com'),
                    groupe : groupeUser).save() 
                
                def clientInfo = new ChoraClientInfo(nomsociete:'OMNIDIOR',raisonSocial : "OMNIDIOR", telephone : null, fax : null,email:null,patente:null,rc:null,idF:null,cnss:null,site:null,repertoirBackup:".")
                clientInfo.setTrans_logo(null)
                clientInfo.save()
                
                def clientInfoGOLDENROAD = new ChoraClientInfo(nomsociete:'GOLDEN ROAD',raisonSocial : "GOLDEN ROAD", telephone : null, fax : null,email:null,patente:null,rc:null,idF:null,cnss:null,site:null,repertoirBackup:".")
                clientInfo.setTrans_logo(null)
                clientInfoGOLDENROAD.save()
                
                
                //                userRoot.addToGroupeClients(clientInfo)
                //statut
                def statutPlannifieProjet = new Statut(code : "01", intitule:"EN PLANIFICATION",type : "PROJET").save()
                def statutEncoursProjet = new Statut(code : "02", intitule:"EN EXECUTION",type : "PROJET").save()
                def statutTermineProjet = new Statut(code : "03", intitule:"CLOTURE",type : "PROJET").save()
                def statutEnCommercialisationProjet = new Statut(code : "28", intitule:"EN COMMERCIALISATION",type : "PROJET").save()
                
                def statutLibreBien = new Statut(code : "04", intitule:"LIBRE",type : "BIEN").save()
                def statutEnoptionBien = new Statut(code : "25", intitule:"EN OPTION",type : "BIEN").save()
                def statutReserveBien = new Statut(code : "05", intitule:"RESERVE",type : "BIEN").save()
                def statutVenduBien = new Statut(code : "06", intitule:"VENDU",type : "BIEN").save()
                
                def statutEncoursPhase = new Statut(code : "07", intitule:"EN COURS",type : "PHASE").save()
                def statutSuspenduePhase = new Statut(code : "08", intitule:"SUSPENDUE",type : "PHASE").save()
                def statutClotureePhase = new Statut(code : "09", intitule:"CLOTUREE",type : "PHASE").save()
                def statutAnnuleePhase = new Statut(code : "10", intitule:"ANNULEE",type : "PHASE").save()
                def statutExpireePhase = new Statut(code : "27", intitule:"EXPIREE",type : "PHASE").save()
                
                def statutEncoursPaiement = new Statut(code : "11", intitule:"EN COURS",type : "PAIEMENT").save()
                def statutPayePaiement = new Statut(code : "12", intitule:"ENCAISSE",type : "PAIEMENT").save()
                def statutNonValidePaiement = new Statut(code : "13", intitule:"RETOUR IMPAYE",type : "PAIEMENT").save()
                def statutAnnulePaiement = new Statut(code : "14", intitule:"ANNULE",type : "PAIEMENT").save()
                
                def statutEncoursEcheance = new Statut(code : "15", intitule:"EN COURS",type : "ECHEANCE").save()
                def statutPayeEcheance = new Statut(code : "16", intitule:"PAYEE",type : "ECHEANCE").save()
                def statutAnnuleeEcheance = new Statut(code : "17", intitule:"ANNULEE",type : "ECHEANCE").save()
                
                def statutVersementPaiement = new Statut(code : "19", intitule:"VERSEMENT",type : "PAIEMENTTYPE").save()
                def statutAvancePaiement = new Statut(code : "18", intitule:"AVANCE",type : "PAIEMENTTYPE").save()               
                
                def statutEnattenteAlerte = new Statut(code : "20", intitule:"ENATTENTE",type : "ALERTE").save()
                def statutExpireeAlerte = new Statut(code : "21", intitule:"EXPIREE",type : "ALERTE").save()
                def statutAnnuleeAlerte = new Statut(code : "22", intitule:"ANULLEE",type : "ALERTE").save()
                def statutTermineeAlerte = new Statut(code : "26", intitule:"TERMINEE",type : "ALERTE").save()
                
                def statutProspectClient = new Statut(code : "23", intitule : "PROSPECT",type : "CLIENT").save()
                def statutClientClient = new Statut(code : "24", intitule : "CLIENT",type : "CLIENT").save()
                
                

                //pays                
                def paysTunisie = new Pays(intitule: 'Tunisie').save()
               
                //ville
                
                new Pays(intitule : 'Angola').save();
                new Pays(intitule : 'Anguilla').save();
                new Pays(intitule : 'Antigua-et-Barbuda').save();
                new Pays(intitule : 'Antilles néerlandaises').save();
                new Pays(intitule : 'Arabie saoudite').save();
                new Pays(intitule : 'Argentine').save();
                new Pays(intitule : 'Arménie').save();
                new Pays(intitule : 'Aruba').save();
                new Pays(intitule : 'Australie').save();
                new Pays(intitule : 'Autriche').save();
                new Pays(intitule : 'Azerbaïdjan').save();
                new Pays(intitule : 'Bahamas').save();
                new Pays(intitule : 'Bahreïn').save();
                new Pays(intitule : 'Bangladesh').save();
                new Pays(intitule : 'Barbade').save();
                new Pays(intitule : 'Belau').save();
                new Pays(intitule : 'Belgique').save();
                new Pays(intitule : 'Belize').save();
                new Pays(intitule : 'Bénin').save();
                new Pays(intitule : 'Bermudes').save();
                new Pays(intitule : 'Bhoutan').save();
                new Pays(intitule : 'Biélorussie').save();
                new Pays(intitule : 'Birmanie').save();
                new Pays(intitule : 'Bolivie').save();
                new Pays(intitule : 'Bosnie-Herzégovine').save();
                new Pays(intitule : 'Botswana').save();
                new Pays(intitule : 'Brésil').save();
                new Pays(intitule : 'Brunei').save();
                new Pays(intitule : 'Bulgarie').save();
                new Pays(intitule : 'Burkina Faso').save();
                new Pays(intitule : 'Burundi').save();
                new Pays(intitule : 'Cambodge').save();
                new Pays(intitule : 'Cameroun').save();
                new Pays(intitule : 'Canada').save();
                new Pays(intitule : 'Cap-Vert').save();
                new Pays(intitule : 'Chili').save();
                new Pays(intitule : 'Chine').save();
                new Pays(intitule : 'Chypre').save();
                new Pays(intitule : 'Colombie').save();
                new Pays(intitule : 'Comores').save();
                new Pays(intitule : 'Congo').save();
                new Pays(intitule : 'Congo (République démocratique du )').save();
                new Pays(intitule : 'Corée du Nord').save();
                new Pays(intitule : 'Corée du Sud').save();
                new Pays(intitule : 'Costa Rica').save();
                new Pays(intitule : 'Côte d\'Ivoire').save();
                new Pays(intitule : 'Croatie').save();
                new Pays(intitule : 'Cuba').save();
                new Pays(intitule : 'Danemark').save();
                new Pays(intitule : 'Djibouti').save();
                new Pays(intitule : 'Dominique').save();
                new Pays(intitule : 'Égypte').save();
                new Pays(intitule : 'Émirats arabes unis').save();
                new Pays(intitule : 'Équateur').save();
                new Pays(intitule : 'Érythrée').save();
                new Pays(intitule : 'Espagne').save();
                new Pays(intitule : 'Estonie').save();
                new Pays(intitule : 'États-Unis').save();
                new Pays(intitule : 'Éthiopie').save();
                new Pays(intitule : 'Fidji').save();
                new Pays(intitule : 'Finlande').save();
                new Pays(intitule : 'France').save();
                new Pays(intitule : 'Gabon').save();
                new Pays(intitule : 'Gambie').save();
                new Pays(intitule : 'Géorgie').save();
                new Pays(intitule : 'Ghana').save();
                new Pays(intitule : 'Gibraltar').save();
                new Pays(intitule : 'Grèce').save();
                new Pays(intitule : 'Grenade').save();
                new Pays(intitule : 'Groenland').save();
                new Pays(intitule : 'Guadeloupe').save();
                new Pays(intitule : 'Guam').save();
                new Pays(intitule : 'Guatemala').save();
                new Pays(intitule : 'Guinée').save();
                new Pays(intitule : 'Guinée équatoriale').save();
                new Pays(intitule : 'Guinée-Bissao').save();
                new Pays(intitule : 'Guyana').save();
                new Pays(intitule : 'Guyane française').save();
                new Pays(intitule : 'Haïti').save();
                new Pays(intitule : 'Honduras').save();
                new Pays(intitule : 'Hong Kong').save();
                new Pays(intitule : 'Hongrie').save();
                new Pays(intitule : 'Ile Christmas').save();
                new Pays(intitule : 'Ile Norfolk').save();
                new Pays(intitule : 'Iles Cayman').save();
                new Pays(intitule : 'Iles Cook').save();
                new Pays(intitule : 'Iles des Cocos').save();
                new Pays(intitule : 'Iles Falkland').save();
                new Pays(intitule : 'Iles Féroé').save();
                new Pays(intitule : 'Îles Marshall').save();
                new Pays(intitule : 'Iles Pitcairn').save();
                new Pays(intitule : 'Iles Salomon').save();
                new Pays(intitule : 'Iles Svalbard et Jan Mayen').save();
                new Pays(intitule : 'Iles Turks-et-Caicos').save();
                new Pays(intitule : 'Iles Vierges américaines').save();
                new Pays(intitule : 'Iles Vierges britanniques').save();
                new Pays(intitule : 'Inde').save();
                new Pays(intitule : 'Indonésie').save();
                new Pays(intitule : 'Iran').save();
                new Pays(intitule : 'Iraq').save();
                new Pays(intitule : 'Irlande').save();
                new Pays(intitule : 'Islande').save();
                new Pays(intitule : 'Italie').save();
                new Pays(intitule : 'Jamaïque').save();
                new Pays(intitule : 'Japon').save();
                new Pays(intitule : 'Jordanie').save();
                new Pays(intitule : 'Kazakhstan').save();
                new Pays(intitule : 'Kenya').save();
                new Pays(intitule : 'Kirghizistan').save();
                new Pays(intitule : 'Kiribati').save();
                new Pays(intitule : 'Koweït').save();
                new Pays(intitule : 'Laos').save();
                new Pays(intitule : 'Lesotho').save();
                new Pays(intitule : 'Lettonie').save();
                new Pays(intitule : 'Liban').save();
                new Pays(intitule : 'Liberia').save();
                new Pays(intitule : 'Libye').save();
                new Pays(intitule : 'Liechtenstein').save();
                new Pays(intitule : 'Lituanie').save();
                new Pays(intitule : 'Luxembourg').save();
                new Pays(intitule : 'Macao').save();
                new Pays(intitule : 'Macédoine').save();
                new Pays(intitule : 'Madagascar').save();
                new Pays(intitule : 'Malaisie').save();
                new Pays(intitule : 'Malawi').save();
                new Pays(intitule : 'Maldives').save();
                new Pays(intitule : 'Mali').save();
                new Pays(intitule : 'Malte').save();
                new Pays(intitule : 'Mariannes du Nord').save();
                new Pays(intitule : 'Martinique').save();
                new Pays(intitule : 'Maurice').save();
                new Pays(intitule : 'Mauritanie').save();
                new Pays(intitule : 'Mayotte').save();
                new Pays(intitule : 'Mexique').save();
                new Pays(intitule : 'Micronésie').save();
                new Pays(intitule : 'Moldavie').save();
                new Pays(intitule : 'Monaco').save();
                new Pays(intitule : 'Mongolie').save();
                new Pays(intitule : 'Montserrat').save();
                new Pays(intitule : 'Mozambique').save();
                new Pays(intitule : 'Namibie').save();
                new Pays(intitule : 'Nauru').save();
                new Pays(intitule : 'Népal').save();
                new Pays(intitule : 'Nicaragua').save();
                new Pays(intitule : 'Niger').save();
                new Pays(intitule : 'Nigeria').save();
                new Pays(intitule : 'Nioué').save();
                new Pays(intitule : 'Norvège').save();
                new Pays(intitule : 'Nouvelle-Calédonie').save();
                new Pays(intitule : 'Nouvelle-Zélande').save();
                new Pays(intitule : 'Oman').save();
                new Pays(intitule : 'Ouganda').save();
                new Pays(intitule : 'Ouzbékistan').save();
                new Pays(intitule : 'Pakistan').save();
                new Pays(intitule : 'Panama').save();
                new Pays(intitule : 'Papouasie-Nouvelle-Guinée').save();
                new Pays(intitule : 'Paraguay').save();
                new Pays(intitule : 'Pays-Bas').save();
                new Pays(intitule : 'Pérou').save();
                new Pays(intitule : 'Philippines').save();
                new Pays(intitule : 'Pologne').save();
                new Pays(intitule : 'Polynésie française').save();
                new Pays(intitule : 'Porto Rico').save();
                new Pays(intitule : 'Portugal').save();
                new Pays(intitule : 'Paléstine').save();
                new Pays(intitule : 'Qatar').save();
                new Pays(intitule : 'République centrafricaine').save();
                new Pays(intitule : 'République dominicaine').save();
                new Pays(intitule : 'République tchèque').save();
                new Pays(intitule : 'Réunion').save();
                new Pays(intitule : 'Roumanie').save();
                new Pays(intitule : 'Royaume-Uni').save();
                new Pays(intitule : 'Russie').save();
                new Pays(intitule : 'Rwanda').save();
                new Pays(intitule : 'Saint-Christophe-et-Niévès').save();
                new Pays(intitule : 'Sainte-Hélène').save();
                new Pays(intitule : 'Sainte-Lucie').save();
                new Pays(intitule : 'Saint-Marin').save();
                new Pays(intitule : 'Saint-Pierre-et-Miquelon').save();
                new Pays(intitule : 'Saint-Siège').save();
                new Pays(intitule : 'Saint-Vincent-et-les-Grenadines').save();
                new Pays(intitule : 'Salvador').save();
                new Pays(intitule : 'Samoa').save();
                new Pays(intitule : 'Samoa américaines').save();
                new Pays(intitule : 'Sao Tomé-et-Principe').save();
                new Pays(intitule : 'Sénégal').save();
                new Pays(intitule : 'Serbie-et-Monténégro').save();
                new Pays(intitule : 'Seychelles').save();
                new Pays(intitule : 'Sierra Leone').save();
                new Pays(intitule : 'Singapour').save();
                new Pays(intitule : 'Slovaquie').save();
                new Pays(intitule : 'Slovénie').save();
                new Pays(intitule : 'Somalie').save();
                new Pays(intitule : 'Soudan').save();
                new Pays(intitule : 'Sri Lanka').save();
                new Pays(intitule : 'Suède').save();
                new Pays(intitule : 'Suisse').save();
                new Pays(intitule : 'Suriname').save();
                new Pays(intitule : 'Swaziland').save();
                new Pays(intitule : 'Syrie').save();
                new Pays(intitule : 'Tadjikistan').save();
                new Pays(intitule : 'Taïwan').save();
                new Pays(intitule : 'Tanzanie').save();
                new Pays(intitule : 'Tchad').save();
                new Pays(intitule : 'Thaïlande').save();
                new Pays(intitule : 'Timor oriental').save();
                new Pays(intitule : 'Togo').save();
                new Pays(intitule : 'Tokélaou').save();
                new Pays(intitule : 'Tonga').save();
                new Pays(intitule : 'Trinité-et-Tobago').save();
                new Pays(intitule : 'Turkménistan').save();
                new Pays(intitule : 'Turquie').save();
                new Pays(intitule : 'Tuvalu').save();
                new Pays(intitule : 'Ukraine').save();
                new Pays(intitule : 'Uruguay').save();
                new Pays(intitule : 'Vanuatu').save();
                new Pays(intitule : 'Venezuela').save();
                new Pays(intitule : 'Viêt Nam').save();
                new Pays(intitule : 'Wallis-et-Futuna').save();
                new Pays(intitule : 'Yémen').save();
                new Pays(intitule : 'Zambie').save();
                new Pays(intitule : 'Zimbabwe').save();
                
                def maroc = new Pays(intitule: 'Maroc').save()
                
                new Ville(intitule : 'AGADIR', pays : maroc).save();
                new Ville(intitule : 'AGLOU', pays : maroc).save();
                new Ville(intitule : 'AHFIR', pays : maroc).save();
                new Ville(intitule : 'AHFIR', pays : maroc).save();
                new Ville(intitule : 'AL HOCEIMA', pays : maroc).save();
                new Ville(intitule : 'AROUIT', pays : maroc).save();
                new Ville(intitule : 'ASILAH', pays : maroc).save();
                new Ville(intitule : 'AZROU', pays : maroc).save();
                new Ville(intitule : 'BEN AHMED', pays : maroc).save();
                new Ville(intitule : 'BERKANE', pays : maroc).save();
                new Ville(intitule : 'BERKANE', pays : maroc).save();
                new Ville(intitule : 'BERRECHID', pays : maroc).save();
                new Ville(intitule : 'BOUBEKER', pays : maroc).save();
                new Ville(intitule : 'BOUFEKRANE', pays : maroc).save();
                new Ville(intitule : 'CHEFCHAOUEN', pays : maroc).save();
                new Ville(intitule : 'DAKHLA', pays : maroc).save();
                new Ville(intitule : 'EL JADIDA', pays : maroc).save();
                new Ville(intitule : 'ESSAOUIRA', pays : maroc).save();
                new Ville(intitule : 'FÈS', pays : maroc).save();
                new Ville(intitule : 'GOULIMIME', pays : maroc).save();
                new Ville(intitule : 'GOULMINA', pays : maroc).save();
                new Ville(intitule : 'IFRANE', pays : maroc).save();
                new Ville(intitule : 'INEZGANE', pays : maroc).save();
                new Ville(intitule : 'JERADA', pays : maroc).save();
                new Ville(intitule : 'KHOURIBGA', pays : maroc).save();
                new Ville(intitule : 'MEKNÈS', pays : maroc).save();
                new Ville(intitule : 'MIDELT', pays : maroc).save();
                new Ville(intitule : 'MIRLEFT', pays : maroc).save();
                new Ville(intitule : 'NADOR', pays : maroc).save();
                new Ville(intitule : 'OUARZAZATE', pays : maroc).save();
                new Ville(intitule : 'OUEZZANE', pays : maroc).save();
                new Ville(intitule : 'OUED EL HEIMER', pays : maroc).save();
                new Ville(intitule : 'OUJDA', pays : maroc).save();
                new Ville(intitule : 'OUZOUD', pays : maroc).save();
                new Ville(intitule : 'CHELLA', pays : maroc).save();
                new Ville(intitule : 'RICH', pays : maroc).save();
                new Ville(intitule : 'SAFI', pays : maroc).save();
                new Ville(intitule : 'SAÏDIA', pays : maroc).save();
                new Ville(intitule : 'SALÉ', pays : maroc).save();
                new Ville(intitule : 'SETTAT', pays : maroc).save();
                new Ville(intitule : 'SIDI IFNI', pays : maroc).save();
                new Ville(intitule : 'TAFILALET', pays : maroc).save();
                new Ville(intitule : 'TAFORALT', pays : maroc).save();
                new Ville(intitule : 'TAFRAOUTE', pays : maroc).save();
                new Ville(intitule : 'TAGHAZOUT', pays : maroc).save();
                new Ville(intitule : 'TANGER', pays : maroc).save();
                new Ville(intitule : 'TAROUDANT', pays : maroc).save();
                new Ville(intitule : 'TAZA', pays : maroc).save();
                new Ville(intitule : 'TÉMARA', pays : maroc).save();
                new Ville(intitule : 'TÉNÈS', pays : maroc).save();
                new Ville(intitule : 'TÉTOUAN', pays : maroc).save();
                new Ville(intitule : 'TIMIDERTE', pays : maroc).save();
                new Ville(intitule : 'TINEGHIR', pays : maroc).save();
                new Ville(intitule : 'TIZNIT', pays : maroc).save();
                new Ville(intitule : 'TOUISSIT', pays : maroc).save();
                new Ville(intitule : 'VALLÉE DU DRÂA', pays : maroc).save();
                def zagora = new Ville(intitule : 'ZAGORA', pays : maroc).save();
                def villeInconnue = new Ville(intitule : 'INCONNU', pays : maroc).save();
                
                def villeCasablanca = new Ville(intitule : 'CASABLANCA',pays :maroc ).save()
                def villeKenitra = new Ville(intitule : 'KENITRA',pays :maroc ).save()
                def villeRabat = new Ville(intitule : 'RABAT',pays :maroc ).save()
                def villeMarrakech = new Ville(intitule : 'MARRAKECH',pays :maroc ).save()
                
                def villeTunis = new Ville(intitule : 'TUNIS',pays :paysTunisie ).save()
                
                //nationalité
                def nationaliteMarocaine =new Nationalite(intitule : 'MAROCAINE').save()
                def nationaliteTunisienne =new Nationalite(intitule : 'TUNISIENNE').save()
                def nationaliteEgyptienne =new Nationalite(intitule : 'EGYPTIENNE').save()
                def nationaliteAlgerienne =new Nationalite(intitule : 'ALHERIENNE').save()
         
                //Source information 
                def sourceInformationLematin = new SourceInformation(intitule : 'Le matin').save()
                def sourceInformationLopinion = new SourceInformation(intitule : 'L\'opinion').save()
                new SourceInformation(intitule : 'Site web').save()
                new SourceInformation(intitule : 'Panneau publicitaire').save()
                new SourceInformation(intitule : 'Bouche à oreil').save()
                def sourceInformationAutre = new SourceInformation(intitule : 'Autre').save()
                
                //canal
                def canalAppel = new Canal(intitule : 'Appel téléphonique').save()
                def canalVisite = new Canal(intitule : 'Visite sur les lieux').save()
                def canalVisiteShowroom = new Canal(intitule : 'Visite show room').save()
                def canalEvenement = new Canal(intitule : 'Evénement').save()
                def canalSite = new Canal(intitule : 'Visite site web').save()
                new Canal(intitule : 'Forum').save()
                
                //Mode de paiement                                                                
                
                def modePaiementCheque =  new ModePaiement(type : 'CHEQUE').save()
                new ModePaiement(type : 'CHEQUE BEN').save()
                new ModePaiement(type : 'CHEQUE BERR').save()
                new ModePaiement(type : 'CHEQUE GR').save()
                new ModePaiement(type : 'CHEQUE OM').save()
                
                new ModePaiement(type : 'E OM / C OM').save()
                
                new ModePaiement(type : 'EFFET').save()

                def modePaiementEspece =  new ModePaiement(type : 'ESPECE').save()
                
                new ModePaiement(type : 'ESPECE BEN').save()
                new ModePaiement(type : 'ESPECE BERR').save()
                new ModePaiement(type : 'ESPECE OM').save()
                
                new ModePaiement(type : 'C OM / E OM').save()
                new ModePaiement(type : 'C GR / E BEN').save()
                
                new ModePaiement(type : 'LETTRE DE CHANGE GR').save()
                new ModePaiement(type : 'LETTRE DE CHANGE OM').save()
                
                new ModePaiement(type : 'V OM / E OM').save()
                
                
                def modePaiementVersement =  new ModePaiement(type : 'VIREMENT').save()
                new ModePaiement(type : 'VIREMENT BEN').save()
                new ModePaiement(type : 'VIREMENT GR').save()
                new ModePaiement(type : 'VIREMENT OM').save()

                
                
                



                  
                //client 
                //                def client1 = new Client(adresse : 'HY al QODS, RES Mustapha lamaani, IMM B',cin : 'BB00 001',commentaire : 'respect des delais de paiement', dateContact : new Date('01/09/2011'),dateModification : new Date('01/09/2011'),dateNaissance : new Date('03/01/1982'), fixe : '512345678', gsm : '663111111',mail : 'kamal@test.com',nom:'ABER', prenom : 'Kamal',sexe : 'M',statut : statutProspectClient,nationalite : nationaliteMarocaine, ville : villeRabat).save()
                //                def client2 = new Client(adresse : '431, HY al amal 3, Bernoussi',cin : 'BB00 002',commentaire : 'retardement du paiement', dateContact : new Date('05/05/2010'),dateModification : new Date('01/09/2011'),dateNaissance : new Date('01/03/1985'), fixe : '512345678', gsm : '663111112',mail : 'rabii@test.com',nom:'AZIZ', prenom : 'Rabii',sexe : 'M',statut : statutClientClient,nationalite : nationaliteTunisienne, ville : villeTunis).save()
                //                def client3 = new Client(adresse : 'BV Zerktouni, IMM A25',cin : 'BB00 003',commentaire : 'prospect interessé par plusieurs biens', dateContact : new Date('15/03/209'),dateModification : new Date('01/09/2011'),dateNaissance : new Date('05/10/1980'), fixe : '512345678', gsm : '663111113',mail : 'ahmed@test.com',nom:'AHMED', prenom : 'Ahmed',sexe : 'M',statut : statutProspectClient,nationalite : nationaliteMarocaine, ville : villeRabat).save()
                //                def client4 = new Client(adresse : 'BV Test, IMM 52L',cin : 'BB00 004',commentaire : 'client souvent en deplacement', dateContact : new Date('20/03/2011'),dateModification : new Date('01/09/2011'),dateNaissance : new Date('03/01/1962'), fixe : '512345678', gsm : '663111113',mail : 'mahmoud@test.com',nom:'EL MOSTAPHA', prenom : 'Mahmoud',sexe : 'M',statut : statutClientClient,nationalite : nationaliteMarocaine, ville : villeKenitra).save()
                //                def client5 = new Client(adresse : 'Ain seebaa IMM 15B',cin : 'BB00 005',commentaire : 'paiement à l avance', dateContact : new Date('01/09/2011'),dateModification : new Date('01/09/2011'),dateNaissance : new Date('03/01/1968'), fixe : '512345678', gsm : '663111114',mail : 'mohammed@test.com',nom:'CHADI', prenom : 'Mohammed',sexe : 'M',statut : statutClientClient,nationalite : nationaliteMarocaine, ville : villeMarrakech).save()
                //                                def client52 = new Client(adresse : 'Ain seebaa IMM 15B2',cin : 'BB00 0052',commentaire : 'paiement à l avance', dateContact : new Date('01/09/2011'),dateModification : new Date('01/09/2011'),dateNaissance : new Date('03/01/1968'), fixe : '512345678', gsm : '663111114',mail : 'mohammed@test.com',nom:'CHADI', prenom : 'Mohammed',sexe : 'M',statut : statutClientClient,nationalite : nationaliteMarocaine, ville : villeMarrakech).save()
                //                def client53 = new Client(adresse : 'Ain seebaa IMM 15B',cin : 'BB00 0053',commentaire : 'paiement à l avance', dateContact : new Date('01/09/2011'),dateModification : new Date('01/09/2011'),dateNaissance : new Date('03/01/1968'), fixe : '512345678', gsm : '663111114',mail : 'mohammed@test.com',nom:'CHADI', prenom : 'Mohammed',sexe : 'M',statut : statutClientClient,nationalite : nationaliteMarocaine, ville : villeMarrakech).save()
                //                def client54 = new Client(adresse : 'Ain seebaa IMM 15B',cin : 'BB00 0054',commentaire : 'paiement à l avance', dateContact : new Date('01/09/2011'),dateModification : new Date('01/09/2011'),dateNaissance : new Date('03/01/1968'), fixe : '512345678', gsm : '663111114',mail : 'mohammed@test.com',nom:'CHADI', prenom : 'Mohammed',sexe : 'M',statut : statutClientClient,nationalite : nationaliteMarocaine, ville : villeMarrakech).save()
                //                def client55 = new Client(adresse : 'Ain seebaa IMM 15B',cin : 'BB00 0055',commentaire : 'paiement à l avance', dateContact : new Date('01/09/2011'),dateModification : new Date('01/09/2011'),dateNaissance : new Date('03/01/1968'), fixe : '512345678', gsm : '663111114',mail : 'mohammed@test.com',nom:'CHADI', prenom : 'Mohammed',sexe : 'M',statut : statutClientClient,nationalite : nationaliteMarocaine, ville : villeMarrakech).save()
                //                def client56 = new Client(adresse : 'Ain seebaa IMM 15B',cin : 'BB00 0056',commentaire : 'paiement à l avance', dateContact : new Date('01/09/2011'),dateModification : new Date('01/09/2011'),dateNaissance : new Date('03/01/1968'), fixe : '512345678', gsm : '663111114',mail : 'mohammed@test.com',nom:'CHADI', prenom : 'Mohammed',sexe : 'M',statut : statutClientClient,nationalite : nationaliteMarocaine, ville : villeMarrakech).save()
                //                
                //notaire
                //                def notaire1 = new Notaire(adresse : '256, BV Test, IMM B55',fax : '512345679',fixe : '512345678',gsm : '661378933',idFiscal : '1234656',nom : 'Rachidi',patente : '987654',prenom : 'Ahmed', email : 'ahmed@notaire.org').save()
                //                def notaire2 = new Notaire(adresse : '55, BV Azerty, IMM 33',fax : '512345676',fixe : '512345678',gsm : '661378922',idFiscal : '1234656',nom : 'Mohamed',patente : '987654',prenom : 'Rachid', email : 'rachid@notaire.org').save()
                //                def notaire3 = new Notaire(adresse : 'HY Test, BV Test, IMM 142',fax : '512345675',fixe : '512345678',gsm : '661378955',idFiscal : '1234656',nom : 'Taja',patente : '987654',prenom : 'Soufiane', email : 'soufiane@notaire.org').save()
                //                
                //modele
                
                //def modeleGTIA = new Modele(code : "GTIA").save()
                def modeleLIA = new Modele(code : "L-I-A").save()
                //def modeleIA = new Modele(code : "IA").save()
                def modelTBEA = new Modele(code : "T-B-E-A").save()
                def modelTGHIEA= new Modele(code : "T-GH-I-E-A").save()                   
                def modelLL = new Modele(code : "L-L").save()
                def modelTIEA= new Modele(code : "T-I-E-A").save() 
                //type                 
                
                def typeHS = new Type(intitule : 'HAUT STANDING', type : "PROJET" ).save()  
                def typeMS = new Type(intitule : 'MOYEN STANDING', type : "PROJET" ).save()  
                def typeSoc = new Type(intitule : 'SOCIAL', type : "PROJET" ).save()  
                def typeEco = new Type(intitule : 'ECONOMIQUE', type : "PROJET" ).save()  
                
                def typeGroupeH = new Type(intitule : 'GH', type : "BIEN" ).save()  
                def typeTranche = new Type(intitule : 'TRANCHE', type : "BIEN" ).save()  
                def typeImmeuble = new Type(intitule : 'IMMEUBLE', type : "BIEN" ).save()  
                def typeBatiment = new Type(intitule : 'BATIMENT', type : "BIEN" ).save()  
                def typeEtage = new Type(intitule : 'ETAGE', type : "BIEN" ).save()  
                def typeVilla = new Type(intitule : 'VILLA', type : "BIEN" ).save()  
                def typeMagasin = new Type(intitule : 'MAGASIN', type : "BIEN" ).save()  
                def typeAppartement = new Type(intitule : 'APPARTEMENT', type : "BIEN" ).save()  
                
                //architecture bassatine kénitra
                def typeLotP = new Type(intitule : 'LOT', type : "BIEN" ).save()  
                def typeLot = new Type(intitule : 'LOTISSEMENT', type : "BIEN" ).save()  
                
                
                def ModeleTypeL1 = new ModeleType(modele : modelLL, type : typeLotP , numOrdre : 1).save()
                def ModeleTypeL2 = new ModeleType(modele : modelLL, type : typeLot, numOrdre : 2).save()
                
                //ModeleType
                def ModeleTypeLIA1 = new ModeleType(modele : modeleLIA, type : typeLot, numOrdre : 1).save()
                def ModeleTypeLIA2 = new ModeleType(modele : modeleLIA, type : typeImmeuble, numOrdre : 2).save()
                def ModeleTypeLIA3 = new ModeleType(modele : modeleLIA, type : typeAppartement, numOrdre : 3).save()
                
                def ModeleTypeTIEA1 = new ModeleType(modele : modelTIEA, type : typeTranche, numOrdre : 1).save()
                def ModeleTypeTIEA2 = new ModeleType(modele : modelTIEA, type : typeImmeuble, numOrdre : 2).save()
                def ModeleTypeTIEA3 = new ModeleType(modele : modelTIEA, type : typeEtage, numOrdre : 3).save()
                def ModeleTypeTIEA4 = new ModeleType(modele : modelTIEA, type : typeAppartement, numOrdre : 4).save()
                
                
                def mtt = new ModeleType(modele : modelTGHIEA, type : typeTranche, numOrdre : 1).save()
                def mgh = new ModeleType(modele : modelTGHIEA, type : typeGroupeH, numOrdre : 2).save()
                def mtb = new ModeleType(modele : modelTGHIEA, type : typeImmeuble, numOrdre : 3).save()
                def mte = new ModeleType(modele : modelTGHIEA, type : typeEtage, numOrdre : 4).save()
                def mta = new ModeleType(modele : modelTGHIEA, type : typeAppartement, numOrdre : 5).save()
                
                
                //projet
                //                def projetTmaraGarden = new Projet(adresse : 'Temara, 55 bv Test',designation : 'Témara Garden',code : 'TG001',type : typeHS,ville : villeRabat, modele : modeleLIA,statut : statutPlannifieProjet, groupeClient : clientInfo, reservationEnLigne : true, generationOptionEnLigne : true , plan : null, delaiPhase : 30 ).save()
                //                def projetBasatinekenitra = new Projet(adresse : 'Secteur Fouarat dans la commune urbaine de Kenitra',designation : 'Bassatine Kénitra',code : 'BA001',statut : statutEncoursProjet,type : typeMS,ville : villeKenitra,modele : modelLL, groupeClient : clientInfo, reservationEnLigne : true, generationOptionEnLigne : true , plan : "Lotissementkenitra-2791-800.jpg", delaiPhase : 30 ).save() 
                //                //def projetBB = new Projet(adresse : '431, qaurtier Test',designation : 'Bab el baida',code : 'RC002',statut : statutPlannifieProjet,type : typeMS,ville : villeCasablanca,modele : modeleLIA, groupeClient : clientInfo, reservationEnLigne : true, generationOptionEnLigne : true , plan : null, delaiPhase : 30 ).save() 
                //                def projetBasatineCasablanca = new Projet(adresse : '431, qaurtier Test',designation : 'Bassatine Casablanca',code : 'BC002',statut : statutPlannifieProjet,type : typeMS,ville : villeCasablanca,modele : modeleLIA, groupeClient : clientInfo, reservationEnLigne : true, generationOptionEnLigne : true , plan : null, delaiPhase : 30 ).save() 
                //                def projetBasatineRabat = new Projet(adresse : 'Adresse Rabat, N55',designation : 'Bassatine Rabat',code : 'BM003',statut : statutPlannifieProjet, type : typeHS,ville : villeRabat,modele : modeleLIA, groupeClient : clientInfo, reservationEnLigne : true, generationOptionEnLigne : true , plan : null, delaiPhase : 30 ).save() 
                //                def projetImmeubleOmni = new Projet(adresse : 'Adresse Rabat, N55',designation : 'Marrakech Garden',code : 'BR003',statut : statutTermineProjet, type : typeMS,ville : villeMarrakech,modele :modelTBEA, groupeClient : clientInfo, reservationEnLigne : true, generationOptionEnLigne : true , plan : null, delaiPhase : 30 ).save() 
                // def projetBB = new Projet(adresse : 'BOUSKOURA',designation : 'BAB AL BIDA',code : 'BB',statut : statutEncoursProjet, type : typeEco,ville : villeCasablanca,modele :modelTGHIEA, groupeClient : clientInfo, reservationEnLigne : false, generationOptionEnLigne : false , plan : null, delaiPhase : 30 ).save() 
                
                //                def projetToDelete = new Projet(adresse : 'Adresse Rabat, N55',designation : 'Project to delete',code : 'PRJ666',statut : statutTermineProjet, type : typeMS,ville : villeMarrakech,modele :modelTBEA, groupeClient : clientInfo, reservationEnLigne : true, generationOptionEnLigne : true , plan : null, delaiPhase : 30 ).save() 
                //                def projetToDelete2 = new Projet(adresse : 'Adresse Rabat, N55',designation : 'Project to delete2',code : 'PRJ6662',statut : statutTermineProjet, type : typeMS,ville : villeMarrakech,modele :modelTBEA, groupeClient : clientInfo, reservationEnLigne : true, generationOptionEnLigne : true , plan : null, delaiPhase : 30 ).save() 
                //                def projetToDelete3 = new Projet(adresse : 'Adresse Rabat, N55',designation : 'Project to delete3',code : 'PRJ6663',statut : statutTermineProjet, type : typeMS,ville : villeMarrakech,modele :modelTBEA, groupeClient : clientInfo, reservationEnLigne : true, generationOptionEnLigne : true , plan : null, delaiPhase : 30 ).save() 
                //                def projetToDelete4 = new Projet(adresse : 'Adresse Rabat, N55',designation : 'Project to delete4',code : 'PRJ6664',statut : statutTermineProjet, type : typeMS,ville : villeMarrakech,modele :modelTBEA, groupeClient : clientInfo, reservationEnLigne : true, generationOptionEnLigne : true , plan : null, delaiPhase : 30 ).save() 
                //                
               
                //add user projeaddcts
                //                userRoot.addToProjets(projetTmaraGarden) 
                //                userRoot.addToProjets(projetBasatinekenitra) 
                //                userRoot.addToProjets(projetBB) 
                //                userRoot.addToProjets(projetBasatineCasablanca) 
                //                userRoot.addToProjets(projetBasatineRabat) 
                //                userRoot.addToProjets(projetImmeubleOmni) 
                                
                //add user clients
                //                userRoot.addToClients(client1) 
                //                userRoot.addToClients(client2) 
                //                userRoot.addToClients(client3)
                //                userRoot.addToClients(client4)
                //                userRoot.addToClients(client5)

                
                userRoot = utilisateurService.update(userRoot)
            }
                                            
            production {
        
            }
            
            test {
        
            }
              
        }
                
    }
    def destroy = {
    }
}
