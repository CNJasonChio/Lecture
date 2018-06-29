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
 * Created by zhaoyaobang on 2018/6/26.
 */
@Entity
public class LectureMessageDB {
	@Id
	long messageId;             //留言ID
	String userHead;            //留言用户头像地址
	String userName;            //留言用户名
	int messageLikeorNot;       //是否对该留言已经点赞
	int messageLikersNum;       //留言点赞数
	String messageContent;      //留言正文
	@Generated(hash = 1892609660)
	public LectureMessageDB(long messageId, String userHead, String userName,
			int messageLikeorNot, int messageLikersNum, String messageContent) {
		this.messageId = messageId;
		this.userHead = userHead;
		this.userName = userName;
		this.messageLikeorNot = messageLikeorNot;
		this.messageLikersNum = messageLikersNum;
		this.messageContent = messageContent;
	}
	@Generated(hash = 1161060462)
	public LectureMessageDB() {
	}
	public long getMessageId() {
		return this.messageId;
	}
	public void setMessageId(long messageId) {
		this.messageId = messageId;
	}
	public String getUserHead() {
		return this.userHead;
	}
	public void setUserHead(String userHead) {
		this.userHead = userHead;
	}
	public String getUserName() {
		return this.userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public int getMessageLikeorNot() {
		return this.messageLikeorNot;
	}
	public void setMessageLikeorNot(int messageLikeorNot) {
		this.messageLikeorNot = messageLikeorNot;
	}
	public int getMessageLikersNum() {
		return this.messageLikersNum;
	}
	public void setMessageLikersNum(int messageLikersNum) {
		this.messageLikersNum = messageLikersNum;
	}
	public String getMessageContent() {
		return this.messageContent;
	}
	public void setMessageContent(String messageContent) {
		this.messageContent = messageContent;
	}
}
