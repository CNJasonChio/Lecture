package com.jasonchio.lecture;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class LibraryDetailActivity extends AppCompatActivity {

	ImageView libraryImage;

	TextView titleLayoutTitleText;
	TextView libraryName;
	TextView libraryContent;
	TextView libraryOriginal;

	Button titleFirstButton;
	Button titleSecondButton;

	boolean ifFocused=false;

	String name="武汉理工大学图书馆";
	String contents="十八大以来我国所取得的巨大进入了加速圆梦期，中华民族伟大复兴的中国梦正在由“遥想”“遥望”变为“近看”“凝视”。您是否在为一篇篇手动输入参考文献而痛苦？您是否在用EXCEL等原始手段为文献排序？您是否还在为从电脑成堆的文档中寻找所需要的文献而烦恼？您是否在茫茫文献海洋中迷失";

	String original="http://lgdy.whut.edu.cn/index.php?c=home&a=detail&id=90705";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_library_detail);

		//隐藏自带标题栏
		if (Build.VERSION.SDK_INT >= 21) {
			View decorView = getWindow().getDecorView();
			decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
					| View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
			getWindow().setStatusBarColor(Color.TRANSPARENT);
		}
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.hide();
		}


		libraryImage=(ImageView)findViewById(R.id.library_photo_image);
		libraryName=(TextView)findViewById(R.id.library_name_text);
		libraryContent=(TextView)findViewById(R.id.library_content_text);
		libraryOriginal=(TextView)findViewById(R.id.library_original_text);
		titleFirstButton=(Button)findViewById(R.id.library_title_first_button);
		titleSecondButton=(Button)findViewById(R.id.library_title_second_button);
		titleLayoutTitleText=(TextView)findViewById(R.id.library_titlelayout_title_text);

		titleLayoutTitleText.setText("图书馆详情");

		libraryContent.setText(contents);
		libraryImage.setImageResource(R.drawable.test_image);
		libraryName.setText(name);

		if(ifFocused){
			titleSecondButton.setText("已关注");
			titleSecondButton.setTextColor(Color.argb(255,255,157,0));
			titleSecondButton.setBackgroundResource(R.drawable.button_shape_origin);

		}else {
			titleSecondButton.setText("点击关注");
			titleSecondButton.setTextColor(Color.argb(255,16,16,16));
			titleSecondButton.setBackgroundResource(R.drawable.button_shape_black);
		}

		titleFirstButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		titleSecondButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(ifFocused){
					titleSecondButton.setText("点击关注");
					titleSecondButton.setTextColor(Color.argb(255,16,16,16));
					titleSecondButton.setBackgroundResource(R.drawable.button_shape_black);
					ifFocused=false;
				}else {
					titleSecondButton.setText("已关注");
					titleSecondButton.setTextColor(Color.argb(255,255,157,0));
					titleSecondButton.setBackgroundResource(R.drawable.button_shape_origin);
					ifFocused=true;
				}
			}
		});

		libraryOriginal.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri.parse(original));
				startActivity(intent);
			}
		});
	}
}
