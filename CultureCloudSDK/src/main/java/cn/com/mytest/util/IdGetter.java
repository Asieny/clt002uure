package cn.com.mytest.util;

import android.content.Context;
import android.util.AndroidRuntimeException;

/**
 * @author wangcw
 */
public class IdGetter {
	public static final String anim			= "anim";
	public static final String attr			= "attr";
	public static final String color		= "color";
	public static final String dimen		= "dimen";
	public static final String drawable		= "drawable";
	public static final String id			= "id";
	public static final String layout		= "layout";
	public static final String raw			= "raw";
	public static final String string		= "string";
	public static final String style		= "style";
	public static final String styleable	= "styleable";

	public static int getAnimId(Context context, String name) {
		return getId(context, name, anim, true);
	}
	
	public static int getAttrId(Context context, String name) {
		return getId(context, name, attr, true);
	}
	
	public static int getColorId(Context context, String name) {
		return getId(context, name, color, true);
	}
	
	public static int getDimenId(Context context, String name) {
		return getId(context, name, dimen, true);
	}
	
	public static int getDrawableId(Context context, String name) {
		return getId(context, name, drawable, true);
	}
	
	public static int getIdId(Context context, String name) {
		return getId(context, name, id, true);
	}
	
	public static int getLayoutId(Context context, String name) {
		return getId(context, name, layout, true);
	}
	
	public static int getRawId(Context context, String name) {
		return getId(context, name, raw, true);
	}
	
	public static int getStringId(Context context, String name) {
		return getId(context, name, string, true);
	}
	
	public static int getStyleId(Context context, String name) {
		return getId(context, name, style, true);
	}
	
	public static int getStyleableId(Context context, String name) {
		return getId(context, name, styleable, true);
	}
	
	public static int getId(Context context, String name, String type, boolean check) {
		int id = context.getResources().getIdentifier(name, type, context.getPackageName());
		if(check) checkId(id, name, type);
		return id;
	}

	public static int getId(Context context, String name, String type, int defId) {
		int id = context.getResources().getIdentifier(name, type, context.getPackageName());
		id = id == 0 ? defId : id;
		checkId(id, name, type);
		return id;
	}

	private static void checkId(int id, String name, String type) {
		if(id == 0) throw new AndroidRuntimeException("缺少id：R." + type + "." + name);
	}
}
