package com.jasonchio.lecture.util;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jasonchio.lecture.database.LectureDB;
import com.jasonchio.lecture.database.UserDB;
import com.jasonchio.lecture.gson.FindPwdResult;
import com.jasonchio.lecture.gson.LectureRequestResult;
import com.jasonchio.lecture.gson.LoginResult;
import com.jasonchio.lecture.gson.SigninResult;
import com.orhanobut.logger.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.tablemanager.Connector;

import java.util.List;

import es.dmoral.toasty.Toasty;

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
	public static int handleSigninRespose(String response,Context context){
		Logger.d("开始解析和处理服务器返回数据");
		if(!TextUtils.isEmpty(response)){

			Logger.json(response);
			Gson gson=new Gson();
			gson.fromJson(response, SigninResult.class);

			//解析服务器返回的注册结果数据
			SigninResult result=gson.fromJson(response,SigninResult.class);

			int userID=result.getUser_id();
			int state=result.getState();

			//state==0：注册成功
			if(state==0){
				Connector.getDatabase();
				UserDB userDB=new UserDB();
				userDB.setUserId(userID);
				userDB.save();
				Logger.d("解析和处理完毕");
				return 1;
			}else{
				if(state==1){
					Toasty.error(context,"该手机号已经注册，请直接登录");
				}else if(state==2){
					Toasty.error(context,"用户创建失败，请稍后再试");
				}
				return -1;
			}
		}
		else{
			Logger.d("服务器返回数据为空");
		}
		return -1;
	}

	//解析和处理服务器返回的登录信息
	public static int handleLoginRespose(String response, Context context){
		if(!TextUtils.isEmpty(response)){

			Logger.json(response);

			Gson gson=new Gson();
			gson.fromJson(response, LoginResult.class);

			LoginResult loginResult=gson.fromJson(response,LoginResult.class);

			int userID=loginResult.getUser_id();
			int state=loginResult.getUser_id();
			UserDB userDB=new UserDB();

			if(state==0){
				return 1;
			}else{
				if(state==1){
					Toasty.error(context,"用户不存在，请先注册");
				}else if(state==2){
					Toasty.error(context,"服务登录校验出错，请稍候再试");
				}
			}
			/*
			如果userID不等于-1，则判断状态码
			根据状态码确认用户的登录情况
			如果正确，数据库中查找是否存在该用户，不在的话添加到数据库
			 * }
			 * */
			//如果userID等于-1，提示用户不存在，必须先注册

		}

		return -1;
	}

	//解析和处理服务器返回的找回密码信息
	public static int handleFindPwdRespose(String response,Context context){
		if(!TextUtils.isEmpty(response)){

			Logger.json(response);

			Gson gson=new Gson();
			gson.fromJson(response,LectureRequestResult.class);

			FindPwdResult result=gson.fromJson(response,FindPwdResult.class);

			int userID=result.getUser_id();
			int state=result.getState();

			if(state==0){
				return 1;
			}else{
				if(state==1){
					Toasty.error(context,"该用户不存在");
				}else if(state==2){
					Toasty.error(context,"密码修改失败，请稍候再试");
				}
				return -1;
			}

		}

		return -1;
	}

	//解析和处理服务器返回的讲座数据
	public static int handleLectureResponse(String response,Context context){

		Gson gson=new Gson();

		List<LectureDB> lectureList = gson.fromJson(response, new TypeToken<List<LectureDB>>() {}.getType());

		if(!lectureList.isEmpty()){
			for(LectureDB lectureDB:lectureList){
				lectureDB.save();
				Log.d("PRETTY", String.valueOf(lectureDB.getLectureId()));
			}
			return 1;
		}
		return -1;
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
