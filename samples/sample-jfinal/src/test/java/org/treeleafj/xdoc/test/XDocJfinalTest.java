package org.treeleafj.xdoc.test;

import org.junit.Test;
import org.treeleafj.xdoc.XDoc;
import org.treeleafj.xdoc.format.http.HtmlForamt;
import org.treeleafj.xdoc.jfinal.JfinalHttpFramework;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.List;

public class XDocJfinalTest {

    @Test
    public void buildMarkdown() {
        //生成离线的Markdown格式的接口文档
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        String rootDir = System.getProperty("user.dir");
        XDoc xDoc = new XDoc(new File(rootDir + "/src/main/java/org/treeleafj"), new JfinalHttpFramework());
//        xDoc.build(out, new MarkdownFormat());

        System.out.println(out.toString());
    }

    @Test
    public void buildHtml() throws Exception {
        //生成离线的HTML格式的接口文档
        String userDir = System.getProperty("user.dir");
        FileOutputStream out = new FileOutputStream(new File(userDir, "api.html"));
        File srcDir1 = new File(userDir + "/src/main/java/org/treeleafj");
        File srcDir2 = new File(userDir + "/../sample-base/src/main/java/org/treeleafj");
        List<File> list = Arrays.asList(srcDir1, srcDir2);
        XDoc xDoc = new XDoc(list, new JfinalHttpFramework());
        xDoc.build(out, new HtmlForamt());
    }
}
