package org.treeleafj.xdoc.test.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.treeleafj.xdoc.test.vo.Account;

import java.util.UUID;

/**
 * 用户账户模块
 *
 * @author leaf
 * @date 2017-03-10 10:43
 */
@Controller
@RequestMapping("api/account")
public class AccountController {

    /**
     * 获取当前登录用户的账户资产信息,用户不存在会返回code为9999的错误信息
     *
     * @param type 账户类型(1-普通账户)|必填
     * @return 用户的资产
     * @title 查询用户资产
     * @see Account
     */
    @ResponseBody
    @RequestMapping("info")
    Account info(String type) {
        Account account = new Account();
        account.setId(UUID.randomUUID().toString());
        account.setBalance(100D);
        account.setScore(666666);
        return account;
    }
}
