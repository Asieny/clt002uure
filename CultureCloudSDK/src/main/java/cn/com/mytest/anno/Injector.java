package cn.com.mytest.anno;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @author wangcw
 */
public class Injector {
	public static void inject(Object injectObj, Activity activity) {
		inject(injectObj, activity, null);
	}

	public static void inject(Object injectObj, Activity activity, Class<?> stopSearch) {
		try {
			injectView(injectObj, activity, stopSearch);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void inject(Object injectObj, Dialog dialog) {
		inject(injectObj, dialog, null);
	}

	public static void inject(Object injectObj, Dialog dialog, Class<?> stopSearch) {
		try {
			injectView(injectObj, dialog, stopSearch);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void inject(Object injectObj, View view) {
		inject(injectObj, view, null);
	}

	public static void inject(Object injectObj, View view, Class<?> stopSearch) {
		try {
			injectView(injectObj, view, stopSearch);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static int layoutID(Class<?> clazz) {
		ViewLayoutId layoutId = clazz.getAnnotation(ViewLayoutId.class);
		return layoutId != null ? layoutId.value() : 0;
	}

	public static int listViewID(Class<?> clazz) {
		ViewListId listId = clazz.getAnnotation(ViewListId.class);
		return listId != null ? listId.value() : 0;
	}

	private static void injectView(Object injectObj, Object contaner, Class<?> stopSearch) throws Exception {
		if(!(contaner instanceof View)) {
			int layoutId = layoutID(injectObj.getClass());
			if(layoutId > 0) {
				if(contaner instanceof Activity) {
					((Activity)contaner).setContentView(layoutId);
				}else if (contaner instanceof Dialog) {
					((Dialog)contaner).setContentView(layoutId);
				}
			}
		}
		List<Field> fields = ReflectUtils.getFields(injectObj.getClass(), stopSearch);
		ViewId viewId;
		View view;
		for (Field field : fields) {
			if (!View.class.isAssignableFrom(field.getType())) continue;
			viewId = field.getAnnotation(ViewId.class);
			if (viewId == null) continue;
			if(contaner instanceof Activity) {
				view = ((Activity)contaner).findViewById(viewId.value());
			}else if(contaner instanceof Dialog) {
				view = ((Dialog)contaner).findViewById(viewId.value());
			}else if(contaner instanceof View) {
				view = ((View)contaner).findViewById(viewId.value());
			}else {
				view = null;
			}
			if(view != null) {
				field.setAccessible(true);
				field.set(injectObj, view);
			}
		}
	}
}