package org.ar4k

/**
 *  Rappresentazione proxy di rete
 * 
 * 
 * 
 * @author andrea ambrosini
 *
 */
class Proxy {
	/** id univoco proxy */
	String idProxy = UUID.randomUUID()
	/** indirizzo ip o dns host connessione ssh */
	String macchina = '127.0.0.1'
	/** porta connessione ssh */
	Integer porta = 3128
	/** utente proxy */
	String utente = null
	/** password proxy */
	String password = null
	/** tipologia proxy */
	String protocollo = 'http'

	String toString() {
		String utenza = ''
		if (utente != null) utenza = utente+'@'
		return protocollo+'://'+utenza+macchina+':'+porta
	}
}