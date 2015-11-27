<%-- modal scansione rete --%>
<div aria-hidden="false" aria-labelledby="Meme" role="dialog"
	tabindex="-1" id="scansioneReteModal" class="modal fade in"
	ng-show="scansioneRete" style="display: block; padding-right: 13px;">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header" style="min-height: 40px;">
				<button aria-hidden="true" data-dismiss="modal"
					ng-click="scansioneRete=false" class="close" type="button">×</button>
				<h4 id="memiModalLabel" class="modal-title">Scansione rete da
					vaso</h4>
			</div>
			<div class="modal-body">
				<form class="ng-pristine ng-valid" role="form">
					<div class="container-fluid">
						<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12 text-left">
							<div class="col-xs-12 col-sm-12 col-md-3 col-lg-3 text-center">
								<div class="row vertical-center-row">
									<button style="margin: 0.2em;"
										class="btn btn-circle btn-warning btn-xs" type="submit"
										ng-click="$parent.scansioneReteVeloceE(vaso.vaso.idVaso)"
										uib-tooltip="inizia scansione veloce">
										<i class="fa fa-share-alt"></i>
									</button>
									<button style="margin: 0.2em;"
										class="btn btn-circle btn-success btn-xs" type="submit"
										ng-click="$parent.scansioneReteNascostaE(vaso.vaso.idVaso)"
										uib-tooltip="inizia scansione nascosta">
										<i class="fa fa-share-alt"></i>
									</button>
									<button style="margin: 0.2em;"
										class="btn btn-circle btn-danger btn-xs" type="submit"
										ng-click="$parent.scansioneOpenVasE(vaso.vaso.idVaso)"
										uib-tooltip="inizia scansione con OpenVas">
										<i class="fa fa-sun-o"></i>
									</button>
								</div>
							</div>
							<div class="col-xs-12 col-sm-12 col-md-9 col-lg-9 text-left">
								<div class="form-group">
									<label for="target">Target scansione</label> <input
										placeholder="a.b.c.d, e.f.g.h/24, nome_dns.xx"
										ng-model="target" class="form-control">
								</div>
								<div class="form-group">
									<label for="target">Porte scansite</label> <input
										placeholder="xxx, yy-zzz" ng-model="target"
										class="form-control">
								</div>
							</div>
						</div>
					</div>
				</form>
			</div>
			<div class="modal-footer">
				<button data-dismiss="modal" ng-click="scansioneRete=false"
					class="btn btn-default" type="button">Chiudi</button>
			</div>
		</div>
		<!-- /.modal-content -->
	</div>
	<!-- /.modal-dialog -->
</div>
<%-- modal assegna a meme --%>
<div aria-hidden="false" aria-labelledby="Meme" role="dialog"
	tabindex="-1" id="assegnaMemeModal" class="modal fade in"
	ng-show="assegnaMeme" style="display: block; padding-right: 13px;">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header" style="min-height: 40px;">
				<button aria-hidden="true" data-dismiss="modal"
					ng-click="assegnaMeme=false" class="close" type="button">×</button>
				<h4 id="memiModalLabel" class="modal-title">{{titolo}}</h4>
			</div>
			<div class="modal-body">
				<%-- contenuto modal --%>
				in input i memi, la funzione (elenco del meme) e il tasto per
				assegnare
			</div>
			<div class="modal-footer">
				<button data-dismiss="modal" ng-click="assegnaMeme=false"
					class="btn btn-default" type="button">Chiudi</button>
			</div>
		</div>
		<!-- /.modal-content -->
	</div>
	<!-- /.modal-dialog -->
</div>
<%-- modal scansione vaso --%>
<div aria-hidden="false" aria-labelledby="Meme" role="dialog"
	tabindex="-1" id="scansioneVasoModal" class="modal fade in"
	ng-show="scansioneVaso" style="display: block; padding-right: 13px;">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header" style="min-height: 40px;">
				<button aria-hidden="true" data-dismiss="modal"
					ng-click="scansioneVaso=false" class="close" type="button">×</button>
				<h4 id="memiModalLabel" class="modal-title">Scansione vaso</h4>
			</div>
			<div class="modal-body">
				<%-- contenuto modal --%>
				man mano che i test progrediscono appaiono
			</div>
			<div class="modal-footer">
				<button data-dismiss="modal" ng-click="scansioneVaso=false"
					class="btn btn-default" type="button">Chiudi</button>
			</div>
		</div>
		<!-- /.modal-content -->
	</div>
	<!-- /.modal-dialog -->
</div>


<%--pagina principale --%>
<div class="row">
	<div style="margin-top: 5px;">
		<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
			<div class="panel panel-default">
				<div class="panel-heading">
					<h3 class="text-right">
						<i class="fa fa-linux fa-3" /> Vasi (connessioni SSH)
						<button style="margin: 0.1em;"
							class="btn btn-circle btn-default btn-xs" type="button"
							ng-click="focusDocumentazione=!focusDocumentazione"
							tooltip-placement="left"
							uib-tooltip="visualizza la documentazione sui vasi.">
							<i class="fa fa-question"></i>
						</button>
					</h3>
					<p class="text-justify" style="text-align: justify;">
						Un <strong>vaso</strong> AR4K corrisponde a un accesso SSH su un <strong>nodo</strong>
						Unix. I vasi possono ospitare agenti Consul e Console di gestione
						Ar4k. E' possibile configurare i permessi di accesso al vaso
						aggiungendo o togliendo chiavi ssh tramite l'apposita
						funzionalità. Il sistema Ar4k è testato per accedere a vasi con
						sistema operativo Linux. In particolare su distribuzioni
						RedHat,CentOS o Fedora. E' possibile creare nuovi vasi tramite
						servizi cloud esterni (AWS,Azure) o tramite procedure di
						installazione automatiche che permettono l'istanziazione di nuovi
						host tramite bootstrap di rete.
						<button style="margin: 0.1em;" class="btn btn-default btn-xs"
							type="button" ng-click="focusDocumentazione=!focusDocumentazione">
							<i class="fa fa-ellipsis-h"></i>
						</button>
					<p>
					<div marked="vasoHelp" ng-show="focusDocumentazione"></div>
				</div>
				<div class="panel-body">
					<div name="nuovo-vaso" ng-show="nuovo" class="panel panel-yellow">
						<div class="panel-body">
							<form class="ng-pristine ng-valid" role="form">
								<div class="form-group">
									<input placeholder="Etichetta" ng-model="vaso.etichetta"
										class="form-control">
								</div>
								<div class="form-group">
									<input placeholder="Descrizione" ng-model="vaso.descrizione"
										class="form-control">
								</div>
								<div class="form-group">
									<input
										placeholder="indirizzo host ( ess. 127.0.0.1 o nome.rossonet.com )"
										ng-model="vaso.hostssh" class="form-control">
								</div>
								<div class="form-group">
									<input placeholder="porta accesso ssh ( ess. 22)"
										ng-model="vaso.porta" class="form-control">
								</div>
								<div class="form-group">
									<input placeholder="utente accesso ssh ( ess. root)"
										ng-model="vaso.utente" class="form-control">
								</div>
								<div class="form-group">
									<textarea
										placeholder='copiare la chiave privata ssh comprese le linee di intestazione e chiusura "-----BEGIN RSA PRIVATE KEY-----" e "-----END RSA PRIVATE KEY-----"'
										ng-model="vaso.key" class="form-control"></textarea>
								</div>
								<div class="form-group text-right">
									<input type="submit" class="btn btn-default"
										ng-click="nuovoVaso(vaso);nuovo=false" value="Salva" /> <input
										type="button" class="btn btn-default"
										ng-click="nuovo=false;reset()" value="Annulla" />
								</div>
							</form>
						</div>
					</div>
					<div class="container-fluid">



						<div class="col-xs-12 col-sm-12 col-md-6 col-lg-6 text-left">

							<button alt="Aggiungi un nuovo nodo SSH al contesto"
								class="btn btn-outline btn-primary" ng-hide="nuovo"
								type="button" ng-click="nuovo=true"
								uib-tooltip="aggiunge un nuovo vaso. Richiede la configurazione dell'accesso SSH al nodo.">NUOVO
								VASO</button>

							<button class="btn btn-success btn-primary" ng-hide="nuovo"
								type="button" ng-click="nuovo=true"
								uib-tooltip="aggiungi una risorsa di calcolo fornita da Rossonet.">
								<i class="fa fa-shopping-cart"></i> CODICE AR4K
							</button>
						</div>


						<div
							class="col-xs-12 col-sm-12 col-md-6 col-lg-6 text-right form-group input-group div4-padding">
							<input placeholder="ricerca testuale" ng-model="queryRicerca"
								class="form-control" /><span class="input-group-btn">
								<button class="btn btn-default" type="button"
									ng-click="queryRicerca=''">
									<i class="fa fa-times"></i>
								</button>
							</span>
						</div>

						<div ng-repeat="vaso in vasi | filter:queryRicerca"
							ng-class-odd="'col-xs-12 col-sm-12 col-md-12 col-lg-12 dispari'"
							ng-class-even="'col-xs-12 col-sm-12 col-md-12 col-lg-12 pari'">
							<div class="col-xs-12 col-sm-12 col-md-5 col-lg-4 div4-padding">{{vaso.vaso.etichetta}}</div>
							<div class="col-xs-12 col-sm-12 col-md-3 col-lg-4 div4-padding">{{vaso.vaso.descrizione}}</div>
							<div
								class="col-xs-6 col-sm-6 col-md-2 col-lg-2 text-left div4-padding">
								<button style="margin: 0.1em;"
									class="btn btn-circle btn-success btn-xs" type="button"
									ng-click="$parent.assegnaMemeF(vaso.vaso.idVaso)"
									uib-tooltip="installa servizio Consul">
									<i class="fa fa-bolt"></i>
								</button>
								<!--  button style="margin: 0.1em;"
									class="btn btn-circle btn-info btn-xs" type="button"
									ng-click="$parent.dettagli(meme.idMeme)"
									
									uib-tooltip="installa e configura un'interfaccia su questo nodo.">
									<i class="fa fa-desktop "></i>
								</button-->
								<button style="margin: 0.1em;"
									class="btn btn-circle btn-info btn-xs" type="button"
									ng-click="$parent.scansioneVasoF(vaso.vaso.idVaso)"
									uib-tooltip="scansione vaso">
									<i class="fa fa-steam"></i>
								</button>
								<button style="margin: 0.1em;"
									class="btn btn-circle btn-warning btn-xs" type="button"
									ng-click="$parent.scansioneReteF(vaso.vaso.idVaso)"
									uib-tooltip="scansiona rete dal vaso">
									<i class="fa fa-share-alt"></i>
								</button>
								<button style="margin: 0.1em;"
									class="btn btn-circle btn-danger btn-xs" type="button"
									ng-click="$parent.modificaACL(dato.key)"
									uib-tooltip="imposta accessi ssh per questo vaso">
									<i class="fa fa-lock"></i>
								</button>
								<button style="margin: 0.1em;"
									class="btn btn-circle btn-warning btn-xs" type="button"
									ng-click="$parent.modifica(vaso.vaso.idVaso)"
									uib-tooltip="accesso ssh">
									<i class="fa fa-terminal"></i>
								</button>
								<button style="margin: 0.1em;"
									class="btn btn-circle btn-primary btn-xs" type="button"
									ng-click="$parent.modifica(vaso.vaso.idVaso)"
									uib-tooltip="console Ar4k su questo vaso">
									<i class="fa fa-desktop"></i>
								</button>
								<button style="margin: 0.1em;"
									class="btn btn-circle btn-danger btn-xs" type="button"
									ng-click="$parent.modifica(vaso.vaso.idVaso)"
									uib-tooltip="modifica le impostazioni per la connessione">
									<i class="fa fa-edit"></i>
								</button>
								<button style="margin: 0.1em;"
									class="btn btn-circle btn-danger btn-xs" type="button"
									ng-click="$parent.eliminaVaso(vaso.vaso.idVaso)"
									uib-tooltip="elimina il collegamento con il vaso"
									ng-hide="vaso.master">
									<i class="fa fa-trash-o"></i>
								</button>

							</div>
							<div
								class="col-xs-6 col-sm-6 col-md-2 col-lg-2 text-right div4-padding">
								<div
									ng-class="vaso.stato[0].status.name == 'PASSING'?'panel panel-green text-center':'panel panel-red text-center'"
									style="margin-bottom: unset;">
									<div class="panel-heading">
										<i class="fa fa-info-circle"></i> {{vaso.stato[0].output}}
									</div>
								</div>
							</div>
						</div>

					</div>
				</div>
			</div>
		</div>
	</div>
</div>
