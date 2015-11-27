package org.ar4k

import grails.converters.JSON

import java.util.List;

import javax.validation.OverridesAttribute;

import org.codehaus.groovy.grails.commons.GrailsApplication;

import com.ecwid.consul.v1.ConsulClient
import com.jcraft.jsch.*
import com.subgraph.orchid.TorClient
import com.subgraph.orchid.TorInitializationListener


/**
 *  Rappresentazione connessione ssh
 *  con gestione proxy
 * 
 * 
 * @author andrea
 *
 */




import grails.util.Holders

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

	/** salva in uno Stato specifico a catena per N profondità */
	Boolean salva(Stato stato,Integer contatore) {
		stato.salvaValore(idConnessioneSSH,'org-ar4k-connessioneSSH',(esporta() as JSON).toString())
		if (contatore > 0) {
			contatore = contatore -1
			// nessun oggetto a catena
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

	/** esporta la connessione ssh */
	def esporta() {
		return [
			idConnessioneSSH:idConnessioneSSH,
			macchina:macchina,
			porta:porta,
			utente:utente,
			key:key,
			proxyMaster:proxyMaster
		]
	}

	/** attiva i tunnel */
	Boolean verificaAvvia(Integer gwPort,String targetHost,Integer targetPort,Integer callBackPort) {
		if (!connessione) {
			connessione = new JSch()
		}
		try {
			addLTunnel(gwPort,targetHost,targetPort)
			addRTunnel(callBackPort,'127.0.0.1',6630)
			return true
		} catch (Exception ee) {
			log.warn("Errore avvio tunnel: "+ee.printStackTrace())
			return false
		}
	}

	/** attiva i tunnel con porta di callback predefinita  */
	Boolean verificaAvvia(Integer gwPort,String targetHost,Integer targetPort) {
		return verificaAvvia(gwPort,targetHost,targetPort,3630)
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

	/** Aggiunge un tunnel SSH Left */
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
			try {sessione.setPortForwardingL(lport, rhost, rport)} catch(Exception e){log.warn("Errore nel tunnel ssh per nodo "+macchina+" tunnel left "+lport+":"+rhost+":"+rport+". Probabilmente la porta è già in uso")}
		}	catch(JSchException e){
			log.warn("Errore nella creazione del tunnel left SSH: "+e.printStackTrace())
		}
	}

	/** Aggiunge un tunnel SSH Right */
	private void addRTunnel(int lport, String lhost, int rport) {
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
			try {sessione.setPortForwardingR(lport, lhost, rport)} catch(Exception e){log.warn("Errore nel tunnel ssh per nodo "+macchina+" tunnel right "+lport+":"+lhost+":"+rport+". Probabilmente la porta è già in uso")}
		}	catch(JSchException e){
			log.warn("Errore nella creazione del tunnel right SSH: "+e.printStackTrace())
		}
	}

	/** salva un file testuale su fs */
	Boolean salvaFileTestuale(String percorsoAssoluto, String file) {
		String interruzione = "EOF-"+UUID.randomUUID()+"-AR4K"
		String fileElaborato = java.util.regex.Matcher.quoteReplacement(file.toString(true))
		String comandoEsecuzione="cat > "+file+" << "+interruzione+"\n"+fileElaborato+"\n"+interruzione+"\n"
		String comandoControllo="cat "+file
		esegui(comandoEsecuzione)
		String risultato=esegui(comandoControllo)
		return risultato == file.toString(true)+"\n"?true:false
	}

	/** legge un target sul vaso con il comando cat e ritorna l'output */
	String leggiConCat(String target){
		return esegui('cat '+target)
	}

	/** crea la struttura di directory per ar4k */
	Boolean creaStruttura() {
		String creaDirectory = """
		cd ~
		mkdir -p .ar4k
		mkdir -p .ar4k/contesti
		mkdir -p .ar4k/ricettari
		mkdir -p .ar4k/dati
		mkdir -p .ar4k/dati/consul
		mkdir -p .ar4k/dati/memi
		mkdir -p .ar4k/dati/backup
		mkdir -p .ar4k/esecuzioni
		"""

		String risultato=esegui(creaDirectory)
		return risultato?true:false
	}
	
	/** crea la struttura di directory per ar4k */
	Boolean killConsul() {
		String kill = """
		killall consul
		killall consul_i386
		killall consul_amd64
		"""

		String risultato=esegui(kill)
		return risultato?true:false
	}

	/** carica un file binario da ssh
	 *
	 * */
	OutputStream leggiBinario(String percorso,String target,OutputStream ritornoStream) {
		//comando = 'export PATH='+path+' ; '+comando
		InputStream input = null
		Integer portaProxyT = null
		String macchinaProxyT = null
		try {
			Channel canale
			ChannelSftp channelSftp
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
			sessione.connect()
			canale = sessione.openChannel("sftp")
			canale.connect()
			channelSftp = (ChannelSftp)canale
			channelSftp.cd(percorso)
			InputStream risultato = channelSftp.get(target)
			int c
			byte[] buffer=new byte[1024]
			while ((c=risultato.read(buffer))!=-1 ) {
				ritornoStream.write(buffer,0,c)
			}
			risultato.close()
			ritornoStream.close()
			canale.disconnect()
			sessione.disconnect()
		}catch(JSchException e) {
			log.warn("Errore connesione SSH nella lettura del file .bar: "+e.printStackTrace())
		}
		return ritornoStream
	}
}


/**
 *  job SSH asincroni con gestione dello stato e mappatura del ciclo
 *  di vita tramite Consule K/V
 * 
 * @author andrea
 *
 */
class LavorazioniSSH {
	String tipoOggetto = 'org-ar4k-lavorazioneSSH'

	/** id univoco lavorazione */
	String idLavorazione = new Date().format("yyyyMMddHHmmss", TimeZone.getTimeZone("UTC")).toString()+'_'+UUID.randomUUID()
	/** etichetta */
	String etichetta = null
	/** descrizione */
	String descrizione = null
	/** stato lavorazione **/
	String stato = 'init'
	/** repository necessari sul nodo, verrano aggiornati prima dell'esecuzione */
	List<Ricettario> ricettariNecessari = []
	
	/** salva in uno Stato specifico a catena per N profondità */
	Boolean salva(Stato stato,Integer contatore) {
		stato.salvaValore(idLavorazione,tipoOggetto,(esporta() as JSON).toString())
		if (contatore > 0) {
			contatore = contatore -1
			ricettariNecessari*.salva(stato,contatore)
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

	/** esporta la connessione ssh */
	def esporta() {
		return [
			idLavorazione:idLavorazione,
			etichetta:etichetta,
			descrizione:descrizione
		]
	}
}

/** job di scansione
 * 
 * @author andrea
 *
 */
class Scansione extends LavorazioniSSH {
	String tipoOggetto = 'org-ar4k-Scansione'

}

/** servizio permanente in userspace supportato con cron di scansione
 *
 * @author andrea
 *
 */
class Servizio extends LavorazioniSSH {
	String tipoOggetto = 'Servizio'
}

/** servizio permanente in userspace supportato con supporto env consul
 *
 * @author andrea
 *
 */
class EnvConsul extends LavorazioniSSH {
	String tipoOggetto = 'EnvConsulSSH'
}