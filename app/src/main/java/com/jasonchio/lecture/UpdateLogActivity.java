package com.jasonchio.lecture;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class UpdateLogActivity extends BaseActivity {
	
	TextView updateLogText;
	TitleLayout titleLayout;
	Button titleFirstButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update_log);

		initWidget();

		initView();

		initEvent();

	}

	@Override
	void initView() {
		HideSysTitle();
		titleLayout.setSecondButtonVisible(View.GONE);
		titleLayout.setTitle("更新日志");
		updateLogText.setText("v1.0\n" + "改善了推荐算法；\n" + "优化了发表对讲座评论的功能，在评论之前可以选择讲座；\n" + "支持对搜索结果的分地区和分是否关注筛选。");
	}

	@Override
	void initWidget() {
		updateLogText =(TextView)findViewById(R.id.update_log_content_text);
		titleLayout=(TitleLayout)findViewById(R.id.update_log_title_layout);
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
