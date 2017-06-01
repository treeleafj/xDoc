package org.treeleafj.xdoc.test.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 通用接口
 * <p>
 * Created by leaf on 2017/6/1.
 */
@Controller
public class CommController {

    /**
     * 首页
     *
     * @return 首页页面
     */
    @RequestMapping("index")
    public String index() {
        return "";
    }
}
