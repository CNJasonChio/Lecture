package com.jasonchio.lecture.greendao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

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

@Entity
public class InterimLectureDB {

	@Id
	long lectureId;       //讲座ID

	String lectureTitle;    //讲座标题

	String lectureLocation;    //讲座地点

	String lectureTime;     //讲座时间

	String lecutreSource;   //讲座的来源

	String lectureContent;  //讲座正文

	String lectureUrl;      //讲座原文URL

	int lecutreLikers;      //讲座收藏数

	String lectureImage;    //讲座图片

	int isWanted;

	@Generated(hash = 1537006485)
	public InterimLectureDB(long lectureId, String lectureTitle,
			String lectureLocation, String lectureTime, String lecutreSource,
			String lectureContent, String lectureUrl, int lecutreLikers,
			String lectureImage, int isWanted) {
		this.lectureId = lectureId;
		this.lectureTitle = lectureTitle;
		this.lectureLocation = lectureLocation;
		this.lectureTime = lectureTime;
		this.lecutreSource = lecutreSource;
		this.lectureContent = lectureContent;
		this.lectureUrl = lectureUrl;
		this.lecutreLikers = lecutreLikers;
		this.lectureImage = lectureImage;
		this.isWanted = isWanted;
	}

	@Generated(hash = 1900539246)
	public InterimLectureDB() {
	}

	public long getLectureId() {
		return this.lectureId;
	}

	public void setLectureId(long lectureId) {
		this.lectureId = lectureId;
	}

	public String getLectureTitle() {
		return this.lectureTitle;
	}

	public void setLectureTitle(String lectureTitle) {
		this.lectureTitle = lectureTitle;
	}

	public String getLectureLocation() {
		return this.lectureLocation;
	}

	public void setLectureLocation(String lectureLocation) {
		this.lectureLocation = lectureLocation;
	}

	public String getLectureTime() {
		return this.lectureTime;
	}

	public void setLectureTime(String lectureTime) {
		this.lectureTime = lectureTime;
	}

	public String getLecutreSource() {
		return this.lecutreSource;
	}

	public void setLecutreSource(String lecutreSource) {
		this.lecutreSource = lecutreSource;
	}

	public String getLectureContent() {
		return this.lectureContent;
	}

	public void setLectureContent(String lectureContent) {
		this.lectureContent = lectureContent;
	}

	public String getLectureUrl() {
		return this.lectureUrl;
	}

	public void setLectureUrl(String lectureUrl) {
		this.lectureUrl = lectureUrl;
	}

	public int getLecutreLikers() {
		return this.lecutreLikers;
	}

	public void setLecutreLikers(int lecutreLikers) {
		this.lecutreLikers = lecutreLikers;
	}

	public String getLectureImage() {
		return this.lectureImage;
	}

	public void setLectureImage(String lectureImage) {
		this.lectureImage = lectureImage;
	}

	public int getIsWanted() {
		return this.isWanted;
	}

	public void setIsWanted(int isWanted) {
		this.isWanted = isWanted;
	}

}
