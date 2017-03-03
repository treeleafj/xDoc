package org.treeleafj.xdoc;

import org.junit.Test;

/**
 * Created by leaf on 2017/3/3 003.
 */
public class XDocTest {

    @Test
    public void build() throws Exception {
        XDoc xDoc = new XDoc("E:\\project\\xDoc\\src\\main\\java\\org\\treeleafj", "");
        xDoc.build();
    }

}