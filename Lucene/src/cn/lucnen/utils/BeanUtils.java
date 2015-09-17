package cn.lucnen.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Map;

import cn.lucene.model.FileInformation;

public class BeanUtils {

	public static Object populate(Object bean, Map<String, String[]> map)
			throws IllegalArgumentException, IllegalAccessException,
			SecurityException, NoSuchMethodException, InvocationTargetException {
		String property = "";
		Field[] fields = bean.getClass().getDeclaredFields();
		for (Field field : fields) {
			property = field.getName();
			if (field.getType().isArray() && map.containsKey(property + "[]")) {
				invokeMethod(bean, property, field, map.get(property + "[]"));
			} else if (field.getType().equals(String.class)
					&& map.containsKey(property)) {
				invokeMethod(bean, property, field, map.get(property)[0]);
			} else if (field.getType().equals(Date.class)) {
				invokeMethod(bean, property, field, new SimpleDateFormat(
						"yyyy-MM-dd").format(map.get(property)));
			} else if (map.containsKey(property)) {
				invokeMethod(bean, property, field, map.get(property));
			}
		}
		return bean;
	}

	public static void invokeMethod(Object bean, String property, Field field,
			Object value) throws IllegalArgumentException, SecurityException,
			IllegalAccessException, InvocationTargetException,
			NoSuchMethodException {
		bean.getClass()
				.getDeclaredMethod(getSetMethod(property), field.getType())
				.invoke(bean, value);
	}

	public static String getSetMethod(String property) {
		return "set" + property.substring(0, 1).toUpperCase()
				+ property.substring(1);
	}
	
	@SuppressWarnings("unchecked")
	public static ArrayList<FileInformation> addFileInformationList(String className, String arrayListName, Object object) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SecurityException, NoSuchFieldException {
		FileInformation fileInformation = (FileInformation) object;
		Class<?> clazz = Class.forName(className);
		FileInformation file = (FileInformation) clazz.newInstance();
		Field array = file.getClass().getField(arrayListName);
		ArrayList<FileInformation> fileList = (ArrayList<FileInformation>) array.get(file);
		fileList.add(fileInformation);
		return fileList;
	}
}
