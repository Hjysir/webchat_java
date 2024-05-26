package com.project.webchat_java.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.project.webchat_java.entity.ChatRoom;
import com.project.webchat_java.entity.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ChatRoomMapper extends BaseMapper<ChatRoom> {
    @Insert("INSERT INTO chatroom (chatname, avatar) " +
            "VALUES (#{name}, 'https://userpic.codeforces.org/no-title.jpg')")
    void CreateChatRoom(String name);

    @Delete("DELETE FROM chatroom " +
            "WHERE chatid = #{Id}")
    void deleteChatRoomById(String Id);

    @Delete("DELETE FROM chat " +
            "WHERE chatid = #{chatRoomId}")
    void deleteChatRoomRelatedById(String chatRoomId);

    @Insert("INSERT INTO chat (userid, chatid) " +
            "VALUES (#{userId}, #{chatRoomId})")
    void addUserToChatRoom(String userId, String chatRoomId);

    @Delete("DELETE FROM chat " +
            "WHERE chatid = #{chatRoomId} AND userid = #{userId}")
    void deleteUserFromChatRoom(String chatRoomId, String userId);

    @Select("SELECT * FROM chatroom " +
            "WHERE chatid = #{Id}")
    ChatRoom getChatRoomById(String Id);

    @Select("SELECT chatid " +
            "FROM chat " +
            "WHERE userid = #{userId}")
    List<String> getChatRoomsByUserId(String userId);

    @Select("SELECT userid " +
            "FROM chat " +
            "WHERE chatid = #{chatRoomId}")
    List<String> getUsersByChatRoomId(String chatRoomId);

    @Select("SELECT * " +
            "FROM chatroom " +
            "WHERE chatname = #{chatRoomName}")
    ChatRoom getChatRoomByName(String chatRoomName);
}
