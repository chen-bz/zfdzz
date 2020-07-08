package com.hzy.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "datas")
public class Data {
    @Id
    @Column(name = "data_id")
    private Integer dataId;

    /**
     * 类别ID
     */
    @Column(name = "type_id")
    private Integer typeId;

    /**
     * 类别名称
     */
    @Column(name = "type_name")
    private String typeName;

    /**
     * 标题
     */
    private String title;

    /**
     * 封面
     */
    private String cover;

    /**
     * 简介
     */
    private String intro;

    /**
     * 作者
     */
    private String author;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Long createTime;

    /**
     * 是否启用
     */
    private Integer disabled;

    /**
     * 删除状态
     */
    private Integer deleted;

    /**
     * 内容
     */
    private String content;

    /**
     * @Author Chensb
     * @Description TODO 是否显示标题、作者、日期：0：显示；1：隐藏
     * @Date 2018/10/13 14:33
     * @Param
     * @return
     */
    @Column(name = "show_info")
    private Integer showInfo;

    public Integer getShowInfo() {
        return showInfo;
    }

    public void setShowInfo(Integer showInfo) {
        this.showInfo = showInfo;
    }

    /**
     * @return data_id
     */
    public Integer getDataId() {
        return dataId;
    }

    /**
     * @param dataId
     */
    public void setDataId(Integer dataId) {
        this.dataId = dataId;
    }

    /**
     * 获取类别ID
     *
     * @return type_id - 类别ID
     */
    public Integer getTypeId() {
        return typeId;
    }

    /**
     * 设置类别ID
     *
     * @param typeId 类别ID
     */
    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
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
     * 获取标题
     *
     * @return title - 标题
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置标题
     *
     * @param title 标题
     */
    public void setTitle(String title) {
        this.title = title;
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
     * 获取简介
     *
     * @return intro - 简介
     */
    public String getIntro() {
        return intro;
    }

    /**
     * 设置简介
     *
     * @param intro 简介
     */
    public void setIntro(String intro) {
        this.intro = intro;
    }

    /**
     * 获取作者
     *
     * @return author - 作者
     */
    public String getAuthor() {
        return author;
    }

    /**
     * 设置作者
     *
     * @param author 作者
     */
    public void setAuthor(String author) {
        this.author = author;
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
     * 获取是否启用
     *
     * @return disabled - 是否启用
     */
    public Integer getDisabled() {
        return disabled;
    }

    /**
     * 设置是否启用
     *
     * @param disabled 是否启用
     */
    public void setDisabled(Integer disabled) {
        this.disabled = disabled;
    }

    /**
     * 获取删除状态
     *
     * @return deleted - 删除状态
     */
    public Integer getDeleted() {
        return deleted;
    }

    /**
     * 设置删除状态
     *
     * @param deleted 删除状态
     */
    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    /**
     * 获取内容
     *
     * @return content - 内容
     */
    public String getContent() {
        return content;
    }

    /**
     * 设置内容
     *
     * @param content 内容
     */
    public void setContent(String content) {
        this.content = content;
    }
}