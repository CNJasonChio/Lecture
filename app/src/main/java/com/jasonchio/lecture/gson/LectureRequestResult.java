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
public class LectureRequestResult {

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
	private Object lecture_source;
	private String lecture_url;
	private int lecture_fans_amount;

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

	public Object getLecture_source() {
		return lecture_source;
	}

	public void setLecture_source(Object lecture_source) {
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
}
