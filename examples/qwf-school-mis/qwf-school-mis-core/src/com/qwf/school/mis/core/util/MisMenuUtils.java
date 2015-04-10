package com.qwf.school.mis.core.util;

import java.util.LinkedHashMap;
import java.util.Map;

public class MisMenuUtils {

	private static Map<String, Map<String, String>> allMenuMap = new LinkedHashMap<String, Map<String, String>>();

	public static Map<String, Map<String, String>> getAllMenuMap() {
		return allMenuMap;
	}

	/**
	 * 注册菜单
	 * 
	 * @param menuName
	 * @param subMenuMap
	 */
	public static void registerMenu(String menuName,
			Map<String, String> subMenuMap) {
		Map<String, String> menuSubMenuMap = null;
		if (allMenuMap.containsKey(menuName)) {
			menuSubMenuMap = allMenuMap.get(menuName);
		} else {
			menuSubMenuMap = new LinkedHashMap<String, String>();
			allMenuMap.put(menuName, menuSubMenuMap);
		}
		for (String key : subMenuMap.keySet()) {
			if (menuSubMenuMap.containsKey(key)) {
				continue;
			}
			menuSubMenuMap.put(key, subMenuMap.get(key));
		}
	}

	/**
	 * 移除菜单
	 * 
	 * @param menuName
	 * @param subMenuName
	 */
	public static void removeMenu(String menuName, String subMenuName) {
		if (!allMenuMap.containsKey(menuName)) {
			return;
		}
		if (subMenuName == null) {
			allMenuMap.remove(menuName);
		} else {
			Map<String, String> subMenuMap = allMenuMap.get(menuName);
			subMenuMap.remove(subMenuName);
		}
	}
}
