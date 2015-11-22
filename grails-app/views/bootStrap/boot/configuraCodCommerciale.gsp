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
					<h2>AR4K by Rossonet</h2>
					<p class="text-justify" style="text-align: justify;">
						<a href="http://www.rossonet.org" target="_new">Rossonet</a> offre
						servizi cloud, assistenza e moduli aggiuntivi per il prodotto open
						source Ar4k. Maggiori dettagli nel nostro <a
							href="http://www.rossonet.org/" target="_new">sito internet.</a>
					<p>
					<form class="form-style validate-form clearfix" autocomplete='off'
						action="${createLink(event: 'completata')}"
						name="configuraCodCommerciale" method="post">
						<div class="form-group">
							<label for="codCommerciale">Codice AR4K</label> <input
								type="text" name="codCommerciale"
								class="text-field form-control validate-field required"
								data-validation-type="string" id="codCommerciale"
								placeholder="Inserire il cod.ar4k ricevuto per email">
						</div>
						<div class="form-group">
							<button name="_eventId"
								class="link-scroll btn btn-success btn-outline-inverse btn-lg"
								value="completata"
								onClick="document.forms['configuraCodCommerciale'].submit();">Attiva
								l'interfaccia AR4K con questo codice</button>
						</div>
						<p>
							<a href="${createLink(event: 'indietro')}"
								class="link-scroll btn btn-warning btn-outline-inverse btn-lg">...ricomincia
								la procedura di boot di questa interfaccia AR4K</a>
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

