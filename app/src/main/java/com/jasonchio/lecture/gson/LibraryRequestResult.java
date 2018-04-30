package com.jasonchio.lecture.gson;

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
	 * state :
	 * library_info :
	 */

	private int state;
	private LibraryInfoBean library_info;

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
		 * library_id : 1
		 * library_name : 武汉理工大学图书馆
		 * library_picture_url : null
		 * library_information : null
		 * library_address_url : null
		 * library_longtitude : null
		 * library_latitude : null
		 * user_focus_lib :
		 */

		private int library_id;
		private String library_name;
		private String library_picture_url;
		private String library_information;
		private String library_address_url;
		private double library_longtitude;
		private double library_latitude;
		private int user_focus_lib;

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

		public int getUser_focus_lib() {
			return user_focus_lib;
		}

		public void setUser_focus_lib(int user_focus_lib) {
			this.user_focus_lib = user_focus_lib;
		}
	}
}
