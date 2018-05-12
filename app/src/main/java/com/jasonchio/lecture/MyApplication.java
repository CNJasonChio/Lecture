package com.jasonchio.lecture;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.jasonchio.lecture.greendao.DaoMaster;
import com.jasonchio.lecture.greendao.DaoSession;

import org.greenrobot.greendao.database.Database;

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
 * Created by zhaoyaobang on 2018/4/8.
 */
public class MyApplication extends Application{

	public static final boolean ENCRYPTED = false;//是否创建加密数据库
	private DaoSession daoSession;

	@Override
	public void onCreate() {
		super.onCreate();

		//创建数据库lecture.db"
		DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "lecture", null);
		//获取可写数据库
		SQLiteDatabase db = helper.getWritableDatabase();
		//获取数据库对象
		DaoMaster daoMaster = new DaoMaster(db);
		//获取Dao对象管理者
		daoSession = daoMaster.newSession();
	}

	public DaoSession getDaoSession() {
		return daoSession;
	}
}
