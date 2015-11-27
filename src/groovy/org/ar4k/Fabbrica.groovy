package org.ar4k

import grails.converters.JSON
import grails.util.Holders

class Fabbrica {
	/** id univoco fabbrica */
	String idFabbrica = UUID.randomUUID()
	/** etichetta */
	String etichetta = null
	/** descrizione */
	String descrizione = null

	/** salva in uno Stato specifico a catena per N profondità */
	Boolean salva(Stato stato,Integer contatore) {
		stato.salvaValore(idFabbrica,'org-ar4k-fabbrica',(esporta() as JSON).toString())
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

	/** esporta il contenuto dell'oggetto */
	def esporta() {
		return [
			idFabbrica:idFabbrica,
			etichetta:etichetta,
			descrizione:descrizione
		]
	}
}
