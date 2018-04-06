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
		 * lecture_information : null
		 * lecture_address_url : null
		 * lecture_longtitude : null
		 * lecture_latitude : null
		 */

		private int library_id;
		private String library_name;
		private String library_picture_url;
		private String lecture_information;
		private String lecture_address_url;
		private double lecture_longtitude;
		private double lecture_latitude;

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

		public String getLecture_information() {
			return lecture_information;
		}

		public void setLecture_information(String lecture_information) {
			this.lecture_information = lecture_information;
		}

		public String getLecture_address_url() {
			return lecture_address_url;
		}

		public void setLecture_address_url(String lecture_address_url) {
			this.lecture_address_url = lecture_address_url;
		}

		public double getLecture_longtitude() {
			return lecture_longtitude;
		}

		public void setLecture_longtitude(double lecture_longtitude) {
			this.lecture_longtitude = lecture_longtitude;
		}

		public double getLecture_latitude() {
			return lecture_latitude;
		}

		public void setLecture_latitude(double lecture_latitude) {
			this.lecture_latitude = lecture_latitude;
		}
	}
}
