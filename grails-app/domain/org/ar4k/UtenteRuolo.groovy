/**
 * UtenteRuolo Ar4k
 *
 * <p>UtenteRuolo</p>
 *
 * <p style="text-justify">
 * UtenteRuolo Ar4k
 *
 * Gestione associazione molti a molti tra Utenti e Ruoli.
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

import org.apache.commons.lang.builder.HashCodeBuilder

class UtenteRuolo implements Serializable {

	private static final long serialVersionUID = 1

	Utente utente
	Ruolo ruolo

	/** esporta gli utenti nel contesto. Esportando UtenteRuolo si ottengo anche i dettagli degli oggetti annidati (Utente,Ruolo) */
	def esporta() {
		log.info("Esporta UtenteRuolo: "+utente?.username+" nel ruolo "+ruolo?.authority)
		return [
			utente:utente.esporta(),
			ruolo:ruolo.esporta()
		]
	}

	static UtenteRuolo importa(Utente utente,Ruolo ruolo){
		//UtenteRuolo utenteRuolo = UtenteRuolo.findAllByUtenteAndRuolo(utente,ruolo)[0]
		//if (!utenteRuolo){
		UtenteRuolo	utenteRuolo = new UtenteRuolo(
				utente:utente,
				ruolo:ruolo
				)
		//}
		//utenteRuoloCreato.save(flush:true)
		return utenteRuolo
	}

	boolean equals(other) {
		if (!(other instanceof UtenteRuolo)) {
			return false
		}

		other.utente?.id == utente?.id &&
				other.ruolo?.id == ruolo?.id
	}

	int hashCode() {
		def builder = new HashCodeBuilder()
		if (utente) builder.append(utente.id)
		if (ruolo) builder.append(ruolo.id)
		builder.toHashCode()
	}

	static UtenteRuolo get(String utenteId, String ruoloId) {
		UtenteRuolo.where {
			utente == Utente.load(utenteId) &&
					ruolo == Ruolo.load(ruoloId)
		}.get()
	}

	static boolean exists(String utenteId, String ruoloId) {
		UtenteRuolo.where {
			utente == Utente.load(utenteId) &&
					ruolo == Ruolo.load(ruoloId)
		}.count() > 0
	}

	static UtenteRuolo create(Utente utente, Ruolo ruolo, boolean flush = false) {
		def instance = new UtenteRuolo(utente: utente, ruolo: ruolo)
		instance.save(flush: flush, insert: true)
		instance
	}

	static boolean remove(Utente u, Ruolo r, boolean flush = false) {
		if (u == null || r == null) return false

		int rowCount = UtenteRuolo.where {
			utente == Utente.load(u.id) &&
					ruolo == Ruolo.load(r.id)
		}.deleteAll()

		if (flush) {
			UtenteRuolo.withSession { it.flush() } }

		rowCount > 0
	}

	static void removeAll(Utente u, boolean flush = false) {
		if (u == null) return

			UtenteRuolo.where {
				utente == Utente.load(u.id)
			}.deleteAll()

		if (flush) {
			UtenteRuolo.withSession { it.flush() } }
	}

	static void removeAll(Ruolo r, boolean flush = false) {
		if (r == null) return

			UtenteRuolo.where {
				ruolo == Ruolo.load(r.id)
			}.deleteAll()

		if (flush) {
			UtenteRuolo.withSession { it.flush() } }
	}

	static constraints = {
		ruolo validator: { Ruolo r, UtenteRuolo ur ->
			if (ur.utente == null) return
				boolean existing = false
			UtenteRuolo.withNewSession {
				existing = UtenteRuolo.exists(ur.utente.id, r.id)
			}
			if (existing) {
				return 'userRole.exists'
			}
		}
	}

	static mapping = {
		id composite: ['ruolo', 'utente']
		version false
	}

	String toString() {
		return this.ruolo.toString()+" - "+this.utente.toString()
	}
}
