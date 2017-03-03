package org.leaf.anno.doc;

import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author leaf
 * @date 2017-03-03 11:09
 */
@Data
public class ApiAction {

    private String name;

    private List uri;

    private List methods;

    private String comment;

    private String author;

    private String returnMsg;

    private VOInfo returnInfo;

    private Map<String, String> params = new TreeMap<String, String>();
}
