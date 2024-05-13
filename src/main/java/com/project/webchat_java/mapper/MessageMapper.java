package com.project.webchat_java.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.project.webchat_java.entity.Message;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

import java.util.List;

public interface MessageMapper extends BaseMapper<Message> {

    @Insert("INSERT INTO messages (id, type, content, senderId, chatRoomId, timestamp, filename) VALUES (#{id}, #{type}, #{content}, #{senderId}, #{chatRoomId}, #{timestamp}, #{filename})")
    void insertMessage(Message message);

    @Select("SELECT * FROM messages WHERE chatRoomId = #{chatRoomId}")
    List<Message> getMessagesByChatRoomId(String chatRoomId);
}
