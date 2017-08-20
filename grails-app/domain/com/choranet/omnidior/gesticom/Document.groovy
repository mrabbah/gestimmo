

package com.choranet.omnidior.gesticom


import java.io.IOException
import org.zkoss.image.AImage
import org.zkoss.image.Image
import com.choranet.omnidior.gesticom.util.Utilitaire
import java.io.Serializable;

/**
 * Type Domain Object 
 */
class Document implements Serializable {	
    private static final long serialVersionUID = 1L;
    	
    String intitule	 
    
    byte[] documentNumerise
    
    Image trans_document    
    
    Integer tempCode 
    
    static transients = ['trans_document']
    
    static mapping = { 
        intitule index : "document_intitule"
        batchSize: 14 
    }
   
    static constraints = {
    
        intitule(blank : false, nullable : false)
        documentNumerise()
        tempCode()
    }
	
    String toString() {
        return intitule
    }
    
    public void setDocumentNumerise(byte[] bd) {
        documentNumerise = bd
//        if(documentNumerise != null) {
//            //trans_document = new AImage(this.intitule, bd);
//            OutputStream out = new FileOutputStream("out.pdf");
//            out.write(documentNumerise);
//            out.close();
//        }
    }

    public void setTrans_document(Image img) {        
        if(img == null) {            
            img = Utilitaire.getDumpDocImage(getClass())
        }
        trans_document = img
        documentNumerise = trans_document.getByteData()     
    }
    
}