package com.jasonchio.lecture;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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

public class CommentAdapter extends ArrayAdapter<Comment> {

	private int commentItemId;

	public CommentAdapter(Context context, int commentItemId, List<Comment> objects){
		super(context,commentItemId,objects);
		this.commentItemId= commentItemId;
	}
	@Override
	public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
		Comment comment=getItem(position);
		View view= LayoutInflater.from(getContext()).inflate(commentItemId,parent,false);

		Lecture lecture=comment.getLecture();

		CircleImageView userPhoto=(CircleImageView)view.findViewById(R.id.comment_photo_image);
		TextView commentText=(TextView)view.findViewById(R.id.comment_text);
		TextView userName=(TextView)view.findViewById(R.id.comment_user_name_text);
		TextView commentTime=(TextView)view.findViewById(R.id.comment_time_text);
		TextView commentLikers=(TextView)view.findViewById(R.id.comment_likers_text);

		TextView lectureTitle=(TextView)view.findViewById(R.id.lecture_title_text);
		ImageView lectureImage=(ImageView)view.findViewById(R.id.lecture_source_image);
		TextView lectureContent=(TextView)view.findViewById(R.id.lecture_content_text);
		TextView lectureTime=(TextView)view.findViewById(R.id.lecture_time_text);
		TextView lectureSource=(TextView)view.findViewById(R.id.lecture_source_text);
		TextView lectureLikers=(TextView)view.findViewById(R.id.lecture_likers_text);


		userPhoto.setImageResource(comment.getUserPhotoId());
//		userName.setText(comment.getUserName());
//		commentText.setText(comment.getCommentText());
//		commentTime.setText(comment.getCommentTime());
//		commentLikers.setText(comment.getCommentLikers());

		userName.setText("赵耀邦");
		commentText.setText("测试");
		commentLikers.setText("100");
		commentTime.setText("2018年");

		lectureTitle.setText(lecture.getLectureTitle());
		lectureImage.setImageResource(lecture.getLectureImageId());
		lectureContent.setText(lecture.getLectureContent());
		lectureTime.setText(lecture.getLectureTime());
		lectureSource.setText(lecture.getLectureSource());
		lectureLikers.setText(String.valueOf(lecture.getLectureLikers()));


		return view;
	}

}
