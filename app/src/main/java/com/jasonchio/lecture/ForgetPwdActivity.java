package com.jasonchio.lecture;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.jasonchio.lecture.util.InterfaceControl;
import com.jasonchio.lecture.util.JudgePhoneNums;
import com.mob.MobSDK;

import org.json.JSONException;
import org.json.JSONObject;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class ForgetPwdActivity extends AppCompatActivity {

	EditText newpasswordEdit;       //填写新密码的编辑框
	EditText fgtpwdaccountEdit;     //填写找回密码的手机号
	EditText confirmEdit;           //确定新密码的编辑框
	EditText vercodeEdit;           //填写验证码的编辑框

	Button backButton;              //返回按钮
	Button changepwdButton;         //确定修改密码的按钮
	Button sendForpwdVercodeButton; //发送验证码的按钮
	ImageView pwdCanSee;               //密码是否可见
	ImageView repwdCanSee;             //重复密码是否可见

	TitleLayout titleLayout;        //标题栏
	Handler handler;                //验证码的回调监听
	int countdown=30;               //获取验证码倒计时30s
	JudgePhoneNums judgePhoneNums=new JudgePhoneNums();             //自定义判断手机号是否合法类对象
	InterfaceControl interfaceControl=new InterfaceControl();       //自定义界面控制器类对象

	boolean pwdcansee=true;        //密码是否可见状态
	boolean repwdcansee=true;      //重复密码是否可见状态

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//MobSDK.init(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forgetpwd);

		newpasswordEdit=(EditText)findViewById(R.id.pgtpwd_newpassword_edit);
		fgtpwdaccountEdit=(EditText)findViewById(R.id.fgtpwd_account_edit);
		confirmEdit=(EditText)findViewById(R.id.fgtpwd_repassword_edit);
		backButton=titleLayout.getFirstButton();
		changepwdButton=(Button)findViewById(R.id.fgtpwd_change_password);
		sendForpwdVercodeButton=(Button)findViewById(R.id.fgtpwd_send_vercode);
		vercodeEdit=(EditText)findViewById(R.id.fgtpwd_vercode_edit);
		titleLayout=(TitleLayout)findViewById(R.id.fgtpwd_title_layout);
		pwdCanSee=(ImageView) findViewById(R.id.fgtpwd_pwd_cansee);
		repwdCanSee=(ImageView) findViewById(R.id.fgtpwd_repwd_cansee);

		titleLayout.setTitle("找回密码");
		titleLayout.setSecondButtonVisible(View.GONE);
		backButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		//隐藏自带标题栏
		if(Build.VERSION.SDK_INT>=21){
			View decorView=getWindow().getDecorView();
			decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
					| View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
			getWindow().setStatusBarColor(Color.TRANSPARENT);
		}
		ActionBar actionBar=getSupportActionBar();
		if(actionBar!=null){
			actionBar.hide();
		}

		interfaceControl.ChangeHintSize(fgtpwdaccountEdit,"请输入手机号",14);
		interfaceControl.ChangeHintSize(newpasswordEdit,"请输入新密码",14);
		interfaceControl.ChangeHintSize(confirmEdit,"请再次输入新密码",14);

		//默认密码不可见
		confirmEdit.setTransformationMethod(PasswordTransformationMethod.getInstance());
		newpasswordEdit.setTransformationMethod(PasswordTransformationMethod.getInstance());

		//设置密码是否可见按钮点击事件
		pwdCanSee.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (pwdcansee==false){
					//如果是不能看到密码的情况下，
					newpasswordEdit.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
					pwdCanSee.setImageResource(R.drawable.ic_pwd_cansee);
					pwdcansee=true;
				}else {
					//如果是能看到密码的状态下
					newpasswordEdit.setTransformationMethod(PasswordTransformationMethod.getInstance());
					pwdCanSee.setImageResource(R.drawable.ic_pwd_cantsee);
					pwdcansee=false;
				}
			}
		});

		repwdCanSee.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (repwdcansee==false){
					//如果是不能看到密码的情况下，
					confirmEdit.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
					repwdCanSee.setImageResource(R.drawable.ic_pwd_cansee);
					repwdcansee=true;
				}else {
					//如果是能看到密码的状态下
					confirmEdit.setTransformationMethod(PasswordTransformationMethod.getInstance());
					repwdCanSee.setImageResource(R.drawable.ic_pwd_cantsee);
					repwdcansee=false;
				}
			}
		});

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
		sendForpwdVercodeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String phone=fgtpwdaccountEdit.getText().toString();
				if (!judgePhoneNums.judgePhoneNums(phone)) {// 判断输入号码是否正确
					Toast.makeText(ForgetPwdActivity.this,"手机号码不正确",Toast.LENGTH_SHORT).show();
					return;
				}
				SMSSDK.getVerificationCode("86", phone); // 调用sdk发送短信验证
				sendForpwdVercodeButton.setClickable(false);// 设置按钮不可点击 显示倒计时
				sendForpwdVercodeButton.setText("重新发送(" + countdown + ")");
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
			}
		});

		//设置注册按钮监听事件
		changepwdButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String password=newpasswordEdit.getText().toString();
				String repassword=confirmEdit.getText().toString();
				String vercode=vercodeEdit.getText().toString();
				String phone=fgtpwdaccountEdit.getText().toString();
				if(!isPwdsame(password,repassword)){
					Toast.makeText(ForgetPwdActivity.this,"两次密码不一致",Toast.LENGTH_SHORT).show();
					return;
				}else{
					SMSSDK.submitVerificationCode("86", phone, vercode);
				}
			}
		});

		Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				if (msg.what == -9) {
					sendForpwdVercodeButton.setText("重新发送(" + countdown + ")");
				} else if (msg.what == -8) {
					sendForpwdVercodeButton.setText("发送验证码");
					sendForpwdVercodeButton.setClickable(true); // 设置可点击
					countdown = 30;
				} else {
					int event = msg.arg1;
					int result = msg.arg2;
					Object data = msg.obj;
					if (result == SMSSDK.RESULT_COMPLETE) {
						// 短信注册成功后，返回MainActivity,然后提示
						if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {// 提交验证码成功
							Toast.makeText(getApplicationContext(), "注册成功",
									Toast.LENGTH_SHORT).show();
							// 验证成功后跳转登录界面
							Intent intent = new Intent(ForgetPwdActivity.this, LoginActivity.class);
							startActivity(intent);
							finish();// 成功跳转之后销毁当前页面
						} else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
							Toast.makeText(getApplicationContext(), "验证码已经发送",
									Toast.LENGTH_SHORT).show();
						} else {
							((Throwable) data).printStackTrace();
						}
					}
					else{
						Toast.makeText(ForgetPwdActivity.this,"注册失败，请稍候再试",Toast.LENGTH_SHORT).show();
					}
				}
			}
		};
	}

	//判断两次输入的密码是否相同
	protected boolean isPwdsame(String password,String repassword){
		if(password.equals(repassword))
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
