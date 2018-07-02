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
 * Created by zhaoyaobang on 2018/7/2.
 */
public class DynamicsRequestResult {
	/**
	 * state : 0
	 * dynamic : [{"dynamic_id":1,"dynamic_user":"ohlala","dynamic_user_face_url":"http://119.29.93.31:2000/user/3.jpg","dynamic_information":"你读过的书决定你是什么样的人","dynamic_time":"2014-06-18 22:30:37","dynamic_good_amount":0,"dynamic_comment_amount":2,"dynamic_user_like":0},{"dynamic_id":2,"dynamic_user":"学习爱我","dynamic_user_face_url":"http://119.29.93.31:2000/user/2.jpg","dynamic_information":"太饿了，好想吃东西","dynamic_time":"2015-12-15 14:12:35","dynamic_good_amount":0,"dynamic_comment_amount":1,"dynamic_user_like":0},{"dynamic_id":3,"dynamic_user":null,"dynamic_user_face_url":null,"dynamic_information":"最近有两部日剧很好看《Legal High》和《我的危险妻子》","dynamic_time":"2016-03-29 23:06:15","dynamic_good_amount":0,"dynamic_comment_amount":3,"dynamic_user_like":0},{"dynamic_id":4,"dynamic_user":"小猪p7","dynamic_user_face_url":"http://119.29.93.31:2000/user/4.jpg","dynamic_information":"工欲善其事必先利其器","dynamic_time":"2017-08-30 15:12:31","dynamic_good_amount":0,"dynamic_comment_amount":0,"dynamic_user_like":0},{"dynamic_id":5,"dynamic_user":null,"dynamic_user_face_url":null,"dynamic_information":"烤冷面配七喜好像很赞的样子","dynamic_time":"2016-07-19 00:38:52","dynamic_good_amount":0,"dynamic_comment_amount":0,"dynamic_user_like":0},{"dynamic_id":6,"dynamic_user":"最帅的开发邦","dynamic_user_face_url":"http://119.29.93.31:2000/user/62018-06-02-23-25-03,time.localtime(time.time).jpg","dynamic_information":"努力成为一名优秀的程序猿","dynamic_time":"2018-03-30 04:23:33","dynamic_good_amount":0,"dynamic_comment_amount":0,"dynamic_user_like":0},{"dynamic_id":7,"dynamic_user":"最帅的开发邦","dynamic_user_face_url":"http://119.29.93.31:2000/user/62018-06-02-23-25-03,time.localtime(time.time).jpg","dynamic_information":"熬夜伤肝，保重身体","dynamic_time":"2018-04-01 02:32:26","dynamic_good_amount":0,"dynamic_comment_amount":0,"dynamic_user_like":0},{"dynamic_id":8,"dynamic_user":null,"dynamic_user_face_url":null,"dynamic_information":"编程就像和一个无法容忍语法错误的外国人聊天","dynamic_time":"2017-09-01 15:26:39","dynamic_good_amount":0,"dynamic_comment_amount":0,"dynamic_user_like":0},{"dynamic_id":9,"dynamic_user":"小猪p7","dynamic_user_face_url":"http://119.29.93.31:2000/user/4.jpg","dynamic_information":"数据线是当代社会人群的输氧管","dynamic_time":"2017-06-21 12:35:46","dynamic_good_amount":0,"dynamic_comment_amount":0,"dynamic_user_like":0},{"dynamic_id":10,"dynamic_user":"ohlala","dynamic_user_face_url":"http://119.29.93.31:2000/user/3.jpg","dynamic_information":"如果双方球迷都很不满，那么裁判应该很公正了","dynamic_time":"2016-04-23 10:25:33","dynamic_good_amount":0,"dynamic_comment_amount":0,"dynamic_user_like":0}]
	 */

	private int state;
	private List <DynamicBean> dynamic;

	public static DynamicsRequestResult objectFromData(String str) {

		return new Gson().fromJson(str, DynamicsRequestResult.class);
	}

	public static DynamicsRequestResult objectFromData(String str, String key) {

		try {
			JSONObject jsonObject = new JSONObject(str);

			return new Gson().fromJson(jsonObject.getString(str), DynamicsRequestResult.class);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static List <DynamicsRequestResult> arrayDynamicsRequestResultFromData(String str) {

		Type listType = new TypeToken <ArrayList <DynamicsRequestResult>>() {
		}.getType();

		return new Gson().fromJson(str, listType);
	}

	public static List <DynamicsRequestResult> arrayDynamicsRequestResultFromData(String str, String key) {

		try {
			JSONObject jsonObject = new JSONObject(str);
			Type listType = new TypeToken <ArrayList <DynamicsRequestResult>>() {
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

	public List <DynamicBean> getDynamic() {
		return dynamic;
	}

	public void setDynamic(List <DynamicBean> dynamic) {
		this.dynamic = dynamic;
	}

	public static class DynamicBean {
		/**
		 * dynamic_id : 1
		 * dynamic_user : ohlala
		 * dynamic_user_face_url : http://119.29.93.31:2000/user/3.jpg
		 * dynamic_information : 你读过的书决定你是什么样的人
		 * dynamic_time : 2014-06-18 22:30:37
		 * dynamic_good_amount : 0
		 * dynamic_comment_amount : 2
		 * dynamic_user_like : 0
		 */

		private int dynamic_id;
		private String dynamic_user;
		private String dynamic_user_face_url;
		private String dynamic_information;
		private String dynamic_time;
		private int dynamic_good_amount;
		private int dynamic_comment_amount;
		private int dynamic_user_like;

		public static DynamicBean objectFromData(String str) {

			return new Gson().fromJson(str, DynamicBean.class);
		}

		public static DynamicBean objectFromData(String str, String key) {

			try {
				JSONObject jsonObject = new JSONObject(str);

				return new Gson().fromJson(jsonObject.getString(str), DynamicBean.class);
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		public static List <DynamicBean> arrayDynamicBeanFromData(String str) {

			Type listType = new TypeToken <ArrayList <DynamicBean>>() {
			}.getType();

			return new Gson().fromJson(str, listType);
		}

		public static List <DynamicBean> arrayDynamicBeanFromData(String str, String key) {

			try {
				JSONObject jsonObject = new JSONObject(str);
				Type listType = new TypeToken <ArrayList <DynamicBean>>() {
				}.getType();

				return new Gson().fromJson(jsonObject.getString(str), listType);

			} catch (JSONException e) {
				e.printStackTrace();
			}

			return new ArrayList();


		}

		public int getDynamic_id() {
			return dynamic_id;
		}

		public void setDynamic_id(int dynamic_id) {
			this.dynamic_id = dynamic_id;
		}

		public String getDynamic_user() {
			return dynamic_user;
		}

		public void setDynamic_user(String dynamic_user) {
			this.dynamic_user = dynamic_user;
		}

		public String getDynamic_user_face_url() {
			return dynamic_user_face_url;
		}

		public void setDynamic_user_face_url(String dynamic_user_face_url) {
			this.dynamic_user_face_url = dynamic_user_face_url;
		}

		public String getDynamic_information() {
			return dynamic_information;
		}

		public void setDynamic_information(String dynamic_information) {
			this.dynamic_information = dynamic_information;
		}

		public String getDynamic_time() {
			return dynamic_time;
		}

		public void setDynamic_time(String dynamic_time) {
			this.dynamic_time = dynamic_time;
		}

		public int getDynamic_good_amount() {
			return dynamic_good_amount;
		}

		public void setDynamic_good_amount(int dynamic_good_amount) {
			this.dynamic_good_amount = dynamic_good_amount;
		}

		public int getDynamic_comment_amount() {
			return dynamic_comment_amount;
		}

		public void setDynamic_comment_amount(int dynamic_comment_amount) {
			this.dynamic_comment_amount = dynamic_comment_amount;
		}

		public int getDynamic_user_like() {
			return dynamic_user_like;
		}

		public void setDynamic_user_like(int dynamic_user_like) {
			this.dynamic_user_like = dynamic_user_like;
		}
	}
}
