package com.jasonchio.lecture;

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

import com.jasonchio.lecture.database.LibraryDB;
import com.jasonchio.lecture.util.ConstantClass;
import com.jasonchio.lecture.util.HttpUtil;
import com.jasonchio.lecture.util.Utility;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class LibraryDetailActivity extends BaseActivity {

	ImageView libraryImage;
	TextView titleLayoutTitleText;
	TextView libraryName;
	TextView libraryContent;
	TextView libraryOriginal;

	Button titleFirstButton;
	Button titleSecondButton;

	int isFocuse=0;

	String response;

	String libName;

	String original;

	List<LibraryDB> libraryDBList;

	Handler handler;

	int libraryRequestResult=-1;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_library_detail);

		Intent intent=getIntent();
		libName=intent.getStringExtra("library_id");


		//初始化控件
		initWidget();
		//初始化视图
		initView();

		initLibrary(libName);
		titleFirstButton.setOnClickListener(this);

		titleSecondButton.setOnClickListener(this);

		libraryOriginal.setOnClickListener(this);

		handler = new Handler(new Handler.Callback() {
			@Override
			public boolean handleMessage(Message msg) {
				switch (msg.what) {
					case 1:
						if (libraryRequestResult == 0) {
							Toasty.success(LibraryDetailActivity.this, "获取图书馆详情成功").show();
							initLibrary(libName);
						} else if (libraryRequestResult == 1) {
							Toasty.error(LibraryDetailActivity.this, "图书馆信息暂无").show();
						} else {
							Toasty.error(LibraryDetailActivity.this, "服务器出错，请稍候再试").show();
						}
						break;
				}
				return true;
			}
		});
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
					titleSecondButton.setText("点击关注");
					titleSecondButton.setTextColor(Color.argb(255,16,16,16));
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
				Intent intent=new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri.parse(original));
				startActivity(intent);
				break;
			}
			default:
		}
	}

	protected void initWidget(){
		libraryImage=(ImageView)findViewById(R.id.library_photo_image);
		libraryName=(TextView)findViewById(R.id.library_name_text);
		libraryContent=(TextView)findViewById(R.id.library_content_text);
		libraryOriginal=(TextView)findViewById(R.id.library_original_text);
		titleFirstButton=(Button)findViewById(R.id.library_title_first_button);
		titleSecondButton=(Button)findViewById(R.id.library_title_second_button);
		titleLayoutTitleText=(TextView)findViewById(R.id.library_titlelayout_title_text);
	}
	protected void initView(){

		HideSysTitle();
		titleLayoutTitleText.setText("图书馆详情");


		if(isFocuse ==1){
			titleSecondButton.setText("已关注");
			titleSecondButton.setTextColor(Color.argb(255,255,157,0));
			titleSecondButton.setBackgroundResource(R.drawable.button_shape_origin);

		}else {
			titleSecondButton.setText("点击关注");
			titleSecondButton.setTextColor(Color.argb(255,16,16,16));
			titleSecondButton.setBackgroundResource(R.drawable.button_shape_black);
		}
	}

	private void FocuseChangeRequest() {

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					//获取服务器返回数据
					response = HttpUtil.AddLibraryFocusedRequest(ConstantClass.ADDRESS, ConstantClass.ADD_CANCEL_FOCUSE_REQUEST_PORT,4,10,isFocuse);
					Logger.json(response);
					//解析和处理服务器返回的数据
					//signinResult = Utility.handleSigninRespose(response, SigninWithPhoneActivity.this);
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
	private void LibraryRequest() {

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					//获取服务器返回数据
					response = HttpUtil.LibraryRequest(ConstantClass.ADDRESS, ConstantClass.LIBRARY_REQUEST_PORT,libName);
					Logger.json(response);
					//解析和处理服务器返回的数据
					libraryRequestResult = Utility.handleLibraryResponse(response);
					handler.sendEmptyMessage(1);
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

	private void initLibrary(String libName){
		/*
		* 现在数据库里查找，没有就向服务器请求
		* */

		libraryDBList= DataSupport.where("libraryName=?",libName).find(LibraryDB.class);

		if(libraryDBList.size()==0){
			LibraryRequest();
		}else{
			for(LibraryDB libraryDB:libraryDBList){
				//libraryImage=(ImageView)findViewById(R.id.library_photo_image);
				libraryName.setText(libraryDB.getLibraryName());
				//libraryContent=(TextView)findViewById(R.id.library_content_text);
				original=libraryDB.getLibraryUrl();
			}
		}
	}
}
