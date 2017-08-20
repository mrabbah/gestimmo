package com.choranet.omnidior.gesticom

import org.codehaus.groovy.grails.plugins.orm.auditable.AuditLogEvent

class AuditLogEventService extends SuperService {

    static transactional = true

    def list() throws Exception {
        return super.list(AuditLogEvent.class)
    }
    
}
