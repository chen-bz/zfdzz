package com.hzy.service.data;


import com.hzy.domain.Data;
import java.util.Map;

/**
 * @类名 DataService
 * @描述 TODO 文章
 * @作者 Chensb
 * @日期 2018/12/5 19:55
 * @版本号 1.0
 */
public interface DataService {

    /**
     * @Author Chensb
     * @Description TODO 根据上级分类获取二级分类列表
     * @Date 2018/8/30 14:36
     * @param typeId 类别id
     * @return java.lang.Object
     */
    Map queryDataType(Integer typeId);

    /**
     * @作者 Chensb
     * @描述 TODO 根据分类获取文章分页列表
     * @日期 2018/12/5 19:56
     *
     * @param typeId 类别id
     * @param pageNo 分页
     * @param pageSize 分页大小
     * @return java.lang.Object
     */
    Map queryDatas(Integer typeId, Integer pageNo, Integer pageSize);

    /**
     * @作者 Chensb
     * @描述 TODO 获取文章详情
     * @日期 2018/12/5 19:56
     *
     * @param dataId 文章id
     * @return com.hzy.domain.Data
     */
    Data queryDataDetail(Integer dataId);
}
