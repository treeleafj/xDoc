package org.treeleafj.xdoc.filter;

/**
 * Created by leaf on 2017/4/3 0003.
 */
public class ClassFilterFactory {

    public static ClassFilter classFilter = new DfaultClassFilterImpl();

    public static void setDefaultFilter(ClassFilter classFilter) {
        ClassFilterFactory.classFilter = classFilter;
    }

    public static ClassFilter getDefaultFilter() {
        return ClassFilterFactory.classFilter;
    }
}
