package com.jasonchio.lecture.util;

import android.graphics.Bitmap;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import com.google.gson.Gson;
import com.jasonchio.lecture.greendao.CommentDB;
import com.jasonchio.lecture.greendao.CommentDBDao;
import com.jasonchio.lecture.greendao.InterimLectureDB;
import com.jasonchio.lecture.greendao.InterimLectureDBDao;
import com.jasonchio.lecture.greendao.LectureDB;
import com.jasonchio.lecture.greendao.LectureDBDao;
import com.jasonchio.lecture.greendao.LibraryDB;
import com.jasonchio.lecture.greendao.LibraryDBDao;
import com.jasonchio.lecture.greendao.UserDB;
import com.jasonchio.lecture.greendao.UserDBDao;
import com.jasonchio.lecture.gson.ChangeUserHeadResult;
import com.jasonchio.lecture.gson.CommentRequestResult;
import com.jasonchio.lecture.gson.CommonStateResult;
import com.jasonchio.lecture.gson.FindPwdResult;
import com.jasonchio.lecture.gson.LectureRequestResult;
import com.jasonchio.lecture.gson.LectureSearchResult;
import com.jasonchio.lecture.gson.LibraryRequestResult;
import com.jasonchio.lecture.gson.LoginResult;
import com.jasonchio.lecture.gson.MyFocuseResult;
import com.jasonchio.lecture.gson.MyWantedResult;
import com.jasonchio.lecture.gson.MyinfoResult;
import com.jasonchio.lecture.gson.SigninResult;
import com.jasonchio.lecture.gson.VercodeResult;
import com.orhanobut.logger.Logger;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
	public static int handleLoginRespose(String response, String userPhone,UserDBDao mUserDao) {

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
				UserDB user=mUserDao.queryBuilder().where(UserDBDao.Properties.UserId.eq(userID)).build().unique();
				if(user==null){
					Logger.d("将该用户存入数据库");
					UserDB newUser = new UserDB();
					newUser.setUserId(userID);
					newUser.setUserPhone(userPhone);
					mUserDao.insert(newUser);
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
	public static int handleLectureResponse(String response, LectureDBDao mLectureDao) {

		Gson gson = new Gson();

		//GSON直接解析成对象
		LectureRequestResult resultBean = gson.fromJson(response, LectureRequestResult.class);
		//对象中拿到集合
		List<LectureRequestResult.LectureBean> lectureBeanList = resultBean.getLecture();

		int state = resultBean.getState();

		if(state==0){
			for(LectureRequestResult.LectureBean lecture:lectureBeanList){
				LectureDB templecture=mLectureDao.queryBuilder().where(LectureDBDao.Properties.LectureId.eq(lecture.getLecture_id())).build().unique();
				if(templecture!=null){
					LectureDB lectureDB=new LectureDB();
					lectureDB.setLectureId(lecture.getLecture_id());
					lectureDB.setLectureTitle(lecture.getLecture_title());
					lectureDB.setLectureLocation(lecture.getLecture_location());
					lectureDB.setLectureTime(lecture.getLecture_time());
					lectureDB.setLecutreSource(lecture.getLecture_source());
					lectureDB.setLecutreLikers(lecture.getLecture_fans_amount());
					lectureDB.setLectureUrl(lecture.getLecture_url());
					lectureDB.setLectureContent(lecture.getLecture_information());
					mLectureDao.insert(lectureDB);
					if(lectureDB.getLectureContent()!=null && lectureDB.getLectureContent().length()!=0){
						getLectureContent(lectureDB.getLectureId(),lectureDB.getLectureContent(),mLectureDao);
					}
				}
			}
		}
		return state;
	}

	//解析和处理服务器返回的评论数据
	public static int handleCommentResponse(String response, CommentDBDao mCommentDao) {
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
					mCommentDao.insert(commentDB);
				}
			}

		}
		return state;
	}

	//解析和处理服务器返回的用户数据
	public static int handleUserInfoResponse(String response,UserDBDao mUserDao) {

		Gson gson=new Gson();

		int state = -1;
		if (!TextUtils.isEmpty(response)) {

			MyinfoResult result=gson.fromJson(response,MyinfoResult.class);

			state=result.getState();
			if(state==0){
				MyinfoResult.UserInfoBean userInfoBean=result.getUser_info();

				UserDB user=mUserDao.queryBuilder().where(UserDBDao.Properties.UserId.eq(result.getUser_info().getUser_id())).build().unique();

				user.setUserPhone(userInfoBean.getUser_phone_num());
				user.setUserBirthday(userInfoBean.getUser_birthday());
				user.setUserLatitude(userInfoBean.getUser_latitude());
				user.setUserLongitude(userInfoBean.getUser_longtitude());
				user.setUserName(userInfoBean.getUser_name());
				user.setUserPhotoUrl(userInfoBean.getUser_face_url());
				user.setUserSex(userInfoBean.getUser_sex());
				user.setUserSchool(userInfoBean.getUser_school());
				mUserDao.update(user);
			}
		}
		return state;
	}

	//解析和处理服务器返回的修改用户信息的数据
	public static int handleChangeInfoResponse(String response,UserDBDao mUserDao,UserDB user) {

		int state = -1;

		Gson gson=new Gson();

		if (!TextUtils.isEmpty(response)) {
			CommonStateResult result=gson.fromJson(response,CommonStateResult.class);
			result.getState();
			if(state==0){
				UserDB userDB=mUserDao.queryBuilder().where(UserDBDao.Properties.UserId.eq(ConstantClass.userOnline)).build().unique();

				userDB.setUserBirthday(user.getUserBirthday());
				userDB.setUserSex(user.getUserSex());
				userDB.setUserSchool(user.getUserSchool());
				userDB.setUserPhone(user.getUserPhone());
				userDB.setUserName(user.getUserName());
				mUserDao.update(userDB);
			}
		}

		return state;
	}

	//解析和处理服务器返回的修改用户头像的数据
	public static int handleChangeUserHeadResponse(String response,UserDBDao mUserDao) {
		int state = -1;
		if (!TextUtils.isEmpty(response)) {
			Gson gson=new Gson();
			ChangeUserHeadResult result=gson.fromJson(response,ChangeUserHeadResult.class);

			state=result.getState();
			if(state==0){
				String url=result.getUser_face_url();
				UserDB user=mUserDao.queryBuilder().where(UserDBDao.Properties.UserId.eq(ConstantClass.userOnline)).build().unique();
				user.setUserPhotoUrl(url);
				mUserDao.update(user);
			}
		}

		return state;
	}

	//解析和处理服务器返回的“我的关注”数据
	public static int handleFocuseLibraryResponse(String response, UserDBDao mUserDao) {

		int state ;

		Gson gson = new Gson();

		MyFocuseResult result = gson.fromJson(response, MyFocuseResult.class);

		state = result.getState();

		List<String> focuseLibrary=result.getFocus_library_id();

		if(state==0){
			String userFocuseLirary="";
			for(String focuse:focuseLibrary){
				userFocuseLirary +=focuse+",";
			}
			UserDB userDB=mUserDao.queryBuilder().where(UserDBDao.Properties.UserId.eq(ConstantClass.userOnline)).build().unique();
			userDB.setUserFocuseLirary(userFocuseLirary);
			mUserDao.update(userDB);
		}

		return state;
	}

	//解析和处理服务器返回的“我的想看”数据
	public static int handleWantedLectureResponse(String response, UserDBDao mUserDao) {

		int state ;

		Gson gson = new Gson();

		MyWantedResult result = gson.fromJson(response, MyWantedResult.class);

		state = result.getState();

		List<Integer> wantedLecture=result.getWant_lectures_id();

		if(state==0){
			String userWantedLecture="";
			for(int wanted:wantedLecture){
				userWantedLecture +=Integer.toString(wanted)+",";
			}
			UserDB userDB=mUserDao.queryBuilder().where(UserDBDao.Properties.UserId.eq(ConstantClass.userOnline)).build().unique();
			userDB.setUserWantedLecture(userWantedLecture);
			mUserDao.update(userDB);
		}

		return state;
	}

	//解析和处理服务器返回的图书馆数据
	public static int handleLibraryResponse(String response, LibraryDBDao mLibraryDao) {
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
				mLibraryDao.insert(libraryDB);
			}
		}
		return state;
	}

	//解析和处理服务器返回的通用数据
	public static int handleCommonResponse(String response) {

		Gson gson = new Gson();
		CommonStateResult result = gson.fromJson(response, CommonStateResult.class);
		int state = result.getState();
		return state;
	}

	//解析和处理服务器返回的讲座搜索数据
	public static int handleLectureSearchResponse(String response, InterimLectureDBDao mInterLecDao,LectureDBDao mLectureDao){

		int state;

		Gson gson=new Gson();

		LectureSearchResult result=gson.fromJson(response,LectureSearchResult.class);

		state=result.getState();

		if(state==0){
			List<Integer> lectureId=result.getLectures_id();
			for(int id:lectureId){
				LectureDB lectureDB=mLectureDao.queryBuilder().where(LectureDBDao.Properties.LectureId.eq(id)).build().unique();
				if(lectureDB==null){
					return 3;
				}else{
					InterimLectureDB interimLec=mInterLecDao.queryBuilder().where(InterimLectureDBDao.Properties.LectureId.eq(id)).build().unique();
						if(interimLec==null){
							InterimLectureDB interimLectureDB=new InterimLectureDB();
							interimLectureDB.setLectureContent(lectureDB.getLectureContent());
							interimLectureDB.setLectureId(lectureDB.getLectureId());
							interimLectureDB.setLectureImage(lectureDB.getLectureImage());
							interimLectureDB.setLectureLocation(lectureDB.getLectureLocation());
							interimLectureDB.setLectureTime(lectureDB.getLectureTime());
							interimLectureDB.setLectureTitle(lectureDB.getLectureTitle());
							interimLectureDB.setLecutreLikers(lectureDB.getLecutreLikers());
							interimLectureDB.setLecutreSource(lectureDB.getLecutreSource());
							interimLectureDB.setLectureUrl(lectureDB.getLectureUrl());
							mInterLecDao.insert(interimLectureDB);
						}
				}
			}
		}

		return state;
	}

	//解析和处理服务器返回的添加评论数据
	public static int handleAddCommentResponse(String response,UserDBDao mUserDao){

		int state;

		Gson gson=new Gson();
		CommonStateResult result=gson.fromJson(response,CommonStateResult.class);

		state=result.getState();

		if(state==0){

			String userComment=getUserComment(ConstantClass.userOnline,mUserDao);

			UserDB user=mUserDao.queryBuilder().where(UserDBDao.Properties.UserId.eq(ConstantClass.userOnline)).build().unique();

			/*
			* 修改用户的评论
			* */
			//添加返回的用户评论ID


			user.setUserComment(userComment);
			mUserDao.update(user);
		}

		return state;
	}

	//解析和处理服务器返回的正文数据
	public static void handleContentResponse(String response,long lectureId,LectureDBDao mLectureDao){

		if(!TextUtils.isEmpty(response)){
			Logger.d(response);

			LectureDB lecture = mLectureDao.queryBuilder().where(LectureDBDao.Properties.LectureId.eq(lectureId)).build().unique();

			if(lecture!=null){
				lecture.setLectureContent(response);
				mLectureDao.update(lecture);
			}
		}
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

	public static long lastLetureinDB(LectureDBDao mLectureDao){

		long lectureID;

		LectureDB lectureDB=mLectureDao.queryBuilder().where(LectureDBDao.Properties.LectureId.eq(mLectureDao.count())).build().unique();

		if(lectureDB==null){
			lectureID=0;

		}else{
			lectureID=lectureDB.getLectureId();
		}

		return lectureID;
	}

	public static long lastCommentinDB(CommentDBDao mCommentDao){

		long commentID;

		CommentDB commentDB=mCommentDao.queryBuilder().where(CommentDBDao.Properties.CommentId.eq(mCommentDao.count())).build().unique();

		if(commentDB==null){
			commentID=0;
		}else{
			commentID=commentDB.getCommentId();
		}

		return commentID;
	}

	//分割用户的关注、想看、评论ID字符串
	public static String[] getStrings(String userString){
		String[] result;
		if(userString.length()!=0 && userString !=null){
			 result= userString.split(",");
		}else{
			result=null;
		}

		return result;
	}

	public static String getUserFocuse(long userID,UserDBDao mUserDao){

		UserDB user=mUserDao.queryBuilder().where(UserDBDao.Properties.UserId.eq(userID)).build().unique();

		String userFocuse;
		userFocuse=user.getUserFocuseLirary();

		return userFocuse;
	}

	public static String getUserComment(long userID,UserDBDao mUserDao){

		UserDB user=mUserDao.queryBuilder().where(UserDBDao.Properties.UserId.eq(userID)).build().unique();

		String userFocuse;
		userFocuse=user.getUserComment();

		return userFocuse;
	}

	public static String getUserWanted(long userID,UserDBDao mUserDao){

		UserDB user=mUserDao.queryBuilder().where(UserDBDao.Properties.UserId.eq(userID)).build().unique();

		String userFocuse;
		userFocuse=user.getUserWantedLecture();

		return userFocuse;
	}

	public static boolean getLectureContent(final long lectureID, String lectureInfo, final LectureDBDao mLectureDao){

		final boolean[] result = new boolean[1];
		ContentRequest(lectureInfo, new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {
				Logger.d("content获取失败");
				result[0]=false;
			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				Utility.handleContentResponse(response.body().string(),lectureID,mLectureDao);
				result[0] =true;
			}
		});
		return result[0];
	}

	public static String getNowTime(){

		Date dNow = new Date( );
		SimpleDateFormat ft = new SimpleDateFormat ("yyyy.MM.dd hh:mm:ss");
		String nowTime=ft.format(dNow);

		return nowTime;
	}

	public static int getBitmapSize(Bitmap bitmap){
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){     //API 19
			return bitmap.getAllocationByteCount();
		}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1){//API 12
			return bitmap.getByteCount();
		}
		return bitmap.getRowBytes() * bitmap.getHeight();                //earlier version
	}
}
