/**
 * OAuthID
 *
 * <p>OAuthID</p>
 *
 * <p style="text-justify">
 * OAuthID Ar4k
 * 
 * La console mantiene in database tutti i valori relativi.
 *
 * Le classi coinvolte nella gestione degli utenti sono: Utente,Ruolo,UtenteRuolo,Client,OAuthID,AccessToken e RefreshToken.
 * 
 * Simple domain class that records the identities of users authenticating via
 * an OAuth provider. Each identity consists of the OAuth account name and the
 * name of the provider. It also has a reference to the corresponding Spring Security
 * user account, although only long IDs are supported at the moment.
 * 
 * </p>
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

import java.util.Date;

import org.ar4k.Utente


class OAuthID implements Serializable {

	String provider
	String accessToken
	Date dateCreated
	Date lastUpdated

	static belongsTo = [user: Utente]

	static constraints = { accessToken unique: true }

	static mapping = {
		provider    index: "identity_idx"
		accessToken index: "identity_idx"
	}

	/** esporta la configurazione */
	def esporta() {
		return [
			provider:provider,
			accessToken:accessToken,
			dateCreated:dateCreated,
			lastUpdated:lastUpdated
		]
	}
}
