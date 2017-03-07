package org.treeleafj.xdoc;

import org.junit.Test;
import org.treeleafj.xdoc.output.spring.MarkdownFormat;
import org.treeleafj.xdoc.output.spring.SpringXDocOutputImpl;

import java.io.ByteArrayOutputStream;

/**
 * Created by leaf on 2017/3/3 003.
 */
public class XDocTest {

    @Test
    public void build() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        String rootDir = System.getProperty("user.dir");
        SpringXDocOutputImpl output = new SpringXDocOutputImpl(out, new MarkdownFormat());
        XDoc xDoc = new XDoc(rootDir + "/src/main/java/org/treeleafj", output);
        xDoc.build();

        System.out.println(out.toString());
    }

}