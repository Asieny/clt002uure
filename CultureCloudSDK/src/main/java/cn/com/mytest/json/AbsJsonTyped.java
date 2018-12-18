package cn.com.mytest.json;

import org.json.JSONException;
import org.json.JSONObject;

import cn.com.mytest.L;

import com.google.gson.reflect.TypeToken;

/**
 * @author wangcw
 */
public abstract class AbsJsonTyped<T extends AbsJsonTyped<T>> extends AbsJson<T> {
	private String absDataBelongToType;	//子类变量名不能与本变量名重复，否则gson反序列化会报错，这里取复杂一点防止重复

	protected boolean parseType(JSONObject json) {
		String typeKey = typeKey();
		if (json.has(typeKey)) {
			try {
				absDataBelongToType = json.getString(typeKey);
				return absDataBelongToType != null;
			} catch (JSONException e) {
				L.e(this, e);
			}
		}
		return false;
	}

	@Override
	public boolean isBelongToMe(JSONObject json) {
		return parseType(json) && equalsTypeValue();
	}

	private boolean equalsTypeValue() {
		for (String t : typeValues()) {
			if (t.equals(absDataBelongToType)) return true;
		}
		return false;
	}

	@Override
	public T fromJson(String json) {
		return fromJsonWithAllFields(json, getTypeToken());
	}

	@Override
	public String toJson() {
		return toJsonWithAllFields(this);
	}

	protected abstract String typeKey();
	protected abstract String[] typeValues();
	protected abstract TypeToken<T> getTypeToken();
}
