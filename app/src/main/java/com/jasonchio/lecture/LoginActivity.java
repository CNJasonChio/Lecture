package com.jasonchio.lecture;


import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jasonchio.lecture.util.InterfaceControl;

import cn.smssdk.SMSSDK;


public class LoginActivity extends AppCompatActivity {

	EditText passwordEdit;      //填写密码的编辑框
	EditText accountEdit;       //填写帐号的编辑框
	Button loginButton;         //登录按钮
	Button isCanSee;            //密码是否可见按钮
	TextView fgtpwdText;        //忘记密码
	TextView signinText;        //新用户注册
	ImageView wechatLoginImage; //微信登录
	ImageView qqLoginImage;     //QQ登录
	ImageView sinaLoginImage;   //新浪微博登录

	boolean cansee=false;        //密码是否可见状态

	InterfaceControl interfaceControl=new InterfaceControl();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		passwordEdit=(EditText)findViewById(R.id.login_password_edit);
		accountEdit=(EditText)findViewById(R.id.login_account_edit);
		loginButton=(Button)findViewById(R.id.login_button);
		fgtpwdText=(TextView)findViewById(R.id.login_fgtpwd_text);
		signinText=(TextView)findViewById(R.id.login_signin_text);
		wechatLoginImage=(ImageView)findViewById(R.id.login_wechat_login);
		qqLoginImage=(ImageView)findViewById(R.id.login_qq_login);
		sinaLoginImage=(ImageView)findViewById(R.id.login_sina_login);
		isCanSee=(Button)findViewById(R.id.login_pwd_cansee);

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

		//动态设置输入邮箱和密码提示语的文字大小
		interfaceControl.ChangeHintSize(accountEdit,"请输入手机号码",14);
		interfaceControl.ChangeHintSize(passwordEdit,"请输入密码",14);

		signinText.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(LoginActivity.this,SigninWithPhoneActivity.class);
				startActivity(intent);
			}
		});

		fgtpwdText.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(LoginActivity.this,ForgetPwdActivity.class);
				startActivity(intent);
			}
		});

		//默认密码不可见
		passwordEdit.setTransformationMethod(PasswordTransformationMethod.getInstance());

		isCanSee.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (cansee==false){
					//如果是不能看到密码的情况下，
					passwordEdit.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
					cansee=true;
				}else {
					//如果是能看到密码的状态下
					passwordEdit.setTransformationMethod(PasswordTransformationMethod.getInstance());
					cansee=false;
				}
			}
		});

		loginButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(LoginActivity.this,FirstpageActivity.class);
				startActivity(intent);
			}
		});

	}
}
