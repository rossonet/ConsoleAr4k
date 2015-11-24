/**
 * Contesto
 *
 * <p>Un contesto operativo gestisce la correlazione tra vasi, ricettari, utenti e memi</p>
 *
 * <p style="text-justify">
 * Il contesto rappresenta un progetto, un insieme di risorse, utenti e codice che interagiscono.</br>
 * Il contesto espone la factory dei dns, QR e risorse di calcolo fornite da Provider.</br>
 * i memi possono associare degli eventi ai contesti.</br>
 * Il Contesto è collegato ad un sistema Consul attivo tramite accesso diretto alle API HTTP di un server Consul oppure tramite un
 * bridge SSH passante per la rete o tramite Onion (TOR)
 * </br>
 * Scopo di questa classe è mantenere le comunicazioni con il contesto Consul e mappare tutte le risorse del sistema mantenedo
 * la sincronizzaizone dei dati con lo storage dati Consul.
 * </p>
 *
 * @author Andrea Ambrosini (Rossonet s.c.a r.l)
 * @version 0.1-alpha
 * @see org.ar4k.Meme
 * @see org.ar4k.Vaso
 * @see org.ar4k.Regione
 * @see org.ar4k.Ricettario
 * @see org.ar4k.Interfaccia
 * @see org.ar4k.Connesione
 * @see org.ar4k.Utente
 */

package org.ar4k

import grails.converters.JSON
import groovy.transform.AutoClone
import grails.util.Holders

class Contesto {
	/** id univoco contesto */
	String idContesto = UUID.randomUUID()
	/** etichetta contesto */
	String etichetta = null
	/** descrizione contesto */
	String descrizione = null
	/** campo per link a CRM (progetto)*/
	String idProgetto = null
	/** ricettari a disposizione del contesto*/
	List<Ricettario> ricettari= []
	/** interfacce collegate al contesto*/
	List<Interfaccia> interfacce= []
	/** memi attivi nel contesto */
	List<Meme> memi = []
	/** vasi disponibili nel contesto */
	List<Vaso> vasi =[]
	/** lista nodi disponibili */
	List<Nodo> nodi = []
	/** lista regioni (datacenter Consul) disponibili */
	List<Regione> regioni = []
	/** factories installazioni e Cloud */
	List<Fabbrica> fabbriche = []
	/** utenti del contesto da importare in configurazione */
	List<UtenteRuolo> utentiRuoli = []
	/** Datacenter vasi contesto - condivisibile tra più contesti - */
	String datacenterConsul = null

	/** dump oggetto per funzioni di salvataggio*/
	def esporta() {
		log.info("esporta() il contesto: "+idContesto)
		return [
			idContesto:idContesto,
			etichetta:etichetta,
			descrizione:descrizione,
			idProgetto:idProgetto,
			interfacce:interfacce*.idInterfaccia,
			memi:memi*.idMeme,
			datacenterConsul:datacenterConsul,
			nodi:nodi*.idNodo,
			regioni:regioni*.idRegione,
			fabbriche:fabbriche*.idFabbrica,
			vasi:vasi*.idVaso,
			ricettari:ricettari*.idRicettario,
			utentiRuoli:utentiRuoli.each{
				it.ruolo+'/'+it.utente
			}
		]
	}

	/** salva il contesto sul nodo master */
	Boolean salva(Stato stato) {
		stato.salvaValore(idContesto,'org-ar4k-contesto',(esporta() as JSON).toString())
	}

	/** campo per l'avanzamanto del bootstrap (nascita,vita,virus,morte)*/
	private String statoBootStrap = 'nascita'
	/** se vero, salva il contesto su tutti i vasi connessi  */
	Boolean clonaOvunque = false


	/** lista provider Cloud - da verificare - */
	List<CloudProvider> cloudProviders= []
	/** short link e qr */
	List<Puntatore> puntatori = []

	/** vaso principale del contesto con demone Consul */
	Vaso vasoMaster

	/** ditruttore di classe (utile per la gestione della pulizia dei vasi)*/
	def destroy() {
		// Pulizia dei vasi
	}

	/** aggiunge il vaso master al contesto */
	Boolean configuraMaster(Vaso vaso) {
		vasoMaster = vaso
		vasi.add(vaso)
		return true
	}


	/** verifica ed eventualmente prova a riavviare tutti gli oggetti collegati*/
	Boolean avviaContesto() {
		Boolean risultato = true
		for (Vaso vaso in vasi ) {
			if (!vaso.provaVaso()) risultato = false
			for (Ricettario ricettario in ricettari) {
				if (!vaso.avviaRicettario(ricettario)) risultato = false
			}
		}
		for (Meme meme in memi) {
			if (!meme.verificaAvvia()) risultato = false
		}

		if (!risultato) log.warn("Problemi nel caricamento dei memi")

		log.debug("Importa "+utentiRuoli.size()+" utenti/ruoli")

		utentiRuoli.each{
			Holders.applicationContext.getBean("bootStrapService").aggiungiUtenteRuolo(it)
		}
		log.debug("Importati utenti e ruoli")

		if (risultato) {
			log.info("Contesto "+etichetta+" avviato.")
			statoBootStrap = 'avviato'
		} else {
			log.warn("Il contesto non è stato caricato")
		}
		return risultato
	}

	Contesto importa(Map json){
		log.info("importa() il contesto: "+json.idContesto)
		Contesto contestoCreato = new Contesto(
				idContesto:json.idContesto,
				etichetta:json.etichetta,
				descrizione:json.descrizione,
				idProgetto:json.idProgetto,
				clonaOvunque:json.clonaOvunque,
				vasoMaster:new Vaso().importa(json.vasoMaster)
				)
		json.interfacce.each{contestoCreato.interfacce.add(new Interfaccia().importa(it))}
		json.memi.each{contestoCreato.memi.add(new Meme().importa(it))}
		json.utentiRuoli.each{
			Utente u = Utente.importa(it.utente)
			Ruolo r = Ruolo.importa(it.ruolo)
			UtenteRuolo ur = UtenteRuolo.importa(u,r)
			contestoCreato.utentiRuoli.add(ur)
			log.info("importato UtenteRuolo, utente "+ur.utente+" ruolo "+ur.ruolo)
		}
		json.vasi.each{contestoCreato.vasi.add(new Vaso().importa(it))}
		json.ricettari.each{contestoCreato.ricettari.add(new Ricettario().importa(it))}
		json.cloudProviders.each{contestoCreato.cloudProviders.add(new CloudProvider(it))}
		json.puntatori.each{contestoCreato.puntatori.add(new Puntatore(it))}

		return contestoCreato
	}

	String toString() {
		return etichetta + " su "+vasoMaster+" ("+statoBootStrap+")"
	}
}

class CloudProvider {
	/** id univoco metodo */
	String idCloudProvider = UUID.randomUUID()
	String etichetta = 'nessuna etichetta'
	String descrizione = 'nessuna descrizione'
	/** percorso file bar (Activiti) nel repository git -Ricettario- */
	String urlAccesso = ''
	String tipoCloud = ''
	String utenza = ''
	String chiaveAccesso = ''

	def esporta() {
		return [
			idMetodo:idMetodo,
			etichetta:etichetta,
			descrizione:descrizione,
			urlAccesso:urlAccesso,
			tipoCloud:tipoCloud,
			utenza:utenza,
			chiaveAccesso:chiaveAccesso
		]
	}
}

class Puntatore {
	/** id univoco */
	String idPuntatore = UUID.randomUUID()
	/** qr */
	String qr = UUID.randomUUID()
	String etichetta = 'nessuna etichetta'
	String descrizione = 'nessuna descrizione'
	/** percorso principale */
	String urlAccesso = ''
	/** tipo filtro: JSoup,Camel,Redirect,Risorsa su deploy Activiti,Risorsa Consul (da cat su nodo tramite consul) */
	String tipoFiltro = ''
	/** dati configurazione (dipende da tipo) */
	String datiJson = ''
	/** lista chiavi accesso alla risorsa -per sicurezza- */
	List<String> chiaviAccesso = []

	def esporta() {
		return [
			idPuntatore:idPuntatore,
			etichetta:etichetta,
			descrizione:descrizione,
			urlAccesso:urlAccesso,
			tipoFiltro:tipoFiltro,
			datiJson:datiJson,
			chiaveAccesso:chiaviAccesso
		]
	}
}
