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
	-webkit-transition: opacity 5s ease-in;
	transition: opacity 5s ease-in;
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

					<h2>Configurazione Completata</h2>
					<p class="text-justify" style="text-align: justify;">
						<i class="fa fa-check fa-3x"></i> La console è correttamente
						configurata.
					</p>
					<p>
						<a href="" onClick="fine();"
							class="link-scroll btn btn-success btn-outline-inverse btn-lg">Inizia
							ad usare Ar4k</a>
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

		function fine() {
			var d = document.getElementById("benvenutoModal");
			d.className = "modal fade ng-isolate-scope";
			setTimeout(function(){ window.location = "${createLink(event: 'completata')}
		";
			}, 6000);
		}
	</script>
</body>
</html>
