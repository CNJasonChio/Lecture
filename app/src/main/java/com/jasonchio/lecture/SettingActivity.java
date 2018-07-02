package com.jasonchio.lecture;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
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
	String response = null;

	Handler handler;

	Dialog checkUpdateDialog;

	Rationale mRationale;               //申请权限多次被拒绝后提示对象

	String updateFileUrl;

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
			cacheSizeText.setText("(" + CleanCache.getTotalCacheSize(SettingActivity.this) + ")");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	void initWidget() {
		titleLayout = (TitleLayout) findViewById(R.id.setting_title_layout);
		titleFirstButton = titleLayout.getFirstButton();
		signoutButton = (Button) findViewById(R.id.setting_signout_button);
		cleanCacheLayout = (RelativeLayout) findViewById(R.id.setting_delete_cache_layout);
		helpLayout = (RelativeLayout) findViewById(R.id.setting_help_layout);
		updateLogLayout = (RelativeLayout) findViewById(R.id.setting_updatelog_layout);
		checkUpdateLayout = (RelativeLayout) findViewById(R.id.setting_update_layout);
		recommentLayout = (RelativeLayout) findViewById(R.id.setting_share_layout);
		contactDeveloperLayout = (RelativeLayout) findViewById(R.id.setting_contact_layout);
		aboutLayout = (RelativeLayout) findViewById(R.id.setting_about_layout);
		cacheSizeText = (TextView) findViewById(R.id.setting_cache_size);
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

		handler = new Handler(new Handler.Callback() {
			@Override
			public boolean handleMessage(Message msg) {
				switch (msg.what) {
					case 1:
						if (checkUpdateResult == 0) {
							Gson gson = new Gson();
							final CheckUpdateResult result = gson.fromJson(response, CheckUpdateResult.class);

							updateFileUrl = result.getUpdateurl();
							DialogUtils.closeDialog(checkUpdateDialog);
							AlertDialog.Builder mDialog = new AlertDialog.Builder(SettingActivity.this);
							mDialog.setTitle("版本更新:" + result.getVersion());
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
									if (ContextCompat.checkSelfPermission(SettingActivity.this, Manifest.permission_group.STORAGE) != PackageManager.PERMISSION_GRANTED) {
										askforPermisson();
									} else {
										Logger.d("开始下载");
										goToDownloadApk(updateFileUrl);
									}
								}
							}).create().show();
						} else if (checkUpdateResult == 1) {
							DialogUtils.closeDialog(checkUpdateDialog);
							AlertDialog.Builder mDialog = new AlertDialog.Builder(SettingActivity.this);
							mDialog.setTitle("版本更新");
							mDialog.setMessage("当前为最新版本");
							mDialog.setNegativeButton("确定", new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog, int which) {
									dialog.dismiss();
								}
							}).create().show();
						} else {
							DialogUtils.closeDialog(checkUpdateDialog);
						}
						break;
					case 2:
						if (checkUpdateResult == 0) {
							Gson gson = new Gson();
							final CheckUpdateResult result = gson.fromJson(response, CheckUpdateResult.class);
							updateFileUrl = result.getUpdateurl();
							if(updateFileUrl!=null){
								shareToOthers();
							}else{
								Toasty.error(SettingActivity.this, "获取《聚讲座》推荐链接失败，请稍候再试").show();
							}
						} else {
							Toasty.error(SettingActivity.this, "获取《聚讲座》推荐链接失败，请稍候再试").show();
						}
					default:

				}
				return true;
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.title_first_button: {
				finish();
				break;
			}
			case R.id.setting_signout_button:
				Intent intent = new Intent("com.jasonchio.lecture.SIGNOUT");
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
				Intent intent1 = new Intent(SettingActivity.this, HelpActivity.class);
				startActivity(intent1);
				break;
			case R.id.setting_update_layout:
				checkUpdateDialog = DialogUtils.createLoadingDialog(SettingActivity.this, "正在检查更新");
				checkUpdate();
				break;
			case R.id.setting_updatelog_layout:
				Intent intent2 = new Intent(SettingActivity.this, UpdateLogActivity.class);
				startActivity(intent2);
				break;
			case R.id.setting_share_layout:
				Logger.d("分享给朋友");
				getAPKdownloadUrl();
				break;
			case R.id.setting_contact_layout:
				if (!joinQQGroup("VIztUFDYMzISbuGFM0JegDHM3xdWmIHS")) {
					Toasty.error(SettingActivity.this, "未安装手机 QQ 或者版本不支持").show();
				}
				break;
			case R.id.setting_about_layout:
				Intent intent3 = new Intent(SettingActivity.this, AboutActivity.class);
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

	public void checkUpdate() {
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
						handler.sendEmptyMessage(1);
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
		Intent intent = new Intent(SettingActivity.this, DownloadApkService.class);
		intent.putExtra("apkUrl", downloadUrl);
		Logger.d("启动下载 service");
		startService(intent);
	}

	private void shareToOthers() {

		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_SUBJECT, "Share");
		intent.putExtra(Intent.EXTRA_TEXT, "给你推荐一款敲极好用的讲座信息收集平台，它就是《聚讲座》！！！\n" + updateFileUrl+"\n点击链接下载安装，马上体验一下吧！！！");
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(Intent.createChooser(intent, getTitle()));
	}

	//申请权限
	private void askforPermisson() {

		AndPermission.with(this).permission(Permission.Group.STORAGE).rationale(mRationale).onGranted(new Action() {
			@Override
			public void onAction(List <String> permissions) {
				Logger.d("开始下载");
				goToDownloadApk(updateFileUrl);
			}
		}).onDenied(new Action() {

			@Override
			public void onAction(List <String> permissions) {
				Toasty.error(SettingActivity.this, "获取权限失败");
				if (AndPermission.hasAlwaysDeniedPermission(SettingActivity.this, permissions)) {
					// 这里使用一个Dialog展示没有这些权限应用程序无法继续运行，询问用户是否去设置中授权。
					final SettingService settingService = AndPermission.permissionSetting(SettingActivity.this);
					android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(SettingActivity.this);
					builder.setTitle("权限请求");
					builder.setMessage("小的需要您的相机权限和存储权限来为您更改头像，不然小主可能换不了自己的好看的头像了");
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
				android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(SettingActivity.this);
				builder.setTitle("权限请求");
				builder.setMessage("小的需要您的相机权限和存储权限来为您更改头像，不然小主可能换不了自己的好看的头像了");
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

	private void getAPKdownloadUrl() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					response = HttpUtil.UpdateRequest(ConstantClass.ADDRESS, ConstantClass.UPDATE_COM, Integer.toString(0));
					checkUpdateResult = Utility.handleUpdateResponse(response);
					handler.sendEmptyMessage(2);
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				if (response != null) {
					Logger.json(response);
				}
			}

		}).start();
	}
}
