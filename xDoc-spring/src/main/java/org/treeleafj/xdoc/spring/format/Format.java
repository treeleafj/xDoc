package org.treeleafj.xdoc.spring.format;

import org.treeleafj.xdoc.spring.SpringApiModule;

import java.util.List;

/**
 * Created by leaf on 2017/3/4.
 */
public interface Format {

    String format(List<SpringApiModule> list);

    String format(SpringApiModule apiModule);
}
