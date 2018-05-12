package com.jasonchio.lecture;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jasonchio.lecture.greendao.DaoSession;
import com.jasonchio.lecture.greendao.UserDB;
import com.jasonchio.lecture.greendao.UserDBDao;
import com.jasonchio.lecture.util.CircleImageView;
import com.jasonchio.lecture.util.DialogUtils;
import com.jasonchio.lecture.util.HttpUtil;
import com.jasonchio.lecture.util.ConstantClass;
import com.jasonchio.lecture.util.Utility;
import com.orhanobut.logger.Logger;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RequestExecutor;
import com.yanzhenjie.permission.SettingService;

import org.json.JSONException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import es.dmoral.toasty.Toasty;

import static android.content.ContentValues.TAG;

public class MyInfoActivity extends BaseActivity {

	String[] sexArray = new String[]{"蓝孩纸", "吕孩纸", "不告诉他们"};

	RelativeLayout photoLayout;         //用户头像布局

	RelativeLayout nameLayout;          //用户昵称布局

	RelativeLayout sexLayout;           //用户性别布局

	RelativeLayout schoolLayout;        //用户学校布局

	RelativeLayout birthdayLayout;      //用户生日布局

	RelativeLayout phoneLayout;         //用户手机号布局

	LinearLayout ll_popup;              //更改用户头像选择布局

	CircleImageView photoImage;         //用户头像布局

	TextView nameText;                  //用户昵称

	String userName=null;               //用户昵称

	TextView sexText;                   //用户性别

	String userSex=null;                //用户性别

	TextView schoolText;                //用户学校

	String userSchool=null;             //用户学校

	TextView birthdayText;              //用户生日

	String userBirthday=null;           //用户生日

	TextView phoneText;                 //用户手机号

	String userPhone;                   //用户手机号

	TitleLayout titleLayout;            //标题栏

	Button titleFirstButton;            //标题栏第一个按钮

	Uri finalUri;                       //裁剪后的头像 uri

	PopupWindow pop;                    //选择窗口

	View popupWindowView;               //选择窗口视图

	public static final int TAKE_PHOTO = 1;     //拍照
	public static final int OPEN_ALBUM = 2;     //打开相册
	public static final int PHOTO_ALREADY = 3;  //裁剪完成

	int myinfoRequestResult;            //用户信息请求结果

	int changeInfoResult;               //更改用户信息结果

	int changeUserHeadResult;           //更改用户头像结果

	DaoSession daoSession;              //数据库操作对象

	UserDBDao mUserDao;                 //用户表操作对象

	Handler handler;                    //handler对象

	Dialog myinfoLoadDialog;            //加载对话框

	Rationale mRationale;               //申请权限多次被拒绝后提示对象

	Bitmap newHead;                     //新的用户头像

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_info);

		//初始化控件
		initWidget();
		//初始化视图
		initView();
		//初始化事件响应
		initEvent();
		//显示用户信息
		showMyinfo();
	}

	@Override
	void initView() {
		//BaseActivity方法，隐藏系统标题栏
		HideSysTitle();

		titleLayout.setSecondButtonVisible(View.GONE);
		titleLayout.setTitle("我的资料");
	}

	@Override
	void initWidget() {
		titleLayout = (TitleLayout) findViewById(R.id.myinfo_title_layout);
		photoLayout = (RelativeLayout) findViewById(R.id.myinfo_profilephoto_layout);
		nameLayout = (RelativeLayout) findViewById(R.id.myinfo_name_layout);
		sexLayout = (RelativeLayout) findViewById(R.id.myinfo_sex_layout);
		schoolLayout = (RelativeLayout) findViewById(R.id.myinfo_school_layout);
		birthdayLayout = (RelativeLayout) findViewById(R.id.myinfo_birthday_layout);
		phoneLayout=(RelativeLayout)findViewById(R.id.myinfo_phone_layout);

		photoImage = (CircleImageView) findViewById(R.id.myinfo_photo_image);
		nameText = (TextView) findViewById(R.id.myinfo_name_text);
		sexText = (TextView) findViewById(R.id.myinfo_sex_text);
		schoolText = (TextView) findViewById(R.id.myinfo_school_text);
		birthdayText = (TextView) findViewById(R.id.myinfo_birthday_text);
		phoneText=(TextView)findViewById(R.id.myinfo_phone_text);

		titleFirstButton = titleLayout.getFirstButton();

		daoSession=((MyApplication)getApplication()).getDaoSession();
		mUserDao=daoSession.getUserDBDao();
	}

	@Override
	void initEvent() {
		photoLayout.setOnClickListener(this);
		nameLayout.setOnClickListener(this);
		sexLayout.setOnClickListener(this);
		schoolLayout.setOnClickListener(this);
		birthdayLayout.setOnClickListener(this);
		titleFirstButton.setOnClickListener(this);

		handler=new Handler(new Handler.Callback() {
			@Override
			public boolean handleMessage(Message msg) {
				switch (msg.what){
					case 1:
						if ( myinfoRequestResult == 0) {
							DialogUtils.closeDialog(myinfoLoadDialog);
							showMyinfo();
						} else {
							DialogUtils.closeDialog(myinfoLoadDialog);
							Toasty.error(MyInfoActivity.this, "获取用户信息失败，请稍候再试").show();
						}
						break;
					case 2:
						if(changeInfoResult==0){
							DialogUtils.closeDialog(myinfoLoadDialog);
							Toasty.success(MyInfoActivity.this,"修改成功").show();
						}else {
							DialogUtils.closeDialog(myinfoLoadDialog);
							Toasty.error(MyInfoActivity.this, "修改失败，请稍候再试").show();
						}
						break;
					case 3:
						if(changeUserHeadResult==0){
							DialogUtils.closeDialog(myinfoLoadDialog);
							if(newHead!=null){
								photoImage.setImageBitmap(newHead);
								Toasty.success(MyInfoActivity.this,"修改成功").show();
							}else{
								Toasty.error(MyInfoActivity.this,"获取新头像失败，修改失败").show();
							}
						}else{
							DialogUtils.closeDialog(myinfoLoadDialog);
							Toasty.error(MyInfoActivity.this, "修改失败，请稍候再试").show();
						}
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
			case R.id.myinfo_profilephoto_layout: {
				if(ContextCompat.checkSelfPermission(MyInfoActivity.this, Manifest.permission_group.CAMERA)!= PackageManager.PERMISSION_GRANTED){
					askforPermisson();
				}else{
					if(ContextCompat.checkSelfPermission(MyInfoActivity.this, Manifest.permission_group.STORAGE)!= PackageManager.PERMISSION_GRANTED){
						askforPermisson();
					}else{
						showPopupWindow();
					}
				}
				break;
			}
			case R.id.myinfo_name_layout: {
				onCreateNameDialog();
				break;
			}
			case R.id.myinfo_sex_layout: {
				showSexChooseDialog();
				break;
			}
			case R.id.myinfo_school_layout: {
				onSetSchool();
				break;
			}
			case R.id.myinfo_birthday_layout: {
				Calendar nowdate = Calendar.getInstance();
				final int mYear = nowdate.get(Calendar.YEAR);
				final int mMonth = nowdate.get(Calendar.MONTH);
				final int mDay = nowdate.get(Calendar.DAY_OF_MONTH);
				//调用DatePickerDialog
				new DatePickerDialog(MyInfoActivity.this, onDateSetListener, mYear, mMonth, mDay).show();
				break;
			}
			case R.id.myinfo_phone_layout:{
				onSetPhone();
				break;
			}
			default:
		}
	}

	/**
	 * 日期选择器对话框监听
	 */
	private DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			int mYear = year;
			int mMonth = monthOfYear;
			int mDay = dayOfMonth;
			String days;
			days = new StringBuffer().append(mYear).append("年").append(mMonth).append("月").append(mDay).append("日").toString();
			birthdayText.setText(days);
			myinfoLoadDialog=DialogUtils.createLoadingDialog(MyInfoActivity.this,"很快的");
			ChangeMyinfoRequest();
		}
	};

	//设置学校
	private void onSetSchool() {
		// 使用LayoutInflater来加载dialog_setname.xml布局
		LayoutInflater layoutInflater = LayoutInflater.from(this);
		View nameView = layoutInflater.inflate(R.layout.myinfo_dialog_setname, null);
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

		// 使用setView()方法将布局显示到dialog
		alertDialogBuilder.setView(nameView).setTitle("请输入您的学校").setMessage("例：武汉理工大学、华中科技大学");

		final EditText userInput = (EditText) nameView.findViewById(R.id.changename_edit);

		// 设置Dialog按钮
		alertDialogBuilder.setCancelable(false).setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				// 获取edittext的内容,显示到textview
				schoolText.setText(userInput.getText());
				myinfoLoadDialog=DialogUtils.createLoadingDialog(MyInfoActivity.this,"很快的");
				ChangeMyinfoRequest();
			}
		}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
	}

	//设置用户手机号
	private void onSetPhone() {
		// 使用LayoutInflater来加载dialog_setname.xml布局
		LayoutInflater layoutInflater = LayoutInflater.from(this);
		View nameView = layoutInflater.inflate(R.layout.myinfo_dialog_setname, null);
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

		// 使用setView()方法将布局显示到dialog
		alertDialogBuilder.setView(nameView).setTitle("请输入您的手机号").setMessage("PS：手机号作为唯一登录方式，不可重复");

		final EditText userInput = (EditText) nameView.findViewById(R.id.changename_edit);

		// 设置Dialog按钮
		alertDialogBuilder.setCancelable(false).setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				// 获取edittext的内容,显示到textview
				phoneText.setText(userInput.getText());
				myinfoLoadDialog=DialogUtils.createLoadingDialog(MyInfoActivity.this,"很快的");
				ChangeMyinfoRequest();
			}
		}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
	}

	//设置用户昵称
	private void onCreateNameDialog() {
		// 使用LayoutInflater来加载dialog_setname.xml布局
		LayoutInflater layoutInflater = LayoutInflater.from(this);
		View nameView = layoutInflater.inflate(R.layout.myinfo_dialog_setname, null);
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

		// 使用setView()方法将布局显示到dialog
		alertDialogBuilder.setView(nameView).setTitle("请输入昵称");

		final EditText userInput = (EditText) nameView.findViewById(R.id.changename_edit);

		// 设置Dialog按钮
		alertDialogBuilder.setCancelable(false).setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				// 获取edittext的内容,显示到textview
				nameText.setText(userInput.getText());
				myinfoLoadDialog=DialogUtils.createLoadingDialog(MyInfoActivity.this,"很快的");
				ChangeMyinfoRequest();
			}
		}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
	}

	//设置用户性别
	private void showSexChooseDialog() {
		AlertDialog.Builder builder3 = new AlertDialog.Builder(this);// 自定义对话框
		builder3.setSingleChoiceItems(sexArray, 0, new DialogInterface.OnClickListener() {// 2默认的选中

			@Override
			public void onClick(DialogInterface dialog, int which) {// which是被选中的位置
				// showToast(which+"");
				sexText.setText(sexArray[which]);
				dialog.dismiss();// 随便点击一个item消失对话框，不用点击确认取消
				myinfoLoadDialog=DialogUtils.createLoadingDialog(MyInfoActivity.this,"很快的");
				ChangeMyinfoRequest();
			}
		});
		builder3.show();// 让弹出框显示
	}

	/****
	 * 头像提示框
	 */
	public void showPopupWindow() {
		//初始化头像提示框
		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		popupWindowView = inflater.inflate(R.layout.choose_photo_popupwindow, null);
		pop = new PopupWindow(popupWindowView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
		ll_popup = (LinearLayout) popupWindowView.findViewById(R.id.ll_popup);

		//设置提示框属性
		pop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
		pop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
		pop.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
		pop.setFocusable(true);
		pop.setOutsideTouchable(true);
		pop.setContentView(popupWindowView);

		//初始化提示框按钮
		RelativeLayout parent = (RelativeLayout) popupWindowView.findViewById(R.id.parent);
		Button chooseAlbum = (Button) popupWindowView.findViewById(R.id.item_popupwindows_Photo);
		Button chooseCamrea = (Button) popupWindowView.findViewById(R.id.item_popupwindows_camera);
		Button cancel = (Button) popupWindowView.findViewById(R.id.item_popupwindows_cancel);
		pop.showAtLocation(popupWindowView, Gravity.CENTER, 0, 0);

		//提示框点击处理监听器
		parent.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				pop.dismiss();
				 ll_popup.clearAnimation();
			}
		});
		chooseAlbum.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				pop.dismiss();
				ll_popup.clearAnimation();
				Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(intent, OPEN_ALBUM);
			}
		});
		chooseCamrea.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				pop.dismiss();
				ll_popup.clearAnimation();
				openCamera();
			}
		});
		cancel.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK) {
			switch (requestCode) {
				case OPEN_ALBUM: //从相册图片后返回的uri
					//启动裁剪
					Intent cutphotointent = CutForPhoto(data.getData());
					if (cutphotointent != null) {
						startActivityForResult(cutphotointent, PHOTO_ALREADY);
					} else {
						Toasty.error(MyInfoActivity.this,"没有裁剪照片的应用").show();
					}
					break;
				case TAKE_PHOTO: //相机返回的 uri
					//启动裁剪
					String path = getExternalCacheDir().getPath();
					String name = "output.png";
					Intent cutcamreaintent = CutForCamera(path, name);
					if (cutcamreaintent != null) {
						startActivityForResult(cutcamreaintent, PHOTO_ALREADY);
					} else {
						Toasty.error(MyInfoActivity.this,"没有裁剪照片的应用").show();
					}

					break;
				case PHOTO_ALREADY:
					try {
						//获取裁剪后的图片，并显示出来
						Bitmap bitmap = BitmapFactory.decodeStream(
								getContentResolver().openInputStream(finalUri));

						newHead=bitmap;

						myinfoLoadDialog=DialogUtils.createLoadingDialog(MyInfoActivity.this,"正在修改");

						changeUserHead(bitmap,Utility.getBitmapSize(bitmap));
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
					break;
				default:
			}
		}
	}

	//打开相机
	private void openCamera() {
		File outputfile = new File(getExternalCacheDir(), "output.png");
		try {
			if (outputfile.exists()) {
				outputfile.delete();//删除
			}
			outputfile.createNewFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Uri imageuri;
		if (Build.VERSION.SDK_INT >= 24) {
			imageuri = FileProvider.getUriForFile(MyInfoActivity.this, "com.jasonchio.lecture.fileprovider", //可以是任意字符串
					outputfile);
		} else {
			imageuri = Uri.fromFile(outputfile);
		}
		//启动相机程序
		Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageuri);
		startActivityForResult(intent, TAKE_PHOTO);
	}

	/**
	 * 图片裁剪
	 * @param uri
	 * @return
	 */
	@NonNull
	private Intent CutForPhoto(Uri uri) {
		try {
			//直接裁剪
			Intent intent = new Intent("com.android.camera.action.CROP");
			//设置裁剪之后的图片路径文件
			File cutfile = new File(getExternalCacheDir(), "cutcamera.png"); //随便命名一个
			if (cutfile.exists()){ //如果已经存在，则先删除,这里应该是上传到服务器，然后再删除本地的，没服务器，只能这样了
				cutfile.delete();
			}
			cutfile.createNewFile();
			//初始化 uri
			Uri imageUri = uri; //返回来的 uri
			Uri outputUri = null; //真实的 uri
			Log.d(TAG, "CutForPhoto: "+cutfile);
			outputUri = Uri.fromFile(cutfile);
			finalUri = outputUri;
			Log.d(TAG, "mCameraUri: "+finalUri);
			// crop为true是设置在开启的intent中设置显示的view可以剪裁
			intent.putExtra("crop",true);
			// aspectX,aspectY 是宽高的比例，这里设置正方形
			intent.putExtra("aspectX",1);
			intent.putExtra("aspectY",1);
			//设置要裁剪的宽高
			intent.putExtra("outputX",200); //200dp
			intent.putExtra("outputY",200);
			intent.putExtra("scale",true);
			//如果图片过大，会导致oom，这里设置为false
			intent.putExtra("return-data",false);
			if (imageUri != null) {
				intent.setDataAndType(imageUri, "image/*");
			}
			if (outputUri != null) {
				intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
			}
			intent.putExtra("noFaceDetection", true);
			//压缩图片
			intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());

			return intent;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 拍照之后，启动裁剪
	 * @param camerapath 路径
	 * @param imgname img 的名字
	 * @return
	 */
	@NonNull
	private Intent CutForCamera(String camerapath,String imgname) {
		try {

			//设置裁剪之后的图片路径文件
			File cutfile = new File(getExternalCacheDir(), "cutcamera.png"); //随便命名一个
			if (cutfile.exists()){ //如果已经存在，则先删除,这里应该是上传到服务器，然后再删除本地的，没服务器，只能这样了
				cutfile.delete();
			}
			cutfile.createNewFile();
			//初始化 uri
			Uri imageUri = null; //返回来的 uri
			Uri outputUri = null; //真实的 uri
			Intent intent = new Intent("com.android.camera.action.CROP");
			//拍照留下的图片
			File camerafile = new File(camerapath,imgname);
			if (Build.VERSION.SDK_INT >= 24) {
				intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
				imageUri = FileProvider.getUriForFile(MyInfoActivity.this,
						"com.jasonchio.lecture.fileprovider",
						camerafile);
			} else {
				imageUri = Uri.fromFile(camerafile);
			}
			outputUri = Uri.fromFile(cutfile);
			//把这个 uri 提供出去，就可以解析成 bitmap了
			finalUri = outputUri;
			// crop为true是设置在开启的intent中设置显示的view可以剪裁
			intent.putExtra("crop",true);
			// aspectX,aspectY 是宽高的比例，这里设置正方形
			intent.putExtra("aspectX",1);
			intent.putExtra("aspectY",1);
			//设置要裁剪的宽高
			intent.putExtra("outputX", 200);
			intent.putExtra("outputY",200);
			intent.putExtra("scale",true);
			//如果图片过大，会导致oom，这里设置为false
			intent.putExtra("return-data",false);
			if (imageUri != null) {
				intent.setDataAndType(imageUri, "image/*");
			}
			if (outputUri != null) {
				intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
			}
			intent.putExtra("noFaceDetection", true);
			//压缩图片
			intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
			return intent;

		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	//用户信息请求
	private void MyinfoRequest(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					//获取服务器返回的数据
					String response = HttpUtil.UserInfoRequest(ConstantClass.ADDRESS, ConstantClass.MYINFO_REQUEST_COM,ConstantClass.userOnline );
					//解析和处理服务器返回的数据
					myinfoRequestResult= Utility.handleUserInfoResponse(response,mUserDao);
					//处理结果
					handler.sendEmptyMessage(1);
				} catch (IOException e) {
					Logger.d("连接失败，IO error");
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	//修改用户信息
	private void ChangeMyinfoRequest(){

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					//获取用户更新后的信息
					userName=nameText.getText().toString();
					userSex=sexText.getText().toString();
					userSchool=schoolText.getText().toString();
					userBirthday=birthdayText.getText().toString();
					userPhone=phoneText.getText().toString();

					//获取服务器返回的数据
					String response = HttpUtil.changeUserInfo(ConstantClass.ADDRESS, ConstantClass.CHANGE_MYINFO_REQUEST_COM,ConstantClass.userOnline,userName,userPhone,userSex,userSchool,userBirthday);

					//创建临时用户
					UserDB user=new UserDB();
					user.setUserName(userName);
					user.setUserPhone(userPhone);
					user.setUserSex(userSex);
					user.setUserSchool(userSchool);
					user.setUserBirthday(userBirthday);

					//解析和处理服务器返回的数据
					changeInfoResult= Utility.handleChangeInfoResponse(response,mUserDao,user);
					//处理结果
					handler.sendEmptyMessage(2);
				} catch (IOException e) {
					Logger.d("连接失败，IO error");
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	//修改用户头像
	private void changeUserHead(final Bitmap bitmap, final int size){
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					//获取服务器返回的数据
					String response = HttpUtil.changeUserHead(ConstantClass.ADDRESS, ConstantClass.CHANGE_HEAD_COM,ConstantClass.userOnline,bitmap,size);
					//解析和处理服务器返回的数据
					changeUserHeadResult= Utility.handleChangeUserHeadResponse(response,mUserDao);
					//处理结果
					handler.sendEmptyMessage(3);
				} catch (IOException e) {
					Logger.d("连接失败，IO error");
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	//显示用户资料
	private void showMyinfo(){

		UserDB user=mUserDao.queryBuilder().where(UserDBDao.Properties.UserId.eq(ConstantClass.userOnline)).build().unique();

		if(user==null){
			myinfoLoadDialog=DialogUtils.createLoadingDialog(MyInfoActivity.this,"正在加载");
			MyinfoRequest();
			return;
		}else{
			String userHead=user.getUserPhotoUrl();
			if(userHead!=null && userHead.length()!=0){
				Glide.with(MyInfoActivity.this).load(userHead).into(photoImage);
			}else{
				photoImage.setImageResource(R.drawable.ic_defult_userhead);
			}
			if(user.getUserBirthday()!=null && user.getUserBirthday().length()!=0){
				nameText.setText(user.getUserName());
			}else{
				nameText.setText(" 讲座萌新");
			}
			if(user.getUserSex()!=null && user.getUserSex().length()!=0){
				sexText.setText(user.getUserSex());
			}else{
				sexText.setText("暂未设置");
			}
			if(user.getUserSchool()!=null && user.getUserSchool().length()!=0){
				schoolText.setText(user.getUserSchool());
			}else{
				schoolText.setText("暂未设置");
			}
			if(user.getUserBirthday()!=null&& user.getUserBirthday().length()!=0){
				birthdayText.setText(user.getUserBirthday());
			}else{
				birthdayText.setText("暂未设置");
			}
			phoneText.setText(user.getUserPhone());
		}

	}

	//申请权限
	private void askforPermisson(){

		AndPermission.with(this)
				.permission(Permission.Group.CAMERA,Permission.Group.STORAGE)
				.rationale(mRationale)
				.onGranted(new Action() {
					@Override
					public void onAction(List<String> permissions) {
						showPopupWindow();
					}
				})
				.onDenied(new Action() {

					@Override
					public void onAction(List <String> permissions) {
						Toasty.error(MyInfoActivity.this,"获取权限失败");
						if (AndPermission.hasAlwaysDeniedPermission(MyInfoActivity.this, permissions)) {
							// 这里使用一个Dialog展示没有这些权限应用程序无法继续运行，询问用户是否去设置中授权。
							final SettingService settingService = AndPermission.permissionSetting(MyInfoActivity.this);
							android.support.v7.app.AlertDialog.Builder builder=new android.support.v7.app.AlertDialog.Builder(MyInfoActivity.this);
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
				})
				.start();

		mRationale = new Rationale() {
			@Override
			public void showRationale(Context context, List<String> permissions,
			                          final RequestExecutor executor) {
				// 这里使用一个Dialog询问用户是否继续授权。
				android.support.v7.app.AlertDialog.Builder builder=new android.support.v7.app.AlertDialog.Builder(MyInfoActivity.this);
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

}