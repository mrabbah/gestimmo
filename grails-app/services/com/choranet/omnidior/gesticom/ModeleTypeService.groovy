/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.choranet.omnidior.gesticom

/**
 *
 * @author kamal
 */
class ModeleTypeService extends SuperService{
    static transactional = true

    def list() throws Exception {
        return super.list(ModeleType.class)
    }
    
    def getTypeByModele(md){
        def criteria = ModeleType.createCriteria()
        def result = criteria.list {
            eq("modele", md)
            order("numOrdre","asc")
        }
        
        return result 
    }
    
    def getTypeParent(modele,type){
        def i =  0
        def indexMod = -1
        def criteria = ModeleType.createCriteria()
        def result = criteria.list {
            eq("modele",modele)
        }
        
        if (result != null && result.size()> 0) 
        {
            result.each
            { rs ->
                
                if (rs.type.intitule == type){
                    indexMod = i-1
                }
                i = i+ 1
            }
        
            if (indexMod >= 0){

                return result[indexMod]   
            }
        }
         
    }
    
    def verifierModeleType(ModeleType mt){
        
        def MessageError = "null"
        def criteria = ModeleType.createCriteria()
        def result = criteria.list {
            eq("modele", mt.modele)
            order("numOrdre","asc")
        }
        
        if (result != null && result.size()>0 ) {

            for (it in result){
                if(it.id != mt.id){
                    if (it.type.intitule == mt.type.intitule){

                        MessageError = "Le type "+ mt.type + " existe déjà dans le modèle " + mt.modele      
                        break;
                    }
                    if (it.numOrdre == mt.numOrdre){   

                        MessageError = "Le numéro d'ordre "+ mt.numOrdre + " existe déjà dans le modèle " + mt.modele  
                        break;
                    }
                }
                //                else if(action == "update"){

                //                    if (l.type == mt.type && l.id != mt.id){
                //                        MessageError = "Le type "+ mt.type + " existe déjà dans le modèle " + mt.modele
                //                        break
                //                    }
                //                    if (l.numOrdre == mt.numOrdre && l.id != mt.id){
                //                        MessageError = "Le numéro d'ordre "+ mt.numOrdre + " existe déjà dans le modèle " + mt.modele
                //                        break
                //                    }
                //                } //end lse if(action == "update")
            }// end closure
        } // end if (result)
        return MessageError
    }

}   
