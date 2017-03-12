package org.treeleafj.xdoc.test.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.treeleafj.xdoc.test.vo.Account;
import org.treeleafj.xdoc.test.vo.User;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户账户模块
 *
 * @author leaf
 * @date 2017-03-10 10:43
 */
@Controller
@RequestMapping("account")
public class AccountController {

    /**
     * 获取登录用户的账户资产信息
     *
     * @return 用户的资产
     * @see Account
     */
    @RequestMapping("info")
    Account info(User user) {
        return new Account();
    }

    /**
     * 查询用户的账户状态,如果返回被冻结,说明用户账户不可用,相应的交易操作也不能用
     *
     * @return 账户状态
     * @title 查询用户账户状态
     * @respbody {"id":"123","frozen":true,"frozenDesc":"非法充值"}
     * @resp frozen 冻结|boolean|必填
     * @resp frozenDesc 冻结描述|string|必填
     * @see User
     */
    @ResponseBody
    @RequestMapping(value = "status", method = RequestMethod.GET)
    Map<String, String> status(User user) {
        return new HashMap<>();
    }
}
