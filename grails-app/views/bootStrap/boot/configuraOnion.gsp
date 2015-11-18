<!DOCTYPE html>
<html lang="en">
<head>

<meta name="layout" content="atterraggio" />

<title>Nuova Console Ar4k in fa bootstrap</title>


<style type="text/css">
.s2ui_hidden_button {
	visibility: hidden;
}

.fade {
	opacity: 0;
}

.fade.in {
	opacity: 0.9;
	-webkit-transition: opacity 1s ease-in;
	transition: opacity 1s ease-in;
}
</style>

</head>

<body>
	<!-- #main-content -->

	<div style="z-index: 1050; display: block; background-color: black;"
		modal-animation="true" animate="animate" index="0"
		uib-modal-window="modal-window" modal-render="true" tabindex="-1"
		role="dialog" class="modal fade ng-isolate-scope"
		uib-modal-animation-class="fade" modal-in-class="in"
		id="benvenutoModal">
		<div class="modal-dialog modal-lg" style="width: 90%;">
			<div class="modal-content" uib-modal-transclude="">
				<div class="col-md-10 col-xs-12 col-sm-12 pull-right">

					<section class="feature-text">
						<h2 style="text-align: right;">Configurazione accesso tramite rete Onion</h2>
						<!-- Se il server non è connesso direttamente a internet -->
						<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
							<img src="${resource(dir: 'images', file: 'onion.png')}"
								class="col-xs-4 col-sm-4 col-md-4 col-lg-4" />
							<p class="col-xs-8 col-sm-8 col-md-8 col-lg-8 text-justify"
								style="text-align: justify;">
								Il <a href="https://it.wikipedia.org/wiki/Tor_%28software%29"
									target="_tor">progetto TOR</a> offre una soluzione open source
								per la scambio dati anonimo. La piattaforma Ar4k non usa Tor per
								garantire l'anonimato delle connessioni, per esempio le
								richieste ai server dns vengono fatte in chiaro, ma utilizza la
								rete Onion per garantire la connessione di ambienti poco
								raggiungibili per via dell'architettura di rete o per motivi
								operativi. Per connettersi ad una istallazione raggiungibile su
								rete Onion è necessario installare un proxy Tor. Per scaricare
								il software necessario utilizzare il link seguente: <a
									href="https://www.torproject.org/download/download.html.en"
									target="_tor">https://www.torproject.org/download/download.html.en</a>
							</p>
							<form class="form-style validate-form clearfix"
								autocomplete='off' action="${createLink(event: 'successo')}"
								name="configuraProxy" method="post">
								<div class="form-group">
									<label for="proxyMaster">Indirizzo e porta proxy Tor</label> <input
										type="text" name="proxyMaster"
										class="text-field form-control validate-field required"
										data-validation-type="string" id="proxyMaster"
										placeholder="<macchina>:<porta>">
								</div>
								<div class="form-group">
									<button name="_eventId"
										class="link-scroll btn btn-success btn-outline-inverse btn-lg"
										value="configuraProxyMaster"
										onClick="document.forms['configuraProxy'].submit();">Configura
										l'accesso TOR</button>
								</div>
								<p>
									<a
										href="${createLink(event: 'indietro')}&provenienza=${provenienza}"
										class="link-scroll btn btn-warning btn-outline-inverse btn-lg">indietro</a>
								</p>
								<p>
									Nella maggior parte dei casi per connettersi ad una
									installazione si dovrà accedere via SSH ad un host.<br /> <a
										href="${createLink(event: 'configuraSSH')}&provenienza=${provenienza}"
										class="link-scroll btn btn-warning btn-outline-inverse btn-lg">Configura
										i parametri di accesso SSH</a>
								</p>

								<p>
									E' possibile utilizzare un normale proxy per accedere
									all'installazione Ar4k. Se verranno configurati un proxy e un
									accesso alla rete Onion verrà utilizzato il proxy per accedere
									al gateway Tor.<br /> <a
										href="${createLink(event: 'configuraProxy')}&provenienza=${provenienza}"
										class="link-scroll btn btn-warning btn-outline-inverse btn-lg">Configura
										un accesso proxy</a>
								</p>
								<p>
									<a href="${createLink(event: 'configuraCodCommerciale')}"
										class="link-scroll btn btn-success btn-outline-inverse btn-lg">Utilizza
										un codice Rossonet</a>
								</p>
								<p>
									<a href="${createLink(event: 'fallita')}"
										class="link-scroll btn btn-danger btn-outline-inverse btn-lg">Richiedi
										assistenza</a>
								</p>
							</form>
						</div>
					</section>

				</div>
				<!-- .col-sm-10 -->
			</div>
			<!-- .content-wrapper -->
		</div>
	</div>
	<script>
		$(document).ready(function() {
			scroll_to_top();
			var d = document.getElementById("benvenutoModal");
			d.className = d.className + " in";
		});

		$(document).ready(function() {
			scroll_to_top();
		});
	</script>

</body>
</html>

</body>
</html>

