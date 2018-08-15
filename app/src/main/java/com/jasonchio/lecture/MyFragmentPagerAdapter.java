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
 * Created by zhaoyaobang on 2018/3/7.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.jasonchio.lecture.FocuseFragment;
import com.jasonchio.lecture.RecommentFragment;
import com.jasonchio.lecture.NearFragment;

/**
 * Created by Carson_Ho on 16/7/22.
 */
public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

	private String[] mTitles = new String[]{"推荐", "关注", "附近"};

	public MyFragmentPagerAdapter(FragmentManager fragmentManager) {
		super(fragmentManager);
	}

	@Override
	public Fragment getItem(int position) {
		if (position == 1) {
			return new RecommentFragment();
		} else if (position == 2) {
			return new FocuseFragment();
		}else if (position==3){
			return new NearFragment();
		}
		return new RecommentFragment();
	}

	@Override
	public int getCount() {
		return mTitles.length;
	}

	//ViewPager与TabLayout绑定后，这里获取到PageTitle就是Tab的Text
	@Override
	public CharSequence getPageTitle(int position) {
		return mTitles[position];
	}
}
