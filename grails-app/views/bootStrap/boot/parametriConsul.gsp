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

					<div class="feature-text">
						<h2>Connetti ad una installazione esistente</h2>
						<p class="text-justify" style="text-align: justify;">
							I dati di un <strong>contesto</strong> Ar4k sono mantenuti dal
							suo servizio Consul. Per connettersi ad una installazione
							esistente inserire l'indirizzo e la porta TCP di un server Consul
							attivo.
						<p>
						<form class="form-style validate-form clearfix" autocomplete='off'
							action="${createLink(event: 'completato')}"
							name="configuraConsulEsistente" method="post">
							<div class="form-group">
								<label for="macchina">Indirizzo installazione Consul</label> <input
									type="text" name="macchina"
									class="text-field form-control validate-field required"
									data-validation-type="string" id="macchina"
									placeholder="127.0.0.1">
							</div>
							<div class="form-group">
								<label for="porta">Porta TCP installazione Consul</label> <input
									type="text" name="porta"
									class="text-field form-control validate-field required"
									data-validation-type="string" id="porta" placeholder="8500">
							</div>
							<div class="form-group">
								<button name="_eventId"
									class="link-scroll btn btn-success btn-outline-inverse btn-lg"
									value="completato"
									onClick="document.forms['configuraConsulEsistente'].submit();">Connetti
									Consul</button>
							</div>
							<a href="${createLink(event: 'indietro')}"
								class="link-scroll btn btn-info btn-outline-inverse btn-lg">Indietro
								alla schermata di benvenuto</a>
							<p>
								Nella maggior parte dei casi per connettersi ad una
								installazione si dovrà accedere via SSH ad un host.
								<g:if test="${ssh}">Sono già configurate le connessioni a ${ssh}
								</g:if>
								<br /> <a href="${createLink(event: 'configuraSSH')}"
									class="link-scroll btn btn-warning btn-outline-inverse btn-lg">Configura
									i parametri di accesso SSH</a>
							</p>
							<p>
								E' possibile utilizzare un proxy per la connessione SSH. Questa
								funzionalità permette l'utilizzo di questa interfaccia da reti
								che non accedono ad Internet direttamente.<br /> <a
									href="${createLink(event: 'configuraProxy')}"
									class="link-scroll btn btn-warning btn-outline-inverse btn-lg">Configura
									un proxy</a>
							</p>
							<p>
								E' possibile accedere ad una installazione Consul in ascolto
								sulla rete Onion.<br /> <a
									href="${createLink(event: 'configuraOnion')}"
									class="link-scroll btn btn-info btn-outline-inverse btn-lg">Configura
									un accesso alla rete Onion</a>
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

