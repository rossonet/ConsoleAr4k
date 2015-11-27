/**
 * Ricettario
 *
 * <p>Un ricettario corrisponde ad un archivio git accessibile da un nodo e contiene semi utilizzabili nel vaso per la creazione dei memi.</p>
 *
 * <p style="text-justify">
 * In un vaso sono presenti più ricettari. Un vaso può utilizzare i ricettari compatibili con le funzionalità di cui dispone (base e aggiunte con memi).</br>
 * Un ricettario espone tutti i metodi per operare con il repository git di riferimento.</br>
 * </p>
 *
 * @author Andrea Ambrosini (Rossonet s.c.a r.l)
 * @version 0.1-alpha
 * @see org.ar4k.Meme
 * @see org.ar4k.Vaso
 * @see org.ar4k.Contesto
 */

package org.ar4k

import grails.converters.JSON
import grails.util.Holders

class Ricettario {
	/** id univoco ricettario */
	String idRicettario = UUID.randomUUID()
	/** etichetta ricettario */
	String etichetta = 'Ar4k base'
	/** descrizione ricettario */
	String descrizione ='Ricettario (git) AR4K by Rossonet'
	/** repository git */
	RepositoryGit repositoryGit = new RepositoryGit()

	/** esporta il ricettario */
	def esporta() {
		log.info("esporta() il ricettario: "+idRicettario)
		return [
			idRicettario:idRicettario,
			etichetta:etichetta,
			descrizione:descrizione,
			repositoryGit:repositoryGit?.idRepository
		]
	}
	
	/** salva in uno Stato specifico a catena per N profondità */
	Boolean salva(Stato stato,Integer contatore) {
		stato.salvaValore(idRicettario,'org-ar4k-ricettario',(esporta() as JSON).toString())
		if (contatore > 0) {
			contatore = contatore -1
			repositoryGit?.salva(stato,contatore)
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

	Ricettario importa(Map json){
		log.info("importa() ricettario: "+json.idRicettario)
		Ricettario ricettarioCreato = new Ricettario(
				idRicettario:json.idRicettario,
				etichetta:json.etichetta,
				descrizione:json.descrizione,
				repositoryGit:new RepositoryGit(json.repositoryGit),
				aggiornato: json.aggiornato,
				clonaOvunque:json.clonaOvunque,
				semi:semi*.esporta(),
				caricaInBootstrap:json.caricaInBootstrap
				)
		json.semi.each{ricettarioCreato.semi.add(new Seme().importa(it))}
		return ricettarioCreato
	}
}

/**
 * RepositoryGit
 *
 * Struttura dati per lista repository GIT
 *
 * @author Andrea Ambrosini (Rossonet s.c.a r.l)
 *
 */
class RepositoryGit {
	/** id univoco */
	String idRepository = UUID.randomUUID()
	/** utente, se null accesso anonimo */
	String utente = null
	/** password utente */
	String password = null
	/** URL Repository */
	String indirizzo = null
	/** directory ricettario in vaso */
	String nomeCartella = null
	/** stato */
	Boolean configurato = true

	def esporta() {
		log.info("esporta: "+indirizzo )
		return [
			idRepository:idRepository,
			utente:utente,
			password:password,
			indirizzo:indirizzo,
			nomeCartella:nomeCartella,
			configurato:configurato
		]
	}
	
	/** salva in uno Stato specifico a catena per N profondità */
	Boolean salva(Stato stato,Integer contatore) {
		stato.salvaValore(idRepository,'org-ar4k-repository',(esporta() as JSON).toString())
		if (contatore > 0) {
			contatore = contatore -1
			// nessuna dipendenza
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

/**
 * Classe store per il meme, rappresenta una directory nel ricettario.
 *
 * in particolare mette a disposizione del sistema i metadati per la gestione
 * dei memi istanziabili.
 *
 * @author Andrea Ambrosini (Rossonet s.c.a r.l)
 *
 */
class Seme {
	/** id univoco */
	String idSeme = UUID.randomUUID()
	/** dati meme germinante*/
	Meme meme
	String percorso

	/** esporta il seme */
	def esporta() {
		log.info("esporta() il seme in : "+percorso)
		return [
			meme:meme?.idMeme,
			percorso:percorso
		]
	}

	/** salva in uno Stato specifico a catena per N profondità */
	Boolean salva(Stato stato,Integer contatore) {
		stato.salvaValore(idSeme,'org-ar4k-seme',(esporta() as JSON).toString())
		if (contatore > 0) {
			contatore = contatore -1
			meme?.salva(stato,contatore)
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