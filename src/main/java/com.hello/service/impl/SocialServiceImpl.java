package com.hello.service.impl;

import com.hello.dao.ISocialDao;
import com.hello.model.ChatRecord;
import com.hello.model.Subscribe;
import com.hello.model.User;
import com.hello.service.ISocialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * author Pei Jiyuan
 * date 2019/4/27
 * desc
 */

@Service
public class SocialServiceImpl implements ISocialService {



    @Autowired
   private ISocialDao socialDao;


    @Override
    public ChatRecord[] getChatRecord(User user){
        ChatRecord records[] = socialDao.getPersonalChatRecord(user.getUsername());
        socialDao.removePersonalChatRecord(user.getUsername());
        return records;
    }

    @Override
    public int setChatRecord(ChatRecord cr){
        socialDao.setChatRecord(cr);
        return 1;
    }

    @Override
    public int subscribe(User user,String name){
        Subscribe oldsubs = socialDao.getSubscribe(user.getId(),name);
        if(oldsubs != null) return -1;
        Subscribe subs = new Subscribe(user.getId(),name);
        socialDao.subscribe(subs);
        return 0;
    }

    @Override
    public int unSubscribe(User user,String name){
        Subscribe subs= socialDao.getSubscribe(user.getId(),name);
        if(subs == null) return-1;
        socialDao.unSubscribe(subs.getId());
        return 0;
    }

    @Override
    public Subscribe[] subscribeList(User user){
        Subscribe subs[]= socialDao.getSubscribeList(user.getId());
        return subs;
    }

}
