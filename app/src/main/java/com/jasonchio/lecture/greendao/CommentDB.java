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
public class CommentDB{

	@Id
	long commentId;       //评论的ID

	String  commentuserName;   //该评论的用户名

	String userHead;

	long  commentlecureId; //该评论的讲座ID

	String commentContent;  //评论的内容

	String commentTime;     //评论的时间

	int commentLikers;   //评论的点赞数

	int isLike;          //是否已经点赞

	@Generated(hash = 278975562)
	public CommentDB(long commentId, String commentuserName, String userHead,
			long commentlecureId, String commentContent, String commentTime,
			int commentLikers, int isLike) {
		this.commentId = commentId;
		this.commentuserName = commentuserName;
		this.userHead = userHead;
		this.commentlecureId = commentlecureId;
		this.commentContent = commentContent;
		this.commentTime = commentTime;
		this.commentLikers = commentLikers;
		this.isLike = isLike;
	}

	@Generated(hash = 1839661279)
	public CommentDB() {
	}

	public long getCommentId() {
		return this.commentId;
	}

	public void setCommentId(long commentId) {
		this.commentId = commentId;
	}

	public String getCommentuserName() {
		return this.commentuserName;
	}

	public void setCommentuserName(String commentuserName) {
		this.commentuserName = commentuserName;
	}

	public String getUserHead() {
		return this.userHead;
	}

	public void setUserHead(String userHead) {
		this.userHead = userHead;
	}

	public long getCommentlecureId() {
		return this.commentlecureId;
	}

	public void setCommentlecureId(long commentlecureId) {
		this.commentlecureId = commentlecureId;
	}

	public String getCommentContent() {
		return this.commentContent;
	}

	public void setCommentContent(String commentContent) {
		this.commentContent = commentContent;
	}

	public String getCommentTime() {
		return this.commentTime;
	}

	public void setCommentTime(String commentTime) {
		this.commentTime = commentTime;
	}

	public int getCommentLikers() {
		return this.commentLikers;
	}

	public void setCommentLikers(int commentLikers) {
		this.commentLikers = commentLikers;
	}

	public int getIsLike() {
		return this.isLike;
	}

	public void setIsLike(int isLike) {
		this.isLike = isLike;
	}
	
}
