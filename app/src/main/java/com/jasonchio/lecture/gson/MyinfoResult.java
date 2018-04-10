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
 * Created by zhaoyaobang on 2018/3/31.
 */
public class MyinfoResult {

	/**
	 * state :
	 * user_info :
	 */

	private int state;
	private UserInfoBean user_info;

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public UserInfoBean getUser_info() {
		return user_info;
	}

	public void setUser_info(UserInfoBean user_info) {
		this.user_info = user_info;
	}

	public static class UserInfoBean {
		/**
		 * user_id :
		 * user_name :
		 * user_phone_num :
		 * user_information :
		 * user_email :
		 * user_face_url :
		 * user_sex :
		 * user_school :
		 */

		private long user_id;
		private String user_name;
		private String user_phone_num;
		private String user_information;
		private String user_email;
		private String user_face_url;
		private String user_sex;
		private String user_school;
		private String user_birthday;
		private double user_longtitude;
		private double user_latitude;

		public long getUser_id() {
			return user_id;
		}

		public void setUser_id(long user_id) {
			this.user_id = user_id;
		}

		public String getUser_name() {
			return user_name;
		}

		public void setUser_name(String user_name) {
			this.user_name = user_name;
		}

		public String getUser_phone_num() {
			return user_phone_num;
		}

		public void setUser_phone_num(String user_phone_num) {
			this.user_phone_num = user_phone_num;
		}

		public String getUser_information() {
			return user_information;
		}

		public void setUser_information(String user_information) {
			this.user_information = user_information;
		}

		public String getUser_email() {
			return user_email;
		}

		public void setUser_email(String user_email) {
			this.user_email = user_email;
		}

		public String getUser_face_url() {
			return user_face_url;
		}

		public void setUser_face_url(String user_face_url) {
			this.user_face_url = user_face_url;
		}

		public String getUser_sex() {
			return user_sex;
		}

		public void setUser_sex(String user_sex) {
			this.user_sex = user_sex;
		}

		public String getUser_school() {
			return user_school;
		}

		public void setUser_school(String user_school) {
			this.user_school = user_school;
		}

		public String getUser_birthday() {
			return user_birthday;
		}

		public void setUser_birthday(String user_birthday) {
			this.user_birthday = user_birthday;
		}

		public double getUser_longtitude() {
			return user_longtitude;
		}

		public void setUser_longtitude(double user_longtitude) {
			this.user_longtitude = user_longtitude;
		}

		public double getUser_latitude() {
			return user_latitude;
		}

		public void setUser_latitude(double user_latitude) {
			this.user_latitude = user_latitude;
		}
	}
}
