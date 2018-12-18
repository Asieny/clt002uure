package cn.com.mytest.anno;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * @authorwangcw
 */
public class ReflectUtils {
	public static Field getField(Class<?> clazz, String fieldName) throws NoSuchFieldException {
		try {
			return clazz.getDeclaredField(fieldName);
		} catch (NoSuchFieldException e) {
			Class<?> superClazz = clazz.getSuperclass();
			if (superClazz != null) {
				return getField(superClazz, fieldName);
			}
			throw e;
		}
	}

	public static List<Field> getFields(Class<?> clazz, Class<?> stopSearch) {
		List<Field> array = new ArrayList<Field>();
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			array.add(field);
		}
		if (stopSearch != null) {
			Class<?> superClazz = clazz.getSuperclass();
			if (superClazz != null && stopSearch.isAssignableFrom(superClazz)) {
				array.addAll(getFields(superClazz, stopSearch));
			}
		}
		return array;
	}

	public static Object getFieldValue(Object object, String fieldName) throws NoSuchFieldException, IllegalAccessException {
		Field field = getField(object.getClass(), fieldName);
		field.setAccessible(true);
		return field.get(object);
	}

	/**获取基本类型变量值，对于复合类型将返回null**/
	public static final String getBasicFieldValue(Field field, Object object) {
		String value = null;
		try {
			Class<?> fieldType = field.getType();
			if(fieldType.isPrimitive()
					|| String.class.isAssignableFrom(fieldType)
					|| Byte.class.isAssignableFrom(fieldType)
					|| Character.class.isAssignableFrom(fieldType)
					|| Integer.class.isAssignableFrom(fieldType)
					|| Long.class.isAssignableFrom(fieldType)
					|| BigInteger.class.isAssignableFrom(fieldType)
					|| Float.class.isAssignableFrom(fieldType)
					|| Double.class.isAssignableFrom(fieldType)
					|| BigDecimal.class.isAssignableFrom(fieldType)) {
				/*if(!field.isAccessible()) {	//不论是否public的都返回false
						field.setAccessible(true);
					}*/
				field.setAccessible(true);
				Object valueObj = field.get(object);
				value = valueObj==null ? null : valueObj.toString();
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return value;
	}
}
