
package com.choranet.omnidior.gesticom.util;

import java.io.File;
import java.io.InputStream;
import org.zkoss.image.AImage;
import org.zkoss.image.Image;

/**
 *
 * @author RABBAH
 */
public class Utilitaire {

    private static String pathToLogoImage = "com/choranet/omnidior/gesticom/images/logo.png";

    public static Image getLogoImage(Class clazz) throws Exception {
        ClassLoader cl = clazz.getClassLoader();
        InputStream is;
        try {
            is = cl.getResourceAsStream(pathToLogoImage);
        } catch (Exception e) {
            throw new Exception("Impossible d obtenir l image logo", e);
        }
        Image img = new AImage("logo", is);
        return img;
    }

    public static Boolean  validateEmail(String email) {
        if (email == null || !email.matches(".+@.+\\.[a-z]+")) {
            return false;
        }
        else
        {
            return true;
        }
        
    }
}
