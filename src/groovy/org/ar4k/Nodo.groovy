/**
 * Nodo
 *
 * <p>Un nodo rappresenta una macchina fisica o virtuale</p>
 *
 * <p style="text-justify">
 * In un nodo sono presenti i vasi, cioè i suoi accessi ssh. Un nodo è parte di una o più regione.</br>
 * </p>
 *
 * @author Andrea Ambrosini (Rossonet s.c.a r.l)
 * @version 0.1-alpha
 * @see org.ar4k.Vaso
 * @see org.ar4k.Regione
 * @see org.ar4k.Interfaccia
 * @see org.ar4k.Contesto
 */

package org.ar4k

import grails.converters.JSON
import grails.util.Holders

class Nodo {
	/** id univoco nodo */
	String idNodo = UUID.randomUUID()
	/** descrizione */
	String descrizione = null
	/** etichetta */
	String etichetta = null
	/** note */
	String note = null
	/** lista vasi su questo nodo */
	List<Vaso> vasi = []
	/** vaso per accesso amministrativo (in cui funziona #sudo) */
	Vaso vasoMaster = null
	/** impronta per identificazione univoca nodo.
	 * Composta da dati rilevabili in user space utili all'identificazione
	 * univoca del nodo
	 */
	Impronta impronta = null
	/** lavorazioni richieste con account amministrativo */
	List<LavorazioniSSH> richiesteAdmin = []
	/** identificativo Consul per il nodo */
	String indirizzoConsul = null
	/** regioni di appartenenza */
	List<Regione> regioni = []
	/** il nodo a connessione discontinua? */
	Boolean mobile = false

	/** crea un nuovo vaso sul nodo */
	LavorazioniSSH creaVaso(String username){
		return false
	}

	/** salva in uno Stato specifico a catena per N profondità */
	Boolean salva(Stato stato,Integer contatore) {
		stato.salvaValore(idNodo,'org-ar4k-nodo',(esporta() as JSON).toString())
		if (contatore > 0) {
			contatore = contatore -1
			vasoMaster?.salva(stato,contatore)
			impronta?.salva(stato,contatore)
			richiesteAdmin*.salva(stato,contatore)
			regioni*.salva(stato,contatore)
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

	/** esporta il nodo */
	def esporta() {
		return [
			idNodo:idNodo,
			descrizione:descrizione,
			etichetta:etichetta,
			note:note,
			vasoMaster:vasoMaster?.idVaso,
			mobile:mobile,
			regioni:regioni*.idRegione,
			impronta:impronta?.idImpronta,
			vasi:vasi*.idVaso
		]
	}
}

/** Impronta univoca per un nodo
 * 
 * @author andrea
 *
 */
class Impronta {
	/** id univoco nodo */
	String idImpronta = UUID.randomUUID()

	/** salva in uno Stato specifico a catena per N profondità */
	Boolean salva(Stato stato,Integer contatore) {
		stato.salvaValore(idImpronta,'org-ar4k-impronta',(esporta() as JSON).toString())
		if (contatore > 0) {
			contatore = contatore -1
			//nessuna dipendenza
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

	/** esporta */
	def esporta() {
		return [
			idImpronta:idImpronta
		]
	}
}
