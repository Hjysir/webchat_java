package com.project.webchat_java.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.project.webchat_java.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface UserMapper extends BaseMapper<User> {

    @Select("SELECT * FROM users WHERE username = #{username}")
    User getUserByUsername(String username);

    @Select("SELECT * FROM users WHERE id = #{id}")
    User getUserById(String id);

    @Insert("INSERT INTO users (username, password, email, avatar, isonline) VALUES (#{userName}, #{passWord}, #{email}, 'https://userpic.codeforces.org/no-title.jpg', false)")
    void insertUser(User user);

    @Select("SELECT * FROM users WHERE email = #{email}")
    User getUserByEmail(String email);

    @Update("UPDATE users SET avatar = #{avatar} WHERE id = #{id}")
    void updateUserAvatar(String avatar, String id);

    @Update("UPDATE users SET password = #{password} WHERE id = #{userId}")
    void updateUserPassword(String password, String userId);

    @Update("UPDATE users SET isonline = false WHERE username = #{username}")
    void userLogout(String username);
}
