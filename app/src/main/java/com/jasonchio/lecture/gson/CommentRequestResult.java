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
public class CommentRequestResult {


	/**
	 * state : 0
	 * comment :
	 */

	private int state;
	private List<CommentBean> comment;

	public static CommentRequestResult objectFromData(String str) {

		return new Gson().fromJson(str, CommentRequestResult.class);
	}

	public static CommentRequestResult objectFromData(String str, String key) {

		try {
			JSONObject jsonObject = new JSONObject(str);

			return new Gson().fromJson(jsonObject.getString(str), CommentRequestResult.class);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static List<CommentRequestResult> arrayCommentRequestResultFromData(String str) {

		Type listType = new TypeToken<ArrayList<CommentRequestResult>>() {
		}.getType();

		return new Gson().fromJson(str, listType);
	}

	public static List<CommentRequestResult> arrayCommentRequestResultFromData(String str, String key) {

		try {
			JSONObject jsonObject = new JSONObject(str);
			Type listType = new TypeToken<ArrayList<CommentRequestResult>>() {
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

	public List<CommentBean> getComment() {
		return comment;
	}

	public void setComment(List<CommentBean> comment) {
		this.comment = comment;
	}

	public static class CommentBean {
		/**
		 * comment_id :
		 * comment_user :
		 * comment_information :
		 * comment_lecture :
		 * comment_time :
		 * comment_good_amount :
		 */

		private int comment_id;
		private int comment_user;
		private String comment_information;
		private int comment_lecture;
		private String comment_time;
		private int comment_good_amount;

		public static CommentBean objectFromData(String str) {

			return new Gson().fromJson(str, CommentBean.class);
		}

		public static CommentBean objectFromData(String str, String key) {

			try {
				JSONObject jsonObject = new JSONObject(str);

				return new Gson().fromJson(jsonObject.getString(str), CommentBean.class);
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		public static List<CommentBean> arrayCommentBeanFromData(String str) {

			Type listType = new TypeToken<ArrayList<CommentBean>>() {
			}.getType();

			return new Gson().fromJson(str, listType);
		}

		public static List<CommentBean> arrayCommentBeanFromData(String str, String key) {

			try {
				JSONObject jsonObject = new JSONObject(str);
				Type listType = new TypeToken<ArrayList<CommentBean>>() {
				}.getType();

				return new Gson().fromJson(jsonObject.getString(str), listType);

			} catch (JSONException e) {
				e.printStackTrace();
			}

			return new ArrayList();


		}

		public int getComment_id() {
			return comment_id;
		}

		public void setComment_id(int comment_id) {
			this.comment_id = comment_id;
		}

		public int getComment_user() {
			return comment_user;
		}

		public void setComment_user(int comment_user) {
			this.comment_user = comment_user;
		}

		public String getComment_information() {
			return comment_information;
		}

		public void setComment_information(String comment_information) {
			this.comment_information = comment_information;
		}

		public int getComment_lecture() {
			return comment_lecture;
		}

		public void setComment_lecture(int comment_lecture) {
			this.comment_lecture = comment_lecture;
		}

		public String getComment_time() {
			return comment_time;
		}

		public void setComment_time(String comment_time) {
			this.comment_time = comment_time;
		}

		public int getComment_good_amount() {
			return comment_good_amount;
		}

		public void setComment_good_amount(int comment_good_amount) {
			this.comment_good_amount = comment_good_amount;
		}
	}
}
