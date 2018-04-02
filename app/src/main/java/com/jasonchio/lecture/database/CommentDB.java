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

public class CommentDB extends DataSupport{

	int commentId;       //评论的ID

	String commentuserId;   //该评论的用户ID

	String commentlecureId; //该评论的讲座ID

	String commentContent;  //评论的内容

	String commentTime;     //评论的时间

	String commentLikers;   //评论的点赞数

	LectureDB lectureDB;    //评论对应的讲座



	public int getCommentId() {
		return commentId;
	}

	public void setCommentId(int commentId) {
		this.commentId = commentId;
	}

	public String getCommentuserId() {
		return commentuserId;
	}

	public void setCommentuserId(String commentuserId) {
		this.commentuserId = commentuserId;
	}

	public String getCommentlecureId() {
		return commentlecureId;
	}

	public void setCommentlecureId(String commentlecureId) {
		this.commentlecureId = commentlecureId;
	}

	public String getCommentContent() {
		return commentContent;
	}

	public void setCommentContent(String commentContent) {
		this.commentContent = commentContent;
	}

	public String getCommentTime() {
		return commentTime;
	}

	public void setCommentTime(String commentTime) {
		this.commentTime = commentTime;
	}

	public String getCommentLikers() {
		return commentLikers;
	}

	public void setCommentLikers(String commentLikers) {
		this.commentLikers = commentLikers;
	}

	public LectureDB getLectureDB() {
		return lectureDB;
	}

	public void setLectureDB(LectureDB lectureDB) {
		this.lectureDB = lectureDB;
	}
}
