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

	private String headers[] = {"与我相关", "地区"};              // 条件选择的标题

	private List <View> popupViews = new ArrayList <View>();    //条件选择的视图

	private DropDownMenu mDropDownMenu;                         //自定义VIew类的对象

	private TitleLayout titleLayout;                            //标题栏

	private Button titleFirstButton;                            //标题栏的第一个按钮

	private ListDropDownAdapter aboutMeAdapter;                 //与我相关显示的适配器

	private ListDropDownAdapter areaAdapter;                    //地区显示的适配器

	private String aboutUser[] = {"不限", "我的学校", "我的关注", "我的附近"};

	private String area[] = {"不限", "武昌区", "汉口区", "洪山区", "江岸区", "江汉区", "硚口区", "汉阳区", "青山区", "东西湖区", "蔡甸区", "江夏区", "黄陂区", "新洲区", "汉南区"};

	String searchStr;                                           //搜索关键字

	String searchResponse;                                      //讲座搜索服务器返回的数据

	Handler handler;                                            // handler 对象

	int lectureSearchResult;                                    //讲座搜索结果

	int lectureRequestResult;                                   //讲座请求结果

	int myFocuseLibRequestResult;                               //“我的关注”请求结果

	int userPosition;                                           //“与我相关”选择位置

	int locationPosition;                                       //地区条件选择位置

	LectureAdapter mAdapter;                                    //讲座适配器

	List <LectureDB> lecturelist = new ArrayList <>();          //讲座列表

	DaoSession daoSession;                                      //数据库操作对象

	UserDBDao mUserDao;                                         //用户表操作对象

	LectureDBDao mLectureDao;                                   //讲座表操作对象

	InterimLectureDBDao mInterLecDao;                           //临时讲座表操作对象

	Dialog searchLoadDialog;                                    //加载对话框

	ListView aboutMeView;                                       //“与我相关”视图

	ListView areaView;                                          //地区选择视图

	ListView listView;                                          //中间显示的结果视图

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result_sift);

		Intent intent = getIntent();

		searchStr = intent.getStringExtra("search_key");

		//初始化控件
		initWidget();
		//初始化视图
		initView();
		//初始化响应事件
		initEvent();

		mInterLecDao.deleteAll();

		searchLoadDialog = DialogUtils.createLoadingDialog(ResultSiftActivity.this, "正在搜索");

		searchLectureRequest();

	}


	/**
	 * 初始化数据
	 */
	protected void initView() {

		//BaseActivity方法，隐藏系统标题栏
		HideSysTitle();

		titleLayout.setTitle("搜索：" + searchStr);

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

		//初始化数据
		initData();
	}

	@Override
	void initWidget() {
		mDropDownMenu = (DropDownMenu) findViewById(R.id.dropDownMenu);
		titleLayout = (TitleLayout) findViewById(R.id.result_sift_title_layout);
		titleFirstButton = titleLayout.getFirstButton();
		daoSession = ((MyApplication) getApplication()).getDaoSession();
		mUserDao = daoSession.getUserDBDao();
		mLectureDao = daoSession.getLectureDBDao();
		mInterLecDao = daoSession.getInterimLectureDBDao();
	}

	//初始化筛选条件
	private void initData() {
		//init popupViews
		popupViews.add(aboutMeView);
		popupViews.add(areaView);

		//init context view

		listView = new ListView(this);
		listView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		//init dropdownview
		mDropDownMenu.setDropDownMenu(Arrays.asList(headers), popupViews, listView);

		mAdapter = new LectureAdapter(ResultSiftActivity.this, lecturelist);

		listView.setAdapter(mAdapter);
	}

	/**
	 * 设置两个条件选择的点击事件
	 */

	@Override
	void initEvent() {
		//add item click event

		aboutMeView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView <?> parent, View view, int position, long id) {
				aboutMeAdapter.setCheckItem(position);
				mDropDownMenu.setTabText(position == 0 ? headers[0] : aboutUser[position]);
				mDropDownMenu.closeMenu();
				userPosition = position;
				handler.sendEmptyMessage(3);
			}

		});

		areaView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView <?> parent, View view, int position, long id) {
				areaAdapter.setCheckItem(position);
				mDropDownMenu.setTabText(position == 0 ? headers[1] : area[position]);
				mDropDownMenu.closeMenu();
				locationPosition = position;
				handler.sendEmptyMessage(3);
			}
		});

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView <?> parent, View view, int position, long id) {

				LectureDB lecture = lecturelist.get(position);
				Intent intent = new Intent(ResultSiftActivity.this, LectureDetailActivity.class);
				intent.putExtra("lecture_id", (int) lecture.getLectureId());
				startActivity(intent);

			}
		});

		titleFirstButton.setOnClickListener(this);

		handler = new Handler(new Handler.Callback() {
			@Override
			public boolean handleMessage(Message msg) {
				switch (msg.what) {
					case 1:
						if (lectureSearchResult == 0) {
							DialogUtils.closeDialog(searchLoadDialog);
							Toasty.success(ResultSiftActivity.this, "查找讲座成功").show();
							showLectureInfoToTop();
						} else if (lectureSearchResult == 1) {
							DialogUtils.closeDialog(searchLoadDialog);
							Toasty.error(ResultSiftActivity.this, "无符合条件的讲座").show();
						} else if (lectureSearchResult == 3) {
							LectureRequest();
						} else {
							DialogUtils.closeDialog(searchLoadDialog);
							Toasty.error(ResultSiftActivity.this, "服务器出错，请稍候再试").show();
						}
						break;
					case 2:
						if (lectureRequestResult == 0) {
							DialogUtils.closeDialog(searchLoadDialog);
							lectureSearchResult = Utility.handleLectureSearchResponse(searchResponse, mInterLecDao, mLectureDao);
							handler.sendEmptyMessage(1);
						}
					case 3:
						Logger.d("start showsiftLecture");
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
		switch (v.getId()) {
			case R.id.title_first_button: {
				finish();
				break;
			}
		}
	}

	//搜索讲座
	private void searchLectureRequest() {

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					//获取服务器返回的数据
					searchResponse = HttpUtil.SearchLectureRequest(ConstantClass.ADDRESS, ConstantClass.SEARCH_LECTURE_REQUEST_COM, searchStr);
					//解析和处理服务器返回的数据
					lectureSearchResult = Utility.handleLectureSearchResponse(searchResponse, mInterLecDao, mLectureDao);
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

	//请求讲座信息
	private void LectureRequest() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					//获取数据库中最后一条讲座的 id
					long lastLecureID = Utility.lastLetureinDB(mLectureDao);
					//获取服务器返回的数据
					String lectureresponse = HttpUtil.LectureRequest(ConstantClass.ADDRESS, ConstantClass.LECTURE_REQUEST_COM, ConstantClass.userOnline, lastLecureID,ConstantClass.REQUEST_FIRST);
					//解析和处理服务器返回的数据
					lectureRequestResult = Utility.handleLectureResponse(lectureresponse, mLectureDao);
					//处理结果
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

		//将保存在临时讲座表中的讲座都加载到临时讲座列表中
		List <InterimLectureDB> lectureDBList = mInterLecDao.loadAll();

		List <LectureDB> lectureDBS = new ArrayList <>();

		if (lectureDBList.size() < 1) {
			return;
		} else {
			//将临时讲座列表中的讲座复制到讲座列表中
			for (InterimLectureDB interimLectureDB : lectureDBList) {
				LectureDB lectureDB = new LectureDB();
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

		lecturelist.addAll(0, lectureDBS);
		listView.setSelection(0);
		mAdapter.notifyDataSetChanged();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		//清空临时讲座表
		mInterLecDao.deleteAll();
	}

	private void showSiftLecture() {
		//显示加载对话框
		searchLoadDialog = DialogUtils.createLoadingDialog(ResultSiftActivity.this, "正在搜索");
		List <InterimLectureDB> lectureDBList = new ArrayList <>();
		UserDB userDB = mUserDao.queryBuilder().where(UserDBDao.Properties.UserId.eq(ConstantClass.userOnline)).build().unique();

		//清空讲座列表
		lecturelist.clear();
		mAdapter.notifyDataSetChanged();

		//判断“与我相关”的条件
		switch (userPosition) {
			//全部
			case 0:
				Logger.d(userPosition);
				lectureDBList = mInterLecDao.loadAll();
				break;
			//我的学校
			case 1:
				//判断用户的学校
				if (userDB.getUserSchool() == null) {
					Toasty.error(ResultSiftActivity.this, "你还没有设置你的学校呢").show();
				} else {
					Logger.d("userPosition==1" + userDB.getUserSchool());
					//在临时讲座表中搜索来源是用户学校的讲座
					lectureDBList = mInterLecDao.queryBuilder().where(InterimLectureDBDao.Properties.LecutreSource.like("%" + userDB.getUserSchool() + "%")).build().list();
				}
				break;
			//我的关注
			case 2:
				//获取用户关注的讲座信息来源
				String[] userFocuse = Utility.getStrings(Utility.getUserFocuse(ConstantClass.userOnline, mUserDao));
				//如果本地没有用户关注的讲座，就从服务器请求
				if (userFocuse == null || userFocuse.length == 0) {
					MyFocuseRequest();
				} else {
					//在临时讲座表中搜索来源是用户关注的来源的讲座
					for (String focuse : userFocuse) {
						List <InterimLectureDB> focuseLibLecture = mInterLecDao.queryBuilder().where(InterimLectureDBDao.Properties.LecutreSource.eq(focuse)).build().list();
						if (focuseLibLecture == null || focuseLibLecture.isEmpty()) {
							continue;
						} else {
							lectureDBList.addAll(focuseLibLecture);
						}
						Logger.d("userPosition==2 " + "focuse=" + focuse);
					}
				}
				break;
			//我的附近
			case 3:
				Logger.d("userPosition==3 ");
				lectureDBList = mInterLecDao.loadAll();
				break;
			default:
		}

		Logger.d("location "+area[locationPosition]);

		if (!lectureDBList.isEmpty()) {
			//判断地区筛选条件
			if (locationPosition != 0) {
				List <InterimLectureDB> interimLectureDBList = new ArrayList <>();
				for (InterimLectureDB interimLectureDB : lectureDBList) {
					if (interimLectureDB.getLectureDistrict().contains(area[locationPosition])) {
						interimLectureDBList.add(interimLectureDB);
					}
				}
				lectureDBList.clear();
				lectureDBList.addAll(interimLectureDBList);
			}
		}


		//判断筛选结果
		if (lectureDBList.isEmpty()) {
			Toasty.error(ResultSiftActivity.this, "无符合条件的讲座").show();
		} else {
			List <LectureDB> lectureDBS = new ArrayList <>();
			for (InterimLectureDB interimLectureDB : lectureDBList) {
				LectureDB lectureDB = new LectureDB();
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
			lecturelist.addAll(0, lectureDBS);
			listView.setSelection(0);
			mAdapter.notifyDataSetChanged();
		}

		DialogUtils.closeDialog(searchLoadDialog);
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
					//结果处理
					handler.sendEmptyMessage(4);
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