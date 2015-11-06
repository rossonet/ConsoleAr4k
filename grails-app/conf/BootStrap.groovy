import org.codehaus.groovy.grails.commons.GrailsApplication;

import grails.util.Environment

import org.ar4k.Vaso;
import org.ar4k.secure.*
import org.ar4k.ConnessioneSSH
import org.ar4k.Proxy

class BootStrap {

	GrailsApplication grailsApplication
	def bootStrapService

	def init = { servletContext ->
		switch (Environment.current) {
			case Environment.DEVELOPMENT:
				log.info("Sistema in configurazione di sviluppo... ")
				if ( provaConfigurazioni() ) log.info("Sistema configurato e pronto...")
				break;
			case Environment.PRODUCTION:
				if ( provaConfigurazioni() ) log.info("Sistema configurato e pronto...")
				break;
		}

		log.debug("\nIn BootStrap.groovy:\n***  ")
		for (String property: System.getProperty("java.class.path").split(";")) {
			log.debug(property + "\n")
		}
		log.debug("***\n")
	}

	def destroy = {
	}

	Boolean provaConfigurazioni() {
		if (grailsApplication.config.codiceAttivazione) {
			log.info("Trovato codice nel file di configurazione...")
			bootStrapService.stato.codiceAttivazione = SgrailsApplication.config.codiceAttivazione
		}

		if(System.getProperty("rossonet.codattivazione")) {
			log.info("Trovato codice bootstrap in linea di comando...")
			bootStrapService.stato.codiceAttivazione = System.getProperty("rossonet.codattivazione")
		}

		if (grailsApplication.config.macchinaConsul) {
			log.info("Configuro i parametri di configurazione trovati su file locale")
			bootStrapService.contesto = grailsApplication.config.contesto?:null
			bootStrapService.interfaccia = grailsApplication.config.interfaccia?:null
			bootStrapService.stato.macchinaConsul = grailsApplication.config.macchinaConsul?:null
			bootStrapService.stato.portaConsul = grailsApplication.config.portaConsul?:null
			bootStrapService.stato.connessioneSSH = grailsApplication.config.connessioneSSH?:false
			bootStrapService.stato.connessioneOnion = grailsApplication.config.connessioneOnion?:false
			bootStrapService.stato.controlloConsul = grailsApplication.config.controlloConsul?:true
			bootStrapService.stato.connessioneProxySocks = grailsApplication.config.connessioneProxySocks?:false
			if (grailsApplication.config.connessioneSSH) {
				log.info("Configuro i parametri di configurazione trovati su file per un accesso SSH")
				ConnessioneSSH connessione = new ConnessioneSSH()
				connessione.macchina = grailsApplication.config.hostSSH.macchina?:null
				connessione.porta = grailsApplication.config.hostSSH.porta?:22
				connessione.utente = grailsApplication.config.hostSSH.utente?:'root'
				connessione.key = grailsApplication.config.hostSSH.key?:null
				bootStrapService.stato.listaGWSSH.add(connessione)
			}
			if (grailsApplication.config.connessioneProxySocks) {
				log.info("Configuro i parametri di configurazione trovati su file per un proxy")
				Proxy proxy = Proxy()
				proxy.macchina = grailsApplication.config.proxy.macchina?:null
				proxy.porta = grailsApplication.config.proxy.porta?:3128
				proxy.utente = grailsApplication.config.proxy.utente?:''
				proxy.password = grailsApplication.config.proxy.password?:null
				proxy.tipologia = grailsApplication.config.proxy.tipologia?:'SOCKS5'
				bootStrapService.stato.listaProxy.add(proxy)
			}
		}
		if (grailsApplication.config.utente && grailsApplication.config.password) {
			log.info("Configuro un utente sul sistema")
			bootStrapService.aggiungiUtente(grailsApplication.config.utente?:'',grailsApplication.config.password?:'')
		}
		return bootStrapService.inizio()
	}
}