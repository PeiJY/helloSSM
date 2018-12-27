package com.hello.controller.IM;


import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Component
public class SpringWebSocketHandlerInterceptor extends HttpSessionHandshakeInterceptor {

    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) throws Exception {
        // TODO Auto-generated method stub
        System.out.println("Before Handshake");
        //获取当前Session
        if (request instanceof ServletServerHttpRequest) {
            System.out.println("FINISHED");
            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
            HttpSession session = servletRequest.getServletRequest().getSession();
            if (session != null) {
                System.out.println("session != null");
            }
            else{
                System.out.println("session = null");
            }
        }
        return super.beforeHandshake(request, response, wsHandler, attributes);

    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
                               Exception ex) {
        // TODO Auto-generated method stub
        super.afterHandshake(request, response, wsHandler, ex);
    }

}