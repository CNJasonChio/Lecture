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
public class CommentRequestResult {


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
	private Object comment_time;
	private int comment_good_amount;

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

	public Object getComment_time() {
		return comment_time;
	}

	public void setComment_time(Object comment_time) {
		this.comment_time = comment_time;
	}

	public int getComment_good_amount() {
		return comment_good_amount;
	}

	public void setComment_good_amount(int comment_good_amount) {
		this.comment_good_amount = comment_good_amount;
	}
}
