class UrlMappings {

	static excludes = ['/zkau/*']
        
	static mappings = {
                
                "/auditLogEvent"(uri:'/zul/errors/error404.zul')
		"/$controller/$action?/$id?"{
			constraints {
				// apply constraints here
			}
		}
                
		"/"(controller:"choraBarrage")
		"500"(uri:'/zul/errors/error500.zul')
                "404"(uri:'/zul/errors/error404.zul')
                "403"(uri:'/zul/errors/error404.zul')
	}
}
