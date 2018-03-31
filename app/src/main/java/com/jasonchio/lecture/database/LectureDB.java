package com.jasonchio.lecture.database;

import android.net.Uri;

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

public class LectureDB extends DataSupport {

	int lectureId;       //讲座ID

	String lectureTitle;    //讲座标题

	String lecturePlace;    //讲座地点

	String lectureTime;     //讲座时间

	String lecutreSource;   //讲座的来源

	String lectureContent;  //讲座正文

	String lecutreUri;      //讲座原文URL

	int lecutreLikers;      //讲座收藏数

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

	public String getLecturePlace() {
		return lecturePlace;
	}

	public void setLecturePlace(String lecturePlace) {
		this.lecturePlace = lecturePlace;
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

	public String getLecutreUri() {
		return lecutreUri;
	}

	public void setLecutreUri(String lecutreUri) {
		this.lecutreUri = lecutreUri;
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
