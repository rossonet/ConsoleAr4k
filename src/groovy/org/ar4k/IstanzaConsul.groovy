package org.ar4k

import java.util.List;

import com.ecwid.consul.v1.ConsulClient
import com.jcraft.jsch.*
import com.subgraph.orchid.TorClient
import com.subgraph.orchid.TorInitializationListener



/**
 * IstanzaConsul
 *
 * <p>Rappresenta un'istanza consul su un vaso</p>
 *
 * <p style="text-justify">
 *
 * </p>
 *
 * @author Andrea Ambrosini (Rossonet s.c.a r.l)
 * @version 0.1-alpha
 * @see org.ar4k.Meme
 * @see org.ar4k.Ricettario
 * @see org.ar4k.Interfaccia
 * @see org.ar4k.Contesto
 */

class IstanzaConsul {
	/** id univoco */
	String idIstanzaConsul = UUID.randomUUID()
	/** ciclo di vita nel vaso */
	String stato = 'Inizializzazione'
	/** dns - The DNS server, -1 to disable. Default 8600.*/
	Integer portaDNS = 8600
	/** http - The HTTP API, -1 to disable. Default 8500.*/
	Integer portaHTTP = 8500
	/** https - The HTTPS API, -1 to disable. Default -1 (disabled).*/
	Integer portaHTTPS = -1
	/** rpc - The RPC endpoint. Default 8400.*/
	Integer portaRPC = 8400
	/** serf_lan - The Serf LAN port. Default 8301.*/
	Integer portaSerfLAN = 8301
	/** serf_wan - The Serf WAN port. Default 8302.*/
	Integer portaSerfWAN = 8302
	/** server - Server RPC address. Default 8300.*/
	Integer portaServer = 8300
	/** indirizzo presentazione wan per protocollo gossip */
	String advertiseWan = null
	/** avviare Consul in modalità bootstrap? */
	Boolean avvioInBootStrap = false
	/** encript key per consul */
	String chiave = null
	/** nodi Consul su cui tentare la connessione durante il bootstrap
	 * verrà usato il comando "-retry-join"
	 */
	List<String> listaJoin =[]
	/** lista nodi per il join wan in bootstrap */
	List<String> listaJoinWan =[]
	/** l'agente Consul è in modalità server */
	Boolean server = true
	/** dominio per dns */
	String dominio = 'ar4k.net.'
	/** stringa datacenter */
	String datacenter = 'ar4k-test'
}