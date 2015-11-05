class UrlMappings {

	static mappings = {
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }
		
		"/$qrcode" {
			controller = "Admin"
			action = "codeQR"
			constraints { qrcode(matches:/[0-9a-f]{32}/) }
		}

    "/"(controller:"Admin")
	"500"(view:'/error')
	"404"(view:'/error')
	"405"(view:'/error')
	}
}
