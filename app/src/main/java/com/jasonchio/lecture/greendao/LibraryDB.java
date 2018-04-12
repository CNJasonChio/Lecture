package com.jasonchio.lecture.greendao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

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
 * Created by zhaoyaobang on 2018/3/23.
 */

@Entity
public class LibraryDB{

	@Id
	long libraryID;           //图书馆id

	String libraryName;         //图书馆名字

	String libraryContent;      //图书馆正文

	String libraryUrl;          //图书馆源地址的URL

	String libraryImageUrl;     //图书馆照片的URL

	double libraryLatitude;     //图书馆纬度

	double libraryLongitude;    //图书馆经度

	int isFocused;

	@Generated(hash = 923803696)
	public LibraryDB(long libraryID, String libraryName, String libraryContent,
			String libraryUrl, String libraryImageUrl, double libraryLatitude,
			double libraryLongitude, int isFocused) {
		this.libraryID = libraryID;
		this.libraryName = libraryName;
		this.libraryContent = libraryContent;
		this.libraryUrl = libraryUrl;
		this.libraryImageUrl = libraryImageUrl;
		this.libraryLatitude = libraryLatitude;
		this.libraryLongitude = libraryLongitude;
		this.isFocused = isFocused;
	}

	@Generated(hash = 1958976301)
	public LibraryDB() {
	}

	public long getLibraryID() {
		return this.libraryID;
	}

	public void setLibraryID(long libraryID) {
		this.libraryID = libraryID;
	}

	public String getLibraryName() {
		return this.libraryName;
	}

	public void setLibraryName(String libraryName) {
		this.libraryName = libraryName;
	}

	public String getLibraryContent() {
		return this.libraryContent;
	}

	public void setLibraryContent(String libraryContent) {
		this.libraryContent = libraryContent;
	}

	public String getLibraryUrl() {
		return this.libraryUrl;
	}

	public void setLibraryUrl(String libraryUrl) {
		this.libraryUrl = libraryUrl;
	}

	public String getLibraryImageUrl() {
		return this.libraryImageUrl;
	}

	public void setLibraryImageUrl(String libraryImageUrl) {
		this.libraryImageUrl = libraryImageUrl;
	}

	public double getLibraryLatitude() {
		return this.libraryLatitude;
	}

	public void setLibraryLatitude(double libraryLatitude) {
		this.libraryLatitude = libraryLatitude;
	}

	public double getLibraryLongitude() {
		return this.libraryLongitude;
	}

	public void setLibraryLongitude(double libraryLongitude) {
		this.libraryLongitude = libraryLongitude;
	}

	public int getIsFocused() {
		return this.isFocused;
	}

	public void setIsFocused(int isFocused) {
		this.isFocused = isFocused;
	}
}
