package com.hzy.domain;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "pay_log")
public class PayLog {
    /**
     * 支付记录id
     */
    @Id
    @Column(name = "log_id")
    private Integer logId;

    /**
     * 日志编号
     */
    @Column(name = "log_no")
    private String logNo;

    /**
     * 订单编号
     */
    @Column(name = "order_no")
    private String orderNo;

    /**
     * 支付凭证编号
     */
    @Column(name = "trade_no")
    private String tradeNo;

    /** 支付方式 */
    @Column(name = "pay_type")
    private String payType;

    /**
     * 订单类型
     */
    @Column(name = "order_type")
    private String orderType;

    /**
     * 支付订单金额
     */
    private Double money;

    @Column(name = "pay_money")
    private Double payMoney;

    /**
     * 发起支付时间
     */
    @Column(name = "create_time")
    private Long createTime;

    /**
     * 支付回调成功时间
     */
    @Column(name = "callback_time")
    private Long callbackTime;

    /**
     * 第三方返回支付成功时间
     */
    @Column(name = "success_time")
    private Long successTime;

    /**
     * 订单状态更改完成时间
     */
    @Column(name = "finish_time")
    private Long finishTime;

    /**
     * 状态：0：向第三方下单；1：第三方返回支付成功；2：平台修改订单状态成功，订单完成；
     */
    private Integer status;

    /**
     * 统一下单结果json
     */
    @Column(name = "unified_result")
    private String unifiedResult;

    /**
     * 回调结果json
     */
    @Column(name = "callback_result")
    private String callbackResult;

    /**
     * 订单查询结果json
     */
    @Column(name = "query_result")
    private String queryResult;

    /**
     * 获取支付记录id
     *
     * @return log_id - 支付记录id
     */
    public Integer getLogId() {
        return logId;
    }

    /**
     * 设置支付记录id
     *
     * @param logId 支付记录id
     */
    public void setLogId(Integer logId) {
        this.logId = logId;
    }

    /**
     * 获取日志编号
     *
     * @return log_no - 日志编号
     */
    public String getLogNo() {
        return logNo;
    }

    /**
     * 设置日志编号
     *
     * @param logNo 日志编号
     */
    public void setLogNo(String logNo) {
        this.logNo = logNo;
    }

    /**
     * 获取订单编号
     *
     * @return order_no - 订单编号
     */
    public String getOrderNo() {
        return orderNo;
    }

    /**
     * 设置订单编号
     *
     * @param orderNo 订单编号
     */
    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    /**
     * 获取支付凭证编号
     *
     * @return trade_no - 支付凭证编号
     */
    public String getTradeNo() {
        return tradeNo;
    }

    /**
     * 设置支付凭证编号
     *
     * @param tradeNo 支付凭证编号
     */
    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    /**
     * 获取订单类型
     *
     * @return order_type - 订单类型
     */
    public String getOrderType() {
        return orderType;
    }

    /**
     * 设置订单类型
     *
     * @param orderType 订单类型
     */
    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    /**
     * 获取支付订单金额
     *
     * @return money - 支付订单金额
     */
    public Double getMoney() {
        return money;
    }

    /**
     * 设置支付订单金额
     *
     * @param money 支付订单金额
     */
    public void setMoney(Double money) {
        this.money = money;
    }

    /**
     * @return pay_money
     */
    public Double getPayMoney() {
        return payMoney;
    }

    /**
     * @param payMoney
     */
    public void setPayMoney(Double payMoney) {
        this.payMoney = payMoney;
    }

    /**
     * 获取发起支付时间
     *
     * @return create_time - 发起支付时间
     */
    public Long getCreateTime() {
        return createTime;
    }

    /**
     * 设置发起支付时间
     *
     * @param createTime 发起支付时间
     */
    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取支付回调成功时间
     *
     * @return callback_time - 支付回调成功时间
     */
    public Long getCallbackTime() {
        return callbackTime;
    }

    /**
     * 设置支付回调成功时间
     *
     * @param callbackTime 支付回调成功时间
     */
    public void setCallbackTime(Long callbackTime) {
        this.callbackTime = callbackTime;
    }

    /**
     * 获取第三方返回支付成功时间
     *
     * @return success_time - 第三方返回支付成功时间
     */
    public Long getSuccessTime() {
        return successTime;
    }

    /**
     * 设置第三方返回支付成功时间
     *
     * @param successTime 第三方返回支付成功时间
     */
    public void setSuccessTime(Long successTime) {
        this.successTime = successTime;
    }

    /**
     * 获取订单状态更改完成时间
     *
     * @return finish_time - 订单状态更改完成时间
     */
    public Long getFinishTime() {
        return finishTime;
    }

    /**
     * 设置订单状态更改完成时间
     *
     * @param finishTime 订单状态更改完成时间
     */
    public void setFinishTime(Long finishTime) {
        this.finishTime = finishTime;
    }

    /**
     * 获取状态：0：向第三方下单；1：第三方返回支付成功；2：平台修改订单状态成功，订单完成；
     *
     * @return status - 状态：0：向第三方下单；1：第三方返回支付成功；2：平台修改订单状态成功，订单完成；
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置状态：0：向第三方下单；1：第三方返回支付成功；2：平台修改订单状态成功，订单完成；
     *
     * @param status 状态：0：向第三方下单；1：第三方返回支付成功；2：平台修改订单状态成功，订单完成；
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取统一下单结果json
     *
     * @return callback_result - 回调结果json
     */
    public String getUnifiedResult() {
        return unifiedResult;
    }

    /**
     * 设置统一下单结果json
     *
     * @param unifiedResult 回调结果json
     */
    public void setUnifiedResult(String unifiedResult) {
        this.unifiedResult = unifiedResult;
    }

    /**
     * 获取回调结果json
     *
     * @return callback_result - 回调结果json
     */
    public String getCallbackResult() {
        return callbackResult;
    }

    /**
     * 设置回调结果json
     *
     * @param callbackResult 回调结果json
     */
    public void setCallbackResult(String callbackResult) {
        this.callbackResult = callbackResult;
    }

    /**
     * 获取订单查询结果json
     *
     * @return query_result - 订单查询结果json
     */
    public String getQueryResult() {
        return queryResult;
    }

    /**
     * 设置订单查询结果json
     *
     * @param queryResult 订单查询结果json
     */
    public void setQueryResult(String queryResult) {
        this.queryResult = queryResult;
    }

    public String createSn() {
        String sn = "Log" + new Date().getTime();
        int num = (int) (Math.random() * 899) + 100;
        return sn + num;
    }
}