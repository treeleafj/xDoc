package org.treeleafj.xdoc.test.controller;

import com.jfinal.core.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.treeleafj.xdoc.test.vo.Account;

import java.util.UUID;

/**
 * 用户账户模块
 *
 * @author leaf
 * @date 2017-03-10 10:43
 */
public class AccountController extends Controller {

    private Logger log = LoggerFactory.getLogger(AccountController.class);

    /**
     * 获取当前登录用户的账户资产信息,用户不存在会返回code为9999的错误信息,见:https://github.com/treeleafj/xDoc
     *
     * @param type 账户类型(1-普通账户)|必填
     * @param balance 重写@paramObj中AccountEx的balance的注释
     * @paramObj AccountEx
     * @return 用户的资产
     * @title 查询用户资产
     * @resp balance 账户余额|double
     */
    public void info() {
        String type = this.get("type");
        Account account = new Account();
        account.setId(UUID.randomUUID().toString());
        account.setBalance(100D);
        account.setScore(666666);
        this.renderJson(account);
    }
}
