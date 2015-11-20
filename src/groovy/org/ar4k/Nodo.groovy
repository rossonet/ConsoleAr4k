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

class Nodo {
	/** id univoco nodo */
	String idNodo = UUID.randomUUID()
	/** lista vasi su questo nodo */
	List<Vaso> vasi = []
}
