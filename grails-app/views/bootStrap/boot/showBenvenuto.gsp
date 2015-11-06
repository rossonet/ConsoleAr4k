<!DOCTYPE html>
<html lang="en">
<head>

<meta name="layout" content="atterraggio" />

<title>Benvenuti in Ar4k!</title>


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

	<div
		style="z-index: 1050; display: block; background-color: black;"
		modal-animation="true" animate="animate" index="0"
		uib-modal-window="modal-window" modal-render="true" tabindex="-1"
		role="dialog" class="modal fade ng-isolate-scope"
		uib-modal-animation-class="fade" modal-in-class="in" id="benvenutoModal">
		<div class="modal-dialog modal-lg" style="width: 90%;">
			<div class="modal-content" uib-modal-transclude="">





				<div class="col-md-10 col-xs-12 col-sm-12 pull-right">
					<h2>Configurazione iniziale console AR4K</h2>
					<p class="text-justify" style="text-align: justify;">
						La piattaforma non è ancora configurata. Il sistema Ar4k è
						composto dalla console (denominata <strong>interfaccia</strong>)
						che genera le pagine web che state leggendo in questo momento e da
						uno o più macchine a cui la console accede via ssh con
						autenticazione tramite chiave privata. Nella terminologia del
						progetto Ar4k un <strong>vaso</strong> è un accesso ssh a un <strong>nodo</strong>
						con una specifica utenza. Un <strong>nodo</strong> è un sistema
						operativo in esecuzione su un server reale o virtuale.
					</p>

				</div>
				<g:if test="${verifica==true}">
					<!-- Se il server raggiunge internet -->
					<div class="col-md-10 col-xs-12 col-sm-12 pull-right">
						<p class="text-justify" style="text-align: justify;">
							E' possibile inserire un codice di configurazione automatica
							reperibile dopo la registrazione gratuita su <a
								href="https://registrazione.ar4k.eu" target="_new">https://registrazione.ar4k.eu</a>
							per avviare questa interfaccia con una configurazione
							predefinita.
						</p>
						<p>
							<a href="${createLink(event: 'configuraCodCommerciale')}"
								class="link-scroll btn-success btn btn-outline-inverse btn-lg">Inserisci
								codice AR4K</a>
						</p>
						<p class="text-justify" style="text-align: justify;">
							oppure si può procedere alla configurazione inserendo manualmente
							i parametri.<br />Per iniziare bisogna configurare l'accesso SSH
							con un utente, non necessariamente "root", su una macchina
							Unix/Linux. Un account su una macchina linux, nella terminologia
							di Ar4k è denominato <strong>vaso</strong>. Maggiore
							documentazione è disponibile nella <a
								href="https://github.com/rossonet/agenteAr4k" target="_new">pagina
								del progetto su GitHub</a>.<strong> Nel video seguente
								viene illustrata la procedura per creare un utenza per l'accesso
								e la relativa chiave ssh. Per inserire i parametri di
								connessione, la chiave e procedere con la configurazione
								dell'interfaccia, usare il pulsante rosso sotto il video.</strong>
						</p>
						<iframe width="560" height="315"
							src="https://www.youtube.com/embed/FohP0BmYhB8" frameborder="0"
							allowfullscreen></iframe>
						<p>
							<br /> <a href="${createLink(event: 'configuraMaster')}"
								class="link-scroll btn btn-danger btn-outline-inverse btn-lg">Procedi
								nella configurazione</a>
						</p>
						<!-- .col-sm-10 -->
					</div>
				</g:if>
				<g:else>
					<!-- Se il server non è connesso direttamente a internet -->
					<div class="col-md-10 col-xs-12 col-sm-12 pull-right">
						<p class="text-justify" style="text-align: justify;">Il
							sistema AR4K non è in grado di accedere direttamente a Internet.
							Il problema potrebbe essere generato dalla configurazione di rete
							delle macchina che sta generando questa pagina web: il
							calcolatore potrebbe essere scollegata dalla rete, i server dns
							potrebbero non funzionare (se necessario si può utilizzare l'ip
							8.8.8.8 di Google o il 193.43.2.1 del Cineca) o la rete che
							ospita la macchina su cui è in esecuzione questa interfaccia
							potrebbe avvere un accesso a Internet filtrato (firewall) per
							motivi di sicurezza. E' possibile configurare un proxy, sockes o
							http, per accedere a Internet. Per evitare che la piattaforma
							faccia ulteriori verifiche sulla raggiungibilità di rete inserire
							"NO NETWORK TEST" nel campo proxy. Questa ultima opzione è
							particolarmente utile per eseguire la piattaforma su reti
							private.</p>
					</div>
					<div class="col-md-10 col-xs-12 col-sm-12 pull-right">
						<form class="form-style validate-form clearfix" autocomplete='off'
							action="${createLink(event: 'configuraProxyJvm')}"
							name="configuraProxyJvm" method="post">
							<div class="form-group">
								<label for="proxyJvm">Indirizzo e utente</label> <input
									type="text" name="proxyJvm"
									class="text-field form-control validate-field required"
									data-validation-type="string" id="proxyJvm"
									placeholder="<http/socks5>://<utente>@<macchina>:<porta>">
							</div>
							<div class="form-group">
								<label for="passwordJvm">Password</label> <input type="password"
									name="passwordJvm"
									class="text-field form-control validate-field"
									data-validation-type="string" id="passwordJvm"
									placeholder="Password del proxy">
							</div>
							<div class="form-group">
								<button name="_eventId" class="btn btn-sm btn-outline-inverse"
									value="verificaMaster"
									onClick="document.forms['configuraProxyJvm'].submit();">Configura
									i parametri del proxy</button>
							</div>
						</form>
					</div>

				</g:else>
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
