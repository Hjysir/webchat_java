package com.project.webchat_java.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.project.webchat_java.entity.ChatRoom;
import com.project.webchat_java.entity.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ChatRoomMapper extends BaseMapper<ChatRoom> {
    @Insert("INSERT INTO chatrooms (name, avatar) VALUES (#{name}, 'https://userpic.codeforces.org/no-title.jpg')")
    void CreateChatRoom(String name);

    @Delete("DELETE FROM chatrooms WHERE id = #{Id}")
    void deleteChatRoomById(String Id);

    @Delete("DELETE FROM chatrooms_users WHERE chatroom_id = #{chatRoomId}")
    void deleteChatRoomRelatedById(String chatRoomId);

    @Insert("INSERT INTO chatroom_users (user_id, chatroom_id) VALUES (#{userId}, #{chatRoomId})")
    void addUserToChatRoom(String userId, String chatRoomId);

    @Delete("DELETE FROM chatroom_users WHERE chatroom_id = #{chatRoomId} AND user_id = #{userId}")
    void deleteUserFromChatRoom(String chatRoomId, String userId);

    @Select("SELECT * FROM chatrooms WHERE id = #{Id}")
    ChatRoom getChatRoomById(String Id);

    @Select("SELECT chatroom_id FROM chatroom_users WHERE user_id = #{userId}")
    List<String> getChatRoomsByUserId(String userId);

    @Select("SELECT user_id FROM chatroom_users WHERE chatroom_id = #{chatRoomId}")
    List<String> getUsersByChatRoomId(String chatRoomId);

    @Select("SELECT * FROM chatrooms WHERE name = #{chatRoomName}")
    ChatRoom getChatRoomByName(String chatRoomName);
}
