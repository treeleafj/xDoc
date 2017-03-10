package org.treeleafj.xdoc.test.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.treeleafj.xdoc.test.vo.User;

/**
 * 用户模块
 *
 * @author leaf
 * @date 2017-03-03 10:11
 */
@Controller
@RequestMapping("user")
public class UserController {

    /**
     * 查询当前登录用户的基本信息
     *
     * @param user 当前登录用户
     * @return 当前登录用户的基本信息
     * @see User
     */
    @ResponseBody
    @RequestMapping("info")
    public User info(User user) {
        return new User();
    }


    /**
     * 用户注册接口
     *
     * @param user# username 用户名|必填
     * @param user :password 密码|必填
     * @return 当前登录用户的基本信息
     * @title 用户注册
     * @respbody {"id":"123","password":"123456","username":"admin"kasbdkjaskjda}
     * @see User
     */
    @ResponseBody
    @RequestMapping(value = "register", method = RequestMethod.POST)
    User register(User user) {
        return new User();
    }
}
