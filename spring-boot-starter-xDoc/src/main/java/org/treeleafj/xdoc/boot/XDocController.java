package org.treeleafj.xdoc.boot;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.treeleafj.xdoc.XDoc;
import org.treeleafj.xdoc.model.ApiDoc;
import org.treeleafj.xdoc.spring.framework.SpringWebFramework;
import org.treeleafj.xdoc.utils.JsonUtils;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * XDoc的Spring Web入口
 *
 * @author leaf
 * @date 2017-03-09 15:36
 */
@RequestMapping("xdoc")
public class XDocController {

    private Logger log = LoggerFactory.getLogger(XDocController.class);

    @Autowired
    private XDocProperties xDocProperties;

    private static ApiDoc apiDoc;

    @PostConstruct
    public void init() {
        if (!xDocProperties.isEnable()) {
            return;
        }

        String path = xDocProperties.getSourcePath();

        if (StringUtils.isBlank(path)) {
            path = ".";//默认为当前目录
        }

        List<String> paths = Arrays.asList(path.split(","));

        log.debug("starting XDoc, source path:{}", paths);

        try {
            XDoc xDoc = new XDoc(paths, new SpringWebFramework());

            Thread thread = new Thread(() -> {
                try {
                    apiDoc = xDoc.resolve();
                    HashMap<String, Object> properties = new HashMap<>();
                    properties.put("version", xDocProperties.getVersion());
                    properties.put("title", xDocProperties.getTitle());
                    apiDoc.setProperties(properties);

                    log.info("start up XDoc");
                } catch (Exception e) {
                    log.error("start up XDoc error", e);
                }
            });
            thread.start();
        } catch (Exception e) {
            log.error("start up XDoc error", e);
        }
    }

    /**
     * 跳转到xdoc接口文档首页
     */
    @GetMapping
    public String index() {
        return "redirect:index.html";
    }

    /**
     * 获取所有文档api
     *
     * @return 系统所有文档接口的数据(json格式)
     */
    @ResponseBody
    @RequestMapping("apis")
    public Object apis() {
        return JsonUtils.toJson(apiDoc);
    }

    /**
     * 重新构建文档
     *
     * @return 文档页面
     */
    @GetMapping("rebuild")
    public String rebuild() {
        init();
        return "redirect:index.html";
    }
}
