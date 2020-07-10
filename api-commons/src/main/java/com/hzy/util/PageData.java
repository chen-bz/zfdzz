package com.hzy.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class PageData<K, V> extends HashMap<Object, Object> implements Map<Object, Object> {

	private static final long serialVersionUID = 12164651654616L;

	Map<Object, Object> map = null;
	private Integer currentPage = 1;
	private Integer pageSize = 10;
	HttpServletRequest request;

	public PageData(HttpServletRequest request) {
		this.request = request;
		Map<String, String[]> properties = request.getParameterMap();
		Map<Object, Object> returnMap = new HashMap<Object, Object>();
		Iterator<Entry<String, String[]>> entries = properties.entrySet().iterator();
		Entry entry;
		String name = "";
		String value = "";
		while (entries.hasNext()) {
			entry = (Entry) entries.next();
			name = (String) entry.getKey();
			Object valueObj = entry.getValue();
			if (null == valueObj) {
				value = "";
			} else if (valueObj instanceof String[]) {
				String[] values = (String[]) valueObj;
				for (int i = 0; i < values.length; i++) {
					value = values[i] + ",";
				}
				value = value.substring(0, value.length() - 1);
			} else {
				value = valueObj.toString();
			}
			returnMap.put(name, value);
			if ("pageno".equals(name) && value != null && !"".equals(value)) {
				currentPage = Integer.valueOf(value);
			}
			if ("pageSize".equals(name) && value != null && !"".equals(value)) {
				pageSize = Integer.valueOf(value);
			}
		}
		map = returnMap;
	}

	public PageData() {
		map = new HashMap<Object, Object>();
	}

	/**
	 * 创建并且添加参数，参数必须为偶数个，即key1,value1,key2,value2...顺序排列
	 * @Title:PageData 
	 * @Description:TODO 
	 * @param objects
	 */
	public PageData(Object... objects) {
		map = new HashMap<Object, Object>();
		for (int i = 0; i < objects.length; i += 2) {
			map.put(objects[i], ((i + 1) < objects.length) ? objects[i + 1] : null);
		}
	}

	@Override
	public Object get(Object key) {
		Object obj = null;
		if (map.get(key) instanceof Object[]) {
			Object[] arr = (Object[]) map.get(key);
			obj = request == null ? arr : (request.getParameter((String) key) == null ? arr : arr[0]);
		} else {
			obj = map.get(key);
		}
		return obj;
	}

	public String getString(Object key) {
		return (String) get(key);
	}

	@Override
	public Object put(Object key, Object value) {
		return map.put(key, value);
	}

	@Override
	public Object remove(Object key) {
		return map.remove(key);
	}

	public void clear() {
		map.clear();
	}

	public boolean containsKey(Object key) {
		// TODO Auto-generated method stub
		return map.containsKey(key);
	}

	public boolean containsValue(Object value) {
		// TODO Auto-generated method stub
		return map.containsValue(value);
	}

	public Set entrySet() {
		// TODO Auto-generated method stub
		return map.entrySet();
	}

	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return map.isEmpty();
	}

	public Set keySet() {
		// TODO Auto-generated method stub
		return map.keySet();
	}

	public void putAll(Map t) {
		// TODO Auto-generated method stub
		map.putAll(t);
	}

	public int size() {
		// TODO Auto-generated method stub
		return map.size();
	}

	public Collection values() {
		// TODO Auto-generated method stub
		return map.values();
	}

	/**
	 * @方法名: getCurrentPage
	 * @描述: TODO 当前页
	 * @return
	 * @返回类型: Integer @修改人： @修改时间： @修改备注：
	 */
	public Integer getCurrentPage() {
		return currentPage;
	}

	/**
	 * @方法名: setCurrentPage
	 * @描述: TODO 当前页
	 * @param currentPage
	 * @返回类型: void @修改人： @修改时间： @修改备注：
	 */
	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

	/**
	 * @方法名: getPageSize
	 * @描述: TODO 每页条数
	 * @return
	 * @返回类型: Integer @修改人： @修改时间： @修改备注：
	 */
	public Integer getPageSize() {
		return pageSize;
	}

	/**
	 * @方法名: setPageSize
	 * @描述: TODO 每页条数
	 * @param pageSize
	 * @返回类型: void @修改人： @修改时间： @修改备注：
	 */
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

}
