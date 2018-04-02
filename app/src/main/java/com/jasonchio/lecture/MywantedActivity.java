package com.jasonchio.lecture;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.jasonchio.lecture.database.LectureDB;

import java.util.ArrayList;
import java.util.List;

public class MywantedActivity extends BaseActivity {

	SwipeToLoadLayout swipeToLoadLayout;
	TitleLayout titleLayout;
	Button titleFirstButton;
	LectureAdapter mAdapter;

	String contents = "十八大以来我国所取得的巨大进入了加速圆梦期，中华民族伟大复兴的中国梦正在由“遥想”“遥望”变为“近看”“凝视”。您是否在为一篇篇手动输入参考文献而痛苦？您是否在用EXCEL等原始手段为文献排序？您是否还在为从电脑成堆的文档中寻找所需要的文献而烦恼？您是否在茫茫文献海洋中迷失";
	ListView listView;
	int consts = 0;

	//LectureDB lecture = new LectureDB("NoteExpress文献管理与论文写作讲座", "2017年12月7日(周三)14：30", "武汉大学图书馆", consts, contents, R.drawable.test_image);

	List<LectureDB> lecturelist = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mywanted);

		//初始化控件
		initWidget();
		//初始化视图
		initView();

		titleFirstButton.setOnClickListener(this);

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				LectureDB lecture=lecturelist.get(position);
				Intent intent=new Intent(MywantedActivity.this,LectureDetailActivity.class);
				startActivity(intent);

			}
		});

		swipeToLoadLayout.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {

				//lecturelist.add(lecture);
				mAdapter.notifyDataSetChanged();
				swipeToLoadLayout.setRefreshing(false);
			}
		});

		swipeToLoadLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
			@Override
			public void onLoadMore() {

				//lecturelist.add(lecture);
				mAdapter.notifyDataSetChanged();
				swipeToLoadLayout.setLoadingMore(false);
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
		titleLayout.setTitle("我的想看");
		listView.setAdapter(mAdapter);
	}

	@Override
	void initWidget() {
		swipeToLoadLayout = (SwipeToLoadLayout) findViewById(R.id.swipeToLoadLayout);
		titleLayout=(TitleLayout)findViewById(R.id.mywanted_title_layout);
		titleFirstButton=titleLayout.getFirstButton();
		listView = (ListView) findViewById(R.id.swipe_target);

		mAdapter = new LectureAdapter(MywantedActivity.this, R.layout.lecure_listitem, lecturelist);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.title_first_button:{
				finish();
				break;
			}
			default:
		}
	}
}
