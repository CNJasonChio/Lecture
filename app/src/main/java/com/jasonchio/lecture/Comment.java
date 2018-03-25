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
 * Created by zhaoyaobang on 2018/3/19.
 */

public class Comment {
	private int userPhotoId;
	private String userName;
	private Lecture lecture;
	private String commentTime;
	private int commentLikers;
	private String commentText;

	public Comment(int userPhotoId, String userName, Lecture lecture, String commentTime, int commentLikers, String commentText) {
		this.userPhotoId = userPhotoId;
		this.userName = userName;
		this.lecture = lecture;
		this.commentTime = commentTime;
		this.commentLikers = commentLikers;
		this.commentText = commentText;
	}

	public String getCommentText() {
		return commentText;
	}

	public void setCommentText(String commentText) {
		this.commentText = commentText;
	}

	public int getUserPhotoId() {
		return userPhotoId;
	}

	public void setUserPhotoId(int userPhotoId) {
		this.userPhotoId = userPhotoId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Lecture getLecture() {
		return lecture;
	}

	public void setLecture(Lecture lecture) {
		this.lecture = lecture;
	}

	public String getCommentTime() {
		return commentTime;
	}

	public void setCommentTime(String commentTime) {
		this.commentTime = commentTime;
	}

	public int getCommentLikers() {
		return commentLikers;
	}

	public void setCommentLikers(int commentLikers) {
		this.commentLikers = commentLikers;
	}
}
