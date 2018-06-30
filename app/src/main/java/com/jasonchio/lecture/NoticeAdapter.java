package com.jasonchio.lecture;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jasonchio.lecture.greendao.NoticeItemDB;
import com.jasonchio.lecture.util.CircleImageView;

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
 * Created by zhaoyaobang on 2018/6/30.
 */
public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.ViewHolder>{
	List<NoticeItemDB> noticeList;
	Context context;

	public NoticeAdapter(List <NoticeItemDB> noticeList, Context context) {
		this.noticeList = noticeList;
		this.context = context;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.notice_item,parent,false);
		ViewHolder holder=new ViewHolder(view);
		return holder;
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		NoticeItemDB noticeItemDB=noticeList.get(position);
		holder.noticeTime.setText(noticeItemDB.getNoticeTime());
		if(noticeItemDB.getNoticeType()=="dynamics"){
			holder.noticeContent.setText("回复了你的动态："+noticeItemDB.getNoticeContent());
		}else if (noticeItemDB.getNoticeType()=="comment"){
			holder.noticeContent.setText("回复了你的评论："+noticeItemDB.getNoticeContent());
		}else{
			holder.noticeContent.setText("回复了你的评论回复："+noticeItemDB.getNoticeContent());
		}
		holder.userName.setText(noticeItemDB.getUserName());
		if(noticeItemDB.getUserHead()!=null && !noticeItemDB.getUserHead().isEmpty()){
			Glide.with(context).load(noticeItemDB.getUserHead()).into(holder.userHead);
		}else{
			holder.userHead.setImageResource(R.drawable.ic_defult_userhead);
		}
	}

	@Override
	public int getItemCount() {
		return noticeList.size();
	}

	static class ViewHolder extends RecyclerView.ViewHolder{
		TextView userName;
		TextView noticeContent;
		TextView noticeTime;
		CircleImageView userHead;
		public ViewHolder(View view){
			super(view);
			userHead=(CircleImageView)view.findViewById(R.id.notice_userhead_image);
			userName=(TextView)view.findViewById(R.id.notice_user_name);
			noticeContent =(TextView)view.findViewById(R.id.notice_content);
			noticeTime=(TextView)view.findViewById(R.id.notice_time);
		}
	}
}
