package com.hello.controller.IM;

import com.hello.model.ChatRecord;
import com.hello.model.User;
import com.hello.service.ISocialService;
import com.hello.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@Component
public class SystemWebSocketHandler implements WebSocketHandler {
    private static HashMap<String , WebSocketSession> ChatUserMap = new HashMap<>();

    @Resource
    @Autowired
    private IUserService userService;

    @Resource
    @Autowired
    private ISocialService socialService;
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.print("连接成功");
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        String schatMessage = (String) message.getPayload();//用户输入
        TextMessage offlinemsg = new TextMessage("system,Reserver offline");
        TextMessage formatmsg = new TextMessage("system,Unknown message format");
        TextMessage idmsg = new TextMessage("system,Temporaryid illegal");
        TextMessage successmsg = new TextMessage("system,Success");
        System.out.println("用户输入" + schatMessage);
        String contents[] = schatMessage.split(",");
        if(contents.length>=2) {
            String id = contents[1];
            switch (contents[0]){
                case "login":
                    long tempoararyid = Long.parseLong(id);
                    User user = userService.findUserByTemporaryid(tempoararyid);
                    if(user==null) this.sendMessageToUser(idmsg,session);
                    else {
                        String username = user.getUsername();
                        ChatUserMap.put(username, session);
                        this.sendMessageToUser(successmsg,session);
                    }
                    break;
                case "chat":
                    String username = "";
                    for (Map.Entry<String, WebSocketSession> entry : ChatUserMap.entrySet()) {
                        if (entry.getValue().equals(session)) {
                            username=entry.getKey();
                        }
                    }
                    String recvname1 = contents[1];
                    if( ChatUserMap.get(recvname1)!=null)sendMessageToUser((message),ChatUserMap.get(recvname1));
                    else {
                        session.sendMessage(offlinemsg);
                        socialService.setChatRecord(new ChatRecord(username+","+schatMessage));
                    }
                    break;
                default: sendMessageToUser(formatmsg,session);
            }
        }
        else session.sendMessage(formatmsg);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        if (session.isOpen()) {
            session.close();
        }
        for (Map.Entry<String, WebSocketSession> entry : ChatUserMap.entrySet()) {
            if(entry.getValue().equals(session)){
                ChatUserMap.remove(session);
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        System.out.print("关闭连接    ");
        removeUser(session);
        System.out.println("用户数量: " + ChatUserMap.size());
    }

    //广播到全部用户
    private void sendMessageToUsers(WebSocketMessage<?> message) {
        for (Map.Entry<String, WebSocketSession> entry : ChatUserMap.entrySet()) {
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
    private void sendMessageToUser(WebSocketMessage<?> message,WebSocketSession session ){
        try {
            if (session.isOpen()) {
                session.sendMessage(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void removeUser(WebSocketSession session) {
        for (Map.Entry<String, WebSocketSession> entry : ChatUserMap.entrySet()) {
            if (entry.getValue().equals(session)) {
                ChatUserMap.remove(entry);
            }
        }
    }



    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

}
