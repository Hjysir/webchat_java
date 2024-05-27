package com.project.webchat_java.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.project.webchat_java.entity.Message;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface MessageMapper extends BaseMapper<Message> {

    @Insert("INSERT INTO message (id, chatid, content, timestamp, messagetype, URL) " +
            "VALUES (#{id}, #{chatid}, #{content}, #{timestamp}, #{messagetype}, #{URL})")
    void insertMessage(Message message);

    @Select("SELECT * FROM message " +
            "WHERE chatid = #{chatid}")
    List<Message> getMessagesByChatRoomId(String chatid);

    @Select("SELECT * FROM message " +
            "WHERE chatid = #{chatid} " +
            "AND content LIKE CONCAT('%', #{tag}, '%')")
    List<Message> getMessagesByChatRoomIdAndTag(String chatid, String tag);

    @Select("SELECT * FROM message " +
            "WHERE id = #{Id}")
    Message getMessageByUserId(String Id);
}
