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
 * Created by zhaoyaobang on 2018/2/21.
 */

public class UserDB extends DataSupport {

	int userId;          //用户ID

	String userName;        //用户名

	String userPhone;       //用户手机号

	String userSex;         //用户性别

	String userSchool;      //用户所在学校

	String userPhotoUri;    //用户头像的URL

	double userLatitude;    //用户纬度

	double userLongitude;   //用户经度

	String userFocuseLirary;    //用户关注的图书馆

	String userWantedLecture;   //用户的评论

	String userComment;     //用户的评论

	public String getUserFocuseLirary() {
		return userFocuseLirary;
	}

	public void setUserFocuseLirary(String userFocuseLirary) {
		this.userFocuseLirary = userFocuseLirary;
	}

	public String getUserWantedLecture() {
		return userWantedLecture;
	}

	public void setUserWantedLecture(String userWantedLecture) {
		this.userWantedLecture = userWantedLecture;
	}

	public String getUserComment() {
		return userComment;
	}

	public void setUserComment(String userComment) {
		this.userComment = userComment;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public double getUserLatitude() {
		return userLatitude;
	}

	public void setUserLatitude(double userLatitude) {
		this.userLatitude = userLatitude;
	}

	public double getUserLongitude() {
		return userLongitude;
	}

	public void setUserLongitude(double userLongitude) {
		this.userLongitude = userLongitude;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	public String getUserSex() {
		return userSex;
	}

	public void setUserSex(String userSex) {
		this.userSex = userSex;
	}

	public String getUserSchool() {
		return userSchool;
	}

	public void setUserSchool(String userSchool) {
		this.userSchool = userSchool;
	}

	public String getUserPhotoUri() {
		return userPhotoUri;
	}

	public void setUserPhotoUri(String userPhotoUri) {
		this.userPhotoUri = userPhotoUri;
	}

}
