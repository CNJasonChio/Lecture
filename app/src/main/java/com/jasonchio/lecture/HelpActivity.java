package com.jasonchio.lecture;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jasonchio.lecture.util.ConstantClass;
import com.orhanobut.logger.Logger;

import java.io.IOException;

import me.codeboy.android.aligntextview.AlignTextView;
import me.codeboy.android.aligntextview.CBAlignTextView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.jasonchio.lecture.util.HttpUtil.ContentRequest;

public class HelpActivity extends BaseActivity{

	AlignTextView helpText;          //正文控件

	TitleLayout titleLayout;    //标题栏

	Button titleFirstButton;    //标题栏的第一个按钮

	Handler handler;

	String helpContent;
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
		getHelpContent(ConstantClass.HELP_ADDRESS,handler);
	}

	@Override
	void initView() {
		//隐藏标题栏
		HideSysTitle();
		titleLayout.setSecondButtonVisible(View.GONE);
		titleLayout.setTitle("使用帮助");
	}

	@Override
	void initWidget() {
		helpText=(AlignTextView) findViewById(R.id.help_content_text);
		titleLayout=(TitleLayout)findViewById(R.id.help_title_layout);
		titleFirstButton=(Button)findViewById(R.id.title_first_button);
	}

	@Override
	void initEvent() {
		titleFirstButton.setOnClickListener(this);

		handler = new Handler(new Handler.Callback() {
			@Override
			public boolean handleMessage(Message msg) {
				switch (msg.what) {
					case 1:
						helpContent =msg.obj.toString();
						helpText.setText(helpContent);
						break;
					default:
				}
				return true;
			}
		});
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

	public static void getHelpContent(String address, final Handler handler) {
		ContentRequest(address, new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {
				Logger.d("content获取失败");
			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				Message message=new Message();
				message.what=1;
				message.obj=response.body().string();
				handler.sendMessage(message);
			}
		});
	}
}
