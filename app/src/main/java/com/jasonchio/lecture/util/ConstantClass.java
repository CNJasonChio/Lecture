package com.jasonchio.lecture.util;

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
 * Created by zhaoyaobang on 2018/3/31.
 */
public class ConstantClass {

	//当前登录用户的ID
	public static long userOnline=0;

	//服务器地址
	public static final String ADDRESS="119.29.93.31";

	//动态
	public static int TYPE_DYNAMICS=2;
	//留言
	public static int TYPE_MESSAGE=1;
	//评论
	public static int TYPE_COMMENT=3;
	//回复
	public static int TYPE_REPLY=4;
	//请求更多新的讲座(动态)
	public static int REQUEST_NEW=0;
	//请求加载更多讲座（动态）
	public static int REQUEST_OLD=1;
	//第一次使用软件，请求服务器放回较新的讲座（动态）
	public static int REQUEST_FIRST=2;
	//请求单独的一个讲座（动态）
	public static int REQUEST_SINGLE=3;


	//注册命令
	public static final int SIGNIN_COM=1;
	//找回密码命令
	public static final int FINDPWD_COM=2;
	//登录命令
	public static final int LOGIN_COM=3;
	//请求讲座信息命令
	public static final int LECTURE_REQUEST_COM=4;
	/*//请求评论信息
	public static final int COMMENT_REQUEST_COM=5;*/
	//请求动态信息
	public static final int DYNAMICS_REQUEST_COM=5;
	//请求我的信息
	public static final int MYINFO_REQUEST_COM=6;
	//更改我的信息
	public static final int CHANGE_MYINFO_REQUEST_COM=7;
	//查找讲座
	public static final int SEARCH_LECTURE_REQUEST_COM=8;
	//请求"我的想看"
	public static final int MYWANTED_LECTURE_REQUEST_COM=9;
	//请求"我的关注"
	public static final int MYFOCUSE_LIBRARY_REQUEST_COM=10;
	//请求"我的留言"
	public static final int MYMESSAGE_REQUEST_COM=11;
	//添加或取消想看
	public static final int ADD_CANCEL_WANTED_REQUEST_COM=12;
	//添加或取消关注
	public static final int ADD_CANCEL_FOCUSE_REQUEST_COM=13;
	//请求讲座来源的详细信息
	public static final int LIBRARY_REQUEST_COM=14;
	//发送位置信息
	public static final int SEND_POSITION_COM=15;
	/*//添加评论
	public static final int ADD_COMMENT_COM=16;*/
	//添加讲座留言
	public static final int LEAVE_MESSAGE_COM=16;
	/*//给评论点赞或取消点赞
	public static final int LIKE_COMMENT_COM=17;*/
	//点赞或者取消点赞  1-留言、2-动态、3-评论
	public static final int LIKEORNOT_CHAGNE_COM=17;
	//修改用户头像
	public static final int CHANGE_HEAD_COM=18;
	//请求推荐讲座的顺序
	public static final int RECOMMENT_COM=19;
	//检查更新
	public static final int UPDATE_COM=20;
	//添加动态
	public static final int ADD_DYNAMICS_COM=21;
	//添加评论信息
	public static final int ADD_COMMENT_COM=22;
	//添加回复信息
	public static final int ADD_REPLY_COM=23;
	//删除、留言、动态、评论信息
	public static final int DELETE_COM=24;
	//查询一个讲座的留言信息
	public static final int MESSAGE_REQUEST_COM=25;
	//请求某一条动态的所有评论及回复
	public static final int COMMENT_REPLY_REQUEST_COM=26;
	//请求附近的讲座
	public static final int NEAR_LECTURE_EQUEST_COM=27;
	//请求"我的动态"
	public static final int MYDYNAMICS_REQUEST_COM=28;
}
