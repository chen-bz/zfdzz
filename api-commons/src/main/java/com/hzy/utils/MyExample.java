package com.hzy.utils;

import tk.mybatis.mapper.entity.Example;

/**
 * .
 *
 * @ClassName Example
 * @Description TODO
 * @Author Chensb
 * @Date 2018/10/18 14:38
 * @Version 1.0
 */
public class MyExample extends Example {

  private Criteria criteria;

  public MyExample(Class<?> entityClass) {
    super(entityClass);
  }

  public MyExample(Class<?> entityClass, boolean exists) {
    super(entityClass, exists);
  }

  public MyExample(Class<?> entityClass, boolean exists, boolean notNull) {
    super(entityClass, exists, notNull);
  }

  /**
   * .
   *
   * @return com.hzy.util.MyExample
   * @Author Chensb
   * @Description TODO
   * @Date 2018/10/18 14:42
   * @Param [property, orderBy]
   */
  public MyExample orderBy(String property, String orderby) {
    if (orderby != null && orderby.equals("desc")) {
      this.orderBy(property).desc();
    } else {
      this.orderBy(property).asc();
    }
    return this;
  }

  /**
   * .
   *
   * @return tk.mybatis.mapper.domain.Example.Criteria
   * @Author Chensb
   * @Description TODO
   * @Date 2018/10/18 17:29
   * @Param []
   */
  public Criteria getCriteria() {
    return criteria;
  }

  /**
   * .
   *
   * @return void
   * @Author Chensb
   * @Description TODO
   * @Date 2018/10/18 17:29
   * @Param [criteria]
   */
  public void setCriteria(Criteria criteria) {
    this.criteria = criteria;
  }
}
