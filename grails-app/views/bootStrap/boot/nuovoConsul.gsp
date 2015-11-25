<!DOCTYPE html>
<html lang="en">
<head>

<meta name="layout" content="atterraggio" />

<title>Nuova istanza Consul</title>


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
					<h2>Crea una nuova installazione Ar4k</h2>
					<p class="text-justify" style="text-align: justify;">
						I dati di un <strong>contesto</strong> Ar4k sono mantenuti dal suo
						servizio Consul. Per creare una nuova istanza Consul su un host
						raggiungibile via SSH impostare i seguenti parametri.
					<p>
					<form class="form-style validate-form clearfix" autocomplete='off'
						action="${createLink(event: 'completato')}" name="configuraConsul"
						method="post">
						<div class="form-group">
							<label for="etichetta">Nome Contesto Ar4k</label> <input
								type="text" name="etichetta"
								class="text-field form-control required"
								data-validation-type="string" id="etichetta"
								placeholder="etichetta">
						</div>
						<div class="form-group">
							<label for="descrizione">Descrizione contesto.</label>
							<textarea name="descrizione" class="text-area form-control"
								data-validation-type="required" id="descrizione"
								placeholder='Contesto Ar4k generato automaticamente'></textarea>
						</div>
						<div class="form-group">
							<label for="progetto">Riferimento progetto</label> <input
								type="text" name="progetto" class="text-field form-control"
								data-validation-type="string" id="progetto"
								placeholder="id progetto">
						</div>
						<div class="form-group">
							<label for="sshTunnel">Connessione SSH per avvio Consul</label> <select
								name="sshTunnel" class="selectpicker text-field form-control"
								style="background-color: black; height: 54px; font-size: 0.97em; color: white;"
								id="sshTunnel">
								<g:each in="${ssh?.split(', ')}">
									<option value="${it}">
										${it}
									</option>
								</g:each>
							</select>
						</div>
						<div class="form-group">
							<label for="datacenter">etichetta datacenter</label> <input
								type="text" name="datacenter" class="text-field form-control"
								data-validation-type="string" id="datacenter"
								placeholder="datacenter">
						</div>
						<div class="form-group">
							<label for="keyConsul">Chiave crittografica Consul</label> <input
								type="text" name="keyConsul" class="text-field form-control"
								data-validation-type="string" id="keyConsul"
								placeholder="yIetiZno0c7464rOCaIThQ==">
						</div>
						<div class="form-group">
							<label for="portaConsul">Porta TCP installazione Consul</label> <input
								type="text" name="portaConsul" class="text-field form-control"
								data-validation-type="string" id="portaConsul"
								placeholder="8500">
						</div>
						<div class="form-group">
							<label for="dnsConsul">Dominio DNS Consul</label> <input
								type="text" name="dnsConsul" class="text-field form-control"
								data-validation-type="string" id="dnsConsul"
								placeholder="ar4k.net">
						</div>
						<div class="form-group">
							<button name="_eventId" type="button"
								class="link-scroll btn btn-success btn-outline-inverse btn-lg"
								value="completato"
								onClick="document.forms['configuraConsul'].submit();hide(this);">Crea
								servizio Consul</button>
						</div>
						<a href="${createLink(event: 'indietro')}"
							class="link-scroll btn btn-info btn-outline-inverse btn-lg">Indietro
							alla schermata di benvenuto</a>
						<p>
							Nella maggior parte dei casi per connettersi ad una installazione
							si dovrà accedere via SSH ad un host.
							<g:if test="${ssh}">Sono già configurate le connessioni a ${ssh}
							</g:if>
							<br /> <a href="${createLink(event: 'configuraSSH')}"
								class="link-scroll btn btn-warning btn-outline-inverse btn-lg">Configura
								i parametri di accesso SSH</a>
						</p>
						<p>
							E' possibile utilizzare un proxy per la connessione SSH. Questa
							funzionalità permette l'utilizzo di questa interfaccia da reti
							che non accedono ad Internet direttamente.
							<g:if test="${proxies}">Sono già configurate le connessioni a ${proxies}
							</g:if>
							<br /> <a href="${createLink(event: 'configuraProxy')}"
								class="link-scroll btn btn-warning btn-outline-inverse btn-lg">Configura
								un proxy</a>
						</p>
						<p>
							E' possibile accedere ad una installazione Consul in ascolto
							sulla rete Onion o utilizzare Tor come proxy.<br />
							<g:if test="${tor}">
								<a href="${createLink(event: 'configuraOnion')}"
									class="link-scroll btn btn-info btn-outline-inverse btn-lg">Cambia
									circuito Onion</a>
							</g:if>
							<g:else>
								<a href="${createLink(event: 'configuraOnion')}"
									class="link-scroll btn btn-info btn-outline-inverse btn-lg">Attiva
									Onion</a>
							</g:else>
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

