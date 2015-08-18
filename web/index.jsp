<%@ page language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html ng-app="dotnarSPA">
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta http-equiv="Content-Type" content="text/html;charset=UTF-8"/>
    <title>Dotnar</title>
    <link rel="stylesheet" href="css/bootstrap-theme.min.css"/>
    <link rel="stylesheet" href="css/bootstrap.min.css"/>
    <link rel="stylesheet" href="css/style.css"/>
    <link rel="stylesheet" href="css/animate.css"/>
</head>
<body>


<div class="container-fluid" ng-controller="indexCtrl">
    <div class="row col-lg-12">
        <table class="table table-hover table-responsive">
            <thead>
            <tr>
                <td><h4>地址</h4></td>
                <td><h4>操作</h4></td>
                <td><h4>最后更新时间</h4></td>
            </tr>
            </thead>
            <tbody>
            <tr ng-repeat="git in gits">
                <td>
                    <span ng-bind="git.url"></span>&nbsp;&nbsp;&nbsp;(项目名称：<span ng-bind="git.name"></span>)
                </td>
                <td>
                    <button class="btn btn-danger" disabled="disabled" ng-if="git.state== 'downing' ">
                        正在下载中...
                    </button>
                    <button class="btn btn-danger" disabled="disabled" ng-if="git.state== 'updating' ">
                        正在更新中...
                    </button>
                    <button class="btn btn-default" ng-click="updateGit(git.id)" ng-if="git.state== 'done'">
                        更新
                    </button>
                    <button class="btn btn-default">
                        删除
                    </button>
                </td>
                <td>
                    {{git.updateOn | date:'yyyy-MM-dd HH:mm:ss'}}
                </td>
            </tr>
            <tr>
                <td>
                    <form class="form-inline">
                        <div class="form-group col-lg-12">
                            <div class="input-group col-lg-12">
                                <input type="text" class="form-control" ng-model="gitHttps.url" placeholder="https://">
                            </div>
                        </div>
                    </form>
                </td>
                <td>
                    <button class="btn btn-default" ng-click="configGit()" ng-if="!gitHttps.submit">
                        提交
                    </button>
                    <button class="btn btn-default" ng-click="configGit()" disabled="disabled" ng-if="gitHttps.submit">
                        提交中
                    </button>
                </td>
                <td></td>
            </tr>
            </tbody>
        </table>
    </div>
</div>


<script src="js/jquery-2.1.4.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/angular/angular.js"></script>
<script src="js/angular/angular-sanitize.min.js"></script>
<script src="js/angular/angular-route.min.js"></script>
<script src="js/angular/angular-animate.min.js"></script>
<%--mainjs--%>
<script src="js/app.js"></script>
<script src="js/controller/index.controller.js"></script>

</body>
</html>