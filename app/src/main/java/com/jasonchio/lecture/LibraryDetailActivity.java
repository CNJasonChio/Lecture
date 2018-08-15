package com.jasonchio.lecture;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jasonchio.lecture.greendao.DaoSession;
import com.jasonchio.lecture.greendao.LibraryDB;
import com.jasonchio.lecture.greendao.LibraryDBDao;
import com.jasonchio.lecture.greendao.UserDBDao;
import com.jasonchio.lecture.util.ConstantClass;
import com.jasonchio.lecture.util.DialogUtils;
import com.jasonchio.lecture.util.HttpUtil;
import com.jasonchio.lecture.util.Utility;
import com.orhanobut.logger.Logger;
import org.json.JSONException;
import java.io.IOException;
import es.dmoral.toasty.Toasty;
import me.codeboy.android.aligntextview.AlignTextView;

public class LibraryDetailActivity extends BaseActivity {

	ImageView libraryImage;             //讲座信息来源图片

	TextView titleLayoutTitleText;      //讲座信息来源标题文字

	TextView libraryName;               //讲座信息来源名称

	AlignTextView libraryContent;            //讲座信息来源简介

	TextView libraryOriginal;           //讲座信息来源原文

	Button titleFirstButton;            //标题栏第一个按钮

	Button titleSecondButton;           //标题栏第二个按钮

	int isFocuse=0;                     //是否已经关注该讲座信息来源

	String libName;                     //讲座信息来源名称

	String original;                    //讲座信息来源原文

	Handler handler;                    //handler 对象

	int libraryRequestResult;           //讲座信息来源请求结果

	int changeFocuseResult;             //改变关注服务器返回的结果

	DaoSession daoSession;              //数据库操作对象

	LibraryDBDao mLibraryDao;           //讲座信息来源表操作对象

	UserDBDao mUserDao;                 //用户表操作对象

	Dialog libraryDialog;               //加载对话框

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_library_detail);

		//获取上文传递的讲座信息来源名称
		Intent intent=getIntent();
		libName=intent.getStringExtra("library_name");

		//初始化控件
		initWidget();
		//初始化视图
		initView();
		//初始化事件响应
		initEvent();
		//初始化讲座信息来源
		initLibrary(libName);
	}

	@Override
	public void onClick(View v) {
			switch (v.getId()){
				case R.id.library_title_first_button:{
					finish();
					break;
				}
				case R.id.library_title_second_button:{
					if(isFocuse ==1){
						titleSecondButton.setText("关注");
						titleSecondButton.setTextColor(Color.argb(255,63,63,63));
						titleSecondButton.setBackgroundResource(R.drawable.button_shape_black);
						isFocuse =0;
						FocuseChangeRequest();
					}else {
						titleSecondButton.setText("已关注");
						titleSecondButton.setTextColor(Color.argb(255,255,157,0));
						titleSecondButton.setBackgroundResource(R.drawable.button_shape_origin);
						isFocuse =1;
						FocuseChangeRequest();
					}
					break;
				}
				case R.id.library_original_text:{
					//打开讲座信息来源原文
					if(original!=null){
						Intent intent=new Intent(Intent.ACTION_VIEW);
						intent.setData(Uri.parse(original));
						startActivity(intent);
					}
					break;
				}
				default:
			}

	}

	@Override
	void initWidget(){
		libraryImage=(ImageView)findViewById(R.id.library_photo_image);
		libraryName=(TextView)findViewById(R.id.library_name_text);
		libraryContent=(AlignTextView) findViewById(R.id.library_content_text);
		libraryOriginal=(TextView)findViewById(R.id.library_original_text);
		titleFirstButton=(Button)findViewById(R.id.library_title_first_button);
		titleSecondButton=(Button)findViewById(R.id.library_title_second_button);
		titleLayoutTitleText=(TextView)findViewById(R.id.library_titlelayout_title_text);

		daoSession=((MyApplication)getApplication()).getDaoSession();
		mLibraryDao=daoSession.getLibraryDBDao();
		mUserDao=daoSession.getUserDBDao();
	}

	@Override
	void initEvent() {
		titleFirstButton.setOnClickListener(this);

		titleSecondButton.setOnClickListener(this);

		libraryOriginal.setOnClickListener(this);

		handler = new Handler(new Handler.Callback() {
			@Override
			public boolean handleMessage(Message msg) {
				switch (msg.what) {
					case 1:
						if (libraryRequestResult == 0) {
							initLibrary(libName);
							DialogUtils.closeDialog(libraryDialog);
						} else if (libraryRequestResult == 1) {
							DialogUtils.closeDialog(libraryDialog);
							Toasty.error(LibraryDetailActivity.this, "暂无讲座来源的信息").show();
						} else{
							DialogUtils.closeDialog(libraryDialog);
							Toasty.error(LibraryDetailActivity.this, "服务器出错，请稍候再试").show();
						}
						break;
				}
				return true;
			}
		});
	}

	@Override
	void initView(){
		//隐藏标题栏
		HideSysTitle();
		titleLayoutTitleText.setText("来源详情");
	}

	//改变关注请求
	private void FocuseChangeRequest() {
		//开启新线程
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					//获取服务器返回数据
					String response = HttpUtil.AddLibraryFocusedRequest(ConstantClass.ADDRESS, ConstantClass.ADD_CANCEL_FOCUSE_REQUEST_COM,ConstantClass.userOnline,libName,isFocuse);
					//解析和处理服务器返回的数据
					changeFocuseResult = Utility.handleFocuseChangeResponse(response,libName,mUserDao,mLibraryDao,isFocuse);
				} catch (IOException e) {
					Logger.d(e);
				} catch (JSONException e) {
					Logger.d(e);
				}
			}
		}).start();
	}

	//讲座信息来源详情请求
	private void LibraryRequest() {
		//显示加载对话框
		libraryDialog= DialogUtils.createLoadingDialog(LibraryDetailActivity.this,"正在加载");

		//开启新线程
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					//获取服务器返回数据
					String response = HttpUtil.LibraryRequest(ConstantClass.ADDRESS, ConstantClass.LIBRARY_REQUEST_COM, ConstantClass.userOnline,libName);
					//解析和处理服务器返回的数据
					libraryRequestResult = Utility.handleLibraryResponse(response,mLibraryDao);
					//处理结果
					handler.sendEmptyMessageDelayed(1,500);
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

	//初始化讲座信息来源
	private void initLibrary(String libName){

		//从数据库中查找对应的讲座信息来源
		LibraryDB library=mLibraryDao.queryBuilder().where(LibraryDBDao.Properties.LibraryName.eq(libName)).build().unique();
		//如果数据库中有该来源的详情
		if(library!=null){

			libraryName.setText(library.getLibraryName());
			if(library.getLibraryContent()==null){
				libraryContent.setText("暂无介绍");
			}else{
				libraryContent.setText(library.getLibraryContent());
			}
			isFocuse=library.getIsFocused();
			if(isFocuse ==1){
				titleSecondButton.setText("已关注");
				titleSecondButton.setTextColor(Color.argb(255,255,157,0));
				titleSecondButton.setBackgroundResource(R.drawable.button_shape_origin);
			}else {
				titleSecondButton.setText("关注");
				titleSecondButton.setTextColor(Color.argb(255,16,16,16));
				titleSecondButton.setBackgroundResource(R.drawable.button_shape_black);
			}
			original=library.getLibraryUrl();
			isFocuse=library.getIsFocused();
			if(library.getLibraryImageUrl()==null){
				libraryImage.setImageResource(R.drawable.ic_nopicture);
			}else{
				Glide.with(LibraryDetailActivity.this).load(library.getLibraryImageUrl()).into(libraryImage);
			}
		}
		//如果数据库中没有该来源的详情，就向服务器发起请求
		else{
			LibraryRequest();
		}
	}
}
