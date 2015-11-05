<!DOCTYPE html>
<html lang="en">
<head>

<meta name="layout" content="atterraggio" />

<title><g:message code='spring.security.ui.login.title' /></title>


<style type="text/css">
.s2ui_hidden_button {
	visibility: hidden;
}
</style>

</head>

<body>

	<!-- #main-content -->
	<script>
		$(document).ready(function() {
			scroll_to_top();
		});
	</script>

	<article id="bootstrap" class="section-wrapper clearfix"
		data-custom-background-img="${resource(dir: 'atterraggio', file: 'images/other_images/bg4.jpg')}">
		<div class="content-wrapper clearfix">
			<div class="col-sm-10 col-md-9 pull-right">

				<section class="feature-text">
					<h2>Scegli l'interfaccia applicativa</h2>
					<p class="text-justify" style="text-align: justify;">Ogni
						contesto dispone di varie interfacce, scegli quale utilizzare tra
						quelle diponibili.</p>
					<g:each in="${listaInterfacce}">
						<g:if test="${it}">
							<p>
								<a
									href="${createLink(event: 'provaUtente')}&interfaccia=${it.id}"
									class="link-scroll btn btn-danger btn-outline-inverse btn-lg">Scegli
									l'interfaccia "${it.descrizione?:it.id}"
								</a>
							</p>
						</g:if>
					</g:each>
				</section>

			</div>
			<!-- .col-sm-10 -->
		</div>
		<!-- .content-wrapper -->
	</article>

</body>
</html>

