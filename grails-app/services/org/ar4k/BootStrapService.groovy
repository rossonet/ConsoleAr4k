/**
 * Bootstrap
 *
 * <p>Service per la gestione del boot dell'interfaccia agenteAr4k</p>
 *
 * <p style="text-justify">
 * Questo service istanzia il sistema all'avvio</br>
 * 
 * Il service, per tutto il ciclo di vita dell'interfaccia grafica, conserva lo stato e rende disponibili 
 * i metodi per cambiare il vaso master e/o il contesto chiudendo correttamente il precedente.</b>
 * 
 * Questo service è invocato nel bootstrap per avviare l'interfaccia.</b>
 *
 * @author Andrea Ambrosini (Rossonet s.c.a r.l)
 * @version 0.1-alpha
 * @see org.ar4k.Interfaccia
 */

package org.ar4k

import grails.transaction.Transactional
import groovy.json.JsonSlurper

import com.jcraft.jsch.*

@Transactional
class BootStrapService {

	/** service InterfacciaContestoService da popolare */
	InterfacciaContestoService interfacciaContestoService

	/** bind con Consul */
	Stato stato = new Stato()

	/** vero se il sistema è in bootstrap  */
	Boolean inAvvio = true
	/** vero se il sistema è in reset */
	Boolean inReset = false
	/** contesto scelto */
	String contesto = null
	/** interfaccia scelta */
	String interfaccia = null
	/** host pubblico per test di raggiungibilità */
	String indirizzoTest='http://hc.rossonet.name'
	/** host test per connessione Onion */
	String indirizzoTestOnion='http://mvgf7rnxqeczjb4i.onion'
	/** identificativo di boot */
	String valoreCasuale=org.apache.commons.lang.RandomStringUtils.random(5, true, true).toString()
	/** contesto creato durante il boot */
	Contesto contestoCreato = null
	/** interfaccia creata durante il boot */
	Interfaccia interfacciaCreata = null
	/** istanza Consul creata */
	IstanzaConsul nuovaIstanzaConsul = null
	/** vaso creato per l'accesso al sistema */
	Vaso nuovoVaso = null
	/** nodo creato per l'accesso al sistema */
	Nodo nuovoNodo = null
	/** regione creata per l'accesso al sistema */
	Regione nuovaRegione = null
	/** stringhe di errore per interfaccia */
	List<String> errori = []
	/** base link codici attivazione */
	String baseNegozio = "http://www.rossonet.org/dati/codar4k/"


	String toString() {
		return stato
	}

	/** lanciato dalla procedura di boot di Grails. 
	 * Grails si preoccupa di popolare il servizio BootStrap (scope singleton)
	 * con i parametri trovati nel file di configurazione
	 * o recuperati dalla linea di comando. Per passare un parametro
	 * durante il lancio dell'applicatico
	 * JAVA usare la sintassi "-Drossonet.parametro=valore"
	 * 
	 * @see BootStrap 
	 * */
	Boolean inizio() {
		Boolean risultato = true
		if (stato.connetti()) {
			if (contesto) {
				def listaContesti = stato.listaIdContesti()
				def contestoTarget = null
				if (listaContesti.size()>0) {
					contestoTarget = listaContesti.find{it.key == contesto}?.value
				}
				if (contestoTarget) {
					log.info("Trovata la definizione del contesto "+contesto+" in Consul")
					// implementare caricamento
					// inAvvio = false
					// inReset = false
				} else {
					log.warn("Non trovato il "+contesto+" Consul sul nodo")
					try{
						// crea il nuovo contesto vergine
						ConnessioneSSH connessioneLocale = stato.listaGWSSH.find{it.toString() == stato.tunnelSSH}
						// se riesce a salvare la struttura dati su consul, avvia l'interfaccia!
						if (contestoCreato?.salva(stato,20)) {
							interfacciaContestoService.stato = stato
							interfacciaContestoService.contesto = contestoCreato
							interfacciaContestoService.contesto.datacenterConsul = nuovaIstanzaConsul.datacenter
							interfacciaContestoService.interfaccia = contestoCreato.interfacce.find{it.idInterfaccia==interfaccia}
							inAvvio = false
							inReset = false
							log.info("creato il contesto "+contestoCreato+" in Consul")
						} else {
							risultato = false
							log.warn("Errore salvataggio contesto...")}
					} catch(Exception fff) {
						log.warn("Errore creazione contesto vergine "+fff.printStackTrace())
					}
				}

			}
		}

		return risultato
	}

	/** crea un consul sullo Stato corrente */
	Boolean creaNuovoConsulVergine(IstanzaConsul consulClient, Contesto nuovoContesto) {
		Boolean ritorno = true
		// identifica la connessione ssh
		ConnessioneSSH connessioneLocale = stato.listaGWSSH.find{it.toString() == stato.tunnelSSH}
		log.info("Avvio consul sulla connessione "+connessioneLocale)
		Interfaccia nuovaInterfaccia = new Interfaccia()
		nuovaInterfaccia.idInterfaccia = 'InterfacciaBootstrap'
		nuovaInterfaccia.etichetta = "Interfaccia generata automaticamente "+new Date().format("yyyyMMddHHmmss", TimeZone.getTimeZone("UTC")).toString()
		// nuovo vaso
		nuovoVaso = new Vaso(
				etichetta: connessioneLocale.toString(),
				descrizione: "Connessione generata in fase di bootstrap",
				ssh: connessioneLocale
				)
		// nuovo nodo
		nuovoNodo = new Nodo(
				etichetta:'nodo master',
				descrizione:'nodo generato in fase di boot'
				)
		// nuova regione
		nuovaRegione = new Regione(
				etichetta:'regione creazione',
				descrizione:'regione generata in fase di boot',
				datacenterConsul: consulClient.datacenter
				)
		// ricettari base nuovo contesto Ar4k
		Ricettario ricettarioBase = new Ricettario(
				etichetta:'Strumenti base',
				descrizione:"creato in fase di bootstrap",
				repositoryGit: new RepositoryGit(
				indirizzo:'https://github.com/rossonet/templateAr4k.git',
				nomeCartella:'ar4k_open'
				)
				)
		Ricettario ricettarioConsole = new Ricettario(
				etichetta:'Interfaccia web Console Ar4k',
				descrizione:"creato in fase di bootstrap",
				repositoryGit: new RepositoryGit(
				indirizzo:'https://github.com/rossonet/ConsoleAr4k.git',
				nomeCartella:'ar4k_console'
				)
				)

		nuovoVaso.nodo = nuovoNodo
		nuovoNodo.vasi.add(nuovoVaso)
		nuovoNodo.regioni.add(nuovaRegione)
		nuovaRegione.nodi.add(nuovoNodo)

		nuovoVaso.funzionalita.add('BOOTSTRAP')
		nuovoVaso.funzionalita.add('BOOTSTRAP '+nuovoContesto.etichetta)
		
		nuovoContesto.ricettari.add(ricettarioBase)
		nuovoContesto.ricettari.add(ricettarioConsole)

		nuovoVaso.ricettari.add(ricettarioBase)
		nuovoContesto.datacenterConsul = consulClient.datacenter
		nuovoContesto.interfacce.add(nuovaInterfaccia)
		nuovoContesto.vasi.add(nuovoVaso)
		nuovoContesto.nodi.add(nuovoNodo)
		nuovoContesto.regioni.add(nuovaRegione)
		nuovoContesto.vasoMaster = nuovoVaso
		// kill eventuali processi esistenti
		String killAll = """
		killall consul
		killall consul_i386
		killall consul_amd64
		"""
		// rileva l'architettura
		String architettura = 'i386'
		String architetturaDesc = connessioneLocale.esegui('uname -a')
		if (architetturaDesc =~ /x86_64/) architettura = 'amd64'
		log.info("architettura rilevata: "+architettura)
		// crea la struttura delle directory
		String creaDirectory = """
		cd ~
		mkdir -p .ar4k
		mkdir -p .ar4k/contesti
		mkdir -p .ar4k/ricettari
		mkdir -p .ar4k/dati
		mkdir -p .ar4k/dati/consul
		"""

		// configura il proxy per il nodo ssh se necessario e
		// scarica o aggiorna il repository base
		String scaricamentoGit = ''
		if (false) scaricamentoGit += 'export http_proxy= ...'
		String comandoGit = "cd ~/.ar4k/ricettari ; if [ -e ar4k_open ] ; then cd ar4k_open; git pull; else git clone https://github.com/rossonet/templateAr4k.git ar4k_open; fi"
		// lancia il binario
		String comandoAvvio = '~/.ar4k/ricettari/ar4k_open/'+architettura+'/consul_'+architettura+' agent -data-dir ~/.ar4k/dati/consul -bootstrap -server -dc '+consulClient.datacenter
		comandoAvvio += consulClient.chiave?' -encrypt "'+consulClient.chiave+'"':''
		comandoAvvio += consulClient.dominio?' -domain '+consulClient.dominio:''
		comandoAvvio += ' </dev/null &>/dev/null &'
		stato.macchinaConsul = '127.0.0.1'
		stato.portaConsul = consulClient.portaHTTP
		try {
			connessioneLocale.esegui(killAll)
			connessioneLocale.esegui(creaDirectory)
			connessioneLocale.esegui(comandoGit)
			connessioneLocale.esegui(comandoAvvio)
			// connetti consul
			stato.connetti()
			// crea la struttura dati
			contesto = nuovoContesto.idContesto
			interfaccia = nuovaInterfaccia.idInterfaccia
			//nuovaInterfaccia.salva(stato)
			//nuovoContesto.salva(stato)
			contestoCreato = nuovoContesto
			interfacciaCreata = nuovaInterfaccia
			nuovaIstanzaConsul = consulClient
		} catch (Exception fff) {
			log.warn("Errore nell'istanziazione dell'istanza Consul "+fff.printStackTrace())
			ritorno = false
			log.warn("contesto NON creato: "+nuovoContesto.esporta())
		}
		return ritorno
	}

	/** Aggiunge un utente amministratore */
	Boolean aggiungiUtente(String nome,String password) {
		log.debug("Creo l'utente "+nome)
		Boolean risultato = false
		def adminRole
		if (Ruolo.find{ authority == 'ROLE_ADMIN' }) {
			adminRole = Ruolo.find{ authority == 'ROLE_ADMIN' }
		} else {
			adminRole = Ruolo.create()
			adminRole.authority='ROLE_ADMIN'
		}
		def testUser = Utente.create()
		testUser.username=nome
		testUser.password=password
		UtenteRuolo utenteRuolo = UtenteRuolo.create()
		utenteRuolo.utente=testUser
		utenteRuolo.ruolo=adminRole
		try {
			testUser.save(flush:true)
			adminRole.save(flush:true)
			utenteRuolo.save(flush:true)
			risultato = true
			if (contestoCreato) contestoCreato.utentiRuoli.add(utenteRuolo)
			log.info("utente creato: "+testUser.username+" in gruppo "+adminRole.authority)
		} catch (Exception e){
			log.warn("Impossibile salvare l'utente "+(testUser?:'')+" nel ruolo "+(adminRole?:'')+" Errore: "+e.toString())
		}
		return risultato
	}

	/** verifica la raggiungibilità di Internet -la variabile indirizzoTest è impostata staticamente. Attualmente-*/
	Boolean verificaConnettivitaInterfaccia() {
		Boolean risultato=false
		log.debug("verificaConnettivitaInterfaccia() verso "+indirizzoTest)
		try {
			URL url = new URL(indirizzoTest)
			HttpURLConnection con = (HttpURLConnection)url.openConnection()
			log.debug(con.responseCode)
			if (con.responseCode==200){
				risultato = true
				log.debug(indirizzoTest+" verificaConnettivitaInterfaccia(): (ok)")
			} else {
				log.warn(indirizzoTest+" verificaConnettivitaInterfaccia(): ERRORE")
			}
			con.disconnect()
		} catch (MalformedURLException e) {
			log.warn("verificaConnettivitaInterfaccia(): "+e.printStackTrace())
		} catch (IOException e) {
			log.warn("verificaConnettivitaInterfaccia(): "+e.printStackTrace())
		}
		return risultato
	}

	/** verifica la presenza di una connessione funzionante via ssh.
	 * Se è presente un proxy o una connesione Onion verrano utilizzate nel test*/
	String verificaSSH() {
		log.info("Elenco connessioni SSH: "+stato.listaGWSSH)
		if (stato.listaGWSSH?.size() > 1) return stato.listaGWSSH.unique().join(", ")
		if (stato.listaGWSSH?.size() < 1) return null
		if (stato.listaGWSSH?.size() == 1) return stato.listaGWSSH[0]
		if (stato.listaGWSSH == null) return null
	}

	/** verifica la presenza di un proxy funzionante per connettersi via ssh */
	String verificaProxy() {
		log.info("Elenco proxy: "+stato.listaProxy)
		if (stato.listaProxy?.size() > 1) return stato.listaProxy.unique().join(", ")
		if (stato.listaProxy?.size() < 1) return null
		if (stato.listaProxy?.size() == 1) return stato.listaProxy[0]
		if (stato.listaProxy == null) return null
	}

	/** verifica la raggiungibilità di un gateway Onion funzionate */
	Boolean verificaOnion() {
		return stato.torAttivo
	}

	/** utilizza un codice di attivazione Ark4*/
	Boolean importaCodiceAttivazione(String codice) {
		return impostaCodiceCommercialeWeb(codice)
	}

	/** importa il codice commerciale da sito web statico */
	Boolean impostaCodiceCommercialeWeb(String codiceCom){
		log.info("Configuro utilizzato il codice di attivazione commerciale.")
		String data
		Properties properties = new Properties()
		try{
			data = new URL(baseNegozio+codiceCom).getText()
			if (data){
				def slurper = new JsonSlurper()
				def configurazioneImportata = slurper.parseText(data)
				ConnessioneSSH nuovaConnessione
				nuovaConnessione.macchina = configurazioneImportata.host?:null
				nuovaConnessione.porta = configurazioneImportata.port?:null
				nuovaConnessione.utente = configurazioneImportata.user?:null
				nuovaConnessione.key = configurazioneImportata.key?:null
				nuovaConnessione.proxyMaster = configurazioneImportata.proxyMaster?:null
				contesto = configurazioneImportata.contesto?:null
				interfaccia = configurazioneImportata.interfaccia?:null
				stato.listaGWSSH.add(nuovaConnessione)
				stato.tunnelSSH = nuovaConnessione.toString()
				stato.connessioneSSH = true
				stato.macchinaConsul = configurazioneImportata.macchinaConsul?:null
				stato.portaConsul = configurazioneImportata.portaConsul?:null
				if (configurazioneImportata.proxy) {
					Proxy proxyImportato = Proxy.creaDaUrl(configurazioneImportata.proxy, configurazioneImportata.proxyPassword?:null)
					stato.connessioneProxy = true
					stato.listaProxy.add(proxyImportato)
				}
			}
		}catch (Exception eee){log.warn("Errore nella lettura dell'url relativa al codice commerciale: "+eee)}
		return data?true:false
	}

}