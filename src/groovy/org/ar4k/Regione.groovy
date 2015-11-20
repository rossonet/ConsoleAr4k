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

class Regione {
	/** id univoco regione */
	String idRegione = UUID.randomUUID()
	/** lista nodi in questa regione */
	List<Nodo> nodi = []
}
