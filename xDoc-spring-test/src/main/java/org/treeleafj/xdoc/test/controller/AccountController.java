package org.treeleafj.xdoc.test.controller;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.treeleafj.xdoc.test.vo.Account;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.Callable;

/**
 * 用户账户模块
 *
 * @author leaf
 * @date 2017-03-10 10:43
 */
@Controller
@RequestMapping("api/account")
public class AccountController {

    private Logger log = LoggerFactory.getLogger(AccountController.class);

    /**
     * 获取当前登录用户的账户资产信息,用户不存在会返回code为9999的错误信息,见:https://github.com/treeleafj/xDoc
     *
     * @param type 账户类型(1-普通账户)|必填
     * @return 用户的资产
     * @title 查询用户资产
     * @see Account
     */
    @ResponseBody
    @RequestMapping(value = "info", method = RequestMethod.POST)
    Account info(String type) {
        Account account = new Account();
        account.setId(UUID.randomUUID().toString());
        account.setBalance(100D);
        account.setScore(666666);
        return account;
    }

    /**
     * 文件/图片获取
     *
     * @param id   文件Id|必填
     * @param type 业务类型,1-用户头像,2-绘画完成的作品,3-心情,4-图案,5-关于我们logo|必填
     * @return 文件(如果文件不存在, http response status会返回404)
     * @throws IOException
     */
    @RequestMapping("get")
    public Callable get(String id, String type, HttpServletResponse resp, @RequestHeader HttpHeaders httpHeaders) {

        if (id.contains("..") || StringUtils.isBlank(id)) {
            log.warn("文件ID非法:" + id);
            resp.setStatus(404);
            return null;
        }

        //实现304缓存
        String ifModifiedSince = httpHeaders.getFirst("If-Modified-Since");
        if (StringUtils.isNotBlank(ifModifiedSince)) {
            resp.setStatus(304);
            return null;
        }

        resp.setHeader("Last-Modified", new Date().toString());

        String path = "/";
        File file = new File(path + "/" + type + "/" + id);
        if (!file.exists()) {
            resp.setStatus(404);
            return null;
        }

        Callable callback = () -> {
            ServletOutputStream out = null;
            FileInputStream in = null;
            try {
                out = resp.getOutputStream();
                in = new FileInputStream(file);
                IOUtils.copy(in, out);
            } catch (IOException e) {
                log.info("输出文件错误:{}", id, e);
            } finally {
                IOUtils.closeQuietly(out);
                IOUtils.closeQuietly(in);
            }
            return "";
        };
        return callback;
    }
}
