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
 * Created by zhaoyaobang on 2018/5/2.
 */
public class RecommentLectureResult {


	/**
	 * state :
	 * recommend :
	 */

	private int state;
	private List <Integer> recommend;

	public static RecommentLectureResult objectFromData(String str) {

		return new Gson().fromJson(str, RecommentLectureResult.class);
	}

	public static RecommentLectureResult objectFromData(String str, String key) {

		try {
			JSONObject jsonObject = new JSONObject(str);

			return new Gson().fromJson(jsonObject.getString(str), RecommentLectureResult.class);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static List <RecommentLectureResult> arrayRecommentLectureResultFromData(String str) {

		Type listType = new TypeToken <ArrayList <RecommentLectureResult>>() {
		}.getType();

		return new Gson().fromJson(str, listType);
	}

	public static List <RecommentLectureResult> arrayRecommentLectureResultFromData(String str, String key) {

		try {
			JSONObject jsonObject = new JSONObject(str);
			Type listType = new TypeToken <ArrayList <RecommentLectureResult>>() {
			}.getType();

			return new Gson().fromJson(jsonObject.getString(str), listType);

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return new ArrayList();


	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public List <Integer> getRecommend() {
		return recommend;
	}

	public void setRecommend(List <Integer> recommend) {
		this.recommend = recommend;
	}
}
