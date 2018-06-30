package com.jasonchio.lecture.greendao;

import org.greenrobot.greendao.annotation.Entity;

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
 * Created by zhaoyaobang on 2018/6/30.
 */

public class DynamicsDB {
	long id;
	String userHead;
	String userName;
	String dynamicsContent;
	String time;
	int likerNum;
	int commentNum;
	int isLikeorNot;

	public DynamicsDB() {
	}

	public DynamicsDB(long id, String userHead, String userName, String dynamicsContent, String time, int likerNum, int commentNum, int isLikeorNot) {
		this.id=id;
		this.userHead = userHead;
		this.userName = userName;
		this.dynamicsContent = dynamicsContent;
		this.time = time;
		this.likerNum = likerNum;
		this.commentNum = commentNum;
		this.isLikeorNot = isLikeorNot;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getIsLikeorNot() {
		return isLikeorNot;
	}

	public void setIsLikeorNot(int isLikeorNot) {
		this.isLikeorNot = isLikeorNot;
	}

	public String getUserHead() {
		return userHead;
	}

	public void setUserHead(String userHead) {
		this.userHead = userHead;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getDynamicsContent() {
		return dynamicsContent;
	}

	public void setDynamicsContent(String dynamicsContent) {
		this.dynamicsContent = dynamicsContent;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public int getLikerNum() {
		return likerNum;
	}

	public void setLikerNum(int likerNum) {
		this.likerNum = likerNum;
	}

	public int getCommentNum() {
		return commentNum;
	}

	public void setCommentNum(int commentNum) {
		this.commentNum = commentNum;
	}
}
