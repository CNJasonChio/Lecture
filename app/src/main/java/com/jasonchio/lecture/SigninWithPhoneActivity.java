package com.jasonchio.lecture;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.jasonchio.lecture.util.JudgePhoneNums;
import com.mob.MobSDK;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class SigninWithPhoneActivity extends BaseActivity implements View.OnClickListener {

	EditText verCodeEdit;           //填写验证码的编辑框
	EditText phoneEdit;             //填写注册手机号的编辑框
	EditText passwordEdit;          //填写密码的编辑框
	EditText confirmPwdEdit;        //填写确认密码的编辑框

	Button titleFirstButton;        //标题返回按钮
	Button sendCodeButton;          //发送验证码的按钮
	Button signInButton;            //确认注册的按钮
	ImageView pwdCanSee;               //密码是否可见
	ImageView repwdCanSee;             //重复密码是否可见

	Handler handler;                //验证码回调监听接口

	TitleLayout titleLayout;        //标题栏

	int countdown = 30;             //获取验证码倒计时30s

	JudgePhoneNums judgePhoneNums = new JudgePhoneNums();             //自定义判断手机号是否合法类对象

	boolean pwdcansee = false;        //密码是否可见状态
	boolean repwdcansee = false;      //重复密码是否可见状态

	@SuppressLint("HandlerLeak")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		MobSDK.init(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signin_with_phone);
		//初始化控件
		initWidget();
		//初始化视图
		initView();

		//设置标题返回按钮的点击监听
		titleFirstButton.setOnClickListener(this);
		//设置密码是否可见按钮的点击监听
		pwdCanSee.setOnClickListener(this);

		repwdCanSee.setOnClickListener(this);

		EventHandler eventHandler = new EventHandler() {
			@Override
			public void afterEvent(int event, int result, Object data) {
				Message msg = new Message();
				msg.arg1 = event;
				msg.arg2 = result;
				msg.obj = data;
				handler.sendMessage(msg);
			}
		};

		SMSSDK.registerEventHandler(eventHandler); // 注册回调监听接口

		//设置发送验证码按钮监听事件
		sendCodeButton.setOnClickListener(this);

		//设置注册按钮监听事件
		signInButton.setOnClickListener(this);

		handler = new Handler() {
			public void handleMessage(Message msg) {
				if (msg.what == -9) {
					sendCodeButton.setText("重新发送(" + countdown + ")");
				} else if (msg.what == -8) {
					sendCodeButton.setText("发送验证码");
					sendCodeButton.setClickable(true); // 设置可点击
					countdown = 30;
				} else {
					int event = msg.arg1;
					int result = msg.arg2;
					Object data = msg.obj;
					if (result == SMSSDK.RESULT_COMPLETE) {
						// 短信注册成功后，返回LoginActivity,然后提示
						if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {// 提交验证码成功
							Toast.makeText(getApplicationContext(), "注册成功",
									Toast.LENGTH_SHORT).show();
							// 验证成功后跳转登录界面
							Intent intent = new Intent(SigninWithPhoneActivity.this, LoginActivity.class);
							startActivity(intent);
							finish();// 成功跳转之后销毁当前页面
						} else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
							Toast.makeText(getApplicationContext(), "验证码已经发送",
									Toast.LENGTH_SHORT).show();
						} else {
							//else((Throwable) data).printStackTrace();
						}
					}
					else{
						Toast.makeText(SigninWithPhoneActivity.this,"注册失败，请稍候再试",Toast.LENGTH_SHORT).show();
					}
				}
			}
		};
	}

	protected void initWidget(){
		titleLayout = (TitleLayout) findViewById(R.id.signin_title_layout);
		verCodeEdit = (EditText) findViewById(R.id.signin_vercode_edit);
		phoneEdit = (EditText) findViewById(R.id.signin_account_edit);
		sendCodeButton = (Button) findViewById(R.id.signin_send_vercode);
		signInButton = (Button) findViewById(R.id.signin);
		titleFirstButton=titleLayout.getFirstButton();
		passwordEdit = (EditText) findViewById(R.id.signin_newpassword_edit);
		confirmPwdEdit = (EditText) findViewById(R.id.signin_repassword_edit);
		pwdCanSee = (ImageView) findViewById(R.id.signin_pwd_cansee);
		repwdCanSee = (ImageView) findViewById(R.id.signin_repwd_cansee);
	}
	protected void initView(){
		ChangeHintSize(phoneEdit, "请输入手机号", 14);
		ChangeHintSize(passwordEdit, "请输入密码", 14);
		ChangeHintSize(confirmPwdEdit, "请重新输入密码", 14);

		//BaseActivity方法，隐藏系统标题栏
		HideSysTitle();

		titleLayout.setTitle("新用户注册");
		titleLayout.setSecondButtonVisible(View.GONE);

		//设置密码默认不可见
		confirmPwdEdit.setTransformationMethod(PasswordTransformationMethod.getInstance());
		passwordEdit.setTransformationMethod(PasswordTransformationMethod.getInstance());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.title_first_button:{
				finish();
				break;
			}
			case R.id.signin_pwd_cansee:{
				if (pwdcansee == false) {
					//如果是不能看到密码的情况下，
					passwordEdit.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
					pwdCanSee.setImageResource(R.drawable.ic_pwd_cansee);
					pwdcansee = true;
				} else {
					//如果是能看到密码的状态下
					passwordEdit.setTransformationMethod(PasswordTransformationMethod.getInstance());
					pwdCanSee.setImageResource(R.drawable.ic_pwd_cantsee);
					pwdcansee = false;
				}
				break;
			}
			case R.id.signin_repwd_cansee:{
				if (repwdcansee == false) {
					//如果是不能看到密码的情况下，
					confirmPwdEdit.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
					repwdCanSee.setImageResource(R.drawable.ic_pwd_cansee);
					repwdcansee = true;
				} else {
					//如果是能看到密码的状态下
					confirmPwdEdit.setTransformationMethod(PasswordTransformationMethod.getInstance());
					repwdCanSee.setImageResource(R.drawable.ic_pwd_cantsee);
					repwdcansee = false;
				}
				break;
			}
			case R.id.signin_send_vercode:{
				String phone=phoneEdit.getText().toString();
				if (!judgePhoneNums.judgePhoneNums(phone)) {// 判断输入号码是否正确
					Toast.makeText(SigninWithPhoneActivity.this,"手机号码不正确",Toast.LENGTH_SHORT).show();
					return;
				}
				SMSSDK.getVerificationCode("86", phone); // 调用sdk发送短信验证
				sendCodeButton.setClickable(false);// 设置按钮不可点击 显示倒计时
				sendCodeButton.setText("重新发送(" + countdown + ")");
				new Thread(new Runnable() {
					@Override
					public void run() {
						for (countdown = 30; countdown > 0; countdown--) {
							handler.sendEmptyMessage(-9);
							if (countdown <= 0) {
								break;
							}
							try {
								Thread.sleep(1000);// 线程休眠实现读秒功能
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
						handler.sendEmptyMessage(-8);// 在30秒后重新显示为获取验证码
					}
				}).start();
				break;
			}
			case R.id.signin:{
				String password=passwordEdit.getText().toString();
				String repassword=confirmPwdEdit.getText().toString();
				String vercode=verCodeEdit.getText().toString();
				String phoneNum=phoneEdit.getText().toString();
				if(!isPwdsame(password,repassword)){
					Toast.makeText(SigninWithPhoneActivity.this,"两次密码不一致",Toast.LENGTH_SHORT).show();
					return;
				}else{
					SMSSDK.submitVerificationCode("86", phoneNum, vercode);
				}
				break;
			}
			default:
		}
	}

	//判断两次输入的密码是否相同
	protected boolean isPwdsame(String password, String repassword) {
		if (password.equals(repassword))
			return true;
		else
			return false;
	}

	//销毁handler
	protected void onDestroy() {
		super.onDestroy();
		SMSSDK.unregisterAllEventHandler();
	}
}

