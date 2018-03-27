package com.jasonchio.lecture;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;

import java.util.ArrayList;
import java.util.List;

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
 * Created by zhaoyaobang on 2018/3/6.
 */

public class DiscoveryFragment extends Fragment implements CommentAdapter.InnerItemOnclickListener,AdapterView.OnItemClickListener{

	private View rootview;

	SwipeToLoadLayout swipeToLoadLayout;

	CommentAdapter mAdapter;

	TitleLayout titleLayout;

	Button titleSecondButton;

	String contents="十八大以来我国所取得的巨大进入了加速圆梦期，中华民族伟大复兴的中国梦正在由“遥想”“遥望”变为“近看”“凝视”。您是否在为一篇篇手动输入参考文献而痛苦？您是否在用EXCEL等原始手段为文献排序？您是否还在为从电脑成堆的文档中寻找所需要的文献而烦恼？您是否在茫茫文献海洋中迷失";

	int likers =0;

	int lecturelikes=0;

	boolean islike=false;

	Lecture lecture;

	String time="2018年12月7日";

	String userName="赵耀邦";

	Comment comment;

	ListView listView;

	List<Comment> commentList =new ArrayList<>();
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,

	                         Bundle savedInstanceState) {

		//在fragment onCreateView 里缓存View，防止每次onCreateView 的时候重绘View
		if(rootview == null){
			rootview=inflater.inflate(R.layout.fragment_discovery,null);
			Toast.makeText(getActivity(),"FragmentDiscovery==onCreateView",Toast.LENGTH_SHORT ).show();
		}
		ViewGroup parent=(ViewGroup)rootview.getParent();
		if(parent!=null){
			parent.removeView(rootview);
		}

		swipeToLoadLayout = (SwipeToLoadLayout) rootview.findViewById(R.id.swipeToLoadLayout);
		titleLayout=(TitleLayout)rootview.findViewById(R.id.discovery_title_layout);

		titleSecondButton=titleLayout.getSecondButton();
		titleSecondButton.setBackgroundResource(R.drawable.ic_title_addcomment);
		titleLayout.setFirstButtonVisible(View.GONE);
		titleLayout.setTitle("发现");
		titleSecondButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(getActivity(),SelecteLectureCommentActivity.class);
				startActivity(intent);
			}
		});


		listView = (ListView) rootview.findViewById(R.id.swipe_target);

		mAdapter = new CommentAdapter(commentList,getContext());

		listView.setAdapter(mAdapter);

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

		return rootview;
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
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Comment comment= commentList.get(position);
		Lecture lecture1=comment.getLecture();
		Intent intent=new Intent(getActivity(),LectureDetailActivity.class);
		startActivity(intent);
	}

	@Override
	public void itemClick(View v) {
		int position;
		position = (Integer) v.getTag();

		Comment comment= commentList.get(position);

		switch (v.getId()){
			case R.id.comment_user_layout:
				Toast.makeText(getActivity(),"查看评论人资料",Toast.LENGTH_SHORT).show();
				break;
			case R.id.comment_lecture_layout:

				Intent intent=new Intent(getActivity(),LectureDetailActivity.class);
				startActivity(intent);
				break;
			case R.id.comment_like_layout:

				if(islike){
					Toast.makeText(getActivity(),"取消点赞",Toast.LENGTH_SHORT).show();
					comment.setCommentLikers(comment.getCommentLikers()-1);
					comment.setCommentLikersImage(R.drawable.ic_discovery_comment_like);
					mAdapter.notifyDataSetChanged();
					islike=false;
				}else{
					Toast.makeText(getActivity(),"点赞",Toast.LENGTH_SHORT).show();
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
	}
}

