/**
 * Vaso
 *
 * <p>Un vaso rappresenta un account su un nodo Linux</p>
 *
 * <p style="text-justify">
 * Il vaso è l'unita base in cui opera Ar4k. Tramite il vaso si gesticono le installazioni Consul e della console stessa.
 * E' possibile effettuare scansioni di rete e locali e connettersi ai vasi via ssh.
 * Uno o più vasi appartengono a un nodo.</br>
 * </p>
 *
 * @author Andrea Ambrosini (Rossonet s.c.a r.l)
 * @version 0.1-alpha
 * @see org.ar4k.ConnessioneSSH
 * @see org.ar4k.Nodo
 * @see org.ar4k.Contesto
 */

package org.ar4k

import org.jclouds.json.config.GsonModule.ByteArrayAdapter;
import com.google.common.io.ByteStreams.ByteArrayDataInputStream
import com.google.common.io.ByteStreams.ByteArrayDataOutputStream;
import com.jcraft.jsch.*
import grails.converters.JSON
import groovy.json.JsonSlurper
import java.util.zip.ZipInputStream
import grails.util.Holders

class Vaso {
	/** id univoco vaso */
	String idVaso = UUID.randomUUID()
	/** etichetta vaso */
	String etichetta = null
	/** descrizione vaso */
	String descrizione = null
	/** nodo contenitore del vaso. Ovvero l'host che ospita l'account ssh associato a questo vaso */
	Nodo nodo = null
	/** parametri di connessione ssh */
	ConnessioneSSH ssh = null
	/** Stringa proxy (esportata come http_proxy) */
	String proxy = null
	/** l'utenza ha l'accesso root sulla macchina come root usando #sudo su - */
	Boolean sudo = false
	/** variabile PATH sulla macchina */
	String path = '/usr/local/bin:/usr/bin:/bin'
	/** job gestiti dalla machina via ssh */
	List<LavorazioneSSH> lavorazioni = []
	/** ricettari necessari sul vaso */
	List<Ricettario> ricettari = []
	/** funzionalità. In particolare: 
	 * - accesso a internet; 
	 * - software installato sul sistema; 
	 * - particolari funzionalità hardware;
	 * - dispositivi collegati via usb, seriale, midi ecc...
	 * le funzionalità vengono rilevate dal sistema di scansione
	 */
	List<String> funzionalita = []
	/** host pubblico per test di raggiungibilità */
	String indirizzoTest='http://hc.rossonet.name'

	/** salva in uno Stato specifico a catena per N profondità */
	Boolean salva(Stato stato,Integer contatore) {
		stato.salvaValore(idVaso,'org-ar4k-vaso',(esporta() as JSON).toString())
		if (contatore > 0) {
			contatore = contatore -1
			lavorazioni*.salva(stato,contatore)
			ricettari*.salva(stato,contatore)
		}
	}
	/** salva in uno Stato specifico solo l'oggetto */
	Boolean salva(Stato stato) {
		salva(stato,0)
	}
	/** salva nello stato di default solo l'oggetto */
	Boolean salva() {
		salva(Holders.applicationContext.getBean("interfacciaContestoService").stato,0)
	}
	/** salva nello stato di default a catena per N profondità */
	Boolean salva(Integer contatore) {
		salva(Holders.applicationContext.getBean("interfacciaContestoService").stato,contatore)
	}

	/** esporta il contenuto dell'oggetto */
	def esporta() {
		return [
			idVaso:idVaso,
			etichetta:etichetta,
			descrizione:descrizione,
			sudo:sudo,
			path:path,
			funzionalita:funzionalita,
			proxy:proxy,
			nodo:nodo?.idNodo,
			lavorazioni:lavorazioni*.idLavorazione,
			ricettari:ricettari*.idRicettario
		]
	}

	/** avvia agente consul in modalità client */
	LavorazioneSSH avviaConsulClient(IstanzaConsul consul,Boolean reset=false) {
		return false
	}

	/** avvia agente consul in modalità client */
	LavorazioneSSH avviaConsulServer(IstanzaConsul consul,Boolean reset=false) {
		return false
	}

	/** avvia interfaccia ar4k */
	LavorazioneSSH avviaInterfacciaAr4k(Boolean reset=false) {
		return false
	}

	/** avvia interfaccia web ssh in node */
	LavorazioneSSH avviaInterfacciaSSHWeb(Boolean reset=false) {
		return new ConsoleJS(this).avviaConsole()
	}

	/** Scansione funzionalità e stato
	 * 
	 * @return oggetto scansione 
	 */
	Scansione selfScan() {
		return 'non implementata'
	}

	/** Scansione con NMAP 
	 * 
	 * @return oggetto scansione 
	 */
	Scansione scansioneNmap() {
		return 'non implementata'
	}

	/** Scansione con OpenVas
	 * 
	 * @return oggetto scansione 
	 */
	Scansione scansioneOpenVas() {
		return 'non implementata'
	}

	/** creazione directory per ar4k */
	Boolean creaStruttura() {
		return ssh.creaStruttura()
	}

	/** importa la configurazione di un vaso da json */
	Vaso importa(Map json){
		log.debug("importa() il vaso: "+json.idVaso)
		Vaso vasoCreato = new Vaso(
				idVaso:json.idVaso,
				etichetta:json.etichetta,
				descrizione:json.descrizione,
				macchina:json.macchina,
				porta:json.porta,
				utente:json.utente,
				key:json.key,
				sudo:json.sudo,
				path:json.path,
				funzionalita:json.funzionalita,
				javaVersion:json.javaVersion,
				proxy:json.proxy
				)
		return vasoCreato
	}

	/** stringa predefinita */
	String toString() {
		return etichetta+" => "+ssh.utente+"@"+ssh.macchina+":"+ssh.porta
	}
}





