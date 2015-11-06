/**
 * RefreshToken
 *
 * <p>RefreshToken</p>
 *
 * <p style="text-justify">
 * RefreshToken
 *
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

class RefreshToken {

    String value
    Date expiration
    byte[] authentication

    static constraints = {
        value nullable: false, blank: false, unique: true
        expiration nullable: true
        authentication nullable: false, minSize: 1, maxSize: 1024 * 4
    }

    static mapping = {
        version false
    }
}
