package com.hzy.domain.pay;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "sys_money_record")
public class SysMoneyRecord {
    @Id
    @Column(name = "record_id")
    private Integer recordId;

    /**
     * 金额
     */
    private Double money;

    /**
     * 订单编号
     */
    @Column(name = "order_no")
    private String orderNo;

    /**
     * 商户订单号
     */
    @Column(name = "trade_no")
    private String tradeNo;

    /**
     * 名称
     */
    @Column(name = "record_name")
    private String recordName;

    /**
     * 类别名称
     */
    @Column(name = "type_name")
    private String typeName;

    /**
     * 类别id
     */
    @Column(name = "type_id")
    private Integer typeId;

    /**
     * 类型：0：支出；1：收入；
     */
    private Integer type;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Long createTime;

    /**
     * 会员
     */
    @Column(name = "member_id")
    private Integer memberId;

    /**
     * 是否管理员
     */
    @Column(name = "is_admin")
    private Integer isAdmin;

    /**
     * 管理员
     */
    private String administrator;

    /**
     * 备注
     */
    private String remark;

    /**
     * @return record_id
     */
    public Integer getRecordId() {
        return recordId;
    }

    /**
     * @param recordId
     */
    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }

    /**
     * 获取金额
     *
     * @return money - 金额
     */
    public Double getMoney() {
        return money;
    }

    /**
     * 设置金额
     *
     * @param money 金额
     */
    public void setMoney(Double money) {
        this.money = money;
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
     * 获取商户订单号
     *
     * @return trade_no - 商户订单号
     */
    public String getTradeNo() {
        return tradeNo;
    }

    /**
     * 设置商户订单号
     *
     * @param tradeNo 商户订单号
     */
    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    /**
     * 获取名称
     *
     * @return record_name - 名称
     */
    public String getRecordName() {
        return recordName;
    }

    /**
     * 设置名称
     *
     * @param recordName 名称
     */
    public void setRecordName(String recordName) {
        this.recordName = recordName;
    }

    /**
     * 获取类别名称
     *
     * @return type_name - 类别名称
     */
    public String getTypeName() {
        return typeName;
    }

    /**
     * 设置类别名称
     *
     * @param typeName 类别名称
     */
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    /**
     * 获取类别id
     *
     * @return type_id - 类别id
     */
    public Integer getTypeId() {
        return typeId;
    }

    /**
     * 设置类别id
     *
     * @param typeId 类别id
     */
    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    /**
     * 获取类型：0：支出；1：收入；
     *
     * @return type - 类型：0：支出；1：收入；
     */
    public Integer getType() {
        return type;
    }

    /**
     * 设置类型：0：支出；1：收入；
     *
     * @param type 类型：0：支出；1：收入；
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    public Long getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取会员
     *
     * @return member_id - 会员
     */
    public Integer getMemberId() {
        return memberId;
    }

    /**
     * 设置会员
     *
     * @param memberId 会员
     */
    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    /**
     * 获取是否管理员
     *
     * @return is_admin - 是否管理员
     */
    public Integer getIsAdmin() {
        return isAdmin;
    }

    /**
     * 设置是否管理员
     *
     * @param isAdmin 是否管理员
     */
    public void setIsAdmin(Integer isAdmin) {
        this.isAdmin = isAdmin;
    }

    /**
     * 获取管理员
     *
     * @return administrator - 管理员
     */
    public String getAdministrator() {
        return administrator;
    }

    /**
     * 设置管理员
     *
     * @param administrator 管理员
     */
    public void setAdministrator(String administrator) {
        this.administrator = administrator;
    }

    /**
     * 获取备注
     *
     * @return remark - 备注
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置备注
     *
     * @param remark 备注
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }
}