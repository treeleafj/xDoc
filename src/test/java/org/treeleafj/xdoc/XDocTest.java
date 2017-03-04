package org.treeleafj.xdoc;

import org.junit.Test;

/**
 * Created by leaf on 2017/3/3 003.
 */
public class XDocTest {

    @Test
    public void build() throws Exception {
        String rootDir = System.getProperty("user.dir");
        XDoc xDoc = new XDoc(rootDir + "/src/main/java/org/treeleafj", "");
        xDoc.build();
    }

}