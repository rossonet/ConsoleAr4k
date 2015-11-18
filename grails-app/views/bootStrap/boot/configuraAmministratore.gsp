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
				<div class="col-sm-10 col-md-9 pull-right">

					<section class="feature-text">
						<h2>Configura un accesso amministrativo per Ar4k</h2>
						<p class="text-justify" style="text-align: justify;">Il
							contesto scelto non ha nessun utente configurato. Per completare
							la procedura è necessario configurare un amministratore che avrà
							tutti i permessi su questo contesto.
						<p>
						<form class="form-style validate-form clearfix" autocomplete='off'
							action="${createLink(event: 'completata')}"
							name="configuraUtenteDemo" method="post">
							<div class="form-group">
								<label for="utenteDemo">Nome utente desiderato</label> <input
									type="text" name="utenteDemo"
									class="text-field form-control validate-field"
									data-validation-type="string" id="utenteDemo"
									placeholder="ess. rossonet">
							</div>
							<div class="form-group">
								<label for="passwordDemo1">Password</label> <input
									type="password" name="passwordDemo1"
									class="text-field form-control validate-field"
									data-validation-type="string" id="passwordDemo1"
									placeholder="Password">
							</div>
							<div class="form-group">
								<label for="passwordDemo2">Password (controllo)</label> <input
									type="password" name="passwordDemo2"
									class="text-field form-control validate-field"
									data-validation-type="string" id="passwordDemo2"
									placeholder="Password ripetuta">
							</div>
							<div class="form-group">
								<button name="_eventId"
									class="btn btn-success btn-sm btn-outline-inverse"
									value="completata"
									onClick="document.forms['configuraUtenteDemo'].submit();">Aggiungi
									l'utente e avvia l'interfaccia</button>
							</div>
						</form>
						<p>
							<a href="${createLink(event: 'fallita')}"
								class="link-scroll btn-danger btn btn-outline-inverse btn-lg">Interrompi
								la procedura di configurazione</a>
						</p>
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

