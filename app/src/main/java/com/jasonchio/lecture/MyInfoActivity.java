package com.jasonchio.lecture;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
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

import com.google.gson.Gson;
import com.jasonchio.lecture.gson.UserTest;
import com.jasonchio.lecture.util.CircleImageView;
import com.jasonchio.lecture.util.HttpUtil;

import org.json.JSONException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;

import static android.content.ContentValues.TAG;

public class MyInfoActivity extends BaseActivity {

	String[] sexArray = new String[]{"蓝孩纸", "吕孩纸", "不告诉他们"};

	RelativeLayout photoLayout;

	RelativeLayout nameLayout;

	RelativeLayout sexLayout;

	RelativeLayout schoolLayout;

	RelativeLayout birthdayLayout;

	LinearLayout ll_popup;

	CircleImageView photoImage;

	TextView nameText;

	TextView sexText;

	TextView schoolText;

	TextView birthdayText;

	TitleLayout titleLayout;

	Button titleFirstButton;

	Uri finalUri;

	String userInfoResult;

	PopupWindow pop;

	private View popupWindowView;

	public static final int TAKE_PHOTO = 1;
	public static final int OPEN_ALBUM = 2;
	public static final int PHOTO_ALREADY = 3;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_info);

		//初始化控件
		initWidget();
		//初始化视图
		initView();

		photoLayout.setOnClickListener(this);
		nameLayout.setOnClickListener(this);
		sexLayout.setOnClickListener(this);
		schoolLayout.setOnClickListener(this);
		birthdayLayout.setOnClickListener(this);
		titleFirstButton.setOnClickListener(this);

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

		photoImage = (CircleImageView) findViewById(R.id.myinfo_photo_image);
		nameText = (TextView) findViewById(R.id.myinfo_name_text);
		sexText = (TextView) findViewById(R.id.myinfo_sex_text);
		schoolText = (TextView) findViewById(R.id.myinfo_school_text);
		birthdayText = (TextView) findViewById(R.id.myinfo_birthday_text);
		titleFirstButton = titleLayout.getFirstButton();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.title_first_button: {
				finish();
				break;
			}
			case R.id.myinfo_profilephoto_layout: {
				showPopupWindow();
				//getUserInfo(1);
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
		}
	};

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

	private void showSexChooseDialog() {
		AlertDialog.Builder builder3 = new AlertDialog.Builder(this);// 自定义对话框
		builder3.setSingleChoiceItems(sexArray, 0, new DialogInterface.OnClickListener() {// 2默认的选中

			@Override
			public void onClick(DialogInterface dialog, int which) {// which是被选中的位置
				// showToast(which+"");
				sexText.setText(sexArray[which]);
				dialog.dismiss();// 随便点击一个item消失对话框，不用点击确认取消
			}
		});
		builder3.show();// 让弹出框显示
	}

	/****
	 * 头像提示框
	 */
	public void showPopupWindow() {
		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		popupWindowView = inflater.inflate(R.layout.choose_photo_popupwindow, null);
		pop = new PopupWindow(popupWindowView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);

		ll_popup = (LinearLayout) popupWindowView.findViewById(R.id.ll_popup);
		pop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
		pop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
		pop.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
		pop.setFocusable(true);
		pop.setOutsideTouchable(true);
		pop.setContentView(popupWindowView);
		RelativeLayout parent = (RelativeLayout) popupWindowView.findViewById(R.id.parent);
		Button chooseAlbum = (Button) popupWindowView.findViewById(R.id.item_popupwindows_Photo);
		Button chooseCamrea = (Button) popupWindowView.findViewById(R.id.item_popupwindows_camera);
		Button cancel = (Button) popupWindowView.findViewById(R.id.item_popupwindows_cancel);
		pop.showAtLocation(popupWindowView, Gravity.CENTER, 0, 0);

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
						Toast.makeText(MyInfoActivity.this, "cut for photo intent is null", Toast.LENGTH_SHORT).show();
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
						Toast.makeText(MyInfoActivity.this, "cut for camrea intent is null", Toast.LENGTH_SHORT).show();
					}

					break;
				case PHOTO_ALREADY:
					try {
						//获取裁剪后的图片，并显示出来
						Bitmap bitmap = BitmapFactory.decodeStream(
								getContentResolver().openInputStream(finalUri));
						photoImage.setImageBitmap(bitmap);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
					break;
				default:
			}
		}
	}



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


//	private void getUserInfo(final int userID){
//		Log.d("UserTest","点击开始通讯");
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//				try {
//					Log.d("UserTest","尝试与服务器连接");
//					userInfoResult=HttpUtil.UserInfoRequest(ADDRESS,PORT,userID);
//					Log.d("UserTest","通讯结束");
//				} catch (IOException e) {
//					Log.d("UserTest","连接失败，IO");
//					e.printStackTrace();
//				} catch (JSONException e) {
//					Log.d("UserTest","连接失败JSON");
//					e.printStackTrace();
//				}
//				runOnUiThread(new Runnable() {
//					@Override
//					public void run() {
//						Log.d("UserTest","在主线程中更改UI");
//						Gson gson=new Gson();
//						UserTest userTest =gson.fromJson(userInfoResult, UserTest.class);
//						Log.d("UserTest",userInfoResult);
//						Log.d("UserTest",userTest.getUser_name());
//						Log.d("UserTest",userTest.getUser_password());
//
//					}
//				});
//			}
//		}).start();
//
//	}
}