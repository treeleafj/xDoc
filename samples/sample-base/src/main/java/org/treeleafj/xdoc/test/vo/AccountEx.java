package org.treeleafj.xdoc.test.vo;

import lombok.Data;

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

    /**
     * 重写父类的注释,新的含义是:已消费的积分
     */
    private Integer score;
}
