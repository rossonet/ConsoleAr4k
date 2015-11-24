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
					ssh:bootStrapService.verificaSSH(),
					proxies:bootStrapService.verificaProxy(),
					tor:bootStrapService.verificaOnion()
				]
			}.to "showBenvenuto"
			on (Exception).to "showBenvenuto"
			on ("completata").to("completata")
			on ("reset"){[inReset:true]}.to("showBenvenuto")
		}

		showBenvenuto {
			on ("configuraConsul").to "parametriConsul"
			on ("configuraCodCommerciale").to "configuraCodCommerciale"
			on ("configuraNuovoConsul").to "nuovoConsul"
			on ("fallita"){ [messaggioOlark:"Salve. Posso aiutare?"] }.to("fallita")
		}

		configuraCodCommerciale {
			on ("indietro").to "entrata"
			on ("completata").to("provaCodiceCommerciale")
			on ("fallita"){ [messaggioOlark:"Salve. Come posso aiutare?"] }.to("fallita")
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
			on ("errore").to("configuraCodCommerciale")
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
				Boolean errore = false
				String macchina = '127.0.0.1'
				Integer porta = 3128
				String utente = null
				String password = null
				String protocollo = 'http'
				try{
					URL targetProxy = new URL(params.proxyMaster)
					protocollo = targetProxy.getProtocol()?:'http'
					porta = targetProxy.getPort()?:3128
					macchina = targetProxy.getHost()?:'127.0.0.1'
				} catch(Exception ee) {
					errore = true
					log.error(ee)
				}
				if (errore==false ) {
					Proxy nuovoProxy = new Proxy(
							macchina:macchina,
							porta:porta,
							utente:utente,
							password:password,
							protocollo:protocollo)
					bootStrapService.stato.listaProxy.add(nuovoProxy)
					bootStrapService.stato.connessioneProxy = true
					return successo()
				} else {
					bootStrapService.errori.add("Errore nella configurazione del proxy")
					return errore()
				}
			}
			on ("successo"){[proxies:bootStrapService.verificaProxy()]}.to {params.provenienza?:"entrata"}
			on ("errore").to "configuraProxyMaster"
			on (Exception).to "configuraProxyMaster"
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
					} catch(Exception ee) {
					}
				}
				//costruisce l'oggetto connessione
				ConnessioneSSH provaSSH = new ConnessioneSSH(
						macchina:params.indirizzoMaster?:'127.0.0.1',
						porta:porta,
						utente:params.utenteMaster?:'root',
						key:params.chiaveMaster?:null,
						proxyMaster:params.proxyMaster!='nessuno'?params.proxyMaster:null
						)

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
			on ("successo"){[ssh:bootStrapService.verificaSSH()]}.to {params.provenienza?:"entrata"}
			on ("errore").to "configuraMaster"
			on (Exception).to "configuraMaster"
		}

		configuraOnion {
			on ("successo").to "testOnion"
			on ("indietro").to {params.provenienza?:"entrata"}
			on ("configuraCodCommerciale").to "configuraCodCommerciale"
			on ("configuraSSH").to "configuraMaster"
			on ("configuraProxy").to "configuraProxyMaster"
		}

		testOnion {
			action {
				String risultato = bootStrapService.stato.attivaProxyTOR()
				return risultato?successo():errore()
			}
			on ("successo"){[tor:bootStrapService.verificaOnion()]}.to {params.provenienza?:"entrata"}
			on ("errore").to "configuraOnion"
		}

		creaConsul {
			action { return successo() }
			on ("successo").to "verifica"
			on ("errore").to "nuovoConsul"
		}

		verifica {
			action {
				if ( params.sshTunnel != 'nessuno' ) {
					bootStrapService.stato.tunnelSSH = params.sshTunnel
				} else {
					bootStrapService.stato.tunnelSSH = null
				}
				return bootStrapService.stato.connetti()?successo():errore()
			}
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
