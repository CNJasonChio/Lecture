package com.jasonchio.lecture.database;

import org.litepal.crud.DataSupport;

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
 * Created by zhaoyaobang on 2018/3/23.
 */

public class InterimLectureDB extends DataSupport {

	int lectureId;       //讲座ID

	String lectureTitle;    //讲座标题

	String lectureLocation;    //讲座地点

	String lectureTime;     //讲座时间

	String lecutreSource;   //讲座的来源

	String lectureContent;  //讲座正文

	String lectureUrl;      //讲座原文URL

	int lecutreLikers;      //讲座收藏数

	String lectureImage;    //讲座图片


	public InterimLectureDB(String lectureTitle, String lectureTime, String lecutreSource, String lectureContent, int lecutreLikers) {
		this.lectureTitle = lectureTitle;
		this.lectureLocation = lectureLocation;
		this.lectureTime = lectureTime;
		this.lecutreSource = lecutreSource;
		this.lectureContent = lectureContent;
		this.lecutreLikers = lecutreLikers;
	}

	public InterimLectureDB() {
	}

	public String getLectureImage() {
		return lectureImage;
	}

	public void setLectureImage(String lectureImage) {
		this.lectureImage = lectureImage;
	}

	public int getLectureId() {
		return lectureId;
	}

	public void setLectureId(int lectureId) {
		this.lectureId = lectureId;
	}

	public String getLectureTitle() {
		return lectureTitle;
	}

	public void setLectureTitle(String lectureTitle) {
		this.lectureTitle = lectureTitle;
	}

	public String getLectureLocation() {
		return lectureLocation;
	}

	public void setLectureLocation(String lectureLocation) {
		this.lectureLocation = lectureLocation;
	}

	public String getLectureTime() {
		return lectureTime;
	}

	public void setLectureTime(String lectureTime) {
		this.lectureTime = lectureTime;
	}

	public String getLecutreSource() {
		return lecutreSource;
	}

	public void setLecutreSource(String lecutreSource) {
		this.lecutreSource = lecutreSource;
	}

	public String getLecutreUrl() {
		return lectureUrl;
	}

	public void setLecutreUrl(String lecutreUri) {
		this.lectureUrl = lecutreUri;
	}

	public int getLecutreLikers() {
		return lecutreLikers;
	}

	public void setLecutreLikers(int lecutreLikers) {
		this.lecutreLikers = lecutreLikers;
	}


	public String getLectureContent() {
		return lectureContent;
	}

	public void setLectureContent(String lectureContent) {
		this.lectureContent = lectureContent;
	}
}
