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
import org.treeleafj.xdoc.spring.framework.SpringWebHttpFramework;
import org.treeleafj.xdoc.utils.JsonUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.ArrayList;
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
public class XDocSpringController {

    private Logger logger = LoggerFactory.getLogger(XDocSpringController.class);

    @Autowired
    private XDocProperties xDocProperties;

    private static ApiDoc apiDoc;

    @PostConstruct
    public void _init() {
        //使用多线程异步去初始化,尽量不阻塞系统启动
        try {
            Thread thread = new Thread(this::init);
            thread.start();
        } catch (Exception e) {
            logger.error("start up XDoc error", e);
        }
    }

    public void init() {
        if (!xDocProperties.isEnable()) {
            return;
        }

        String path = xDocProperties.getSourcePath();

        if (StringUtils.isBlank(path)) {
            path = ".";//默认为当前目录
        }

        List<String> paths = Arrays.asList(path.split(","));

        List<File> srcDirs = new ArrayList<>(paths.size());
        List<String> srcDirPaths = new ArrayList<>(paths.size());

        try {
            for (String s : paths) {
                File dir = new File(s);
                srcDirs.add(dir);
                srcDirPaths.add(dir.getCanonicalPath());
            }
        } catch (Exception e) {
            logger.error("获取源码目录路径错误", e);
            return;
        }

        logger.debug("starting XDoc, source path:{}", srcDirPaths);

        XDoc xDoc = new XDoc(srcDirs, new SpringWebHttpFramework());

        try {
            apiDoc = xDoc.resolve();
            HashMap<String, Object> properties = new HashMap<>();
            properties.put("version", xDocProperties.getVersion());
            properties.put("title", xDocProperties.getTitle());
            apiDoc.setProperties(properties);

            logger.info("start up XDoc");
        } catch (Exception e) {
            logger.error("start up XDoc error", e);
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
    @GetMapping({"rebuild", "rebuild.html"})
    public String rebuild() {
        init();
        return "redirect:index.html";
    }
}
