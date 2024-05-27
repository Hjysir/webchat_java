package com.project.webchat_java.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.project.webchat_java.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface UserMapper extends BaseMapper<User> {

    @Select("SELECT * FROM user " +
            "WHERE name = #{username}")
    User getUserByUsername(String username);

    @Select("SELECT * " +
            "FROM user " +
            "WHERE id = #{id}")
    User getUserById(String id);

    @Insert("INSERT INTO user (id, name, password, email, avatar, phone) " +
            "VALUES (#{id}, #{name}, #{password}, #{email}, 'https://userpic.codeforces.org/no-title.jpg', 12312341234)")
    void insertUser(User user);

    @Select("SELECT * " +
            "FROM user " +
            "WHERE email = #{email}")
    User getUserByEmail(String email);

    @Update("UPDATE user " +
            "SET avatar = #{avatar} " +
            "WHERE id = #{id}")
    void updateUserAvatar(String avatar, String id);

    @Update("UPDATE user " +
            "SET password = #{password} " +
            "WHERE id = #{userId}")
    void updateUserPassword(String password, String userId);
}
