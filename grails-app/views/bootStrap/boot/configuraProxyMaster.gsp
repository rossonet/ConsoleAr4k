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


					<g:if test="${errore}">
						<div class="alert alert-danger">
							${errore}
						</div>
					</g:if>
					<h2>Configurazione proxy</h2>
					<!-- Se il server non è connesso direttamente a internet -->
					<p class="text-justify" style="text-align: justify;">Puoi
						configurare uno o più proxy da questa interfaccia. I proxy
						potranno essere usati per connettere l'interfaccia ad un server
						Consul oppure per connettere un tunnel SSH. Un tunnel SSH genera
						un proxy quando attivo.</p>
					<form class="form-style validate-form clearfix" autocomplete='off'
						action="${createLink(event: 'successo')}" name="configuraProxy"
						method="post">
						<div class="form-group">
							<label for="proxyMaster">Indirizzo e utente</label> <input
								type="text" name="proxyMaster"
								class="text-field form-control required"
								data-validation-type="string" id="proxyMaster"
								placeholder='http o https://<utente>@macchina:<porta>'>
						</div>
						<div class="form-group">
							<label for="passwordProxyMaster">Password</label> <input
								type="password" name="passwordProxyMaster"
								class="text-field form-control" data-validation-type="string"
								id="passwordProxyMaster" placeholder="Password del proxy">
						</div>
						<input type="hidden" name="provenienza" value="${provenienza}">
						<div class="form-group">
							<button name="_eventId"
								class="link-scroll btn btn-success btn-outline-inverse btn-lg"
								value="configuraProxyMaster"
								onClick="document.forms['configuraProxy'].submit();">Configura
								i parametri del proxy sul vaso</button>
						</div>
						<p>
							<a
								href="${createLink(event: 'indietro')}&provenienza=${provenienza}"
								class="link-scroll btn btn-warning btn-outline-inverse btn-lg">indietro</a>
						</p>
						<p>
							Nella maggior parte dei casi per connettersi ad una installazione
							si dovrà accedere via SSH ad un host.
							<g:if test="${ssh}">Sono già configurate le connessioni a ${ssh}
							</g:if>
							<br /> <a
								href="${createLink(event: 'configuraSSH')}&provenienza=${provenienza}"
								class="link-scroll btn btn-warning btn-outline-inverse btn-lg">Configura
								i parametri di accesso SSH</a>
						</p>

						<p>
							E' possibile accedere ad una installazione Consul in ascolto
							sulla rete Onion.<br /> <a
								href="${createLink(event: 'configuraOnion')}&provenienza=${provenienza}"
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

