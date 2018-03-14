package com.jasonchio.lecture;

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
 * Created by zhaoyaobang on 2018/3/13.
 */

public class Library {

	private String libraryName;

	private String libraryOriginal;

	private String libraryContent;

	private int libraryImageId;

	public Library(String libraryName, int libraryImageId, String libraryContent, String libraryOriginal) {
		this.libraryName = libraryName;
		this.libraryOriginal = libraryOriginal;
		this.libraryContent = libraryContent;
		this.libraryImageId = libraryImageId;
	}

	public Library(String libraryName) {
		this.libraryName = libraryName;
	}

	public String getLibraryName() {
		return libraryName;
	}

	public void setLibraryName(String libraryName) {
		this.libraryName = libraryName;
	}

	public String getLibraryOriginal() {
		return libraryOriginal;
	}

	public void setLibraryOriginal(String libraryOriginal) {
		this.libraryOriginal = libraryOriginal;
	}

	public String getLibraryContent() {
		return libraryContent;
	}

	public void setLibraryContent(String libraryContent) {
		this.libraryContent = libraryContent;
	}

	public int getLibraryImageId() {
		return libraryImageId;
	}

	public void setLibraryImageId(int libraryImageId) {
		this.libraryImageId = libraryImageId;
	}
}
