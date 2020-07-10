package com.hzy.domain.pay;

public enum OrderType {
  /** 抽奖小程序 */
  ORDER("Order", "O");

  private String typeName;
  private String perfix;

  private OrderType(String typeName, String perfix) {
    this.typeName = typeName;
    this.perfix = perfix;
  }

  public static String getOrderType(String orderNo) {
    for (OrderType t : OrderType.values()) {
      if (orderNo.indexOf(t.perfix) == 0) {
        return t.typeName;
      }
    }
    return null;
  }

  public static String getPrefix(String orderNo) {
    return orderNo.substring(0, 1);
  }

  public String getTypeName() {
    return typeName;
  }

  public void setTypeName(String typeName) {
    this.typeName = typeName;
  }

  public String getPerfix() {
    return perfix;
  }

  public void setPerfix(String perfix) {
    this.perfix = perfix;
  }
}
