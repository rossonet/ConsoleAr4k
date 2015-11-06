/**
 * Interfaccia
 *
 * <p>Un interfaccia rappresenta i dati di un istanza Ar4k</p>
 *
 * <p style="text-justify">
 * Per funzionare, un istanza è sempre parte di un Contesto collegato ad un Vaso</br>
 * Il processo di creazione dell'interfaccia prevede il collegamento SSH ad un vaso tramite file
 * di configurazione o wizard iniziale.</br>
 * Ogni interfaccia dispone di propria associazione a gli account social in funzione delle password centrali, un contesto può ospitare più interfacce
 * ed ha un unico store degli utenti (con nome assoluto) e dei relativi ruoli.</br>
 * </br>
 * Un interfaccia recupera le informazioni del suo funzionamento dal contesto a cui è collegata ed espone al contesto i metodi per operare
 * sui service</br>
 * </p>
 *
 * @author Andrea Ambrosini (Rossonet s.c.a r.l)
 * @version 0.1-alpha
 * @see org.ar4k.Contesto
 * @see org.ar4k.SshService
 * @see org.ar4k.CamelService
 */

package org.ar4k

import grails.converters.JSON

class Interfaccia {
	/** id univoco interfaccia */
	String idInterfaccia = UUID.randomUUID()
	/** etichetta interfaccia */
	String etichetta = ''
	/** descrizione interfaccia */
	String descrizione =''
	/** schema grafico */
	TemplateInterfaccia grafica = new TemplateInterfaccia()

	/** esporta la configurazione dell'interfaccia */
	def esporta() {
		log.info("esporta() l'interfaccia: "+idInterfaccia)
		return [
			idInterfaccia:idInterfaccia,
			etichetta:etichetta,
			descrizione:descrizione,
			grafica:grafica.esporta()
		]
	}

	/** salva il contesto sul nodo master */
	Boolean salva(Stato stato) {
		stato.salvaValore(idInterfaccia,'org-ar4k-Interfaccia',((esporta() as JSON).toString()))
	}


	Interfaccia importa(Map json){
		log.info("importa() l'interfaccia: "+json.idInterfaccia)
		Interfaccia interfacciaCreata = new Interfaccia(
				idInterfaccia:json.idInterfaccia,
				etichetta:json.etichetta,
				descrizione:json.descrizione,
				grafica:new TemplateInterfaccia(json.grafica),
				portaConsul:json.portaConsul,
				portaActiveMQ:json.portaActiveMQ
				)
		return interfacciaCreata
	}

	/** metodo descrizione */
	String toString() {
		return "["+idInterfaccia+"] "+etichetta
	}

	/** configura i service in funzione dell'interfaccia */
	Boolean avviaInterfaccia() {
		return true
	}
}

/** descrive il template grafico dell'interfaccia */
class TemplateInterfaccia {
	String idGrafica = UUID.randomUUID()
	String etichetta = 'Base Ar4k'
	String immagineLogo = 'http://www.rossonet.org/wp-content/uploads/2015/01/logoRossonet4.png'
	Boolean sviluppo = true
	def colori = [
		bordo: '#152039',
		fondoBody: '#717A8F',
		footerColor: '#152039',
		menuAttivo: '#565519',
		menuFocus: '#69682F',
		menuColor: '#D5D5A4',
		menuSfondo: '#3A445B',
		rigaDispariColor: 'black',
		rigaDispariSfondo: '#717A8F',
		rigaPariColor: 'black',
		rigaPariSfondo: '#D5D5A4',
		primary: 'grey',
		success: 'green',
		info: 'white',
		warning: 'orange',
		danger: 'red'
	]

	def esporta() {
		log.info("esporta() il template grafico: "+etichetta)
		return [
			etichetta:etichetta,
			immagineLogo:immagineLogo,
			sviluppo:sviluppo,
			colori:[
				bordo: colori.bordo,
				fondoBody: colori.fondoBody,
				footerColor: colori.footerColor,
				menuAttivo: colori.menuAttivo,
				menuFocus: colori.menuFocus,
				menuColor: colori.menuColor,
				menuSfondo: colori.menuSfondo,
				rigaDispariColor: colori.rigaDispariColor,
				rigaDispariSfondo: colori.rigaDispariSfondo,
				rigaPariColor: colori.rigaPariColor,
				rigaPariSfondo: colori.rigaPariSfondo,
				primary: colori.primary,
				success: colori.success,
				info: colori.info,
				warning: colori.warning,
				danger: colori.danger
			]
		]
	}

	String toString() {
		return etichetta
	}
}
