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
import android.widget.TextView;

public class LectureDetailActivity extends BaseActivity {

	TitleLayout titleLayout;

	Button titleFirstButton;
	Button titleSecondButton;

	TextView lectureTitle;
	TextView lectureSource;
	TextView lectureTime;
	TextView lecturePlace;
	TextView lectureContent;
	TextView lectureOriginal;

	Lecture lecture;

	boolean ifLike=false;

	String title="自觉担当新时代党的历史使命";
	String source="武汉理工大学理工党员网";
	String time="2018-03-08 09:15:46";
	String place="理工党员网";
	String contents="要更加自觉地增强对中国特色社会主义理论、道路、制度、文化的自信和对党的领导的自信，努力做政治过硬、本领高强的共产党人，既不走封闭僵化的老路，也不走改旗易帜的邪路\n" +
			"\n" +
			"从严治党必须抓住解决人们头脑中“总开关”问题开展工作，不松劲、不间歇，不失之于宽、松、软\n" +
			"\n" +
			"习近平总书记在党的十九大报告中庄严宣告“中国特色社会主义进入了新时代”，专门就新时代“中国共产党的历史使命”进行了精准阐述，号召全党“不忘初心，牢记使命，高举旗帜，团结奋进”。党在新时代肩负的历史使命，也是各级党组织和每个党员应该自觉担当的历史使命。\n" +
			"\n" +
			"准确把握新时代新要求，不忘初心、勇担使命，竭力推动建设社会主义现代化强国的伟大飞跃\n" +
			"\n" +
			"习近平总书记指出：“中国特色社会主义进入新时代，意味着近代以来久经磨难的中华民族迎来了从站起来、富起来到强起来的伟大飞跃”。“这个新时代，是承前启后、继往开来，在新的历史条件下继续夺取中国特色社会主义伟大胜利的时代。”我们党在新时代的历史使命，就是坚持和发展中国特色社会主义，为建设社会主义现代化强国、实现中华民族伟大复兴而奋斗。习近平总书记关于新时代、新使命的论断，是巧妙运用辩证唯物史观和人类社会发展规律，在深刻分析中国共产党领导中国人民为实现中华民族振兴长期奋斗历史经验的基础上，既立足国情放眼世界、又立足现实预测未来得出的科学结论。我们必须坚持这样的立场、世界观和方法论来理解中国特色社会主义新时代和党肩负的历史使命，才能领悟精髓，把握本质。\n" +
			"\n" +
			"时代决定使命，使命呼唤担当。不同时代的使命，习近平总书记在十九大报告中对新时代党的建设总要求的阐述，深刻反映了执政党建设的规律，充分体现了其中的科学性、时代性、原则性、系统性，是我们推进新时代伟大工程的总纲，是提高党的建设水平和管党治党能力的思想遵循及行动引领。我们要更加自觉地学习贯彻习近平新时代中国特色社会主义思想，遵从党中央的集中统一领导，维护党中央的权威，密切联系坚持和发展中国特色社会主义事业、实现中华民族伟大复兴中国梦的伟大斗争实践，把新时代党的建设伟大工程做得更强更好。";

	 String original="http://lgdy.whut.edu.cn/index.php?c=home&a=detail&id=90705";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lecture_detail);

		//初始化控件
		initWidget();
		//初始化视图
		initView();

		//设置标题栏返回按钮点击监听事件
		titleFirstButton.setOnClickListener(this);
		//设置标题栏添加我想看按钮点击监听事件
		titleSecondButton.setOnClickListener(this);
		//设置讲座详情页查看原文按钮点击监听事件
		lectureOriginal.setOnClickListener(this);
		//设置讲座详情页的发布图书馆按钮点击监听事件
		lectureSource.setOnClickListener(this);
	}

	protected void initWidget(){
		titleLayout=(TitleLayout)findViewById(R.id.lecture_detail_title_layout);
		titleFirstButton=titleLayout.getFirstButton();
		titleSecondButton=titleLayout.getSecondButton();
		lectureTitle=(TextView)findViewById(R.id.lecture_detail_title_text) ;
		lectureSource=(TextView)findViewById(R.id.lecture_detail_source_text) ;
		lectureTime=(TextView)findViewById(R.id.lecture_detail_time_text) ;
		lecturePlace=(TextView)findViewById(R.id.lecture_detail_place_text) ;
		lectureContent=(TextView)findViewById(R.id.lecture_detail_content_text) ;
		lectureOriginal=(TextView)findViewById(R.id.lecture_detail_original_text) ;
	}
	protected void initView(){

		//隐藏系统标题栏
		HideSysTitle();

		lectureTitle.setText(title);
		lectureSource.setText(source);
		lectureTime.setText(time);
		lecturePlace.setText(place);
		lectureContent.setText(contents);
		titleLayout.setTitle("讲座详情");

		//判断是否已经添加想看
		if(ifLike){
			titleSecondButton.setBackgroundResource(R.drawable.ic_lecture_likes_selected);
		}else{
			titleSecondButton.setBackgroundResource(R.drawable.ic_lecture_likes);
		}
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.title_first_button:{
				finish();
				break;
			}
			case R.id.title_second_button:{
				if(ifLike){
					titleSecondButton.setBackgroundResource(R.drawable.ic_lecture_likes);
					ifLike=false;
				}else{
					titleSecondButton.setBackgroundResource(R.drawable.ic_lecture_likes_selected);
					ifLike=true;
				}
				break;
			}
			case R.id.lecture_detail_original_text:{
				Intent intent=new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri.parse(original));
				startActivity(intent);
				break;
			}
			case R.id.lecture_detail_source_text:{
				Intent intent=new Intent(LectureDetailActivity.this,LibraryDetailActivity.class);
				startActivity(intent);
				break;
			}
			default:
		}
	}
}
