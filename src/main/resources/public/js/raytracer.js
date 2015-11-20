angular.module('Raytracer', [])
  .controller('searchCtrl', ['$scope', '$http', function($scope, $http) {
    $scope.containerIsTop = false;
    $scope.loading = false;

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
          type: $scope.searchType
        }
      };
      $http(req)
        .success(function(response) {
          $scope.results = response;
          //$scope.loading = false;
        })
        .error(function() {
          //TODO show error message
          //$scope.loading = false;
        });
    };
  }]);
