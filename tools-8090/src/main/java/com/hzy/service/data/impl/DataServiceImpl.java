package com.hzy.service.data.impl;

import com.github.pagehelper.Page;
import com.hzy.domain.Data;
import com.hzy.domain.DataType;
import com.hzy.mapper.DataMapper;
import com.hzy.mapper.DataTypeMapper;
import com.hzy.service.base.BaseService;
import com.hzy.service.data.DataService;
import com.hzy.utils.PageData;
import com.hzy.utils.ResultUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

@Service
public class DataServiceImpl extends BaseService implements DataService {

	@Autowired
	private DataTypeMapper dataTypeMapper;
	@Autowired
	private DataMapper dataMapper;

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public Map queryDataType(Integer typeId) {
		Example example = this.getAndExample(DataType.class, "pid=", typeId, "disabled=", 0, "deleted=", 0);
		example.orderBy("sort").asc();
		List<DataType> typeList = dataTypeMapper.selectByExample(example);
		for (DataType type : typeList) {
			type.setCover(this.upImgPath(type.getCover()));
		}
		return ResultUtil.getResult("请求成功", typeList);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public Map queryDatas(Integer typeId, Integer pageno, Integer pageSize) {
		pageno = pageno == null || pageno < 1 ? 1 : pageno;
		pageSize = pageSize == null || pageSize < 1 ? 10 : pageSize;
		Example example = this.getAndExample(Data.class, "type_id=", typeId, "deleted=", 0, "disabled=", 0);
		example.orderBy("createTime").desc();
		Page<Data> dataList = this.startPage(pageno, pageSize);
		dataList = (Page<Data>) dataMapper.selectByExample(example);
		if (pageno > dataList.getPageNum()) {
			return ResultUtil.getResult("请求成功", new ArrayList(), dataList.getPageNum(), dataList.getPages(), dataList.getTotal());
		}
		for (Data data : dataList) {
			data.setCover(this.upImgPath(data.getCover()));
		}
		return ResultUtil.getResult("请求成功", dataList, dataList.getPageNum(), dataList.getPages(), dataList.getTotal());
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public Data queryDataDetail(Integer dataId) {
		Data data;
		if (dataId < 10000) {
			data = dataMapper.selectByPrimaryKey(dataId);
		} else {
			data = dataMapper.queryPushArticle(new PageData("dataId", dataId));
		}
		data.setCover(this.upImgPath(data.getCover()));
		return data;
	}

}
