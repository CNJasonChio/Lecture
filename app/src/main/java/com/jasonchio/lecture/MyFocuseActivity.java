package com.jasonchio.lecture;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.jasonchio.lecture.greendao.DaoSession;
import com.jasonchio.lecture.greendao.LectureDB;
import com.jasonchio.lecture.greendao.LectureDBDao;
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
import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class MyFocuseActivity extends BaseActivity implements FocuseLibraryAdapter.InnerItemOnclickListener, AdapterView.OnItemClickListener {

	TitleLayout titleLayout;                    //标题栏

	Button titleFirstButton;                    //标题的第一个按钮

	SwipeToLoadLayout swipeToLoadLayout;        //刷新布局

	FocuseLibraryAdapter mAdapter;              //关注讲座信息来源的适配器

	List <String> librarylist = new ArrayList <>();     //讲座信息来源列表

	ListView listView;                          //要显示的 listview

	Button addCancelButton;                     //添加或取消关注按钮

	DaoSession daoSession;                      //数据库操作对象

	UserDBDao mUserDao;                         //用户表操作对象

	LibraryDBDao mLibraryDao;                   //讲座信息来源表操作对象

	int focuseLibChangeResult;                  //添加或取消关注的结果

	int myFocuseLibRequestResult;               //“我的关注”请求服务器返回结果

	int libraryRequestResult;                   //讲座信息来源详情请求结果

	Handler handler;                            // handler 对象

	Dialog myFocuseLoadDialog;                  //加载对话框

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_focuse);

		//初始化控件
		initWidget();
		//初始化视图
		initView();
		//初始化事件响应
		initEvent();
		//自动刷新
		autoRefresh();
	}

	//自动刷新
	private void autoRefresh() {
		swipeToLoadLayout.post(new Runnable() {
			@Override
			public void run() {
				swipeToLoadLayout.setRefreshing(true);
			}
		});
	}

	@Override
	void initView() {
		//BaseActivity方法，隐藏系统标题栏
		HideSysTitle();

		titleLayout.setSecondButtonVisible(View.GONE);
		titleLayout.setTitle("我的关注");
		listView.setAdapter(mAdapter);

	}

	@Override
	void initWidget() {

		titleLayout = (TitleLayout) findViewById(R.id.myfocuse_title_layout);
		swipeToLoadLayout = (SwipeToLoadLayout) findViewById(R.id.swipeToLoadLayout);
		titleFirstButton = titleLayout.getFirstButton();
		listView = (ListView) findViewById(R.id.swipe_target);
		mAdapter = new FocuseLibraryAdapter(librarylist, MyFocuseActivity.this);
		addCancelButton = (Button) findViewById(R.id.myfocuse_add_cancel_button);
		daoSession = ((MyApplication) getApplication()).getDaoSession();
		mLibraryDao = daoSession.getLibraryDBDao();
		mUserDao = daoSession.getUserDBDao();
	}

	@Override
	void initEvent() {

		mAdapter.setOnInnerItemOnClickListener(this);
		listView.setOnItemClickListener(this);
		titleFirstButton.setOnClickListener(this);

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView <?> parent, View view, int position, long id) {
				//显示对应的讲座信息来源的详情
				String libraryName = librarylist.get(position);
				Intent intent = new Intent(MyFocuseActivity.this, LibraryDetailActivity.class);
				intent.putExtra("library_name",libraryName);
				startActivity(intent);
			}
		});

		swipeToLoadLayout.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				//显示加载对话框
				myFocuseLoadDialog=DialogUtils.createLoadingDialog(MyFocuseActivity.this,"正在加载");
				//显示关注的讲座信息来源
				showFocuseLibrary();
				swipeToLoadLayout.setRefreshing(false);
			}
		});

		handler = new Handler(new Handler.Callback() {
			@Override
			public boolean handleMessage(Message msg) {
				switch (msg.what) {
					case 1:
						if (myFocuseLibRequestResult == 0) {
							showFocuseLibrary();
						} else if (myFocuseLibRequestResult == 1) {
							DialogUtils.closeDialog(myFocuseLoadDialog);
							Toasty.error(MyFocuseActivity.this, "数据库无更新").show();
						} else if (myFocuseLibRequestResult == 3) {
							DialogUtils.closeDialog(myFocuseLoadDialog);
							Toasty.info(MyFocuseActivity.this, "还没有关注的图书馆哟，先去找找自己感兴趣的图书馆吧！").show();
						} else {
							DialogUtils.closeDialog(myFocuseLoadDialog);
							Toasty.error(MyFocuseActivity.this, "服务器出错，请稍候再试").show();
						}
						break;
					case 2:
						if (libraryRequestResult == 0) {
							showFocuseLibrary();
						} else {
							DialogUtils.closeDialog(myFocuseLoadDialog);
							Toasty.error(MyFocuseActivity.this, "服务器出错，请联系开发者").show();
						}
						break;
					case 3:
						if(focuseLibChangeResult==0){
							Toasty.success(MyFocuseActivity.this,"成功取消关注").show();
						}else {
							Toasty.error(MyFocuseActivity.this,"取消关注失败").show();
							showFocuseLibrary();
						}
						break;
					default:
						break;
				}
				return true;
			}
		});
	}

	@Override
	public void onClick(View v) {
			switch (v.getId()){
				case R.id.title_first_button:
					finish();
					break;
				default:
			}
	}

	public void onItemClick(AdapterView <?> parent, View view, int position, long id) {
		String library = librarylist.get(position);
		Intent intent = new Intent(MyFocuseActivity.this, LibraryDetailActivity.class);
		intent.putExtra("library_name", library);
		startActivity(intent);
	}

	public void itemClick(View v) {

		//获得对应的讲座信息来源
		int position;
		position = (Integer) v.getTag();
		String library = librarylist.get(position);

		switch (v.getId()) {
			case R.id.myfocuse_add_cancel_button:
				Toasty.info(MyFocuseActivity.this, "已取消关注");
				//想服务器发出改变关注的请求
				FocuseChangeRequest(library);
				//从列表中移除对应讲座信息来源
				librarylist.remove(position);
				mAdapter.notifyDataSetChanged();
				break;
			default:
		}
	}

	//请求“我的关注”
	private void MyFocuseRequest() {

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					//获取服务器返回数据
					String response = HttpUtil.MyFocusedRequest(ConstantClass.ADDRESS, ConstantClass.MYFOCUSE_LIBRARY_REQUEST_COM, ConstantClass.userOnline);
					//解析和处理服务器返回的数据
					myFocuseLibRequestResult = Utility.handleFocuseLibraryResponse(response, mUserDao);
					//处理结果
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

	//关注改变请求
	private void FocuseChangeRequest(final String libName) {

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					//获取服务器返回数据
					String response = HttpUtil.AddLibraryFocusedRequest(ConstantClass.ADDRESS, ConstantClass.ADD_CANCEL_FOCUSE_REQUEST_COM, ConstantClass.userOnline, libName, 0);
					//解析和处理服务器返回的数据
					focuseLibChangeResult = Utility.handleFocuseChangeResponse(response,libName,mUserDao,mLibraryDao,0);
					handler.sendEmptyMessage(3);
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

	//显示关注的讲座信息来源
	private void showFocuseLibrary() {

		//从数据库查询用户的关注
		String temp = Utility.getUserFocuse(ConstantClass.userOnline, mUserDao);
		//如果数据库中没有用户的关注信息，就从服务器请求
		if (temp == null || temp.length() == 0) {
			MyFocuseRequest();
			return;
		}
		//将用户的关注转换成字符串数组，方便操作
		String[] focuseLibrary = Utility.getStrings(temp);

		if (focuseLibrary.length == 0) {
			Toasty.error(MyFocuseActivity.this, "解析用户关注的讲座失败，请联系开发者").show();
			return;
		} else {
			//如果此时的讲座信息来源列表不为空，则清空
			if (!librarylist.isEmpty()) {
				librarylist.clear();
			}
			//依次添加用户的关注到关注列表
			for (int i = 0; i < focuseLibrary.length; i++) {
				librarylist.add(focuseLibrary[i]);
			}
		}

		listView.setSelection(0);
		mAdapter.notifyDataSetChanged();
		DialogUtils.closeDialog(myFocuseLoadDialog);
	}
}




