package com.jasonchio.lecture;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import com.jasonchio.lecture.greendao.DaoSession;
import com.jasonchio.lecture.greendao.InterimLectureDB;
import com.jasonchio.lecture.greendao.InterimLectureDBDao;
import com.jasonchio.lecture.greendao.LectureDB;
import com.jasonchio.lecture.greendao.LectureDBDao;
import com.jasonchio.lecture.greendao.UserDB;
import com.jasonchio.lecture.greendao.UserDBDao;
import com.jasonchio.lecture.util.ConstantClass;
import com.jasonchio.lecture.util.DialogUtils;
import com.jasonchio.lecture.util.HttpUtil;
import com.jasonchio.lecture.util.ResultSift.DropDownMenu;
import com.jasonchio.lecture.util.ResultSift.ListDropDownAdapter;
import com.jasonchio.lecture.util.Utility;
import com.orhanobut.logger.Logger;

import org.greenrobot.greendao.query.Query;
import org.json.JSONException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class ResultSiftActivity extends BaseActivity {

	private String headers[] = {"与我相关", "地区"};// 条件选择的标题
	private List<View> popupViews = new ArrayList<View>();
	private DropDownMenu mDropDownMenu;//自定义VIew类的对象
	private TitleLayout titleLayout;
	private Button titleFirstButton;

	private ListDropDownAdapter aboutMeAdapter; //与我相关显示的适配器
	private ListDropDownAdapter areaAdapter;    //地区显示的适配器

	private String aboutUser[] = {"不限", "我的学校", "我的关注", "我的附近"};
	private String area[] = {"不限", "武昌区", "汉口区","洪山区","江岸区","江汉区","硚口区","汉阳区","青山区","东西湖区","蔡甸区","江夏区","黄陂区","新洲区","汉南区"};

	String searchStr;

	String response;

	Handler handler;

	int lectureSearchResult;

	int lectureRequestResult;

	int myFocuseLibRequestResult;
	int userPosition;

	int locationPosition;

	LectureAdapter mAdapter;

	List<LectureDB> lecturelist = new ArrayList<>();

	DaoSession daoSession;

	UserDBDao mUserDao;

	LectureDBDao mLectureDao;

	InterimLectureDBDao mInterLecDao;

	Dialog searchLoadDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result_sift);

		Intent intent=getIntent();

		searchStr= intent.getStringExtra("search_key");

		//初始化控件
		initWidget();
		//初始化视图
		initView();

		titleFirstButton.setOnClickListener(this);
		initData();//初始化 数据

		handler=new Handler(new Handler.Callback() {
			@Override
			public boolean handleMessage(Message msg) {
				switch (msg.what){
					case 1:
						if (lectureSearchResult == 0) {
							DialogUtils.closeDialog(searchLoadDialog);
							Toasty.success(ResultSiftActivity.this, "查找讲座成功").show();
							showLectureInfoToTop();
						} else if (lectureSearchResult == 1) {
							DialogUtils.closeDialog(searchLoadDialog);
							Toasty.error(ResultSiftActivity.this, "无符合条件的讲座").show();
						} else if(lectureSearchResult==3){
							LectureRequest();
						}else {
							DialogUtils.closeDialog(searchLoadDialog);
							Toasty.error(ResultSiftActivity.this, "服务器出错，请稍候再试").show();
						}
						break;
					case 2:
						if(lectureRequestResult==0){
							DialogUtils.closeDialog(searchLoadDialog);
							Utility.handleLectureSearchResponse(response,mInterLecDao,mLectureDao);
						}
					case 3:
						showSiftLecture();
						break;
					case 4:
						if (myFocuseLibRequestResult == 0) {
							showSiftLecture();
						} else if (myFocuseLibRequestResult == 1) {
							Toasty.error(ResultSiftActivity.this, "数据库无更新").show();
						} else if (myFocuseLibRequestResult == 3) {
							Toasty.info(ResultSiftActivity.this, "还没有关注的图书馆哟，先去找找自己感兴趣的图书馆吧！").show();
						} else {
							Toasty.error(ResultSiftActivity.this, "服务器出错，请稍候再试").show();
						}
						break;
					case 5:


				}
				return true;
			}
		});

		initEvent();// 初始化点击事件

		searchLoadDialog= DialogUtils.createLoadingDialog(ResultSiftActivity.this,"正在搜索");

		searchLectureRequest();

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				LectureDB lecture = lecturelist.get(position);
				Intent intent = new Intent(ResultSiftActivity.this, LectureDetailActivity.class);
				intent.putExtra("lecture_id", lecture.getLectureId());
				startActivity(intent);

			}
		});
	}

	//下面四个是点击选择条件选择是弹出的视图View，可以是ListView也可以是VIew

	ListView aboutMeView;
	ListView areaView;


	//TextView textView;//选择条件中间显示的视图，可以是View、ListView、TextView
	ListView listView;
	/**
	 * 初始化数据
	 */
	protected void initView() {

		//BaseActivity方法，隐藏系统标题栏
		HideSysTitle();

		titleLayout.setTitle("搜索："+searchStr);

		titleLayout.setSecondButtonVisible(View.GONE);

		//init about menu
		aboutMeView = new ListView(this);
		aboutMeView.setDividerHeight(0);
		aboutMeAdapter = new ListDropDownAdapter(this, Arrays.asList(aboutUser));
		aboutMeView.setAdapter(aboutMeAdapter);

		//init area menu
		areaView = new ListView(this);
		areaView.setDividerHeight(0);
		areaAdapter = new ListDropDownAdapter(this, Arrays.asList(area));
		areaView.setAdapter(areaAdapter);

	}

	@Override
	void initWidget() {

		mDropDownMenu = (DropDownMenu) findViewById(R.id.dropDownMenu);

		titleLayout=(TitleLayout)findViewById(R.id.result_sift_title_layout);
		titleFirstButton=titleLayout.getFirstButton();

		daoSession=((MyApplication)getApplication()).getDaoSession();
		mUserDao=daoSession.getUserDBDao();
		mLectureDao=daoSession.getLectureDBDao();
		mInterLecDao=daoSession.getInterimLectureDBDao();
	}


	private void initData() {
		//init popupViews
		popupViews.add(aboutMeView);
		popupViews.add(areaView);

		//init context view

		listView =new ListView(this);
		listView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		//init dropdownview
		mDropDownMenu.setDropDownMenu(Arrays.asList(headers), popupViews, listView);

		mAdapter = new LectureAdapter(ResultSiftActivity.this,listView,lecturelist,mLectureDao);

		listView.setAdapter(mAdapter);
	}

	/**
	 * 设置两个条件选择的点击事件
	 */

	void initEvent() {
		//add item click event

		aboutMeView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				aboutMeAdapter.setCheckItem(position);
				mDropDownMenu.setTabText(position == 0 ? headers[0] : aboutUser[position]);
				mDropDownMenu.closeMenu();
				userPosition=position;
			}
		});

		areaView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				areaAdapter.setCheckItem(position);
				mDropDownMenu.setTabText(position == 0 ? headers[1] : area[position]);
				mDropDownMenu.closeMenu();
				locationPosition=position;
			}
		});

		handler.sendEmptyMessage(3);
	}

	/**
	 * 监听点击回退按钮事件
	 */
	@Override
	public void onBackPressed() {
		//退出activity前关闭菜单
		if (mDropDownMenu.isShowing()) {
			mDropDownMenu.closeMenu();
		} else {
			super.onBackPressed();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.title_first_button:{
				finish();
				break;
			}
		}
	}

	private void searchLectureRequest(){

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {

					//response = HttpUtil.SearchLectureRequest(ConstantClass.ADDRESS, ConstantClass.SEARCH_LECTURE_REQUEST_PORT,searchStr);
					response = HttpUtil.SearchLectureRequest(ConstantClass.ADDRESS, ConstantClass.SEARCH_LECTURE_REQUEST_COM,searchStr);

					Logger.json(response);

					lectureSearchResult= Utility.handleLectureSearchResponse(response,mInterLecDao,mLectureDao);

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

	private void LectureRequest() {

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {

					long lastLecureID=Utility.lastLetureinDB(mLectureDao);

					//String lectureresponse = HttpUtil.LectureRequest(ConstantClass.ADDRESS, ConstantClass.LECTURE_REQUEST_PORT, lastLecureID);
					String lectureresponse = HttpUtil.LectureRequest(ConstantClass.ADDRESS, ConstantClass.LECTURE_REQUEST_COM,  ConstantClass.userOnline,lastLecureID);

					lectureRequestResult=Utility.handleLectureResponse(lectureresponse,mLectureDao);

					handler.sendEmptyMessage(2);
				} catch (IOException e) {
					Logger.d("连接失败，IO error");
					e.printStackTrace();
				} catch (JSONException e) {
					Logger.d("解析失败，JSON error");
					e.printStackTrace();
				}
			}
		}).start();
	}

	//将从数据库中查找到的讲座显示到界面中
	private void showLectureInfoToTop() {

		List<InterimLectureDB> lectureDBList=mInterLecDao.queryBuilder().offset(mAdapter.getCount()).limit(10).orderDesc(InterimLectureDBDao.Properties.LectureId).build().list();

		List<LectureDB> lectureDBS=new ArrayList<>();

		if(lectureDBList.size()<1){
			return;
		}else{
			for(InterimLectureDB interimLectureDB:lectureDBList){
				LectureDB lectureDB=new LectureDB();
				lectureDB.setLectureContent(interimLectureDB.getLectureContent());
				lectureDB.setLecutreLikers(interimLectureDB.getLecutreLikers());
				lectureDB.setLecutreSource(interimLectureDB.getLecutreSource());
				lectureDB.setLectureTime(interimLectureDB.getLectureTime());
				lectureDB.setLectureLocation(interimLectureDB.getLectureLocation());
				lectureDB.setLectureTitle(interimLectureDB.getLectureTitle());
				lectureDB.setLectureId(interimLectureDB.getLectureId());
				lectureDB.setLectureImage(interimLectureDB.getLectureImage());
				lectureDB.setLectureUrl(interimLectureDB.getLectureUrl());

				lectureDBS.add(lectureDB);
			}
		}
		lecturelist.addAll(0,lectureDBS);

		listView.setSelection(0);
		mAdapter.notifyDataSetChanged();
		}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mInterLecDao.deleteAll();
	}

	private void showSiftLecture(){

		List<InterimLectureDB> lectureDBList=new ArrayList <>();
		UserDB userDB=mUserDao.queryBuilder().where(UserDBDao.Properties.UserId.eq(ConstantClass.userOnline)).build().unique();

		switch (userPosition){
			case 0:
				lectureDBList=mInterLecDao.loadAll();
				break;
			case 1:
				if(userDB.getUserSchool()==null){
					Toasty.error(ResultSiftActivity.this,"你还没有设置你的学校呢").show();
				}else{
					lectureDBList=mInterLecDao.queryBuilder().where(InterimLectureDBDao.Properties.LecutreSource.like(userDB.getUserSchool())).build().list();
				}
				break;
			case 2:
				String []userFocuse=Utility.getStrings(Utility.getUserFocuse(ConstantClass.userOnline,mUserDao));
				if(userFocuse==null || userFocuse.length==0){
					MyFocuseRequest();
				}else{
					for(String focuse:userFocuse){
						List<InterimLectureDB> focuseLibLecture=mInterLecDao.queryBuilder().where(InterimLectureDBDao.Properties.LecutreSource.eq(focuse)).build().list();
						if(focuseLibLecture==null || focuseLibLecture.isEmpty()){
							continue;
						}else{
							lectureDBList.addAll(focuseLibLecture);
						}
					}
				}
				break;
			case 3:
				lectureDBList=mInterLecDao.loadAll();
				break;
				default:
		}

		switch (locationPosition){
			case 0:
				break;

		}

		if(lectureDBList.isEmpty()){
			Toasty.error(ResultSiftActivity.this,"无符合条件的讲座");
		}else{
			List<LectureDB> lectureDBS=new ArrayList<>();

			for(InterimLectureDB interimLectureDB:lectureDBList){
				LectureDB lectureDB=new LectureDB();
				lectureDB.setLectureContent(interimLectureDB.getLectureContent());
				lectureDB.setLecutreLikers(interimLectureDB.getLecutreLikers());
				lectureDB.setLecutreSource(interimLectureDB.getLecutreSource());
				lectureDB.setLectureTime(interimLectureDB.getLectureTime());
				lectureDB.setLectureLocation(interimLectureDB.getLectureLocation());
				lectureDB.setLectureTitle(interimLectureDB.getLectureTitle());
				lectureDB.setLectureId(interimLectureDB.getLectureId());
				lectureDB.setLectureImage(interimLectureDB.getLectureImage());
				lectureDB.setLectureUrl(interimLectureDB.getLectureUrl());
				lectureDBS.add(lectureDB);
			}
			lecturelist.clear();
			lecturelist.addAll(0,lectureDBS);
			listView.setSelection(0);
			mAdapter.notifyDataSetChanged();
		}


	}

	private void MyFocuseRequest() {

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					//获取服务器返回数据
					//response = HttpUtil.MyFocusedRequest(ConstantClass.ADDRESS, ConstantClass.MYFOCUSE_LIBRARY_REQUEST_PORT,ConstantClass.userOnline);
					response = HttpUtil.MyFocusedRequest(ConstantClass.ADDRESS, ConstantClass.MYFOCUSE_LIBRARY_REQUEST_COM, ConstantClass.userOnline);

					Logger.json(response);
					//解析和处理服务器返回的数据
					myFocuseLibRequestResult = Utility.handleFocuseLibraryResponse(response, mUserDao);

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