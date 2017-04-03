package org.treeleafj.xdoc.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by leaf on 2017/4/3 0003.
 */
public class CommentUtils {

    /**
     * 获取注释的类型
     *
     * @param coment
     * @return @see @param @resp @return等
     */
    public static String findTagType(String coment) {
        Pattern compile = Pattern.compile("^@[\\w]+[\\t ]");
        Matcher m = compile.matcher(coment);
        if (m.find()) {
            return m.group().trim();
        } else {
            return null;
        }
    }

    /**
     * 解析基本的文本注释
     *
     * @param coment
     * @return
     */
    public static String parseCommentText(String coment) {
        List<String> coments = asCommentList(coment);
        for (String s : coments) {
            if (!s.startsWith("@")) {
                return s;
            }
        }
        return coment;
    }

    /**
     * 将注释转为多行文本
     *
     * @param coment
     * @return
     */
    public static List<String> asCommentList(String coment) {
        coment = coment.replaceAll("\\*", "")
                .replaceAll("\\/*", "")
                .replaceAll("\\*\\/", "").trim();
        String[] array = coment.split("\n");
        List<String> comments = new ArrayList(array.length);
        for (int i = 0; i < array.length; i++) {
            String c = array[i].trim();
            if (StringUtils.isNotBlank(c)) {
                comments.add(c);
            }
        }
        return comments;
    }
}
