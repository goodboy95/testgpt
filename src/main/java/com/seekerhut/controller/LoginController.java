package com.seekerhut.controller;

import com.seekerhut.model.mysqlModel.User;
import com.seekerhut.service.AuthService;
import com.seekerhut.utils.CommonFunctions;
import com.seekerhut.utils.JedisHelper;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/jsapi/login")
public class LoginController extends BaseController {
    private AuthService authService;
    public LoginController(AuthService authService) {
        this.authService = authService;
    }

    @CrossOrigin
    @PostMapping("user_register")
    @ApiImplicitParams({
            //@ApiImplicitParam(name = "replyJson", value = "", paramType = "body", dataType = "String"),
    })
    public @ResponseBody String register(@RequestBody Map<String, String> payload) {
        try {
            String nickname = payload.get("nickname");
            String userqq = payload.get("userqq");
            String password = payload.get("password");
            Pattern pattern = Pattern.compile("^\\d{5,12}$");
            Matcher matcher = pattern.matcher(userqq);
            if (!matcher.matches()) {
                return Fail(2, String.format("您输入的QQ号：\"%s\"，不是正确QQ号（可能情况：非纯数字、过短/过长），请修改后重新提交。", userqq));
            }
            var passSha256 = CommonFunctions.getSHA256Hash(password);
            var addResult = authService.addUser(nickname, userqq, passSha256);
            if (addResult < 0) {
                return Fail(-1, "注册用户时出现未知异常，请联系管理员处理。");
            } else if (addResult > 0) {
                return Fail(1, "该用户已被他人注册，并完成邮箱验证。如果该QQ确实是您的，请联系管理员处理。");
            } else {
                return Success("用户已完成注册，请前往QQ邮箱进行邮箱验证");
            }
        }
        catch (Exception e) {
            return Fail(-1, "msg: " + e.getMessage() + ", stack: " + String.join("\n", Arrays.stream(e.getStackTrace()).map(s -> s.toString()).collect(Collectors.toList())));
        }
    }

    @CrossOrigin
    @PostMapping("user_verify")
    @ApiImplicitParams({
            //@ApiImplicitParam(name = "replyJson", value = "", paramType = "body", dataType = "String"),
    })
    public @ResponseBody String userVerify(@RequestBody Map<String, String> payload) {
        try {
            String userqq = payload.get("userqq");
            String verifyCode = payload.get("verifyCode");
            Pattern pattern = Pattern.compile("^\\d{5,12}$");
            Matcher matcher = pattern.matcher(userqq);
            if (!matcher.matches()) {
                return Fail(2, String.format("您输入的QQ号：\"%s\"，不是正确QQ号（可能情况：非纯数字、过短/过长），请修改后重新提交。", userqq));
            }
            var verifyResult = authService.verifyUser(userqq, verifyCode);
            if (verifyResult < 0) {
                return Fail(-1, "邮箱验证时出现未知异常，请联系管理员处理。");
            } else if (verifyResult > 0) {
                return Fail(1, "验证码输入有误，请重新输入。");
            } else {
                return Success("用户已完成注册，请登录");
            }
        }
        catch (Exception e) {
            return Fail(-1, "msg: " + e.getMessage() + ", stack: " + String.join("\n", Arrays.stream(e.getStackTrace()).map(s -> s.toString()).collect(Collectors.toList())));
        }
    }

    @CrossOrigin
    @PostMapping("user_login")
    @ApiImplicitParams({
            //@ApiImplicitParam(name = "replyJson", value = "", paramType = "body", dataType = "String"),
    })
    public @ResponseBody String UserLogin(@RequestBody Map<String, String> payload, HttpServletRequest request, HttpServletResponse response) {
        try {
            String userqq = payload.get("userqq");
            String password = payload.get("password");
            var passSha256 = CommonFunctions.getSHA256Hash(password);
            var loginResponse = authService.userLogin(userqq, passSha256);
            if (loginResponse.getCode() != 0) {
                return Fail(loginResponse.getCode(), loginResponse.getMsg());
            } else {
                var token = loginResponse.getLoginToken();
                var cookie = new Cookie("userqq", userqq);
                cookie.setPath("/");
                var cookie2 = new Cookie("logintoken", token);
                cookie2.setPath("/");
                response.addCookie(cookie);
                response.addCookie(cookie2);
                return Success("ok");
            }
        }
        catch (Exception e) {
            return Fail(-1, "msg: " + e.getMessage() + ", stack: " + String.join("\n", Arrays.stream(e.getStackTrace()).map(s -> s.toString()).collect(Collectors.toList())));
        }
    }
}
