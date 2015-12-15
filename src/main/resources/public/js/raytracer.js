angular.module('Raytracer', [])
  .controller('searchCtrl', ['$scope', '$http', function($scope, $http) {
    $scope.containerIsTop = false;
    $scope.loading = false;

    //search option
    $scope.searchOptions = [{
       name: 'Events',
       value: '1'
    }, {
       name: 'Restaurants',
       value: '2'
    }];

    $scope.submitSearch = function(keyEvent) {
      if (keyEvent.which === 13)
        $scope.search();
    };

    $scope.search = function() {
      $scope.loading = true;
      if(!$scope.containerIsTop){
        $scope.containerIsTop = true;
      }
      var req = {
        method: 'POST',
        url: '/search',
        headers: {
          'Content-Type': 'application/json'
        },
        data: {
          text: $scope.searchText,
          type: $scope.searchType.value
        }
      };
      $http(req)
        .success(function(res) {
          $scope.error = null;
          $scope.results = res;
          $scope.loading = false;
        })
        .error(function(res) {
          $scope.error = 'Error: Could not get server results.';
          $scope.loading = false;
        });
    };

    $scope.loadMap = function($id, $result) {
      if($result.showMap == true){
        $result.showMap = false;
        return;
      }

      $result.showMap = true;

      setTimeout(function(){
    	var latlng;
    	if($result.latitude == 0 || $result.longitude == 0) {
    		var geocoder = new google.maps.Geocoder();
    		 geocoder.geocode({address: $result.venue, region: 'AT'}, function(results, status) {
    			 if (status == google.maps.GeocoderStatus.OK) {
    				 latlng = results[0].geometry.location;
    				 createMap(latlng);
    		     } else {
    		       alert("Geocode was not successful for the following reason: " + status);
    		     }
    		 });
    	}
    	else {
    		latlng = new google.maps.LatLng($result.latitude, $result.longitude);
    		createMap(latlng);
    	}

      }, 1000);

      function createMap(latlng) {
        	var mapProp = {
            center: latlng,
            zoom: 15,
            mapTypeId: google.maps.MapTypeId.ROADMAP
          };
          var map=new google.maps.Map(document.getElementById("map_" + $id), mapProp);

          var marker = new google.maps.Marker({
            position: latlng,
            map: map,
            title: $result.venue
          });
       }
    }
  }]);
