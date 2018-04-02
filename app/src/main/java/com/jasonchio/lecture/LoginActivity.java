package com.jasonchio.lecture;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.jasonchio.lecture.util.HttpUtil;
import com.jasonchio.lecture.util.ConstantClass;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import org.json.JSONException;
import java.io.IOException;

import es.dmoral.toasty.Toasty;

import static com.orhanobut.logger.Logger.addLogAdapter;

public class LoginActivity extends BaseActivity {

	EditText passwordEdit;      //填写密码的编辑框
	EditText accountEdit;       //填写帐号的编辑框
	Button loginButton;         //登录按钮

	TextView fgtpwdText;        //忘记密码
	TextView signinText;        //新用户注册

	ImageView isCanSee;         //密码是否可见
	ImageView wechatLoginImage; //微信登录
	ImageView qqLoginImage;     //QQ登录
	ImageView sinaLoginImage;   //新浪微博登录

	boolean cansee=false;       //密码是否可见状态

	String response;
	String userPhone;
	String userPWd;

	int loginResult=-1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		addLogAdapter(new AndroidLogAdapter());

		//初始化控件
		initWidget();
		//初始化视图
		initView();

		//设置注册按钮监听时间
		signinText.setOnClickListener(this);
		//设置找回密码监听事件
		fgtpwdText.setOnClickListener(this);
		//设置密码是否可见按钮监听事件
		isCanSee.setOnClickListener(this);
		//设置登录按钮监听事件
		loginButton.setOnClickListener(this);

	}

	protected void initView(){
		//BaseActivity方法，隐藏系统标题栏
		HideSysTitle();

		//BaseActivity方法，动态设置输入邮箱和密码提示语的文字大小
		ChangeHintSize(accountEdit,"请输入手机号码",14);
		ChangeHintSize(passwordEdit,"请输入密码",14);

		//默认密码不可见
		passwordEdit.setTransformationMethod(PasswordTransformationMethod.getInstance());
	}

	protected void initWidget(){

		passwordEdit=(EditText)findViewById(R.id.login_password_edit);
		accountEdit=(EditText)findViewById(R.id.login_account_edit);
		loginButton=(Button)findViewById(R.id.login_button);
		fgtpwdText=(TextView)findViewById(R.id.login_fgtpwd_text);
		signinText=(TextView)findViewById(R.id.login_signin_text);
		wechatLoginImage=(ImageView)findViewById(R.id.login_wechat_login);
		qqLoginImage=(ImageView)findViewById(R.id.login_qq_login);
		sinaLoginImage=(ImageView)findViewById(R.id.login_sina_login);
		isCanSee=(ImageView)findViewById(R.id.login_pwd_cansee);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.login_signin_text:{
				Intent intent=new Intent(LoginActivity.this,SigninWithPhoneActivity.class);
				startActivity(intent);
				break;
			}
			case R.id.login_fgtpwd_text:{
				Intent intent=new Intent(LoginActivity.this,ForgetPwdActivity.class);
				startActivity(intent);
				break;
			}
			case R.id.login_pwd_cansee:{
				if (cansee==false){
					//如果是不能看到密码的情况下，
					passwordEdit.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
					isCanSee.setImageResource(R.drawable.ic_pwd_cansee);
					cansee=true;
				}else {
					//如果是能看到密码的状态下
					passwordEdit.setTransformationMethod(PasswordTransformationMethod.getInstance());
					isCanSee.setImageResource(R.drawable.ic_pwd_cantsee);
					cansee=false;
				}
				break;
			}
			case R.id.login_button:{
				userPhone=accountEdit.getText().toString();
				userPWd=passwordEdit.getText().toString();
				/*LoginRequest("15871714056","123");
				if(loginResult==1){
					Toasty.success(LoginActivity.this,"登录成功").show();
					Intent intent=new Intent(LoginActivity.this,MainPageActivity.class);
					startActivity(intent);
				}else{

				}*/
				LoginRequest(userPhone,userPWd);
				if(loginResult==1){
					Toasty.success(LoginActivity.this,"登录成功").show();
					Intent intent=new Intent(LoginActivity.this,MainPageActivity.class);
					startActivity(intent);
				}else{

				}
				Intent intent=new Intent(LoginActivity.this,MainPageActivity.class);
				startActivity(intent);
				break;
			}
			default:
		}
	}
	private void LoginRequest(final String userPhone,final String userPwd){
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Logger.d("开始与服务器通讯，发出登录请求");
					response = HttpUtil.LoginRequest(ConstantClass.ADDRESS, ConstantClass.LOGIN_PORT, userPhone,userPwd);

					//loginResult= Utility.handleLoginRespose(response,LoginActivity.this);

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
