package com.jasonchio.lecture;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.jasonchio.lecture.util.HttpUtil;
import com.jasonchio.lecture.util.JudgePhoneNums;
import com.jasonchio.lecture.util.ConstantClass;
import com.jasonchio.lecture.util.Utility;
import com.mob.MobSDK;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import es.dmoral.toasty.Toasty;

public class ForgetPwdActivity extends BaseActivity {

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

	boolean pwdcansee=true;        //密码是否可见状态
	boolean repwdcansee=true;      //重复密码是否可见状态

	String response;

	int findPwdResult;

	String password;
	String repassword;
	String vercode;
	String phone;

	@SuppressLint("HandlerLeak")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		MobSDK.init(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forgetpwd);

		//初始化控件
		initWidget();
		//初始化视图
		initView();

		//标题返回按钮的点击监听
		backButton.setOnClickListener(this);
		//设置密码是否可见按钮的点击监听
		pwdCanSee.setOnClickListener(this);
		//设置重复密码是否可见按钮的点击监听
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
		sendForpwdVercodeButton.setOnClickListener(this);
		//设置注册按钮监听事件
		changepwdButton.setOnClickListener(this);

		handler = new Handler() {
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
						// 短信找回密码成功后，返回LoginActivity,然后提示
						if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {// 提交验证码成功
							FindPwdRequest(phone,password);
							if (findPwdResult == 1) {
								Toasty.success(ForgetPwdActivity.this, "找回密码成功").show();
								// 验证成功后跳转登录界面
								Intent intent = new Intent(ForgetPwdActivity.this, LoginActivity.class);
								startActivity(intent);
								finish();// 成功跳转之后销毁当前页面
							} else {

							}

						} else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
							Toasty.success(ForgetPwdActivity.this, "短信验证码发送成功").show();
						} else {

						}
					} else {
						((Throwable) data).printStackTrace();
						Throwable throwable = (Throwable) data;
						try {
							JSONObject obj = new JSONObject(throwable.getMessage());
							final String des = obj.optString("detail");
							if (!TextUtils.isEmpty(des)) {
								Logger.d(des);
							}
							Toasty.error(ForgetPwdActivity.this, "验证码系统异常，请稍候再试").show();
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}
			}
		};
	}

		protected void initWidget() {
			titleLayout = (TitleLayout) findViewById(R.id.fgtpwd_title_layout);
			newpasswordEdit = (EditText) findViewById(R.id.pgtpwd_newpassword_edit);
			fgtpwdaccountEdit = (EditText) findViewById(R.id.fgtpwd_account_edit);
			confirmEdit = (EditText) findViewById(R.id.fgtpwd_repassword_edit);
			backButton = titleLayout.getFirstButton();
			changepwdButton = (Button) findViewById(R.id.fgtpwd_change_password);
			sendForpwdVercodeButton = (Button) findViewById(R.id.fgtpwd_send_vercode);
			vercodeEdit = (EditText) findViewById(R.id.fgtpwd_vercode_edit);
			pwdCanSee = (ImageView) findViewById(R.id.fgtpwd_pwd_cansee);
			repwdCanSee = (ImageView) findViewById(R.id.fgtpwd_repwd_cansee);
		}

		protected void initView() {

			//BaseActivity方法，隐藏系统标题栏
			HideSysTitle();

			titleLayout.setTitle("找回密码");
			titleLayout.setSecondButtonVisible(View.GONE);

			//设置密码默认不可见
			confirmEdit.setTransformationMethod(PasswordTransformationMethod.getInstance());
			newpasswordEdit.setTransformationMethod(PasswordTransformationMethod.getInstance());
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.title_first_button: {
					finish();
					break;
				}
				case R.id.fgtpwd_pwd_cansee: {
					if (pwdcansee == false) {
						//如果是不能看到密码的情况下，
						newpasswordEdit.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
						pwdCanSee.setImageResource(R.drawable.ic_pwd_cansee);
						pwdcansee = true;
					} else {
						//如果是能看到密码的状态下
						newpasswordEdit.setTransformationMethod(PasswordTransformationMethod.getInstance());
						pwdCanSee.setImageResource(R.drawable.ic_pwd_cantsee);
						pwdcansee = false;
					}
					break;
				}
				case R.id.fgtpwd_repwd_cansee: {
					if (repwdcansee == false) {
						//如果是不能看到密码的情况下，
						confirmEdit.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
						repwdCanSee.setImageResource(R.drawable.ic_pwd_cansee);
						repwdcansee = true;
					} else {
						//如果是能看到密码的状态下
						confirmEdit.setTransformationMethod(PasswordTransformationMethod.getInstance());
						repwdCanSee.setImageResource(R.drawable.ic_pwd_cantsee);
						repwdcansee = false;
					}
					break;
				}
				case R.id.fgtpwd_send_vercode: {
					phone = fgtpwdaccountEdit.getText().toString();
					if (!judgePhoneNums.judgePhoneNums(phone)) {// 判断输入号码是否正确
						Toast.makeText(ForgetPwdActivity.this, "手机号码不正确", Toast.LENGTH_SHORT).show();
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
					break;
				}
				case R.id.fgtpwd_change_password: {
					password = newpasswordEdit.getText().toString();
					repassword = confirmEdit.getText().toString();
					vercode = vercodeEdit.getText().toString();
					phone = fgtpwdaccountEdit.getText().toString();
					if (!isPwdsame(password, repassword)) {
						Toast.makeText(ForgetPwdActivity.this, "两次密码不一致", Toast.LENGTH_SHORT).show();
						return;
					} else {
						//SMSSDK.submitVerificationCode("86", phone, vercode);

						/*
						* 临时测试
						* */

						FindPwdRequest("15871714056","123");
					}
					break;
				}
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

		private void FindPwdRequest(final String userPhone, final String userPwd) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						//获取服务器返回数据
						response = HttpUtil.FindPwdRequest(ConstantClass.ADDRESS, ConstantClass.FINDPWD_PORT, userPhone, userPwd);
						//解析和处理服务器返回的数据
						findPwdResult = Utility.handleFindPwdRespose(response, ForgetPwdActivity.this);
					} catch (IOException e) {
						Logger.e(e,"IOException");
					} catch (JSONException e) {
						Logger.e(e,"JSONEException");
					}
				}
			}).start();
		}
	}
