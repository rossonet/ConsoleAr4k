package org.ar4k

import grails.converters.JSON
import grails.util.Holders

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
	String macchina = null
	/** porta connessione ssh */
	Integer porta = null
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

	/** salva in uno Stato specifico a catena per N profondità */
	Boolean salva(Stato stato,Integer contatore) {
		stato.salvaValore(idProxy,'org-ar4k-proxy',(esporta() as JSON).toString())
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

	/** esporta il vaso */
	def esporta() {
		return [
			idProxy:idProxy,
			macchina:macchina,
			porta:porta,
			utente:utente,
			password:password,
			protocollo:protocollo
		]
	}

	static Proxy creaDaUrl(String urlProxy,String password) {
		Proxy nuovoProxy = new Proxy()
		try{
			URL targetProxy = new URL(urlProxy)
			nuovoProxy.protocollo = targetProxy.getProtocol()?:null
			nuovoProxy.porta = targetProxy.getPort()?:null
			nuovoProxy.macchina = targetProxy.getHost()?:null
			nuovoProxy.utente = targetProxy.getUserInfo()?:null
			nuovoProxy.password = password
		} catch(Exception ee) {
			nuovoProxy = null
		}
		return nuovoProxy
	}
}