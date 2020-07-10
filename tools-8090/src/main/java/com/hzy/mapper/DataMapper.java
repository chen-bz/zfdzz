package com.hzy.mapper;


import com.hzy.domain.Data;
import com.hzy.util.MyMapper;
import com.hzy.util.PageData;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DataMapper extends MyMapper<Data> {
	Data queryPushArticle(PageData pd);
}