/**
 * Created by chovans on 15/8/15.
 */
angular.module("index.controller", [])
    .controller("indexCtrl", function ($scope, $http,$location) {
        $scope.gits = [];
        $scope.gitHttps = {
            url:"",
            submit:false
        };


        $scope.configGit = function () {
            if ($scope.gitHttps.url.indexOf("https://") != 0) {
                alert("请使用https://开头");
                return;
            }

            $scope.gitHttps.submit = true;

            $http.post("/configGit.do", {url: $scope.gitHttps.url.trim()})
                .success(function (data) {
                    if(data.errmsg == "success"){
                        $scope.loadData();
                    }
                    else{
                        alert(data.errmsg);
                    }
                });

        };


        $scope.updateGit = function(id){

            for(var i=0;i<$scope.gits.length;i++){
                if($scope.gits[i].id == id){
                    $scope.gits[i].state = 'updating';
                }
            }

            $http.post("/updateGit.do",{id:id})
                .success(function(data){
                    if(data.errmsg != "success"){
                        alert(data.errmsg);
                    }
                    $scope.loadData();
                })
        };

        $scope.deleteGit = function(id){
            $http.post("/deleteGit.do",{id:id})
                .success(function(data){
                    if(data.errmsg == "success")
                        $scope.loadData();
                })
        };


        $scope.loadData = function(){

            $scope.gitHttps = {
                url:"",
                submit:false
            };

            $http.get("/getGitProjects.do").then(
                function (response) {
                    $scope.gits = response.data;
                }
            );

            $http.get("/getInfoByTemplateName/javaGitTest").then(
                function(data){
                    console.log(data.data);
                }
            )

        };

        $scope.loadData();

    });