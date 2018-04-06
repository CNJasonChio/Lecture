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
import com.jasonchio.lecture.util.Utility;
import com.mob.MobSDK;
import com.orhanobut.logger.Logger;
import org.json.JSONException;
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
	int countdown = 30;               //获取验证码倒计时30s

	boolean pwdcansee = true;        //密码是否可见状态
	boolean repwdcansee = true;      //重复密码是否可见状态

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
				} else if(msg.what==-7){
					Logger.d("短信验证码验证成功，正在向服务器发送注册请求");
					if (findPwdResult == 0) {
						// 验证成功后跳转登录界面
						Intent intent = new Intent(ForgetPwdActivity.this, LoginActivity.class);
						startActivity(intent);
						// 成功跳转之后销毁当前页面
						finish();
						Toasty.success(ForgetPwdActivity.this, "修改密码成功").show();
					} else if (findPwdResult == 1) {
						Toasty.error(ForgetPwdActivity.this, "该用户不存在").show();
					} else {
						Toasty.error(ForgetPwdActivity.this, "服务器出错，请稍候再试").show();
					}

				}else {
					int event = msg.arg1;
					int result = msg.arg2;
					Object data = msg.obj;
					if (result == SMSSDK.RESULT_COMPLETE) {
						// 短信找回密码成功后，返回LoginActivity,然后提示
						if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {// 提交验证码成功
							//验证码验证通过后，向服务器请求更改密码
							FindPwdRequest();

						} else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
							Toasty.success(ForgetPwdActivity.this, "验证码发送成功").show();
						} else {
							Toasty.info(ForgetPwdActivity.this, "出现了未知事件，请联系开发者并说明情况").show();
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
				if (!Utility.judgePhoneNums(phone)) {// 判断输入号码是否正确
					Toasty.error(ForgetPwdActivity.this, "手机号码不正确");
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
					Toasty.error(ForgetPwdActivity.this, "两次密码不一致");
					return;
				} else if (password.isEmpty() | phone.isEmpty() | repassword.isEmpty() | vercode.isEmpty()) {
					Toasty.error(ForgetPwdActivity.this, "手机号、密码、确认密码、验证码为必填项").show();
				} else {
					//验证手机验证码是否正确
					SMSSDK.submitVerificationCode("86", phone, vercode);
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

	private void vercodeResult(int vercodeResult) {
		if (vercodeResult == 462) {
			Toasty.error(ForgetPwdActivity.this, "验证码发送太频繁，请一分钟后再试").show();
		} else if (vercodeResult == 463) {
			Toasty.error(ForgetPwdActivity.this, "这个号码今天收到了太多验证码了，让它歇一歇吧，明天再试").show();
		} else if (vercodeResult == 464) {
			Toasty.error(ForgetPwdActivity.this, "这个手机今天收到了太多验证码了，让它歇一歇吧，明天再试").show();
		} else if (vercodeResult == 467) {
			Toasty.error(ForgetPwdActivity.this, "5分钟内校验错误超过3次，验证码失效").show();
		} else if (vercodeResult == 468) {
			Toasty.error(ForgetPwdActivity.this, "验证码错误").show();
		} else {
			Toasty.error(ForgetPwdActivity.this, "验证码系统错误，请稍候再试").show();
		}
	}

	private void FindPwdRequest() {
		//获取手机号与新密码
		phone = fgtpwdaccountEdit.getText().toString();
		password = newpasswordEdit.getText().toString();
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					//获取服务器返回数据
					response = HttpUtil.FindPwdRequest(ConstantClass.ADDRESS, ConstantClass.FINDPWD_PORT, phone, password);
					//解析和处理服务器返回的数据
					findPwdResult = Utility.handleFindPwdRespose(response);
					handler.sendEmptyMessage(-7);
				} catch (IOException e) {
					Logger.e(e, "IOException");
				} catch (JSONException e) {
					Logger.e(e, "JSONEException");
				}
			}
		}).start();
	}
}
