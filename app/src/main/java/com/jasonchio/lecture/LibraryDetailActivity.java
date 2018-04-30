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

	Handler handler;

	int libraryRequestResult;

	int changeFocuseResult;

	DaoSession daoSession;

	LibraryDBDao mLibraryDao;

	UserDBDao mUserDao;

	Dialog libraryDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_library_detail);

		Intent intent=getIntent();
		libName=intent.getStringExtra("library_name");
		//初始化控件
		initWidget();


		//初始化视图
		initView();

		initEvent();

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
							//DialogUtils.closeDialog(libraryDialog);
							Toasty.success(LibraryDetailActivity.this, "获取图书馆详情成功").show();
							initLibrary(libName);
						} else if (libraryRequestResult == 1) {
							DialogUtils.closeDialog(libraryDialog);
							Toasty.error(LibraryDetailActivity.this, "暂无图书馆信息").show();
						} else if(libraryRequestResult==5){
							DialogUtils.closeDialog(libraryDialog);
							Toasty.error(LibraryDetailActivity.this, "获取成功jin").show();
						}else if(libraryRequestResult==4){
							initLibrary(libName);
						}else{
							DialogUtils.closeDialog(libraryDialog);
							Toasty.error(LibraryDetailActivity.this, "服务器出错，请稍候再试").show();
						}
						break;
				}
				return true;
			}
		});
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
					//response = HttpUtil.AddLibraryFocusedRequest(ConstantClass.ADDRESS, ConstantClass.ADD_CANCEL_FOCUSE_REQUEST_PORT,ConstantClass.userOnline,libName,isFocuse);
					response = HttpUtil.AddLibraryFocusedRequest(ConstantClass.ADDRESS, ConstantClass.ADD_CANCEL_FOCUSE_REQUEST_COM,ConstantClass.userOnline,libName,isFocuse);

					Logger.json(response);
					//解析和处理服务器返回的数据
					changeFocuseResult = Utility.handleFocuseChangeResponse(response,libName,mUserDao,mLibraryDao,isFocuse);
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

		libraryDialog= DialogUtils.createLoadingDialog(LibraryDetailActivity.this,"正在加载");

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					//获取服务器返回数据
					//response = HttpUtil.LibraryRequest(ConstantClass.ADDRESS, ConstantClass.LIBRARY_REQUEST_PORT,libName);
					response = HttpUtil.LibraryRequest(ConstantClass.ADDRESS, ConstantClass.LIBRARY_REQUEST_COM, ConstantClass.userOnline,libName);
					Logger.json(response);
					//解析和处理服务器返回的数据
					libraryRequestResult = Utility.handleLibraryResponse(response,mLibraryDao);
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

		LibraryDB library=mLibraryDao.queryBuilder().where(LibraryDBDao.Properties.LibraryName.eq(libName)).build().unique();
		if(library!=null){
			//libraryImage=(ImageView)findViewById(R.id.library_photo_image);
			libraryName.setText(library.getLibraryName());
			if(library.getLibraryContent()==null){
				libraryContent.setText("暂无介绍");
			}else{
				libraryContent.setText(library.getLibraryContent());
			}

			original=library.getLibraryUrl();
			isFocuse=library.getIsFocused();
			if(library.getLibraryImageUrl()==null){
				libraryImage.setImageResource(R.drawable.ic_nopicture);
			}else{
				Glide.with(LibraryDetailActivity.this).load(library.getLibraryImageUrl()).into(libraryImage);
			}
		}else{
			LibraryRequest();
		}

	}

}
