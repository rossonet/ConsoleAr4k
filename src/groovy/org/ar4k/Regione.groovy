/**
 * Regione
 *
 * <p>Una regione rappresenta un organizzazione di più nodi in un contesto.</p>
 *
 * <p style="text-justify">
 * La regione può rappresentare un'area della rete, una zona di calcolo, un'area geografica ecc...</br>
 * </p>
 *
 * @author Andrea Ambrosini (Rossonet s.c.a r.l)
 * @version 0.1-alpha
 * @see org.ar4k.Nodo
 * @see org.ar4k.Vaso
 * @see org.ar4k.Interfaccia
 * @see org.ar4k.Contesto
 */


package org.ar4k

import grails.converters.JSON
import grails.util.Holders

class Regione {
	/** id univoco regione */
	String idRegione = UUID.randomUUID()
	/** etichetta regione */
	String etichetta = null
	/** descrizione regione */
	String descrizione = null
	/** lista nodi in questa regione */
	List<Nodo> nodi = []
	/** datacenter consul della regione - più regioni possono essere sullo stesso dc */
	String datacenterConsul = null

	/** salva in uno Stato specifico a catena per N profondità */
	Boolean salva(Stato stato,Integer contatore) {
		stato.salvaValore(idRegione,'org-ar4k-regione',(esporta() as JSON).toString())
		if (contatore > 0) {
			contatore = contatore -1
			nodi*.salva(stato,contatore)
		}
	}
	/** salva in uno Stato specifico solo l'oggetto */
	Boolean salva(Stato stato) {
		salva(stato,0)
	}
	/** salva nello stato di defalt solo l'oggetto */
	Boolean salva() {
		salva(Holders.applicationContext.getBean("interfacciaContestoService").stato,0)
	}
	/** salva nello stato di defalt a catena per N profondità */
	Boolean salva(Integer contatore) {
		salva(Holders.applicationContext.getBean("interfacciaContestoService").stato,contatore)
	}

	/** esporta la regione */
	def esporta() {
		return [
			idRegione:idRegione,
			descrizione:descrizione,
			etichetta:etichetta,
			datacenterConsul:datacenterConsul,
			nodi:nodi*.idNodo
		]
	}
}
