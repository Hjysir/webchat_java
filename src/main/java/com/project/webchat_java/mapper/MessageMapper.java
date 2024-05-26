package com.project.webchat_java.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.project.webchat_java.entity.Message;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

import java.util.List;

public interface MessageMapper extends BaseMapper<Message> {

    @Insert("INSERT INTO messages (id, chatid, content, timestamp, messagetype, URL) " +
            "VALUES (#{id}, #{chatid}, #{content}, #{timestamp}, #{messagetype}, #{URL})")
    void insertMessage(Message message);

    @Select("SELECT * FROM messages WHERE chatid = #{chatid}")
    List<Message> getMessagesByChatRoomId(String chatid);
}
