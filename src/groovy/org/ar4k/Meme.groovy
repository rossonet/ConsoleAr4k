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
			celle:celle*.esporta()
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
	
}