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
	 * state : 0
	 * user_info : {"user_id":4,"user_name":null,"user_phone_num":"15871714056","user_information":null,"user_email":null,"user_face_url":null,"user_sex":null,"user_school":null}
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
		 * user_id : 4
		 * user_name : null
		 * user_phone_num : 15871714056
		 * user_information : null
		 * user_email : null
		 * user_face_url : null
		 * user_sex : null
		 * user_school : null
		 */

		private int user_id;
		private Object user_name;
		private String user_phone_num;
		private Object user_information;
		private Object user_email;
		private Object user_face_url;
		private Object user_sex;
		private Object user_school;

		public int getUser_id() {
			return user_id;
		}

		public void setUser_id(int user_id) {
			this.user_id = user_id;
		}

		public Object getUser_name() {
			return user_name;
		}

		public void setUser_name(Object user_name) {
			this.user_name = user_name;
		}

		public String getUser_phone_num() {
			return user_phone_num;
		}

		public void setUser_phone_num(String user_phone_num) {
			this.user_phone_num = user_phone_num;
		}

		public Object getUser_information() {
			return user_information;
		}

		public void setUser_information(Object user_information) {
			this.user_information = user_information;
		}

		public Object getUser_email() {
			return user_email;
		}

		public void setUser_email(Object user_email) {
			this.user_email = user_email;
		}

		public Object getUser_face_url() {
			return user_face_url;
		}

		public void setUser_face_url(Object user_face_url) {
			this.user_face_url = user_face_url;
		}

		public Object getUser_sex() {
			return user_sex;
		}

		public void setUser_sex(Object user_sex) {
			this.user_sex = user_sex;
		}

		public Object getUser_school() {
			return user_school;
		}

		public void setUser_school(Object user_school) {
			this.user_school = user_school;
		}
	}
}
