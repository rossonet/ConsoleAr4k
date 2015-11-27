/**
 * Interfaccia Contesto
 *
 * <p>Gestisce la struttura dati del contesto attivo.</p>
 *
 * <p style="text-justify">
 * Il servizio viene attivato da BootStrapService.</br>
 * La configurazione di Quartz è in PingJob.
 * </p>
 *
 * @author Andrea Ambrosini (Rossonet s.c.a r.l)
 * @version 0.2-alpha
 * @see org.ar4k.BootStrapService
 * @see org.ar4k.Interfaccia
 * @see org.ar4k.Contesto
 */

package org.ar4k

import java.util.zip.ZipInputStream

import grails.converters.JSON
import grails.util.Holders
import org.codehaus.groovy.grails.commons.GrailsApplication
import org.grails.plugins.atmosphere_meteor.AtmosphereMeteor
import org.jclouds.Context
import org.jclouds.ContextBuilder
import org.jclouds.compute.*
import org.jclouds.compute.domain.NodeMetadata
import org.jclouds.compute.domain.Template
import org.jclouds.rest.config.SetCaller.Module
import org.pentaho.di.repository.Repository;

import com.ecwid.consul.v1.ConsulClient
import com.jcraft.jsch.JSch;
import com.ecwid.consul.v1.agent.model.NewService
import org.pentaho.di.core.KettleEnvironment
import org.atmosphere.cpr.Broadcaster
import org.atmosphere.cpr.DefaultBroadcaster

class InterfacciaContestoService {

	// Tramite Camel garantire l'accesso da vari protocolli
	//static expose = ['jmx']

	/** Contesto applicativo Grails */	
	GrailsApplication grailsApplication
	// bean atmosphereMeteor
	def atmosphereMeteor //= Holders.applicationContext.getBean("atmosphereMeteor")
	/** Contesto in esecuzione */
	Contesto contesto
	/** Stato in esecuzione -Consul- */
	Stato stato
	/** Interfaccia corrente */
	Interfaccia interfaccia

	/** attiva il sottosistema Kettle **/
	void initKettle() {
		KettleEnvironment.init()
	}

	/** descrizione contesto */
	String toString() {
		String risultato = "CONTESTO ["+contesto.etichetta+"]\n"
		risultato += contesto.descrizione+"\n"
		risultato += contesto.toString()+"\n"
		risultato += "--------------------------------------\n"
		risultato += stato.toString()+"\n"
		risultato += "--------------------------------------\n"
		return risultato
	}

	/** esegue le procedura ogni 5 min. tramite Quartz */
	void battito() {
		try {
			sendMessage("seda:input.queue", "Memoria libera: "+Runtime.getRuntime().freeMemory())
			//sendMessage("activemq:queue:contesto.salvataggio", contesto.esporta())
		} catch (Exception ee) {
			log.warn "errore esecuzione check: "+ee.toString()+" (Questo può essere normale in fase di bootstrap)"
		}
	}

}

