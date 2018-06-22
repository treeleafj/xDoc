package org.treeleafj.xdoc.test.vo;

import lombok.Data;
import org.apache.commons.beanutils.PropertyUtils;

import java.beans.PropertyDescriptor;
import java.util.Date;

/**
 * Created by leaf on 2018/6/22.
 */
@Data
public class AccountEx extends Account {

    /**
     * 创建时间
     */
    private Date createdtime;


    /**
     * 等级,数字越高级别越大
     */
    private Integer level;

    public static void main(String[] args) {
        PropertyDescriptor[] propertyDescriptors = PropertyUtils.getPropertyDescriptors(AccountEx.class);
        System.out.println(propertyDescriptors);
    }
}
