package org.treeleafj.xdoc.test;

import org.junit.Test;
import org.treeleafj.xdoc.XDoc;
import org.treeleafj.xdoc.spring.SpringXDocOutputImpl;
import org.treeleafj.xdoc.spring.format.MarkdownFormat;

import java.io.ByteArrayOutputStream;

/**
 * Created by leaf on 2017/3/3 003.
 */
public class XDocTest {

    @Test
    public void build() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        String rootDir = System.getProperty("user.dir");
        org.treeleafj.xdoc.spring.SpringXDocOutputImpl output = new SpringXDocOutputImpl(out, new MarkdownFormat());
        XDoc xDoc = new XDoc(rootDir + "/src/main/java/org/treeleafj", output);
        xDoc.build();

        System.out.println(out.toString());
    }

}