package com.hzy.domain.pay;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "sys_money_type")
public class SysMoneyType {
    @Id
    @Column(name = "money_type_id")
    private Integer moneyTypeId;

    /**
     * 名称
     */
    @Column(name = "type_name")
    private String typeName;

    /**
     * 类型：0：支出；1：收入；
     */
    private Integer type;

    /**
     * 是否系统管理员操作
     */
    @Column(name = "is_admin")
    private Integer isAdmin;

    /**
     * 备注
     */
    private String remark;

    /**
     * @return money_type_id
     */
    public Integer getMoneyTypeId() {
        return moneyTypeId;
    }

    /**
     * @param moneyTypeId
     */
    public void setMoneyTypeId(Integer moneyTypeId) {
        this.moneyTypeId = moneyTypeId;
    }

    /**
     * 获取名称
     *
     * @return type_name - 名称
     */
    public String getTypeName() {
        return typeName;
    }

    /**
     * 设置名称
     *
     * @param typeName 名称
     */
    public void setTypeName(String typeName) {
        this.typeName = typeName;
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
     * 获取是否系统管理员操作
     *
     * @return is_admin - 是否系统管理员操作
     */
    public Integer getIsAdmin() {
        return isAdmin;
    }

    /**
     * 设置是否系统管理员操作
     *
     * @param isAdmin 是否系统管理员操作
     */
    public void setIsAdmin(Integer isAdmin) {
        this.isAdmin = isAdmin;
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