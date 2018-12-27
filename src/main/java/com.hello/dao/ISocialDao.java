package com.hello.dao;

import com.hello.model.ChatRecord;
import com.hello.model.Subscribe;
import com.hello.model.User;
import org.apache.ibatis.annotations.Param;

/**
 * project helloSSM
 * authod wuyanhui
 * datetime 2017/11/23 16:17
 * desc
 */
public interface ISocialDao {
  public ChatRecord[] getPersonalChatRecord(String recvnam);
  public void removePersonalChatRecord(String recvname);
  public void subscribe(Subscribe subs);
  public void unSubscribe(long id);
  public Subscribe getSubscribe(@Param("scruserid")long scruserid, @Param("tousername")String tousername);
  public Subscribe[] getSubscribeList(long scruserid);
  public void setChatRecord(ChatRecord chatrecord);
}
