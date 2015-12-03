/**
 * Meme (istanza del Seme)
 * 
 * <p>Meme contiene i gruppo funzionale di processi Activiti</p>
 * 
 * <p style="text-justify">
 * Un meme è un insieme di definizioni di processo Activiti le cui funzionalità sono tra di loro correlate.
 * Il meme rende disponibile al contesto funzionalità e dipende da quelle messe a disposizione da altri memi nel contesto.
 * </p>
 * 
 * @author Andrea Ambrosini (Rossonet s.c.a r.l)
 * @version 0.2-alpha
 * @see org.ar4k.Ricettario
 * @see org.ar4k.Vaso
 */
package org.ar4k


import java.util.Map;

import grails.converters.JSON
import grails.util.Holders
import groovy.transform.AutoClone

@AutoClone
class Meme {
	/** id univoco meme */
	String idMeme = UUID.randomUUID()
	/** etichetta meme */
	String etichetta = 'Meme generato atomaticamente'
	/** descrizione meme */
	String descrizione ='Meme AR4K by Rossonet'
	/** autore meme */
	String autore = 'Andrea Ambrosini'
	/** gestore meme */
	String gestore = 'Rossonet s.c.a r.l.'
	/** versione meme */
	String versione = '0.2'
	/** lista versioni compatibili -per aggiornamento- */
	List<String> versioniCompatibili = []
	/** icona generale meme */
	String icona = icona
	/** stato del meme */
	/** possibili stati censiti:
	 * 
	 *  nuovo: meme appena istanziato
	 *  setup: l'applicazione è in fase di setup (scarico repository, compilazioni ecc...)
	 *  installazione: il meme è in fase di installazione sul vaso/sui vasi
	 *  bloccato: il meme è in fase di blocco (per le procedure di disaster recovery. Importante il dump dei db in questa fase! )
	 *  avviata: in funzione
	 *  fermato: il meme è disattivato, ma configurato sui vasi
	 *  
	 */
	String stato = 'nuovo'
	/** attivare alla creazione del meme? */
	Boolean autoStart = false
	/** lista funzionalità di cui è necessaria la presenza nel contesto per operare */
	List<String> dipendenze = []
	/** lista funzionalità rese disponibili al contesto */
	List<String> funzionalita = []
	/** vasi utilizzati e loro funzione per il meme */
	List<Cella> celle = []
	/** servizio maschera principale del Meme - 
	 * se non null, il meme sarà integrato nella console
	 * puntando ai contenuti erogati dal servizio
	 */
	Servizio mascheraGestione = null
	/** maschera da iframe -  
	 * se vero il servizio mascheraGestione viene incluso nelle
	 * pagine della console come IFrame
	*/
	Boolean mascheraInIframe = true
	/** maschera in div -
	 * se non null e con mascheraInIframe false
	 * il contenuto viene parsato includendo in
	 * particolare i dati sul servizio, l'utente 
	 * e altre variabili utili.
	 * il caso d'uso previsto è l'istanziazione di uno
	 * specifico controller AngularJS e ralativi contenuti.
	 * il dato è espresso come JSON ed importato
	 * direttamente dal seme
	 */
	String mascheraInDiv = null
	/** chiavi ssh privata del meme per accedere ai vasi */
	String chiavePrivata = []

	/** dump dati del meme */
	def esporta() {
		log.info("esporta() il meme: "+idMeme)
		return [
			idMeme:idMeme,
			etichetta:etichetta,
			descrizione:descrizione,
			autore:autore,
			gestore:gestore,
			versione:versione,
			versioniCompatibili:versioniCompatibili,
			icona:icona,
			stato:stato,
			autoStart:autoStart,
			dipendenze:dipendenze,
			funzionalita:funzionalita,
			celle:celle*.esporta(),
			mascheraInDiv:mascheraInDiv,
			mascheraInIframe:mascheraInIframe,
			mascheraGestione:mascheraGestione
		]
	}

	/** salva in uno Stato specifico a catena per N profondità */
	Boolean salva(Stato stato,Integer contatore) {
		stato.salvaValore(idMeme,'org-ar4k-meme',(esporta() as JSON).toString())
		if (contatore > 0) {
			contatore = contatore -1
			// nessuna eredità
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
}

/***
 * una cella è un processo in esecuzione su un vaso per conto di un meme
 * 
 * 
 * @author andrea
 *
 */
class Cella {
	/** id univoco cella */
	String idCella = UUID.randomUUID()
	/** tipo oggetto */
	String tipoOggetto = 'org-ar4k-cella'
	/** comandi gestiti dalla cella */
	List<LavorazioneSSH> lavorazioni = []
	/** lista ricettari necessari */
	List<Ricettario> ricettari = []
	/** servizi erogati */
	List<Servizio> serviziErogati = []
	/** dipendente da servizi */
	List<Servizio> dipendenzeServizi = []
	/** string tag cella */
	List<String> etichette = []

	/** ciclo di vita su lavorazione? il ciclo della vita della cella è gestito con il ciclo di vita di una lavorazione?
	 *  se si usa la lavorazione con refInterna impostato a 'controllo' ai cambi di stato viene eseguita passando il parametro
	 *  dello stato seguito dai parametri aggiuntivi. - valutare in fase di running come comportarsi senza 'controllo'
	 *
	 *  */
	Boolean gestitoConInitScript = false
	/** se vero, tra le lavorazioni devono essere presenti quelle con  refInterna per le seguenti operazioni: 
	 * installa,
	 * prova (ritorna se il meme è funzionante implementando meccanismi di auto-diagnosi)
	 * avvia,
	 * ferma,
	 * clona (utile per la gestione dei cicli di vita del software, l'esportazione degli ambienti di sviluppo ecc..),
	 * backup (salva su uno storage il contenuto del meme),
	 * restore (ripristina il contenuto di un meme in un'altro ed avvia il meme),
	 * creaSeme (da verificare. La possibilità di creare un seme dal meme),
	 * metodo (chiamata generica per eleborare un'aggiornamento delle variabili d'ambiente)
	 * 
	 * verrà creata un'entry in KV consul ../<oggetto cella>/controllo e ../<oggetto cella>/stato
	 * se viene modificata la prima genera una callback allo script 'controllo'
	 * l'ultimo output dello script viene salvato in ../stato
	 *
	 */
	Boolean gestitoConConsul = false
	/** utilizza la lavorazione 'controllo' con una script che attende comandi rest su una specifica porta
	 *  vedere l'esempio in documentazione testRest.sh 
	 *  il servizio verrà pubblicato su Consul
	 *  */
	Integer portaControlloScriptRest = null
	/** token autorizzati per la gestione delle comunicazioni rest dirette */
	List<String> chiaviControlloScript = []

	/** vaso della cella */
	Vaso vaso = null

	/** dump dati del meme */
	def esporta() {
		return [
			idCella:idCella,
			tipoOggetto:tipoOggetto,
			lavorazioni:lavorazioni*.esporta(),
			gestitoConInitScript:gestitoConInitScript,
			gestitoConConsul:gestitoConConsul,
			chiaviControlloScript:chiaviControlloScript*.toString(),
			portaControlloScriptRest:portaControlloScriptRest,
			vaso:vaso?.idVaso
		]
	}
}

/**
 * Classe store per il meme, rappresenta una directory nel ricettario.
 *
 * in particolare mette a disposizione del sistema i metadati per la gestione
 * dei memi istanziabili.
 *
 * @author Andrea Ambrosini (Rossonet s.c.a r.l)
 *
 */
class Seme implements Serializable {
	/** id univoco */
	String idSeme = UUID.randomUUID()

	/** esporta il seme */
	def esporta() {
		log.info("esporta() il seme : "+idSeme)
		return [
			seme:idSeme
		]
	}

	/** salva in uno Stato specifico a catena per N profondità */
	Boolean salva(Stato stato,Integer contatore) {
		stato.salvaValore(idSeme,'org-ar4k-seme',(esporta() as JSON).toString())
		if (contatore > 0) {
			contatore = contatore -1
			//
		}
	}
	/** salva in uno Stato specifico solo l'oggetto */
	Boolean salva(Stato stato) {
		salva(stato,0)
	}
	/** salva nello stato di defualt solo l'oggetto */
	Boolean salva() {
		salva(Holders.applicationContext.getBean("interfacciaContestoService").stato,0)
	}
	/** salva nello stato di defualt a catena per N profondità */
	Boolean salva(Integer contatore) {
		salva(Holders.applicationContext.getBean("interfacciaContestoService").stato,contatore)
	}

	Seme importa(Map json){
		log.info("importa() il seme: "+json.meme.idMeme)
		Seme semeCreato = new Seme(
				percorso:json.percorso,
				meme:new Meme().importa(json.meme)
				)
		return semeCreato
	}
}

/** servizio registrato in Consul */
class Servizio {
	/** id univoco */
	String idServizio = UUID.randomUUID()

	/** esporta il seme */
	def esporta() {
		log.info("esporta() il seme : "+idSeme)
		return [
			servizio:idServizio
		]
	}

	/** salva in uno Stato specifico a catena per N profondità */
	Boolean salva(Stato stato,Integer contatore) {
		stato.salvaValore(idServizio,'org-ar4k-servizio',(esporta() as JSON).toString())
		if (contatore > 0) {
			contatore = contatore -1
			//
		}
	}
	/** salva in uno Stato specifico solo l'oggetto */
	Boolean salva(Stato stato) {
		salva(stato,0)
	}
	/** salva nello stato di defualt solo l'oggetto */
	Boolean salva() {
		salva(Holders.applicationContext.getBean("interfacciaContestoService").stato,0)
	}
	/** salva nello stato di defualt a catena per N profondità */
	Boolean salva(Integer contatore) {
		salva(Holders.applicationContext.getBean("interfacciaContestoService").stato,contatore)
	}
}