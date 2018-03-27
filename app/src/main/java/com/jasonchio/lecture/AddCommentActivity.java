package com.jasonchio.lecture;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class AddCommentActivity extends BaseActivity {

	TitleLayout titleLayout;
	Button titleFirstButton;
	Button titleSecondButton;

	TextView comment_text;
	List<Lecture> lectureList=new ArrayList<>();
	LectureAdapter lectureAdapter;

	String contents="十八大以来我国所取得的巨大进入了加速圆梦期，中华民族伟大复兴的中国梦正在由“遥想”“遥望”变为“近看”“凝视”。您是否在为一篇篇手动输入参考文献而痛苦？您是否在用EXCEL等原始手段为文献排序？您是否还在为从电脑成堆的文档中寻找所需要的文献而烦恼？您是否在茫茫文献海洋中迷失";
	int consts=0;

	Lecture lecture=new Lecture("NoteExpress文献管理与论文写作讲座","2017年12月7日(周三)14：30","武汉大学图书馆",consts,contents,R.drawable.test_image);

	ListView listView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_comment);

		//初始化控件
		initWidget();
		//初始化视图
		initView();

		lectureList.add(lecture);

		titleFirstButton.setOnClickListener(this);
		titleSecondButton.setOnClickListener(this);
	}

	@Override
	void initView() {

		//BaseActivity方法，隐藏系统标题栏
		HideSysTitle();
		titleLayout.setTitle("发表我的评论");
		titleSecondButton.setBackgroundResource(R.drawable.ic_title_send_comment);
	}

	@Override
	void initWidget() {
		titleLayout=(TitleLayout)findViewById(R.id.add_comment_title_layout);
		titleFirstButton=titleLayout.getFirstButton();
		titleSecondButton=titleLayout.getSecondButton();
		comment_text=(TextView)findViewById(R.id.comment_edit) ;
		listView=(ListView)findViewById(R.id.comment_lecture_selected_list);
		lectureAdapter=new LectureAdapter(AddCommentActivity.this,R.layout.lecure_listitem,lectureList);
		listView.setAdapter(lectureAdapter);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.title_first_button:{
				finish();
				break;
			}
			case R.id.title_second_button:{
				finish();
				break;
			}
			default:
		}
	}
}
