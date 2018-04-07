package com.jasonchio.lecture.util;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.jasonchio.lecture.database.CommentDB;
import com.jasonchio.lecture.database.InterimLectureDB;
import com.jasonchio.lecture.database.LectureDB;
import com.jasonchio.lecture.database.LibraryDB;
import com.jasonchio.lecture.database.UserDB;
import com.jasonchio.lecture.gson.CommentRequestResult;
import com.jasonchio.lecture.gson.CommonStateResult;
import com.jasonchio.lecture.gson.FindPwdResult;
import com.jasonchio.lecture.gson.LectureRequestResult;
import com.jasonchio.lecture.gson.LectureSearchResult;
import com.jasonchio.lecture.gson.LibraryRequestResult;
import com.jasonchio.lecture.gson.LoginResult;
import com.jasonchio.lecture.gson.MyFocuseResult;
import com.jasonchio.lecture.gson.SigninResult;
import com.jasonchio.lecture.gson.VercodeResult;
import com.orhanobut.logger.Logger;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.jasonchio.lecture.util.HttpUtil.ContentRequest;

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
 * Created by zhaoyaobang on 2018/3/4.
 */

public class Utility {

	//解析和处理短信验证返回的信息
	public static int handleVercodeResponse(String response) {
		Logger.d("短信验证出错");

		Gson gson = new Gson();

		//解析服务器返回的验证结果数据
		VercodeResult result = gson.fromJson(response, VercodeResult.class);

		//获取错误状态码
		int state = result.getStatus();

		return state;
	}

	//解析和处理服务器返回的注册信息
	public static int handleSigninRespose(String response) {

		Logger.d("开始解析和处理服务器返回数据");

		if (!TextUtils.isEmpty(response)) {

			Logger.json(response);
			Gson gson = new Gson();

			//解析服务器返回的注册结果数据
			SigninResult result = gson.fromJson(response, SigninResult.class);

			int state = result.getState();

			Logger.d(result.getState());

			return state;
		} else {
			Logger.d("服务器返回数据为空");
		}
		return -1;
	}

	//解析和处理服务器返回的找回密码信息
	public static int handleFindPwdRespose(String response) {

		if (!TextUtils.isEmpty(response)) {

			Logger.json(response);

			//解析服务器返回的修改密码结果数据
			Gson gson = new Gson();
			gson.fromJson(response, LectureRequestResult.class);

			FindPwdResult result = gson.fromJson(response, FindPwdResult.class);

			int state = result.getState();

			return state;
		}
		return -1;
	}

	//解析和处理服务器返回的登录信息
	public static int handleLoginRespose(String response) {

		if (!TextUtils.isEmpty(response)) {

			Logger.json(response);
			Gson gson = new Gson();
			gson.fromJson(response, LoginResult.class);

			LoginResult loginResult = gson.fromJson(response, LoginResult.class);

			int userID = loginResult.getUser_id();
			int state = loginResult.getState();

			//记录当前登录的用户
			ConstantClass.userOnline = userID;

			if (state == 0) {
				/*
				 * 查找数据库有没有该用户，没有则保存在数据库中
				 */
				int result = DataSupport.where("userId=?",Integer.toString(userID)).count(UserDB.class);

				if (result==0) {
					Logger.d("将该用户存入数据库");
					UserDB user = new UserDB();
					user.setUserId(userID);
					user.save();
				}else{
					Logger.d("该用户已经在数据库中");
				}
			} else if (state == -1) {
				//
				if (userID == -1)
					return 3;
			}
			return state;
		}

		return -1;
	}

	//解析和处理服务器返回的讲座数据
	public static int handleLectureResponse(String response) {

		Gson gson = new Gson();

		//GSON直接解析成对象
		LectureRequestResult resultBean = new Gson().fromJson(response, LectureRequestResult.class);
		//对象中拿到集合
		List<LectureRequestResult.LectureBean> lectureBeanList = resultBean.getLecture();

		int state = resultBean.getState();

		if(state==0){
			for(LectureRequestResult.LectureBean lecture:lectureBeanList){
				LectureDB lectureDB=new LectureDB();
				lectureDB.setLectureId(lecture.getLecture_id());
				lectureDB.setLectureTitle(lecture.getLecture_title());
				lectureDB.setLectureLocation(lecture.getLecture_location());
				lectureDB.setLectureTime(lecture.getLecture_time());
				lectureDB.setLecutreSource(lecture.getLecture_source());
				lectureDB.setLecutreLikers(lecture.getLecture_fans_amount());
				lectureDB.setLecutreUrl(lecture.getLecture_url());
				lectureDB.setLectureContent(lecture.getLecture_information());
				lectureDB.save();
				if(lectureDB.getLectureContent()!=null && lectureDB.getLectureContent().length()!=0){
					getLectureContent(lectureDB.getLectureId(),lectureDB.getLectureContent());
				}
			}
		}

		return state;
	}

	//解析和处理服务器返回的评论数据
	public static int handleCommentResponse(String response) {
		int state = -1;
		if (!TextUtils.isEmpty(response)) {
			Gson gson=new Gson();
			CommentRequestResult result=gson.fromJson(response,CommentRequestResult.class);
			//对象中拿到集合
			List<CommentRequestResult.CommentBean> commentBeanList = result.getComment();
			state=result.getState();
			if(state==0){
				for(CommentRequestResult.CommentBean comment:commentBeanList){
					CommentDB commentDB=new CommentDB();
					commentDB.setCommentId(comment.getComment_id());
					commentDB.setCommentTime(comment.getComment_time());
					commentDB.setCommentLikers(comment.getComment_good_amount());
					commentDB.setCommentlecureId(comment.getComment_lecture());
					commentDB.setCommentContent(comment.getComment_information());
					/*
					* 评论对应的用户名和用户头像
					* */
					//commentDB.setCommentuserId(comment.getComment_user());
					//lectureDB.setLectureContent();
					commentDB.save();
				}
			}

		}
		return state;
	}

	//解析和处理服务器返回的用户数据
	public static int handleUserInfoResponse(String response) {
		if (!TextUtils.isEmpty(response)) {

		}
		int state = -1;
		return state;
	}

	//解析和处理服务器返回的“我的关注”数据
	public static int handelFocuseLectureResponse(String response) {

		int state = -1;

		Gson gson = new Gson();

		Log.d("test",response);

		MyFocuseResult result = gson.fromJson(response, MyFocuseResult.class);

		state = result.getState();

		List<Integer> focuseLibrary=result.getFocus_library_id();

		if(state==0){
			String userFocuseLirary="";
			for(int focuse:focuseLibrary){
				userFocuseLirary +=Integer.toString(focuse)+",";
			}
			UserDB userDB=new UserDB();
			userDB.setUserFocuseLirary(userFocuseLirary);
			userDB.updateAll("userId=?",Integer.toString(ConstantClass.userOnline));
		}

		return state;
	}

	//解析和处理服务器返回的图书馆数据
	public static int handleLibraryResponse(String response) {
		int state = -1;
		if (!TextUtils.isEmpty(response)) {
			Gson gson=new Gson();
			LibraryRequestResult result=gson.fromJson(response,LibraryRequestResult.class);
			state=result.getState();
			if(state==0){
				LibraryRequestResult.LibraryInfoBean library_info=result.getLibrary_info();

				LibraryDB libraryDB=new LibraryDB();
				libraryDB.setLibraryID(library_info.getLibrary_id());
				libraryDB.setLibraryLatitude(library_info.getLecture_latitude());
				libraryDB.setLibraryImageUrl(library_info.getLibrary_picture_url());
				libraryDB.setLibraryLongitude(library_info.getLecture_longtitude());
				libraryDB.setLibraryName(library_info.getLibrary_name());
				libraryDB.setLibraryUrl(library_info.getLecture_address_url());
				libraryDB.setLibraryContent(library_info.getLecture_information());
				libraryDB.save();
			}
		}
		return state;
	}

	public static int handleCommonResponse(String response) {

		Gson gson = new Gson();
		CommonStateResult result = gson.fromJson(response, CommonStateResult.class);
		int state = result.getState();
		return state;
	}

	public static int handleLectureSearchResponse(String response){

		int state=-1;

		Gson gson=new Gson();

		LectureSearchResult result=gson.fromJson(response,LectureSearchResult.class);

		state=result.getState();

		if(state==0){
			List<Integer> lectureId=result.getLectures_id();
			for(int id:lectureId){
				List<LectureDB> lectureDBList=DataSupport.where("lectureId=?",Integer.toString(id)).find(LectureDB.class);
				if(lectureDBList.size()==0){
					return 3;
				}else{
					for(LectureDB lectureDB:lectureDBList){
						List<InterimLectureDB> interimLectureDBS=DataSupport.where("lectureId=?",Integer.toString(id)).find(InterimLectureDB.class);
						if(interimLectureDBS.size()==0){
							InterimLectureDB interimLectureDB=new InterimLectureDB();
							interimLectureDB.setLectureContent(lectureDB.getLectureContent());
							interimLectureDB.setLectureId(lectureDB.getLectureId());
							interimLectureDB.setLectureImage(lectureDB.getLectureImage());
							interimLectureDB.setLectureLocation(lectureDB.getLectureLocation());
							interimLectureDB.setLectureTime(lectureDB.getLectureTime());
							interimLectureDB.setLectureTitle(lectureDB.getLectureTitle());
							interimLectureDB.setLecutreLikers(lectureDB.getLecutreLikers());
							interimLectureDB.setLecutreSource(lectureDB.getLecutreSource());
							interimLectureDB.setLecutreUrl(lectureDB.getLecutreUrl());
							interimLectureDB.save();
						}
					}
				}
			}
		}

		return state;
	}

	/**
	 * 判断手机号码是否合理
	 *
	 * @param phoneNums
	 */
	public static boolean judgePhoneNums(String phoneNums) {
		if (isMatchLength(phoneNums, 11) && isMobileNO(phoneNums)) {
			return true;
		}
		return false;
	}

	private static boolean isMatchLength(String str, int length) {
		if (str.isEmpty()) {
			return false;
		} else {
			if (str.length() == length)
				return true;
			else {
				return false;
			}
		}
	}

	private static boolean isMobileNO(String mobileNums) {
		String telRegex = "[1][345789]\\d{9}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
		if (TextUtils.isEmpty(mobileNums))
			return false;
		else {
			if (mobileNums.matches(telRegex)) {
				return true;
			} else {
				return false;
			}
		}
	}

	public static int lastLetureinDB(){

		int lectureID=0;

		LectureDB lectureDB=DataSupport.findLast(LectureDB.class);

		if(lectureDB==null){
			lectureID=0;

		}else{
			lectureID=lectureDB.getLectureId();
		}

		return lectureID;
	}

	public static int lastCommentinDB(){

		int commentID=0;

		CommentDB commentDB=DataSupport.findLast(CommentDB.class);

		if(commentDB==null){
			commentID=0;
		}else{
			commentID=commentDB.getCommentId();
		}

		return commentID;
	}

	public static void handleContentResponse(String response,int lectureId){

		if(!TextUtils.isEmpty(response)){
			Logger.d(response);

			LectureDB lectureDB=new LectureDB();
			lectureDB.setLectureContent(response);

			lectureDB.updateAll("lectureId=?",Integer.toString(lectureId));

		}
	}

	//分割用户的关注、想看、评论ID字符串
	public static String[] getStrings(String userString){

		String[] result = userString.split(",");
		return result;
	}

	public static String getUserFocuse(int userID){

			List<UserDB> userDBList=DataSupport.where("userId=?",Integer.toString(userID)).find(UserDB.class);
			String userFocuse=null;
			for(UserDB userDB:userDBList){
				userFocuse=userDB.getUserFocuseLirary();
			}
			return userFocuse;
	}

	public static boolean getLectureContent(final int lectureID, String lectureInfo){

		final boolean[] result = new boolean[1];
		ContentRequest(lectureInfo, new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {
				Logger.d("content获取失败");
				result[0]=false;
			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				Utility.handleContentResponse(response.body().string(),lectureID);
				result[0] =true;
			}
		});
		return result[0];
	}

}
