package org.treeleafj.xdoc.test.controller;

import com.jfinal.core.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class UserController extends Controller {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 登录
     *
     * @param :username 用户名|必填
     * @param :password 密码
     * @return 当前登录用户的基本信息
     * @resp code 返回码(0000表示登录成功,其它表示失败)|string|必填
     * @resp msg 登录信息|string
     * @resp username 登录成功后返回的用户名|string
     */
    public void login() {

        String username = this.get("username");
        String password = this.get("password");

        logger.info("username={}, passowrd={}", username, password);

        Map<String, String> model = new HashMap<>();
        model.put("code", "0000");
        model.put("msg", "登录成功");
        model.put("username", username);
        this.renderJson(model);
    }


    /**
     * 用户注册
     *
     * @param :username 用户名|必填
     * @param :password 密码
     * @return 注册后生成的用户的基本信息
     * @respbody {"id":"123","password":"123456","username":"admin"}
     * @title 注册
     * @see User
     * @resp score 分数
     */
    public void register() {
        User user = getModel(User.class);
        user.setId(UUID.randomUUID().toString());
        this.renderJson(user);
    }
}
