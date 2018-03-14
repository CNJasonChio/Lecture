package com.jasonchio.lecture;

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
 * Created by zhaoyaobang on 2018/3/10.
 */

public class Lecture {

	private String lectureTitle;

	private String lectureTime;

	private String lectureSource;

	private int lectureLikers;

	private String lectureContent;

	private int lectureImageId;

	public Lecture(String lectureTitle, String lectureTime, String lectureSource, int lectureLikers, String lectureContent, int lectureImageId) {
		this.lectureTitle = lectureTitle;
		this.lectureTime = lectureTime;
		this.lectureSource = lectureSource;
		this.lectureLikers = lectureLikers;
		this.lectureContent = lectureContent;
		this.lectureImageId = lectureImageId;
	}

	public String getLectureTitle() {
		return lectureTitle;
	}

	public void setLectureTitle(String lectureTitle) {
		this.lectureTitle = lectureTitle;
	}

	public String getLectureTime() {
		return lectureTime;
	}

	public void setLectureTime(String lectureTime) {
		this.lectureTime = lectureTime;
	}

	public String getLectureSource() {
		return lectureSource;
	}

	public void setLectureSource(String lectureSource) {
		this.lectureSource = lectureSource;
	}

	public int getLectureLikers() {
		return lectureLikers;
	}

	public void setLectureLikers(int lectureLikers) {
		this.lectureLikers = lectureLikers;
	}

	public String getLectureContent() {
		return lectureContent;
	}

	public void setLectureContent(String lectureContent) {
		this.lectureContent = lectureContent;
	}

	public int getLectureImageId() {
		return lectureImageId;
	}

	public void setLectureImageId(int lectureImageId) {
		this.lectureImageId = lectureImageId;
	}
}
