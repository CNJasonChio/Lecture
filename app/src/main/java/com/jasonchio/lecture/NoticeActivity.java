package com.jasonchio.lecture;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.jasonchio.lecture.greendao.LectureMessageDB;
import com.jasonchio.lecture.greendao.NoticeItemDB;

import java.util.ArrayList;
import java.util.List;

public class NoticeActivity extends BaseActivity {

	List<NoticeItemDB> noticeItemDBList=new ArrayList<>();

	SwipeToLoadLayout swipeToLoadLayout;        //刷新布局

	RecyclerView noticeRecyclerView;

	NoticeAdapter noticeAdapter;

	TitleLayout titleLayout;

	Button titleFirstButton;
	Button titleSecondButton;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notice);

		initNotice();
		initWidget();
		initView();
		initEvent();
	}

	@Override
	void initWidget() {
		titleLayout=(TitleLayout)findViewById(R.id.notice_title_layout);
		titleFirstButton=titleLayout.getFirstButton();
		titleSecondButton=titleLayout.getSecondButton();
		noticeRecyclerView=(RecyclerView)findViewById(R.id.notice_listview);
		LinearLayoutManager layoutManager=new LinearLayoutManager(this);
		noticeRecyclerView.setLayoutManager(layoutManager);
		noticeAdapter=new NoticeAdapter(noticeItemDBList,NoticeActivity.this);
		noticeRecyclerView.setAdapter(noticeAdapter);
		titleSecondButton.setOnClickListener(this);
		titleFirstButton.setOnClickListener(this);
	}

	@Override
	void initView() {
		HideSysTitle();
		titleLayout.setTitle("通知");
		titleSecondButton.setBackgroundResource(R.drawable.ic_notice_clear);
	}

	@Override
	void initEvent() {

	}

	@Override
	public void onClick(View v) {

	}

	public void initNotice(){
		for(int i=0;i<4;i++){
			NoticeItemDB noticeItemDB=new NoticeItemDB();
			noticeItemDB.setDynamicsId(i);
			noticeItemDB.setUserName("zhaoyoabang");
			noticeItemDB.setNoticeTime("3分钟前");
			noticeItemDB.setNoticeContent("测试测试测试测试测测试测试测测试测试测试测试");
			noticeItemDB.setNoticeType("dynamics");
			noticeItemDBList.add(noticeItemDB);
		}
		for(int i=0;i<4;i++){
			NoticeItemDB noticeItemDB=new NoticeItemDB();
			noticeItemDB.setDynamicsId(i+4);
			noticeItemDB.setUserName("wanghuan");
			noticeItemDB.setNoticeTime("3分钟前");
			noticeItemDB.setNoticeContent("测试测试测试测试测试测试");
			noticeItemDB.setNoticeType("comment");
			noticeItemDBList.add(noticeItemDB);
		}
		for(int i=0;i<4;i++){
			NoticeItemDB noticeItemDB=new NoticeItemDB();
			noticeItemDB.setDynamicsId(i+4);
			noticeItemDB.setUserName("sunfanglei");
			noticeItemDB.setNoticeTime("3分钟前");
			noticeItemDB.setNoticeContent("测试测试测试测试测试测测试测试测测试测试测试");
			noticeItemDB.setNoticeType("reply");
			noticeItemDBList.add(noticeItemDB);
		}
	}
}
