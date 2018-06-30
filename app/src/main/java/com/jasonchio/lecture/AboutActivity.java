package com.jasonchio.lecture;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AboutActivity extends BaseActivity {

	TextView aboutText;             //关于界面的正文

	TitleLayout titleLayout;        //关于界面的标题栏

	Button titleFirstButton;        //标题栏的第一个按钮

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);

		//初始化控件
		initWidget();
		//初始化视图
		initView();
		//初始化点击响应事件
		initEvent();
	}

	@Override
	void initView() {
		HideSysTitle();
		titleLayout.setSecondButtonVisible(View.GONE);
		titleLayout.setTitle("关于");
		aboutText.setText("\t精心整理来自武汉各高校图书馆、社会书店等发布源发布的讲座信息，帮你轻松获取学习信息；\n" + "对感兴趣的讲座发表评论，与更多人分享你的见解；\n" + "人性化的讲座收藏、图书馆关注功能，让你更方便留意喜爱的讲座信息；\n" + "即时动态发表，与更多用户分享你的见闻......\n" + "更多功能敬请期待。\n" + "聚讲座，你想看的，都在这里。");
	}

	@Override
	void initWidget() {
		aboutText = (TextView) findViewById(R.id.about_content_text);
		titleLayout = (TitleLayout) findViewById(R.id.about_title_layout);
		titleFirstButton = (Button) findViewById(R.id.title_first_button);
	}

	@Override
	void initEvent() {
		titleFirstButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			//标题栏的第一个按钮
			case R.id.title_first_button:
				finish();
				break;
			default:
		}
	}
}