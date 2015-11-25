/**
 * Stato
 *
 * <p>Lo stato rappresenta la situazione attuale dell'infrastruttura di rete mantenuta da Consul
 * per la connessione vengono usate le API Java (https://github.com/Ecwid/consul-api)
 * </p>
 *
 * <p style="text-justify">
 * Uno stato rappresenta una connesione al demone Consul tramite le API Java.
 * </p>
 *
 * @author Andrea Ambrosini (Rossonet s.c.a r.l)
 * @version 0.2-alpha
 * @see org.ar4k.Contesto
 */
package org.ar4k

import java.util.List;

import com.ecwid.consul.v1.ConsulClient
import com.jcraft.jsch.*
import com.subgraph.orchid.TorClient
import com.subgraph.orchid.TorInitializationListener

class Stato {
	/** server bootsrap iniettato */
	BootStrapService bootStrapService
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
		if (stato=='installazione') ritorno = 'installazione Consul ['+idStato+'] su host '+macchina
		if (stato=='attivo') ritorno = consul.getStatusLeader()
		return ritorno
	}

	/** testa la connessione senza sincronizzare i dati e chiudendola immediatamente */
	Boolean testConnessione() {
	}

	/** connette il ponte ssh */
	private Boolean connettiSSH() {
	}

	/** attiva tor */
	Boolean attivaProxyTOR() {
		Boolean errore = false
		/*
		 try{
		 ServerSocket s = new ServerSocket(0) // dovrebbe ritornare una porta libera casuale
		 portaProxyTOR = s.getLocalPort()
		 s.close()
		 } catch (IOException e) {
		 errore =true
		 log.warn("non trovo una porta tcp libera...")
		 }
		 */

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
								log.warn('Errore avvio TOR in listener: '+e.printStackTrace())
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
			log.warn('Errore avvio TOR '+eee.printStackTrace())
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
			if (risposta) ritorno = true
		} catch (Exception ee) {
			// non stampa tutto lo stacktrace in avvio
			log.warn("Errore connessione Consul")
		}

		return ritorno
	}

	/** salva un valore sul datastore Consul */
	Boolean salvaValore(String chiave,String tipologia,String valore) {
		return consul.setKVValue(tipologia+'/'+chiave, valore)
	}

	/** leggi un valore dal datastore Consul */
	String leggiValore(String id,String tipologia) {
	}

	/** sincronizza i dati */
	Boolean sincronizza() {
	}

	/** Ritorna l'elenco degli id contesti per il bootstrap*/
	def listaIdContesti() {
		def risultati = []
		consul.getKVValues('').getValue().each{
			log.info("trovato contesto "+new String(it.value.decodeBase64()))
			risultati.add([
				key:it.key,
				createIndex:it.createIndex,
				modifyIndex:it.modifyIndex,
				value:new String(it.value.decodeBase64())
			])
		}
		return risultati
	}
}



