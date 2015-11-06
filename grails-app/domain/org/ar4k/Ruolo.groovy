/**
 * Ruolo Ar4k
 *
 * <p>Ruolo</p>
 *
 * <p style="text-justify">
 * Ruolo nel sistema Ar4k
 * 
 * La fonte primaria per la gestione dei ruoli, la loro registrazione e l'associazione a gli Utenti è la console.
 * La console mantiene in database tutti i valori relativi.
 * 
 * Le classi coinvolte nella gestione degli utenti sono: Utente,Ruolo,UtenteRuolo,Client,OAuthID,AccessToken e RefreshToken.
 * </p>
 * 
 * 
 * TODO: implementazione OAuthID server per tutta l'infrastruttura Ar4k sulla Console
 *
 * TODO: esportazione e gestione sincronizzazione con alberi ldap esterni  
 *
 * @author Andrea Ambrosini (Rossonet s.c.a r.l)
 * @version 0.1-alpha
 * @see org.ar4k.Contesto
 * @see org.ar4k.Utente
 * @see org.ar4k.Ruolo
 * @see org.ar4k.UtenteRuolo
 * @see org.ar4k.Client
 * @see org.ar4k.OAuthID
 * @see org.ar4k.AccessToken
 * @see org.ar4k.RefreshToken
 */

package org.ar4k

import grails.converters.JSON;

import org.activiti.engine.identity.Group

class Ruolo implements Group {

	/** etichetta per la definizione del ruolo */
	String id
	String authority
	String type

	/** dump dati verso Ar4k */
	def esporta() {
		return [
			authority:authority,
			type:type
		]
	}

	static Ruolo importa(Map json){
		Ruolo ruolo = Ruolo.findAllByAuthority(json.authority)[0]
		if(!ruolo){
			ruolo = new Ruolo(
					authority:json.authority,
					type:json.type
					)
		}
		//ruoloCreato.save(flush:true)
		return ruolo
	}

	static mapping = {
		cache true
		id generator: 'uuid'
	}

	static constraints = {
		authority blank: false, unique: true
		type nullable: true
	}

	String getName() {
		return authority;
	}

	void setName(String arg0) {
		// TODO Auto-generated method stub
	}

	String toString() {
		return this.authority
	}
}
