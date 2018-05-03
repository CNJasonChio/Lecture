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
 * Created by zhaoyaobang on 2018/2/21.
 */

@Entity
public class UserDB {

	@Id
	long userId;          //用户ID

	String userName;        //用户名

	String userPhone;       //用户手机号

	String userSex;         //用户性别

	String userSchool;      //用户所在学校

	String userPhotoUrl;    //用户头像的URL

	String userBirthday;    //用户的生日

	double userLatitude;    //用户纬度

	double userLongitude;   //用户经度

	String userFocuseLirary;    //用户关注的图书馆

	String userWantedLecture;   //用户想看的讲座

	String userComment;     //用户的评论

	String recommentLectureOrder;   //针对该用户推荐的讲座次序

	@Generated(hash = 1414690546)
	public UserDB(long userId, String userName, String userPhone, String userSex,
			String userSchool, String userPhotoUrl, String userBirthday,
			double userLatitude, double userLongitude, String userFocuseLirary,
			String userWantedLecture, String userComment, String recommentLectureOrder) {
		this.userId = userId;
		this.userName = userName;
		this.userPhone = userPhone;
		this.userSex = userSex;
		this.userSchool = userSchool;
		this.userPhotoUrl = userPhotoUrl;
		this.userBirthday = userBirthday;
		this.userLatitude = userLatitude;
		this.userLongitude = userLongitude;
		this.userFocuseLirary = userFocuseLirary;
		this.userWantedLecture = userWantedLecture;
		this.userComment = userComment;
		this.recommentLectureOrder = recommentLectureOrder;
	}

	@Generated(hash = 1312299826)
	public UserDB() {
	}

	public long getUserId() {
		return this.userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPhone() {
		return this.userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	public String getUserSex() {
		return this.userSex;
	}

	public void setUserSex(String userSex) {
		this.userSex = userSex;
	}

	public String getUserSchool() {
		return this.userSchool;
	}

	public void setUserSchool(String userSchool) {
		this.userSchool = userSchool;
	}

	public String getUserPhotoUrl() {
		return this.userPhotoUrl;
	}

	public void setUserPhotoUrl(String userPhotoUrl) {
		this.userPhotoUrl = userPhotoUrl;
	}

	public String getUserBirthday() {
		return this.userBirthday;
	}

	public void setUserBirthday(String userBirthday) {
		this.userBirthday = userBirthday;
	}

	public double getUserLatitude() {
		return this.userLatitude;
	}

	public void setUserLatitude(double userLatitude) {
		this.userLatitude = userLatitude;
	}

	public double getUserLongitude() {
		return this.userLongitude;
	}

	public void setUserLongitude(double userLongitude) {
		this.userLongitude = userLongitude;
	}

	public String getUserFocuseLirary() {
		return this.userFocuseLirary;
	}

	public void setUserFocuseLirary(String userFocuseLirary) {
		this.userFocuseLirary = userFocuseLirary;
	}

	public String getUserWantedLecture() {
		return this.userWantedLecture;
	}

	public void setUserWantedLecture(String userWantedLecture) {
		this.userWantedLecture = userWantedLecture;
	}

	public String getUserComment() {
		return this.userComment;
	}

	public void setUserComment(String userComment) {
		this.userComment = userComment;
	}

	public String getRecommentLectureOrder() {
		return this.recommentLectureOrder;
	}

	public void setRecommentLectureOrder(String recommentLectureOrder) {
		this.recommentLectureOrder = recommentLectureOrder;
	}
	
}
