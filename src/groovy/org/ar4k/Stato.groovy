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

/**
 *  Rappresentazione proxy di rete
 * 
 * 
 * 
 * @author andrea ambrosini
 *
 */
class Proxy {
	/** id univoco proxy */
	String idProxy = UUID.randomUUID()
	/** indirizzo ip o dns host connessione ssh */
	String macchina = '127.0.0.1'
	/** porta connessione ssh */
	Integer porta = 3128
	/** utente proxy */
	String utente = null
	/** password proxy */
	String password = null
	/** tipologia proxy */
	String protocollo = 'http'

	String toString() {
		String utenza = ''
		if (utente != null) utenza = utente+'@'
		return protocollo+'://'+utenza+macchina+':'+porta
	}
}

/**
 *  Rappresentazione connessione ssh
 *  con gestione proxy
 * 
 * 
 * @author andrea
 *
 */
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
	/** proxy per la connesione se 'tor' utilizza localhost 9050 */
	String proxyMaster = null
	/** bind SSH */
	JSch connessione = new JSch()

	/** attiva il tunnel senza proxy */
	Boolean verificaAvvia(Integer gwPort,String targetHost,Integer targetPort) {
		if (!connessione) {
			connessione = new JSch()
		}
		try {
			addLTunnel(gwPort,targetHost,targetPort)
			return true
		} catch (Exception ee) {
			log.warn("Errore avvio tunnel: "+ee.printStackTrace())
			return false
		}

	}

	String toString() {
		if (proxyMaster == null){
			return utente+'@'+macchina+':'+porta
		} else {
			return utente+'@'+macchina+':'+porta+' ('+proxyMaster+')'
		}
	}


	/** esegui un comando ssh */
	String esegui(String comando) {
		String risultato = ''
		//comando = 'export PATH='+path+' ; '+comando
		InputStream input = null
		Integer portaProxyT = null
		String macchinaProxyT = null
		try {
			Channel canale
			// Aggiunge la chiave privata
			connessione.addIdentity(utente,key.getBytes(),null,null)
			Session sessione=connessione.getSession(utente, macchina, porta)
			if (proxyMaster=='tor') {
				portaProxyT = 9050
				macchinaProxyT = '127.0.0.1'
			} else if (proxyMaster) {
				try{
					URL targetProxy = new URL(proxyMaster)
					portaProxyT = targetProxy.getPort()
					macchinaProxyT = targetProxy.getHost()
				} catch(Exception ee) {
					log.warn("Non riesco ad utilizzare il proxy "+proxyMaster)
				}
			}
			if (portaProxyT?.toInteger() != null && macchinaProxyT != null) {
				sessione.setProxy(new ProxySOCKS5(macchinaProxyT, portaProxyT.toInteger()))
			}
			sessione.setConfig("StrictHostKeyChecking","no")
			sessione.setConfig("UserKnownHostsFile","/dev/null")
			try{
				sessione.connect()
				canale=sessione.openChannel("exec")
				((ChannelExec)canale).setCommand(comando)
				canale.setInputStream(null)
				((ChannelExec)canale).setErrStream(System.err)
				input=canale.getInputStream()
				canale.connect()
			}catch(Exception ee){
				log.error("Errore apertura SSH: "+comando+" ERRORE: "+ee.printStackTrace())
			}
			byte[] tmp=new byte[1024]
			if(input){
				while(true){
					while(input.available()>0){
						int i=input.read(tmp, 0, 1024)
						if(i<0)break
							risultato += new String(tmp, 0, i)
					}
					if(canale.isClosed()){
						if(input.available()>0) continue
							//
							break
					}
					try{Thread.sleep(500);}catch(Exception ee){}
				}
			}
			canale.disconnect()
			sessione.disconnect()
		}catch(Exception e) {
			log.error("Errore connesione SSH nell'esecuzione del comando: "+comando+" ERRORE: "+e.printStackTrace())
		}
		return risultato
	}

	/** prova la connesione ssh */
	Boolean provaConnessione() {
		String comando = 'echo -n $((6+4))'
		String atteso = '10'
		String risultato = esegui(comando)
		log.info("risultato connessione "+toString()+" "+risultato+" (atteso:"+atteso+")")
		return risultato == atteso?true:false
	}

	/** Aggiunge un tunnel SSH Left*/
	private void addLTunnel(int lport, String rhost, int rport) {
		Integer portaProxyT = null
		String macchinaProxyT = null
		try {
			Channel canale
			// Aggiunge la chiave privata
			connessione.addIdentity(utente,key.getBytes(),null,null)
			Session sessione=connessione.getSession(utente, macchina, porta)
			sessione.setConfig("StrictHostKeyChecking","no")
			if (proxyMaster=='tor') {
				portaProxyT = 9050
				macchinaProxyT = '127.0.0.1'
			} else if (proxyMaster) {
				try{
					URL targetProxy = new URL(proxyMaster)
					portaProxyT = targetProxy.getPort()
					macchinaProxyT = targetProxy.getHost()
				} catch(Exception ee) {
					log.warn("Non riesco ad utilizzare il proxy "+proxyMaster)
				}
			}
			if (portaProxyT?.toInteger() != null && macchinaProxyT != null) {
				sessione.setProxy(new ProxySOCKS5(macchinaProxyT, portaProxyT.toInteger()))
			}
			sessione.connect()
			try {sessione.setPortForwardingL(lport, rhost, rport)} catch(Exception e){log.warn("Errore nel tunnel ssh per nodo "+macchina+" tunnel "+lport+":"+rhost+":"+rport+". Probabilmente la porta è già in uso")}
		}	catch(JSchException e){
			log.warn("Errore nella creazione del tunnel SSH: "+e.printStackTrace())
		}
	}
}

/**
 * IstanzaConsul
 *
 * <p>Rappresenta un'istanza consul su un vaso</p>
 *
 * <p style="text-justify">
 *
 * </p>
 *
 * @author Andrea Ambrosini (Rossonet s.c.a r.l)
 * @version 0.1-alpha
 * @see org.ar4k.Meme
 * @see org.ar4k.Ricettario
 * @see org.ar4k.Interfaccia
 * @see org.ar4k.Contesto
 */

class IstanzaConsul {
	/** id univoco */
	String idIstanzaConsul = UUID.randomUUID()
	/** ciclo di vita nel vaso */
	String stato = 'Inizializzazione'
	/** dns - The DNS server, -1 to disable. Default 8600.*/
	Integer portaDNS = 8600
	/** http - The HTTP API, -1 to disable. Default 8500.*/
	Integer portaHTTP = 8500
	/** https - The HTTPS API, -1 to disable. Default -1 (disabled).*/
	Integer portaHTTPS = -1
	/** rpc - The RPC endpoint. Default 8400.*/
	Integer portaRPC = 8400
	/** serf_lan - The Serf LAN port. Default 8301.*/
	Integer portaSerfLAN = 8301
	/** serf_wan - The Serf WAN port. Default 8302.*/
	Integer portaSerfWAN = 8302
	/** server - Server RPC address. Default 8300.*/
	Integer portaServer = 8300
	/** indirizzo presentazione wan per protocollo gossip */
	String advertiseWan = null
	/** avviare Consul in modalità bootstrap? */
	Boolean avvioInBootStrap = false
	/** encript key per consul */
	String chiave = null
	/** nodi Consul su cui tentare la connessione durante il bootstrap
	 * verrà usato il comando "-retry-join"
	 */
	List<String> listaJoin =[]
	/** lista nodi per il join wan in bootstrap */
	List<String> listaJoinWan =[]
	/** l'agente Consul è in modalità server */
	Boolean server = true
	/** dominio per dns */
	String dominio = 'ar4k.net.'
	/** stringa datacenter */
	String datacenter = 'ar4k-test'
}