angular.module('Raytracer', [])
  .controller('searchCtrl', ['$scope', '$http', function($scope, $http) {
    $scope.containerIsTop = false;

    $scope.search = function() {
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
          type: $scope.searchType
        }
      };
      $http(req)
        .success(function(response) {
          //TODO set result items here
        })
        .error(function() {
          //TODO show error message
        });
    };
  }]);
