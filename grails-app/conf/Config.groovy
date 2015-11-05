// locations to search for config files that get merged into the main config;
// config files can be ConfigSlurper scripts, Java properties files, or classes
// in the classpath in ConfigSlurper format

// grails.config.locations = [ "classpath:${appName}-config.properties",
//                             "classpath:${appName}-config.groovy",
//                             "file:${userHome}/.grails/${appName}-config.properties",
//                             "file:${userHome}/.grails/${appName}-config.groovy"]

// if (System.properties["${appName}.config.location"]) {
//    grails.config.locations << "file:" + System.properties["${appName}.config.location"]
// }
grails.config.locations = [
	"file:${userHome}/.ar4k/${appName}-config.groovy"
]

grails.project.groupId = appName // change this to alter the default package name and Maven publishing destination

// The ACCEPT header will not be used for content negotiation for user agents containing the following strings (defaults to the 4 major rendering engines)
grails.mime.disable.accept.header.userAgents = [
	'Gecko',
	'WebKit',
	'Presto',
	'Trident'
]
grails.mime.types = [ // the first one is the default format
	all:           '*/*', // 'all' maps to '*' or the first available format in withFormat
	atom:          'application/atom+xml',
	css:           'text/css',
	csv:           'text/csv',
	form:          'application/x-www-form-urlencoded',
	html:          [
		'text/html',
		'application/xhtml+xml'
	],
	js:            'text/javascript',
	json:          [
		'application/json',
		'text/json'
	],
	multipartForm: 'multipart/form-data',
	rss:           'application/rss+xml',
	text:          'text/plain',
	hal:           [
		'application/hal+json',
		'application/hal+xml'
	],
	xml:           [
		'text/xml',
		'application/xml']
]

// URL Mapping Cache Max Size, defaults to 5000
//grails.urlmapping.cache.maxsize = 1000

// Legacy setting for codec used to encode data with ${}
grails.views.default.codec = "html"

// The default scope for controllers. May be prototype, session or singleton.
// If unspecified, controllers are prototype scoped.
grails.controllers.defaultScope = 'singleton'


// GSP settings
grails {
	views {
		gsp {
			encoding = 'UTF-8'
			htmlcodec = 'xml' // use xml escaping instead of HTML4 escaping
			codecs {
				expression = 'html' // escapes values inside ${}
				scriptlet = 'html' // escapes output from scriptlets in GSPs
				taglib = 'none' // escapes output from taglibs
				staticparts = 'none' // escapes output from static template parts
			}
		}
		// escapes all not-encoded output at final stage of outputting
		// filteringCodecForContentType.'text/html' = 'html'
	}
}


grails.converters.encoding = "UTF-8"
// scaffolding templates configuration
grails.scaffolding.templates.domainSuffix = 'Instance'

// Set to false to use the new Grails 1.2 JSONBuilder in the render method
grails.json.legacy.builder = false
// enabled native2ascii conversion of i18n properties files
grails.enable.native2ascii = true
// packages to include in Spring bean scanning
grails.spring.bean.packages = []
// whether to disable processing of multi part requests
grails.web.disable.multipart=false

// request parameters to mask when logging exceptions
grails.exceptionresolver.params.exclude = ['password']

// configure auto-caching of queries by default (if false you can cache individual queries with 'cache: true')
grails.hibernate.cache.queries = false

// configure passing transaction's read-only attribute to Hibernate session, queries and criterias
// set "singleSession = false" OSIV mode in hibernate configuration after enabling
grails.hibernate.pass.readonly = false
// configure passing read-only to OSIV session by default, requires "singleSession = false" OSIV mode
grails.hibernate.osiv.readonly = false

environments {
	development {
		grails.logging.jul.usebridge = true
	}
	production {
		grails.logging.jul.usebridge = false
		// TODO: grails.serverURL = "http://www.changeme.com"
	}
}

// log4j configuration
log4j.main = {
	// Example of changing the log pattern for the default console appender:
	//
	//appenders {
	//    console name:'stdout', layout:pattern(conversionPattern: '%c{2} %m%n')
	//}

	error  'org.codehaus.groovy.grails.web.servlet',        // controllers
			'org.codehaus.groovy.grails.web.pages',          // GSP
			'org.codehaus.groovy.grails.web.sitemesh',       // layouts
			'org.codehaus.groovy.grails.web.mapping.filter', // URL mapping
			'org.codehaus.groovy.grails.web.mapping',        // URL mapping
			'org.codehaus.groovy.grails.commons',            // core / classloading
			'org.codehaus.groovy.grails.plugins',            // plugins
			'org.codehaus.groovy.grails.orm.hibernate',      // hibernate integration
			'org.springframework',
			'org.hibernate',
			'net.sf.ehcache.hibernate'
	fatal	'org.hibernate.tool.hbm2ddl.SchemaExport' // bug grails 11198 work around by Ambrosini
	fatal   'org.apache.camel.component.jms.DefaultJmsMessageListenerContainer'
	environments {
				development  {
					info 'grails.app'
					info 'org.codehaus.groovy.grails.web.servlet'
					info 'org.codehaus.groovy.grails.web.sitemesh'
					info 'org.codehaus.groovy.grails.plugins'
					info 'org.codehaus.groovy.grails.commons'
					info 'org.activiti'
					info 'org.ar4k'
					//debug 'org.hibernate'
				}
	}
}


///////////////////////////////////////////////////////////////////////////
///////////// INIZIO CONFIGURAZIONI ROSSONET //////////////////////////////
///////////////////////////////////////////////////////////////////////////

environments {
	development {
	}
	production {
	}
}

oauth {
	providers {
		twitter {
			api = org.scribe.builder.api.TwitterApi
			key = 'oauth_twitter_key'
			secret = 'oauth_twitter_secret'
			successUri = '/oauth/twitter/success'
			failureUri = '/oauth/twitter/error'
			//callback = "${baseURL}/oauth/twitter/callback"
			callback = "http://localhost:8080/AgenteAr4k/oauth/twitter/callback"
		}
		linkedin {
			api = org.scribe.builder.api.LinkedInApi
			key = 'oauth_linkedin_key'
			secret = 'oauth_linkedin_secret'
			successUri = '/oauth/linkedin/success'
			failureUri = '/oauth/linkedin/error'
			//callback = "${baseURL}/oauth/linkedin/callback"
			callback = "http://localhost:8080/AgenteAr4k/oauth/linkedin/callback"
		}
		google {
			api = org.grails.plugin.springsecurity.oauth.GoogleApi20
			//api = org.scribe.builder.api.GoogleApi
			key = 'oauth_google_key'
			secret = 'oauth_google_secret'
			successUri = '/oauth/google/success'
			failureUri = '/oauth/google/error'
			//callback = "${baseURL}/oauth/google/callback"
			callback = "http://localhost:8080/AgenteAr4k/oauth/google/callback"
			scope = 'https://www.googleapis.com/auth/userinfo.profile https://www.googleapis.com/auth/userinfo.email'
		}
		facebook {
			api = org.scribe.builder.api.FacebookApi
			key = 'oauth_facebook_key'
			secret = 'oauth_facebook_secret'
			successUri = '/oauth/facebook/success'
			failureUri = '/oauth/facebook/error'
			//callback = "${baseURL}/oauth/facebook/callback"
			callback = "http://localhost:8080/AgenteAr4k/oauth/facebook/callback"
		}
	}
}

// Configurazione invio mail
grails {
	mail {
		host = "smtp.gmail.com"
		port = 465
		username = "youracount@gmail.com"
		password = "yourpassword"
		props = ["mail.smtp.auth":"true",
			"mail.smtp.socketFactory.port":"465",
			"mail.smtp.socketFactory.class":"javax.net.ssl.SSLSocketFactory",
			"mail.smtp.socketFactory.fallback":"false"]
	}
}

// Per l'assistenza remota
olark.key='1445-771-10-6904'

master.host = '127.0.0.1'
master.port = 22
master.user = 'ar4k'
// Iniziare la stringa chiave con """ e concludere con uguale combinazione
master.key = """
-----BEGIN RSA PRIVATE KEY-----
MIIEoAIBAAKCAQEAnMVL/qgLI9p7kAPC/JDH9Z8tv1dRpLWCyBxMr8Z2p94K+/NE
avP10wflj8Mxk89v9Z4JOPsP5vSVsFdNJJD5oD++5cD5XGlBSjtKfxZPVz+VKgBo
Aouke+mrnQ27BUYH6g7Aas/LUXhDkqBCckKE1f8ukwEAWkTvKcTJ0t8Ir6WqONiu
8ITxxRa7LNxFYIzt+1N3ScMVLEfszQe4xsG02WwPS2JgBT2mVjg3J3cWy78RDJjm
oO6UpT7QpxykV/pVBkRTBwzN+gqKfXxc2xvULbHgfpakGs/GijTKNiqOonsB3eyV
D9MacP/kWMUkh2REjzE7jw5UKfwKM/8o5NMcPwIBIwKCAQAj1VMy5JTU/r0oO1/E
s2Dnr1rp6BKpTg9DrrJxUe88UAKCu0LWnimPUkMZi7OPf98TkdY46O2xIfZUMTY0
PmTxbaf6ADj/LgBLe0Q6Tj4FUFyb4tXx9AhW1lMclWyTfbiqhwdo3wnmvGc3dRaA
hDueoLowOr5AhMjzmrHJzJRFZ1ZKpZt/2mAXRYde0Hn4ujdyddGgw0LAFkchUCHN
/Ymsi7Nl75o5idE/CxF+2HtknolldcnPBP7H7WtFQYOFBH6e2MVuR/BMhuVQMfO9
RDktF3uyv7aFn/LfGGfnoZ86mt3tF441OU4Crhyz03yvHDwk3M1wtxeoiYYQzZrd
0TkLAoGBAMrRI743ALpUvunf5sxlBAI1p1I54YC722IsAiQT6qe9ckVHm+dnIviX
EAXYx200D2QVh6xrxKuPGRmsviSqNf2fI/orGaHbZXf1dScHX0zcTQykSf2teB0d
CCxwm+EHhpbOV/f/2O1nSoG6+/mCLGU++t4JF7zkilwM8MUeI7pZAoGBAMXhIGhq
KNMKh6EOJWNbWjkw6K/wi1BoCxNvRwDNodLJFpklzjXiX7pLV8TDvI2/ehPbLedL
dA5rdwGrX+BH0K1wK/kZeHDP1udJLKt3sg0sHhasRefku+LdIdcwK5T6juAiTY7n
H3e9+6ULC+mHHFgu8Fq41Y/EGAOdit48PAhXAoGAaE5M5XtfdcUu++9+ArebqV1r
/mbpAGCctiVC7fudQFLNDa/MhaLBhykeLuSD0cMAmd8v1QQ5QkmX4VGGW//D/sbf
TXVAYd6L9IzdKgPKnI6OBoBgkRdiWB2PLMwy6MIKs/UXTFexZCZ+FtUxITuhzatc
cjCP3XzZcSs5/vmOt5sCgYBra5U//x154SUOSYICvJAQawH5HC5e2WUZLcd1dvFy
bSmGVln//ou85xJjgDMoYKFbPHgBN5bF/9Ljpiy7lLMcUtYK4fDsyJk7ugJOdC10
3W9rZNV94pHhjfxtgIyv9btGeQWIFxEVHf2ivNqUtw9jEiq05/7npPcX588K7X+s
wQKBgHwRHbOAi1L5SrZgPpdMa/Y3hxd9HXz/E8k0S5PuXgIR/uIwM31j6AvQTh0b
By5hGzU8Jimq23HxpiO8w3zpFN7pLVQWb+WVFBUsz8rH2OfI5x/SF2Od2MQr/g+G
XY4EKah0OgIHWHJ1FkWvyVUYDn+BTSGj6roqszef3NNOsutc
-----END RSA PRIVATE KEY-----
"""

// Contesto
contesto = 'Bootstrap-Ar4k'

// Interfaccia
interfaccia = 'Bootstrap-Ar4k'

// Codice attivazione Ar4K (Attivazioni commerciali)
//codiceAttivazione = ''

// Configurazione proxy tra la JVM e il vaso master
//proxyVersoMaster = ''

// Configurazione proxy tra il vaso master e Internet
//proxyMasterInternet = ''

// Configurazione ActiveMQ
grails.plugin.routing.jms.brokerURL = 'tcp://localhost:61616'
grails.plugin.routing.jms.userName = 'admin'
grails.plugin.routing.jms.password =  'admin'

grails.routing.camelContextId = 'ar4kInterfaccia'

///////////////////////////////////////////////////////////////////////////
///////////// FINE CONFIGURAZIONI ROSSONET ////////////////////////////////
///////////////////////////////////////////////////////////////////////////



// Added by the Spring Security Core plugin:
grails.plugin.springsecurity.userLookup.userDomainClassName = 'org.ar4k.Utente'
grails.plugin.springsecurity.userLookup.authorityJoinClassName = 'org.ar4k.UtenteRuolo'
grails.plugin.springsecurity.authority.className = 'org.ar4k.Ruolo'
grails.plugin.springsecurity.controllerAnnotations.staticRules = [
	'/':                              ['permitAll'],
	'/index':                         ['permitAll'],
	'/index.gsp':                     ['permitAll'],
	'/assets/**':                     ['permitAll'],
	'/**/js/**':                      ['permitAll'],
	'/**/css/**':                     ['permitAll'],
	'/**/images/**':                  ['permitAll'],
	// by Andrea Ambrosini
	'/register/**':                   ['permitAll'],
	'/bootStrap/**':                  ['permitAll'], // Ricordarsi di bloccare la sicurezza sul Controller!
//	'/console/**':                    ['permitAll'], // Solo per debug della sicurezza!!! 
//	'/plugins/console-1.5.4/**':      ['permitAll'], // Solo per debug della sicurezza!!!
	'/login/**':    				  ['permitAll'],
	'/logout/**':                     ['permitAll'],
	'/oauth/**':                      ['permitAll'],
	'/oauth/authorize.dispatch':      ["isFullyAuthenticated() and (request.getMethod().equals('GET') or request.getMethod().equals('POST'))"],
	'/oauth/token.dispatch':          ["isFullyAuthenticated() and request.getMethod().equals('POST')"],
	'/atterraggio/**':                ['permitAll'],
	'/springSecurityOAuth/**':	      ['permitAll'],
	'/codeqr':			        	  ['permitAll'],
	// Accesso a Admin -per utente demo-
	'/**':                            ['ROLE_ADMIN'],
	//'/admin/**':                    ['ROLE_USER'],
	'/admin/**':                      ['ROLE_ADMIN','ROLE_USER'],
	//'/Ar4kActiviti/**':               ['ROLE_ADMIN','ROLE_USER'], 
	'/Ar4kActiviti/**':               ['permitAll'], // Attenzione!!
	// Accesso a tutti autenticati
	//'/**':                          ['IS_AUTHENTICATED_REMEMBERED'],
	// Accesso a tutti
	//'/**':                            ['permitAll'], // Solo per debug!
	// favicon
	'/**/favicon.ico':                ['permitAll']]


// Added by the Spring Security OAuth plugin:
grails.plugin.springsecurity.oauth.domainClass = 'org.ar4k.OAuthID'

grails.exceptionresolver.params.exclude = ['password', 'client_secret']

// Testo per registrazione
grails.plugin.springsecurity.ui.register.emailBody = 'Benvenuto $user,<br>per completare la procedura di registrazione in AR4K selezionare <a href="$url">questo link</a>.<br><br><bold>BOT AR4K</bold><br>(sistema automatico)'
grails.plugin.springsecurity.ui.register.emailSubject = 'Completa la registrazione in AR4K'
grails.plugin.springsecurity.ui.register.defaultRoleNames = ['ROLE_REGISTRATO']

grails.plugin.springsecurity.ui.forgotPassword.emailBody = 'Salve $user,<br>per completare la procedura di cambio della password AR4K selezionare <a href="$url">questo link</a>.<br><br><bold>BOT AR4K</bold><br>(sistema automatico)'
grails.plugin.springsecurity.ui.forgotPassword.emailSubject = 'Completa il reset della password AR4K'


// configurazioni activiti
activiti {
	  jobExecutorActivate = true
	  mailServerHost = "smtp.yourserver.com"
	  mailServerPort = "25"
	  mailServerUsername = ""
	  mailServerPassword = ""
	  mailServerDefaultFrom = "ar4k@rossonet.com"
	  history = "audit" // "none", "activity", "audit" or "full"
}

cors.headers = ['Access-Control-Allow-Origin': '*']


// Added by the Spring Security OAuth2 Provider plugin:
grails.plugin.springsecurity.oauthProvider.clientLookup.className = 'org.ar4k.Client'
grails.plugin.springsecurity.oauthProvider.authorizationCodeLookup.className = 'org.ar4k.AuthorizationCode'
grails.plugin.springsecurity.oauthProvider.accessTokenLookup.className = 'org.ar4k.AccessToken'
grails.plugin.springsecurity.oauthProvider.refreshTokenLookup.className = 'org.ar4k.RefreshToken'

