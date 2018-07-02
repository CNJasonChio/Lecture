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

import com.jasonchio.lecture.util.HttpUtil;
import com.jasonchio.lecture.util.ConstantClass;
import com.jasonchio.lecture.util.MD5Util;
import com.jasonchio.lecture.util.Utility;
import com.mob.MobSDK;

import org.json.JSONException;

import java.io.IOException;

import com.orhanobut.logger.Logger;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import es.dmoral.toasty.Toasty;

public class SigninWithPhoneActivity extends BaseActivity implements View.OnClickListener {

	EditText verCodeEdit;           //填写验证码的编辑框
	EditText phoneEdit;             //填写注册手机号的编辑框
	EditText passwordEdit;          //填写密码的编辑框
	EditText confirmPwdEdit;        //填写确认密码的编辑框

	Button titleFirstButton;        //标题返回按钮
	Button sendCodeButton;          //发送验证码的按钮
	Button signInButton;            //确认注册的按钮
	ImageView pwdCanSee;            //密码是否可见
	ImageView repwdCanSee;          //重复密码是否可见

	Handler handler;                //验证码回调监听接口

	TitleLayout titleLayout;        //标题栏

	int countdown = 30;             //获取验证码倒计时30s

	boolean pwdcansee = false;      //密码是否可见状态
	boolean repwdcansee = false;    //重复密码是否可见状态

	String response;                //服务器返回数据
	String password;                //密码
	String repassword;              //确认密码
	String vercode;                 //验证码
	String phoneNum;                //手机号

	int signinResult = -1;          //注册结果

	int networkState;               //网络状态

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

		initEvent();
	}

	//初始化控件
	protected void initWidget() {
		titleLayout = (TitleLayout) findViewById(R.id.signin_title_layout);
		verCodeEdit = (EditText) findViewById(R.id.signin_vercode_edit);
		phoneEdit = (EditText) findViewById(R.id.signin_account_edit);
		sendCodeButton = (Button) findViewById(R.id.signin_send_vercode);
		signInButton = (Button) findViewById(R.id.signin);
		titleFirstButton = titleLayout.getFirstButton();
		passwordEdit = (EditText) findViewById(R.id.signin_newpassword_edit);
		confirmPwdEdit = (EditText) findViewById(R.id.signin_repassword_edit);
		pwdCanSee = (ImageView) findViewById(R.id.signin_pwd_cansee);
		repwdCanSee = (ImageView) findViewById(R.id.signin_repwd_cansee);
	}

	@Override
	void initEvent() {
		//设置标题返回按钮的点击监听
		titleFirstButton.setOnClickListener(this);
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
		sendCodeButton.setOnClickListener(this);

		//设置注册按钮监听事件
		signInButton.setOnClickListener(this);

		handler = new Handler(new Handler.Callback() {
			@Override
			public boolean handleMessage(Message msg) {
				if (msg.what == -9) {
					sendCodeButton.setText("重新发送(" + countdown + ")");
				} else if (msg.what == -8) {
					sendCodeButton.setText("发送验证码");
					sendCodeButton.setClickable(true); // 设置可点击
					countdown = 30;
				} else if (msg.what == -7) {
					Logger.d("短信验证码验证成功，正在向服务器发送注册请求");
					//结果为0：注册成功
					if (signinResult == 0) {
						// 验证成功后跳转登录界面
						Intent intent = new Intent(SigninWithPhoneActivity.this, LoginActivity.class);
						startActivity(intent);
						// 成功跳转之后销毁当前页面
						finish();
						Toasty.success(SigninWithPhoneActivity.this, "注册成功").show();
					} else if (signinResult == 1) {
						Toasty.error(SigninWithPhoneActivity.this, "该手机号已经注册，请直接登录").show();
					} else {
						Toasty.error(SigninWithPhoneActivity.this, "服务器出错，请稍候再试").show();
					}
				} else {
					Logger.d("正在验证短信验证码");
					int event = msg.arg1;
					int result = msg.arg2;
					Object data = msg.obj;
					if (result == SMSSDK.RESULT_COMPLETE) {
						// 提交验证码成功
						if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
							//发送注册请求
							SigninRequest(phoneNum, MD5Util.md5encrypt(password));

						} else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
							Toasty.success(SigninWithPhoneActivity.this, "验证码发送成功").show();
						} else {
							Toasty.info(SigninWithPhoneActivity.this, "出现了未知事件，请联系开发者并说明情况").show();
						}
					} else {
						((Throwable) data).printStackTrace();
						Throwable throwable = (Throwable) data;
						String des = throwable.getMessage();
						Logger.json(des);
						if (!TextUtils.isEmpty(des)) {
							//短信验证结果
							final int VercodeResult;
							//处理短信验证结果
							VercodeResult = Utility.handleVercodeResponse(des);
							//错误结果分析
							vercodeResult(VercodeResult);
						}
					}
				}
				return false;
			}
		});
	}

	//初始化视图
	protected void initView() {
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

	//点击事件处理
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.title_first_button: {
				finish();
				break;
			}
			case R.id.signin_pwd_cansee: {
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
			case R.id.signin_repwd_cansee: {
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
			case R.id.signin_send_vercode: {
					String phone = phoneEdit.getText().toString();
					// 判断输入号码是否正确
					if (!Utility.judgePhoneNums(phone)) {
						Toasty.error(SigninWithPhoneActivity.this, "手机号码不正确");
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
			case R.id.signin: {
					//获取界面输入信息
					password = passwordEdit.getText().toString();
					repassword = confirmPwdEdit.getText().toString();
					vercode = verCodeEdit.getText().toString();
					phoneNum = phoneEdit.getText().toString();
					repassword = confirmPwdEdit.getText().toString();
					//输入信息验证
					if (!isPwdsame(password, repassword)) {
						Toasty.error(SigninWithPhoneActivity.this, "两次密码不一致").show();
					} else if (password.isEmpty() | phoneNum.isEmpty() | repassword.isEmpty() | vercode.isEmpty()) {
						Toasty.error(SigninWithPhoneActivity.this, "手机号、密码、确认密码、验证码为必填项").show();
					} else {
						//验证短信验证码是否正确
						SMSSDK.submitVerificationCode("86", phoneNum, vercode);
					}

				break;
			}
			default:
				break;
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

	//短信验证错误结果分析
	private void vercodeResult(int vercodeResult) {
		if (vercodeResult == 462) {
			Toasty.error(SigninWithPhoneActivity.this, "验证码发送太频繁，请一分钟后再试").show();
		} else if (vercodeResult == 463) {
			Toasty.error(SigninWithPhoneActivity.this, "这个号码今天收到了太多验证码了，让它歇一歇吧，明天再试").show();
		} else if (vercodeResult == 464) {
			Toasty.error(SigninWithPhoneActivity.this, "这个手机今天收到了太多验证码了，让它歇一歇吧，明天再试").show();
		} else if (vercodeResult == 467) {
			Toasty.error(SigninWithPhoneActivity.this, "5分钟内校验错误超过3次，验证码失效，请重新获取验证码").show();
		} else if (vercodeResult == 468) {
			Toasty.error(SigninWithPhoneActivity.this, "验证码错误").show();
		} else if (vercodeResult == 477) {
			Toasty.error(SigninWithPhoneActivity.this, "每天仅能请求十次验证码，明天再来吧").show();
		} else {
			Toasty.error(SigninWithPhoneActivity.this, "验证码系统错误，请稍候再试").show();
			handler.sendEmptyMessage(-8);
		}
	}

	//注册请求
	private void SigninRequest(final String userPhone, final String userPwd) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Logger.d("password：signin" + MD5Util.md5encrypt(userPwd));
					//获取服务器返回数据
					response = HttpUtil.SigninRequest(ConstantClass.ADDRESS, ConstantClass.SIGNIN_COM, userPhone, MD5Util.md5encrypt(userPwd));
					//解析和处理服务器返回的数据
					signinResult = Utility.handleSigninRespose(response);

					handler.sendEmptyMessage(-7);
				} catch (IOException e) {
					Logger.d("连接失败，IO error");
					e.printStackTrace();
				} catch (JSONException e) {
					Logger.d("连接失败，JSON error");
					e.printStackTrace();
				}
			}
		}).start();
	}

}