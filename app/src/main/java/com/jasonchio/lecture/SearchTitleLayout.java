package com.jasonchio.lecture;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * /**
 * <p>
 * ----------Dragon be here!----------/
 * 　　　┏┓　　　┏┓
 * 　　┏┛┻━━━┛┻┓
 * 　　┃　　　　　　　┃
 * 　　┃　　　━　　　┃
 * 　　┃　┳┛　┗┳　┃
 * 　　┃　　　　　　　┃
 * 　　┃　　　┻　　　┃
 * 　　┃　　　　　　　┃
 * 　　┗━┓　　　┏━┛
 * 　　　　┃　　　┃神兽保佑
 * 　　　　┃　　　┃代码无BUG！
 * 　　　　┃　　　┗━━━┓
 * 　　　　┃　　　　　　　┣┓
 * 　　　　┃　　　　　　　┏┛
 * 　　　　┗┓┓┏━┳┓┏┛
 * 　　　　　┃┫┫　┃┫┫
 * 　　　　　┗┻┛　┗┻┛
 * ━━━━━━神兽出没━━━━━━by:zhaoyaobang
 * <p>
 * Created by zhaoyaobang on 2018/2/22.
 */

public class SearchTitleLayout extends LinearLayout{

	ImageView titleNoticeImage;
	ImageView titleSearchImage;
	EditText titleInputEdit;

	// 自定义属性设置
	// 1. 搜索字体属性设置：大小、颜色 & 默认提示
	private Float textSizeSearch;
	private int textColorSearch;
	private String textHintSearch;

	// 2. 搜索框设置：高度 & 颜色
	private int searchBlockHeight;
	private int searchBlockColor;
	private LinearLayout search_block; // 搜索框布局

	public SearchTitleLayout(Context context) {
		super(context,null);
	}

	public SearchTitleLayout(final Context context, AttributeSet attrs) {
		super(context, attrs);
		//引入布局
		LayoutInflater.from(context).inflate(R.layout.search_title,this);
		initAttrs(context,attrs);
		init();
	}

	public void init() {
		//绑定搜索框EditText
		titleInputEdit = (EditText) findViewById(R.id.title_search_edit);
		titleInputEdit.setTextSize(textSizeSearch);
		titleInputEdit.setTextColor(textColorSearch);
		titleInputEdit.setHint(textHintSearch);

		//搜索框背景颜色
		search_block = (LinearLayout)findViewById(R.id.search_title_block);
		search_block.setBackgroundResource(R.drawable.button_shape_light_grey);
		/*LayoutParams params = (LayoutParams) search_block.getLayoutParams();
		params.height = searchBlockHeight;

		search_block.setBackgroundColor(searchBlockColor);
		search_block.setLayoutParams(params);*/

		//搜索按键
		titleSearchImage = (ImageView) findViewById(R.id.search_title_search_image);

		//通知按钮
		titleNoticeImage =(ImageView) findViewById(R.id.search_title_notice_image);

	}

	private void initAttrs(Context context, AttributeSet attrs) {

		// 控件资源名称
		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Search_View);

		// 搜索框字体大小（dp）
		textSizeSearch = typedArray.getDimension(R.styleable.Search_View_textSizeSearch, 14);

		// 搜索框字体颜色（使用十六进制代码，如#333、#8e8e8e）
		int defaultColor = context.getResources().getColor(R.color.colorText); // 默认颜色 = 灰色
		textColorSearch = typedArray.getColor(R.styleable.Search_View_textColorSearch, defaultColor);

		// 搜索框提示内容（String）
		textHintSearch="讲座标题/讲座地点/讲座来源/正文关键词";

		// 搜索框高度
		searchBlockHeight = typedArray.getInteger(R.styleable.Search_View_searchBlockHeight, 100);

		// 搜索框颜色
		int defaultColor2 = context.getResources().getColor(R.color.colorDefault); // 默认颜色
		searchBlockColor = typedArray.getColor(R.styleable.Search_View_searchBlockColor, defaultColor2);

		// 释放资源
		typedArray.recycle();
	}
}
