package com.jasonchio.lecture;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jasonchio.lecture.greendao.DaoSession;
import com.jasonchio.lecture.greendao.UserDBDao;
import com.jasonchio.lecture.util.ConstantClass;
import com.jasonchio.lecture.util.HttpUtil;
import com.jasonchio.lecture.util.MD5Util;
import com.jasonchio.lecture.util.Utility;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import org.json.JSONException;

import java.io.IOException;

import es.dmoral.toasty.Toasty;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WelcomeActivity extends BaseActivity {

	ImageView welcomeImage;

	SharedPreferences preferences;

	Handler handler;

	String response;
	String userPhone;
	String userPwd;

	int loginResult = -1;

	boolean isRemPwd=false;

	DaoSession mDaoSession ;

	UserDBDao mUserDao;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final View view = View.inflate(this, R.layout.activity_welcome, null);
		setContentView(view);
		Logger.addLogAdapter(new AndroidLogAdapter());
		//初始化控件
		initWidget();
		//初始化视图
		initView();

		initEvent();

		//渐变背景图片
		AlphaAnimation aa = new AlphaAnimation(0.3f, 1.0f);
		aa.setDuration(1500);//设置动画持续时间
		view.startAnimation(aa);
		aa.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationEnd(Animation arg0) {
				checkSharePreference();
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationStart(Animation animation) {
			}

		});

	}

//	private void loadBingPic() {
//		String requestBingPic="http://guolin.tech/api/bing_pic";
//		HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
//			@Override
//			public void onFailure(Call call, IOException e) {
//				e.printStackTrace();
//			}
//
//			@Override
//			public void onResponse(Call call, Response response) throws IOException {
//				final String bingPic=response.body().string();
//				SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(WelcomeActivity.this).edit();
//				editor.apply();
//				runOnUiThread(new Runnable() {
//					@Override
//					public void run() {
//						Glide.with(WelcomeActivity.this).load(bingPic).into(welcomeImage);
//					}
//				});
//			}
//		});
//	}


	private void checkSharePreference(){
		isRemPwd=preferences.getBoolean("remember_password",false);
		if(isRemPwd){
			userPhone=preferences.getString("account","");
			userPwd=preferences.getString("password","");
			loginRequest();
		}else {
			Intent intent=new Intent(WelcomeActivity.this,LoginActivity.class);
			startActivity(intent);
			finish();
		}
	}


	@Override
	void initView() {
		//BaseActivity方法，隐藏系统标题栏
		HideSysTitle();

//		SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(this);
//
//		String bingPic=preferences.getString("bing_pic",null);
//		if(bingPic!=null){
//			Glide.with(this).load(bingPic).into(welcomeImage);
//		}else{
//			loadBingPic();
//		}
	}

	@Override
	void initWidget() {
		welcomeImage=(ImageView) findViewById(R.id.welcome_image);
		preferences=PreferenceManager.getDefaultSharedPreferences(this);
		mDaoSession = ((MyApplication)getApplication()).getDaoSession();
		mUserDao = mDaoSession.getUserDBDao();
		isRemPwd=preferences.getBoolean("rememeber_password",false);
	}

	@Override
	void initEvent() {
		handler=new Handler(new Handler.Callback() {
			@Override
			public boolean handleMessage(Message msg) {
				switch (msg.what){
					case 1:
						if (loginResult == 0) {
							Intent intent = new Intent(WelcomeActivity.this, MainPageActivity.class);
							startActivity(intent);
							finish();
						} else if (loginResult == 1) {
							Toasty.error(WelcomeActivity.this, "密码已过期，请重新登录").show();
							Intent intent=new Intent(WelcomeActivity.this,LoginActivity.class);
							intent.putExtra("login_result",false);
							startActivity(intent);
							finish();
						} else {
							Toasty.error(WelcomeActivity.this, "服务器出错，请稍候再试").show();
							finish();
						}
						break;
				}
				return true;
			}
		});
	}

	@Override
	public void onClick(View v) {

	}

	private void loginRequest() {

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					//response = HttpUtil.LoginRequest(ConstantClass.ADDRESS, ConstantClass.LOGIN_PORT, userPhone, userPwd);
					response = HttpUtil.LoginRequest(ConstantClass.ADDRESS, ConstantClass.LOGIN_COM, userPhone, MD5Util.md5encrypt(userPwd));
					Logger.d(response);
					loginResult = Utility.handleLoginRespose(response,userPhone,mUserDao);
					handler.sendEmptyMessage(1);
				} catch (IOException e) {
					Logger.d("通信失败，IO error");
					e.printStackTrace();
				} catch (JSONException e) {
					Logger.d("通信失败，JSON error");
					e.printStackTrace();
				}
			}
		}).start();
	}
}