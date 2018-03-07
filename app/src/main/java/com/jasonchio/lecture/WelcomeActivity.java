package com.jasonchio.lecture;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jasonchio.lecture.util.HttpUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WelcomeActivity extends AppCompatActivity {

	ImageView welcomeImage;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final View view = View.inflate(this, R.layout.activity_welcome, null);
		setContentView(view);

		welcomeImage=(ImageView) findViewById(R.id.welcome_image);


		//隐藏自带标题栏
		if (Build.VERSION.SDK_INT >= 21) {
			View decorView = getWindow().getDecorView();
			decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
					| View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
			getWindow().setStatusBarColor(Color.TRANSPARENT);
		}
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.hide();
		}

		SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(this);

		String bingPic=preferences.getString("bing_pic",null);
		if(bingPic!=null){
			Glide.with(this).load(bingPic).into(welcomeImage);
		}else{
			loadBingPic();
		}

		//渐变背景图片
		AlphaAnimation aa = new AlphaAnimation(0.3f, 1.0f);
		aa.setDuration(2000);//设置动画持续时间
		view.startAnimation(aa);
		aa.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationEnd(Animation arg0) {
				toMainActivity();
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationStart(Animation animation) {
			}

		});

	}

	private void loadBingPic() {
		String requestBingPic="http://guolin.tech/api/bing_pic";
		HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {
				e.printStackTrace();
			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				final String bingPic=response.body().string();
				SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(WelcomeActivity.this).edit();
				editor.apply();
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Glide.with(WelcomeActivity.this).load(bingPic).into(welcomeImage);
					}
				});
			}
		});
	}

	//从登陆界面跳转到主界面LoginActivity

	private void toMainActivity() {
		Intent intent = new Intent(this, LoginActivity.class);
		startActivity(intent);
		finish();
	}
}