<!DOCTYPE html>
<html lang="en">
<head>

<meta name="layout" content="atterraggio" />

<title>Richiesta assistenza via Olark chat</title>


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

					<h2>Configurazione Annullata</h2>
					<p class="text-justify" style="text-align: justify;">
						La configurazione della piattaforma non è andata a buon fine. Se
						il browser con cui state visualizzando queste pagine accede a
						Internet, potete ricominciare la procedura con il supporto di un
						operatore. Una chat apparira nell'angolo sinistro in basso, sarà
						possibile interagire con un operatore o delegare l'operatività
						sulla console a <a href="http://www.rossonet.org" target="_new">Rossonet</a>.
					</p>
					<p>
						<a href="${createLink(event: 'indietro')}"
							class="link-scroll btn btn-success btn-outline-inverse btn-lg">Ricomincia
							la procedura di configurazione iniziale</a>
					</p>
					<p>
						<a href="${createLink(event: 'configuraCodCommerciale')}"
							class="link-scroll btn btn-success btn-outline-inverse btn-lg">Utilizza
							un codice AR4K emesso da Rossonet</a>
					</p>


				</div>
				<!-- .col-sm-10 -->
			</div>
		</div>
	</div>
	<!-- .content-wrapper -->
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
