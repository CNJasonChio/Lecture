package com.jasonchio.lecture;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jasonchio.lecture.greendao.DynamicsDB;
import com.jasonchio.lecture.util.CircleImageView;
import com.orhanobut.logger.Logger;

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
public class DynamicsAdapter extends RecyclerView.Adapter<DynamicsAdapter.ViewHolder>implements View.OnClickListener{

	List<DynamicsDB> dynamicsDBList;
	Context context;
	OnItemClickListener mItemClickListener;

	public DynamicsAdapter(List <DynamicsDB> dynamicsDBList, Context context) {
		this.dynamicsDBList = dynamicsDBList;
		this.context = context;
	}

	@Override
	public DynamicsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dynamics_listitem, parent, false);
		view.setOnClickListener(this);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(DynamicsAdapter.ViewHolder holder, int position) {
		DynamicsDB dynamicsDB=dynamicsDBList.get(position);
		if(dynamicsDB.getIsLikeorNot()==1){
			holder.likeImage.setImageResource(R.drawable.ic_dynamics_like_seletcted);
		}else{
			holder.likeImage.setImageResource(R.drawable.ic_dynamics_like);
		}
		if(dynamicsDB.getUserHead()!=null && !dynamicsDB.getUserHead().isEmpty()){
			Glide.with(context).load(dynamicsDB.getUserHead()).into(holder.userHead);
		}else{
			holder.userHead.setImageResource(R.drawable.ic_defult_userhead);
		}
		holder.userName.setText(dynamicsDB.getUserName());
		holder.dynamicsContent.setText(dynamicsDB.getDynamicsContent());
		holder.dynamicsTime.setText(dynamicsDB.getTime());
		holder.likesNum.setText(String.valueOf(dynamicsDB.getLikerNum()));
		holder.commentNum.setText(String.valueOf(dynamicsDB.getCommentNum()));
		holder.dynamicsLayout.setTag(position);
		holder.likeImage.setOnClickListener(this);
		holder.likeImage.setTag(position);
		holder.commentImage.setOnClickListener(this);
		holder.commentImage.setTag(position);
	}

	@Override
	public int getItemCount() {
		return dynamicsDBList.size();
	}

	@Override
	public void onClick(View v) {
		if (mItemClickListener!=null){
			mItemClickListener.onItemClick((Integer) v.getTag(),v);
		}
	}

	public class ViewHolder extends RecyclerView.ViewHolder {

		CircleImageView userHead;
		TextView userName;
		TextView dynamicsContent;
		TextView dynamicsTime;
		TextView likesNum;
		TextView commentNum;
		ImageView likeImage;
		ImageView commentImage;
		LinearLayout dynamicsLayout;
		public ViewHolder(View itemView) {
			super(itemView);
			userHead=(CircleImageView) itemView.findViewById(R.id.dynamics_userhead_image);
			userName=(TextView)itemView.findViewById(R.id.dynamics_username_text);
			dynamicsContent=(TextView)itemView.findViewById(R.id.dynamics_content_text);
			dynamicsTime=(TextView)itemView.findViewById(R.id.dynamics_time_text);
			likesNum=(TextView)itemView.findViewById(R.id.dynamics_like_num_text);
			commentNum=(TextView)itemView.findViewById(R.id.dynamics_comment_num_text);
			likeImage=(ImageView)itemView.findViewById(R.id.dynamics_comment_like_image);
			commentImage=(ImageView)itemView.findViewById(R.id.dynamics_comment_image);
			dynamicsLayout=(LinearLayout)itemView.findViewById(R.id.dynamics_layout);

		}
	}

	public interface OnItemClickListener{
		void onItemClick(int position,View view);
	}

	public void setItemClickListener(OnItemClickListener itemClickListener) {
		mItemClickListener = itemClickListener;
	}
}
