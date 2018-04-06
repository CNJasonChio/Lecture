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

	String  commentuserName;   //该评论的用户名

	int  commentlecureId; //该评论的讲座ID

	String commentContent;  //评论的内容

	String commentTime;     //评论的时间

	int commentLikers;   //评论的点赞数

	public CommentDB(String commentContent, String commentTime, int commentLikers, LectureDB lectureDB) {
		this.commentContent = commentContent;
		this.commentTime = commentTime;
		this.commentLikers = commentLikers;
	}

	public CommentDB() {
	}

	public int getCommentId() {
		return commentId;
	}

	public void setCommentId(int commentId) {
		this.commentId = commentId;
	}

	public String getCommentuserId() {
		return commentuserName;
	}

	public void setCommentuserId(String commentuserName) {
		this.commentuserName = commentuserName;
	}

	public int getCommentlecureId() {
		return commentlecureId;
	}

	public void setCommentlecureId(int commentlecureId) {
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

	public int getCommentLikers() {
		return commentLikers;
	}

	public void setCommentLikers(int commentLikers) {
		this.commentLikers = commentLikers;
	}

}
