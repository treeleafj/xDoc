package org.treeleafj.xdoc.test.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.treeleafj.xdoc.test.vo.User;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 用户模块
 *
 * @author leaf
 * @date 2017-03-03 10:11
 */
@Controller
@RequestMapping("api/user")
public class UserController {

    /**
     * 登录
     *
     * @param username 用户名|必填
     * @param password 密码
     * @return 当前登录用户的基本信息
     * @resp code 返回码(0000表示登录成功,其它表示失败)|string|必填
     * @resp msg 登录信息|string
     * @resp username 登录成功后返回的用户名|string
     */
    @ResponseBody
    @RequestMapping("login")
    public Map<String, String> login(String username, String password) {
        Map<String, String> model = new HashMap<>();
        model.put("code", "0000");
        model.put("msg", "登录成功");
        model.put("username", username);
        return model;
    }


    /**
     * 用户注册
     *
     * @param user :username 用户名|必填
     * @param user :password 密码
     * @return 注册后生成的用户的基本信息
     * @respbody {"id":"123","password":"123456","username":"admin"}
     * @title 注册
     * @see User
     */
    @ResponseBody
    @RequestMapping(value = "register", method = {RequestMethod.POST, RequestMethod.PUT})
    User register(org.treeleafj.xdoc.test.vo.User user) {
        user.setId(UUID.randomUUID().toString());
        return user;
    }
}
