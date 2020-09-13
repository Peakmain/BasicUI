package com.peakmain.basicui;

import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;

import com.peakmain.basicui.base.BaseActivity;
import com.peakmain.basicui.fragment.HomeFragment;
import com.peakmain.basicui.fragment.MineFragment;
import com.peakmain.basicui.fragment.UtilsFragment;


public class MainActivity extends BaseActivity {
    //View
    BottomNavigationView mBottomNavigation;

    //底部切换的tab常量
    private static final int FRAGMENT_HOME = 0;
    private static final int FRAGMENT_ME = 1;
    private static final int FRAGMENT_UTILS = 2;
    //fragments
    private HomeFragment mHomeFragment;
    private UtilsFragment mUtilsFragment;
    private MineFragment mMineFragment;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        mBottomNavigation = findViewById(R.id.bottom_navigation);
    }

    @Override
    protected void initData() {
        showFragment(FRAGMENT_HOME);
        mBottomNavigation.setOnNavigationItemSelectedListener(this::onOptionsItemSelected);
        mBottomNavigation.setSelectedItemId(R.id.menu_home);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.menu_home:
                showFragment(FRAGMENT_HOME);
                return true;
            case R.id.menu_utils:
                showFragment(FRAGMENT_UTILS);
                return true;
            case R.id.menu_me:
                showFragment(FRAGMENT_ME);
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showFragment(int index) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        hintFragment(ft);
        switch (index) {
            case FRAGMENT_HOME:
                /**
                 * 如果Fragment为空，就新建一个实例
                 * 如果不为空，就将它从栈中显示出来
                 */
                if (mHomeFragment == null) {
                    mHomeFragment = new HomeFragment();
                    ft.add(R.id.container, mHomeFragment, HomeFragment.class.getName());
                } else {
                    ft.show(mHomeFragment);
                }
                break;
            case FRAGMENT_ME:
                if (mMineFragment == null) {
                    mMineFragment = new MineFragment();
                    ft.add(R.id.container, mMineFragment, MineFragment.class.getName());
                } else {
                    ft.show(mMineFragment);
                }
                break;
            case FRAGMENT_UTILS:
                if (mUtilsFragment == null) {
                    mUtilsFragment = new UtilsFragment();
                    ft.add(R.id.container, mUtilsFragment, UtilsFragment.class.getName());
                } else {
                    ft.show(mUtilsFragment);
                }
                break;
            default:
                break;
        }
        ft.commit();
    }

    /**
     * 隐藏fragment
     */
    private void hintFragment(FragmentTransaction ft) {
        // 如果不为空，就先隐藏起来
        if (mHomeFragment != null) {
            ft.hide(mHomeFragment);
        }
        if (mMineFragment != null) {
            ft.hide(mMineFragment);
        }
        if(mUtilsFragment!=null){
            ft.hide(mUtilsFragment);
        }
    }

}
