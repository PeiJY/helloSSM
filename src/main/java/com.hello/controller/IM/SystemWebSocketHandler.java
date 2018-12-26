package com.hello.controller.IM;

import com.hello.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Component
//@WebAppConfiguration("src/main/resources")
//@Service
public class SystemWebSocketHandler implements WebSocketHandler {
    //private static ArrayList<WebSocketSession> users = new ArrayList<WebSocketSession>();
    private static HashMap<Integer , WebSocketSession> UserMap = new HashMap<>();
    @Resource
    @Autowired
    private IUserService userService;


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.print("连接成功");
        //users.add(session);
        UserMap.put((Integer)session.getAttributes().get("userId"),session);
        //for(WebSocketSession a : users){
        //    System.out.println(a.getAttributes());
       //}
        Random random = new Random();
        //System.out.println("当前用户数量: " + users.size());
        System.out.println("当前用户数量: " + UserMap.size());
        String username= (String) session.getAttributes().get("WEBSOCKET_USERNAME");
        int userId=random.nextInt(1000);//Integer)session.getAttributes().get("userId");
        System.out.println("用户名 "+username);
        System.out.println("用户ID "+userId);
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        String schatMessage = (String) message.getPayload();//用户输入
        System.out.println("用户输入" + schatMessage);
        sendMessageToUsers(message);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        if (session.isOpen()) {
            session.close();
        }
        //users.remove(session);
        for (Map.Entry<Integer, WebSocketSession> entry : UserMap.entrySet()) {
            if(entry.getValue().equals(session)){
                UserMap.remove(session);
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        System.out.print("关闭连接    ");
        removeUser(session);
        //System.out.println("用户数量: " + users.size());
        System.out.println("用户数量: " + UserMap.size());
    }
    private void sendMessageToUsers(WebSocketMessage<?> message) {
        /*for (WebSocketSession user : users) {
            try {
                if (user.isOpen()) {
                    user.sendMessage(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/
        for (Map.Entry<Integer, WebSocketSession> entry : UserMap.entrySet()) {
            System.out.println(entry.getKey());
            System.out.println(entry.getValue());
            WebSocketSession session = entry.getValue();
            try {
                if (session.isOpen()) {
                    session.sendMessage(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    private void sendMessageToUser(WebSocketMessage<?> message,int userId){
        String content=(String)message.getPayload();
        String contents[] = content.split(",");
        WebSocketSession session = UserMap.get(contents[0]);
        //session.sendMessage(contents[1]);
    }

    private void removeUser(WebSocketSession session) {
        //users.remove(session);
        //users.remove(session.getId());
        for (Map.Entry<Integer, WebSocketSession> entry : UserMap.entrySet()) {
            if (entry.getValue().equals(session)) {
                UserMap.remove(session);
            }
        }
    }



    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

}
