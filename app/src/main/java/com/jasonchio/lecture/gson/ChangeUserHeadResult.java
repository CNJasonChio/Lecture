package com.jasonchio.lecture.gson;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * /**
 * <p>
 * ----------Dragon be here!----------/
 * 　　　┏┓　　　┏┓
 * 　　┏┛┻━━━┛┻┓
 * 　　┃　　　　　　　┃
 * 　　┃　　　━　　　┃
 * 　　┃　┳┛　┗┳　┃
 * 　　┃　　　　　　　┃
 * 　　┃　　　┻　　　┃
 * 　　┃　　　　　　　┃
 * 　　┗━┓　　　┏━┛
 * 　　　　┃　　　┃神兽保佑
 * 　　　　┃　　　┃代码无BUG！
 * 　　　　┃　　　┗━━━┓
 * 　　　　┃　　　　　　　┣┓
 * 　　　　┃　　　　　　　┏┛
 * 　　　　┗┓┓┏━┳┓┏┛
 * 　　　　　┃┫┫　┃┫┫
 * 　　　　　┗┻┛　┗┻┛
 * ━━━━━━神兽出没━━━━━━by:zhaoyaobang
 * <p>
 * Created by zhaoyaobang on 2018/3/31.
 */
public class ChangeUserHeadResult {

	/**
	 * url :
	 * state :
	 */

	private String url;
	private int state;

	public static ChangeUserHeadResult objectFromData(String str) {

		return new Gson().fromJson(str, ChangeUserHeadResult.class);
	}

	public static ChangeUserHeadResult objectFromData(String str, String key) {

		try {
			JSONObject jsonObject = new JSONObject(str);

			return new Gson().fromJson(jsonObject.getString(str), ChangeUserHeadResult.class);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static List <ChangeUserHeadResult> arrayChangeUserHeadResultFromData(String str) {

		Type listType = new TypeToken <ArrayList <ChangeUserHeadResult>>() {
		}.getType();

		return new Gson().fromJson(str, listType);
	}

	public static List <ChangeUserHeadResult> arrayChangeUserHeadResultFromData(String str, String key) {

		try {
			JSONObject jsonObject = new JSONObject(str);
			Type listType = new TypeToken <ArrayList <ChangeUserHeadResult>>() {
			}.getType();

			return new Gson().fromJson(jsonObject.getString(str), listType);

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return new ArrayList();


	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}
}
