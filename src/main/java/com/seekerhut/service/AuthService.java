package com.seekerhut.service;

import com.seekerhut.bean.LoginReturn;
import com.seekerhut.model.mysqlModel.User;
import com.seekerhut.mySqlMapper.UserDAO;
import com.seekerhut.utils.CommonFunctions;
import com.seekerhut.utils.JedisHelper;
import com.seekerhut.utils.MailUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.apache.commons.codec.digest.DigestUtils;

import javax.annotation.Resource;
import java.lang.ref.SoftReference;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.UUID;

@Service("AuthService")
public class AuthService {
    @Resource
    private KafkaTemplate<String, Object> kafkaTemplate;
    private DateTimeFormatter yearMonthFormatter = DateTimeFormatter.ofPattern("yyyyMM");
    private UserDAO userMapper;

    public AuthService(UserDAO userMapper) {
        this.userMapper = userMapper;
    }

    public int addUser(String nickname, String userqq, String pass_sha256) {
        var qqRegLock = "RegisterQQ:" + userqq;
        qqRegLock.intern();
        synchronized (qqRegLock) {
            var isExist = userMapper.qqIsRegistered(userqq);
            if (!isExist.isEmpty()) {
                return 1;
            }
            var activateToken = UUID.randomUUID().toString().replace("-", "").substring(0, 6);
            var user = new User() {
                {
                    setUserName(nickname);
                    setUserQq(userqq);
                    setUserPass(pass_sha256);
                    setUserActivateToken(activateToken);
                    setUserIsActivate((byte)0);
                    setUserIsDeleted((byte)0);
                }
            };
            var effectLines = userMapper.insertSelective(user);
            if (effectLines > 0) {
                var receiver = userqq + "@qq.com";
                var sendResult = MailUtils.SendMail(receiver, "CyberMate注册-邮箱验证", String.format("您正在使用该邮箱对应的QQ号注册CyberMate，验证码为：%s", activateToken));
            }
            return effectLines > 0 ? 0 : -1;
        }
    }

    public int verifyUser(String userqq, String activateToken) {
        var qqRegLock = "RegisterQQ:" + userqq;
        qqRegLock.intern();
        synchronized (qqRegLock) {
            var userData = userMapper.getUserByQQAndToken(userqq, activateToken);
            if (userData != null) {
                userData.setUserIsActivate((byte)1);
                var effectLines = userMapper.updateByPrimaryKey(userData);
                return effectLines > 0 ? 0 : -1;
            } else {
                return 1;
            }
        }
    }

    public LoginReturn userLogin(String userqq, String pass_sha256) {
        var qqRegLock = "RegisterQQ:" + userqq;
        qqRegLock.intern();
        synchronized (qqRegLock) {
            var userData = userMapper.getUserByValidQQ(userqq);
            if (userData == null) {
                return new LoginReturn(1, "该用户未注册", "");
            }
            if (!userData.getUserPass().equals(pass_sha256)) {
                return new LoginReturn(2, "密码错误！", "");
            }
            var loginToken = UUID.randomUUID().toString().replace("-", "");
            JedisHelper.set("logintoken:" + userqq, loginToken);
            return new LoginReturn(0, "登录成功！", loginToken);
        }
    }
}


