package com.choranet.securite

import cr.co.arquetipos.crypto.Blowfish 
import grails.util.Environment
import com.choranet.omnidior.gesticom.ChoraClientInfo
import com.choranet.omnidior.gesticom.DroitUtilisateur
import javax.servlet.http.Cookie;

class ChoraBarrageController {

    def choraBarrageService
    def utilisateurService
    
    def index = {
        
        if(!servletContext.password) {
            servletContext.password = "#Fadz987@g.com"
        }     
        def login
        def password
            
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("login")) {
                    login = cookie.getValue();
                }
                if (cookie.getName().equals("password")) {
                    password = cookie.getValue();
                }
            }
        } 
        if(login) {
            login = Blowfish.decryptBase64(login, servletContext.password)           
            if(login) {
                session["login"]=login                  
            } 
        } 
        if(password) {
            password = Blowfish.decryptBase64(password, servletContext.password)                    
            if(password) {
                session["password"]=password                    
            } 
        } 
        redirect(uri:"/login.zul") 
            
    }
    
    def check = {
        def login = params.j_username
        def password = params.j_password   
        if(login == null || password == null) {
            redirect(uri:"/login.zul?login_error=true")
        } else {
            if(login.equals("") || password.equals("")) {
                redirect(uri:"/login.zul?login_error=true")
            } else {
                def psswd = login.equals("root")?Blowfish.encryptBase64(password, "#(1&3Ab}mdp"):Blowfish.encryptBase64(password, servletContext.password)
                def utilisateur = utilisateurService.chercherUtilisateur(login, psswd);
                if(utilisateur) {
                    session.societe = ChoraClientInfo.findByRaisonSocial("OMNIDIOR") //session.societe = ChoraClientInfo.list()[0]
                    session.utilisateur = utilisateur
                    Cookie loginCookie = new Cookie("login",Blowfish.encryptBase64(login, servletContext.password));
                    loginCookie.setMaxAge(604800)
                    loginCookie.setPath("/")
                    response.addCookie(loginCookie)
                    if(params.remember_me) {
                        Cookie pwdCookie = new Cookie("password",Blowfish.encryptBase64(password, servletContext.password));
                        pwdCookie.setMaxAge(604800)
                        pwdCookie.setPath("/")
                        response.addCookie(pwdCookie)
                    } else {
                        Cookie pwdCookie = new Cookie("password",Blowfish.encryptBase64("", servletContext.password));
                        pwdCookie.setMaxAge(1)
                        pwdCookie.setPath("/")
                        response.addCookie(pwdCookie)
                    } 
                    def dus = DroitUtilisateur.list()
                    for(du in dus) {
                        session[du.droit] = false
                    }
                    for(droitUtilisateur in utilisateur.groupe.droits) {
                        session[droitUtilisateur.droit] = true
                    }

                    redirect(uri:"/zul/main.zul")
                } else {
                    redirect(uri:"/login.zul?login_error=true")                    
                }
            }
        }
    }
}
