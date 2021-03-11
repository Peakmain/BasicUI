package com.peakmain.basicui.activity.utils;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.peakmain.basicui.R;
import com.peakmain.basicui.adapter.BaseRecyclerStringAdapter;
import com.peakmain.basicui.base.BaseActivity;
import com.peakmain.basicui.bean.Person;
import com.peakmain.ui.navigationbar.DefaultNavigationBar;
import com.peakmain.ui.utils.database.DaoSupportFactory;
import com.peakmain.ui.utils.database.IDaoSupport;

import java.util.ArrayList;
import java.util.List;


public class DataBaseActivity extends BaseActivity {
    private RecyclerView mRecyclerView;
    //权限回调ØØ
    private static final int RC_WRITE_READ = 0x0100;
    private IDaoSupport<Person> mDao;
    private BaseRecyclerStringAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_data_base;
    }

    @Override
    protected void initView() {
        mRecyclerView = findViewById(R.id.recycler_view);
        new DefaultNavigationBar.Builder(this, findViewById(R.id.view_root))
                .hideLeftText()
                .hideRightView()
                .setTitleText("数据库")
                .setToolbarBackgroundColor(R.color.colorAccent)
                .create();
    }

    @Override
    protected void initData() {
        //todo 记得打开权限
        mDao = DaoSupportFactory.getInstance().getDao(Person.class);
        List<Person> list = mDao.querySupport().queryAll();
        if (list == null || list.size() == 0)
            setData(mDao);
        List<String> data = new ArrayList<>();
        for (int i = list.size() - 1; i >= 0; i--) {
            Person person = list.get(i);
            data.add("名字是:" + person.getName() + ",年龄是:" + person.getAge());
        }
        mAdapter = new BaseRecyclerStringAdapter(this, data);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void setData(IDaoSupport<Person> dao) {
        List<Person> persons = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            persons.add(new Person("测试", 100 + i));
        }
        //批量插入
        dao.insert(persons);
    }

    public void insertData(View view) {
        mDao.insert(new Person("新的数据啊", 119110));
        List<Person> list = mDao.querySupport().queryAll();
        List<String> data = new ArrayList<>();
        for (int i = list.size() - 1; i >= 0; i--) {
            Person person = list.get(i);
            data.add("名字是:" + person.getName() + ",年龄是:" + person.getAge());
        }
        mAdapter.setData(data);
    }

    public void queryData(View view) {
        List<Person> list = mDao.querySupport().selection("name like ?").selectionArgs(new String[]{"新%"}).query();
        for (Person person : list) {
            LogUtils.e(person.getName() + "," + person.getAge());
        }
    }

    public void deleteData(View view) {
        int delete = mDao.delete("age = ?", new String[]{"109"});
        LogUtils.e("删除的：" + delete);
        List<String> data = new ArrayList<>();
        List<Person> list = mDao.querySupport().queryAll();
        for (int i = list.size() - 1; i >= 0; i--) {
            Person person = list.get(i);
            data.add("名字是:" + person.getName() + ",年龄是:" + person.getAge());
        }
        mAdapter.setData(data);

    }

    public void updateData(View view) {
        mDao.update(new Person("peakmain", 18), "age =?", new String[]{"108"});
        List<Person> list = mDao.querySupport().queryAll();
        List<String> data = new ArrayList<>();
        for (int i = list.size() - 1; i >= 0; i--) {
            Person person = list.get(i);
            data.add("名字是:" + person.getName() + ",年龄是:" + person.getAge());
        }
        mAdapter.setData(data);
    }

}
