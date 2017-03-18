package org.treeleafj.xdoc.test;

import org.junit.Test;
import org.treeleafj.xdoc.XDoc;
import org.treeleafj.xdoc.spring.SpringXDocOutputImpl;
import org.treeleafj.xdoc.spring.format.HtmlForamt;
import org.treeleafj.xdoc.spring.format.MarkdownFormat;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by leaf on 2017/3/3 003.
 */
public class XDocTest {

    @Test
    public void buildMarkdown() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        String rootDir = System.getProperty("user.dir");
        org.treeleafj.xdoc.spring.SpringXDocOutputImpl output = new SpringXDocOutputImpl(out, new MarkdownFormat());
        XDoc xDoc = new XDoc(rootDir + "/src/main/java/org/treeleafj", output);
        xDoc.build();

        System.out.println(out.toString());
    }

    @Test
    public void buildHtml() throws Exception {
        FileOutputStream out = new FileOutputStream(new File("E:/api.html"));
        String rootDir = System.getProperty("user.dir");
        org.treeleafj.xdoc.spring.SpringXDocOutputImpl output = new SpringXDocOutputImpl(out, new HtmlForamt());
        XDoc xDoc = new XDoc(rootDir + "/src/main/java/org/treeleafj", output);
        xDoc.build();
    }
}