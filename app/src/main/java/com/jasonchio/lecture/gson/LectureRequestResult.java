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
public class LectureRequestResult {


	/**
	 * state :
	 * lecture :
	 * */

	private int state;
	private List<LectureBean> lecture;

	public static LectureRequestResult objectFromData(String str) {

		return new Gson().fromJson(str, LectureRequestResult.class);
	}

	public static LectureRequestResult objectFromData(String str, String key) {

		try {
			JSONObject jsonObject = new JSONObject(str);

			return new Gson().fromJson(jsonObject.getString(str), LectureRequestResult.class);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static List<LectureRequestResult> arrayLectureRequestResultFromData(String str) {

		Type listType = new TypeToken<ArrayList<LectureRequestResult>>() {
		}.getType();

		return new Gson().fromJson(str, listType);
	}

	public static List<LectureRequestResult> arrayLectureRequestResultFromData(String str, String key) {

		try {
			JSONObject jsonObject = new JSONObject(str);
			Type listType = new TypeToken<ArrayList<LectureRequestResult>>() {
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

	public List<LectureBean> getLecture() {
		return lecture;
	}

	public void setLecture(List<LectureBean> lecture) {
		this.lecture = lecture;
	}

	public static class LectureBean {
		/**
		 * lecture_id :
		 * lecture_title :
		 * lecture_location :
		 * lecture_time :
		 * lecture_source :
		 * lecture_url :
		 * lecture_fans_amount :
		 */

		private int lecture_id;
		private String lecture_title;
		private String lecture_location;
		private String lecture_time;
		private String lecture_source;
		private String lecture_url;
		private int lecture_fans_amount;
		private String lecture_information;

		public static LectureBean objectFromData(String str) {

			return new Gson().fromJson(str, LectureBean.class);
		}

		public static LectureBean objectFromData(String str, String key) {

			try {
				JSONObject jsonObject = new JSONObject(str);

				return new Gson().fromJson(jsonObject.getString(str), LectureBean.class);
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		public static List<LectureBean> arrayLectureBeanFromData(String str) {

			Type listType = new TypeToken<ArrayList<LectureBean>>() {
			}.getType();

			return new Gson().fromJson(str, listType);
		}

		public static List<LectureBean> arrayLectureBeanFromData(String str, String key) {

			try {
				JSONObject jsonObject = new JSONObject(str);
				Type listType = new TypeToken<ArrayList<LectureBean>>() {
				}.getType();

				return new Gson().fromJson(jsonObject.getString(str), listType);

			} catch (JSONException e) {
				e.printStackTrace();
			}

			return new ArrayList();


		}

		public int getLecture_id() {
			return lecture_id;
		}

		public void setLecture_id(int lecture_id) {
			this.lecture_id = lecture_id;
		}

		public String getLecture_title() {
			return lecture_title;
		}

		public void setLecture_title(String lecture_title) {
			this.lecture_title = lecture_title;
		}

		public String getLecture_location() {
			return lecture_location;
		}

		public void setLecture_location(String lecture_location) {
			this.lecture_location = lecture_location;
		}

		public String getLecture_time() {
			return lecture_time;
		}

		public void setLecture_time(String lecture_time) {
			this.lecture_time = lecture_time;
		}

		public String getLecture_source() {
			return lecture_source;
		}

		public void setLecture_source(String lecture_source) {
			this.lecture_source = lecture_source;
		}

		public String getLecture_url() {
			return lecture_url;
		}

		public void setLecture_url(String lecture_url) {
			this.lecture_url = lecture_url;
		}

		public int getLecture_fans_amount() {
			return lecture_fans_amount;
		}

		public void setLecture_fans_amount(int lecture_fans_amount) {
			this.lecture_fans_amount = lecture_fans_amount;
		}

		public String getLecture_information() {
			return lecture_information;
		}

		public void setLecture_information(String lecture_information) {
			this.lecture_information = lecture_information;
		}
	}
}
