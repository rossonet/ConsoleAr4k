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
	List<LavorazioniSSH> lavorazioni = []
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
			javaVersion:javaVersion,
			proxy:proxy,
			nodo:nodo?.idNodo,
			lavorazioni:lavorazioni*.idLavorazione,
			ricettari:ricettari*.idRicettario
		]
	}

	/** avvia agente consul in modalità client */
	LavorazioniSSH avviaConsulClient(IstanzaConsul consul,Boolean reset=false) {
		return false
	}

	/** avvia agente consul in modalità client */
	LavorazioniSSH avviaConsulServer(IstanzaConsul consul,Boolean reset=false) {
		return false
	}

	/** avvia interfaccia ar4k */
	LavorazioniSSH avviaInterfacciaAr4k(Boolean reset=false) {
		return false
	}

	/** avvia interfaccia web ssh in node */
	LavorazioniSSH avviaInterfacciaSSHWeb(Boolean reset=false) {
		return false
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
		return false
	}










































	/** host vaso */
	String macchina = null
	/** porta vaso */
	Integer porta = 22
	/** utente vaso */
	String utente = 'root'
	/** chiave privata ssh */
	String key = null
	/** proxy per connettere il vaso dall'interfaccia */
	String proxyAccesso = null


	/** versione java -se installato- rilevata con #java -version*/
	String javaVersion = null
	/** funziona da gw ssh per altri vasi? */
	Boolean gwRete = false


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

	/** verifica la raggiungibilità di internet dal vaso */
	Boolean verificaConnettivita() {
		log.debug("Verifico se "+etichetta+" raggiunge l'esterno.")
		String comando = 'wget '+indirizzoTest+' -O - 2>/dev/null | grep hera | wc -l'
		String atteso = '1\n'
		String risultato = esegui(comando)
		log.debug("risultato "+comando+" = "+risultato+" (atteso: "+atteso+')')
		return risultato == atteso?true:false
	}

	/** salva un contesto sul vaso
	 * */
	Boolean salvaContesto(Contesto contesto) {
		JSON file = contesto.esporta() as JSON
		String interruzione = "EOF-"+UUID.randomUUID()+"-AR4K"
		String fileElaborato = java.util.regex.Matcher.quoteReplacement(file.toString(true))
		String comandoEsecuzione="cat > ~/.ar4k/contesti/"+contesto.idContesto+".json << "+interruzione+"\n"+fileElaborato+"\n"+interruzione+"\n"
		String comandoControllo="cat ~/.ar4k/contesti/"+contesto.idContesto+".json"
		log.debug("salva contesto su vaso con comando: "+comandoEsecuzione)
		esegui(comandoEsecuzione)
		String risultato=esegui(comandoControllo)
		try {
			Holders.applicationContext.getBean("interfacciaContestoService").sendMessage("activemq:topic:interfaccia.eventi",([tipo:'CONTESTOSALVATO',messaggio:"Contesto salvato su "+etichetta+" con il nome "+contesto.idContesto+".json"] as JSON).toString())
		} catch (Exception ee){
			log.warn "Errore nella comunicazione di un evento ad ActiveMQ sul vaso master: "+ee.toString()+" (in fase di bootstrap è normale)"
		}
		return risultato == file.toString(true)+"\n"?true:false
	}

	/** esegui un comando ssh sul vaso */
	String esegui(String comando) {
		log.debug("connessione al vaso: "+etichetta)
		String risultato = ''
		comando = 'export PATH='+path+' ; '+comando
		InputStream input = null
		try {
			JSch jsch = new JSch()
			Channel canale
			// Aggiunge la chiave privata
			jsch.addIdentity(utente,key.getBytes(),null,null)
			Session sessione=jsch.getSession(utente, macchina, porta)
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
			}catch(Exception ee){}
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
							log.debug("Comando: "+comando+" [stato:"+canale.getExitStatus()+"]")
						break
					}
					try{Thread.sleep(500);}catch(Exception ee){}
				}
			}
			canale.disconnect()
			sessione.disconnect()
		}catch(Exception e) {
			log.warn("Errore connesione SSH nell'esecuzione del comando: "+comando+" ERRORE: "+e.toString())
		}
		return risultato
	}

	/** legge un target sul vaso con il comando cat e ritorna l'output */
	String leggiConCat(String target){
		return esegui('cat '+target)
	}

	/** carica un file .bar dal vaso all'interfaccia 
	 * @deprecated
	 * */
	String leggiBinarioProcesso(String percorso,String target) {
		String ritorno = '/tmp/'+UUID.randomUUID()+'.bar'
		FileOutputStream ritornoStream = new FileOutputStream(new File(ritorno))
		log.debug("leggo: "+target+" in "+percorso)
		try {
			JSch jsch = new JSch()
			Channel canale
			ChannelSftp channelSftp
			// Aggiunge la chiave privata
			jsch.addIdentity(utente,key.getBytes(),null,null)
			Session sessione=jsch.getSession(utente, macchina, porta)
			sessione.setConfig("StrictHostKeyChecking","no")
			sessione.setConfig("UserKnownHostsFile","/dev/null")
			sessione.connect()
			canale = sessione.openChannel("sftp")
			canale.connect()
			channelSftp = (ChannelSftp)canale
			channelSftp.cd(percorso)
			log.debug("directory sftp: "+channelSftp.pwd())
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
		return ritorno
	}

	/** prova la connesione ssh al vaso */
	Boolean provaConnessione() {
		log.debug("Provo connessione a "+etichetta)
		String comando = 'echo -n $((6+4))'
		String atteso = '10'
		String risultato = esegui(comando)
		log.debug("risultato "+comando+" = "+risultato+" (atteso: "+atteso+')')
		return risultato == atteso?true:false
	}

	/** prova la funzionalità del vaso come nodo ar4k ed eventualmente configura il filesystem */
	Boolean provaVaso() {
		String creaDirectory = """
		cd ~
		mkdir -p .ar4k
		mkdir -p .ar4k/contesti
		mkdir -p .ar4k/ricettari
		mkdir -p .ar4k/dati
		mkdir -p .ar4k/dati/consul
		mkdir -p .ar4k/dati/activemq/data
		mkdir -p .ar4k/dati/activemq/tmp
		"""
		String battezza = "echo '"+idVaso+"'>>~/.ar4k/dati/prove.log"
		String controllo = """
		if [ -e ~/.ar4k/dati/prove.log ]
		then
		echo -n 42
		fi
		"""
		String atteso = '42'

		esegui(creaDirectory)
		esegui(battezza)

		String risultato=esegui(controllo)
		log.debug("risultato "+controllo+" = "+risultato+" (atteso: "+atteso+')')
		return risultato == atteso?true:false
	}

	/** Avvia il demone consul sul vaso e configura una sessione ssh permanente per accedere tramite le API JAVA al demone Consul creato */
	Boolean avviaConsul(JSch connessione) {
		String consulKey = Holders.applicationContext.getBean("interfacciaContestoService").contesto.consulKey
		String dominioConsul = Holders.applicationContext.getBean("interfacciaContestoService").contesto.dominioConsul
		String datacenterConsul = Holders.applicationContext.getBean("interfacciaContestoService").contesto.datacenterConsul
		String comando = '~/.ar4k/ricettari/ar4k_open/i386/consul_i386 agent -data-dir ~/.ar4k/dati/consul -bootstrap -server -dc '+datacenterConsul
		comando += consulKey?' -encrypt "'+consulKey+'"':''
		comando += dominioConsul?' -domain '+dominioConsul:''
		comando += ' </dev/null &>/dev/null &'
		//String comando = '~/.ar4k/ricettari/ar4k_open/i386/consul_i386 agent -data-dir ~/.ar4k/dati -bootstrap -server -dc ar4kPrivate </dev/null &>/dev/null &'
		String verifica = "~/.ar4k/ricettari/ar4k_open/i386/consul_i386 info | grep 'revision = 9a9cc934' | wc -l"
		String comandoEventiNodi = '~/.ar4k/ricettari/ar4k_open/i386/consul_i386 watch -type nodes ~/.ar4k/ricettari/ar4k_open/bin/eventoConsul.sh "nodi aggiornati"</dev/null &>/dev/null &'
		String comandoEventiService = '~/.ar4k/ricettari/ar4k_open/i386/consul_i386 watch -type services ~/.ar4k/ricettari/ar4k_open/bin/eventoConsul.sh "servizi aggiornati"</dev/null &>/dev/null &'
		String comandoEventiChecks = '~/.ar4k/ricettari/ar4k_open/i386/consul_i386 watch -type checks ~/.ar4k/ricettari/ar4k_open/bin/eventoConsul.sh "checks aggiornati"</dev/null &>/dev/null &'
		if ( esegui(verifica) != '1\n') {
			esegui('killall consul_i386')
			esegui(comando)
			esegui('sleep 6')
			esegui(comandoEventiNodi)
			esegui(comandoEventiService)
			esegui(comandoEventiChecks)
		}
		addLTunnel(connessione,Holders.applicationContext.getBean("interfacciaContestoService").interfaccia.portaConsul,'127.0.0.1',8500)
		return esegui(verifica) == '1\n'?true:false
	}

	/** Avvia consul in modalità client */
	Boolean avviaConsulClient(String serverJoin) {
		String consulKey = Holders.applicationContext.getBean("interfacciaContestoService").contesto.consulKey
		String dominioConsul = Holders.applicationContext.getBean("interfacciaContestoService").contesto.dominioConsul
		String datacenterConsul = Holders.applicationContext.getBean("interfacciaContestoService").contesto.datacenterConsul
		String comando = '~/.ar4k/ricettari/ar4k_open/i386/consul_i386 agent -data-dir ~/.ar4k/dati/consul -dc '+datacenterConsul+' -join '+serverJoin
		comando += consulKey?' -encrypt "'+consulKey+'"':''
		comando += dominioConsul?' -domain '+dominioConsul:''
		comando += ' </dev/null &>/dev/null &'
		//String comando = '~/.ar4k/ricettari/ar4k_open/i386/consul_i386 agent -data-dir ~/.ar4k/dati -bootstrap -server -dc ar4kPrivate </dev/null &>/dev/null &'
		String verifica = "~/.ar4k/ricettari/ar4k_open/i386/consul_i386 info | grep 'revision = 9a9cc934' | wc -l"
		if ( esegui(verifica) != '1\n') {
			esegui('killall consul_i386')
			esegui(comando)
		}
		//addLTunnel(connessione,porta,'127.0.0.1',8500)
		return esegui(verifica) == '1\n'?true:false
	}

	/** Avvia broker ActiveMQ sul nodo Master, configura una sessione ssh permanente per connettere l'Apache Camel integrato nell'Interfaccia  */
	Boolean avviaActiveMQ(JSch connessione) {
		String comando = 'export ACTIVEMQ_DATA=".ar4k/dati/activemq/data"; export ACTIVEMQ_TMP=".ar4k/dati/activemq/tmp" ; ~/.ar4k/ricettari/ar4k_open/apache-activemq-5.12.0/bin/activemq start'
		String verifica = "~/.ar4k/ricettari/ar4k_open/apache-activemq-5.12.0/bin/activemq status | grep 'ActiveMQ is running' | wc -l"
		if ( esegui(verifica) != '1\n') {
			esegui(comando)
		}
		addLTunnel(connessione,Holders.applicationContext.getBean("interfacciaContestoService").interfaccia.portaActiveMQ,'127.0.0.1',61616)
		return esegui(verifica) == '1\n'?true:false
	}

	/** Aggiunge un tunnel SSH Left*/
	void addLTunnel(JSch connessione, int lport, String rhost, int rport) {
		try {
			Channel canale
			// Aggiunge la chiave privata
			connessione.addIdentity(utente,key.getBytes(),null,null)
			Session sessione=connessione.getSession(utente, macchina, porta)
			sessione.setConfig("StrictHostKeyChecking","no")
			sessione.setConfig("UserKnownHostsFile","/dev/null")
			sessione.connect()
			try {sessione.setPortForwardingL(lport, rhost, rport)} catch(Exception e){log.warn("Errore nel tunnel ssh per il vaso "+etichetta+" tunnel "+lport+":"+rhost+":"+rport+". Probabilmente la porta è già in uso")}
		}	catch(JSchException e){
			log.warn("Errore nella creazione del tunnel SSH: "+e.printStackTrace())
		}
	}

	/** recupera i contesti salvati sul vaso
	 *  @deprecated
	 *  */
	List<Contesto> listaContesti() {
		List<Contesto> contesti = []
		String comandoRicerca = "cd ~/.ar4k/contesti ;"
		comandoRicerca += "find . -name '*.json'"
		def ricerca = esegui(comandoRicerca).tokenize('\n')
		ricerca.each{
			JsonSlurper jsonSlurper = new JsonSlurper()
			String fileJson = esegui("cd ~/.ar4k/contesti/; cat "+it)
			log.debug("lettura "+it+" \n"+fileJson+"\n")
			if (!fileJson) fileJson = '{"etichetta":"vuoto","descrizione":"nessun contesto in '+it+'"}'
			try {
				Contesto contestoOggetto = new Contesto().importa(jsonSlurper.parseText(fileJson) as Map)
				log.debug("Trovato contesto: "+contestoOggetto.esporta())
				contesti.add(contestoOggetto)
			} catch(Exception e) {
				log.warn(e.printStackTrace())
			}

		}
		log.debug("Ho trovato "+contesti.size()+" contesti")
		return contesti
	}

	/** stringa predefinita */
	String toString() {
		return etichetta+" => "+utente+"@"+macchina+":"+porta
	}

	/** scarica un ricettario sul nodo se attivo */
	Boolean avviaRicettario(Ricettario ricettario) {
		if (ricettario.repositoryGit.configurato == true) {
			String comando = "cd ~/.ar4k/ricettari ; if [ -e "+ricettario.repositoryGit.nomeCartella+" ] ; then cd "+ricettario.repositoryGit.nomeCartella+"; git pull; else  git clone "+ricettario.repositoryGit.indirizzo+" "+ricettario.repositoryGit.nomeCartella+"; fi"
			esegui(comando)
		}
		String comandoCont = "cd ~/.ar4k/ricettari ; if [ -e "+ricettario.repositoryGit.nomeCartella+"/LICENSE ] ; then echo -n 43; fi"
		String atteso = '43'
		String risultato = esegui(comandoCont)
		log.debug("risultato "+comandoCont+" = "+risultato+" (atteso: "+atteso+')')
		Boolean esito = (risultato == atteso?true:false)
		if (esito) ricettario.aggiornato = new Date().format("yyyyMMddHHmmss", TimeZone.getTimeZone("UTC")).toString()
		if (esito) log.info("Esito aggiornamento ricettario "+ricettario+" su "+etichetta+": (ok)")
		try {
			Holders.applicationContext.getBean("interfacciaContestoService").sendMessage("activemq:topic:interfaccia.eventi",([tipo:'SCARICORICETTARIO',messaggio:ricettario.etichetta+" ("+descrizione+")"] as JSON).toString())
		} catch (Exception ee){
			log.warn "Evento avvia ricettario non comunicato ad ActiveMQ: "+ee.toString()
		}
		return esito
	}

	/** legge i semi e popola i dati dal file JSON */
	Boolean caricaSemi(Ricettario ricettario) {
		if (ricettario.repositoryGit.configurato == true) {
			String comandoRicerca = "cd ~/.ar4k/ricettari ;"
			comandoRicerca += "cd "+ricettario.repositoryGit.nomeCartella+" ;"
			comandoRicerca += "find . -name '*.ar4k'"
			ricettario.semi = []
			def ricerca = esegui(comandoRicerca).tokenize('\n')
			ricerca.each{
				def jsonSlurper = new JsonSlurper()
				String fileJson = esegui("cd ~/.ar4k/ricettari/"+ricettario.repositoryGit.nomeCartella+" ; cat "+it)
				log.debug("lettura "+it+" \n"+fileJson+"\n")
				if (!fileJson) fileJson = '{"etichetta":"vuoto","descrizione":"nessun seme configurato in '+it+'"}'
				try {
					def semeTarget = jsonSlurper.parseText(fileJson)
					Seme semeOggetto = new Seme(
							percorso:it,
							meme:new Meme(
							etichetta:semeTarget.etichetta,
							descrizione:semeTarget.descrizione,
							autore:semeTarget.autore,
							gestore:semeTarget.gestore,
							versione:semeTarget.versione,
							versioniCompatibili:semeTarget.versioniCompatibili,
							autoStart:semeTarget.autoStart,
							disinstallazione:semeTarget.disinstallazione,
							caricaInBootstrap:semeTarget.caricaInBootstrap?:false,
							icona:semeTarget.icona,
							stato:semeTarget.stato,
							dipendenze:semeTarget.dipendenze,
							funzionalita:semeTarget.funzionalita
							))
					semeTarget.metodi.each{semeOggetto.meme.metodi.add(new Metodo(it))}
					semeTarget.stati.each{semeOggetto.meme.stati.add(new StatoMeme(it))}
					ricettario.semi.add(semeOggetto)
					log.debug("Aggiungo "+semeTarget.etichetta)

				} catch(Exception e) {
					log.warn("Errore nel caricamento del ricettario: "+e.printStackTrace())
				}

			}
			log.debug("Nel ricettario "+ricettario+" sono presenti "+ricettario.semi.size()+" semi")
			try {
				Holders.applicationContext.getBean("interfacciaContestoService").sendMessage("activemq:topic:interfaccia.eventi",([tipo:'CARICASEMI',messaggio:ricettario.semi.size().toString()] as JSON).toString())
			} catch (Exception ee){
				log.warn "Evento caricamento ricettario non comunicabile ad ActiveMQ: "+ee.toString()
			}
			return true
		} else {
			return false
		}
	}
}





