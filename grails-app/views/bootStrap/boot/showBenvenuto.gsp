<!DOCTYPE html>
<html lang="en">
<head>

<meta name="layout" content="atterraggio" />

<title>Wizard configurazione contesto Ar4k</title>


<style type="text/css">
.s2ui_hidden_button {
	visibility: hidden;
}

.fade {
	opacity: 0;
}

.fade.in {
	opacity: 0.9;
	-webkit-transition: opacity 5s ease-in;
	transition: opacity 5s ease-in;
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
					<h2>Configurazione iniziale console AR4K</h2>
					<p class="text-justify" style="text-align: justify;">
						La console Ar4k non è ancora configurata. Il sistema Ar4k è
						composto dalla console che genera le pagine web che state leggendo
						in questo momento e da uno o più macchine a cui la console accede
						via ssh con autenticazione tramite chiave privata. Nella
						terminologia del progetto Ar4k un <strong>vaso</strong> è un
						accesso ssh a un <strong>nodo</strong> con una specifica utenza.
						Un <strong>nodo</strong> è un sistema operativo in esecuzione su
						un server reale o virtuale.
					</p>

				</div>

				<div class="col-md-10 col-xs-12 col-sm-12 pull-right">
					<p class="text-justify" style="text-align: justify;">
						E' possibile inserire un codice di configurazione automatica
						reperibile dopo la registrazione gratuita su <a
							href="https://registrazione.ar4k.eu" target="_new">https://registrazione.ar4k.eu</a>
						per avviare questa interfaccia.
					</p>
					<p>
						<a href="${createLink(event: 'configuraCodCommerciale')}"
							class="link-scroll btn-success btn btn-outline-inverse btn-lg">Inserisci
							codice AR4K</a>
					</p>
					<p class="text-justify" style="text-align: justify;">oppure si
						può procedere alla configurazione inserendo manualmente i
						parametri. Se si vuole connettere questa console ad una
						installazione esistente</p>
					<a href="${createLink(event: 'configuraConsul')}"
						class="link-scroll btn btn-success btn-outline-inverse btn-lg">Connetti
						questa interfaccia a una istallazione esistente</a>
					<p class="text-justify" style="text-align: justify;">Per creare
						un nuovo ambiente Ar4k:</p>
					<a href="${createLink(event: 'configuraNuovoConsul')}"
						class="link-scroll btn btn-warning btn-outline-inverse btn-lg">Crea
						un nuovo ambiente Ar4k</a>
					<p class="text-justify" style="text-align: justify;">è anche
						possibile, tramite questa interfaccia, ricevere assistenza in
						tempo reale tramite un programma di chat integrato.</p>
					<a href="${createLink(event: 'fallita')}"
						class="link-scroll btn btn-danger btn-outline-inverse btn-lg">Richiedi
						assistenza</a>

					<!-- .col-sm-10 -->
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
	</script>

</body>
</html>
