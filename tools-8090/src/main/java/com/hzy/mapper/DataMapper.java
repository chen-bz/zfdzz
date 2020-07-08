package com.hzy.mapper;


import com.hzy.entity.Data;
import com.hzy.utils.MyMapper;
import com.hzy.utils.PageData;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DataMapper extends MyMapper<Data> {
	Data queryPushArticle(PageData pd);
}