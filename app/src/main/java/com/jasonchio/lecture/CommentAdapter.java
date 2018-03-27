package com.jasonchio.lecture;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
 * Created by zhaoyaobang on 2018/3/10.
 */

//public class CommentAdapter extends ArrayAdapter<Comment> {
//
//	private int commentItemId;
//
//	public CommentAdapter(Context context, int commentItemId, List<Comment> objects){
//		super(context,commentItemId,objects);
//		this.commentItemId= commentItemId;
//	}
//	@Override
//	public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//		Comment comment=getItem(position);
//		View view= LayoutInflater.from(getContext()).inflate(commentItemId,parent,false);
//
//		Lecture lecture=comment.getLecture();
//
//		CircleImageView userPhoto=(CircleImageView)view.findViewById(R.id.comment_photo_image);
//		TextView commentText=(TextView)view.findViewById(R.id.comment_text);
//		TextView userName=(TextView)view.findViewById(R.id.comment_user_name_text);
//		TextView commentTime=(TextView)view.findViewById(R.id.comment_time_text);
//		TextView commentLikers=(TextView)view.findViewById(R.id.comment_likers_text);
//
//		TextView lectureTitle=(TextView)view.findViewById(R.id.lecture_title_text);
//		ImageView lectureImage=(ImageView)view.findViewById(R.id.lecture_source_image);
//		TextView lectureContent=(TextView)view.findViewById(R.id.lecture_content_text);
//		TextView lectureTime=(TextView)view.findViewById(R.id.lecture_time_text);
//		TextView lectureSource=(TextView)view.findViewById(R.id.lecture_source_text);
//		TextView lectureLikers=(TextView)view.findViewById(R.id.lecture_likers_text);
//
//
//		lectureTitle.setText(lecture.getLectureTitle());
//		lectureImage.setImageResource(lecture.getLectureImageId());
//		lectureContent.setText(lecture.getLectureContent());
//		lectureTime.setText(lecture.getLectureTime());
//		lectureSource.setText(lecture.getLectureSource());
//		lectureLikers.setText(String.valueOf(lecture.getLectureLikers()));
//
//		userPhoto.setImageResource(comment.getUserPhotoId());
//		commentText.setText(comment.getCommentText());
//		userName.setText(comment.getUserName());
//		commentTime.setText(comment.getCommentTime());
//		commentLikers.setText(String.valueOf(comment.getCommentLikers()));
//
//		return view;
//	}
//
//}

public class CommentAdapter extends BaseAdapter implements View.OnClickListener{

	private List<Comment> commentList;
	private Context context;
	private InnerItemOnclickListener listener;

	public CommentAdapter(List<Comment> list,Context context){
		this.commentList=list;
		this.context=context;
	}

	@Override
	public int getCount() {
		return commentList.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}


	@Override
	public View getView(int position, View view, ViewGroup parent) {
		final ViewHolder viewHolder;
		final Comment comment=commentList.get(position);
		Lecture lecture=comment.getLecture();

		if (view == null) {
			viewHolder = new ViewHolder();
			view = LayoutInflater.from(context).inflate(R.layout.comment_listitem,
					null);
			viewHolder.userLayout = (RelativeLayout) view.findViewById(R.id.comment_user_layout);
			viewHolder.commentLecutreLayout = (RelativeLayout) view.findViewById(R.id.comment_lecture_layout);
			viewHolder.commentLikeLayout = (RelativeLayout) view.findViewById(R.id.comment_like_layout);
			viewHolder.commentText=(TextView)view.findViewById(R.id.comment_text);

			viewHolder.userPhoto=(CircleImageView)view.findViewById(R.id.comment_photo_image);
			viewHolder.commentText=(TextView)view.findViewById(R.id.comment_text);
			viewHolder.userName=(TextView)view.findViewById(R.id.comment_user_name_text);
			viewHolder.commentTime=(TextView)view.findViewById(R.id.comment_time_text);
			viewHolder.commentLikers=(TextView)view.findViewById(R.id.comment_likers_text);

			viewHolder.lectureTitle=(TextView)view.findViewById(R.id.lecture_title_text);
			viewHolder.lectureImage=(ImageView)view.findViewById(R.id.lecture_source_image);
			viewHolder.lectureContent=(TextView)view.findViewById(R.id.lecture_content_text);
			viewHolder.lectureTime=(TextView)view.findViewById(R.id.lecture_time_text);
			viewHolder.lectureSource=(TextView)view.findViewById(R.id.lecture_source_text);
			viewHolder.lectureLikers=(TextView)view.findViewById(R.id.lecture_likers_text);
			viewHolder.commentLikersImage=(ImageView)view.findViewById(R.id.comment_like_image);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		viewHolder.userLayout.setOnClickListener(this);
		viewHolder.commentLecutreLayout.setOnClickListener(this);
		viewHolder.commentLikeLayout.setOnClickListener(this);
		viewHolder.commentLikeLayout.setOnClickListener(this);

		viewHolder.userLayout.setTag(position);
		viewHolder.commentLecutreLayout.setTag(position);
		viewHolder.commentLikeLayout.setTag(position);
		viewHolder.commentText.setTag(position);

		viewHolder.lectureTitle.setText(lecture.getLectureTitle());
		viewHolder.lectureImage.setImageResource(lecture.getLectureImageId());
		viewHolder.lectureContent.setText(lecture.getLectureContent());
		viewHolder.lectureTime.setText(lecture.getLectureTime());
		viewHolder.lectureSource.setText(lecture.getLectureSource());
		viewHolder.lectureLikers.setText(String.valueOf(lecture.getLectureLikers()));

		viewHolder.userPhoto.setImageResource(comment.getUserPhotoId());
		viewHolder.commentText.setText(comment.getCommentText());
		viewHolder.	userName.setText(comment.getUserName());
		viewHolder.	commentTime.setText(comment.getCommentTime());
		viewHolder.commentLikers.setText(String.valueOf(comment.getCommentLikers()));
		viewHolder.commentLikersImage.setImageResource(comment.getCommentLikersImage());
		return view;
	}

	public final static class ViewHolder {

		RelativeLayout userLayout;
		RelativeLayout commentLecutreLayout;
		RelativeLayout commentLikeLayout;

		CircleImageView userPhoto;
		TextView commentText;
		TextView userName;
		TextView commentTime;
		TextView commentLikers;
		ImageView commentLikersImage;

		TextView lectureTitle;
		ImageView lectureImage;
		TextView lectureContent;
		TextView lectureTime;
		TextView lectureSource;
		TextView lectureLikers;

	}

	interface InnerItemOnclickListener {
		void itemClick(View v);
	}

	public void setOnInnerItemOnClickListener(InnerItemOnclickListener listener){
		this.listener=listener;
	}

	@Override
	public void onClick(View v) {
		listener.itemClick(v);
	}

}
