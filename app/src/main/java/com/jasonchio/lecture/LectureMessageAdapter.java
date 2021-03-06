package com.jasonchio.lecture;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.jasonchio.lecture.greendao.LectureMessageDB;
import com.jasonchio.lecture.util.CircleImageView;
import com.orhanobut.logger.Logger;
import java.util.List;

import me.codeboy.android.aligntextview.AlignTextView;

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
 * Created by zhaoyaobang on 2018/6/26.
 */
public class LectureMessageAdapter extends RecyclerView.Adapter<LectureMessageAdapter.ViewHolder> implements View.OnClickListener, View.OnLongClickListener {
	List<LectureMessageDB> messageDBList;
	Context context;
	OnItemClickListener mItemClickListener;
	OnItemLongClickListener mItemLongClickListener;


	static class ViewHolder extends RecyclerView.ViewHolder{
		CircleImageView userheadImage;
		TextView userNameText;
		TextView msgLikersNumText;
		AlignTextView messageContentText;
		ImageView messageLikeImage;
		RelativeLayout relativeLayout;

		public ViewHolder(View itemView) {
			super(itemView);
			userheadImage=(CircleImageView)itemView.findViewById(R.id.lecture_message_userhead);
			userNameText=(TextView)itemView.findViewById(R.id.lecture_message_username);
			msgLikersNumText=(TextView)itemView.findViewById(R.id.lecture_message_liker_num);
			messageContentText=(AlignTextView) itemView.findViewById(R.id.lecture_message_content);
			messageLikeImage=(ImageView)itemView.findViewById(R.id.lecture_message_like_image);
			relativeLayout=(RelativeLayout)itemView.findViewById(R.id.lecture_message_layout);
		}
	}

	public LectureMessageAdapter(List<LectureMessageDB> lectureMessageDBList,Context context){
		messageDBList=lectureMessageDBList;
		this.context=context;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view= LayoutInflater.from(parent.getContext())
				.inflate(R.layout.lecture_message,parent,false);
		ViewHolder holder=new ViewHolder(view);
		view.setOnClickListener(this);
		view.setOnLongClickListener(this);
		return holder;
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		holder.relativeLayout.setTag(position);
		LectureMessageDB messageDB=messageDBList.get(position);
		holder.messageLikeImage.setTag(position);
		holder.messageLikeImage.setOnClickListener(this);
		if(messageDB.getMessageLikeorNot()==0){
			holder.messageLikeImage.setImageResource(R.drawable.ic_lecture_likes);
		}else{
			holder.messageLikeImage.setImageResource(R.drawable.ic_lecture_likes_selected);
		}
		//加载评论对应的用户头像
		if (messageDB.getUserHead() != null && messageDB.getUserHead() != "") {
			Glide.with(context).load(messageDB.getUserHead()).into(holder.userheadImage);
		} else {
			holder.userheadImage.setImageResource(R.drawable.ic_defult_userhead);
		}
		holder.messageContentText.setText(messageDB.getMessageContent());
		holder.msgLikersNumText.setText(String.valueOf(messageDB.getMessageLikersNum()));
		holder.userNameText.setText(messageDB.getUserName());
	}

	@Override
	public int getItemCount() {
		return messageDBList.size();
	}

	public interface OnItemClickListener{
		void onItemClick(View view,int position);
	}

	public interface OnItemLongClickListener{
		void onItemLongClick(View view,int position);
	}

	@Override
	public void onClick(View v) {
		if (mItemClickListener!=null){
			mItemClickListener.onItemClick(v,(Integer) v.getTag());
		}
	}

	@Override
	public boolean onLongClick(View v) {
		if (mItemLongClickListener!=null){
			mItemLongClickListener.onItemLongClick(v,(Integer) v.getTag());
		}
		return true;
	}
	public void setItemClickListener(OnItemClickListener itemClickListener) {
		mItemClickListener = itemClickListener;
	}

	public void setItemLongClickListener(OnItemLongClickListener itemLongClickListener) {
		mItemLongClickListener = itemLongClickListener;
	}

}
