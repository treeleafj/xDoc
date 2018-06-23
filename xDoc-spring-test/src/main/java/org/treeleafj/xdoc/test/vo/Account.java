package org.treeleafj.xdoc.test.vo;

import lombok.Data;

/**
 * 用户账户
 * @author leaf
 * @date 2017-03-10 10:43
 */
@Data
public class Account {

    /**
     * 账户ID,跟用户ID一致
     */
    private String id;

    /**
     * 用户余额
     */
    private Double balance;

    /**
     * 用户积分
     */
    private Integer score;
}
