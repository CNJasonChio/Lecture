package com.jasonchio.lecture;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;

import java.util.ArrayList;
import java.util.List;

public class MycommentActivity extends BaseActivity implements CommentAdapter.InnerItemOnclickListener,AdapterView.OnItemClickListener{

	SwipeToLoadLayout swipeToLoadLayout;

	CommentAdapter mAdapter;

	TitleLayout titleLayout;

	Button titleSecondButton;

	Button titleFirstButton;

	String contents="十八大以来我国所取得的巨大进入了加速圆梦期，中华民族伟大复兴的中国梦正在由“遥想”“遥望”变为“近看”“凝视”。您是否在为一篇篇手动输入参考文献而痛苦？您是否在用EXCEL等原始手段为文献排序？您是否还在为从电脑成堆的文档中寻找所需要的文献而烦恼？您是否在茫茫文献海洋中迷失";

	int likers =0;

	int lecturelikes=0;

	boolean islike=false;

	Lecture lecture;

	String time="2017年12月7日(周三)14：30";

	String userName="赵耀邦";

	String commentText="十八大以来我国所取得的巨大进入了加速圆梦期，中华民族伟大复兴的中国梦正在由“遥想”“遥望”变为“近看”“凝视”。您是否在为一篇篇手动输入参考文献而痛苦？您是否在用EX";

	Comment comment;

	List<Comment> commentList =new ArrayList<>();

	ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mycomment);

		//初始化控件
		initWidget();
		//初始化视图
		initView();

		mAdapter.setOnInnerItemOnClickListener(this);
		listView.setOnItemClickListener(this);

		swipeToLoadLayout.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				initComment();
				commentList.add(comment);
				mAdapter.notifyDataSetChanged();
				swipeToLoadLayout.setRefreshing(false);
			}
		});

		swipeToLoadLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
			@Override
			public void onLoadMore() {
				initComment();
				commentList.add(comment);
				mAdapter.notifyDataSetChanged();
				swipeToLoadLayout.setLoadingMore(false);
			}
		});

		autoRefresh();
	}



	private void autoRefresh() {
		swipeToLoadLayout.post(new Runnable() {
			@Override
			public void run() {
				swipeToLoadLayout.setRefreshing(true);
			}
		});
	}
	@Override
	void initView() {
		//BaseActivity方法，隐藏系统标题栏
		HideSysTitle();

		titleLayout.setSecondButtonVisible(View.GONE);
		titleLayout.setTitle("我的点评");
	}

	@Override
	void initWidget() {
		swipeToLoadLayout = (SwipeToLoadLayout)findViewById(R.id.swipeToLoadLayout);
		titleLayout=(TitleLayout)findViewById(R.id.mycomment_title_layout);
		titleSecondButton=titleLayout.getSecondButton();
		titleFirstButton=titleLayout.getFirstButton();
		listView = (ListView) findViewById(R.id.swipe_target);
		mAdapter = new CommentAdapter(commentList,MycommentActivity.this);
		listView.setAdapter(mAdapter);
	}

	@Override
	public void onClick(View v) {

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Comment comment= commentList.get(position);
		Lecture lecture1=comment.getLecture();
		Intent intent=new Intent(MycommentActivity.this,LectureDetailActivity.class);
		startActivity(intent);
	}

	@Override
	public void itemClick(View v) {
		int position;
		position = (Integer) v.getTag();

		Comment comment= commentList.get(position);

		switch (v.getId()){
			case R.id.comment_user_layout:
				Toast.makeText(MycommentActivity.this,"查看评论人资料",Toast.LENGTH_SHORT).show();
				break;
			case R.id.comment_lecture_layout:

				Intent intent=new Intent(MycommentActivity.this,LectureDetailActivity.class);
				startActivity(intent);
				break;
			case R.id.comment_like_layout:

				if(islike){
					Toast.makeText(MycommentActivity.this,"取消点赞",Toast.LENGTH_SHORT).show();
					comment.setCommentLikers(comment.getCommentLikers()-1);
					comment.setCommentLikersImage(R.drawable.ic_discovery_comment_like);
					mAdapter.notifyDataSetChanged();
					islike=false;
				}else{
					Toast.makeText(MycommentActivity.this,"点赞",Toast.LENGTH_SHORT).show();
					comment.setCommentLikers(comment.getCommentLikers()+1);
					comment.setCommentLikersImage(R.drawable.ic_discovery_comment_like_selected);
					mAdapter.notifyDataSetChanged();
					islike=true;
				}
				break;
			default:
		}
	}

	private void initComment(){
		lecturelikes++;
		lecture=new Lecture("NoteExpress文献管理与论文写作讲座","2017年12月7日(周三)14：30","武汉大学图书馆", lecturelikes,contents,R.drawable.test_image);
		comment=new Comment(R.drawable.test_oliver,userName,lecture,time, likers,contents );
	}}
