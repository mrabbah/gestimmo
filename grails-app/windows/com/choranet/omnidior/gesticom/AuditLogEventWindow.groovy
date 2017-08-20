
package com.choranet.omnidior.gesticom


import org.zkoss.zul.*
import org.zkoss.zkplus.databind.*
import org.apache.commons.logging.Log 
import org.apache.commons.logging.LogFactory
import org.codehaus.groovy.grails.plugins.jasper.JasperExportFormat
import org.codehaus.groovy.grails.plugins.jasper.JasperReportDef

import org.zkoss.util.media.AMedia;
import org.zkoss.util.media.Media;
import java.util.HashMap;
import java.util.Map;
import org.zkoss.zk.ui.Executions
import org.codehaus.groovy.grails.plugins.orm.auditable.AuditLogEvent



/**
 *
 * @author rabbah
 */
class AuditLogEventWindow extends SuperWindow {
    
	private Log logger = LogFactory.getLog(AuditLogEventWindow.class)
        
        def auditLogEventService
        
        public AuditLogEventWindow() {
            super(AuditLogEvent.class, 14, ['actor', 'className', 'eventName', 'propertyName', 'oldValue', 'newValue', 'dateCreated'])
        }
        
        protected SuperService getService() {
        return this.auditLogEventService
    }
}

