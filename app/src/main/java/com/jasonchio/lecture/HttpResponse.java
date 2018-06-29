package com.jasonchio.lecture;

import android.os.Handler;
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
 * Created by zhaoyaobang on 2018/6/24.
 */
public class HttpResponse {
	int sigininRequestResult;               //注册请求结果
	int findPwdRequestResult;               //找回密码请求结果
	int loginRequestResult;                 //登录请求结果
	int userInfoRequestResult;              //用户信息请求结果
	int changeUserInfoRequestResult;        //更改用户信息请求结果
	int searchLectureRequestResult;         //搜索讲座请求结果
	int myWantedRequestResult;              //我的想看请求结果
	int myFocusedRequestResult;             //我的关注请求结果
	int myCommentRequestResult;             //我的评论请求结果
	int addLectureWantedRequestResult;      //添加讲座到我的想看结果
	int addLibraryFocusedRequestResult;     //添加讲座来源到我的关注结果
	int sendPositionResult;                 //发送位置信息结果
	int addCommentRequestResult;            //添加评论请求结果
	int likeThisCommentResult;              //给评论点赞结果
	int changeUserHeadResult;               //更改用户头像结果
	int recommentRequestResult;             //请求推荐讲座结果
	int lectureRequestResult;               //讲座请求结果
	int libraryRequestResult;               //讲座来源请求结果
	int commentRequestResult;               //评论信息请求结果
	int updateRequestResult;                //检查更新请求结果
	int sendUserHeadFileResult;             //发送用户头像结果
	String requestResponse;                  //服务器返回数据
	int myFocuseRequestResult;              //我的关注
	int handlerMessage;                     //handler要发送的message参数

	public int getHandlerMessage() {
		return handlerMessage;
	}

	public void setHandlerMessage(int handlerMessage) {
		this.handlerMessage = handlerMessage;
	}

	public int getSigininRequestResult() {
		return sigininRequestResult;
	}

	public void setSigininRequestResult(int sigininRequestResult) {
		this.sigininRequestResult = sigininRequestResult;
	}

	public int getFindPwdRequestResult() {
		return findPwdRequestResult;
	}

	public void setFindPwdRequestResult(int findPwdRequestResult) {
		this.findPwdRequestResult = findPwdRequestResult;
	}

	public int getLoginRequestResult() {
		return loginRequestResult;
	}

	public void setLoginRequestResult(int loginRequestResult) {
		this.loginRequestResult = loginRequestResult;
	}

	public int getUserInfoRequestResult() {
		return userInfoRequestResult;
	}

	public void setUserInfoRequestResult(int userInfoRequestResult) {
		this.userInfoRequestResult = userInfoRequestResult;
	}

	public int getChangeUserInfoRequestResult() {
		return changeUserInfoRequestResult;
	}

	public void setChangeUserInfoRequestResult(int changeUserInfoRequestResult) {
		this.changeUserInfoRequestResult = changeUserInfoRequestResult;
	}

	public int getSearchLectureRequestResult() {
		return searchLectureRequestResult;
	}

	public void setSearchLectureRequestResult(int searchLectureRequestResult) {
		this.searchLectureRequestResult = searchLectureRequestResult;
	}

	public int getMyWantedRequestResult() {
		return myWantedRequestResult;
	}

	public void setMyWantedRequestResult(int myWantedRequestResult) {
		this.myWantedRequestResult = myWantedRequestResult;
	}

	public int getMyFocusedRequestResult() {
		return myFocusedRequestResult;
	}

	public void setMyFocusedRequestResult(int myFocusedRequestResult) {
		this.myFocusedRequestResult = myFocusedRequestResult;
	}

	public int getMyCommentRequestResult() {
		return myCommentRequestResult;
	}

	public void setMyCommentRequestResult(int myCommentRequestResult) {
		this.myCommentRequestResult = myCommentRequestResult;
	}

	public int getAddLectureWantedRequestResult() {
		return addLectureWantedRequestResult;
	}

	public void setAddLectureWantedRequestResult(int addLectureWantedRequestResult) {
		this.addLectureWantedRequestResult = addLectureWantedRequestResult;
	}

	public int getAddLibraryFocusedRequestResult() {
		return addLibraryFocusedRequestResult;
	}

	public void setAddLibraryFocusedRequestResult(int addLibraryFocusedRequestResult) {
		this.addLibraryFocusedRequestResult = addLibraryFocusedRequestResult;
	}

	public int getSendPositionResult() {
		return sendPositionResult;
	}

	public void setSendPositionResult(int sendPositionResult) {
		this.sendPositionResult = sendPositionResult;
	}

	public int getAddCommentRequestResult() {
		return addCommentRequestResult;
	}

	public void setAddCommentRequestResult(int addCommentRequestResult) {
		this.addCommentRequestResult = addCommentRequestResult;
	}

	public int getLikeThisCommentResult() {
		return likeThisCommentResult;
	}

	public void setLikeThisCommentResult(int likeThisCommentResult) {
		this.likeThisCommentResult = likeThisCommentResult;
	}

	public int getChangeUserHeadResult() {
		return changeUserHeadResult;
	}

	public void setChangeUserHeadResult(int changeUserHeadResult) {
		this.changeUserHeadResult = changeUserHeadResult;
	}

	public int getRecommentRequestResult() {
		return recommentRequestResult;
	}

	public void setRecommentRequestResult(int recommentRequestResult) {
		this.recommentRequestResult = recommentRequestResult;
	}

	public int getLectureRequestResult() {
		return lectureRequestResult;
	}

	public void setLectureRequestResult(int lectureRequestResult) {
		this.lectureRequestResult = lectureRequestResult;
	}

	public int getLibraryRequestResult() {
		return libraryRequestResult;
	}

	public void setLibraryRequestResult(int libraryRequestResult) {
		this.libraryRequestResult = libraryRequestResult;
	}

	public int getCommentRequestResult() {
		return commentRequestResult;
	}

	public void setCommentRequestResult(int commentRequestResult) {
		this.commentRequestResult = commentRequestResult;
	}

	public int getUpdateRequestResult() {
		return updateRequestResult;
	}

	public void setUpdateRequestResult(int updateRequestResult) {
		this.updateRequestResult = updateRequestResult;
	}

	public int getSendUserHeadFileResult() {
		return sendUserHeadFileResult;
	}

	public void setSendUserHeadFileResult(int sendUserHeadFileResult) {
		this.sendUserHeadFileResult = sendUserHeadFileResult;
	}

	public String getRequestResponse() {
		return requestResponse;
	}

	public void setRequestResponse(String requestResponse) {
		this.requestResponse = requestResponse;
	}

	public int getMyFocuseRequestResult() {
		return myFocuseRequestResult;
	}

	public void setMyFocuseRequestResult(int myFocuseRequestResult) {
		this.myFocuseRequestResult = myFocuseRequestResult;
	}

}
