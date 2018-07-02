package com.jasonchio.lecture;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jasonchio.lecture.greendao.DaoSession;
import com.jasonchio.lecture.greendao.UserDBDao;
import com.jasonchio.lecture.gson.CheckUpdateResult;
import com.jasonchio.lecture.service.DownloadApkService;
import com.jasonchio.lecture.util.DialogUtils;
import com.jasonchio.lecture.util.HttpUtil;
import com.jasonchio.lecture.util.ConstantClass;
import com.jasonchio.lecture.util.MD5Util;
import com.jasonchio.lecture.util.Utility;
import com.orhanobut.logger.Logger;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RequestExecutor;
import com.yanzhenjie.permission.SettingService;

import org.json.JSONException;
import java.io.IOException;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class LoginActivity extends BaseActivity {

	EditText passwordEdit;      //填写密码的编辑框
	EditText accountEdit;       //填写帐号的编辑框
	Button loginButton;         //登录按钮
	TextView fgtpwdText;        //忘记密码
	TextView signinText;        //新用户注册
	CheckBox remPwdBox;         //记住密码选择框
	ImageView isCanSee;         //密码是否可见
	boolean cansee = false;     //密码是否可见状态
	boolean isRemPwd=false;     //是否记住密码
	String userPhone;           //手机号
	String userPwd;             //密码
	int loginResult = -1;       //登录结果
	Handler handler;            //handler 对象
	DaoSession mDaoSession ;    //数据库操作对象
	UserDBDao mUserDao;         //用户表操作对象
	SharedPreferences preferences;      //SharedPreferences 对象
	SharedPreferences.Editor editor;    //SharedPreferences.Editor对象
	Dialog loginLoadDialog;             //加载对话框
	boolean autoLoginResult;

	int checkUpdateResult;
	String localVersion = null;
	String response = null;
	String updateFileUrl;
	Rationale mRationale;               //申请权限多次被拒绝后提示对象
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/*//初始化Logger 适配器
		FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
				.showThreadInfo(false)  // 是否显示线程信息，默认为ture
				.tag("LECTURE")   // 每个日志的全局标记。默认PRETTY_LOGGER
				.build();*/

		setContentView(R.layout.activity_login);

		Intent intent=getIntent();
		autoLoginResult=intent.getBooleanExtra("login_result",false);

		//初始化控件
		initWidget();
		//初始化视图
		initView();
		//初始化点击等事件
		initEvent();
		//检查更新
		//checkUpdate();
	}

	@Override
	void initView() {
		//BaseActivity方法，隐藏系统标题栏
		HideSysTitle();

		//BaseActivity方法，动态设置输入邮箱和密码提示语的文字大小
		ChangeHintSize(accountEdit, "请输入手机号码", 14);
		ChangeHintSize(passwordEdit, "请输入密码", 14);

		//默认密码不可见
		passwordEdit.setTransformationMethod(PasswordTransformationMethod.getInstance());
			if(isRemPwd){
				accountEdit.setText(preferences.getString("account",""));
				if(autoLoginResult==false){
					passwordEdit.setText(preferences.getString("password",""));
				}
				remPwdBox.setChecked(true);
			}
	}

	@Override
	void initWidget() {
		passwordEdit = (EditText) findViewById(R.id.login_password_edit);
		accountEdit = (EditText) findViewById(R.id.login_account_edit);
		loginButton = (Button) findViewById(R.id.login_button);
		fgtpwdText = (TextView) findViewById(R.id.login_fgtpwd_text);
		signinText = (TextView) findViewById(R.id.login_signin_text);
		/*wechatLoginImage = (ImageView) findViewById(R.id.login_wechat_login);
		qqLoginImage = (ImageView) findViewById(R.id.login_qq_login);
		sinaLoginImage = (ImageView) findViewById(R.id.login_sina_login);*/
		isCanSee = (ImageView) findViewById(R.id.login_pwd_cansee);
		remPwdBox=(CheckBox)findViewById(R.id.login_remember_pwd_checkbox);
		mDaoSession = ((MyApplication)getApplication()).getDaoSession();
		mUserDao = mDaoSession.getUserDBDao();
		preferences= PreferenceManager.getDefaultSharedPreferences(this);
		isRemPwd=preferences.getBoolean("remember_password",false);

	}

	@Override
	void initEvent() {
		//设置注册按钮监听时间
		signinText.setOnClickListener(this);
		//设置找回密码监听事件
		fgtpwdText.setOnClickListener(this);
		//设置密码是否可见按钮监听事件
		isCanSee.setOnClickListener(this);
		//设置登录按钮监听事件
		loginButton.setOnClickListener(this);

		handler=new Handler(new Handler.Callback() {
			@Override
			public boolean handleMessage(Message msg) {
				switch (msg.what){
					case 1:
						if (loginResult == 0) {
							DialogUtils.closeDialog(loginLoadDialog);
							editor=preferences.edit();
							if(remPwdBox.isChecked()){
								editor.putBoolean("remember_password",true);
								editor.putString("account", userPhone);
								editor.putString("password",userPwd);
							}else{
								editor.clear();
							}
							editor.apply();
							Intent intent = new Intent(LoginActivity.this, MainPageActivity.class);
							startActivity(intent);
							finish();
						} else if (loginResult == 1) {
							DialogUtils.closeDialog(loginLoadDialog);
							Toasty.error(LoginActivity.this, "该用户不存在，请注册").show();
						} else if(loginResult==3){
							DialogUtils.closeDialog(loginLoadDialog);
							Toasty.error(LoginActivity.this, "用户名或密码不正确").show();
						}else {
							DialogUtils.closeDialog(loginLoadDialog);
							Toasty.error(LoginActivity.this, "服务器出错，请稍候再试").show();
						}
						break;
					case 2:
						if (checkUpdateResult == 0) {
							Gson gson = new Gson();
							final CheckUpdateResult result = gson.fromJson(response, CheckUpdateResult.class);

							updateFileUrl = result.getUpdateurl();
							AlertDialog.Builder mDialog = new AlertDialog.Builder(LoginActivity.this);
							mDialog.setTitle("版本更新:" + result.getVersion());
							if(Double.valueOf(result.getVersion())-Double.valueOf(localVersion)>=0.5){
								mDialog.setTitle("版本更新:" + result.getVersion()+"\n"+"当前版本过低，请下载最新版体验");
							}
							mDialog.setMessage(result.getUpgradeinfo());
							mDialog.setNegativeButton("暂不更新", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									if(Double.valueOf(result.getVersion())-Double.valueOf(localVersion)>=0.5){
										dialog.dismiss();
										finish();
									}else{
										dialog.dismiss();
									}
								}
							}).setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									dialog.dismiss();
									if (ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission_group.STORAGE) != PackageManager.PERMISSION_GRANTED) {
										askforPermisson();
									} else {
										Logger.d("开始下载");
										goToDownloadApk(updateFileUrl);
									}
								}
							}).create().show();
						}
						break;
						default:
				}
				return true;
			}
		});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.login_signin_text: {
				Intent intent = new Intent(LoginActivity.this, SigninWithPhoneActivity.class);
				startActivity(intent);

				break;
			}
			case R.id.login_fgtpwd_text: {
				Intent intent = new Intent(LoginActivity.this, ForgetPwdActivity.class);
				startActivity(intent);
				break;
			}
			case R.id.login_pwd_cansee: {
				if (cansee == false) {
					//如果是不能看到密码的情况下，
					passwordEdit.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
					isCanSee.setImageResource(R.drawable.ic_pwd_cansee);
					cansee = true;
				} else {
					//如果是能看到密码的状态下
					passwordEdit.setTransformationMethod(PasswordTransformationMethod.getInstance());
					isCanSee.setImageResource(R.drawable.ic_pwd_cantsee);
					cansee = false;
				}
				break;
			}
			case R.id.login_button: {
				userPhone = accountEdit.getText().toString();
				userPwd = passwordEdit.getText().toString();
				if(userPhone.isEmpty() | userPwd.isEmpty()){
					Toasty.error(LoginActivity.this,"用户名和密码不能为空");
				}else{
						loginLoadDialog = DialogUtils.createLoadingDialog(LoginActivity.this,"正在登录");
						loginRequest();
				}
				break;
			}
			default:
		}
	}

	private void loginRequest() {

		//获取用户输入的手机号、密码
		userPhone = accountEdit.getText().toString();
		userPwd = passwordEdit.getText().toString();

		//开启新线程
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					//获取服务器返回的结果 
					String response = HttpUtil.LoginRequest(ConstantClass.ADDRESS, ConstantClass.LOGIN_COM, userPhone,MD5Util.md5encrypt(userPwd));
					//解析和处理服务器返回的结果
					loginResult = Utility.handleLoginRespose(response,userPhone,mUserDao);
					//处理结果
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

	private void checkUpdate() {
		try {
			PackageInfo packageInfo = getApplicationContext().getPackageManager().getPackageInfo(getPackageName(), 0);
			localVersion = packageInfo.versionName;
			Logger.d("本软件的版本号 " + localVersion);
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}

		new Thread(new Runnable() {
			@Override
			public void run() {
				if (localVersion != null) {
					try {
						response = HttpUtil.UpdateRequest(ConstantClass.ADDRESS, ConstantClass.UPDATE_COM, localVersion);
						checkUpdateResult = Utility.handleUpdateResponse(response);
						handler.sendEmptyMessage(2);
					} catch (JSONException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
					Logger.d("localVersion is null");
				}
			}
		}).start();
	}

	private void goToDownloadApk(String downloadUrl) {
		Intent intent = new Intent(LoginActivity.this, DownloadApkService.class);
		intent.putExtra("apkUrl", downloadUrl);
		Logger.d("启动下载 service");
		startService(intent);
	}

	//申请权限
	private void askforPermisson() {

		AndPermission.with(this).permission(Permission.Group.STORAGE).rationale(mRationale).onGranted(new Action() {
			@Override
			public void onAction(List<String> permissions) {
				Logger.d("开始下载");
				goToDownloadApk(updateFileUrl);
			}
		}).onDenied(new Action() {
			@Override
			public void onAction(List <String> permissions) {
				Toasty.error(LoginActivity.this, "获取权限失败");
				if (AndPermission.hasAlwaysDeniedPermission(LoginActivity.this, permissions)) {
					// 这里使用一个Dialog展示没有这些权限应用程序无法继续运行，询问用户是否去设置中授权。
					final SettingService settingService = AndPermission.permissionSetting(LoginActivity.this);
					android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(LoginActivity.this);
					builder.setTitle("权限请求");
					builder.setMessage("小的需要您的存储权限来为您更改头像，不然小主会可能体验不到最新版的功能了");
					builder.setCancelable(true);
					builder.setPositiveButton("准了", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// 如果用户同意去设置：
							settingService.execute();
						}
					});
					builder.setNegativeButton("还是不准", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// 如果用户不同意去设置：
							settingService.cancel();
						}
					});
					builder.show();
				}

			}
		}).start();

		mRationale = new Rationale() {
			@Override
			public void showRationale(Context context, List <String> permissions, final RequestExecutor executor) {
				// 这里使用一个Dialog询问用户是否继续授权。
				android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(LoginActivity.this);
				builder.setTitle("权限请求");
				builder.setMessage("小的需要您的存储权限来为您更改头像，不然小主会可能体验不到最新版的功能了");
				builder.setCancelable(true);
				builder.setPositiveButton("准了", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// 如果用户继续：
						executor.execute();
					}
				});
				builder.setNegativeButton("还是不准", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// 如果用户中断：
						executor.cancel();
					}
				});
				builder.show();
			}
		};
	}
}
