package org.treeleafj.xdoc.test.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.treeleafj.xdoc.test.vo.Account;
import org.treeleafj.xdoc.test.vo.User;

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
     * @return 用户的资产
     * @see Account
     */
    @RequestMapping("info")
    Account info(User user) {
        return new Account();
    }

}
