package com.hzy.mapper;

import com.hzy.domain.PayOrder;
import com.hzy.domain.pay.RefundOrder;
import com.hzy.util.PageData;
import java.util.List;
import java.util.Map;

/**
 * @类名 PublicMapper
 * @描述 TODO 公共Mapper
 * @作者 Chensb
 * @日期 2018/10/29 20:00
 * @版本号 1.0
 */
public interface PublicMapper {
  /**
   * @作者 Chensb
   * @描述 TODO 注册检查会员是否存在
   * @日期 2018/10/29 20:02
   *
   * @param phone 手机号
   * @return java.util.Map
   */
  Map registerCheckMember(Long phone);

  /**
   * @作者 Chensb
   * @描述 TODO 查询订单
   * @日期 2018/12/6 20:30
   *
   * @param pd 参数
   * @return com.hzy.domain.Order
   */
  PayOrder queryOrder(PageData pd);

  /**
   * @作者 Chensb
   * @描述 TODO 设置支付方式
   * @日期 2018/12/30 18:25
   *
   * @param pd 参数
   * @return int
   */
  int setPayType(PageData pd);

  /**
   * @作者 Chensb
   * @描述 TODO 根据广告位ID获取广告列表
   * @日期 2019/1/2 9:23
   *
   * @param pd 参数
   * @return java.util.Map
   */
  List<Map> banners(PageData pd);

  /**
   * TODO 描述:查询线下折扣活动订单VIP/代理商类型
   *
   * @param pd
   * @return java.lang.Integer
   * @author Chensb
   * @date 2019/9/3 11:41
   */
	Integer queryVipType(PageData pd);

	/**
	 * TODO 描述:查询退款订单
	 *
	 * @param pd
	 * @return com.hzy.domain.RefundOrder
	 * @author Chensb
	 * @date 2019/10/23 11:18
	 */
	RefundOrder queryRefundOrder(PageData pd);
}
