package com.jasonchio.lecture;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class SelecteLectureCommentActivity extends BaseActivity {

	TitleLayout titleLayout;
	Button titleFirstButton;

	List<Lecture> lectureList=new ArrayList<>();
	LectureAdapter lectureAdapter;

	String contents="十八大以来我国所取得的巨大进入了加速圆梦期，中华民族伟大复兴的中国梦正在由“遥想”“遥望”变为“近看”“凝视”。您是否在为一篇篇手动输入参考文献而痛苦？您是否在用EXCEL等原始手段为文献排序？您是否还在为从电脑成堆的文档中寻找所需要的文献而烦恼？您是否在茫茫文献海洋中迷失";
	int consts=0;
	ListView listView;
	Lecture lecture=new Lecture("NoteExpress文献管理与论文写作讲座","2017年12月7日(周三)14：30","武汉大学图书馆",consts,contents,R.drawable.test_image);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_selecte_lecture_tocomment);

		initWidget();
		//初始化视图
		initView();


		titleFirstButton.setOnClickListener(this);

		lectureList.add(lecture);

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Lecture lecture=lectureList.get(position);
				Intent intent=new Intent(SelecteLectureCommentActivity.this,AddCommentActivity.class);
				intent.putExtra("lecture_id",lecture.getLectureTitle());
				startActivity(intent);
				finish();
			}
		});
	}

	@Override
	void initView() {
		//BaseActivity方法，隐藏系统标题栏
		HideSysTitle();

		titleLayout.setSecondButtonVisible(View.GONE);
		titleLayout.setTitle("请选择要评论的讲座");
		listView.setAdapter(lectureAdapter);
	}

	@Override
	void initWidget() {
		titleLayout=(TitleLayout)findViewById(R.id.select_lecture_title_layout);
		titleFirstButton=titleLayout.getFirstButton();
		listView=(ListView)findViewById(R.id.select_lecture_list);
		lectureAdapter=new LectureAdapter(SelecteLectureCommentActivity.this,R.layout.lecure_listitem,lectureList);
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
