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
						<h2>Configurazione proxy vaso master</h2>
						<!-- Se il server non è connesso direttamente a internet -->
						<p class="text-justify" style="text-align: justify;">Il vaso
							master non è in grado di comunicare direttamente con Internet.
							Potrebbe essere necessario l'utilizzo di un proxy (http o socks)
							per permettere alla macchina di raggiungere le reti esterni. Per
							evitare che la piattaforma faccia ulteriori test sulla
							raggiungibilità di rete inserire "NO NETWORK TEST" nel campo
							proxy.</p>
						<form class="form-style validate-form clearfix" autocomplete='off'
							action="${createLink(event: 'success')}" name="configuraProxyJvm"
							method="post">
							<div class="form-group">
								<label for="proxyMaster">Indirizzo e utente</label> <input
									type="text" name="proxyMaster"
									class="text-field form-control validate-field required"
									data-validation-type="string" id="proxyMaster"
									placeholder="<http/socks5>://<utente>@<macchina>:<porta>">
							</div>
							<div class="form-group">
								<label for="passwordProxyMaster">Password</label> <input
									type="password" name="passwordProxyMaster"
									class="text-field form-control validate-field"
									data-validation-type="string" id="passwordProxyMaster"
									placeholder="Password del proxy">
							</div>
							<div class="form-group">
								<button name="_eventId" class="btn btn-sm btn-outline-inverse"
									value="configuraProxyMaster"
									onClick="document.forms['configuraProxyJvm'].submit();">Configura
									i parametri del proxy sul vaso</button>
							</div>
						</form>

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

