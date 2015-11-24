<!DOCTYPE html>
<html lang="en">
<head>

<meta name="layout" content="atterraggio" />

<title>Configurazione accesso ssh</title>


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
					<h2>Configura l'accesso SSH al vaso master</h2>
					<p class="text-justify" style="text-align: justify;">
						Per connettersi al vaso sono necessari l'indirizzo e la porta TCP
						a cui connettersi via ssh, il nome utente con cui accedere al
						calcolatore e la chiave privata per permettere l'autenticazione.
						L'autenticazione con password non è permessa per motivi di
						sicurezza. Maggiori informazioni sulla configurazione dell'accesso
						con chiave RSA sono disponibili <a
							href="https://wiki.archlinux.org/index.php/SSH_Keys_%28Italiano%29"
							target="_new">nel wiki archlinux in Italiano.</a> La procedura
						rapida per configurare un nuovo accesso SSH su una macchina Linux
						(RedHat/Fedora/CentOS) esistente è disponibile in <a
							href="https://youtu.be/FohP0BmYhB8" target="_new">questo
							video</a>.
					</p>
					<iframe width="560" height="315"
						src="https://www.youtube.com/embed/FohP0BmYhB8" frameborder="0"
						allowfullscreen></iframe>
				</div>
				<div class="col-md-10 col-xs-12 col-sm-12 pull-right">
					<form class="form-style validate-form clearfix" autocomplete='off'
						action="${createLink(event: 'successo')}" name="vasoMaster"
						method="post">
						<div class="form-group">
							<label for="indirizzoMaster">Indirizzo macchina (ip o
								nome)</label> <input type="text" name="indirizzoMaster"
								class="text-field form-control validate-field required"
								data-validation-type="string" id="indirizzoMaster"
								placeholder="ess. 127.0.0.1">
						</div>
						<div class="form-group">
							<label for="proxyMaster">Utilizza un proxy o connettiti
								tramite TOR a rete Onion</label> <select name="proxyMaster"
								class="selectpicker text-field form-control"
								style="background-color: black; height: 54px; font-size: 0.97em; color: white;"
								id="proxyMaster">
								<option value="nessuno">Nessun proxy</option>
								<g:each in="${proxies?.split(', ')}">
									<option value="${it}">
										${it}
									</option>
								</g:each>
								<g:if test="${tor==true}">
									<option value="tor">Proxy TOR su rete Onion</option>
								</g:if>
							</select>
						</div>
						<div class="form-group">
							<label for="portaMaster">Porta accesso SSH</label> <input
								type="text" name="portaMaster"
								class="text-field form-control validate-field required"
								data-validation-type="required" id="portaMaster"
								placeholder="ess. 22">
						</div>
						<div class="form-group">
							<label for="utenteMaster">Utente accesso SSH</label> <input
								type="text" name="utenteMaster"
								class="text-field form-control validate-field required"
								data-validation-type="required" id="utenteMaster"
								placeholder="ess. root">
						</div>
						<div class="form-group">
							<label for="chiaveMaster">Chiave privata per autenticare</label>
							<textarea name="chiaveMaster"
								class="text-area form-control validate-field required"
								data-validation-type="required" id="chiaveMaster"
								placeholder='copiare la chiave privata ssh comprese le linee di intestazione e chiusura "-----BEGIN RSA PRIVATE KEY-----" e "-----END RSA PRIVATE KEY-----"'></textarea>
						</div>
						<input type="hidden" name="provenienza" value="${provenienza}">
						<div class="form-group">
							<button name="_eventId"
								class="btn btn-danger btn-sm btn-outline-inverse"
								value="verificaMaster"
								onClick="document.forms['vasoMaster'].submit();">Prova
								la connessione SSH</button>
						</div>
						<p>
							<a
								href="${createLink(event: 'indietro')}&provenienza=${provenienza}"
								class="link-scroll btn btn-warning btn-outline-inverse btn-lg">indietro</a>
						</p>
						<p>
							E' possibile utilizzare un proxy per connettersi via SSH.
							<g:if test="${proxies}">Sono già configurate le connessioni a ${proxies}
							</g:if>
							<br /> <a
								href="${createLink(event: 'configuraProxy')}&provenienza=${provenienza}"
								class="link-scroll btn btn-warning btn-outline-inverse btn-lg">Configura
								i parametri per il proxy</a>
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

