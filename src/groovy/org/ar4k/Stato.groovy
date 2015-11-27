
package org.ar4k
import java.util.List

import com.ecwid.consul.v1.ConsulClient
import com.jcraft.jsch.*
import com.subgraph.orchid.TorClient
import com.subgraph.orchid.TorInitializationListener

import grails.converters.JSON
import grails.util.Holders

/**
 * Stato
 *
 * <p>Lo stato rappresenta la situazione attuale dell'infrastruttura di rete mantenuta da Consul
 * per la connessione vengono usate le API Java (https://github.com/Ecwid/consul-api)
 * </p>
 *
 * <p style="text-justify">
 * Uno stato rappresenta una connesione al demone Consul
 * tramite le API Java ed è utilizzato dal bootstrap e dall'interfaccia per interagire con Consul.
 * </p>
 *
 * @author Andrea Ambrosini (Rossonet s.c.a r.l)
 * @version 0.2-alpha
 * @see org.ar4k.Contesto
 * @see org.ar4k.IstanzaConsul
 * @see org.ar4k.ConnessioneSSH
 */

class Stato {
	/** servervice bootsrap iniettato */
	BootStrapService bootStrapService
	/** servervice interfaccia iniettato */
	InterfacciaContestoService interfacciaContestoService
	/** id univoco stato connessione a Consul */
	String idStato = UUID.randomUUID()
	/** client Consul collegato allo Stato */
	ConsulClient consul = null
	/** Host connessione (ip o dns)*/ 
	String macchinaConsul = null
	/** porta connessione Consul */
	Integer portaConsul = null
	/** necessario bridge ssh a un Vaso per effettuare la connessione a Consul? */
	Boolean connessioneSSH = false
	/** lista connessioni SSH per accedere a Consul disponibili */
	List<ConnessioneSSH> listaGWSSH = []
	/** tunnel ssh utilizzato */
	String tunnelSSH = null
	/** necessario ssh over proxy Socks */
	Boolean connessioneProxy = false
	/** lista proxy disponibili */
	List<Proxy> listaProxy = []
	/** connessione Onion (Tor) */
	Boolean connessioneOnion = false
	/** client tor */
	TorClient client
	/** tor avviato? */
	Boolean torAttivo = false

	//Integer torDashPort = 9666
	/** porta proxy tor */
	Integer portaProxyTOR = 9050

	/** porta tunnel ssh */
	Integer portaTunnelSSH = 0

	/** codice accesso commerciale Ar4k */
	String codiceAttivazione = null

	/** stato connessione Consul. Per ora i valori possibili sono "configurazione","installazione" e "attivo" */
	String stato = 'configurazione'

	/** l'installazione Consul è governata da questo contesto */
	Boolean controlloConsul = true

	String toString() {
		String ritorno = idStato+' ['+stato+']'
		if (stato=='configurazione') ritorno = 'template '+idStato
		if (stato=='installazione') ritorno = 'installazione Consul ['+idStato+'] su host '+macchinaConsul
		if (stato=='attivo') ritorno = consul.getStatusLeader()
		return ritorno
	}

	/** testa la connessione senza sincronizzare i dati e chiudendola immediatamente */
	Boolean testConnessione() {
		stato='installazione'
		return false
	}

	/** attiva tor */
	Boolean attivaProxyTOR() {
		stato='installazione'
		Boolean errore = false
		try {
			if (client==null) {
				client = new TorClient()
			}else{
				client.stop()
				client = new TorClient()
			}
			client.addInitializationListener(new TorInitializationListener() {
						@Override
						public void initializationProgress(String string, int i) {
							log.info("Avvio tor al "+i+"% - "+string)
							// throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
						}
						@Override
						public void initializationCompleted() {
							try {
								log.info("Tor pronto!")
								torAttivo=true
							} catch (Exception e) {
								log.warn('Errore avvio TOR: '+e.printStackTrace())
								stato='errore attivazione TOR'
							}
						}
					})
			client.start()
			client.enableSocksListener(9050)
			//client.enableSocksListener(portaProxyTOR)
			//client.waitUntilReady(29000)
			//client.enableDashboard()
		} catch (Exception eee) {
			errore=true
			log.warn('Errore avvio TOR: '+eee.printStackTrace())
			stato='errore attivazione TOR'
		}
		return !errore
	}

	/** connette permanentemente Consul */
	Boolean connetti() {
		Boolean ritorno = false
		if(connessioneOnion) attivaProxyTOR()

		if(connessioneProxy) {
			// configura i parametri per il proxy
		}

		// configura il tunnel ssh
		if(connessioneSSH && tunnelSSH) {
			if (portaTunnelSSH == 0 || portaTunnelSSH == null) {
				try{
					ServerSocket s = new ServerSocket(0) // dovrebbe ritornare una porta libera casuale
					portaTunnelSSH = s.getLocalPort()
					s.close()
					log.info("userò la porta "+portaTunnelSSH+" per il tunnel ssh")
				} catch (IOException e) {
					log.warn("non trovo una porta tcp libera...")
				} }

			ConnessioneSSH connessioneAttiva = listaGWSSH.find{it.toString() == tunnelSSH}
			log.info("uso il tunnel: "+connessioneAttiva.macchina)
			connessioneAttiva.verificaAvvia(portaTunnelSSH,macchinaConsul,portaConsul)
		}

		try {
			if (connessioneSSH && tunnelSSH != 0) {
				consul = new ConsulClient('127.0.0.1',portaTunnelSSH)
			} else {
				if (macchinaConsul && macchinaConsul) consul = new ConsulClient(macchinaConsul,portaConsul)
			}
			String risposta = consul?.getCatalogDatacenters()
			log.info("Connessione Consul. Datacenter disponibili: "+risposta)
			stato='attivo'
			if (risposta) ritorno = true
		} catch (Exception ee) {
			// non stampa tutto lo stacktrace in avvio
			log.warn("Errore connessione Consul")
			stato='errore connessione Consul'
		}

		return ritorno
	}

	/** salva un valore sul datastore Consul */
	Boolean salvaValore(String chiave,String tipologia,String valore) {
		return consul.setKVValue(tipologia+'/'+chiave, valore)
	}

	/** leggi un valore dal datastore Consul */
	String leggiValore(String id,String tipologia) {
		return 'da implementare...'
	}

	/** sincronizza i dati */
	Boolean sincronizza() {
		return false
	}

	/** Ritorna l'elenco dei contesti presenti in consul per il bootstrap  */
	def listaContesti() {
		def risultati = []
		consul.getKVValues('org-ar4k-contesto/').getValue().each{
			risultati.add([
				key:it.key,
				createIndex:it.createIndex,
				modifyIndex:it.modifyIndex,
				value:new String(it.value.decodeBase64())
			])
		}
		return risultati
	}

	/** Ritorna l'elenco delle interfacce presenti in consul per il bootstrap  */
	def listaInterfacce() {
		def risultati = []
		consul.getKVValues('org-ar4k-interfaccia/').getValue().each{
			risultati.add([
				key:it.key,
				createIndex:it.createIndex,
				modifyIndex:it.modifyIndex,
				value:new String(it.value.decodeBase64())
			])
		}
		return risultati
	}

	/** salva in uno Stato specifico a catena per N profondità */
	Boolean salva(Stato stato,Integer contatore) {
		// nessuna attività di salvataggio
		return false
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
}





