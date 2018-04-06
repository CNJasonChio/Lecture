package com.jasonchio.lecture.database;

import org.litepal.crud.DataSupport;

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

public class LibraryDB extends DataSupport {

	int libraryID;           //图书馆id

	String libraryName;         //图书馆名字

	String libraryContent;      //图书馆正文

	String libraryUrl;          //图书馆源地址的URL

	String libraryImageUrl;     //图书馆照片的URL

	double libraryLatitude;     //图书馆纬度

	double libraryLongitude;    //图书馆经度

	public LibraryDB() {
	}

	public LibraryDB(String libraryName) {
		this.libraryName = libraryName;
	}

	public int getLibraryID() {
		return libraryID;
	}

	public void setLibraryID(int libraryID) {
		this.libraryID = libraryID;
	}

	public String getLibraryName() {
		return libraryName;
	}

	public void setLibraryName(String libraryName) {
		this.libraryName = libraryName;
	}

	public String getLibraryContent() {
		return libraryContent;
	}

	public void setLibraryContent(String libraryContent) {
		this.libraryContent = libraryContent;
	}

	public String getLibraryUrl() {
		return libraryUrl;
	}

	public void setLibraryUrl(String libraryUrl) {
		this.libraryUrl = libraryUrl;
	}

	public String getLibraryImageUrl() {
		return libraryImageUrl;
	}

	public void setLibraryImageUrl(String libraryImageUrl) {
		this.libraryImageUrl = libraryImageUrl;
	}

	public double getLibraryLatitude() {
		return libraryLatitude;
	}

	public void setLibraryLatitude(double libraryLatitude) {
		this.libraryLatitude = libraryLatitude;
	}

	public double getLibraryLongitude() {
		return libraryLongitude;
	}

	public void setLibraryLongitude(double libraryLongitude) {
		this.libraryLongitude = libraryLongitude;
	}

}
