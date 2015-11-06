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
	/** identificativo di boot */
	String valoreCasuale=org.apache.commons.lang.RandomStringUtils.random(5, true, true).toString()

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
					log.info("Non trovato il "+contesto+" Consul sul nodo creo il contesto vergine e lo salvo in Consul")
					Contesto nuovoContesto = creaNuovoContestoVergine(contesto,interfaccia)
					// crea il nuovo contesto vergine
					if (nuovoContesto.salva(stato)) {
						interfacciaContestoService.stato = stato
						interfacciaContestoService.contesto = nuovoContesto
						interfacciaContestoService.interfaccia = nuovoContesto.interfacce.find{it.idInterfaccia==interfaccia}
						inAvvio = false
						inReset = false
					}
				}
			}
		}
		return risultato
	}

	Contesto creaNuovoContestoVergine(String idContestoTarget,String idInterfaccia) {
		Contesto nuovoContesto = new Contesto()
		nuovoContesto.idContesto = idContestoTarget
		nuovoContesto.etichetta = 'Contesto '+new Date().format("yyyyMMddHHmmss", TimeZone.getTimeZone("UTC")).toString()+' ('+valoreCasuale+')'
		nuovoContesto.descrizione = "Contesto generato automaticamente "+new Date().format("yyyyMMddHHmmss", TimeZone.getTimeZone("UTC")).toString()
		Interfaccia nuovaInterfaccia = new Interfaccia()
		if (interfaccia) nuovaInterfaccia.idInterfaccia = idInterfaccia
		nuovaInterfaccia.etichetta = "Interfaccia generata automaticamente "+new Date().format("yyyyMMddHHmmss", TimeZone.getTimeZone("UTC")).toString()
		nuovaInterfaccia.salva(stato)
		nuovoContesto.interfacce.add(nuovaInterfaccia)
		return nuovoContesto
	}

	Boolean aggiungiUtente(String nome,String password) {
		log.debug("Creo l'utente "+nome)
		Boolean risultato = false
		def adminRole = Ruolo.create()
		adminRole.authority='ROLE_ADMIN'
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
		} catch (Exception e){
			log.warn("Impossibile salvare l'utente. Errore: "+e.toString())
		}

		return risultato
	}

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
}





/*
 Boolean provaConnessioneMaster() {
 Boolean risultato=false
 vasoMaster= new Vaso(
 etichetta:utenteMaster+'@'+macchinaMaster+':'+portaMaster,
 descrizione:'Nodo Master Ar4k',
 macchina:macchinaMaster,
 porta:portaMaster.toInteger(),
 utente:utenteMaster,
 key:keyMaster
 )
 log.debug("provaConnessioneMaster() verso "+vasoMaster)
 risultato = vasoMaster.provaConnessione()
 if (risultato) {
 log.info("provaConnessioneMaster() verso "+vasoMaster+" (ok)")
 configurato = true
 raggiungibile = true
 } else {
 log.warn("provaConnessioneMaster() verso "+vasoMaster+" NON RIUSCITA!")
 configurato = false
 raggiungibile = false
 }
 return risultato
 }
 Boolean caricaVasoMaster() {
 Boolean risultato = false
 log.debug("caricaVasoMaster() su "+vasoMaster)
 if (provaConnessioneMaster()) risultato = vasoMaster.provaVaso()
 if (risultato) {
 log.info("caricaVasoMaster() su "+vasoMaster+" (ok)")
 vasoConnesso = true
 contestiInMaster = vasoMaster.listaContesti()
 // Aggiunge il contesto demo
 Contesto demo = creaContestoAr4kBoot()
 contestiInMaster.add(demo)
 log.info("Contesti disponibili dopo la creazione del contesto demo: "+contestiInMaster)
 } else {
 log.warn("caricaVasoMaster() su "+vasoMaster+" ERRORE")
 risultato = false
 vasoConnesso = false
 }
 return risultato
 }
 Boolean verificaConnettivitaVasoMaster() {
 log.debug("verificaConnettivitaVasoMaster()")
 Boolean risultato = false
 if(vasoMaster.verificaConnettivita()) {
 risultato = true
 log.info("Il vaso master accede ad Internet")
 } else {
 log.warn("Il vaso master NON accede ad Internet")
 }
 if (escludiProveConnessioneVaso==true) {
 log.info("Escluso il test di raggiungibilità di Internet dal vaso master")
 risultato = true
 }
 return risultato
 }
 Boolean caricaContesto(String contestoSceltaConf) {
 log.debug("caricaContesto("+contestoSceltaConf+")")
 Boolean ritorno = false
 if (caricaVasoMaster()) {
 String primarioContesto = idContestoScelto
 if (contestoSceltaConf) primarioContesto = contestoSceltaConf
 log.debug("Provo il caricamento di "+primarioContesto)
 log.debug("Contesti disponibili: "+contestiInMaster)
 Contesto contestoTarget = contestiInMaster.find{it.idContesto==primarioContesto}
 log.info(contestoTarget?"Caricato contesto "+contestoTarget:"Nessun contesto caricato...")
 if (contestoTarget) {
 contesto = contestoTarget
 if(contesto.avviaContesto()) {
 contestoScelto = true
 ritorno = true
 log.debug("Attestato sul contesto "+contesto)
 idContestoScelto = contesto.idContesto
 interfacceInContesto = []
 contesto.interfacce.each{interfacceInContesto.add(it)}
 log.info("Interfacce trovate nel contesto "+interfacceInContesto)
 utentiInContesto = []
 //contesto.utentiRuoli.each{utentiInContesto.add(it)}
 UtenteRuolo.findAll().each{utentiInContesto.add(it)}
 log.info("UtentiRuolo trovati nel contesto "+utentiInContesto)
 }else {
 log.warn("Impossibile avviare il contesto "+contesto)
 }
 }
 }
 return ritorno
 }
 Boolean avvia(String contestoSceltaConf,String interfacciaSceltaConf) {
 log.debug("avvia("+contestoSceltaConf+","+interfacciaSceltaConf+")")
 Boolean ritorno = false
 interfacciaContestoService.connessioneConsul = new JSch()
 interfacciaContestoService.connessioneActiveMQ = new JSch()
 if (caricaContesto(contestoSceltaConf)) {
 String primarioInterfaccia = idInterfacciaScelta
 if (interfacciaSceltaConf) primarioInterfaccia = interfacciaSceltaConf
 log.debug("Provo il caricamento dell'interfaccia "+primarioInterfaccia)
 log.debug("Interfacce disponibili: "+interfacceInContesto)
 Interfaccia interfacciaTarget = interfacceInContesto.find{it.idInterfaccia==primarioInterfaccia}
 log.info(interfacciaTarget?"Caricata interfaccia "+interfacciaTarget:"Nessuna interfaccia caricata...")
 if (interfacciaTarget) {
 interfaccia = interfacciaTarget
 if(interfaccia.avviaInterfaccia()) {
 interfacciaScelta = true
 inAvvio = false
 ritorno = true
 log.debug("Attestato sull'interfaccia "+interfaccia)
 idInterfacciaScelta = interfaccia.idInterfaccia
 interfacciaContestoService.contesto=contesto
 interfacciaContestoService.interfaccia=interfaccia
 interfacciaContestoService.stato=new Stato()
 //log.info("Attiva il gestore di processo Activiti")
 //interfacciaContestoService.attivaActiviti()
 vasoMaster.avviaConsul(interfacciaContestoService.connessioneConsul)
 vasoMaster.avviaActiveMQ(interfacciaContestoService.connessioneActiveMQ)
 log.info("Attiva Consul")
 interfacciaContestoService.connettiConsul()
 log.info("Attiva Kettle")
 interfacciaContestoService.initKettle()
 log.info("Delegato il controllo a InterfacciaContestoService")
 log.debug("Grafica caricata")
 log.debug(interfacciaContestoService)
 log.info("Aggiorno la situazione utenti")
 try{
 interfacciaContestoService.contesto.utentiRuoli = []
 utentiInContesto.each{interfacciaContestoService.contesto.utentiRuoli.add(it)}
 } catch(Exception ee) {log.warn(ee.toString())}
 log.info("Carico sui vasi i repository con il flag caricaInBootstrap==true")
 try{
 interfacciaContestoService.contesto.ricettari.each{
 if (it.caricaInBootstrap == true) {
 if (it.clonaOvunque) {
 interfacciaContestoService.contesto.vasi*.avviaRicettario(it)
 }
 interfacciaContestoService.contesto.vasoMaster.caricaSemi(it)
 it.aggiornato = new Date().format("yyyyMMddHHmmss", TimeZone.getTimeZone("UTC")).toString()
 }
 }
 interfacciaContestoService.contesto.ricettari.each{ ricettario ->
 ricettario.semi.each{
 log.info("Analizzo il seme: "+it.toString()+" con flag: "+it.meme.caricaInBootstrap)
 if (it.meme.caricaInBootstrap == true) {
 log.info("Carico il seme "+it.meme+" in fase di bootstrap")
 Meme nuovoMeme = new Meme()
 nuovoMeme = it.meme.clone()
 // Rigenera tutti gli id casuali per non rischiare riferimenti errati nelle fasi di esecuzione
 nuovoMeme.idMeme = UUID.randomUUID()
 nuovoMeme.pathVaso = '~/.ar4k/ricettari/'+ ricettario.repositoryGit.nomeCartella
 nuovoMeme.metodi.each{it.idMetodo=UUID.randomUUID()}
 nuovoMeme.caricaProcessiActiviti(vasoMaster,interfacciaContestoService.processEngine)
 interfacciaContestoService.contesto.memi.add(nuovoMeme)
 }
 }
 }
 }catch(Exception ee) {log.warn(ee.toString())}
 }
 }
 }
 return ritorno
 }
 Boolean avvia() {
 avvia(idContestoScelto,idInterfacciaScelta)
 }
 String toString() {
 String risultato = '[ STATO ]\n'
 risultato +='configurato: '+configurato+"\n"
 risultato +='raggiungibile: '+raggiungibile+"\n"
 risultato +='contesto scelto: '+contestoScelto+" ("+idContestoScelto+")\n"
 risultato +='interfaccia scelta: '+interfacciaScelta+" ("+idInterfacciaScelta+")\n"
 risultato +='SISTEMA IN CONFIGURAZIONE: '+inAvvio+"\n"
 risultato +='Codice commerciale Ar4k: '+codiceAttivazioneAr4k+"\n"
 risultato +='Escludi prove di raggiungibilità della rete: '+escludiProveConnessione+"\n"
 risultato +='Escludi prove di raggiungibilità della rete vaso master: '+escludiProveConnessioneVaso+"\n"
 risultato +='Configurazione proxy Java -> Vaso Master: '+proxyVersoMaster+"\n"
 risultato +='Configurazione proxy Vaso Master -> Internet: '+proxyMasterInternet+"\n"
 risultato +='Contesto: '+contesto+"\n"
 risultato +='[ VASO MASTER SSH : '+utenteMaster+'@'+macchinaMaster+':'+portaMaster+' ]\n'
 risultato +=keyMaster+'\n'
 return risultato
 }
 Contesto creaContestoAr4kBoot() {
 log.debug("Creo il contesto di base per il boot su "+vasoMaster)
 Contesto contestoCreato = new Contesto(
 idContesto:'ContestoBootstrapAr4k'+"-"+valoreCasuale,
 etichetta:"Generato automaticamente in data "+new Date().format( 'dd-MM-yyyy' ).toString()+" -"+valoreCasuale+"-"
 )
 contestoCreato.configuraMaster(vasoMaster)
 Interfaccia interfacciaDemo = creaInterfacciaAr4k()
 Ricettario ricettario = new Ricettario()
 contestoCreato.interfacce.add(interfacciaDemo)
 contestoCreato.ricettari.add(ricettario)
 log.debug("Contesto base creato: "+contestoCreato)
 return contestoCreato
 }
 Interfaccia creaInterfacciaAr4k() {
 Interfaccia interfacciaCreata = new Interfaccia(idInterfaccia:'InterfacciaBootstrapAr4k')
 log.debug("Ho creato l'interfaccia base:"+interfacciaCreata)
 return interfacciaCreata
 }
 Boolean aggiungiUtenteRuolo(UtenteRuolo iti) {
 try {
 Utente u = Utente.findAllByUsername(iti.utente.username)[0]
 if (!(u && u.username == iti.utente.username)){
 u = Utente.importa(iti.utente.esporta())
 }
 u.save(flush:true)
 u = Utente.findAllByUsername(iti.utente.username)[0]
 Ruolo r = Ruolo.findAllByAuthority(iti.ruolo.authority)[0]
 if (!(r && r.authority == iti.ruolo.authority)){
 r = Ruolo.importa(iti.ruolo.esporta())
 }
 r.save(flush:true)
 r = Ruolo.findAllByAuthority(iti.ruolo.authority)[0]
 UtenteRuolo ur
 ur = UtenteRuolo.findAllByUtenteAndRuolo(u,r)[0]
 if (!ur){
 ur = UtenteRuolo.importa(u,r)
 }
 ur.save(flush:true)
 } catch (Exception e) {
 log.warn("Errore nel caricamento UtenteRuolo "+iti)
 }
 return true
 }
 Boolean impostaCodiceCommerciale(String codiceCom){
 log.info("Configuro utilizzato il codice di attivazione commerciale.")
 String data
 Properties properties = new Properties()
 try{
 data = new URL("http://www.rossonet.org/dati/codar4k/"+codiceCom).getText()
 if (data){
 def slurper = new JsonSlurper()
 def configurazioneImportata = slurper.parseText(data)
 macchinaMaster = configurazioneImportata.host?:null
 portaMaster = configurazioneImportata.port?:null
 utenteMaster = configurazioneImportata.user?:null
 keyMaster = configurazioneImportata.key?:null
 idContestoScelto = configurazioneImportata.contesto?:null
 idInterfacciaScelta = configurazioneImportata.interfaccia?:null
 avvia()
 }
 }catch (Exception eee){log.warn("Errore nella lettura dell'url relativa al codice commerciale: "+eee)}
 return data?true:false
 }
 }
 */
