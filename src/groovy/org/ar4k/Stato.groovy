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

import com.ecwid.consul.v1.ConsulClient
import com.jcraft.jsch.*;

class Stato {
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
	List<ConnessioneSSH> listaGWSSH = []/** necessario ssh over proxy Socks */
	Boolean connessioneProxySocks = false
	/** lista proxy disponibili */
	List<Proxy> listaProxy = []
	/** connessione Onion (Tor) */
	Boolean connessioneOnion = false

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

	/** connette permanentemente Consul */
	Boolean connetti() {
		Boolean ritorno = false

		if(connessioneOnion) {
			// Avvia il client Tor - Onion
		}

		if(connessioneProxySocks) {
			// configura i parametri per il proxy
		}
		if(connessioneSSH) {
			listaGWSSH*.verificaAvvia(8569,macchinaConsul,portaConsul)
			// connette attraverso un tunnel SSH
			// da gestire l'allocazione delle porte
		}

		try {
			if (connessioneSSH) {
				consul = new ConsulClient('127.0.0.1',8569)
			} else {
				if (macchinaConsul && macchinaConsul) consul = new ConsulClient(macchinaConsul,portaConsul)
			}
			String risposta = consul?.getCatalogDatacenters()
			log.debug("Connessione Consul. Datacenter disponibili: "+risposta)
			if (risposta) ritorno = true
		} catch (Exception ee) {
			log.warn("Errore connessione Consul: "+ee.printStackTrace())
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

class Proxy {
	/** id univoco proxy */
	String idProxy = UUID.randomUUID()
	/** indirizzo ip o dns host connessione ssh */
	String macchina = '127.0.0.1'
	/** porta connessione ssh */
	Integer porta = 3128
	/** utente proxy */
	String utente = ''
	/** password proxy */
	String password = ''
	/** tipologia proxy */
	String tipologia = 'SOCKS5'
}

class ConnessioneSSH {
	/** id univoco connessione SSH */
	String idConnessioneSSH = UUID.randomUUID()
	/** indirizzo ip o dns host connessione ssh */
	String macchina = '127.0.0.1'
	/** porta connessione ssh */
	Integer porta = 22
	/** utente ssh */
	String utente = 'root'
	/** chiave privata ssh */
	String key = null
	/** bind SSH */
	JSch connessione = new JSch()

	Boolean verificaAvvia(Integer gwPort,String targetHost,Integer targetPort) {
		if (!connessione) {
			connessione = new JSch()
		}
		addLTunnel(gwPort,targetHost,targetPort)

		return true
	}

	/** Aggiunge un tunnel SSH Left*/
	private void addLTunnel(int lport, String rhost, int rport) {
		try {
			Channel canale
			// Aggiunge la chiave privata
			connessione.addIdentity(utente,key.getBytes(),null,null)
			Session sessione=connessione.getSession(utente, macchina, porta)
			sessione.setConfig("StrictHostKeyChecking","no")
			sessione.setConfig("UserKnownHostsFile","/dev/null")
			sessione.connect()
			try {sessione.setPortForwardingL(lport, rhost, rport)} catch(Exception e){log.warn("Errore nel tunnel ssh per nodo "+macchina+" tunnel "+lport+":"+rhost+":"+rport+". Probabilmente la porta è già in uso")}
		}	catch(JSchException e){
			log.warn("Errore nella creazione del tunnel SSH: "+e.printStackTrace())
		}
	}
}
