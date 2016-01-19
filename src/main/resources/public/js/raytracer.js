angular.module('Raytracer', [])
  .controller('searchCtrl', ['$scope', '$http', function($scope, $http) {
    $scope.containerIsTop = false;
    $scope.loading = false;
    $scope.requestType = 1;
    $scope.mode = 1;

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
      var type = $scope.searchType.value;
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
          $scope.mode = 1;
          $scope.error = null;
          $scope.requestType = type;
          $scope.results = res;
          $scope.loading = false;
        })
        .error(function(res) {
          $scope.error = 'Error: Could not get server results.';
          $scope.loading = false;
        });
    };

    $scope.find = function(id, result) {
      $scope.loading = true;

      var req = {
        method: 'POST',
        url: '/find',
        headers: {
          'Content-Type': 'application/json'
        },
        data: {
          type: $scope.requestType,
          id: "" + result.id,
          radius: "1.0"
        }
      };
      $http(req)
        .success(function(res) {
          $scope.mode = 2;
          $scope.loading = false;
          $scope.error = null;

          $scope.results = [];
          $scope.results.push(result);
          $scope.findResults = res;


          if(!result.showMap){
            $scope.loadMap(id,result, $scope.requestType);
          }
          else {
            var type;
            if($scope.requestType == 1)
              type = 2;
            else {
              type = 1;
            }
            for(var i = 0; i < $scope.findResults.length; i++) {
              $scope.addMarker(result.map, $scope.findResults[i], type);
            }
          }
        })
        .error(function(res) {
          $scope.error = 'Error: Could not get server results.';
          $scope.loading = false;
        });
    };

    $scope.loadMap = function(id, result, marker) {
      if(result.showMap){
        result.showMap = !result.showMap;
        return;
      }
      result.showMap = true;

      setTimeout(function(){
        var mapProp = {
          center: new google.maps.LatLng(result.latitude, result.longitude),
          zoom: 15,
          mapTypeId: google.maps.MapTypeId.ROADMAP
        };
        result.map = new google.maps.Map(document.getElementById("map_" + id), mapProp);
        $scope.addMarker(result.map, result, $scope.requestType);
        if(marker != null){
          var type;
          if($scope.requestType == 1)
            type = 2;
          else {
            type = 1;
          }
          for(var i = 0; i < $scope.findResults.length; i++) {
            $scope.addMarker(result.map, marker[i], type);
          }
        }
      }, 1000);
    };

    $scope.addMarker = function(map, result, type){
      var latlng, image;
      if(!result || result.latitude == 0 || result.longitude == 0) {
    		var geocoder = new google.maps.Geocoder();
    		 geocoder.geocode({address: result.venue, region: 'AT'}, function(results, status) {
           if (status == google.maps.GeocoderStatus.OK) {
               alert(results);
    		       latlng = results[0].geometry.location;
    		   } else {
    		       alert("Geocode was not successful for the following reason: " + status);
    		   }
    		 });
    	}
    	else {
    		latlng = new google.maps.LatLng(result.latitude, result.longitude);
    	}
      if(type == 1){
        url = 'images/icons/ev32.png';
      }
      else{
        url = 'images/icons/d32.png';
      }
      var image = {
        url: url,
        size: new google.maps.Size(32, 32),
        origin: new google.maps.Point(32, 0),
        anchor: new google.maps.Point(16, 16)
      };
      new google.maps.Marker({
        position: latlng,
        map: map,
        title: result.venue,
        icon: image
      });
    }
  }]);
