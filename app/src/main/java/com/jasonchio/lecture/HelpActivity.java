package com.jasonchio.lecture;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class HelpActivity extends BaseActivity{

	TextView helpText;          //正文控件

	TitleLayout titleLayout;    //标题栏

	Button titleFirstButton;    //标题栏的第一个按钮

	//正文
	String content="       聚讲座为您整合武汉各大讲座信息发布源的讲座信息，内容均来自官网并附原网页链接，您可以放心查看。\n"
			+ "       您可以在首页浏览您关注的各图书馆新讲座信息、您附近的讲座信息、以及开发者为您推荐的讲座信息。当您为某一讲座点击喜欢按钮，系统会为您记录，您可以在“我的”界面“我的想看”页面再次找到它。\n"
			+ "       在“搜索”页面，您可以对感兴趣的讲座关键字\n或图书馆关键字进行搜索，并对搜索结果进行地区、关注的筛选，从而更方便地找到您感兴趣的讲座。\n"
			+ "      在“我的”页面，您可以浏览自己的关注图书馆、想看的讲座，并对相关信息进行管理。同时你可以完善自己的个人信息，我们会严格对其进行保密，您可以放心填写。\n";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help);

		//初始化控件
		initWidget();
		//初始化视图
		initView();
		//初始化响应事件
		initEvent();
	}

	@Override
	void initView() {
		//隐藏标题栏
		HideSysTitle();
		titleLayout.setSecondButtonVisible(View.GONE);
		titleLayout.setTitle("使用帮助");
		helpText.setText(content);
	}

	@Override
	void initWidget() {
		helpText=(TextView)findViewById(R.id.help_content_text);
		titleLayout=(TitleLayout)findViewById(R.id.help_title_layout);
		titleFirstButton=(Button)findViewById(R.id.title_first_button);
	}

	@Override
	void initEvent() {
		titleFirstButton.setOnClickListener(this);
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
}
