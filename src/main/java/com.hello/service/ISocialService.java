package com.hello.service;

import com.hello.model.ChatRecord;
import com.hello.model.Subscribe;
import com.hello.model.User;

public interface ISocialService {


    public ChatRecord[] getChatRecord(User user);

    public int setChatRecord(ChatRecord cr);

    public int subscribe(User user,String name);

    public int unSubscribe(User user,String name);

    public Subscribe[] subscribeList(User user);

}
