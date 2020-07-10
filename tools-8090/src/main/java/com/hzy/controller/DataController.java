package com.hzy.controller;

import com.hzy.domain.Data;
import com.hzy.service.data.DataService;
import com.hzy.utils.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @ClassName DataApiController
 * @Description TODO 文章接口类
 * @Author Chensb
 * @Date 2018/8/30 14:50
 * @Version 1.0
 */
@Slf4j
@Controller
public class DataController {

  @Autowired
  private DataService dataService;

  /**
   * @作者 Chensb
   * @描述 TODO 根据分类id获取二级分类列表接口
   * @日期 2018/12/5 19:58
   *
   * @param typeId 分类id
   * @return java.lang.Object
   */
  @ResponseBody
  @RequestMapping(value = "/data_type")
  public Object dataTypes(Integer typeId) {
    return dataService.queryDataType(typeId);
  }

  /**
   * @作者 Chensb
   * @描述 TODO 根据分类id获取文章分页列表接口
   * @日期 2018/12/5 19:58
   *
   * @param typeId 类别id
   * @param pageNo 分页
   * @param pageSize 分页大小
   * @return java.lang.Object
   */
  @ResponseBody
  @RequestMapping(value = "/data_list")
  public Object dataList(Integer typeId, Integer pageNo, Integer pageSize) {
    return dataService.queryDatas(typeId, pageNo, pageSize);
  }

  /**
   * @作者 Chensb
   * @描述 TODO
   * @日期 2018/12/5 19:58
   *
   * @param map 参数
   * @param dataId 文章id
   * @return java.lang.String
   */
  @RequestMapping(value = "/data_page")
  public String dataDetailPage(ModelMap map, Integer dataId) {
    Data data = dataService.queryDataDetail(dataId);
    map.addAttribute("dataDetail", data);
    return "html/data_detail";
  }

  /**
   * @作者 Chensb
   * @描述 TODO 文章详情数据接口
   * @日期 2018/12/21 19:10
   *
   * @param dataId 文章ID
   * @return java.lang.Object
   */
  @ResponseBody
  @RequestMapping(value = "/data_detail")
  public Object dataDetail(Integer dataId) {
    Data data = dataService.queryDataDetail(dataId);
    return ResultUtil.getResult("请求成功", data);
  }
}
