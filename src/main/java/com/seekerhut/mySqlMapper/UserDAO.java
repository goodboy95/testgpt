package com.seekerhut.mySqlMapper;

import com.seekerhut.model.mysqlModel.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * UserDAO继承基类
 */
@Repository
public interface UserDAO extends MyBatisBaseDao<User, Integer> {
    public Optional<Integer> qqIsRegistered(@Param("userQQ") String qq);

    public User getUserByQQAndToken(@Param("userQQ") String qq, @Param("token") String token);

    public User getUserByValidQQ(@Param("userQQ") String qq);
}