package com.jasonchio.lecture;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jasonchio.lecture.gson.DynamicsResult;
import com.jasonchio.lecture.util.CircleImageView;
import com.jasonchio.lecture.util.TimeUtil;
import com.orhanobut.logger.Logger;

import java.sql.Time;
import java.util.ArrayList;
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
 * Created by zhaoyaobang on 2018/6/30.
 */
public class CommentExpandAdapter extends BaseExpandableListAdapter {
	private List <DynamicsResult.DataBean> commentBeanList;
	private Context context;

	public CommentExpandAdapter(Context context, List <DynamicsResult.DataBean> commentBeanList) {
		this.context = context;
		this.commentBeanList = commentBeanList;
	}

	@Override
	public int getGroupCount() {
		return commentBeanList.size();
	}

	@Override
	public int getChildrenCount(int i) {
		if (commentBeanList.get(i).getReplyList() == null) {
			return 0;
		} else {
			return commentBeanList.get(i).getReplyList().size() > 0 ? commentBeanList.get(i).getReplyList().size() : 0;
		}
	}

	@Override
	public Object getGroup(int i) {
		return commentBeanList.get(i);
	}

	@Override
	public Object getChild(int i, int i1) {
		return commentBeanList.get(i).getReplyList().get(i1);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return getCombinedChildId(groupPosition, childPosition);
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public View getGroupView(final int groupPosition, boolean isExpand, View convertView, ViewGroup viewGroup) {
		final GroupHolder groupHolder;

		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.comment_item_layout, viewGroup, false);
			groupHolder = new GroupHolder(convertView);
			convertView.setTag(groupHolder);
		} else {
			groupHolder = (GroupHolder) convertView.getTag();
		}
		DynamicsResult.DataBean comment=commentBeanList.get(groupPosition);
		if(comment.getUserLogo()!=null&&!comment.getUserLogo().isEmpty()){
			Glide.with(context).load(comment.getUserLogo()).into(groupHolder.commentUserHead);
		}else{
			Glide.with(context).load(R.drawable.ic_defult_userhead).into(groupHolder.commentUserHead);
		}
		groupHolder.commentUserName.setText(commentBeanList.get(groupPosition).getNickName());
		if (commentBeanList.get(groupPosition).getCreateDate() != null) {
			groupHolder.commentTime.setText(TimeUtil.getTimeFormatText(commentBeanList.get(groupPosition).getCreateDate()));
		}
		groupHolder.commentContent.setText(commentBeanList.get(groupPosition).getContent());

		groupHolder.commentLike.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (commentBeanList.get(groupPosition).getComment_user_like() == 1) {
					commentBeanList.get(groupPosition).setComment_user_like(0);
					groupHolder.commentLike.setColorFilter(Color.parseColor("#aaaaaa"));
				} else {
					commentBeanList.get(groupPosition).setComment_user_like(1);
					groupHolder.commentLike.setColorFilter(Color.parseColor("#FF9D00"));
				}
			}
		});
		return convertView;
	}

	@Override
	public View getChildView(final int groupPosition, int childPosition, boolean b, View convertView, ViewGroup viewGroup) {
		final ChildHolder childHolder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.comment_reply_item_layout, viewGroup, false);
			childHolder = new ChildHolder(convertView);
			convertView.setTag(childHolder);
		} else {
			childHolder = (ChildHolder) convertView.getTag();
		}

		String replyUser = commentBeanList.get(groupPosition).getReplyList().get(childPosition).getNickName();
		if (!TextUtils.isEmpty(replyUser)) {
			childHolder.replyUserName.setText(replyUser + ":");
		} else {
			childHolder.replyUserName.setText("无名" + ":");
		}
		childHolder.replyContent.setText(commentBeanList.get(groupPosition).getReplyList().get(childPosition).getContent());

		return convertView;
	}

	@Override
	public boolean isChildSelectable(int i, int i1) {
		return true;
	}

	private class GroupHolder {
		private CircleImageView commentUserHead;
		private TextView commentUserName,  commentTime;
		private AlignTextView commentContent;
		private ImageView commentLike;

		public GroupHolder(View view) {
			commentUserHead = (CircleImageView) view.findViewById(R.id.comment_userhead_image);
			commentContent = (AlignTextView) view.findViewById(R.id.comment_content);
			commentUserName = (TextView) view.findViewById(R.id.comment_userName_text);
			commentTime = (TextView) view.findViewById(R.id.comment_time_text);
			commentLike = (ImageView) view.findViewById(R.id.comment_like_image);
		}
	}

	private class ChildHolder {
		private TextView replyUserName, replyContent;

		public ChildHolder(View view) {
			replyUserName = (TextView) view.findViewById(R.id.reply_item_user);
			replyContent = (TextView) view.findViewById(R.id.reply_item_content);
		}
	}


	/**
	 * by moos on 2018/04/20
	 * func:评论成功后插入一条数据
	 *
	 * @param commentDetailBean 新的评论数据
	 */
	public void addTheCommentData(DynamicsResult.DataBean commentDetailBean) {
		if (commentDetailBean != null) {
			commentBeanList.add(commentDetailBean);
			notifyDataSetChanged();
		} else {
			throw new IllegalArgumentException("评论数据为空!");
		}

	}

	/**
	 * func:回复成功后插入一条数据
	 *
	 * @param replyDetailBean 新的回复数据
	 */
	public void addTheReplyData(DynamicsResult.DataBean.ReplyListBean replyDetailBean, int groupPosition) {
		if (replyDetailBean != null) {
			commentBeanList.get(groupPosition).setReplyTotal(commentBeanList.get(groupPosition).getReplyTotal() + 1);
			if (commentBeanList.get(groupPosition).getReplyList() != null) {
				commentBeanList.get(groupPosition).getReplyList().add(replyDetailBean);
			} else {
				List <DynamicsResult.DataBean.ReplyListBean> replyList = new ArrayList <>();
				replyList.add(replyDetailBean);
				commentBeanList.get(groupPosition).setReplyList(replyList);
			}
			notifyDataSetChanged();
		} else {
			throw new IllegalArgumentException("回复数据为空!");
		}

	}

	/**
	 * func:添加和展示所有回复
	 *
	 * @param replyBeanList 所有回复数据
	 * @param groupPosition 当前的评论
	 */
	private void addReplyList(List <DynamicsResult.DataBean.ReplyListBean> replyBeanList, int groupPosition) {
		if (commentBeanList.get(groupPosition).getReplyList() != null) {
			commentBeanList.get(groupPosition).getReplyList().clear();
			commentBeanList.get(groupPosition).getReplyList().addAll(replyBeanList);
		} else {
			commentBeanList.get(groupPosition).setReplyList(replyBeanList);
		}
		notifyDataSetChanged();
	}

}
