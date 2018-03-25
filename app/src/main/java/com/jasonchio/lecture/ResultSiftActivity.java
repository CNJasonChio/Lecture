package com.jasonchio.lecture;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.jasonchio.lecture.util.ResultSift.DropDownMenu;
import com.jasonchio.lecture.util.ResultSift.ListDropDownAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ResultSiftActivity extends BaseActivity {

	private String headers[] = {"年龄", "性别"};// 条件选择的标题
	private List<View> popupViews = new ArrayList<View>();
	private DropDownMenu mDropDownMenu;//自定义VIew类的对象
	private TitleLayout titleLayout;
	private Button titleFirstButton;
	private Button titleSecondButton;
	private ListDropDownAdapter ageAdapter;  //年龄显示的适配器
	private ListDropDownAdapter sexAdapter;//   性别显示的适配器

	private String ages[] = {"不限", "18岁以下", "18-22岁", "23-26岁", "27-35岁", "35岁以上"};
	private String sexs[] = {"不限", "男", "女"};

	private int constellationPosition = 0;//  选中群体样式列表的序列号

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result_sift);

		//初始化控件
		initWidget();
		//初始化视图
		initView();

		titleFirstButton.setOnClickListener(this);
		initView(); //初始化对象
		initData();//初始化 数据
		initEvent();// 初始化点击事件
	}

	//下面四个是点击选择条件选择是弹出的视图View，可以是ListView也可以是VIew

	ListView ageView;
	ListView sexView;


	TextView textView;//选择条件中间显示的视图，可以是View、ListView、TextView

	/**
	 * 初始化数据
	 */
	protected void initView() {

		//BaseActivity方法，隐藏系统标题栏
		HideSysTitle();

		titleLayout.setTitle("搜索结果");

		titleLayout.setSecondButtonVisible(View.GONE);

		//init age menu
		ageView = new ListView(this);
		ageView.setDividerHeight(0);
		ageAdapter = new ListDropDownAdapter(this, Arrays.asList(ages));
		ageView.setAdapter(ageAdapter);

		//init sex menu
		sexView = new ListView(this);
		sexView.setDividerHeight(0);
		sexAdapter = new ListDropDownAdapter(this, Arrays.asList(sexs));
		sexView.setAdapter(sexAdapter);

	}

	@Override
	void initWidget() {

		mDropDownMenu = (DropDownMenu) findViewById(R.id.dropDownMenu);

		titleLayout=(TitleLayout)findViewById(R.id.result_sift_title_layout);
		titleFirstButton=titleLayout.getFirstButton();
		titleSecondButton=titleLayout.getSecondButton();
	}


	private void initData() {
		//init popupViews
		//popupViews.add(cityView);
		popupViews.add(ageView);
		popupViews.add(sexView);
		//popupViews.add(constellationView);

		//init context view
		textView = new TextView(this);
		textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		textView.setText("内容显示区域"+"\n");
		textView.setGravity(Gravity.CENTER);
		textView.setTextSize(20);

		//init dropdownview
		mDropDownMenu.setDropDownMenu(Arrays.asList(headers), popupViews, textView);

	}

	/**
	 * 设置四个条件选择的点击事件
	 */
	private void initEvent() {
		//add item click event

		ageView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ageAdapter.setCheckItem(position);
				mDropDownMenu.setTabText(position == 0 ? headers[0] : ages[position]);
				textView.append(position == 0 ?headers[0] : ages[position]+"\n");
				mDropDownMenu.closeMenu();
			}
		});

		sexView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				sexAdapter.setCheckItem(position);
				mDropDownMenu.setTabText(position == 0 ? headers[1] : sexs[position]);
				textView.append(position == 0 ?headers[1] : sexs[position]+"\n");
				mDropDownMenu.closeMenu();
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
		switch (v.getId()){
			case R.id.title_first_button:{
				finish();
				break;
			}
		}
	}
}