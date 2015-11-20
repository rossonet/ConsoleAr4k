/**
 * Controller bootstrap del Contesto
 *
 * <p>
 * Il controller gestisce le chiamate per l'avvio dell'ambiente Ar4k
 * Accessibile a tutti all'inizio, diventa accessibile solo all'amministratore
 * dopo la configurazione iniziale.
 * </p>
 *
 * <p style="text-justify">
 * Il Controller lavora insieme al relativo service
 * </p>
 *
 * @author Andrea Ambrosini (Rossonet s.c.a r.l)
 * @version 0.1-alpha
 * @see org.ar4k.BootStrapService
 */

package org.ar4k

class BootStrapController {

	BootStrapService bootStrapService

	def index() {
		redirect(action: "boot")
	}

	def bootFlow = {
		entrata {
			action {
				if (bootStrapService.inAvvio == false) {
					return completata()
				} else {
					if (bootStrapService.inReset == true) {
						return reset()
					} else {
						return success()
					}
				}
			}
			on ("success"){
				[verificaInternet:bootStrapService.verificaConnettivitaInterfaccia(),
					vedrificaSSH:bootStrapService.verificaSSH(),
					verificaProxy:bootStrapService.verificaProxy(),
					verificaOnion:bootStrapService.verificaOnion()
				]
			}.to "showBenvenuto"
			on (Exception).to "showBenvenuto"
			on ("completata").to("completata")
			on ("reset"){[inReset:true]}.to("showBenvenuto")
		}

		showBenvenuto {
			on ("configuraConsul"){[ssh:bootStrapService.stato.listaGWSSH.join(", ")]}.to "parametriConsul"
			on ("configuraCodCommerciale").to "configuraCodCommerciale"
			on ("configuraNuovoConsul"){[ssh:bootStrapService.stato.listaGWSSH.join(", ")]}.to "nuovoConsul"
			on ("fallita"){ [messaggioOlark:"Salve..."] }.to("fallita")
		}

		configuraCodCommerciale {
			on ("indietro").to "entrata"
			on ("completata").to("provaCodiceCommerciale")
			on ("fallita"){ [messaggioOlark:"Salve. Serve aiuto per configurare la piattaforma?"] }.to("fallita")
		}

		provaCodiceCommerciale {
			action {
				if (bootStrapService.importaCodiceAttivazione(params.codCommerciale?:'')) {
					return completata()
				} else {
					return errore()
				}
			}
			on ("completata").to("mascheraFinale")
			on ("errore"){ssh:bootStrapService.stato.listaGWSSH}.to("configuraCodCommerciale")
		}

		nuovoConsul {
			on ("completato").to "creaConsul"
			on ("indietro").to "entrata"
			on ("configuraCodCommerciale").to "configuraCodCommerciale"
			on ("configuraProxy"){[provenienza:'nuovoConsul']}.to "configuraProxyMaster"
			on ("configuraSSH"){[provenienza:'nuovoConsul']}.to "configuraMaster"
			on ("configuraOnion"){[provenienza:'nuovoConsul']}.to "configuraOnion"
			on ("fallita"){ [messaggioOlark:"Salve. Serve aiuto per configurare la piattaforma?"] }.to("fallita")
		}

		parametriConsul {
			on ("completato").to "verifica"
			on ("indietro").to "entrata"
			on ("configuraCodCommerciale").to "configuraCodCommerciale"
			on ("configuraProxy"){[provenienza:'parametriConsul']}.to "configuraProxyMaster"
			on ("configuraSSH"){[provenienza:'parametriConsul']}.to "configuraMaster"
			on ("configuraOnion"){[provenienza:'parametriConsul']}.to "configuraOnion"
			on ("fallita"){ [messaggioOlark:"Salve. Serve aiuto per configurare la piattaforma?"] }.to("fallita")
		}

		configuraProxyMaster {
			on ("successo").to "testProxyMaster"
			on ("configuraCodCommerciale").to "configuraCodCommerciale"
			on ("indietro").to {params.provenienza?:"entrata"}
			on ("configuraSSH").to "configuraMaster"
			on ("configuraOnion").to "configuraOnion"
		}

		testProxyMaster {
			action {
				bootStrapService.proxyMasterInternet=params.proxyMaster?:bootStrapService.proxyMasterInternet
				bootStrapService.passwordProxyMasterInternet=params.passwordProxyMaster?:bootStrapService.passwordProxyMasterInternet
			}
			on ("success"){}.to {params.provenienza?:"entrata"}
			on ("errore").to "configuraProxyMaster"
		}

		configuraMaster {
			on ("indietro").to {params.provenienza?:"entrata"}
			on ("configuraCodCommerciale").to "configuraCodCommerciale"
			on ("successo").to "verificaMaster"
			on ("configuraProxy").to "configuraProxyMaster"
			on ("configuraOnion").to "configuraOnion"
		}

		verificaMaster {
			action {
				Integer porta = 22
				//controllo conversione numero
				if ( params.portaMaster != '' ) {
					try {
						porta = params.portaMaster.toInteger()?:22
					} catch(ee) {
					}
				}
				//costruisce l'oggetto connessione
				ConnessioneSSH provaSSH = new ConnessioneSSH(
						macchina:params.indirizzoMaster?:'127.0.0.1',
						porta:porta,
						utente:params.utenteMaster?:'root',
						key:params.chiaveMaster?:null
						)
				log.error("Stato connessioni: "+bootStrapService.stato.listaGWSSH)

				if (provaSSH.provaConnessione()) {
					bootStrapService.stato.listaGWSSH.add(provaSSH)
					bootStrapService.stato.connessioneSSH = true
					return successo()
				} else {
					if (bootStrapService.stato.listaGWSSH.size() < 1) { bootStrapService.stato.connessioneSSH = false }
					bootStrapService.errori.add("Connessione ssh non riuscita con l'host "+provaSSH.macchina)
					return errore()
				}
			}
			on ("successo"){[ssh:bootStrapService.stato.listaGWSSH.join(", ")]}.to {params.provenienza?:"entrata"}
			on ("errore").to "configuraMaster"
		}

		configuraOnion {
			on ("success").to "testOnion"
			on ("indietro").to {params.provenienza?:"entrata"}
			on ("configuraCodCommerciale").to "configuraCodCommerciale"
			on ("configuraSSH").to "configuraMaster"
			on ("configuraProxy").to "configuraProxyMaster"
		}

		testOnion {
			action {
				bootStrapService.proxyMasterInternet=params.proxyMaster?:bootStrapService.proxyMasterInternet
				bootStrapService.passwordProxyMasterInternet=params.passwordProxyMaster?:bootStrapService.passwordProxyMasterInternet
			}
			on ("success").to {params.provenienza?:"entrata"}
			on ("errore").to "configuraOnion"
		}

		creaConsul {
			action { return successo() }
			on ("successo").to "verifica"
			on ("errore").to "nuovoConsul"
		}

		verifica {
			action { return successo() }
			on ("successo").to "scegliContesto"
			on ("errore").to "parametriConsul"
		}

		scegliContesto {
			on ("indietro").to "entrata"
			on ("scegliInterfaccia").to "provaContesto"
		}

		provaContesto {
			action { return scegliInterfaccia() }
			on ("scegliInterfaccia"){
				def lista = []
				[listaInterfacce:lista]
			}.to "scegliInterfaccia"
			on ("errore").to "verifica"
		}

		scegliInterfaccia {
			on ("indietro").to "verifica"
			on ("avvia").to "provaUtente"
		}

		provaUtente {
			action { return configuraAmministratore() }
			on ("configuraAmministratore").to "configuraAmministratore"
			on ("presente").to("testFinale")
		}

		configuraAmministratore {
			on ("completata").to("testFinale")
			on ("fallita"){ [messaggioOlark:"Salve! La configurazione era quasi finita. Che è successo?"] }.to("fallita")
		}

		testFinale {
			action {
				if(params.utenteDemo ) {
					if ( params.passwordDemo1 == params.passwordDemo2 && params.passwordDemo1 != '') {
						log.info("Creo l'utente "+params.utenteDemo)
						bootStrapService.aggiungiUtente(params.utenteDemo,params.passwordDemo1)
					} else {
						log.info("Le password NON corrispondo per creare l'utente "+params.utenteDemo)
						if ( params.utenteDemo) {
							return configuraAmministratore()
						}
					}
				}

				if(bootStrapService.inizio()) {
					return completata()
				} else {
					return fallita()
				}
			}
			on ("completata").to("mascheraFinale")
			on ("noConsul").to("verifica")
			on ("fallita"){
				[rapporto:bootStrapService.toString(),messaggioOlark:"Salve. Serve aiuto per configurare la piattaforma?"]
			}.to("fallita")
		}

		mascheraFinale {
			on ("completata").to("redirezione")
		}

		redirezione { redirect controller:'admin' }

		// Implementare pagina aiuto via Olark
		fallita {
			on ("indietro").to "entrata"
			on ("configuraCodCommerciale").to "configuraCodCommerciale"
		}
	}
}
