package com.jasonchio.lecture.util;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.jasonchio.lecture.database.LectureDB;
import com.jasonchio.lecture.gson.SigninResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

	//解析和处理服务器返回的注册信息
	public static boolean handleSigninRespose(String response){
		if(!TextUtils.isEmpty(response)){
			Gson gson=new Gson();
			gson.fromJson(response, SigninResult.class);

			int userID=0;
			int status=0;
			//如果userID不等于-1，则写入数据库
			/*
			 * if(userID!=-1){
			 *   UserDB userDB=new UserDB();
			 *   userDB.setuseID(userID);
			 *   usrDB.save();
			 *   return true;
			 * }
			 * */
			//如果userID等于-1，分析状态码

		}

		return false;
	}

	//解析和处理服务器返回的讲座数据
	public static boolean handleLectureResponse(String response){
		if(!TextUtils.isEmpty(response)){
			try{
				JSONArray allLectures=new JSONArray(response);
				for(int i=0;i<allLectures.length();i++){
					JSONObject lectureObject=allLectures.getJSONObject(i);
//					LectureDB lectureDB=new LectureDB();
//					lectureDB.setLectureContent();
//					lectureDB.setLectureId();
//					lectureDB.setLecturePlace();
//					lectureDB.setLectureTime();
//					lectureDB.setLectureTitle();
//					lectureDB.setLecutreLikers();
//					lectureDB.setLecutreSource();
//					lectureDB.setLecutreUri();
//					lectureDB.save();
					return true;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	//解析和处理服务器返回的评论数据
	public static boolean handleCommentResponse(String response){
		if(!TextUtils.isEmpty(response)){

		}
		return false;
	}
	//解析和处理服务器返回的用户数据
	public static boolean handleUserInfoResponse(String response){
		if(!TextUtils.isEmpty(response)){
			Gson gson=new Gson();
//			gson.fromJson()
		}
		return false;
	}
	//解析和处理服务器返回的图书馆数据
	public static boolean handleLibraryResponse(String response){
		if(!TextUtils.isEmpty(response)){

		}
		return false;
	}

}
