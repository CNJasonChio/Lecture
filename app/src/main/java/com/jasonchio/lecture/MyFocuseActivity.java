package com.jasonchio.lecture;

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
import com.jasonchio.lecture.util.HttpUtil;
import com.jasonchio.lecture.util.Utility;
import com.orhanobut.logger.Logger;
import org.json.JSONException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class MyFocuseActivity extends BaseActivity implements FocuseLibraryAdapter.InnerItemOnclickListener,AdapterView.OnItemClickListener{

	TitleLayout titleLayout;

	Button titleFirstButton;

	SwipeToLoadLayout swipeToLoadLayout;

	FocuseLibraryAdapter mAdapter;

	List<LibraryDB> librarylist = new ArrayList<>();

	ListView listView;

	String response;

	int isFocuse=1;

	Button addCancelButton;

	DaoSession daoSession;

	UserDBDao mUserDao;

	LibraryDBDao mLibraryDao;

	int focuseLibChangeResult;

	int myFocuseLibRequestResult;

	int libraryRequestResult;

	Handler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_focuse);

		//初始化控件
		initWidget();
		//初始化视图
		initView();

		mAdapter.setOnInnerItemOnClickListener(this);
		listView.setOnItemClickListener(this);

		titleFirstButton.setOnClickListener(this);

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				LibraryDB library=librarylist.get(position);
				Intent intent=new Intent(MyFocuseActivity.this,LibraryDetailActivity.class);
				startActivity(intent);
			}
		});

		swipeToLoadLayout.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {

				mAdapter.notifyDataSetChanged();
				swipeToLoadLayout.setRefreshing(false);
			}
		});

		swipeToLoadLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
			@Override
			public void onLoadMore() {

				swipeToLoadLayout.setLoadingMore(false);
			}
		});

		handler=new Handler(new Handler.Callback() {
			@Override
			public boolean handleMessage(Message msg) {
				switch (msg.what){
					case 1:
						if (myFocuseLibRequestResult == 0) {
							showFocuseLibrary();
						} else if (myFocuseLibRequestResult == 1) {
							Toasty.error(MyFocuseActivity.this, "数据库无更新").show();
						} else {
							Toasty.error(MyFocuseActivity.this, "服务器出错，请稍候再试").show();
						}
						break;
					case 2:
						if(libraryRequestResult==0){
							showFocuseLibrary();
						}
						default:
							break;
				}
				return true;
			}
		});

		autoRefresh();
	}

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

		titleLayout=(TitleLayout)findViewById(R.id.myfocuse_title_layout);

		swipeToLoadLayout = (SwipeToLoadLayout) findViewById(R.id.swipeToLoadLayout);

		titleFirstButton=titleLayout.getFirstButton();

		listView = (ListView) findViewById(R.id.swipe_target);

		mAdapter = new FocuseLibraryAdapter(librarylist,MyFocuseActivity.this);

		addCancelButton=(Button)findViewById(R.id.myfocuse_add_cancel_button);

		daoSession=((MyApplication)getApplication()).getDaoSession();

		mLibraryDao=daoSession.getLibraryDBDao();

		mUserDao=daoSession.getUserDBDao();
	}

	@Override
	public void onClick(View v) {

	}

	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		LibraryDB library= librarylist.get(position);
		Intent intent=new Intent(MyFocuseActivity.this,LibraryDetailActivity.class);
		intent.putExtra("library_id",library.getLibraryID());
		startActivity(intent);
	}


	public void itemClick(View v) {
		int position;

		position = (Integer) v.getTag();

		LibraryDB library= librarylist.get(position);

		switch (v.getId()){
			case R.id.myfocuse_add_cancel_button:

/*					titleSecondButton.setText("已关注");
					titleSecondButton.setTextColor(Color.argb(255,255,157,0));
					titleSecondButton.setBackgroundResource(R.drawable.button_shape_origin);*/
					isFocuse=0;
					Toasty.info(MyFocuseActivity.this,"已取消关注");
					FocuseChangeRequest(library.getLibraryID());
					librarylist.remove(position);
					mAdapter.notifyDataSetChanged();
				break;
			default:
		}
	}
	private void MyFocuseRequest() {

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					//获取服务器返回数据
					response = HttpUtil.MyFocusedRequest(ConstantClass.ADDRESS, ConstantClass.MYFOCUSE_LIBRARY_REQUEST_PORT,ConstantClass.userOnline);
					Logger.json(response);
					//解析和处理服务器返回的数据
					myFocuseLibRequestResult = Utility.handleFocuseLibraryResponse(response,mUserDao);

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

	private void FocuseChangeRequest(final long libraryID) {

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					//获取服务器返回数据
					response = HttpUtil.AddLibraryFocusedRequest(ConstantClass.ADDRESS, ConstantClass.ADD_CANCEL_FOCUSE_REQUEST_PORT,ConstantClass.userOnline,libraryID,isFocuse);
					Logger.json(response);
					//解析和处理服务器返回的数据
					focuseLibChangeResult = Utility.handleCommonResponse(response);
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

	private void showFocuseLibrary(){

		String temp=Utility.getUserFocuse(ConstantClass.userOnline,mUserDao);

		String[] focuseLibrary = Utility.getStrings(temp);

		if(focuseLibrary.length==0){
			MyFocuseRequest();
			return;
		}else{
			for(int i=0;i<focuseLibrary.length;i++){
				LibraryDB library=mLibraryDao.queryBuilder().where(LibraryDBDao.Properties.LibraryName.eq(focuseLibrary[i])).build().unique();
				if(library==null){
					LibraryRequest(focuseLibrary[i]);
					return;
				}else{
					librarylist.add(library);
				}
			}

			listView.setSelection(0);

			mAdapter.notifyDataSetChanged();
		}
	}

	private void LibraryRequest(final String libraryName) {

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					//获取服务器返回数据
					response = HttpUtil.LibraryRequest(ConstantClass.ADDRESS, ConstantClass.LIBRARY_REQUEST_PORT,libraryName);
					Logger.json(response);
					//解析和处理服务器返回的数据
					libraryRequestResult = Utility.handleLibraryResponse(response,mLibraryDao);

					handler.sendEmptyMessage(2);
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
}




