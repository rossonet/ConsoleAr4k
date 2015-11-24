<!DOCTYPE html>
<html lang="en">
<head>

<meta name="layout" content="atterraggio" />

<title>Scelta contesto</title>


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
				<div class="col-sm-12 col-md-10 pull-right">
					<h2>Scegli il contesto applicativo</h2>
					<p class="text-justify" style="text-align: justify;">
						Una installazione Ar4k può ospitare più <strong>contesti</strong>.
						Un <strong>contesto</strong> associa <strong>utenti</strong>, <strong>vasi</strong>,
						<strong>connessioni</strong> e <strong>memi</strong>. Un'<strong>console</strong>
						può essere associata ad un solo <strong>contesto</strong> per
						volta. Scegliere il contesto per questa <strong>console
							Ar4k</strong> tra quelli diponibili.
					</p>

					<g:each in="[1,2]">
						<g:if test="${it}">
							<p>
								<a
									href="${createLink(event: 'scegliInterfaccia')}&contesto=${it}"
									class="link-scroll btn btn-danger btn-outline-inverse btn-lg">Scegli
									il contesto "${it}"
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

