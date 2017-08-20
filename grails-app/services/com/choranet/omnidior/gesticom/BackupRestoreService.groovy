
package com.choranet.omnidior.gesticom

import grails.converters.*;
import java.io.File;
import java.io.FileWriter;
import java.io.FileInputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Date;
import java.util.Locale;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import com.choranet.omnidior.gesticom.util.BackupDirectoryFilter;
import org.hibernate.ReplicationMode;
import com.choranet.commun.*

class BackupRestoreService {

    def sessionFactory
    
    static transactional = true

    def cleanDataBase() throws Exception {
        try {
            def session = sessionFactory.getCurrentSession()
            session.clear()  

            Parametrage.executeUpdate("delete Parametrage parametrage")

            PriseInformation.executeUpdate("delete PriseInformation priseInformation")

            Tranche.executeUpdate("delete Tranche tranche")

            Bien.executeUpdate("delete Bien bien")

            ModePaiement.executeUpdate("delete modePaiement modePaiement")

            DroitsUtilisateur.executeUpdate("delete DroitsUtilisateur droitsUtilisateur")

            Canal.executeUpdate("delete Canal canal")

            Notaire.executeUpdate("delete Notaire notaire")

            Annulation.executeUpdate("delete Annulation annulation")

            Pays.executeUpdate("delete Pays pays")

            Alerte.executeUpdate("delete Alerte alerte")

            Projet.executeUpdate("delete Projet projet")

            Type.executeUpdate("delete Type type")

            Client.executeUpdate("delete Client client")

            Phase.executeUpdate("delete Phase phase")

            SourceInformation.executeUpdate("delete SourceInformation sourceInformation")

            Echeance.executeUpdate("delete Echeance echeance")

            Contenance.executeUpdate("delete Contenance contenance")

            Ville.executeUpdate("delete Ville ville")

            Standing.executeUpdate("delete Standing standing")

            Paiement.executeUpdate("delete Paiement paiement")

            Nationalite.executeUpdate("delete Nationalite nationalite")
          
            DroitUtilisateur.executeUpdate("delete DroitUtilisateur d") 
            session.createSQLQuery("delete from utilisateur_authorities").executeUpdate();
            Utilisateur.executeUpdate("delete Utilisateur uts")
            ChoraClientInfo.executeUpdate("delete ChoraClientInfo ci")
            session.flush()
            session.clear()
        } catch(Exception ex) {
            throw new Exception("Impossible de supprimer la base de donnée courante", ex)
        }
    }
    
    def bulkInsert(objets) throws Exception {

        def session = sessionFactory.getCurrentSession()

        try {
            def batch = []
            objets.each { objet ->
                batch.add(objet)
                if(batch.size() > 1000) {
                    objet.getClass().withTransaction {
                        for(o in batch) {
                            session.replicate(o, ReplicationMode.OVERWRITE);
                        }
                    }
                    batch.clear()
                    session.flush()
                    session.clear()        
                }
            }
            if(batch.size() > 0) {
                batch[0].getClass().withTransaction {
                    for(o in batch) {
                        session.replicate(o, ReplicationMode.OVERWRITE);
                    }
                }
                batch.clear()
                session.flush()
                session.clear()  
            }
        } catch(Exception ex) {
            throw new Exception("Impossible d'insérer dans la base de données les objets : " + objets, ex)
        }
    }
    
    def xmlStringToObjets(xmls) throws Exception {
        def objets = null
        try {
            objets = XML.parse(xmls)
        } catch (Exception ex) {
            throw new Exception("Problème lors de la convesion de la chaine " + xmls + " en objets", ex)
        }
        return objets
    }
    
    def xmlFileToObjets(File file) throws Exception {
        def objets = null
        try {
            FileInputStream fis = new FileInputStream(file)
            objets = XML.parse(fis, "UTF-8")
        } catch (Exception ex) {
            throw new Exception("Le fichier " + file.getAbsolutePath() + " est corrompu", ex)
        }
        return objets
    }
    
    def sauveguarderBdD() throws Exception {
        def info = ChoraClientInfo.get(1)
        if(info == null) {
            throw new Exception("Aucune informations sur le client trouvé au niveau de la Base")
        } else if(info.repertoirBackup == null || info.repertoirBackup.equals("")) {
            throw new Exception("Vous devez indiquer un répértoir pour la sauveguarde de vos données")
        } else {
            File f;
            try {
                f = new File(info.repertoirBackup)
            } catch(Exception e) {
                throw new Exception("Le chemin indiqué n'est pas une répértoir valide : " + info.repertoirBackup, e)
            }
            if(!f.exists()) {
                throw new Exception("Le chemin indiqué n'est pas une répértoir valide : " + info.repertoirBackup)
            }
            if(!f.isDirectory()) {
                throw new Exception("Le chemin indiqué n'est pas un répértoire : " + info.repertoirBackup)
            }
            if(!f.canWrite()) {
                throw new Exception("Vous n'avez pas le droit d'écriture sur le répértoire : " + info.repertoirBackup)
            }
         
        }
        String FS = System.getProperty("file.separator");
        Date d = new Date()
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmm");
        String fullPathToBackupDirectory = info.repertoirBackup + FS + "BACKUP" + dateFormat.format(d);
        File directoryBackup = new File(fullPathToBackupDirectory);
        directoryBackup.mkdirs();
        
        def cci = ChoraClientInfo.list()
        def du = DroitUtilisateur.list()
        def utilisateurs = Utilisateur.list()

        def parametrage = Parametrage.list()

        def priseInformation = PriseInformation.list()

        def tranche = Tranche.list()

        def bien = Bien.list()

        def modePaiement = ModePaiement.list()

        def canal = Canal.list()

        def notaire = Notaire.list()

        def annulation = Annulation.list()

        def pays = Pays.list()

        def alerte = Alerte.list()

        def projet = Projet.list()

        def type = Type.list()

        def client = Client.list()

        def phase = Phase.list()

        def sourceInformation = SourceInformation.list()

        def echeance = Echeance.list()

        def contenance = Contenance.list()

        def ville = Ville.list()

        def standing = Standing.list()

        def paiement = Paiement.list()

        def nationalite = Nationalite.list()
        
        
        File backupFile = new File(fullPathToBackupDirectory + FS + "chorainformatiquebackup.backup" );
        backupFile.createNewFile();
        println backupFile.getAbsolutePath()
        backupFile.withObjectOutputStream { out ->
            out << cci
            out << gu
            out << utilisateurs
            out << du

        out << parametrage

        out << priseInformation

        out << tranche

        out << bien

        out << modePaiement

       
        out << canal

        out << notaire

        out << annulation

        out << pays

        out << alerte

        out << projet

        out << type

        out << client

        out << phase

        out << sourceInformation

        out << echeance

        out << contenance

        out << ville

        out << standing

        out << paiement

        out << nationalite
 
        }
    }
    
    def getListeDesSauveguardes() {
        def info = ChoraClientInfo.get(1)
        def result = []
        if(info != null && info.repertoirBackup != null && !info.repertoirBackup.equals("")) {
            File f = new File(info.repertoirBackup);
            if(f.exists() && f.isDirectory() && f.canRead()) {
                result = f.list(new BackupDirectoryFilter());
            } 
        } 
        return result;
    }
    
    def restaurerDb(backupname) throws Exception {
        def info = ChoraClientInfo.get(1)
        if(info != null && info.repertoirBackup != null && !info.repertoirBackup.equals("")) {
            File f = new File(info.repertoirBackup);
            if(f.exists() && f.isDirectory() && f.canRead()) {
                String FS = System.getProperty("file.separator");
                String fullPathToBackupDirectory = info.repertoirBackup + FS + backupname;
                File directoryBackup = new File(fullPathToBackupDirectory);
                if(directoryBackup.exists() && directoryBackup.isDirectory() && directoryBackup.canRead()) {
                    File backupFile = new File(fullPathToBackupDirectory + FS + "chorainformatiquebackup.backup" );
                    if(!(backupFile.exists() && backupFile.canRead())) {
                        throw new Exception("Le fichier chorainformatiquebackup.backup n'existe pas ou n'est pas en lecture" )
                    } 
                    //Vérification du fichier de sauveguarde
                    //TODO
                    //                    int compteur = 0;
                    //                    try {
                    //                        backupFile.withObjectInputStream(getClass().classLoader) { instream ->
                    //                            instream.eachObject { 
                    //                                compteur++;
                    //                            }
                    //                        }
                    //                        if(compteur != 20) {
                    //                            throw new Exception("Le fichier chorainformatiquebackup.backup est invalide");
                    //                        }
                    //                    } catch(Exception ex) {
                    //                        throw ex;
                    //                    }
                    
                    //Apres verification supression de la base actuelle
                    cleanDataBase()
                    //lecture des fichiers
                    try {
                        backupFile.withObjectInputStream(getClass().classLoader) { instream ->
                            instream.eachObject { 
                                bulkInsert(it)
                            }
                        }
                    } catch(Exception ex) {
                        ex.printStackTrace()
                        throw ex
                    }
                    
                } else {
                    throw new Exception("Le répertoire de backup : " + fullPathToBackupDirectory + " n'existe pas ou n'est pas en lecture") 
                }
            }else {
                throw new Exception("Le répertoire de backup : " + info.repertoirBackup + " n'existe pas ou n'est pas en lecture") 
            }
        } else {
            throw new Exception("Vous devez indiquer un répértoir pour la sauveguarde de vos données")
        }
    }
}
