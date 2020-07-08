package com.hzy.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @项目名称: xplan
 * @类名: Setting
 * @描述: TODO
 * @作者: Chensb
 * @创建时间: 2018/12/27 14:40
 * @修改人：
 * @修改时间：
 * @修改备注：
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Setting {
  private String s_key;
  private String s_value;
  private String type_id;
  private String name;
  private String disabled;
  private String intro;
  private String setting_id;
  private String value_type;
  private String type_json;

}
