'use strict';
/**
 * @ngdoc overview
 * @name sbAdminApp
 * @description
 * # sbAdminApp
 *
 * Modulo principale by Ambrosini
 */
angular
  .module('sbAdminApp', [
    'oc.lazyLoad',
    'ui.router',
    'ui.bootstrap',
    //'ngAnimate',
    'angular-loading-bar',
    'hc.marked',
    'restangular'
  ])
  .config(['$stateProvider','$urlRouterProvider','$ocLazyLoadProvider',function ($stateProvider,$urlRouterProvider,$ocLazyLoadProvider) {
    
    $ocLazyLoadProvider.config({
      debug:true,
      events:true,
    });

    $urlRouterProvider.otherwise('/dashboard/dashrossonet');

    $stateProvider
      .state('dashboard', {
        url:'/dashboard',
        templateUrl: "${resource(dir: 'admin', file: 'app/views/dashboard/main.html')}",
        resolve: {
            loadMyDirectives:function($ocLazyLoad){
                return $ocLazyLoad.load(
                {
                    name:'sbAdminApp',
                    files:[
                    "${resource(dir: 'admin', file: 'app/scripts/directives/header/header.js')}",
                    "${resource(dir: 'admin', file: 'app/scripts/directives/header/header-notification/header-notification.js')}",
                    "${resource(dir: 'admin', file: 'app/scripts/directives/sidebar/sidebar.js')}",
                    "${resource(dir: 'admin', file: 'app/scripts/directives/sidebar/sidebar-search/sidebar-search.js')}"
                    ]
                }),
                $ocLazyLoad.load(
                {
                   name:'toggle-switch',
                   files:["${resource(dir: 'bower_components', file: 'angular-toggle-switch/angular-toggle-switch.min.js')}",
                          "${resource(dir: 'bower_components', file: 'angular-toggle-switch/angular-toggle-switch.css')}"
                      ]
                }),
                $ocLazyLoad.load(
                {
                  name:'ngAnimate',
                  files:["${resource(dir: 'bower_components', file: 'angular-animate/angular-animate.js')}"]
                })
                $ocLazyLoad.load(
                {
                  name:'ngCookies',
                  files:["${resource(dir: 'bower_components', file: 'angular-cookies/angular-cookies.js')}"]
                })
                $ocLazyLoad.load(
                {
                  name:'ngSanitize',
                  files:["${resource(dir: 'bower_components', file: 'angular-sanitize/angular-sanitize.js')}"]
                })
                $ocLazyLoad.load(
                {
                  name:'ngTouch',
                  files:["${resource(dir: 'bower_components', file: 'angular-touch/angular-touch.js')}"]
                })
            }
        }
    })
      .state('dashboard.home',{
        url:'/home',
        controller: 'MainCtrl',
        templateUrl:"${resource(dir: 'admin', file: 'app/views/dashboard/home.html')}",
        resolve: {
          loadMyFiles:function($ocLazyLoad) {
            return $ocLazyLoad.load({
              name:'sbAdminApp',
              files:[
              "${resource(dir: 'admin', file: 'app/scripts/controllers/main.js')}",
              "${resource(dir: 'admin', file: 'app/scripts/directives/timeline/timeline.js')}",
              "${resource(dir: 'admin', file: 'app/scripts/directives/chat/chat.js')}",
              "${resource(dir: 'admin', file: 'app/scripts/directives/dashboard/stats/stats.js')}"
              ]
            })
          }
        }
      })
      .state('dashboard.api',{
        url:'/apiAr4k',
        controller: 'ApiAr4kCtrl',
        templateUrl:"${resource(dir: 'admin', file: 'apiAr4k')}",
        resolve: {
          loadMyFiles:function($ocLazyLoad) {
            return $ocLazyLoad.load({
              name:'sbAdminApp',
              files:[
              "${resource(dir: 'admin', file: 'apiAr4kCtrl')}"
              ]
            })
          }
        }
      })
      .state('dashboard.rossonet',{
        url:'/rossonet',
        controller: 'RossonetCtrl',
        templateUrl:"${resource(dir: 'admin', file: 'rossonet')}",
        resolve: {
          loadMyFiles:function($ocLazyLoad) {
            return $ocLazyLoad.load({
              name:'sbAdminApp',
              files:[
              "${resource(dir: 'admin', file: 'rossonetCtrl')}"
              ]
            })
          }
        }
      })
      .state('dashboard.oggetti',{
        url:'/oggetti',
        controller: 'OggettiCtrl',
        templateUrl:"${resource(dir: 'admin', file: 'oggetti')}",
        resolve: {
          loadMyFiles:function($ocLazyLoad) {
            return $ocLazyLoad.load({
              name:'sbAdminApp',
              files:[
              "${resource(dir: 'admin', file: 'oggettiCtrl')}"
              ]
            })
          }
        }
      })
      .state('dashboard.utenti',{
        url:'/utenti',
        controller: 'UtentiCtrl',
        templateUrl:"${resource(dir: 'admin', file: 'utenti')}",
        resolve: {
          loadMyFiles:function($ocLazyLoad) {
            return $ocLazyLoad.load({
              name:'sbAdminApp',
              files:[
              "${resource(dir: 'admin', file: 'utentiCtrl')}"
              ]
            })
          }
        }
      })
     .state('dashboard.dashrossonet',{
        url:'/dashrossonet',
        controller: 'DashRossonetCtrl',
        templateUrl:"${resource(dir: 'admin', file: 'dashrossonet')}",
        resolve: {
          loadMyFiles:function($ocLazyLoad) {
            return $ocLazyLoad.load({
              name:'sbAdminApp',
              files:[
              "${resource(dir: 'admin', file: 'dashrossonetCtrl')}"
              ]
            })
          }
        }
      })
      .state('dashboard.memi',{
        url:'/memi',
        controller: 'MemiCtrl',
        templateUrl:"${resource(dir: 'admin', file: 'memi')}",
        resolve: {
          loadMyFiles:function($ocLazyLoad) {
            return $ocLazyLoad.load({
              name:'sbAdminApp',
              files:[
              "${resource(dir: 'admin', file: 'memiCtrl')}"
              ]
            })
          }
        }
      })
      .state('dashboard.memoria',{
        url:'/memoria',
        controller: 'MemoriaCtrl',
        templateUrl:"${resource(dir: 'admin', file: 'memoria')}",
        resolve: {
          loadMyFiles:function($ocLazyLoad) {
            return $ocLazyLoad.load({
              name:'sbAdminApp',
              files:[
              "${resource(dir: 'admin', file: 'memoriaCtrl')}"
              ]
            })
          }
        }
      })
      .state('dashboard.ricettari',{
        url:'/ricettari',
        controller: 'RicettariCtrl',
        templateUrl:"${resource(dir: 'admin', file: 'ricettari')}",
        resolve: {
          loadMyFiles:function($ocLazyLoad) {
            return $ocLazyLoad.load({
              name:'sbAdminApp',
              files:[
              "${resource(dir: 'admin', file: 'ricettariCtrl')}"
              ]
            })
          }
        }
      })
      .state('dashboard.reti',{
        url:'/reti',
        controller: 'RetiCtrl',
        templateUrl:"${resource(dir: 'admin', file: 'reti')}",
        resolve: {
          loadMyFiles:function($ocLazyLoad) {
            return $ocLazyLoad.load({
              name:'sbAdminApp',
              files:[
              "${resource(dir: 'admin', file: 'retiCtrl')}"
              ]
            })
          }
        }
      })
      .state('dashboard.processi',{
        url:'/processi',
        controller: 'ProcessiCtrl',
        templateUrl:"${resource(dir: 'admin', file: 'processi')}",
        resolve: {
          loadMyFiles:function($ocLazyLoad) {
            return $ocLazyLoad.load({
              name:'sbAdminApp',
              files:[
              "${resource(dir: 'admin', file: 'processiCtrl')}"
              ]
            })
          }
        }
      })
      .state('dashboard.form',{
        templateUrl:"${resource(dir: 'admin', file: 'app/views/form.html')}",
        url:'/form'
    })
      .state('dashboard.blank',{
        templateUrl:"${resource(dir: 'admin', file: 'app/views/pages/blank.html')}",
        url:'/blank'
    })
      .state('login',{
        templateUrl:"${resource(dir: 'admin', file: 'app/views/pages/login.html')}",
        url:'/login'
    })
      .state('dashboard.chart',{
        templateUrl:"${resource(dir: 'admin', file: 'app/views/chart.html')}",
        url:'/chart',
        controller:'ChartCtrl',
        resolve: {
          loadMyFile:function($ocLazyLoad) {
            return $ocLazyLoad.load(
                "${resource(dir: 'bower_components', file: 'Chart.js/Chart.min.js')}"
            ),
            $ocLazyLoad.load({
              name:'chart.js',
              files:[
                "${resource(dir: 'bower_components', file: 'angular-chart.js/dist/angular-chart.min.js')}",
                "${resource(dir: 'bower_components', file: 'angular-chart.js/dist/angular-chart.css')}"
              ]
            }),
            $ocLazyLoad.load({
                name:'sbAdminApp',
                files:["${resource(dir: 'admin', file: 'app/scripts/controllers/chartContoller.js')}"]
            })
          }
        }
    })
      .state('dashboard.table',{
        templateUrl:"${resource(dir: 'admin', file: 'app/views/table.html')}",
        url:'/table'
    })
      .state('dashboard.panels-wells',{
          templateUrl:"${resource(dir: 'admin', file: 'app/views/ui-elements/panels-wells.html')}",
          url:'/panels-wells'
      })
      .state('dashboard.buttons',{
        templateUrl:"${resource(dir: 'admin', file: 'app/views/ui-elements/buttons.html')}",
        url:'/buttons'
    })
      .state('dashboard.notifications',{
        templateUrl:"${resource(dir: 'admin', file: 'app/views/ui-elements/notifications.html')}",
        url:'/notifications'
    })
      .state('dashboard.typography',{
       templateUrl:"${resource(dir: 'admin', file: 'app/views/ui-elements/typography.html')}",
       url:'/typography'
   })
      .state('dashboard.icons',{
       templateUrl:"${resource(dir: 'admin', file: 'app/views/ui-elements/icons.html')}",
       url:'/icons'
   })
      .state('dashboard.grid',{
       templateUrl:"${resource(dir: 'admin', file: 'app/views/ui-elements/grid.html')}",
       url:'/grid'
   })
  }])
.controller('taskCtrl', function($scope,$http,$filter,$window) {
	$scope.pannelloMain=false;
	$scope.focusMain='';
	
    $scope.svolgiistanza = function(idTask) {
       	$scope.focusMain = "${createLink(controller:'admin',action:'taskProcessoForm',absolute:'true')}?idTask="+idTask;
       	$scope.pannelloMain = true;
       	};

    $http.get("${createLink(controller:'admin',action:'listaTask',absolute:'true')}")
    	.success(function (response) {
    		$scope.tasks = response.tasks;
    		$scope.numeroTasks = response.conto;
    		}); 
    $scope.aggiornaDaMessaggio = function() {
    	$http.get("${createLink(controller:'admin',action:'listaTask',absolute:'true')}")
    		.success(function (response) {
    			$scope.tasks = response.tasks;
    			$scope.numeroTasks = response.conto;
    			}); 
  		};
  	$scope.salvaConfigurazioneInterfaccia = function() {
    	$http.get("${createLink(controller:'admin',action:'salvaConfigurazioneInterfaccia',absolute:'true')}")
    		.success(function (response) {
    			alert("Configurazione salvata con risultato: "+response);
    			}); 
  		};
  	$scope.scaricaConfigurazioneInterfaccia = function() {
    	$http.get("${createLink(controller:'admin',action:'scaricaConfigurazioneInterfaccia',absolute:'true')}")
    		.success(function (response) {$window.open("${createLink(controller:'admin',action:'scaricaConfigurazioneInterfaccia',absolute:'true')}");}); 
  		};
});
    
