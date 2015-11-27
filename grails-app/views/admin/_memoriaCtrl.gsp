'use strict';
/**
 * @ngdoc function
 * @name sbAdminApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the sbAdminApp
 */
angular.module('sbAdminApp')
  .controller('MemoriaCtrl', function($scope, $http) {
    $http.get("${createLink(controller:'admin',action:'listaStore',absolute:'true')}")
    .success(function (response) {$scope.storedati = response.storedati});
    
    $http.get("${createLink(controller:'documentazione',action:'memoria.md',absolute:'true')}")
    .success(function (response) {$scope.memoriaHelp = response;});    
  
  	$scope.chiave = '';
  	$scope.valore = '';
  
    $scope.nuovo=false;
    
    $scope.focusDocumentazione=false;
    
    var container = document.getElementById("jsoneditor");
    $scope.editor = new JSONEditor(container);
    
    $scope.salva = function(chiave) {
        	var link = "${createLink(controller:'admin',action:'salvaValoreKV',absolute:'true')}";
        	$http.post(link,{chiave:chiave,valore:$scope.editor.getText()})
        		.success(function(response) {
					    $http.get("${createLink(controller:'admin',action:'listaStore',absolute:'true')}")
  						.success(function (response) {$scope.storedati = response.storedati});
  			})
  				.error(function(data, status, headers, config) {
    			// called asynchronously if an error occurs
    			// or server returns response with an error status.
  			});
  	};
  	
  	$scope.cancella = function(chiave) {
        	var link = "${createLink(controller:'admin',action:'cancellaValoreKV',absolute:'true')}";
        	$http.post(link,{chiave:chiave})
        		.success(function(response) {
					    $http.get("${createLink(controller:'admin',action:'listaStore',absolute:'true')}")
  						.success(function (response) {$scope.storedati = response.storedati});
  			})
  				.error(function(data, status, headers, config) {
    			// called asynchronously if an error occurs
    			// or server returns response with an error status.
  			});
  	};
  	
  	$scope.modifica = function(chiave,valore) {
  			$scope.chiave = chiave;
  			$scope.valore = valore;
        	$scope.editor.setText(valore);
  			$scope.nuovo=true;
  			$("html, body").animate({ scrollTop: 180 }, "slow");
  	};
  	
  	$scope.salvaContestoinKV = function() {
        	var link = "${createLink(controller:'admin',action:'salvaContestoinKV',absolute:'true')}";
        	$http.get(link)
        		.success(function(response) {
					    $http.get("${createLink(controller:'admin',action:'listaStore',absolute:'true')}")
  						.success(function (response) {$scope.storedati = response.storedati});
  			})
  				.error(function(data, status, headers, config) {
    			// called asynchronously if an error occurs
    			// or server returns response with an error status.
  			});
  	};
 
  	$scope.salvasumaster = function(dato) {
        	var link = "${createLink(controller:'admin',action:'salvaContestoDaKVsuMaster',absolute:'true')}";
        	$http.post(link,{dato:dato})
        		.success(function(response) {
					    $http.get("${createLink(controller:'admin',action:'listaStore',absolute:'true')}")
  						.success(function (response) {$scope.storedati = response.storedati});
  			})
  				.error(function(data, status, headers, config) {
    			// called asynchronously if an error occurs
    			// or server returns response with an error status.
  			});
  	};
  	
  	$scope.aggiornaDaMessaggio = function() {
    	    $http.get("${createLink(controller:'admin',action:'listaStore',absolute:'true')}")
    		.success(function (response) {$scope.storedati = response.storedati});
  	};
  })
  .filter('prettyJSON', function () {
    function syntaxHighlight(json) {
      return JSON ? JSON.stringify(json, null, 5) : 'your browser doesnt support JSON so cant pretty print';
    }
    return syntaxHighlight;
});