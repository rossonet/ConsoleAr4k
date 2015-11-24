<!DOCTYPE html>
<html lang="en">
<head>

<meta name="layout" content="atterraggio" />

<title>Scelta interfaccia</title>


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


					<h2>Scegli l'interfaccia applicativa</h2>
					<p class="text-justify" style="text-align: justify;">Un
						contesto dispone di varie impostazioni grafiche per la Console.
						Scegliere una impostazione per questa installazione.</p>
					<g:each in="[1,2]">
						<g:if test="${it}">
							<p>
								<a href="${createLink(event: 'avvia')}&interfaccia=${it}"
									class="link-scroll btn btn-danger btn-outline-inverse btn-lg">Scegli
									l'interfaccia "${it}"
								</a>
							</p>
						</g:if>
					</g:each>

					<p>
						<a
							href="${createLink(event: 'indietro')}&provenienza=${provenienza}"
							class="link-scroll btn btn-warning btn-outline-inverse btn-lg">indietro</a>
					</p>

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

