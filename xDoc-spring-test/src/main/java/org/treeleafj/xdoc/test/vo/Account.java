package org.treeleafj.xdoc.test.vo;

/**
 * 用户账户
 * @author leaf
 * @date 2017-03-10 10:43
 */
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

    public String getId() {
        return id;
    }

    public Account setId(String id) {
        this.id = id;
        return this;
    }

    public Double getBalance() {
        return balance;
    }

    public Account setBalance(Double balance) {
        this.balance = balance;
        return this;
    }

    public Integer getScore() {
        return score;
    }

    public Account setScore(Integer score) {
        this.score = score;
        return this;
    }
}
