/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.choranet.omnidior.gesticom.util;

/**
 *
 * @author kamal
 */
public class BienData {

    public final String bien;
    public final String etat;
  
    public BienData(String bien, String etat) {
        this.bien = bien;
        this.etat = etat;
    }


//    public String getBien() {
//        return bien;
//    }
//
//    public String getEtat() {
//        return etat;
//    }
    
    public String tostring(){
        return this.bien + " " + this.etat;
    }

}
