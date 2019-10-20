package org.treeleafj.xdoc.test.controller;

import com.jfinal.core.Controller;

/**
 * 通用接口
 * <p>
 * Created by leaf on 2017/6/1.
 */
public class CommController extends Controller {

    /**
     * 首页
     *
     * @return 首页页面
     */
    public void index() {
        this.renderText("首页");
    }
}
