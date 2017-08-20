security {

    // see DefaultSecurityConfig.groovy for all settable/overridable properties

    active = true

    loginUserDomainClass = "com.choranet.omnidior.gesticom.Utilisateur"
    authorityDomainClass = "com.choranet.omnidior.gesticom.GroupeUtilisateur"
    requestMapClass = "com.choranet.omnidior.gesticom.DroitUtilisateur"
        
    authenticationFailureUrl = '/login.zul?login_error=true'
    loginFormUrl = '/login.zul'
    cookieName = 'gestimmo_remember_me'
    errorPage = '/zul/errors/denied.zul'
    defaultTargetUrl = '/zul/main.zul'
}
