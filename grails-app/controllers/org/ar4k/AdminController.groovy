/**
 * Controller interfaccia web principale della console amministrativa
 * 
 * <p>
 * AdminController gestisce tutte le richieste dell'interfaccia utente tranne autenticazione e bootstrap
 * </p>
 * 
 * <p style="text-justify">
 * In generale questo è il controller utilizzato dall'amministratore
 * dell'applicativo, espone tutte le funzionalità, per eventuali affinamenti
 * della sicurezza, utilizzare i dati di Contesto
 * </p>
 * 
 * @author Andrea Ambrosini (Rossonet s.c.a r.l)
 * @version 0.1-alpha
 * @see org.ar4k.Contesto
 * @see org.ar4k.InterfacciaContestoService
 */

package org.ar4k
import java.util.List;

import javax.swing.text.html.HTML

import org.activiti.engine.FormService
import org.activiti.engine.RepositoryService
import org.activiti.engine.RuntimeService;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity
import org.activiti.engine.repository.ProcessDefinition

import com.ecwid.consul.v1.QueryParams
import com.jcraft.jsch.JSch

import grails.converters.JSON
import groovy.json.*


class AdminController {

	InterfacciaContestoService interfacciaContestoService
	BootStrapService bootStrapService
	RepositoryService repositoryService
	FormService formService
	RuntimeService runtimeService

	/**
	 * 
	 * @return Interfaccia principale
	 */
	def index() {
		//[messaggioOlark: "Benvenuto nel template di sviluppo applicativo AR4K!"]
	}

	/**
	 * In generale le funzionalità legate allo scope session e il menù dell'utente.
	 * @return Pannello in alto a destra
	 */
	def headerNotification() {
		render(template: "headerNotification",model:[grafica: interfacciaContestoService.interfaccia.grafica])
	}

	/**
	 * 
	 * @return Testata
	 */
	def headerHtml() {
		render(template: "headerHtml",model:[grafica: interfacciaContestoService.interfaccia.grafica])
	}

	/**
	 * 
	 * @return Definizione applicazione AngularJS 
	 */
	def appjs() {
		render(template: "appjs",model:[grafica: interfacciaContestoService.interfaccia.grafica])
	}

	/**
	 * 
	 * @return Barra laterale sinistra
	 */
	def sidebar() {
		render(template: "sidebar",model:[grafica: interfacciaContestoService.interfaccia.grafica])
	}

	/**
	 * 
	 * @return Componente html con terminale legato ai websocks
	 */
	def terminale() {
		render(template: "terminale", model:[testoIntro:'...visualizzatore log vaso master...',mappa: 'master',descrizione: 'descrizione di prova',comandoAvvio:'sleep 3 ; clear ; tail -F ~/.ar4k/terminale.log',grafica: interfacciaContestoService.interfaccia.grafica] )
	}

	def oggetti() {
		render(template: "oggetti",model:[grafica: interfacciaContestoService.interfaccia.grafica])
	}

	def oggettiCtrl() {
		render(template: "oggettiCtrl",model:[grafica: interfacciaContestoService.interfaccia.grafica])
	}

	def rossonet() {
		render(template: "rossonet",model:[grafica: interfacciaContestoService.interfaccia.grafica])
	}

	def rossonetCtrl() {
		render(template: "rossonetCtrl",model:[grafica: interfacciaContestoService.interfaccia.grafica])
	}

	def apiAr4k() {
		render(template: "apiAr4k",model:[grafica: interfacciaContestoService.interfaccia.grafica])
	}

	def apiAr4kCtrl() {
		render(template: "apiAr4kCtrl",model:[grafica: interfacciaContestoService.interfaccia.grafica])
	}

	def memoria() {
		render(template: "memoria",model:[grafica: interfacciaContestoService.interfaccia.grafica])
	}

	def memoriaCtrl() {
		render(template: "memoriaCtrl",model:[grafica: interfacciaContestoService.interfaccia.grafica])
	}

	def utenti() {
		render(template: "utenti",model:[grafica: interfacciaContestoService.interfaccia.grafica])
	}

	def utentiCtrl() {
		render(template: "utentiCtrl",model:[grafica: interfacciaContestoService.interfaccia.grafica])
	}
	def processi() {
		render(template: "processi",model:[grafica: interfacciaContestoService.interfaccia.grafica])
	}

	def processiCtrl() {
		render(template: "processiCtrl",model:[grafica: interfacciaContestoService.interfaccia.grafica])
	}

	def memi() {
		render(template: "memi",model:[grafica: interfacciaContestoService.interfaccia.grafica])
	}

	def memiCtrl() {
		render(template: "memiCtrl",model:[grafica: interfacciaContestoService.interfaccia.grafica])
	}

	def reti() {
		render(template: "reti",model:[grafica: interfacciaContestoService.interfaccia.grafica])
	}

	def retiCtrl() {
		render(template: "retiCtrl",model:[grafica: interfacciaContestoService.interfaccia.grafica])
	}

	def ricettari() {
		render(template: "ricettari",model:[grafica: interfacciaContestoService.interfaccia.grafica])
	}

	def ricettariCtrl() {
		render(template: "ricettariCtrl",model:[grafica: interfacciaContestoService.interfaccia.grafica])
	}
	def dashrossonet() {
		render(template: "dashRossonet",model:[grafica: interfacciaContestoService.interfaccia.grafica])
	}

	def dashrossonetCtrl() {
		render(template: "dashRossonetCtrl",model:[grafica: interfacciaContestoService.interfaccia.grafica])
	}

	def messaggiSistema() {
		render(template: "messaggiSistema",model:[grafica: interfacciaContestoService.interfaccia.grafica])
	}

	def messaggiSistemaCtrl() {
		render(template: "messaggiSistemaCtrl",model:[grafica: interfacciaContestoService.interfaccia.grafica])
	}

	/**
	 * 
	 * @return Vasi e oggetti collegati in JSON
	 */
	def listaVasi() {
		def risultato = []
		interfacciaContestoService.contesto.vasi.each{ vaso ->
			String macchina = vaso.macchina
			Boolean eMaster = false;
			def stato = interfacciaContestoService.stato.consul.getHealthChecksForNode(macchina,new QueryParams(interfacciaContestoService.contesto.datacenterConsul)).getValue()
			if (interfacciaContestoService.contesto.vasoMaster.idVaso == vaso.idVaso ) {
				eMaster = true;
			}
			risultato.add([vaso:vaso,stato:stato,master:eMaster])
		}
		def incapsulato = [vasi:risultato]
		render incapsulato as JSON
	}

	/**
	 *
	 * @return Processi e oggetti collegati in JSON
	 */
	def listaProcessi() {
		def risultato = []
		interfacciaContestoService.stato.consul.getAgentServices().getValue().each{
			String stato = interfacciaContestoService.stato.consul.getHealthChecksForService(it.getValue().service,new QueryParams(interfacciaContestoService.contesto.datacenterConsul)).getValue()
			risultato.add(processo:it.getValue(),stato:stato)
		}
		def incapsulato = [processi:risultato]
		render incapsulato as JSON
	}

	/**
	 *
	 * @return Datacenters e oggetti collegati in JSON
	 */
	def listaDataCenters() {
		def risultato = []
		interfacciaContestoService.stato.consul.getCatalogDatacenters().getValue().each{
			List<String> nodi = interfacciaContestoService.stato.consul.getCatalogNodes(new QueryParams(it)).getValue()
			def nodiElaborati = []
			nodi.each{ nodo ->
				def stato = interfacciaContestoService.stato.consul.getHealthChecksForNode(nodo.node,new com.ecwid.consul.v1.QueryParams(it))
				nodiElaborati.add([nodo:nodo,stato:stato])
			}
			risultato.add(datacenter:it,nodi:nodiElaborati)
		}
		def incapsulato = [datacenters:risultato]
		render incapsulato as JSON
	}

	/** @return dettagli per un nodo Consul */
	def nodo(String identificativo,String datacenter) {
		def risultato = interfacciaContestoService.stato.consul.getCatalogNode(identificativo,new com.ecwid.consul.v1.QueryParams(datacenter))
		def stato = interfacciaContestoService.stato.consul.getHealthChecksForNode(identificativo,new com.ecwid.consul.v1.QueryParams(datacenter))
		def incapsulato = [nodo:risultato,stato:stato]
		render incapsulato as JSON
	}

	/**
	 *
	 * @return Ricettari collegati in JSON
	 */
	def listaRicettari() {
		def risultato = []
		interfacciaContestoService.contesto.ricettari.each{ risultato.add(it) }
		def incapsulato = [ricettari:risultato]
		render incapsulato as JSON
	}

	/**
	 *
	 * @return Memi collegati in JSON
	 */
	def listaMemi() {
		def risultato = []
		interfacciaContestoService.contesto.memi.each{ meme ->
			try{
				String idMeme = meme.idMeme
				List<String> listRep = repositoryService.createDeploymentQuery().deploymentName(idMeme).list()*.getId()
				//String datiRep = repositoryService.createDeploymentQuery().deploymentName(idMeme).singleResult()
				def processi = []
				listRep.each{ rep->
					String idRep = rep
					List<String> processiID = repositoryService.createProcessDefinitionQuery().deploymentId(idRep).list()*.getId()
					processiID.each{
						processi.add([processo:it,istanze:runtimeService.createExecutionQuery().processDefinitionId(it).count()])
					}
				}
				def calcolati = [tooltip:meme.tooltip(),maschera:meme.maschera(),dashboard:meme.dashboard(),box:meme.box(),iconaStato:meme.iconaStato()]
				risultato.add([meme:meme,processi:processi,calcolati:calcolati])
			} catch (Exception ee){log.warn("Errore nella lettura del meme: "+ee)}
		}
		def incapsulato = [memi:risultato]
		render incapsulato as JSON
	}

	/**
	 *
	 * @return Store dati JCloud collegati in JSON
	 */
	def listaStore() {
		def risultato = []
		def risultatoBin = []
		interfacciaContestoService.stato.consul.getKVValues('').getValue().each{ risultato.add([key:it.key,createIndex:it.createIndex,modifyIndex:it.modifyIndex,value:new String(it.value.decodeBase64())]) }
		def incapsulato = [storedati:risultato]
		render incapsulato as JSON
	}

	/**
	 * 
	 * Salva un valore in consul
	 */
	def salvaValoreKV() {
		String chiave = request.JSON.chiave
		String valore = request.JSON.valore
		interfacciaContestoService.stato.consul.setKVValue(chiave, valore)
		sendMessage("activemq:topic:interfaccia.eventi",([tipo:'KVAGGIUNTO',chiave:chiave,valore:valore] as JSON).toString())
		render "ok"
	}

	/**
	 *
	 * Salva un valore in consul
	 */
	def cancellaValoreKV() {
		String chiave = request.JSON.chiave
		interfacciaContestoService.stato.consul.deleteKVValue(chiave)
		sendMessage("activemq:topic:interfaccia.eventi",([tipo:'KVRIMOSSO',chiave:chiave] as JSON).toString())
		render "ok"
	}

	/**
	 *
	 * Salva il contesto in consul KV
	 */
	def salvaContestoinKV() {
		JSON contestoJson = interfacciaContestoService.contesto.esporta() as JSON
		interfacciaContestoService.stato.consul.setKVValue("contesto_"+new Date().format("yyyyMMddHHmmss", TimeZone.getTimeZone("UTC")), contestoJson.toString())
		sendMessage("activemq:topic:interfaccia.eventi",([tipo:'KVSALVATAGGIOCONTESTO',contesto:interfacciaContestoService.contesto.descrizione] as JSON).toString())
		render "ok"
	}

	/**
	 *
	 * Salva il contesto su un vaso
	 */
	def salvaContestoSuVaso() {
		String vasoRicerca = request.JSON.vaso
		Vaso vasoTarget = interfacciaContestoService.contesto.vasi.find{ vaso -> vaso.idVaso == vasoRicerca}
		String vasoEtichetta = vasoTarget.etichetta
		Contesto contestoDaSalvare = interfacciaContestoService.contesto
		vasoTarget.salvaContesto(contestoDaSalvare)
		sendMessage("activemq:topic:interfaccia.eventi",([tipo:'VASOSALVATAGGIOCONTESTO',contesto:interfacciaContestoService.contesto.descrizione,vaso:vasoEtichetta] as JSON).toString())
		render "ok"
	}

	/** copia il contesto da KV Consul ad un vaso */
	def salvaContestoDaKVsuMaster() {
		JsonSlurper jsonSlurper = new JsonSlurper()
		Map dato = jsonSlurper.parseText(request.JSON.dato)
		Contesto contestoCaricato = new Contesto().importa(dato)
		contestoCaricato.salva()
		render 'ok'
	}

	def resettaInterfacciaSuVaso() {
		String idVasoTarget = request.JSON.vaso
		Vaso vasoTarget = interfacciaContestoService.contesto.vasi.find{it.idVaso == idVasoTarget}
		bootStrapService.macchinaMaster = vasoTarget.macchina
		bootStrapService.portaMaster = vasoTarget.porta
		bootStrapService.utenteMaster = vasoTarget.utente
		bootStrapService.keyMaster = vasoTarget.key
		bootStrapService.idContestoScelto = null
		bootStrapService.idInterfacciaScelta = null
		bootStrapService.inAvvio = true
		bootStrapService.inReset = true
		bootStrapService.contestoScelto = false
		bootStrapService.interfacciaScelta = false
		bootStrapService.configurato = true
		bootStrapService.raggiungibile = true
		bootStrapService.contesto = null
		bootStrapService.interfaccia = null
		bootStrapService.contestiInMaster = []
		bootStrapService.interfacceInContesto = []
		bootStrapService.utentiInContesto = []
		interfacciaContestoService.connessioneConsul.finalize()
		interfacciaContestoService.connessioneActiveMQ.finalize()
		interfacciaContestoService.connnessioniSSH.each{it.finalize()}
		UtenteRuolo.findAll().each{it.delete(flush: true)}
		Utente.findAll().each{it.delete(flush: true)}
		Ruolo.findAll().each{it.delete(flush: true)}
		bootStrapService.valoreCasuale=org.apache.commons.lang.RandomStringUtils.random(5, true, true).toString()
		render 'ok'
	}

	def salvaConfigurazioneInterfaccia() {
		String risposta = "Errore nel salvataggio della configurazione..."
		try{
			new File("${System.properties.'user.home'}/.ar4k").mkdirs()
			File file = new File("${System.properties.'user.home'}/.ar4k/AgenteAr4k-config.groovy")
			file.write "//File di configurazione Agente Ar4k by Rossonet -generato automaticamente\n"
			file << "//by Ambrosini -Rossonet s.c.a r.l.-\n\n"
			file << "master.host = '"+interfacciaContestoService.contesto.vasoMaster.macchina+"'\n"
			file << "master.port = "+interfacciaContestoService.contesto.vasoMaster.porta+"\n"
			file << "master.user = '"+interfacciaContestoService.contesto.vasoMaster.utente+"'\n"
			file << 'master.key = """'+"\n"
			file << interfacciaContestoService.contesto.vasoMaster.key+"\n"
			file << '"""'+"\n"
			file << "contesto = '"+interfacciaContestoService.contesto.idContesto+"'\n"
			file << "interfaccia = '"+interfacciaContestoService.interfaccia.idInterfaccia+"'\n"

			risposta = "salvataggio effettuato"
		} catch(Exception ee){log.warn("Errore nel salvataggio della configurazione interfaccia in locale: "+ee.toString())}
		render risposta
	}

	def scaricaConfigurazioneInterfaccia() {
		def configurazione = [
			host:interfacciaContestoService.contesto.vasoMaster.macchina,
			port:interfacciaContestoService.contesto.vasoMaster.porta,
			user:interfacciaContestoService.contesto.vasoMaster.utente,
			key:interfacciaContestoService.contesto.vasoMaster.key,
			contesto:interfacciaContestoService.contesto.idContesto,
			interfaccia:interfacciaContestoService.interfaccia.idInterfaccia
		]
		//response.setHeader('Content-disposition', +"attachment; filename="+interfacciaContestoService.contesto.idContesto+".json")
		render text:configurazione as JSON,contentType:"text/json",encoding: "UTF-8"
	}

	/**
	 *
	 * @return Chiamata JSON per creare un nuovo vaso da parametri
	 */
	def aggiungiVaso() {
		def vaso = request.JSON.vaso
		log.info("Richiesta aggiunta vaso: "+vaso)
		Vaso vasoAggiunto= new Vaso(
				etichetta:vaso.etichetta,
				descrizione:vaso.descrizione,
				macchina:vaso.hostssh,
				porta:vaso.porta.toInteger(),
				utente:vaso.utente,
				key:vaso.key
				)
		if (
		interfacciaContestoService.contesto.vasi.add(vasoAggiunto)
		) {
			Vaso vasoTrovato = interfacciaContestoService.contesto.vasi.find{it.macchina == vasoAggiunto.macchina && it.porta == vasoAggiunto.porta && it.utente == vasoAggiunto.utente && it.key == vasoAggiunto.key }
			vasoTrovato.provaConnessione()
			vasoTrovato.provaVaso()
			interfacciaContestoService.contesto.ricettari.each{
				if (it.clonaOvunque == true ) {
					vasoTrovato.avviaRicettario(it)
				}
			}
			vasoTrovato.avviaConsulClient(interfacciaContestoService.contesto.vasoMaster.macchina)
			sendMessage("activemq:topic:interfaccia.eventi",([tipo:'VASOAGGIUNTO',messaggio:vaso] as JSON).toString())
			render "ok"
		} else {
			render "errore"
		}
	}

	def eliminaVaso() {
		String idVaso = request.JSON.vaso
		String risultato = 'Vaso non eliminato'
		if (interfacciaContestoService.contesto.vasoMaster.idVaso != idVaso ) {
			int conto = interfacciaContestoService.contesto.vasi.size()
			interfacciaContestoService.contesto.vasi.removeAll{ it.idVaso == idVaso }
			if (conto!=interfacciaContestoService.contesto.vasi.size()){
				int differenza = conto-interfacciaContestoService.contesto.vasi.size()
				risultato = "Eliminato "+differenza.toString()+" vaso"
				sendMessage("activemq:topic:interfaccia.eventi",([tipo:'VASOELIMINATO',vaso:idVaso] as JSON).toString())
			}
		}
		render risultato
	}

	/**
	 * 
	 * @return Chiamata JSON per aggiungere un ricettario al contesto
	 */
	def aggiungiRicettario() {
		def ricettario = request.JSON.ricettario
		log.info("Richiesta aggiunta ricettario: "+ricettario)
		sendMessage("activemq:topic:interfaccia.eventi",([tipo:'RICETTARIOAGGIUNTO',messaggio:request.JSON.ricettario] as JSON).toString())
		RepositoryGit repositoryGit = new RepositoryGit(indirizzo:ricettario.repo,nomeCartella:ricettario.cartella)
		if (
		interfacciaContestoService.contesto.ricettari.add(
		new Ricettario(etichetta:ricettario.etichetta,descrizione:ricettario.descrizione,repositoryGit:repositoryGit)
		)
		) {
			render "ok"
		} else {
			render "errore"
		}
	}

	/**
	 * Cancella la definizione di un ricettario
	 */
	def eliminaRicettario() {
		String idRicettario = request.JSON.idricettario
		interfacciaContestoService.contesto.ricettari.removeAll{it.idRicettario == idRicettario}
		try {
			sendMessage("activemq:topic:interfaccia.eventi",([tipo:'RICETTARIOELIMINATO',messaggio:idRicettario.toString()]  as JSON).toString())
			render "ok"
		} catch (Exception ee){
			log.info "Evento da AdminController non comunicato: "+ee.toString()
			render "errore"
		}
	}

	/**
	 *
	 * @return Chiamata JSON per aggiornare un ricettario. Aggiorna i repository git e ricarica i semi
	 */
	def aggiornaRicettario() {
		String idRicettario = request.JSON.idricettario
		interfacciaContestoService.contesto.ricettari.each{
			if (it.idRicettario == idRicettario) {
				if (it.clonaOvunque) {
					interfacciaContestoService.contesto.vasi*.avviaRicettario(it)
				}
				interfacciaContestoService.contesto.vasoMaster.caricaSemi(it)
				it.aggiornato = new Date().format("yyyyMMddHHmmss", TimeZone.getTimeZone("UTC")).toString()
			}
		}
		render "ok"
	}

	/**
	 *
	 * @return Chiamata JSON per creare un meme da un seme.
	 */
	def creaMeme() {
		String idSeme = request.JSON.seme
		log.info("Richiesta installazione seme "+idSeme)
		interfacciaContestoService.contesto.ricettari.each{ ricettario ->
			ricettario.semi.each{
				if (it.meme.idMeme == idSeme) {
					log.info("il seme "+it.meme+" corrisponde")
					Meme nuovoMeme = new Meme()
					nuovoMeme = it.meme.clone()
					// Rigenera tutti gli id casuali per non rischiare riferimenti errati nelle fasi di esecuzione
					nuovoMeme.idMeme = UUID.randomUUID()
					nuovoMeme.pathVaso = '~/.ar4k/ricettari/'+ ricettario.repositoryGit.nomeCartella
					nuovoMeme.metodi.each{it.idMetodo=UUID.randomUUID()}
					nuovoMeme.caricaProcessiActiviti(interfacciaContestoService.contesto.vasoMaster,interfacciaContestoService.processEngine)
					interfacciaContestoService.contesto.memi.add(nuovoMeme)
				}
			}
		}
		render "ok"
	}

	/**
	 *
	 * @return Chiamata JSON per ottenere la lista di semi in un ricettario.
	 */
	def listaSemi() {
		String idRicettario = request.JSON.idricettario
		def incapsulato
		List<Seme> risultato = []
		interfacciaContestoService.contesto.ricettari.each{
			if (it.idRicettario == idRicettario) {
				it.semi.each{ seme -> risultato.add(seme) }
				incapsulato = [semi:risultato]
			}
		}
		if ( risultato.size() > 0 ) {
			render incapsulato as JSON
		} else {
			render 'none'
		}
	}

	def listaBlobStoreJCloud() {
		def incapsulato
		interfacciaContestoService.jCloudServer.each{ incapsulato.add(it) }
		render incapsulato as JSON
	}

	/**
	 * 
	 * @return Foglio di stile AngularJS. Tramite la configurazione grafica dell'interfaccia è possibile variare lo stile
	 */
	def sbadmin2() {
		render(template: "sbadmin2",contentType:"text/css",model:[grafica: interfacciaContestoService.interfaccia.grafica])
	}

	/**
	 *
	 * @return Foglio di stile AngularJS. Tramite la configurazione grafica dell'interfaccia è possibile variare lo stile
	 */
	def main() {
		render(template: "main",contentType:"text/css",model:[grafica: interfacciaContestoService.interfaccia.grafica])
	}

	/**
	 *
	 * @return Foglio di stile AngularJS. Tramite la configurazione grafica dell'interfaccia è possibile variare lo stile
	 */
	def timeline() {
		render(template: "timeline",contentType:"text/css",model:[grafica: interfacciaContestoService.interfaccia.grafica])
	}
	
	
	
	/** verifica connettività e utente*/
	def testConnessione() {
		try {
			render "Connessione con utente "+springSecurityService.currentUser.username+" id: "+springSecurityService.currentUser.id
		} catch (Exception e) {
			render "utente anonimo"
		}
	}

	/**
	 * Restituisce il form di avvio per il processo
	 * @return form inizio processo
	 */
	def avvioProcessoForm(String idProcesso) {
		String formStream = formService.getRenderedStartForm(idProcesso)
		render formStream
	}
	
	def taskProcessoForm(String idTask) {
		String formStream = formService.getRenderedTaskForm(idTask)
		render formStream
	}

	/**
	 * Restituisce un percorso relativo al repository di un Meme
	 */
	def ritornaFile(String idMeme,String target) {
		String fileTarget = interfacciaContestoService.contesto.memi.find{it.idMeme == idMeme}.pathVaso + target
		render interfacciaContestoService.contesto.vasoMaster.leggiConCat(fileTarget)
	}

	/**
	 * Restituisce la maschera dello stato attuale di un meme
	 * @return maschera meme
	 */

	def mascheraMeme(String idMeme) {
		log.info "Richiesta maschera per meme "+idMeme
		render interfacciaContestoService.contesto.memi.find{it.idMeme == idMeme}.maschera()
	}
	
	def cancellaMeme(String idMeme) {
		log.info "Richiesta disinstallazione per meme "+idMeme
		render interfacciaContestoService.contesto.memi.find{it.idMeme == idMeme}.disinstallazione
	}

	/** ritorna l'elenco di parametri necessari nella maschera di avvio */
	def variabiliAvvioProcesso(String idProcesso) {
		def variabili = []
		formService.getStartFormData(idProcesso).getFormProperties().each{
			variabili.add([id:it.getId(),name:it.getName(),type:it.getType(),
				value:it.getValue(),readable:it.isReadable(),writable:it.isWritable(),
				required:it.isRequired()])
		}
		def incapsulato = [variabili:variabili]
		render incapsulato as JSON
	}

	/** Avvia il processo -chiamata POST-*/
	def avviaProcesso() {
		String idProcesso = request.JSON.idProcesso
		String dati = request.JSON.dati
		log.info "Richiesto avvio processo: "+idProcesso
		def processo = runtimeService.startProcessInstanceById(idProcesso,dati)
		render processo?'avviato...':'errore!'
	}


	def diagrammaStatoProcesso(String idProcesso) {
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
				.processDefinitionId(idProcesso).singleResult()
		String diagramResourceName = processDefinition.getDiagramResourceName()
		InputStream imageStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(), diagramResourceName)
		render file: imageStream, contentType: 'image/png'
	}

	/**
	 * @return immagine processo
	 */
	def diagrammaProcesso(String idProcesso) {
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
				.processDefinitionId(idProcesso).singleResult()
		String diagramResourceName = processDefinition.getDiagramResourceName()
		InputStream imageStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(), diagramResourceName)
		render file: imageStream, contentType: 'image/png'
	}

	def listaIstanze(String idProcesso){
		def variabili = []
		runtimeService.createProcessInstanceQuery().processDefinitionId(idProcesso).list().each{
			String taskid = taskService.createTaskQuery().processInstanceId(it.getId()).list().last().id
			variabili.add([
				id:it.getId(),
				businessKey:it.getBusinessKey(),
				activityId:it.getActivityId(),
				deploymentId:it.getDeploymentId(),
				name:it.getName(),
				processInstanceId:it.getProcessInstanceId(),
				sospeso:it.isSuspended(),
				taskid:taskid
			])
		}
		def incapsulato = [istanze:variabili]
		render incapsulato as JSON
	}

	def listaTask(){
		def risultato = []
		/*
		taskService.createTaskQuery().list().each{
			String icona = interfacciaContestoService.contesto.memi.find{ meme->
				ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
				.processDefinitionId(it.getProcessDefinitionId()).singleResult()
				String depId = processDefinition.getDeploymentId()
				meme.idMeme == repositoryService.createDeploymentQuery().deploymentId(depId).singleResult().getName()
			}.icona
			
			
			it.getProcessDefinitionId()
			risultato.add([id:it.id,
				icona:icona,
				assignee:it.getAssignee(),
				category:it.getCategory(),
				createTime:it.getCreateTime(),
				delegationState:it.getDelegationState(),
				description:it.getDescription(),
				dueDate:it.getDueDate(),
				executionId:it.getExecutionId(),
				formKey:it.getFormKey(),
				name:it.getName(),
				owner:it.getOwner(),
				parentTaskId:it.getParentTaskId(),
				priority:it.getPriority(),
				processDefinitionId:it.getProcessDefinitionId(),
				processInstanceId:it.getProcessInstanceId(),
				processVariables:it.getProcessVariables(),
				taskDefinitionKey:it.getTaskDefinitionKey(),
				tenantId:it.getTenantId()
			])
		}
		*/
		def incapsulato = [tasks:risultato,conto:risultato.size()]
		render incapsulato as JSON
	}
}

