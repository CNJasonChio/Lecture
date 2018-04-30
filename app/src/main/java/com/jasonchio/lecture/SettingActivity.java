package com.jasonchio.lecture;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jasonchio.lecture.gson.CheckUpdateResult;
//import com.jasonchio.lecture.service.DownloadApkService;
import com.jasonchio.lecture.service.DownloadApkService;
import com.jasonchio.lecture.util.CleanCache;
import com.jasonchio.lecture.util.ConstantClass;
import com.jasonchio.lecture.util.DialogUtils;
import com.jasonchio.lecture.util.HttpUtil;
import com.jasonchio.lecture.util.Utility;
import com.orhanobut.logger.Logger;
import org.json.JSONException;
import java.io.IOException;
import es.dmoral.toasty.Toasty;

public class SettingActivity extends BaseActivity {

	TitleLayout titleLayout;
	Button titleFirstButton;
	Button signoutButton;
	RelativeLayout cleanCacheLayout;
	RelativeLayout helpLayout;
	RelativeLayout updateLogLayout;
	RelativeLayout checkUpdateLayout;
	RelativeLayout recommentLayout;
	RelativeLayout contactDeveloperLayout;
	RelativeLayout aboutLayout;
	TextView cacheSizeText;

	int checkUpdateResult;
	String localVersion = null;
	String response=null;

	Handler handler;

	Dialog checkUpdateDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);

		//初始化控件
		initWidget();
		//初始化视图
		initView();

		initEvent();
	}

	@Override
	void initView() {
		//BaseActivity方法，隐藏系统标题栏
		HideSysTitle();
		titleLayout.setTitle("设置");
		titleLayout.setSecondButtonVisible(View.GONE);
		try {
			cacheSizeText.setText("("+CleanCache.getTotalCacheSize(SettingActivity.this)+")");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	void initWidget() {
		titleLayout=(TitleLayout)findViewById(R.id.setting_title_layout);
		titleFirstButton=titleLayout.getFirstButton();
		signoutButton=(Button)findViewById(R.id.setting_signout_button);
		cleanCacheLayout=(RelativeLayout) findViewById(R.id.setting_delete_cache_layout);
		helpLayout=(RelativeLayout)findViewById(R.id.setting_help_layout);
		updateLogLayout=(RelativeLayout)findViewById(R.id.setting_updatelog_layout);
		checkUpdateLayout=(RelativeLayout)findViewById(R.id.setting_update_layout);
		recommentLayout=(RelativeLayout)findViewById(R.id.setting_share_layout);
		contactDeveloperLayout=(RelativeLayout)findViewById(R.id.setting_contact_layout);
		aboutLayout=(RelativeLayout)findViewById(R.id.setting_about_layout);
		cacheSizeText=(TextView)findViewById(R.id.setting_cache_size);
	}


	@Override
	void initEvent() {
		titleFirstButton.setOnClickListener(this);
		signoutButton.setOnClickListener(this);
		cleanCacheLayout.setOnClickListener(this);
		helpLayout.setOnClickListener(this);
		updateLogLayout.setOnClickListener(this);
		checkUpdateLayout.setOnClickListener(this);
		recommentLayout.setOnClickListener(this);
		contactDeveloperLayout.setOnClickListener(this);
		aboutLayout.setOnClickListener(this);

		handler=new Handler(new Handler.Callback() {
			@Override
			public boolean handleMessage(Message msg) {
				switch (msg.what){
					case 1:
						if(checkUpdateResult==0){
							Gson gson = new Gson();
							final CheckUpdateResult result = gson.fromJson(response, CheckUpdateResult.class);

							DialogUtils.closeDialog(checkUpdateDialog);
							AlertDialog.Builder mDialog= new AlertDialog.Builder(SettingActivity.this);
							mDialog.setTitle("版本更新:"+result.getVersion());
							mDialog.setMessage(result.getUpgradeinfo());
							mDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									dialog.dismiss();
								}
							}).setPositiveButton("确定", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									dialog.dismiss();
									Logger.d("开始下载");
									goToDownloadApk(result.getUpdateurl());
								}
							}).create().show();
						}else if(checkUpdateResult==1){
							DialogUtils.closeDialog(checkUpdateDialog);
							AlertDialog.Builder mDialog= new AlertDialog.Builder(SettingActivity.this);
							mDialog.setTitle("版本更新");
							mDialog.setMessage("当前为最新版本");
							mDialog.setNegativeButton("确定", new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog, int which) {
									dialog.dismiss();
								}
							}).create().show();
						}else{
							DialogUtils.closeDialog(checkUpdateDialog);
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
		switch (v.getId()){
			case R.id.title_first_button:{
				finish();
				break;
			}
			case R.id.setting_signout_button:
				Logger.d("you click signout button");
				Intent intent=new Intent("com.jasonchio.lecture.SIGNOUT");
				sendBroadcast(intent);
				break;
			case R.id.setting_delete_cache_layout:
				CleanCache.clearAllCache(SettingActivity.this);
				try {
					cacheSizeText.setText("(干净了)");
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case R.id.setting_help_layout:
				Intent intent1=new Intent(SettingActivity.this,HelpActivity.class);
				startActivity(intent1);
				break;
			case R.id.setting_update_layout:
				checkUpdateDialog= DialogUtils.createLoadingDialog(SettingActivity.this,"正在检查更新");
				checkUpdate();
				break;
			case R.id.setting_updatelog_layout:
				Intent intent2=new Intent(SettingActivity.this,UpdateLogActivity.class);
				startActivity(intent2);
				break;
			case R.id.setting_share_layout:
				break;
			case R.id.setting_contact_layout:
				if(!joinQQGroup("VIztUFDYMzISbuGFM0JegDHM3xdWmIHS")){
					Toasty.error(SettingActivity.this,"未安装手机 QQ 或者版本不支持").show();
				}
				break;
			case R.id.setting_about_layout:
				Intent intent3=new Intent(SettingActivity.this,AboutActivity.class);
				startActivity(intent3);
				break;
			default:
		}
	}

	private boolean joinQQGroup(String key) {
		Intent intent = new Intent();
		intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key));
		// 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面
		 try {
			startActivity(intent);
			return true;
		} catch (Exception e) {
			// 未安装手Q或安装的版本不支持
			return false;
		}
	}

	private void checkUpdate() {
		try {
			PackageInfo packageInfo = getApplicationContext()
					.getPackageManager()
					.getPackageInfo(getPackageName(), 0);
			localVersion = packageInfo.versionName;
			Logger.d("本软件的版本号 " + localVersion);
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}

		new Thread(new Runnable() {
			@Override
			public void run() {
				if(localVersion!=null){
					try {
						response = HttpUtil.UpdateRequest(ConstantClass.ADDRESS, ConstantClass.UPDATE_COM,localVersion);
						checkUpdateResult= Utility.handleUpdateResponse(response);
						handler.sendEmptyMessage(1);
					} catch (JSONException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					if(response!=null){
						Logger.json(response);
					}
				}else{
					Logger.d("localVersion is null");
				}
			}
		}).start();
	}

	private void goToDownloadApk(String downloadUrl) {
		Intent intent = new Intent(SettingActivity.this, DownloadApkService.class);
		intent.putExtra("apkUrl", downloadUrl);
		Logger.d("启动下载 service");
		startService(intent);
	}
}
