package org.treeleafj.xdoc.test;

import org.junit.Test;
import org.treeleafj.xdoc.XDoc;
import org.treeleafj.xdoc.spring.format.HtmlForamt;
import org.treeleafj.xdoc.spring.format.MarkdownFormat;
import org.treeleafj.xdoc.spring.framework.SpringWebFramework;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by leaf on 2017/3/3 003.
 */
public class XDocTest {

    @Test
    public void buildMarkdown() {
        //生成离线的Markdown格式的接口文档
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        String rootDir = System.getProperty("user.dir");
        XDoc xDoc = new XDoc(rootDir + "/src/main/java/org/treeleafj", new SpringWebFramework());
        xDoc.build(out, new MarkdownFormat());

        System.out.println(out.toString());
    }

    @Test
    public void buildHtml() throws Exception {
        //生成离线的HTML格式的接口文档
        String userDir = System.getProperty("user.dir");
        FileOutputStream out = new FileOutputStream(new File(userDir, "api.html"));
        XDoc xDoc = new XDoc(userDir + "/src/main/java/org/treeleafj", new SpringWebFramework());
        xDoc.build(out, new HtmlForamt());
    }
}