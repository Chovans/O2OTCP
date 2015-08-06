<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>WebSocket/SockJS</title>

    <script src="http://cdn.sockjs.org/sockjs-0.3.min.js"></script>

    <script type="text/javascript">
        var PATH = "${pageContext.request.contextPath}";
        var websocket = null;
//        if (window['WebSocket'])
        // ws://host:port/project/websocketpath
//            websocket = new WebSocket("ws://" + window.location.host + PATH + '/websocket.do');
//        else
            websocket = new SockJS('/websocket');

        websocket.onopen = function(event) {
            console.log('open', event);
            var user = {
                event:"init",
                uuid:"1111"
            };
            websocket.send(JSON.stringify(user));


            var test = {
                event:"send",
                uuid:"1111"
            };
            websocket.send(JSON.stringify(test));

        };
        websocket.onmessage = function(event) {
            console.log(event);
        };
    </script>
</head>
<body>



</body>

</html>