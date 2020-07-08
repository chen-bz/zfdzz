package com.hzy.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "data_type")
public class DataType {
    @Id
    @Column(name = "type_id")
    private Integer typeId;

    /**
     * 上级ID
     */
    private Integer pid;

    /**
     * 类别名称
     */
    private String name;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 封面
     */
    private String cover;

    /**
     * 菜单等级
     */
    private Integer level;

    /**
     * 禁用？：0：否；1：是；
     */
    private Integer disabled;

    /**
     * 删除？：0：否；1：是；
     */
    private Integer deleted;

    /**
     * 备注
     */
    private String remark;

    /**列表显示*/
    private Integer listShow;
    /**
     * @return type_id
     */
    public Integer getTypeId() {
        return typeId;
    }

    /**
     * @param typeId
     */
    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    /**
     * 获取上级ID
     *
     * @return pid - 上级ID
     */
    public Integer getPid() {
        return pid;
    }

    /**
     * 设置上级ID
     *
     * @param pid 上级ID
     */
    public void setPid(Integer pid) {
        this.pid = pid;
    }

    /**
     * 获取类别名称
     *
     * @return name - 类别名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置类别名称
     *
     * @param name 类别名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取排序
     *
     * @return sort - 排序
     */
    public Integer getSort() {
        return sort;
    }

    /**
     * 设置排序
     *
     * @param sort 排序
     */
    public void setSort(Integer sort) {
        this.sort = sort;
    }

    /**
     * 获取封面
     *
     * @return cover - 封面
     */
    public String getCover() {
        return cover;
    }

    /**
     * 设置封面
     *
     * @param cover 封面
     */
    public void setCover(String cover) {
        this.cover = cover;
    }

    /**
     * 获取菜单等级
     *
     * @return level - 菜单等级
     */
    public Integer getLevel() {
        return level;
    }

    /**
     * 设置菜单等级
     *
     * @param level 菜单等级
     */
    public void setLevel(Integer level) {
        this.level = level;
    }

    /**
     * 获取禁用？：0：否；1：是；
     *
     * @return disabled - 禁用？：0：否；1：是；
     */
    public Integer getDisabled() {
        return disabled;
    }

    /**
     * 设置禁用？：0：否；1：是；
     *
     * @param disabled 禁用？：0：否；1：是；
     */
    public void setDisabled(Integer disabled) {
        this.disabled = disabled;
    }

    /**
     * 获取删除？：0：否；1：是；
     *
     * @return deleted - 删除？：0：否；1：是；
     */
    public Integer getDeleted() {
        return deleted;
    }

    /**
     * 设置删除？：0：否；1：是；
     *
     * @param deleted 删除？：0：否；1：是；
     */
    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
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

    public Integer getListShow() {
        return listShow;
    }

    public void setListShow(Integer listShow) {
        this.listShow = listShow;
    }
}