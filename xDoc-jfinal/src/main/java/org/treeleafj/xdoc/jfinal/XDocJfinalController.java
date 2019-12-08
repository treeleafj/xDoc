package org.treeleafj.xdoc.jfinal;

import com.jfinal.core.Controller;
import com.jfinal.kit.PropKit;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.treeleafj.xdoc.XDoc;
import org.treeleafj.xdoc.format.http.HtmlForamt;
import org.treeleafj.xdoc.model.ApiDoc;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * xDoc-jfinal 入口
 */
public class XDocJfinalController extends Controller {


    private Logger logger = LoggerFactory.getLogger(XDocJfinalController.class);

    private static String html;

    private static ApiDoc apiDoc;

    public XDocJfinalController() {
        boolean enable = PropKit.getBoolean("xdoc.enable", true);

        if (!enable || apiDoc != null) {
            return;
        }

        synchronized (XDocJfinalController.class) {
            if (apiDoc != null) {
                return;
            }
            init();
        }

    }

    /**
     * 访问接口文档首页
     */
    public void index() {
        renderHtml(html);
    }

    /**
     * 获取所有文档api
     *
     * @return 系统所有文档接口的数据(json格式)
     */
    public void apis() {
        renderJson(this.apiDoc);
    }

    /**
     * 重新构建文档
     *
     * @return 文档页面
     */
    public void rebuild() {
        init();
        redirect("index");
    }

    /**
     * 初始化Xdoc文档内容
     */
    private void init() {

        String path = PropKit.get("xdoc.sourcePath");
        String version = PropKit.get("xdoc.version");
        String title = PropKit.get("xdoc.title");

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

        XDoc xDoc = new XDoc(srcDirs, new JfinalHttpFramework());

        try {
            apiDoc = xDoc.resolve();
            HashMap<String, Object> properties = new HashMap<>();
            properties.put("version", version);
            properties.put("title", title);
            apiDoc.setProperties(properties);

            //生成接口文档的html
            try(ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                xDoc.build(out, new HtmlForamt());
                html = out.toString("UTF-8");
            } catch (Exception e) {
                logger.error("生成html文档失败");
            }

        } catch (Exception e) {
            logger.error("start up XDoc error", e);
        }
    }
}
