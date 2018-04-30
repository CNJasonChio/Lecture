package com.jasonchio.lecture;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jasonchio.lecture.greendao.CommentDB;
import com.jasonchio.lecture.greendao.CommentDBDao;
import com.jasonchio.lecture.greendao.LectureDB;
import com.jasonchio.lecture.greendao.LectureDBDao;
import com.jasonchio.lecture.greendao.UserDBDao;
import com.jasonchio.lecture.util.CircleImageView;
import com.jasonchio.lecture.util.ConstantClass;
import com.jasonchio.lecture.util.Utility;

import java.util.List;
import java.util.logging.Logger;

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


public class CommentAdapter extends BaseAdapter implements View.OnClickListener{

	private List<CommentDB> commentList;
	private List<LectureDB> lectureList;
	private Context context;
	private InnerItemOnclickListener listener;
	private UserDBDao mUserDao;
	private CommentDBDao mCommentDao;
	private ListView listView;

	public CommentAdapter(ListView listView,List<CommentDB> commentDBList,List<LectureDB> lectureDBList,UserDBDao mUserDao,CommentDBDao mCommentDao,Context context){
		this.listView=listView;
		this.commentList=commentDBList;
		this.context=context;
		this.lectureList=lectureDBList;
		this.mUserDao=mUserDao;
		this.mCommentDao=mCommentDao;
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
		final CommentDB comment=commentList.get(position);
		final LectureDB lecture=lectureList.get(position);

		if (view == null) {
			viewHolder = new ViewHolder();
			view = LayoutInflater.from(context).inflate(R.layout.comment_listitem, null);
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
			viewHolder.lectureWantedImage=(ImageView)view.findViewById(R.id.lecture_wanted_image);
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

		if(lecture.getLectureImage()!=null){
			Glide.with(context).load(lecture.getLectureImage()).into(viewHolder.lectureImage);
		}
		viewHolder.lectureContent.setText(lecture.getLectureContent());
		viewHolder.lectureTime.setText(lecture.getLectureTime());
		viewHolder.lectureSource.setText(lecture.getLecutreSource());
		viewHolder.lectureLikers.setText(String.valueOf(lecture.getLecutreLikers()));

		viewHolder.commentText.setText(comment.getCommentContent());
		viewHolder.commentTime.setText(comment.getCommentTime());
		viewHolder.commentLikers.setText(String.valueOf(comment.getCommentLikers()));

		viewHolder.userName.setText(comment.getCommentuserName());
		viewHolder.userName.setText(comment.getCommentuserName());
		if(lecture.getIsWanted()==0){
			viewHolder.lectureWantedImage.setImageResource(R.drawable.ic_lecture_likes);
		}else {
			viewHolder.lectureWantedImage.setImageResource(R.drawable.ic_myinfo_mywanted);
		}
		if(comment.getIsLike()==1){
			viewHolder.commentLikersImage.setImageResource(R.drawable.ic_discovery_comment_like_selected);
		}else{
			viewHolder.commentLikersImage.setImageResource(R.drawable.ic_discovery_comment_like);
		}

		com.orhanobut.logger.Logger.d(comment.getUserHead());

		if(comment.getUserHead()!=null || comment.getUserHead() != ""){
			Glide.with(context).load(comment.getUserHead()).into(viewHolder.userPhoto);
		}
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
		ImageView lectureWantedImage;
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

	public void changeCommentLike(int position, int islike){

		Message msg=Message.obtain();
		msg.arg1=position;
		if(islike==0){
			//viewHolder.commentLikersImage.setImageResource(R.drawable.ic_discovery_comment_like);
			msg.arg2=R.drawable.ic_discovery_comment_like;
		}else{
			//viewHolder.commentLikersImage.setImageResource(R.drawable.ic_discovery_comment_like_selected);
			msg.arg2=R.drawable.ic_discovery_comment_like_selected;
		}

		CommentDB comment=commentList.get(position);
		if(islike==1){
			comment.setCommentLikers(comment.getCommentLikers()+1);
			mCommentDao.update(comment);
		}else {
			comment.setCommentLikers(comment.getCommentLikers()-1);
			mCommentDao.update(comment);
		}
		commentList.set(position,comment);
		handler.sendMessage(msg);
	}

	private Handler handler = new Handler()
	{
		public void handleMessage(android.os.Message msg)
		{
			updateItem(msg.arg1,msg.arg2);
		}
	};

	/**、
	 * 刷新指定item
	 *
	 * @param index item在listview中的位置
	 */
	private void updateItem(int index,int drawable)
	{
		if (listView == null)
		{
			return;
		}

		// 获取当前可以看到的item位置
		int visiblePosition = listView.getFirstVisiblePosition();

		// 如添加headerview后 firstview就是hearderview
		// 所有索引+1 取第一个view
		// View view = listview.getChildAt(index - visiblePosition + 1);
		// 获取点击的view

		View view = listView.getChildAt(index - visiblePosition);

		ImageView likeImage=view.findViewById(R.id.comment_like_image);

		likeImage.setImageResource(drawable);
	}

}
