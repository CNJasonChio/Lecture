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
 * Created by zhaoyaobang on 2018/4/3.
 */
public class LibraryRequestResult {


	/**
	 * state : 0
	 * library_info :
	 * */

	private int state;
	private LibraryInfoBean library_info;

	public static LibraryRequestResult objectFromData(String str) {

		return new Gson().fromJson(str, LibraryRequestResult.class);
	}

	public static LibraryRequestResult objectFromData(String str, String key) {

		try {
			JSONObject jsonObject = new JSONObject(str);

			return new Gson().fromJson(jsonObject.getString(str), LibraryRequestResult.class);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static List <LibraryRequestResult> arrayLibraryRequestResultFromData(String str) {

		Type listType = new TypeToken <ArrayList <LibraryRequestResult>>() {
		}.getType();

		return new Gson().fromJson(str, listType);
	}

	public static List <LibraryRequestResult> arrayLibraryRequestResultFromData(String str, String key) {

		try {
			JSONObject jsonObject = new JSONObject(str);
			Type listType = new TypeToken <ArrayList <LibraryRequestResult>>() {
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

	public LibraryInfoBean getLibrary_info() {
		return library_info;
	}

	public void setLibrary_info(LibraryInfoBean library_info) {
		this.library_info = library_info;
	}

	public static class LibraryInfoBean {
		/**
		 * library_id :
		 * library_name :
		 * library_picture_url :
		 * library_information :
		 * library_address_url :
		 * library_longtitude :
		 * library_latitude :
		 * library_range :
		 * user_focus_lib :
		 */

		private int library_id;
		private String library_name;
		private String library_picture_url;
		private String library_information;
		private String library_address_url;
		private double library_longtitude;
		private double library_latitude;
		private String library_range;
		private int user_focus_lib;

		public static LibraryInfoBean objectFromData(String str) {

			return new Gson().fromJson(str, LibraryInfoBean.class);
		}

		public static LibraryInfoBean objectFromData(String str, String key) {

			try {
				JSONObject jsonObject = new JSONObject(str);

				return new Gson().fromJson(jsonObject.getString(str), LibraryInfoBean.class);
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		public static List <LibraryInfoBean> arrayLibraryInfoBeanFromData(String str) {

			Type listType = new TypeToken <ArrayList <LibraryInfoBean>>() {
			}.getType();

			return new Gson().fromJson(str, listType);
		}

		public static List <LibraryInfoBean> arrayLibraryInfoBeanFromData(String str, String key) {

			try {
				JSONObject jsonObject = new JSONObject(str);
				Type listType = new TypeToken <ArrayList <LibraryInfoBean>>() {
				}.getType();

				return new Gson().fromJson(jsonObject.getString(str), listType);

			} catch (JSONException e) {
				e.printStackTrace();
			}

			return new ArrayList();


		}

		public int getLibrary_id() {
			return library_id;
		}

		public void setLibrary_id(int library_id) {
			this.library_id = library_id;
		}

		public String getLibrary_name() {
			return library_name;
		}

		public void setLibrary_name(String library_name) {
			this.library_name = library_name;
		}

		public String getLibrary_picture_url() {
			return library_picture_url;
		}

		public void setLibrary_picture_url(String library_picture_url) {
			this.library_picture_url = library_picture_url;
		}

		public String getLibrary_information() {
			return library_information;
		}

		public void setLibrary_information(String library_information) {
			this.library_information = library_information;
		}

		public String getLibrary_address_url() {
			return library_address_url;
		}

		public void setLibrary_address_url(String library_address_url) {
			this.library_address_url = library_address_url;
		}

		public double getLibrary_longtitude() {
			return library_longtitude;
		}

		public void setLibrary_longtitude(double library_longtitude) {
			this.library_longtitude = library_longtitude;
		}

		public double getLibrary_latitude() {
			return library_latitude;
		}

		public void setLibrary_latitude(double library_latitude) {
			this.library_latitude = library_latitude;
		}

		public String getLibrary_range() {
			return library_range;
		}

		public void setLibrary_range(String library_range) {
			this.library_range = library_range;
		}

		public int getUser_focus_lib() {
			return user_focus_lib;
		}

		public void setUser_focus_lib(int user_focus_lib) {
			this.user_focus_lib = user_focus_lib;
		}
	}
}
