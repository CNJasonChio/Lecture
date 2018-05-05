package com.jasonchio.lecture;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.widget.EditText;
import com.jasonchio.lecture.util.ActivityCollector;
import com.orhanobut.logger.AndroidLogAdapter;

import static com.orhanobut.logger.Logger.addLogAdapter;

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
 * Created by zhaoyaobang on 2018/3/17.
 */

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener{

	SignOutReceiver receiver;   //退出登录广播接收器对象

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//添加 activity 到 自定义的activity 列表中
		ActivityCollector.addActivity(this);
		//禁止屏幕旋转
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}

	//隐藏标题栏
	public void HideSysTitle(){
		//隐藏自带标题栏
		if(Build.VERSION.SDK_INT>=21){
			View decorView=getWindow().getDecorView();
			decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |View.SYSTEM_UI_FLAG_LAYOUT_STABLE
					| View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
			getWindow().setStatusBarColor(Color.TRANSPARENT);
		}
		ActionBar actionBar=getSupportActionBar();
		if(actionBar!=null){
			actionBar.hide();
		}
	}

	//动态改变hint字体大小
	public void ChangeHintSize(EditText edit, String hint, int size){
		SpannableString ss = new SpannableString(hint);//定义hint的值
		AbsoluteSizeSpan ass = new AbsoluteSizeSpan(size,true);//设置字体大小 true表示单位是sp
		ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		edit.setHint(new SpannedString(ss));
	}

	//初始化视图
	abstract void initView();

	//初始化控件
	abstract void initWidget();

	//初始化事件
	abstract void initEvent();

	@Override
	protected void onDestroy() {
		super.onDestroy();
		//activity 销毁掉后，将其从 activity 列表中删除
		ActivityCollector.removeActivity(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		//注册广播接收器
		IntentFilter intentFilter=new IntentFilter();
		intentFilter.addAction("com.jasonchio.lecture.SIGNOUT");
		receiver=new SignOutReceiver();
		registerReceiver(receiver,intentFilter);
	}

	@Override
	protected void onPause() {
		super.onPause();
		//注销广播接收器
		if(receiver!=null){
			unregisterReceiver(receiver);
			receiver=null;
		}
	}

	//自定义广播接收器类
	class SignOutReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(final Context context, final Intent intent) {
			AlertDialog.Builder builder=new AlertDialog.Builder(context);
			builder.setTitle("退出登录");
			builder.setMessage("确定退出登录吗？");
			builder.setCancelable(false);
			builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					//确定退出登录后，结束所有的 activity,返回到登录界面
					ActivityCollector.finishAll();
					Intent intent1=new Intent(context,LoginActivity.class);
					context.startActivity(intent1);
				}
			});
			builder.show();
		}
	}

}
