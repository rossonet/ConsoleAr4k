'use strict';
/**
 * @ngdoc function
 * @name sbAdminApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the sbAdminApp
 */
angular.module('sbAdminApp')
  .controller('OggettiCtrl', function($scope, $http, $sce) {
    $http.get("${createLink(controller:'admin',action:'listaVasi',absolute:'true')}")
    .success(function (response) {$scope.vasi = response.vasi;});
    
    $http.get("${createLink(controller:'documentazione',action:'vaso.md',absolute:'true')}")
    .success(function (response) {$scope.vasoHelp = response;});    
  
  	// appare nuovo vaso
    $scope.nuovo=false;
    // appare documentazione
    $scope.focusDocumentazione=false;
    //appare scansione rete
    $scope.scansioneRete = false;
    //appare scansione vaso
    $scope.scansioneVaso = false;
    //appare aggiungi a meme
    $scope.assegnaMeme = false;
    //appare con il terminale
    $scope.terminale = false;
    $scope.iframeTerminale = $sce.trustAsResourceUrl('http://prometeo.rossonet.net:8080/');
    
    //vaso in modifica o nuovo;
    $scope.vaso = null;
 
 	//scansione del vaso esecuzione
    $scope.scansioneVasoF = function(vaso) {
    	$scope.scansioneVaso = true;
    }
    
    //scansione rete apertura modal
    $scope.scansioneReteF = function(vaso) {
    	$scope.scansioneRete = true;
    }
    
    //assegna a meme apertura modal
    $scope.assegnaMemeF = function(vaso) {
    	$scope.assegnaMeme = true;
    }
    
    //scansiona in modalità veloce
    $scope.scansioneReteVeloceE = function(vaso) {
    	//
    }
 
    //scansiona in modalità nascosta
    $scope.scansioneReteNascostaE = function(vaso) {
    	//
    }
    
    //terminale
    $scope.terminaleA = function(vaso) {
    	$scope.terminale = true;
    }
    
    // crea un nuovo vaso testando la connessione prima
    $scope.nuovoVaso = function(vaso) {
        $http.post("${createLink(controller:'admin',action:'aggiungiVaso',absolute:'true')}", {vaso:vaso})
        .success(function(response) {
    		    $http.get("${createLink(controller:'admin',action:'listaVasi',absolute:'true')}")
    			.success(function (response) {$scope.vasi = response.vasi;});
  		})
  		.error(function(data, status, headers, config) {
    		// called asynchronously if an error occurs
    		// or server returns response with an error status.
  		});
      };
    
    // testa e modifica un vaso esistente. Alla fine della procedura le connessioni saranno sulle nuove coordinate  
    $scope.modifica = function(vaso) {
    	$scope.nuovo = true;
    	$("html, body").animate({ scrollTop: 160 }, "slow");
    } 
      
    // elimina una connessione
    $scope.eliminaVaso = function(vaso) {
    	$http.post("${createLink(controller:'admin',action:'eliminaVaso',absolute:'true')}", {vaso:vaso})
        .success(function(response) {
    		    $http.get("${createLink(controller:'admin',action:'listaVasi',absolute:'true')}")
    			.success(function (response) {$scope.vasi = response.vasi;});
  		})
  	  };
  	  
  });