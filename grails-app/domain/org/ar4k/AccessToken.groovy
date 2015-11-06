/**
 * AccessToken Ar4k
 *
 * <p>AccessToken</p>
 *
 * <p style="text-justify">
 * AccessToken autenticazione Ar4k
 *
 * La fonte primaria per la gestione dei token d'accesso e i relativi valori è la console.
 * La console mantiene in database tutti i valori relativi.
 *
 * Le classi coinvolte nella gestione degli token sono: Utente,Ruolo,UtenteRuolo,Client,OAuthID,AccessToken e RefreshToken.
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

class AccessToken {

    String authenticationKey
    byte[] authentication

    String username
    String clientId

    String value
    String tokenType

    Date expiration
    Map<String, Object> additionalInformation

    static hasOne = [refreshToken: String]
    static hasMany = [scope: String]

    static constraints = {
        username nullable: true
        clientId nullable: false, blank: false
        value nullable: false, blank: false, unique: true
        tokenType nullable: false, blank: false
        expiration nullable: false
        scope nullable: false
        refreshToken nullable: true
        authenticationKey nullable: false, blank: false, unique: true
        authentication nullable: false, minSize: 1, maxSize: 1024 * 4
        additionalInformation nullable: true
    }

    static mapping = {
        version false
        scope lazy: false
    }
}
