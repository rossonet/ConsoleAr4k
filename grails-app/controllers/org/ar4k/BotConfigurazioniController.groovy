/**
 * Controller configurazioni generate e script
 *
 * <p>
 * Genera le configurazioni automatiche in funzione delle richieste dei vari software:
 * - genera le configurazioni per Kettle
 * - genera le configurazioni per StrongLoop e Node (Interfacce)
 * - genera i file di configurazione per Mysql e MongoDB
 * - genera le configurazioni per Tomcat
 * - genera gli script bash per le varie installazioni.
 * </p>
 *
 * <p style="text-justify">
 * Il Controller genera i file di configurazione
 * che vengono recuperati dagli script di configurazione automatica
 * </p>
 *
 * @author Andrea Ambrosini (Rossonet s.c.a r.l)
 * @version 0.1-alpha
 * @see org.ar4k.BootStrapService
 */


package org.ar4k

class BotConfigurazioniController {

	def index() { }

	def ping(String messaggio) {
		log.warn("Ricevuto ping con codice: "+messaggio)
		render "ok\nmessaggio: "+messaggio
	}
}
