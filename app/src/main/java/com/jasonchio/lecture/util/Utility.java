package com.jasonchio.lecture.util;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
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
import com.jasonchio.lecture.gson.AddCommentResult;
import com.jasonchio.lecture.gson.ChangeUserHeadResult;
import com.jasonchio.lecture.gson.CheckUpdateResult;
import com.jasonchio.lecture.gson.CommentRequestResult;
import com.jasonchio.lecture.gson.CommonStateResult;
import com.jasonchio.lecture.gson.FindPwdResult;
import com.jasonchio.lecture.gson.LectureMessageResult;
import com.jasonchio.lecture.gson.LectureRequestResult;
import com.jasonchio.lecture.gson.LectureSearchResult;
import com.jasonchio.lecture.gson.LibraryRequestResult;
import com.jasonchio.lecture.gson.LoginResult;
import com.jasonchio.lecture.gson.MyCommentResult;
import com.jasonchio.lecture.gson.MyFocuseResult;
import com.jasonchio.lecture.gson.MyWantedResult;
import com.jasonchio.lecture.gson.MyinfoResult;
import com.jasonchio.lecture.gson.RecommentLectureResult;
import com.jasonchio.lecture.gson.SigninResult;
import com.jasonchio.lecture.gson.VercodeResult;
import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.jasonchio.lecture.util.HttpUtil.ContentRequest;
import static com.jasonchio.lecture.util.HttpUtil.LibraryRequest;

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
	public static int handleLoginRespose(String response, String userPhone, UserDBDao mUserDao) {

		if (!TextUtils.isEmpty(response)) {

			Logger.json(response);
			Gson gson = new Gson();
			gson.fromJson(response, LoginResult.class);

			LoginResult loginResult = gson.fromJson(response, LoginResult.class);

			int userID = loginResult.getUser_id();
			int state = loginResult.getState();

			if (state == 0) {
				/*
				 * 查找数据库有没有该用户，没有则保存在数据库中
				 */
				//记录当前登录的用户
				ConstantClass.userOnline = userID;

				UserDB user = mUserDao.queryBuilder().where(UserDBDao.Properties.UserId.eq(userID)).build().unique();
				if (user == null) {
					Logger.d("将该用户存入数据库");
					UserDB newUser = new UserDB();
					newUser.setUserId(userID);
					newUser.setUserPhone(userPhone);
					mUserDao.insertOrReplace(newUser);
				} else {
					Logger.d("该用户已经在数据库中");
				}
			} else if (state == 1) {

				if (userID != -1)
					return 3;
			}
			return state;
		}

		return -1;
	}

	//解析和处理服务器返回的讲座数据
	public static int handleLectureResponse(String response, LectureDBDao mLectureDao) {

		if (!TextUtils.isEmpty(response)) {
			Gson gson = new Gson();

			//GSON直接解析成对象
			LectureRequestResult resultBean = gson.fromJson(response, LectureRequestResult.class);
			//对象中拿到集合
			List <LectureRequestResult.LectureBean> lectureBeanList = resultBean.getLecture();

			int state = resultBean.getState();

			long[] lectureIDs=getLecturesStoredInDB(mLectureDao);
			if (state == 0) {
				if(lectureIDs!=null){
					for (final LectureRequestResult.LectureBean lecture : lectureBeanList) {
						if(!isLectureStored(lecture.getLecture_id(),lectureIDs)){
							final LectureDB lectureDB = new LectureDB();
							lectureDB.setLectureId(lecture.getLecture_id());
							lectureDB.setLectureTitle(lecture.getLecture_title());
							lectureDB.setLectureLocation(lecture.getLecture_location());
							lectureDB.setLectureTime(lecture.getLecture_time());
							lectureDB.setLecutreSource(lecture.getLecture_source());
							lectureDB.setLecutreLikers(lecture.getLecture_fans_amount());
							lectureDB.setLectureUrl(lecture.getLecture_url());
							lectureDB.setLectureDistrict(lecture.getRange());
							lectureDB.setIsWanted(lecture.getUser_want_lecture());
							lectureDB.setLectureImage(lecture.getLecture_picture());
							if (lecture.getLecture_information() == null || lecture.getLecture_information().length() == 0) {
								lectureDB.setLectureContent("暂无简介，可点击“阅读原文”查看");
								mLectureDao.insertOrReplace(lectureDB);
							} else {
								lectureDB.setLectureContent(lecture.getLecture_information());
								mLectureDao.insertOrReplace(lectureDB);
								getLectureContent(lecture.getLecture_id(), lecture.getLecture_information(), mLectureDao);
							}
						}
					}
				}
			}
			return state;
		}
		return -1;
	}

	//解析和处理服务器针对该用户推荐的讲座顺序
	public static int handleRecommentLectureResponse(String response, UserDBDao mUserDao) {
		int state = 1;

		Gson gson = new Gson();

		RecommentLectureResult result = gson.fromJson(response, RecommentLectureResult.class);

		if (result != null) {
			state = result.getState();
			if (state == 0) {
				UserDB user = mUserDao.queryBuilder().where(UserDBDao.Properties.UserId.eq(ConstantClass.userOnline)).build().unique();
				if (user != null) {
					List <Integer> recommentLectureOrder = result.getRecommend();
					String recommentOrder = "";
					if (!recommentLectureOrder.isEmpty()) {
						for (Integer order : recommentLectureOrder) {
							recommentOrder += Integer.toString(order) + ",";
						}
					}
					if (recommentOrder != null && recommentOrder != "") {
						if (user.getRecommentLectureOrder() != null) {
							if (user.getRecommentLectureOrder().length() == recommentOrder.length()) {
								state = 3;
								return state;
							}
						}
						user.setRecommentLectureOrder(recommentOrder);
						mUserDao.update(user);
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
			Gson gson = new Gson();
			CommentRequestResult result = gson.fromJson(response, CommentRequestResult.class);
			state = result.getState();

			if (state == 0) {

				List <CommentRequestResult.CommentBean> commentBeanList = result.getComment();
				if (!commentBeanList.isEmpty()) {
					for (CommentRequestResult.CommentBean comment : commentBeanList) {
						CommentDB commentDB = new CommentDB();
						commentDB.setCommentId(comment.getComment_id());
						commentDB.setCommentTime(comment.getComment_time());
						commentDB.setCommentLikers(comment.getComment_good_amount());
						commentDB.setCommentlecureId(comment.getComment_lecture());
						commentDB.setCommentContent(comment.getComment_information());
						commentDB.setCommentuserName(comment.getComment_user());
						commentDB.setIsLike(comment.getComment_user_like());
						commentDB.setUserHead(comment.getComment_user_face_url());
						mCommentDao.insertOrReplace(commentDB);
					}
				}
			}

		}
		return state;
	}

	//解析和处理服务器返回的用户数据
	public static int handleUserInfoResponse(String response, UserDBDao mUserDao) {

		Gson gson = new Gson();

		int state = -1;
		if (!TextUtils.isEmpty(response)) {

			MyinfoResult result = gson.fromJson(response, MyinfoResult.class);

			state = result.getState();
			if (state == 0) {
				MyinfoResult.UserInfoBean userInfoBean = result.getUser_info();

				UserDB user = mUserDao.queryBuilder().where(UserDBDao.Properties.UserId.eq(result.getUser_info().getUser_id())).build().unique();

				user.setUserPhone(userInfoBean.getUser_phone_num());
				user.setUserBirthday(userInfoBean.getUser_birthday());
				user.setUserLatitude(userInfoBean.getUser_latitude());
				user.setUserLongitude(userInfoBean.getUser_longtitude());
				if (userInfoBean.getUser_name() == null) {
					user.setUserName("讲座萌新");
				} else {
					user.setUserName(userInfoBean.getUser_name());
				}
				user.setUserPhotoUrl(userInfoBean.getUser_face_url());
				user.setUserSex(userInfoBean.getUser_sex());
				user.setUserSchool(userInfoBean.getUser_school());
				mUserDao.update(user);
			}
		}
		return state;
	}

	//解析和处理服务器返回的修改用户信息的数据
	public static int handleChangeInfoResponse(String response, UserDBDao mUserDao, UserDB user) {

		int state = -1;

		Gson gson = new Gson();

		if (!TextUtils.isEmpty(response)) {
			CommonStateResult result = gson.fromJson(response, CommonStateResult.class);
			state = result.getState();
			if (state == 0) {
				UserDB userDB = mUserDao.queryBuilder().where(UserDBDao.Properties.UserId.eq(ConstantClass.userOnline)).build().unique();

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
	public static int handleChangeUserHeadResponse(String response, UserDBDao mUserDao) {
		int state = -1;
		if (!TextUtils.isEmpty(response)) {
			Gson gson = new Gson();
			ChangeUserHeadResult result = gson.fromJson(response, ChangeUserHeadResult.class);

			state = result.getState();
			if (state == 0) {
				String url = result.getUrl();
				UserDB user = mUserDao.queryBuilder().where(UserDBDao.Properties.UserId.eq(ConstantClass.userOnline)).build().unique();
				user.setUserPhotoUrl(url);
				mUserDao.update(user);
			}
		}

		return state;
	}

	//解析和处理服务器返回的“我的关注”数据
	public static int handleFocuseLibraryResponse(String response, UserDBDao mUserDao) {

		int state;

		Gson gson = new Gson();

		MyFocuseResult result = gson.fromJson(response, MyFocuseResult.class);

		state = result.getState();

		List <String> focuseLibrary = result.getFocus_library_name();

		if (state == 0) {
			if (focuseLibrary.size() == 0 || focuseLibrary.isEmpty()) {
				state = 3;
			} else {
				String userFocuseLirary = "";
				for (String focuse : focuseLibrary) {
					userFocuseLirary += focuse + ",";
				}
				UserDB userDB = mUserDao.queryBuilder().where(UserDBDao.Properties.UserId.eq(ConstantClass.userOnline)).build().unique();
				userDB.setUserFocuseLirary(userFocuseLirary);
				mUserDao.update(userDB);
			}
		}

		return state;
	}

	//解析和处理服务器返回的“我的想看”数据
	public static int handleWantedLectureResponse(String response, UserDBDao mUserDao) {

		int state;

		Gson gson = new Gson();

		MyWantedResult result = gson.fromJson(response, MyWantedResult.class);

		state = result.getState();

		List <Integer> wantedLecture = result.getWant_lectures_id();

		if (state == 0) {
			if (wantedLecture.size() == 0) {
				state = 3;
			} else {
				String userWantedLecture = "";
				for (int wanted : wantedLecture) {
					userWantedLecture += Integer.toString(wanted) + ",";
				}
				UserDB userDB = mUserDao.queryBuilder().where(UserDBDao.Properties.UserId.eq(ConstantClass.userOnline)).build().unique();
				userDB.setUserWantedLecture(userWantedLecture);
				mUserDao.update(userDB);
			}
		}
		return state;
	}

	//解析和处理服务器返回的“我的评论”数据
	public static int handleMyCommentResponse(String response, UserDBDao mUserDao) {

		int state;

		Gson gson = new Gson();

		MyCommentResult result = gson.fromJson(response, MyCommentResult.class);

		state = result.getState();

		List <Integer> myComment = result.getComment_id();

		if (state == 0) {
			String userComment = "";
			for (long wanted : myComment) {
				userComment += Long.toString(wanted) + ",";
			}
			UserDB userDB = mUserDao.queryBuilder().where(UserDBDao.Properties.UserId.eq(ConstantClass.userOnline)).build().unique();
			userDB.setUserComment(userComment);
			mUserDao.update(userDB);
		} else if (state == 1) {
			if (myComment.size() == 0) {
				state = 3;
			}
		}
		return state;
	}

	//解析和处理服务器返回的图书馆数据
	public static int handleLibraryResponse(String response, LibraryDBDao mLibraryDao) {
		int state = -1;
		if (!TextUtils.isEmpty(response)) {
			Gson gson = new Gson();
			LibraryRequestResult result = gson.fromJson(response, LibraryRequestResult.class);
			state = result.getState();
			if (state == 0) {
				LibraryRequestResult.LibraryInfoBean library_info = result.getLibrary_info();

				LibraryDB libraryDB = new LibraryDB();
				libraryDB.setLibraryID(library_info.getLibrary_id());
				libraryDB.setLibraryLatitude(library_info.getLibrary_latitude());
				libraryDB.setLibraryImageUrl(library_info.getLibrary_picture_url());
				libraryDB.setLibraryLongitude(library_info.getLibrary_longtitude());
				libraryDB.setLibraryName(library_info.getLibrary_name());
				libraryDB.setLibraryUrl(library_info.getLibrary_address_url());
				libraryDB.setIsFocused(library_info.getUser_focus_lib());
				if (library_info.getLibrary_information() == null || library_info.getLibrary_information().length() == 0) {
					libraryDB.setLibraryContent("暂无简介，可点击“查看官网”查看");
					mLibraryDao.insertOrReplace(libraryDB);
				} else {
					libraryDB.setLibraryContent(library_info.getLibrary_information());
					mLibraryDao.insertOrReplace(libraryDB);
					getLibraryContent(libraryDB.getLibraryID(), libraryDB.getLibraryContent(), mLibraryDao);
				}
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

	//解析和处理服务器返回的添加或者取消关注的数据
	public static int handleFocuseChangeResponse(String response, String libName, UserDBDao mUserDao, LibraryDBDao mLibraryDao, int isFocuse) {
		Gson gson = new Gson();
		CommonStateResult result = gson.fromJson(response, CommonStateResult.class);
		int state = result.getState();
		if (state == 0) {
			String temp = Utility.getUserFocuse(ConstantClass.userOnline, mUserDao);
			if (temp != null && temp != "") {
				if (isFocuse == 0) {
					String[] focuseLibrary = Utility.getStrings(temp);
					List <String> focuseStr = new ArrayList <>();

					for (int i = 0; i < focuseLibrary.length; i++) {
						focuseStr.add(focuseLibrary[i]);
					}
					for (String lib : focuseStr) {
						if (lib.equals(libName)) {
							focuseStr.remove(lib);
							break;
						}
					}

					String userFocuseLirary = "";
					for (String focuse : focuseStr) {
						userFocuseLirary += focuse + ",";
					}
					UserDB userDB = mUserDao.queryBuilder().where(UserDBDao.Properties.UserId.eq(ConstantClass.userOnline)).build().unique();
					userDB.setUserFocuseLirary(userFocuseLirary);
					mUserDao.update(userDB);
				} else {
					temp += libName + ",";
					UserDB userDB = mUserDao.queryBuilder().where(UserDBDao.Properties.UserId.eq(ConstantClass.userOnline)).build().unique();
					userDB.setUserFocuseLirary(temp);
					mUserDao.update(userDB);
				}
				LibraryDB libraryDB = mLibraryDao.queryBuilder().where(LibraryDBDao.Properties.LibraryName.eq(libName)).build().unique();

				if (libraryDB != null) {
					libraryDB.setIsFocused(isFocuse);
					mLibraryDao.update(libraryDB);
				}
			}


		}
		return state;
	}

	//解析和处理服务器返回的添加或者取消点赞评论的数据
	public static int handleLikeChangeResponse(String response, long commentID, CommentDBDao mCommentDao, int isLike) {

		Gson gson = new Gson();
		CommonStateResult result = gson.fromJson(response, CommonStateResult.class);
		int state = result.getState();
		if (state == 0) {
			CommentDB comment = mCommentDao.queryBuilder().where(CommentDBDao.Properties.CommentId.eq(commentID)).build().unique();
			comment.setIsLike(isLike);
			if (isLike == 0) {
				comment.setCommentLikers(comment.getCommentLikers() - 1);
			} else {
				comment.setCommentLikers(comment.getCommentLikers() + 1);
			}
			mCommentDao.update(comment);
		}
		return state;
	}

	//解析和处理服务器返回的添加或者取消想看的数据
	public static int handleWantedChangeResponse(String response, long lectureID, UserDBDao mUserDao, LectureDBDao mLectureDao, int isWanted) {

		Gson gson = new Gson();
		CommonStateResult result = gson.fromJson(response, CommonStateResult.class);
		int state = result.getState();
		if (state == 0) {
			String temp = Utility.getUserWanted(ConstantClass.userOnline, mUserDao);
			LectureDB lecture = mLectureDao.queryBuilder().where(LectureDBDao.Properties.LectureId.eq(lectureID)).build().unique();
			if (lecture.getIsWanted() != isWanted) {
				if (temp != null && temp != "") {
					if (isWanted == 0) {
						String[] wantedLecture = Utility.getStrings(temp);
						List <String> wantedStr = new ArrayList <>();
						for (int i = 0; i < wantedLecture.length; i++) {
							wantedStr.add(wantedLecture[i]);
						}

						for (String lec : wantedStr) {
							if (lec.equals(Long.toString(lecture.getLectureId()))) {
								wantedStr.remove(lec);
								break;
							}
						}

						String userWantedLecture = "";
						for (String wanted : wantedStr) {
							userWantedLecture += wanted + ",";
						}
						UserDB userDB = mUserDao.queryBuilder().where(UserDBDao.Properties.UserId.eq(ConstantClass.userOnline)).build().unique();
						userDB.setUserWantedLecture(userWantedLecture);
						mUserDao.update(userDB);
						lecture.setLecutreLikers(lecture.getLecutreLikers() - 1);
					} else {
						temp += Long.toString(lectureID) + ",";
						UserDB userDB = mUserDao.queryBuilder().where(UserDBDao.Properties.UserId.eq(ConstantClass.userOnline)).build().unique();

						userDB.setUserWantedLecture(temp);
						mUserDao.update(userDB);
						lecture.setLecutreLikers(lecture.getLecutreLikers() + 1);
					}
					lecture.setIsWanted(isWanted);
					mLectureDao.update(lecture);
				}

			}
		}
		return state;
	}

	//解析和处理服务器返回的讲座搜索数据
	public static int handleLectureSearchResponse(String response, InterimLectureDBDao mInterLecDao, LectureDBDao mLectureDao) {

		int state;

		Gson gson = new Gson();

		LectureSearchResult result = gson.fromJson(response, LectureSearchResult.class);

		state = result.getState();

		if (state == 0) {
			List <Integer> lectureId = result.getLectures_id();
			for (int id : lectureId) {
				LectureDB lectureDB = mLectureDao.queryBuilder().where(LectureDBDao.Properties.LectureId.eq(id)).build().unique();
				if (lectureDB == null) {
					return 3;
				} else {
					InterimLectureDB interimLec = mInterLecDao.queryBuilder().where(InterimLectureDBDao.Properties.LectureId.eq(id)).build().unique();
					if (interimLec == null) {
						InterimLectureDB interimLectureDB = new InterimLectureDB();
						interimLectureDB.setLectureContent(lectureDB.getLectureContent());
						interimLectureDB.setLectureId(lectureDB.getLectureId());
						interimLectureDB.setLectureImage(lectureDB.getLectureImage());
						interimLectureDB.setLectureLocation(lectureDB.getLectureLocation());
						interimLectureDB.setLectureTime(lectureDB.getLectureTime());
						interimLectureDB.setLectureDistrict(lectureDB.getLectureDistrict());
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
	public static int handleAddCommentResponse(String response, UserDBDao mUserDao) {

		int state;

		Gson gson = new Gson();

		AddCommentResult result = gson.fromJson(response, AddCommentResult.class);

		state = result.getState();

		if (state == 0) {

			String userComment = getUserComment(ConstantClass.userOnline, mUserDao);

			UserDB user = mUserDao.queryBuilder().where(UserDBDao.Properties.UserId.eq(ConstantClass.userOnline)).build().unique();

			if (userComment != null) {
				userComment += Long.toString(result.getComment_id()) + ",";
				user.setUserComment(userComment);
				mUserDao.update(user);
			}
		}

		return state;
	}

	//解析和处理服务器返回的更新数据
	public static int handleUpdateResponse(String response) {

		int state;

		Gson gson = new Gson();

		CheckUpdateResult result = gson.fromJson(response, CheckUpdateResult.class);

		state = result.getState();

		if (state == 0) {

		}

		return state;
	}

	//解析和处理服务器返回的正文数据
	public static boolean handleLectureContentResponse(String response, long lectureId, LectureDBDao mLectureDao) {

		boolean isSucceed = false;
		if (!TextUtils.isEmpty(response)) {

			LectureDB lecture = mLectureDao.queryBuilder().where(LectureDBDao.Properties.LectureId.eq(lectureId)).build().unique();

			if (lecture != null) {
				lecture.setLectureContent(response);
				mLectureDao.update(lecture);
				isSucceed = true;
			}
		}
		return isSucceed;
	}

	//解析和处理服务器返回的正文数据
	public static void handleLibraryContentResponse(String response, long libraryID, LibraryDBDao mLibraryDao) {

		if (!TextUtils.isEmpty(response)) {
			LibraryDB library = mLibraryDao.queryBuilder().where(LibraryDBDao.Properties.LibraryID.eq(libraryID)).build().unique();

			if (library != null) {
				library.setLibraryContent(response);
				mLibraryDao.update(library);
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

	public static long lastLetureinDB(LectureDBDao mLectureDao) {

		long lectureID;

		LectureDB lectureDB = mLectureDao.queryBuilder().where(LectureDBDao.Properties.LectureId.eq(mLectureDao.count())).build().unique();

		if (lectureDB == null) {
			lectureID = 0;

		} else {
			lectureID = lectureDB.getLectureId();
		}

		return lectureID;
	}

	public static long lastCommentinDB(CommentDBDao mCommentDao) {

		long commentID;

		CommentDB commentDB = mCommentDao.queryBuilder().where(CommentDBDao.Properties.CommentId.eq(mCommentDao.count())).build().unique();

		if (commentDB == null) {
			commentID = 0;
		} else {
			commentID = commentDB.getCommentId();
		}

		return commentID;
	}

	//分割用户的关注、想看、评论ID字符串
	public static String[] getStrings(String userString) {
		String[] result;
		if (userString != null) {
			result = userString.split(",");
		} else {
			result = null;
		}

		return result;
	}

	public static String getUserFocuse(long userID, UserDBDao mUserDao) {

		UserDB user = mUserDao.queryBuilder().where(UserDBDao.Properties.UserId.eq(userID)).build().unique();
		String userFocuse = "";

		if (user != null) {
			userFocuse = user.getUserFocuseLirary();

		}
		return userFocuse;
	}

	public static String getUserComment(long userID, UserDBDao mUserDao) {

		UserDB user = mUserDao.queryBuilder().where(UserDBDao.Properties.UserId.eq(userID)).build().unique();

		String userComment = "";

		if (user != null) {
			userComment = user.getUserComment();
		}

		return userComment;
	}

	public static String getUserWanted(long userID, UserDBDao mUserDao) {

		UserDB user = mUserDao.queryBuilder().where(UserDBDao.Properties.UserId.eq(userID)).build().unique();

		String userWanted = "";

		if (user != null) {
			userWanted = user.getUserWantedLecture();
		}
		return userWanted;
	}

	public static void getLectureContent(final long lectureID, String lectureInfo, final LectureDBDao mLectureDao) {
		ContentRequest(lectureInfo, new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {
				Logger.d("content获取失败");
			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				Utility.handleLectureContentResponse(response.body().string(), lectureID, mLectureDao);
			}
		});
	}

	public static void getLibraryContent(final long libraryID, String libraryInfo, final LibraryDBDao mLibraryDao) {

		ContentRequest(libraryInfo, new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {
				Logger.d("content获取失败");
			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				Utility.handleLibraryContentResponse(response.body().string(), libraryID, mLibraryDao);
			}
		});
	}

	public static String getNowTime() {

		Date dNow = new Date();
		SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		ft.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));  // 设置北京时区
		String nowTime = ft.format(dNow);

		return nowTime;
	}

	public static int getBitmapSize(Bitmap bitmap) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {     //API 19
			return bitmap.getAllocationByteCount();
		}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {//API 12
			return bitmap.getByteCount();
		}
		return bitmap.getRowBytes() * bitmap.getHeight();                //earlier version
	}

	//获取已经存储在数据库中的讲座ID
	public static long[] getLecturesStoredInDB(LectureDBDao mLectureDdao){
		long[] lectureIDs = null;

		List<LectureDB> lectureDBList=mLectureDdao.loadAll();
		if(lectureDBList!=null){
			lectureIDs=new long[lectureDBList.size()];
			Logger.d("lectureDBList.size= "+lectureDBList.size());
			for(int i=0;i<lectureDBList.size();i++){
				lectureIDs[i]=lectureDBList.get(i).getLectureId();
			}
		}
		return lectureIDs;
	}

	//判断讲座是否已经被存储在数据库中
	public static boolean isLectureStored(long lectureID,long[] lectureIDs){
		boolean result=false;
		for(int i=0;i<lectureIDs.length;i++){
			if (lectureID==lectureIDs[i]){
				result=true;
			}else{
				result=false;
			}
		}
		return result;
	}

	//判断讲座
}
