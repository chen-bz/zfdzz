package com.hzy.mapper;

import com.hzy.domain.Setting;
import com.hzy.util.PageData;
import java.util.HashMap;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SettingsMapper {
    /**
     * @Author Chensb
     * @Description TODO 根据类别key获取设置信息
     * @Date 2018/8/28 10:04
     * @Param [type_key]
     * @return java.util.List<java.util.HashMap<java.lang.String,java.lang.String>>
     */
    public List<HashMap<String, String>> findByTypekey(String type_key);
    /**
     * @作者 Chensb
     * @描述 TODO 获取一条配置信息
     * @日期 2018/12/13 21:17
     *
     * @param pd 参数
     * @return java.util.HashMap<java.lang.String,java.lang.String>
     */
    public Setting findOneByTypekey(PageData pd);
}