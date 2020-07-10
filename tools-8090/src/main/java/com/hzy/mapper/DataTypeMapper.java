package com.hzy.mapper;


import com.hzy.domain.DataType;
import com.hzy.util.MyMapper;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DataTypeMapper extends MyMapper<DataType> {
    /**
     * 视频去水印查询数据
     * @param map
     * @return
     */
    Map getToolsOrderInfo(Map map);

    /**
     * 视频去水印修改使用次数
     * @param map
     */
    void toolsOrderUpdate(Map map);
}